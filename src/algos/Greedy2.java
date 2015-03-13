package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import test.Datacenter;
import test.Pool;
import test.Server;

public class Greedy2 {

	public static void doIt(Datacenter dc, boolean sortServers) {
        
        List<Server> servers = new ArrayList<Server>(dc.allServers);

        if(sortServers) {
        	Collections.sort(servers, new Comparator<Server>() {
        		public int compare(Server s1, Server s2) {
        			float ratio1 = s1.c / s1.z;
        			float ratio2 = s2.c / s2.z;
        			//option 2: ratio
        			if(ratio1 < ratio2) return 1;
        			if(ratio1 > ratio2) return -1;
        			//if(s1.c < s2.c) return 1;
        			//if(s1.c > s2.c) return -1;
        			return 0;
        		}
        	});
        }
        
        int poolID = 0;
        int rowID = 0;

		// now position AND attribute to pool, greedily:
        for(Server server : servers) {
        	int currentScore = dc.getScore();
        	
            int bestScore = -1;
            Pool bestPool = null;
            int bestRow = -1;
            
            // look all positions and pools and chose the best pair
            for(int r=0; r<dc.R; r++) {
            	int put = putServerOnRow(dc, server, r);
            	if(put>=0) { // we placed it physically
            		
            		for(Pool pool : dc.allPools) {
            			
            			// allocate to this pool
            			pool.add(server);
            			server.pool = pool;
            			int score = dc.getScore();
                		if(score>=bestScore) {
                			 bestScore = score;
                	         bestPool = pool;
                	         bestRow = r;
                		}
                		// undo pool allocation
                		pool.remove(pool.size()-1); 
                		server.pool = null;
                	
            		}
            		// undo physical placement
            		SmallToBig.unplaceServer(dc, server);
            	}
            }
            
            if(bestScore>currentScore) {
            	System.out.println("best: "+bestScore);
            	// allocate at best
            	putServerOnRow(dc, server, bestRow);
            	bestPool.add(server);
    			server.pool = bestPool;
            }
            
            else if(bestScore==currentScore) {
            	System.out.println("next: "+currentScore);
            	// just put it anywhere
            	int put = SmallToBig.placeServer(dc, server, rowID);
            	//rowID = put+1;
            	if(rowID>=dc.R) rowID=0;
            	
            	Pool pool = dc.allPools.get(poolID);
            	server.pool = pool;
            	pool.add(server);
            	poolID++;
            	if(poolID>=dc.allPools.size()) poolID=0;
            	
            }
            
            
        }
        
        loopSwaps(dc, 1);
	}
	
	/**
	 * returns corresponding j or -1 if couldn't be placed
	 * @param dc
	 * @param server
	 * @param row
	 * @return
	 */
	public static int putServerOnRow(Datacenter dc, Server server, int row) {
		for(int j=0; j<dc.S; j++) {
			if (SmallToBig.canBePlaced(dc, row, j, server.z)) { 
				//we place the server here
				server.ar = row;
				server.as = j;
				for (int k = j; k < j + server.z; k++){
					dc.available[row][k] = false;
				}
				return j;
			}
		}
		return -1;
	}
	
	public static boolean swapPools(Server s1, Server s2) {
		if(s1.pool == s2.pool || s1.pool==null || s2.pool==null) {
			return false;
		} else {
			Pool p1 = s1.pool;
			Pool p2 = s2.pool;
			
			s1.pool = p2;
			s2.pool = p1;
			
			p1.set(p1.indexOf(s1), s2);
			p2.set(p2.indexOf(s2), s1);
			return true;
		}
	}
	
	/**
	 * assumes same size
	 * @param s1
	 * @param s2
	 */
	public static boolean swapPositions(Server s1, Server s2) {
		if(s1.z != s2.z || !s1.isAllocated() || !s2.isAllocated()) {
			return false;
		} else {
			int ar1 = s1.ar;
			int as1 = s1.as;
			s1.ar = s2.ar;
			s1.as = s2.as;
			s2.ar = ar1;
			s2.as = as1;
			return true;
		}
	}
	
	public static void loopSwaps(Datacenter dc, int loops) {
		
		for(int l=0; l<loops; l++) {
            System.out.println("Swap loop:"+l);
			for(int i=0; i<dc.allServers.size()-1; i++) {
				for(int j=i+1; j<dc.allServers.size(); j++) {
					Server s1 = dc.allServers.get(i);
					Server s2 = dc.allServers.get(j);

					int scoreBefore = dc.getScore();

					// try swap pools
					if(swapPools(s1, s2)) {
						int scoreSwapPools = dc.getScore();
						if(scoreSwapPools<scoreBefore) {
							// undo swap
							swapPools(s1, s2);
						} else { // keep
							scoreBefore = scoreSwapPools;
						}
					}

					// try swap positions
					if(swapPositions(s1, s2)) {
						int scoreSwapPos = dc.getScore();
						if(scoreSwapPos<scoreBefore) {
							// undo swap
							swapPositions(s1, s2);
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Datacenter datacenter = new Datacenter(new File("data/dc.in"));
		doIt(datacenter, true);
		System.out.println(datacenter);
		System.out.println(datacenter.valid());
		System.out.println(datacenter.getScore());
		datacenter.saveSolutionToFile(new File("data/greedy2.txt"));
	}


}
