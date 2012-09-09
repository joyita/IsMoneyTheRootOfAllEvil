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

	public static void main(String[] args) {
		File f = new File("MoneyIsTheRootOfAllEvil/part-00000");
		try {
			List<String> lines = IOUtils.readLines(new FileInputStream(f));
			JSONConverter runner = new JSONConverter();
			System.out.println(runner.getCollatedJSON(runner.getSins(), lines));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<List<Double>> getSins() {
		List<List<Double>> sins = new ArrayList<List<Double>>();
		for (int i = 0; i < 7; i++) {
			sins.add(new ArrayList<Double>());
		}
		return sins;
	}
	
	private void fillSins(List<List<Double>> sins, List<String> lines) {
		for (String record : lines) {
			String data = record.split("\t")[1];
			String[] datums = data.split(",");
			for (int i = 0; i < datums.length; i++) {
				sins.get(i).add(Double.parseDouble(datums[i]));
			}
		}
		
	}
	
	
	private void fillSpecificSin(List<List<Double>> sins, List<String> lines, int index) {
		for (String record : lines) {
			String data = record.split("\t")[1];
			String[] datums = data.split(",");
			for (int i = 0; i < datums.length; i++) {
				double fillVal = Double.parseDouble(datums[i]);
				if(i!=index) {
					fillVal = 0.0;
				}
				sins.get(i).add(fillVal);
			}
		}
		
	}
	
	private String getIndividualJSONs(List<List<Double>> sins, List<String> lines) {
		int sins_length = sins.size();
		String json = "[";
		for (int sinIndex = 0; sinIndex < sins_length; sinIndex++) {
		fillSpecificSin(sins, lines, sinIndex);
		
		json = json + "[";
		for (int sinIndex2 = 0; sinIndex2 < sins_length; sinIndex2++) {
			List<Double> sin = sins.get(sinIndex2);
			String entry = "[";
			int xIncrement = 0;
			int datums_length = sin.size();
			for (int datumIndex = 0; datumIndex < datums_length; datumIndex++) {
				String datum = "{\"x\":" + xIncrement + ",\"y\":" + sin.get(datumIndex)
						* 80000 + ",\"colour\":\"" + getSinColour(sinIndex2) + "\"}";
				if (datumIndex != datums_length - 1) {
					datum = datum + ",";
				}
				entry = entry + datum;
				xIncrement = xIncrement + 1;
			}
			json = json + entry + "]";
			if (sinIndex2 != sins_length - 1) {
				json = json + ",";
			}

		}
		json = json + "]";
		if (sinIndex != sins_length - 1) {
			json = json + ",";
		}
		}
		json = json + "]";
		return json;
	}

	private String getCollatedJSON(List<List<Double>> sins, List<String> lines) {
		fillSins(sins, lines);
		
		String json = "[";
		int sins_length = sins.size();
		for (int sinIndex = 0; sinIndex < sins_length; sinIndex++) {
			List<Double> sin = sins.get(sinIndex);
			String entry = "[";
			int xIncrement = 0;
			int datums_length = sin.size();
			for (int datumIndex = 0; datumIndex < datums_length; datumIndex++) {
				String datum = "{\"x\":" + xIncrement + ",\"y\":" + sin.get(datumIndex)
						* 80000 + ",\"colour\":\"" + getSinColour(sinIndex) + "\"}";
				if (datumIndex != datums_length - 1) {
					datum = datum + ",";
				}
				entry = entry + datum;
				xIncrement = xIncrement + 1;
			}
			json = json + entry + "]";
			if (sinIndex != sins_length - 1) {
				json = json + ",";
			}

		}
		json = json + "]";
		return json;
	}

	private static String getSinColour(int index) {
		String colour = "#000000";
		switch (index) {
		case 0:
			colour = "#A60808"; // lust = red
			break;
		case 1:
			colour = "#0059BF"; // greed = blue
			break;
		case 2:
			colour = "#D10478"; // gluttoy = pink
			break;
		case 3:
			colour = "#318500"; // envy = green
			break;
		case 4:
			colour = "#701BE0"; // pride = purple
			break;
		case 5:
			colour = "#DB5800"; // wrath = orange
			break;
		case 6:
			colour = "#FFDE05"; // sloth = yellow
			break;
		}
		return colour;
	}
}
