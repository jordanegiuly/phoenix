package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import test.Datacenter;
import test.Server;

public class SmallToBig {

	public static void sortServers(Datacenter dc) {
		int M = dc.allServers.size();
		List<Server> sortedServers = new ArrayList<Server>(M);
		
		for(Server server : dc.allServers){
			sortedServers.add(server);
		}
		
		Collections.sort(sortedServers, new Comparator<Server>() {
			public int compare(Server s1, Server s2) {
				if(s1.c < s2.c) return -1;
				if(s1.c > s2.c) return 1;
				return 0;
			}
		});
		
		System.out.println(sortedServers);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Datacenter datacenter = new Datacenter(new File("data/dc.in"));
		sortServers(datacenter);
		//System.out.println(datacenter.R);
		//System.out.println(datacenter.allServers.get(0));
		
		//datacenter.saveSolutionToFile(new File("data/solution0.txt"));
		
	}
}