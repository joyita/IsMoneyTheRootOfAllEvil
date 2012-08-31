package uk.co.fues.submission;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class RankReducer extends MapReduceBase
  implements Reducer<DoubleWritable, DoubleArrayWritable, DoubleWritable, Text> {
  public void reduce(DoubleWritable key, Iterator<DoubleArrayWritable> values, OutputCollector<DoubleWritable, Text> out, Reporter reporter)
	      throws IOException {
	double[] sinScores = new double[7];
	int total = 0;
	while (values.hasNext()) {
		DoubleArrayWritable sinIndex = values.next();
		Writable[] sins = sinIndex.get();
		for(int i=0;i<7;i++) {
			sinScores[i]+=((DoubleWritable) sins[i]).get();			
		}
		total++;
	}
	DoubleWritable[] init = new DoubleWritable[7];
	String retval = "";
	for(int i=0;i<7;i++) {
		init[i] = new DoubleWritable(sinScores[i]/total);
		retval=retval+(Double.valueOf(valueFormat.format(sinScores[i]/total))+ ",");
	}	
	out.collect(key, new Text(retval));
  }
  
	DecimalFormat valueFormat = new DecimalFormat("#.###");

}
