package uk.co.fues.submission.vocabulary;

import java.util.Arrays;
import java.util.List;

public enum Sins {
	
	LUST(Arrays.asList("appetite", "passion", "lust", "desire", "craving", "yearning", "thirst",
			"animalism", "aphrodisia", "appetence", "appetition", "avid", "carnal", "concupiscence", "covetousness", "covet", "craving", "cupidity", "desire", "erotic", "eroticism", "excitement", "fervor", "greed", "hunger", "itch", "lasciviousness", "lechery", "lewd", "libido", 
			"longing", "prurience", "pruriency", "salacious", "salacity", "sensual", "sensuality", "urge", "wanton", "weak", "yen", "relish", "smoulder", "simmer","urge")),
	GLUTTONY(Arrays.asList("indulge","excess", "gluttony", "more", "endless", "limitless", "infinite", "greed", "good living", "high living", "edacity", "gulosity", "crapulence", "guzzling", "blow out", "feast",
			"gourmand", "glutton", "cormorant", "hog", "belly", "gorge",  "overeat", "engorge", "eat one's fill", "cram", "stuff", "guzzle")),
	GREED(Arrays.asList( "greed", "want", "voracious", "greedy", "excess", "avarice", "hunger", "insatiable", "gluttony", "demand", "urge", "miser", "self", "gobbling", "grab", "feverent", "acquisitive", "agog", "ambitious", "antsy", "appetent", "ardent"
			,"athirst", "avid", "breathless", "champing at the bit", "covetous", "desiring", "hankering", "heated", "hot", "impatient", "intent", "keen", "longing", "pining", "restless", "solicitous", "wishful")),
	ENVY(Arrays.asList("jealous, jealousy", "envy", "green eyed", "envious", "backbiting", "coveting", "covetousness", "enviousness", "evil eye", "green-eyed monster", "grudge", 
			"grudging", "grudgingness", "hatred", "heartburn", "ill will", "invidiousness", "jaundiced eye", "malevolence", "malice", "maliciousness", "malignity", "opposition", 
			"prejudice", "resentfulness", "resentment", "rivalry", "spite")),
	PRIDE(Arrays.asList("rightful", "want", "mine", "proud", "boast", "brag", "congratulate", "crow", "exult", "felicitate", "flatter", "gasconade", 
			"glory", "high", "overbear", "pique", "plume", "prance", "preen", "presume", "puff up", "revel", "strut", "swagger", "swell", "vaunt", "imperious", "super", "mega", "lofty", 
			"pompous", "overbear", "scorn", "smug", "cocky", "sass", "narcissist", "elite", "finest", "best", "top")),
	WRATH(Arrays.asList("rage, anger", "angry", "resentment", "mad", "revenge", "vengance", "indignant", "acrimony", "asperity", "boiling point", "conniption", "dander", "displeasure", "exasperation", "flare", 
			"fury", "hate", "hatefulness", "huff", "indignation", "ire", "irritation", "mad", "madness", "offense", "resentment", "stew", "storm", "temper", "vex", "pest", "bother", "scorn", "miff", "outburst", "ire", "fire", "huff", "gall", "spite ")),
	SLOTH(Arrays.asList("sloth, lazy", "easy", "shortcut", "quick", "fast", "easily", "dawdling", "dilly-dally", "dormant", "droning", "goof-off", "inactivity", "indolent", "inertia", "joblessness", "laze", "lazing", "leisure", "lethargy", "loafing", 
			"loitering", "otiosity", "own sweet time", "pottering", "shiftlessness", "slothfulness", "slouch", "slowness", "sluggish", "stupor", "slow", "trifling", "truancy","vegetating", "idle", "drowsy", "passive", "langour", "listless", "laze"));

	private List<String> vocab;
		
	Sins(List<String> vocab) {
		this.vocab = vocab;
	}
	
	public List<String> getVocab() {
		return vocab;
	}
	

	
}
