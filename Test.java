import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;

public class Test {

	public static void main(String[] args) {

		
		Map<String , ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		List<String> ans = new ArrayList<String>();
		int count = 0;
		
		try {	
			String fileName = "/user/cloudera/output/part-r-00000";
			Configuration conf1 = new Configuration();
		 	FileSystem fs;
			fs = FileSystem.get(conf1);
		
			Path inPath = new Path(fileName);
			System.out.println("ReadingFile using BufferedReader - \n");
			BufferedReader bfr=new BufferedReader(new InputStreamReader(fs.open(inPath)));
			
			String line;
			
			while((line = bfr.readLine()) != null){
				//process the line
				String a[] = line.split("\t");
				String key = a[0];
				ArrayList<String> value = new ArrayList<String>();
				String values[] = a[1].split(",");
				if(values[0].contains("["))
					values[0] = values[0].substring(1,values[0].length());
				if(values[0].contains("]"))
					values[0] = values[0].substring(0, values[0].length() - 1);
				if(values[values.length-1].contains("]"))
					values[values.length-1] = values[values.length-1].substring(0 , values[values.length-1].length() - 1);

				for(int i = 0; i < values.length; i++){
					int last = values[i].indexOf(':');
					values[i]=values[i].substring(0,last);

				}

				for(String s:values)
					value.add(s.trim());
				map.put(key, value);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	boolean flag = true;							
	while(flag) {
		try {
	        System.out.println("Enter query to be searched : <word1> <operator> <word2>");
	        System.out.println("Enter any digit to exit");
	        System.out.println("Boolean AND Search can be performed using the & operator");
	        System.out.println("Boolean OR Search can be performed using the | operator");
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        String phrase = in.readLine();
        
	        if(phrase.matches("[0-9]+"))
	        	System.exit(0);
				String[] words = phrase.split("\\s+");

				for(int i = 0; i< words.length; i++) {
	        	if(words.length == 1) {
	        		ans = new ArrayList<String>();
     	            ans.addAll(map.get(words[0]));
     	           
	        		
	        	}
	        	else if(words.length == 3){
	        		
			        	if(words[i].equals("|")) {
			        		
			        		ans = new ArrayList<String>();
			        		ArrayList<String> temp1 = map.get(words[i-1].toLowerCase());
			        		ArrayList<String> temp2 = map.get(words[i+1].toLowerCase());
			        		Set<String> set = new HashSet<String>();
			        		set.addAll(temp1);
			        		//System.out.println(set.size());
			        		set.addAll(temp2);
			        		ans.addAll(set);
			        					        	
			        	}
			        	else if(words[i].equals("&")) {
			        		
			        		ans = new ArrayList<String>();
			        		ArrayList<String> temp1 = map.get(words[i-1].toLowerCase());
			        		ArrayList<String> temp2 = map.get(words[i+1].toLowerCase());
			        		
			        		temp1.retainAll(temp2);
			        		ans.addAll(temp1);
		
			        	}
			        	
	        	}
	        	else {

	        		
	        		
		        	if(words[i].equals("|")) {
		        		
		        		if(count == 0) {
		        			count++;
		        			ans = new ArrayList<String>();
			        		ArrayList<String> temp1 = map.get(words[i-1].toLowerCase());
			        		ArrayList<String> temp2 = map.get(words[i+1].toLowerCase());
			        		Set<String> set = new HashSet<String>();
			        		set.addAll(temp1);
			        		//System.out.println(set.size());
			        		set.addAll(temp2);
			        		ans.addAll(set);
			        		
		        		}
		        		else {
		        			//System.out.println(ans.size());
		        			ArrayList<String> temp2 = map.get(words[i+1].toLowerCase());
		        			Set<String> set = new HashSet<String>();
		        			set.addAll(ans);
			        		set.addAll(temp2);
			        		//System.out.println(set.size());
		        			ans.addAll(set);
		        		}
		        	
		        	}
		        	else if(words[i].equals("&")) {
		        		
			        		if(count == 0) {
				        		count++;
				        		ans = new ArrayList<String>();
				        		ArrayList<String> temp1 = map.get(words[i-1].toLowerCase());
				        		ArrayList<String> temp2 = map.get(words[i+1].toLowerCase());
				        		temp1.retainAll(temp2);
				        		
				        		Set<String> unique = new HashSet<String>(temp1);
				        		ans.addAll(unique);
			        	}
		        		else {
		        			
		        			ArrayList<String> temp1 = map.get(words[i+1].toLowerCase());
		        			temp1.retainAll(ans);
		        			
		        			Set<String> unique = new HashSet<String>(temp1);
			        		ans = new ArrayList<String>();
			        		ans.addAll(unique);
			        		
		        		}
	        	
	        		
		        		}
	        		}	
	        	
				}	
			}catch(NullPointerException e) {
	   	    	 System.out.println("Word not found OR Wrong input format");
	   	    	 System.exit(0);
	   	    }catch (Exception e) {
	   			System.out.println("Word not found OR Wrong input format");
	   			System.exit(0);
	   		}
	   		Set<String> set = new HashSet<String>();
	        set.addAll(ans);
	        System.out.println(Arrays.toString(new ArrayList<String>(set).toArray()));
	        ans = new ArrayList<String>();;
	   		//System.out.println(ans.size());
	    }
	
	}
}
