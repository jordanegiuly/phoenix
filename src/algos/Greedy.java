package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    	
    	for(Pool pool: dc.allPools) {
    		Collections.sort(pool, new Comparator<Server>() {
    			public int compare(Server s1, Server s2) {
    				float ratio1 = s1.c / s1.z;
    				float ratio2 = s2.c / s2.z;
    				//option 2: ratio
    				if(ratio1 < ratio2) return 1;
    				if(ratio1 > ratio2) return -1;
    				return 0;
    			}
    		});
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
    
    
    
    public static void main(String[] args) throws IOException {
    	Datacenter dc = new Datacenter(new File("data/dc.in"));
		allocatePools(dc);
		int minCapacity = Integer.MAX_VALUE;
		for(Pool pool : dc.allPools) {
			int capacity = pool.getTotalCapacity();
			System.out.println(capacity);
			
			if(capacity<minCapacity) {
				minCapacity = capacity;
			}
		}
		
		System.out.println(minCapacity);
		
    }
}
