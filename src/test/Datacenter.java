package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Datacenter {

	public int R; // numRows
	public int S; // numColumns
	public boolean[][] available;
	public int U; // num unavaible servers
	public List<Server> allServers; // of size M
	public List<Pool> allPools;
	//public Pool worstPool;
	
    public Datacenter(File file) throws IOException {
		
		FileInputStream fstream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		int numLine = 0;
		String strLine;
        int M =0;
        while ((strLine = br.readLine()) != null)   {
        	if(numLine==0) {
        		String[] ss = strLine.split(" ");
        		this.R = Integer.parseInt(ss[0]);
        		this.S = Integer.parseInt(ss[1]);
        		this.available = new boolean[R][S];
        		for(int i=0; i<R; i++) {
        			for(int j=0; j<S; j++) {
        				this.available[i][j] = true;
        			}
        		}
        		this.U = Integer.parseInt(ss[2]);
        		
        		int P = Integer.parseInt(ss[3]);
        		this.allPools = new ArrayList<Pool>(P);
        		for(int i=0; i<P; i++) {
        			Pool pool = new Pool();
        			pool.color = i;
        			this.allPools.add(pool);
        		}
        		
        		M = Integer.parseInt(ss[4]);
        		this.allServers = new ArrayList<Server>(M);
        	} else if(numLine>=1 && numLine<=this.U) {
        		String[] ss = strLine.split(" ");
        		int r = Integer.parseInt(ss[0]);
        		int s = Integer.parseInt(ss[1]);
        		this.available[r][s] = false; 
        	} else if(numLine<=this.U+M) {
        		String[] ss = strLine.split(" ");
        		int z = Integer.parseInt(ss[0]);
        		int c = Integer.parseInt(ss[1]);
        		Server server = new Server(z, c);
        		this.allServers.add(server);
        	}
        	numLine++;
        }

		//Close the input stream
		br.close();
	}
    
    public void saveSolutionToFile(File file) throws IOException {
    	FileOutputStream fos = new FileOutputStream(file);
	 	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
	 	for(int i=0; i<allServers.size(); i++) {
	 		Server server = this.allServers.get(i);
			if(server.isAllocated()) {
				if(server.pool != null) {
					bw.write(String.format("%d %d %d", server.ar, server.as, server.pool.color));
				} else {
					bw.write(String.format("%d %d .", server.ar, server.as));
				}
			    	
			} else {
				bw.write("x");
			}
			if(i<allServers.size()-1) {
			    bw.newLine();
			}
		}
	 	bw.close();
    }
    
    public boolean valid() {
    	 
    	boolean[][] occupied = new boolean[R][S];
    	for(int i=0; i<R; i++) {
			for(int j=0; j<S; j++) {
				occupied[i][j] = available[i][j];
			}
		}
    	
    	for(Server server : allServers) {
    		if(!server.isAllocated()) {
    			continue;
    		}
    		
    		if(server.ar<0 || server.ar>=R) return false;
    		if(server.as<0 || server.as>=S) return false;
    		if(server.as+server.z>S) return false;
    		
    		for(int j=server.as; j<server.as+server.z;j++) {
    			if(occupied[server.ar][j]) {
    				return false; // already occupied
    			}
    			occupied[server.ar][j] = true;
    		}
    	}

    	return true; 
    	
    }
    
    public String toString() {
    	
    	String[][]  chars = new String[R][S];
    	
    	for(int i=0; i<R; i++) {
			for(int j=0; j<S; j++) {
				if(!available[i][j]) {
					chars[i][j] = "  x";
				} else {
					chars[i][j] = "  0";
				}
			}
		}
    	
    	for(Server server : allServers) {
    		if(!server.isAllocated()) {
    			continue;
    		}
    		
    		for(int j=server.as; j<server.as+server.z;j++) {
    			int color = server.pool.color;
    			if(color<10) {
    				chars[server.ar][j] = "  "+color;
    			} else {
    				chars[server.ar][j] = " "+color;
    			}
    			 
    		}
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	for(String[] ss : chars) {
    		String line = "";
    		for(String s : ss) {
    			line += s;
    		}
    		line += "\n";
    		sb.append(line);
    	}
    	return sb.toString();
    }
    
    
    public int getScore() {
    	int lowestCapacity = Integer.MAX_VALUE;
    	for(Pool pool: allPools) {
    		int g = pool.guaranteedCapacity(this.R);
    		if(g<lowestCapacity) {
    			lowestCapacity = g;
    		}
    	}
    	return lowestCapacity;
    }
    
    public Pool worstPool() {
    	Pool worstpool = allPools.get(0);
    	int lowestCapacity = Integer.MAX_VALUE;
    	for(Pool pool: allPools) {
    		int g = pool.guaranteedCapacity(this.R);
    		if( g < lowestCapacity) {
    			lowestCapacity = g;
    			worstpool = pool;
    		}
    	}
    	return worstpool;
    }
    
    
    public Server getHigherCapServer(int row) {
    	Server result = allServers.get(0);
    	for(Server server: allServers) {
    		if ((server.ar == row) && (server.c > result.c)) {
    			result = server;
    		}
    	}
    	return result;
    }
   
    
    public static void main(String[] args) throws IOException {
    	
    	Datacenter datacenter = new Datacenter(new File("data/dc.in"));
    	System.out.println(datacenter.R);
    	System.out.println(datacenter);
    	
    	datacenter.saveSolutionToFile(new File("data/solution0.txt"));
    	
    }
    
   
	
}
