package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.RentMedianMapper;
import records.RentMedianRecord;
import reducers.RentMedianReducer;

public class RentMedianJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "housing median");
            job.setJarByClass(RentMedianJob.class);
            job.setMapperClass(RentMedianMapper.class);
            job.setCombinerClass(RentMedianReducer.class);
            job.setReducerClass(RentMedianReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(RentMedianRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(RentMedianRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
