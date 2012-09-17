package uk.co.fues.submission.util.constants;

public enum Directories {

	
	SIN_DIRECTORY("/sins/training/parsed"),
	SIN_UNPARSED_DIRECTORY("/sins/training/unparsed");
	
	String location;
	Directories(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
}
