package uk.co.fues.submission.vocabulary;

import java.util.Arrays;
import java.util.List;

public enum Sins {
	
	LUST(Arrays.asList( "craving", "cupidity", "desire", "erotic", "eroticism", "excitement", "fervor", "greed", "hunger", "itch", "lasciviousness", "lechery", "lewd", "libido", 
			"longing", "prurience", "pruriency", "salacious", "salacity", "sensual", "sensuality", "urge", "wanton", "weak", "yen", "relish", "smoulder", "simmer","urge")),
	GLUTTONY(Arrays.asList( "hog", "belly", "gorge",  "overeat", "engorge", "guzzle")),
	GREED(Arrays.asList(  "impatient", "intent", "keen", "longing", "pining", "restless", "solicitous", "wishful")),
	ENVY(Arrays.asList("jealous", "malice", "maliciousness", "malignity", "opposition", 
			"prejudice", "resentfulness", "resentment", "rivalry", "spite")),
	PRIDE(Arrays.asList(
			"glory", "high", "overbear", "pique", "plume", "prance", "preen", "presume", "revel", "strut", "swagger", "swell", "vaunt", "imperious", "super", "mega", "lofty", 
			"pompous", "overbear", "scorn", "smug", "cocky", "sass", "narcissist", "elite", "finest", "best", "top")),
	WRATH(Arrays.asList( "indignation", "ire", "irritation", "mad", "madness", "offense", "resentment", "stew", "storm", "temper", "vex", "pest", "bother", "scorn", "miff", "outburst", "ire", "fire", "huff", "gall", "spite ")),
	SLOTH(Arrays.asList( "lethargy", "loafing", 
			"loitering", "otiosity", "own sweet time", "pottering", "shiftlessness", "slothfulness", "slouch", "slowness", "sluggish", "stupor", "slow", "trifling", "truancy","vegetating", "idle", "drowsy", "passive", "langour", "listless", "laze")),
//			LUST2(Arrays.asList("appetite", "passion", "lust", "desire", "craving", "yearning", "thirst",
//					"animalism", "aphrodisia", "appetence", "appetition", "avid", "carnal", "concupiscence", "covetousness", "covet", "craving", "cupidity", "desire", "erotic", "eroticism", "excitement", "fervor", "greed", "hunger", "itch", "lasciviousness", "lechery", "lewd", "libido", 
//					"longing", "prurience", "pruriency", "salacious", "salacity", "sensual", "sensuality", "urge", "wanton", "weak", "yen", "relish", "smoulder", "simmer","urge"));
				MONEY(Arrays.asList("rich", "gold", "tender", "loot", "riches", "roll", "salary", "silver", 
			"treasure", "wad", "wealth", "luxury", "bullion", "affluent", "millionaire", "billionaire", "billions", "loaded", "opulent", "prosperous",
			"profit", "profitable", "lavish", "luxurious"));
//	LOVE_MONEY(Arrays.asList("money", "finance", "cost", "cash", "bank", "wage", "salary", "earning", "dollars", "pay", "price",  "banknote", "bankroll", 
//			"bill", "bread", "bucks", "capital", "cash", "check", "chips", "coin", "coinage", "dough", "finances", "fund", "funds", "gold", "gravy", "greenback", 
//			 "tender", "loot", "exchange", "pay", "payment", "pesos", "property", "resources", "riches", "roll", "salary", "silver", "specie", 
//			"treasure", "wad", "wealth", "capital", "stocks", "cent", "cheque", "dime", "gold", "bullion", "coupon", "note", "slip", "coin", "sterling", "pound",
//			"greed", "want", "voracious", "greedy", "excess", "avarice", "hunger", "insatiable", "gluttony", "demand", "urge", "miser", "self", "gobbling", "grab", "feverent", "acquisitive", "agog", "ambitious", "antsy", "appetent", "ardent"
//			,"athirst", "avid", "breathless", "covetous", "desiring", "hankering", "heated", "hot", "impatient", "intent", "keen", "longing", "pining", "restless", "solicitous", "wishful", "love"));

	
	private List<String> vocab;
		
	Sins(List<String> vocab) {
		this.vocab = vocab;
	}
	
	public List<String> getVocab() {
		return vocab;
	}
	

	
}
