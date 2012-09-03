package uk.co.fues.submission.util.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

// takes the output file and cretaes the JSON for input into vis
public class JSONConverter {

	public static void main(String [] args) {
		File f = new File("MoneyIsTheRootOfAllEvil/part-00000");
		List<List<Double>> sins = new ArrayList<List<Double>>();
		for(int i = 0; i<7; i++) {
			sins.add(new ArrayList<Double>());
		}
		try {
			List<String> lines = IOUtils.readLines(new FileInputStream(f));
			for(String record : lines) {
				System.out.println(record);
				String data = record.split("\t")[1];
				String [] datums = data.split(",");
				for(int i = 0; i<datums.length; i++) {
					sins.get(i).add(Double.parseDouble(datums[i]));
				}
			}
		String json = "[";
		int lengthA = sins.size();
		for(int h = 0; h<lengthA; h++) {
			List<Double> sin = sins.get(h);
			String entry = "[";
			int xIncrement = 0;
			int lengthB = sin.size();
			for(int i = 0; i<lengthB; i++) {
				String datum = "{\"x\":" + xIncrement + ",\"y\":" + sin.get(i)*80000 + ",\"colour\":\"" + getSinColour(h) + "\"}";
				if(i!=lengthB-1) {
					datum = datum + ",";
				}
				entry = entry + datum ;
				xIncrement = xIncrement+1;
			}
			json = json + entry + "]";
			if(h!=lengthA-1) {
				json = json + ",";
			}

		}
		json = json + "]";
			
		System.out.println(json);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getSinColour(int index) {
        String colour = "#000000";
        switch (index) {
            case 0:  colour = "#A60808"; //lust = red
                     break;
            case 1:  colour = "#0059BF"; //greed = blue
                     break;
            case 2:  colour = "#D10478"; //gluttoy = pink
                     break;
            case 3:  colour = "#318500"; //envy = green
                     break;
            case 4:  colour = "#701BE0"; //pride = purple
                     break;
            case 5:  colour = "#DB5800"; //wrath = orange
                     break;
            case 6: colour = "#FFDE05"; //sloth = yellow
                     break;
        }
        return colour;
	}
}
