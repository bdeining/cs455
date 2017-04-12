package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.TenureMapper;
import records.TenureRecord;
import reducers.TenureReducer;

public class TenureJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "tenure");
            job.setJarByClass(TenureJob.class);
            job.setMapperClass(TenureMapper.class);
            job.setCombinerClass(TenureReducer.class);
            job.setReducerClass(TenureReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(TenureRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(TenureRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
