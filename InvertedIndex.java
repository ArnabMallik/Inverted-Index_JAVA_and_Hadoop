import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author arnab
 *
 */
public class InvertedIndex {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException{
		
		File dir = new File(".");
		Files.deleteIfExists(Paths.get("/home/cloudera/Assignment2/Index.txt"));
		String fileNames[];
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".txt");
		    }
		});
		
		fileNames = new String[files.length];
		
		int k = 0;
		for(File f : files) {
			fileNames[k++] = f.getName();
		}
		
        Index index = new Index();
        index.buildIndex(fileNames);
        
        while(true) {
	        System.out.println("Enter query to be searched : <word1> <operator> <word2>");
	        System.out.println("Enter any digit to exit");
	        System.out.println("Boolean AND Search can be performed using the & operator");
	        System.out.println("Boolean OR Search can be performed using the | operator");
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        String phrase = in.readLine();
        
	        if(phrase.matches("[0-9]+"))
	        	System.exit(0);
	        else
	        {
	        	Query q = new Query();
	        	q.find(index,phrase);
	        }
	        	//Query.find(phrase);
        }
	}

}
