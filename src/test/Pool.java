package test;

import java.util.ArrayList;

public class Pool extends ArrayList<Server> {

	public int color; // its index in the datacenter pools
	private static final long serialVersionUID = 1L;
	public int worstRow;
	
	public int getCapacity(int rowDown) {
		int capacity = 0;
		for(Server server : this) {
			if(server.isAllocated() && server.ar != rowDown) {
				capacity += server.c;
			}
		}
		return capacity;
	}
	
	public int getTotalCapacity() {
		int capacity = 0;
		for(Server server : this) {
			capacity += server.c;
		}
		return capacity;
	}
	
	public int guaranteedCapacity(int numTotalRows) {
		int minCapacity = Integer.MAX_VALUE;
		
		for(int rowDown=0; rowDown<numTotalRows; rowDown++) {
			int cap = getCapacity(rowDown);
			if(cap<minCapacity) {
				minCapacity=cap;
				worstRow = rowDown;
			}
		}
		return minCapacity;
	}
	
	
	public int worstRow(int numTotalRows) {
		//int minCapacity = guaranteedCapacity(numTotalRows);
		
		int maxCapacity = 0;
		int worstRow =-1;
		for(int rowDown=0; rowDown<numTotalRows; rowDown++) {
			int cap = getCapacity(rowDown);
			if(cap > maxCapacity) {
				maxCapacity = cap;
				worstRow=rowDown;
			}
		}
		return worstRow;
	}
	
	public int worstRowTotal(int numTotalRows) {
		int minCapacity = Integer.MAX_VALUE;
		int minRow = -1;
		for(int r=0; r<numTotalRows; r++) {
			int cap = 0;
			for(Server server : this) {
				if(server.ar==r) {
				    cap += server.c;
				}
			}
			
			if(cap<minCapacity) {
				minCapacity = cap;
				minRow = r;
			}
		}
		return minRow;
	}
}
