package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.HispanicMapper;
import records.HispanicRecord;
import reducers.HispanicReducer;

public class HispanicJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "hispanic");
            job.setJarByClass(HispanicJob.class);
            job.setMapperClass(HispanicMapper.class);
            job.setCombinerClass(HispanicReducer.class);
            job.setReducerClass(HispanicReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(HispanicRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(HispanicRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
