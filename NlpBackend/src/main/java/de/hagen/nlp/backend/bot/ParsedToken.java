package de.hagen.nlp.backend.bot;

public class ParsedToken {

	private String ner;
	private String pos;
	private String lemma;
	private String stemma;
	private int sentiment;
	private String word;
	private int startIndex;
	private int endIndex;

	public void setNamedEntityRecognition(String ner) {
		this.ner = ner;
	}

	public void setPartOfSpeech(String pos) {
		this.pos = pos;
	}

	public void setLemmatization(String lemma) {
		this.lemma = lemma;
	}

	public String getLemmatization() {
		return lemma;
	}

	public String getPartOfSpeech() {
		return pos;
	}

	public String getNamedEntityRecognition() {
		return ner;
	}

	public void setStemmatization(String stemma) {
		this.stemma = stemma;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public String getWord() {
		return this.word;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
