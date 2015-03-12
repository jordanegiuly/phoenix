package test;

public class Server {

	int z; // size (physical)
	int c; // capacity
	Pool pool; // null if not allocated
	
	int ar; // unallocated if <-1 
	int as;
	
	public Server(int z, int c) {
		this.z = z;
		this.c = c;
		this.pool =null;
		this.ar = -1;
		this.as = -1;
	}
	
	public void place(int r, int s) {
		this.ar = r;
		this.as = s;
	}
	
	public void assign(Pool pool) {
		this.pool = pool;
	}
	
	boolean isAllocated() {
		return ar >= 0;
	}
	
}
