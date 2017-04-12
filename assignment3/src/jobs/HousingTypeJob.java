package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.HousingTypeMapper;
import records.HousingTypeRecord;
import reducers.HousingTypeReducer;

public class HousingTypeJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "housing");
            job.setJarByClass(HousingTypeJob.class);
            job.setMapperClass(HousingTypeMapper.class);
            job.setCombinerClass(HousingTypeReducer.class);
            job.setReducerClass(HousingTypeReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(HousingTypeRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(HousingTypeRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
