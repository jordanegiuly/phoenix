package test;

public class Server {

	
	public int z; // size (physical)
	public int c; // capacity
	public Pool pool; // null if not allocated
	
	public int ar; // unallocated if <-1 
	public int as;
	
	public Server(int z, int c) {
		this.z = z;
		this.c = c;
		this.pool = null;
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
	
	public String toString() {
		return this.z+" "+this.c + " " + this.ar + " " + this.as;
	}
}
