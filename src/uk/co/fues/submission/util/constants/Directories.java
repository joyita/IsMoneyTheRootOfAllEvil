package uk.co.fues.submission.util.constants;

public enum Directories {

	
	SIN_DIRECTORY("/sins/training");
	
	String location;
	Directories(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
}
