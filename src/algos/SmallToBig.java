package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import test.Datacenter;
import test.Pool;
import test.Server;

public class SmallToBig {

	public static void sortServersDesc(Datacenter dc) {
		int M = dc.allServers.size();
		List<Server> sortedServers = new ArrayList<Server>(M);
		
		for(Server server : dc.allServers){
			sortedServers.add(server);
		}
		
		Collections.sort(sortedServers, new Comparator<Server>() {
			public int compare(Server s1, Server s2) {
				if(s1.c < s2.c) return 1;
				if(s1.c > s2.c) return -1;
				return 0;
			}
		});
		
		int i = 0;
		for(Server server : sortedServers){
			placeServer(dc, server, i);
			i++;
			if (i == dc.R) {
				i = 0;
			};
		}
	}
	
	
	public static void placeServer(Datacenter dc, Server server, int row) {
		int i = row;
		while(i < dc.R) {
			int j = 0;
			while(j < dc.S) {
				if (canBePlaced(dc, i, j, server.z)) { //we place the server here
					
					server.ar = i;
					server.as = j;
					Random r = new Random();
					
					
					int color = r.nextInt(dc.allPools.size());
					Pool targetPool = dc.allPools.get(color);
					server.pool = targetPool;
					targetPool.add(server);
					
					for (int k = j; k < j + server.z; k++){
						dc.available[i][k] = false;
					}
					return;
				}
				j++;
			};
			i++;
		}
	}
	
	public static boolean canBePlaced(Datacenter dc, int i, int j, int z) {
		if (i < 0 || i >= dc.R) {
			return false;
		} else if (j < 0 || j + z > dc.S) {
			return false;
		} else {
			for(int k = j; k < j+z; k++){
				if (!dc.available[i][k]) {
					return false;
				}
			}
			return true;	
		}
	};
	
	public static void main(String[] args) throws IOException {
		
		Datacenter datacenter = new Datacenter(new File("data/dc.in"));
		sortServersDesc(datacenter);
		System.out.println(datacenter.valid());
		System.out.println(datacenter.getScore());
		
		//System.out.println(datacenter.R);
		//System.out.println(datacenter.allServers.get(0));
		
		datacenter.saveSolutionToFile(new File("data/solution1.txt"));
	}
}