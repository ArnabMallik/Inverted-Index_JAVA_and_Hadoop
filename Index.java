import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author arnab
 *
 */
class Index {
	
	 File f = new File("/home/cloudera/Assignment2/Index.txt");
	
    public Map<Integer,String> sources; // maintains doc id and doc name mapping
    public HashMap<String, ArrayList<Location>> index; // maintains posting
   

    /**
     * 
     */
    Index(){
        sources = new HashMap<Integer,String>(); 
        index = new HashMap<String, ArrayList<Location>>();
    }

    /**
     * @param files
     */
    public void buildIndex(String[] files){
        int i = 0;
        for(String fileName:files){

            
            try(BufferedReader file = new BufferedReader(new FileReader(fileName)))
            {
                sources.put(i,fileName);
                String ln; int wc = 0;
                while( (ln = file.readLine()) !=null) {
                    String[] words = ln.split("\\W+");
                    for(String word:words){
                        word = word.toLowerCase();
                        wc++;
                        if (isValid(word)) {
                        	
                        	Location location = new Location(i,wc);
                        	
                    		
                        	if(!index.containsKey(word)) {
                        		List<Location> list = new ArrayList<Location>();
                        		list.add(location);
	                            index.put(word, (ArrayList<Location>) list);
	                            continue;
                        	}
                        	
                        	List<Location> list = index.get(word);
                        	list.add(location);
                        	index.put(word, (ArrayList<Location>) list);
                        	
                        	
                        }
                    }
                }
                
                //sorting map
                
                TreeMap<String, ArrayList<Location>> sorted = new TreeMap<String, ArrayList<Location>>();
                sorted.putAll(index);
                
                //writing inverted index to file
                
               
               // System.out.println(f.exists());
                	
                
                PrintStream out = new PrintStream(new FileOutputStream(f));

    			// Write objects to file
    			//

    			Set set = sorted.entrySet();
    		    Iterator iterator = set.iterator();
    		    while(iterator.hasNext()) {
    		         Map.Entry mentry = (Map.Entry)iterator.next();
    		         out.print(mentry.getKey()+" : ");
    		         ArrayList<Location> locations = (ArrayList<Location>) mentry.getValue();
    		         for(Location l : locations)
    		        	 out.print(l + ";");
    		         out.println();
    		      }
    			
    			
    			out.close();
    			f.deleteOnExit();

                
            } catch (IOException e){
                System.out.println("File "+fileName+" not found. Skip it");
            }
            catch (NullPointerException e){
                System.out.println("Word not found");
            }
            catch (Exception e) {
    			// TODO: handle exception
        		System.out.println("Word not found");
    		}
            i++;
        }
        
    }

    private boolean isValid(String word) {
		// TODO Auto-generated method stub
    	
    	String stopWords[] = {"and", "but", "is", "the", "to"};
    	
    	if(word.matches("[0-9]+"))
    		return false;
    	
    	
    	for(String s:stopWords)
    	if(word.equals(s))
    		return false;
    	
    	if(!word.matches("[a-z]+"))
    		return false;
    	
    	
		return true;
	}

	/**
     * @param phrase
     */
    public void find(String phrase){}
}
