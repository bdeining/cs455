package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.HousingMedianMapper;
import records.HousingMedianRecord;
import reducers.HousingMedianReducer;

public class HousingMedianJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "housing median");
            job.setJarByClass(HousingMedianJob.class);
            job.setMapperClass(HousingMedianMapper.class);
            job.setCombinerClass(HousingMedianReducer.class);
            job.setReducerClass(HousingMedianReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(HousingMedianRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(HousingMedianRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
