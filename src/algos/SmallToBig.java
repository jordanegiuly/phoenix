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
				float ratio1 = s1.c / s1.z;
				float ratio2 = s2.c / s2.z;
				
				//option 2: ratio
				if(ratio1 < ratio2) return 1;
				if(ratio1 > ratio2) return -1;
				
				//option 1: capacity
				//if(s1.c < s2.c) return 1;
				//if(s1.c > s2.c) return -1;
				//if(s1.z > s2.z) return 1;
				//if(s1.z < s2.z) return -1;
				return 0;
		
			}
		});
		
		int i = 0;
		for(Server server : sortedServers){
			i = placeServer(dc, server, i) + 1;
			if (i == dc.R) {
				i = 0;
			};
		}
		
		Greedy.allocatePools2(dc);
		
		for(Server server : sortedServers){
			System.out.println(server);
		}
	}
	
	
	public static int placeServer(Datacenter dc, Server server, int row) {
		int tries = 0;
		int i = row;
		while(tries < dc.R) {
			int j = 0;
			while(j < dc.S) {
				if (canBePlaced(dc, i, j, server.z)) { //we place the server here
					
					server.ar = i;
					server.as = j;
					
					for (int k = j; k < j + server.z; k++){
						dc.available[i][k] = false;
					}
					return i;
				}
				j++;
			};
			tries++;
			i++;
			if (i == dc.R) {
				i = 0;
			}
		}
		return 0;
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
		
		datacenter.saveSolutionToFile(new File("data/solution3.txt"));
	}
}