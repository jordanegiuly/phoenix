package algos;

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
}
