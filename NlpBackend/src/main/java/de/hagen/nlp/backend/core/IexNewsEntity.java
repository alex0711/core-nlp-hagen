package de.hagen.nlp.backend.core;

public class IexNewsEntity implements IEXNews {

	private String headline;
	private String summary;

	public IexNewsEntity(String headline, String summary) {
		this.summary = summary;
		this.headline = headline;
	}

	public String getHeadline() {
		return this.headline;
	}

	public String getSummary() {
		return this.summary;
	}

	@Override
	public String toString() {
		return this.headline + ' ' + this.summary;
	}

}
