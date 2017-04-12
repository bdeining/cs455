package jobs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import combiners.ElderlyPopulationCombiner;
import mappers.ElderlyPopulationMapper;
import records.ElderlyPopulationRecord;
import reducers.ElderlyPopulationReducer;

public class ElderlyPopulationJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "elderly population");
            job.setJarByClass(ElderlyPopulationJob.class);
            job.setMapperClass(ElderlyPopulationMapper.class);
            job.setCombinerClass(ElderlyPopulationCombiner.class);
            job.setReducerClass(ElderlyPopulationReducer.class);
            job.setNumReduceTasks(1);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(ElderlyPopulationRecord.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
