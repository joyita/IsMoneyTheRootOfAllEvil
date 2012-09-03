package uk.co.fues.submission.trainer.corpuscrawl.publicapi;

import java.util.List;

public interface TagAPI {

	void getUrl(String tag, List<String> urls);
	
}
