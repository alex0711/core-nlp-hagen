package de.hagen.nlp.backend.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.stanford.nlp.semgraph.SemanticGraph;

public class ParsedSentence {

	private int sentiment;
	private List<ParsedToken> tokens = new ArrayList<ParsedToken>();
	private List<Double> sentimentProbabilities;
	private SemanticGraph semanticGraph;
	private String text;

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	
	public int getSentiment() {
		return this.sentiment;
	}

	public ParsedToken addToken() {
		ParsedToken token = new ParsedToken();
		getTokens().add(token);
		return token;
	}

	public boolean isQuestion() {
		String lastToken = getTokens().get(getTokens().size() - 1).getLemmatization();
		if (lastToken.equals("?")) {
			return true;
		}
		return false;

	}

	public boolean isGoodbye() {
		Optional<ParsedToken> token = getTokens().stream()
				.filter(p -> p.getLemmatization().equals("bye") || p.getLemmatization().equals("goodbye")).findAny();
		if (token.isPresent()) {
			return true;
		}
		return false;
	}

	public boolean isGreeting() {
		Optional<ParsedToken> token = getTokens().stream()
				.filter(p -> p.getLemmatization().equals("hello") || p.getLemmatization().equals("hi")).findAny();
		if (token.isPresent()) {
			return true;
		}
		return false;
	}

	public String getNer() {
		Optional<ParsedToken> token = getTokens().stream().filter(p -> p.getNamedEntityRecognition().equals("PERSON"))
				.findAny();
		if (token.isPresent()) {
			return token.get().getWord();
		}
		return "";

	}

	public void setSentimentProbabilities(List<Double> sentProbabilities) {
		this.sentimentProbabilities = sentProbabilities;
	}

	public void setSemanticGraph(SemanticGraph depGraph) {
		this.semanticGraph = depGraph;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public SemanticGraph getSetSemanticGraph() {
		return semanticGraph;
	}

	public List<Double> getSentimentProbabilities() {
		return sentimentProbabilities;
	}

	public List<ParsedToken> getTokens() {
		return tokens;
	}

	public void setTokens(List<ParsedToken> tokens) {
		this.tokens = tokens;
	}

}
