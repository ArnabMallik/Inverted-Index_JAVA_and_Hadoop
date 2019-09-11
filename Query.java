import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Query {

	public void find(Index index2, String phrase) {
		// TODO Auto-generated method stub
		String line;
		
		String filePath = "/home/cloudera/Assignment2/Index.txt";
		File file = new File(filePath);
		
	    HashMap<String, ArrayList<Location>> index = new HashMap<String, ArrayList<Location>>();
	    
		
    	try {
    		
    		@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
    	    
    	    while ((line = reader.readLine()) != null)
    	    {
    	        String[] parts = line.split(":");
	        	ArrayList<Location> value = new ArrayList<Location>();
	            String key = parts[0].trim();
	            String string = parts[1];
	            //string = string.replaceAll("[^0-9]+", " ");
	            String a[] = (string.split(";"));
	            
	            for(int i = 0; i < a.length; i++) 
	            	a[i] = a[i].replaceAll("[^0-9]+", " ");
	            	
	           
	            for(String loc : a) {
	            	String b[] = (loc.trim()).split(" ");
	            	Location l = new Location(Integer.parseInt(b[0]), Integer.parseInt(b[1]));
	            	value.add(l);
	            }
	            
	            index.put(key, value);
    	    }
    	    
    		
    		//words is the query phrase
	        String[] words = phrase.split("\\s+");
	        //Location res;
	        
	        ArrayList<Location> ans = new ArrayList<Location>();
	        int count = 0;
	        for(int i = 0; i< words.length; i++) {
	        	
	        	if(words.length == 1) {
	        		 
	        		ans = new ArrayList<Location>();
	     	        //for(String word: words){
     	            ArrayList<Location> temp = index.get(words[0].toLowerCase());
     	            ans.addAll(temp);
     	            break;
	     	        //}
	        		
	        	}
	        	else if(words.length == 3){
	        		
			        	if(words[i].equals("|")) {
			        		
			        		ans = new ArrayList<Location>();
			        		ArrayList<Location> temp1 = index.get(words[i-1].toLowerCase());
			        		ArrayList<Location> temp2 = index.get(words[i+1].toLowerCase());
			        		ans.addAll(temp1);
			        		ans.addAll(temp2);
			        	
			        	}
			        	else if(words[i].equals("&")) {
			        		
			        		ans = new ArrayList<Location>();
			        		ArrayList<Location> temp1 = index.get(words[i-1].toLowerCase());
			        		ArrayList<Location> temp2 = index.get(words[i+1].toLowerCase());
			        		ArrayList<Location> temp3 = new ArrayList<Location>();
			        		ans.addAll(temp1);
			        		ans.addAll(temp2);
			        		
			        		for(Location a:temp1) {
			        			for(Location b:temp2) {
			        				if(a.getDocId() == b.getDocId()) {
			        					temp3.add(a);
			        					temp3.add(b);
			        				}
			        			}
			        		}
			        		
			        		Set<Location> unique = new HashSet<Location>(temp3);
			        		ans = new ArrayList<Location>();
			        		ans.addAll(unique);
		
			        	}
	        	}
	        	else {

	        		
	        		
		        	if(words[i].equals("|")) {
		        		
		        		//ans = new ArrayList<Location>();
		        		if(count == 0) {
		        			count++;
			        		ArrayList<Location> temp1 = index.get(words[i-1].toLowerCase());
			        		ArrayList<Location> temp2 = index.get(words[i+1].toLowerCase());
			        		ans.addAll(temp1);
			        		ans.addAll(temp2);
		        		}
		        		else {
		        			//System.out.println(ans.size());
		        			ArrayList<Location> temp1 = index.get(words[i+1].toLowerCase());
		        			ans.addAll(temp1);
		        		}
		        	
		        	}
		        	else if(words[i].equals("&")) {
		        		
			        		if(count == 0) {
			        			count++;
			        		ans = new ArrayList<Location>();
			        		ArrayList<Location> temp1 = index.get(words[i-1].toLowerCase());
			        		ArrayList<Location> temp2 = index.get(words[i+1].toLowerCase());
			        		ArrayList<Location> temp3 = new ArrayList<Location>();
			        		ans.addAll(temp1);
			        		ans.addAll(temp2);
			        		
			        		for(Location a:temp1) {
			        			for(Location b:temp2) {
			        				if(a.getDocId() == b.getDocId()) {
			        					temp3.add(a);
			        					temp3.add(b);
			        				}
			        			}
			        		}
			        		
			        		Set<Location> unique = new HashSet<Location>(temp3);
			        		ans = new ArrayList<Location>();
			        		ans.addAll(unique);
			        	}
		        		else {
		        			
		        			ArrayList<Location> temp1 = index.get(words[i+1].toLowerCase());
		        			ArrayList<Location> temp3 = new ArrayList<Location>();
		        			for(Location a:temp1) {
			        			for(Location b:ans) {
			        				if(a.getDocId() == b.getDocId()) {
			        					temp3.add(a);
			        					temp3.add(b);
			        				}
			        			}
			        		}
		        			Set<Location> unique = new HashSet<Location>(temp3);
			        		ans = new ArrayList<Location>();
			        		ans.addAll(unique);
			        		
		        		}
	        		
		        	}
	        	}	
	        }
	        
	        if(ans.isEmpty() || ans == null) {
	            System.out.println("Not found");
	            return;
	        }
	        System.out.println("Found in: ");
	        for(Location num : ans){
	            System.out.println("\t"+index2.sources.get(num.getDocId())+" : "+ num.getWordId());
	        }
    	}
    	catch (NullPointerException e) {
			// TODO: handle exception
    		System.out.println("Wrong input format or Word not found");
		}
    	catch (Exception e) {
			// TODO: handle exception
    		System.out.println("Word not found");
		}
    	
    
		
	}

}
