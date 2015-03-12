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

	int R; // numRows
	int S; // numColumns
	boolean[][] available;
	int U; // num unavaible servers
	List<Server> allServers; // of size M
	List<Pool> allPools;
	
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
			    bw.write(String.format("%d %d %d", server.ar, server.as, server.pool.color));	
			} else {
				bw.write("x");
			}
			if(i<allServers.size()-1) {
			    bw.newLine();
			}
		}
	 	bw.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	Datacenter datacenter = new Datacenter(new File("data/test-data.txt"));
    	
    	System.out.println(datacenter.R);
    	System.out.println(datacenter.allServers.get(0));
    	
    	datacenter.saveSolutionToFile(new File("data/solution0.txt"));
    	
    }
    
   
	
}
