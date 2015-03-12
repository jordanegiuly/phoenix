package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import test.Datacenter;
import test.Server;
import test.Pool;

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
			//System.out.println(server);
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
		return -1;
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
	
	public static void revertWorst(Datacenter dc) {
		int i = 0;
		while(i<300){
			int score = dc.getScore();
			System.out.println("score " + score);
			Pool worstPool = dc.worstPool();
			int minCapat = worstPool.guaranteedCapacity(dc.R);
			System.out.println("minCapat " + minCapat);
			int worstRow = worstPool.worstRow(dc.R);
			System.out.println("worstRow " + worstRow);
			boolean swap = false;
			for (Server serverInRow : dc.allServers) {
				if ((!swap) && (serverInRow.ar == worstRow) && (serverInRow.pool.color == worstPool.color)) {
					System.out.println(serverInRow);
					for (Server server : dc.allServers) {
						if ((!swap) && (server.pool.color == worstPool.color) && 
							(server.z == serverInRow.z) && (server.c < serverInRow.c)
							 && (server.ar != serverInRow.ar)) {
							System.out.println(server);
							System.out.println(serverInRow);
							Server.swapServers(serverInRow, server);
							if (dc.getScore() <= score) {
								Server.swapServers(server, serverInRow);
								
							} else {
								System.out.println("swap!");
								swap = true;
							}
						}
					}
				}
			}
			i++;
		}
		int score = dc.getScore();
		System.out.println(score);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Datacenter datacenter = new Datacenter(new File("data/dc.in"));
		sortServersDesc(datacenter);
		System.out.println(datacenter.valid());
		System.out.println(datacenter.getScore());
		
		for(Pool pool : datacenter.allPools){
			//System.out.println(pool.guaranteedCapacity(datacenter.R) + " " + pool.worstRow(datacenter.R));
		}
		
		revertWorst(datacenter);
		
		datacenter.saveSolutionToFile(new File("data/solution3.txt"));
	}
}