package uk.co.fues.submission.classifier.nlp;

import java.util.Enumeration;
import java.util.Vector;

import weka.core.Option;
import weka.core.tokenizers.Tokenizer;

public class PhraseTokeniser extends Tokenizer {

	@Override
	public String getRevision() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String globalInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object nextElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tokenize(String s) {
		// TODO Auto-generated method stub

	}

	  public Enumeration listOptions() {
		    Vector	result;
		    
		    result = new Vector();
		    
		    result.addElement(new Option(
		        "\tThe delimiters to use\n"
			+ "\t(default ' \\r\\n\\t.,;:'\"()?!').",
		        "delimiters", 1, "-delimiters <value>"));
		    
		    return result.elements();
		  }

}
