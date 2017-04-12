package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mappers.RuralPopulationMapper;
import records.RuralPopulationRecord;
import reducers.RuralPopulationReducer;

public class RuralPopulationJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "urban renting");
            job.setJarByClass(RuralPopulationJob.class);
            job.setMapperClass(RuralPopulationMapper.class);
            job.setCombinerClass(RuralPopulationReducer.class);
            job.setReducerClass(RuralPopulationReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(RuralPopulationRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(RuralPopulationRecord.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
