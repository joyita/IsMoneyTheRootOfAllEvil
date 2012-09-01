package uk.co.fues.submission.vocabulary;

import java.util.Arrays;
import java.util.List;

public enum Money {
	
	MONEY(Arrays.asList("money", "finance", "cost", "cash", "bank", "wage", "salary", "earning", "dollars", "pay", "price", "almighty dollar", "banknote", "bankroll", 
			"bill", "bread", "bucks", "capital", "cash", "check", "chips", "coin", "coinage", "dough", "finances", "fund", "funds", "gold", "gravy", "greenback", 
			"hard cash", "legal tender", "loot", "exchange", "pay", "payment", "pesos", "property", "resources", "riches", "roll", "salary", "silver", "specie", 
			"treasure", "wad", "wealth", "capital", "stocks", "cent", "cheque", "dime", "gold", "bullion", "coupon", "note", "slip", "coin", "sterling", "pound"));
	
	private List<String> vocab;
	
	Money(List<String> vocab) {
		this.vocab = vocab;
	}
	
	public List<String> getVocab() {
		return vocab;
	}

}
