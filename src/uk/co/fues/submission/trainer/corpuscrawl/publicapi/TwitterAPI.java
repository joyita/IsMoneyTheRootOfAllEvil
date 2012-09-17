package uk.co.fues.submission.trainer.corpuscrawl.publicapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.util.parsing.cleansing.TweetSpellCheck;
import uk.co.fues.submission.vocabulary.Sins;

@Component
public class TwitterAPI {

	Logger log = LoggerFactory.getLogger(getClass());
	Twitter twitter;
	TweetSpellCheck spellchecker;

	public TwitterAPI() {
		twitter = new TwitterFactory().getInstance();
		spellchecker = new TweetSpellCheck();
	}

	public void retrieveTweets() {
		for (Sins sin_ : Sins.values()) {
			List<String> sins = sin_.getVocab();
			for (String sin : sins) {
				try {
					Query query = new Query(sin);
					query.setLang("en");
					QueryResult result;
					result = twitter.search(query);
					List<Tweet> tweets = result.getTweets();
					for (Tweet tweet : tweets) {
						String correctedTweet = spellchecker.spellcheck(tweet.getText());
						System.out.println("Original:   " + tweet.getText());
						System.out.println("Corrected:  " + correctedTweet);
						File file = new File(
								Directories.SIN_UNPARSED_DIRECTORY
										.getLocation()
										+ "/"
										+ sin_.name()
										+ "/" + tweet.getFromUser());
						FileUtils.writeStringToFile(file, tweet.getText());
					}

				} catch (TwitterException te) {
					log.error("", te);
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}

	}
}
