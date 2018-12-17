package de.hagen.nlp.backend.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentClass;

public class ParsedStatement {

	List<ParsedSentence> sentences = new ArrayList<ParsedSentence>();
	private Collection<CorefChain> corefs;
	private String text;

	public void addToken() {

	}

	public ParsedSentence addSentence() {
		ParsedSentence sentence = new ParsedSentence();
		sentences.add(sentence);
		return sentence;
	}

	public boolean isGoodbye() {

		ParsedSentence sentence = getFarewell();
		if (!(sentence == null)) {
			return true;
		}
		return false;
	}

	private ParsedSentence getFarewell() {
		Optional<ParsedSentence> sentence = sentences.stream().filter(p -> p.isGoodbye()).findAny();
		if (sentence.isPresent()) {
			return sentence.get();
		}
		return null;
	}

	public boolean isGreeting() {
		ParsedSentence sentence = getGreeting();
		if (!(sentence == null)) {
			return true;
		}
		return false;
	}

	public ParsedSentence getGreeting() {
		Optional<ParsedSentence> sentence = sentences.stream().filter(p -> p.isGreeting()).findAny();
		if (sentence.isPresent()) {
			return sentence.get();
		}
		return null;
	}

	public boolean isQuestion() {
		ParsedSentence sentence = getQuestion();
		if (!(sentence == null)) {
			return true;
		}
		return false;
	}

	public ParsedSentence getQuestion() {
		Optional<ParsedSentence> sentence = sentences.stream().filter(p -> p.getText().endsWith("?")).findAny();
		if (sentence.isPresent()) {
			return sentence.get();
		}
		return null;
	}

	public void setText(String message) {
		this.text = message;
	}

	public boolean isAskingAboutBot() {
		ParsedSentence question = getQuestion();

		IndexedWord root = question.getSetSemanticGraph().getFirstRoot();

		List<SemanticGraphEdge> rootEdges = question.getSetSemanticGraph().getOutEdgesSorted(root);

		boolean hasPronoun = false;
		boolean hasRelation = false;
		String relationTarget = "";

		for (SemanticGraphEdge edge : rootEdges) {

			IndexedWord governor = edge.getGovernor();
			String posGov = governor.get(PartOfSpeechAnnotation.class);
			String lemmaGov = governor.get(LemmaAnnotation.class);
			if (((posGov.equals("WP") || posGov.equals("WDT")))
					&& (lemmaGov.equals("who") || lemmaGov.equals("what"))) {
				hasPronoun = true;
			}

			String relation = edge.getRelation().getShortName();
			if (relation.equals("cop")) {
				hasRelation = true;
			}

			IndexedWord target = edge.getTarget();
			String posTarget = target.get(PartOfSpeechAnnotation.class);
			String lemmaTarget = target.get(LemmaAnnotation.class);

			if ((posTarget.contains("PRP") || posTarget.contains("NN"))
					&& ((lemmaTarget.equals("you") || lemmaTarget.equals("name")))) {
				relationTarget = lemmaTarget;
			}

		}

		if ((hasPronoun && hasRelation) && (relationTarget.equals("you") || relationTarget.equals("name"))) {
			return true;
		}

		return false;

	}

	public Collection<CorefChain> getCorefs() {
		return corefs;
	}

	public void setCorefs(Collection<CorefChain> corefs) {
		this.corefs = corefs;
	}

	public List<ParsedSentence> getSentences() {
		return this.sentences;
	}

}
