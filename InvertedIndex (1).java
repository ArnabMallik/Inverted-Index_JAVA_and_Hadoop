import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {

    public static class IndexMap
            extends Mapper<LongWritable, Text, Text, Text> {

        private FileSplit spliter;
        private Pattern pattern = Pattern.compile("\\b(\\w)+\\b", Pattern.CASE_INSENSITIVE);
        

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            spliter = (FileSplit) context.getInputSplit();
            Matcher matcher = pattern.matcher(value.toString());
            String[] filepath = spliter.getPath().toString().split("/");
            String path = filepath[filepath.length-1];
            Long start = key.get();
            while (matcher.find()) {
	            Long pos = start + matcher.start();
                //removing stopwords
                if(!matcher.toString().toLowerCase().contains("and") || !matcher.toString().toLowerCase().contains("but") || !matcher.toString().toLowerCase().contains("is") || !matcher.toString().toLowerCase().contains("the") || !matcher.toString().toLowerCase().contains("to"))
                	context.write(new Text(matcher.group().toLowerCase()), new Text(path + ":" + pos));
            }
        }
    }

    public static class IndexReduce extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            List<String> list = new ArrayList<String>();
            for (Text text: values) {
                Collections.addAll(list, text.toString().split(","));
            }
            
            Collections.sort(list, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    String[] s1 = o1.split(":"), s2 = o2.split(":");
                    if (!s1[0].equals(s2[0]))
                        return s1[0].compareTo(s2[0]);
                    return Integer.parseInt(s1[1]) - Integer.parseInt(s2[1]);
                }
            });
            
            context.write(key, new Text(list.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Inverted_Index");
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(InvertedIndex.IndexMap.class);
        job.setReducerClass(InvertedIndex.IndexReduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.waitForCompletion(true);
        
       }
}
