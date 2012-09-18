package uk.co.fues.submission;

// Java classes
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.commoncrawl.hadoop.mapred.ArcInputFormat;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.mapper.SinMapper;
import uk.co.fues.submission.reducer.RankReducer;
import uk.co.fues.submission.util.datastructure.DoubleArrayWritable;

public class MoneyIsTheRootOfAllEvil extends Configured implements Tool {

	org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	boolean getPaths = false;


  public static class SampleFilter
      implements PathFilter {

    private static int count =         0;
    private static int max   = 999999999;

    public boolean accept(Path path) {

        if (!path.getName().endsWith(".arc.gz"))
        return false;

      SampleFilter.count++;

      if (SampleFilter.count > SampleFilter.max)
        return false;

      return true;
    }
  }

  /**
   * Implmentation of Tool.run() method, which builds and runs the Hadoop job.
   *
   * @param  args command line parameters, less common Hadoop job parameters stripped
   *              out and interpreted by the Tool class.  
   * @return      0 if the Hadoop job completes successfully, 1 if not. 
   */
  @Override
  public int run(String[] args)
      throws Exception {

    String outputPath = null;
    String configFile = null;

    // Read the command line arguments.
    if (args.length <  1)
      throw new IllegalArgumentException("Example JAR must be passed an output path.");

    outputPath = args[0];

    if (args.length >= 2)
      configFile = args[1];
    
//    String inputPath   = "s3n://aws-publicdatasets/common-crawl/parse-output/segment/1341690163490/1341782247*";
//    String inputPath = "s3n://aws-publicdatasets/common-crawl/parse-output/segment/1341690163490/1341782247571_*";
    String inputPath = "s3n://aws-publicdatasets/common-crawl/parse-output/segment/1341690153900/13417178*";

    

    // Read in any additional config parameters.
    if (configFile != null) {
      log.info("adding config parameters from '"+ configFile + "'");
      this.getConf().addResource(configFile);
    }

    // Creates a new job configuration for this Hadoop job.
    JobConf job = new JobConf(this.getConf());
    
    if(getPaths) {
        FileSystem fs0 = FileSystem.get(new URI("s3n://aws-publicdatasets"), job);
        List<String> paths = new ArrayList<String>();
        getValidArcFiles(paths, fs0);
    }

    job.setJarByClass(MoneyIsTheRootOfAllEvil.class);

    // Scan the provided input path for ARC files.
    log.info("setting input path to '"+ inputPath + "'");
    FileInputFormat.addInputPath(job, new Path(inputPath));
    FileInputFormat.setInputPathFilter(job, SampleFilter.class);

    // Delete the output path directory if it already exists.
    log.info("clearing the  output path at '" + outputPath + "'");

    FileSystem fs = FileSystem.get(new URI(outputPath), job);

    if (fs.exists(new Path(outputPath)))
      fs.delete(new Path(outputPath), true);

    // Set the path where final output 'part' files will be saved.
    log.info("setting output path to '" + outputPath + "'");
    FileOutputFormat.setOutputPath(job, new Path(outputPath));
    FileOutputFormat.setCompressOutput(job, false);

    // Set which InputFormat class to use.
    job.setInputFormat(ArcInputFormat.class);

    // Set which OutputFormat class to use.
    job.setOutputFormat(TextOutputFormat.class);

    // Set the output data types.
    job.setOutputKeyClass(DoubleWritable.class);
    job.setOutputValueClass(DoubleArrayWritable.class);

    // Set which Mapper and Reducer classes to use.
    job.setMapperClass(SinMapper.class);
   // job.setCombinerClass(RankReducer.class);
    job.setReducerClass(RankReducer.class);

    if (JobClient.runJob(job).isSuccessful())
      return 0;
    else
      return 1;
  }
  
  private void getValidArcFiles(List<String> paths, FileSystem fs0) throws IOException {
	    String baseInputPath = "s3n://aws-publicdatasets/common-crawl/parse-output/segment";
	    for (FileStatus fileStatus : fs0.globStatus(new Path("/common-crawl/parse-output/valid_segments/[0-9]*"))) { 
	        String[] parts = fileStatus.getPath().toString().split("/");
	        String segmentInputPath = baseInputPath + "/" + parts[parts.length-1] + "/";
	        log.info("adding segment path '" + segmentInputPath + "'");
	        
	        for (FileStatus fileStatus2 : fs0.globStatus(new Path(segmentInputPath + "*"))) {
	            String[] parts2 = fileStatus2.getPath().toString().split("/");
	            String fileInputPath = segmentInputPath + parts2[parts2.length-1];
	            log.info(fileInputPath + "'");
	            if(fileInputPath.contains(".arc.gz")) {
	                paths.add(fileInputPath);      	  
	            }
	        }
	      }
	      
	      FileUtils.writeLines(new File("resources/job/inputPaths.txt"), paths);
  }

  /**
   * Main entry point that uses the {@link ToolRunner} class to run the example
   * Hadoop job.
   */
  public static void main(String[] args)
      throws Exception {
	  Configuration conf = new Configuration();
	  conf.set("fs.s3n.awsAccessKeyId", "*");
	  conf.set("fs.s3n.awsSecretAccessKey", "*");
	  int res = ToolRunner.run(conf, new MoneyIsTheRootOfAllEvil(), args);
	  System.exit(res);
  }
}

