package algos;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import test.Datacenter;
import test.Pool;
import test.Server;

public class Greedy {

	/**
	 * Starting with all placed servers. 
	 * @param dc
	 */
	public static void allocatePools(Datacenter dc) {
		
		// Add the next server to the pool with lowest full capacity
		for(Server server : dc.allServers) {
			
			Pool minPool = null;
			int minCapacity = Integer.MAX_VALUE;
			for(Pool pool : dc.allPools) {
				int capacity = pool.getTotalCapacity();
				if(capacity<minCapacity) {
					minPool = pool;
					minCapacity = capacity;
				}
			}
			server.pool = minPool;
			server.pool.add(server);
		}
	}
	
    public static void allocatePools2(Datacenter dc) {
		
		// Add the next server to the pool with lowest full capacity
		for(Server server : dc.allServers) {
			
			Pool minPool = null;
			int minCapacity = Integer.MAX_VALUE;
			for(Pool pool : dc.allPools) {
				int capacity = pool.guaranteedCapacity(dc.R);
				if(capacity<minCapacity) {
					minPool = pool;
					minCapacity = capacity;
				}
			}
			server.pool = minPool;
			server.pool.add(server);
		}
	}

    public static void doIt(Datacenter dc) {
    	
    	allocatePools(dc); 
    	
    	// sort by best ratio
    	int maxSize = 0;
    	for(Pool pool: dc.allPools) {
    		Collections.sort(pool, new Comparator<Server>() {
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
    		if(pool.size()>maxSize) {
    			maxSize = pool.size();
    		}
    	}
    	
    	// now position:
    	
    	for(int n = 0; n<maxSize; n++) {
    		
    		for(Pool pool : dc.allPools) {
    			if(pool.size()<=n) continue; // already placed all in pool. 
    			
    			// look for worse row for this pool 
    			int r = pool.worstRowTotal(dc.R);
    			int put = SmallToBig.placeServer(dc, pool.get(n), r);
    			
    		}	
    	}
    	
    	for(Server server : dc.allServers) {
    		if(server.ar < 0) {
    			System.out.println("a");
				
    			int r = server.pool.worstRow(dc.R);
    			for(int r2=0; r2<dc.R; r2++) {
    				if(r==r2) continue;
    				int put = SmallToBig.placeServer(dc, server, r2);
    				if(put>=0) {
    					System.out.println("placed");
    					break;
    				}
    			}
    			
    		}
    	}
    	
    }
    
    public static void doIt2(Datacenter dc) {
    	
    	allocatePools(dc); 
    	
    	for(Server server : dc.allServers) {
    		
    		int scoreBefore = dc.getScore();
    		int bestRow = -1;
    		int bestNewScore = -1;
    		
    		for(int r=0; r<dc.R; r++) {
    			int put = SmallToBig.placeServer(dc, server, r);
    			if(put>=0) {
    				int newScore = dc.getScore();
    				if(newScore>=bestNewScore) {
    					bestNewScore = newScore;
    					bestRow = r;
    				}
    				SmallToBig.unplaceServer(dc, server);
    			}
    		}
    		
    		if(bestRow>-1 && bestNewScore>=scoreBefore) {
    			SmallToBig.placeServer(dc, server, bestRow);
    		} else {
    			System.out.println("not added");
    		}
    		
    		
    		
    		
    		
    	}
    	
    }
    
    public static void doIt3(Datacenter dc) {
    	
    	allocatePools(dc); 
    	Random r = new Random();
    	
    	for(Server server : dc.allServers) {
    		int i = r.nextInt(dc.R);
    		int j = r.nextInt(dc.S);
    		int tries = 0;
    		while(tries<10000 && !SmallToBig.canBePlaced(dc, i, j, server.z)){
    			i = r.nextInt(dc.R);
        		j = r.nextInt(dc.S);
        		tries++;
    		}
    		
    		if(tries<10000) {
    			server.ar = i;
				server.as = j;
				for (int k = j; k < j + server.z; k++){
					dc.available[i][k] = false;
				}
    		}
    		
    	}
    	
    	
    	
    }
    
	/*
	public boolean[][] occupied;
	
	public boolean canBePlaced(Datacenter dc, int i, int j, int z) {
		if (i < 0 || i >= dc.R) {
			return false;
		} else if (j < 0 || j + z > dc.S) {
			return false;
		} else {
			for(int k = j; k < j+z; k++){
				if (occupied[i][k]) {
					return false;
				}
			}
			return true;	
		}
	};
	
	public void doIt(Datacenter dc) {
		
		this.occupied = new boolean[dc.R][dc.S];
    	for(int i=0; i<dc.R; i++) {
			for(int j=0; j<dc.S; j++) {
				occupied[i][j] = !dc.available[i][j];
			}
		}
    	
    	for(Server server : dc.allServers) {
    		
    	}    	
    	
	}
	
	*/
    
    public static void doIt3Loop(Datacenter dc) {
    	int numTries = 0;
    	while(numTries<10000) {
    		numTries++;
    		/*
    		dc.available = new boolean[dc.R][dc.S];
    		for(int i=0; i<dc.R; i++) {
    			for(int j=0; j<dc.S; j++) {
    				dc.available[i][j] = dc.inititalAvailabilities[i][j] ;
    			}
    		}
    		*/
    		for(Server s : dc.allServers) {
    			if(s.isAllocated()){
    			    SmallToBig.unplaceServer(dc, s);
    			}
    		}
    		
    		
    		doIt3(dc);
    		int score = dc.getScore();
    		System.out.println("Got:"+score);
            if(score>336) {
            	break;
            }
    	}
    	
    	
    }
    
    public static void main(String[] args) throws IOException {
    	Datacenter datacenter = new Datacenter(new File("data/dc.in"));
		
    	doIt3Loop(datacenter);
		//SmallToBig.revertWorst(datacenter);
		/*
		int minCapacity = Integer.MAX_VALUE;
		for(Pool pool : dc.allPools) {
			int capacity = pool.getTotalCapacity();
			System.out.println(capacity);
			
			if(capacity<minCapacity) {
				minCapacity = capacity;
			}
		}
		
		System.out.println(minCapacity);
		*/
		System.out.println(datacenter);
		System.out.println(datacenter.valid());
		System.out.println(datacenter.getScore());
		/*
		for(Pool pool : datacenter.allPools){
			System.out.println(pool.guaranteedCapacity(datacenter.R) + " " + pool.worstRow(datacenter.R));
		}
		*/
		datacenter.saveSolutionToFile(new File("data/solution4.txt"));

    }
}
