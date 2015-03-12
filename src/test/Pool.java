package test;

import java.util.HashSet;

public class Pool extends HashSet<Server> {

	int color; // its index in the datacenter pools
	private static final long serialVersionUID = 1L;
	
	int getCapacity(int rowDown) {
		int capacity = 0;
		for(Server server : this) {
			if(server.isAllocated() && server.ar != rowDown) {
				capacity += server.c;
			}
		}
		return capacity;
	}
	
	int guaranteedCapacity(int numRows) {
		int minCapacity = Integer.MAX_VALUE;
		
		for(int rowDown=0; rowDown<numRows; rowDown++) {
			int cap = getCapacity(rowDown);
			if(cap<minCapacity) {
				minCapacity=cap;
			}
		}
		return minCapacity;
	}

}
