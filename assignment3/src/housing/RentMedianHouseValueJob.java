package housing;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import record.RentMedianHouseValueRecord;

public class RentMedianHouseValueJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "urban renting");
            job.setJarByClass(RentMedianHouseValueJob.class);
            job.setMapperClass(RentMedianHouseValueMapper.class);
            job.setCombinerClass(RentMedianHouseValueReducer.class);
            job.setReducerClass(RentMedianHouseValueReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(RentMedianHouseValueRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(RentMedianHouseValueRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
