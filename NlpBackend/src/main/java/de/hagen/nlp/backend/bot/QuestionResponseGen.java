package de.hagen.nlp.backend.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

public class QuestionResponseGen {

	private ParsedStatement statement;

	public QuestionResponseGen(ParsedStatement statement) {
		this.statement = statement;
	}

	public String respondToQuestion(List<ParsedStatement> statements) {

		ParsedSentence question = statement.getQuestion();

		IndexedWord root = question.getSetSemanticGraph().getFirstRoot();
		List<SemanticGraphEdge> rootEdges = question.getSetSemanticGraph().getOutEdgesSorted(root);

		List<SemanticGraphEdge> nouns = rootEdges.stream()
				.filter(p -> p.getTarget().get(PartOfSpeechAnnotation.class).equals("NN")).collect(Collectors.toList());

		List<ParsedToken> tokens = question.getTokens().stream()
				.filter(p -> p.getPartOfSpeech().equals("NN") || p.getNamedEntityRecognition().length() > 2)
				.collect(Collectors.toList());

		Collection<CorefChain> corefsQuestion = statement.getCorefs();

		for (ParsedStatement statement : statements) {
			Collection<CorefChain> corefs = statement.getCorefs();

			for (CorefChain corefChain : corefs) {

				CorefMention mention = corefChain.getRepresentativeMention();
				List<CorefMention> comentions = corefChain.getMentionsInTextualOrder();

				Optional<CorefMention> maxMent = comentions.stream()
						.max((x, y) -> getSimiliarityIndex(question, statement, x)
								- getSimiliarityIndex(question, statement, y));

				if (maxMent.isPresent()) {
					int sim = getSimiliarityIndex(question, statement, maxMent.get());
					if (sim > 50) {
						return getStoredAnswer(question, statement, maxMent.get());
					}
				}
			}
		}

		return "";

	}

	private String getStoredAnswer(ParsedSentence question, ParsedStatement statement, CorefMention corefMention) {

		List<ParsedSentence> sentences = statement.getSentences();
		ParsedSentence sentence = sentences.get(corefMention.sentNum - 1);

		Optional<ParsedToken> whAdverbOpt = question.getTokens().stream().findFirst()
				.filter(p -> p.getPartOfSpeech().equals("WRB"));

		Optional<ParsedToken> tokenDate = sentence.getTokens().stream()
				.filter(p -> p.getNamedEntityRecognition().equals("DATE")).findFirst();

		Optional<ParsedToken> tokenPerson = sentence.getTokens().stream()
				.filter(p -> p.getNamedEntityRecognition().equals("PERSON")).findFirst();

		Optional<ParsedToken> tokenLocation = sentence.getTokens().stream()
				.filter(p -> p.getNamedEntityRecognition().equals("LOCATION")).findFirst();

		for (ParsedSentence parsedSentence : sentences) {
			if (!tokenLocation.isPresent()) {
				tokenLocation = parsedSentence.getTokens().stream()
						.filter(p -> p.getNamedEntityRecognition().equals("LOCATION")).findFirst();
			}
		}

		String result = "";

		result = corefMention.mentionSpan;

		if (whAdverbOpt.isPresent()) {
			switch (whAdverbOpt.get().getLemmatization()) {
			case "when":
				if (tokenDate.isPresent()) {
					result = tokenDate.get().getWord();
				}
				break;
			case "who":
				if (tokenPerson.isPresent()) {
					result = tokenPerson.get().getWord();
				}
				break;
			case "where":
				if (tokenLocation.isPresent()) {
					result = tokenLocation.get().getWord();
				}
				break;
			}
		}

		return result;

	}

	private int getSimiliarityIndex(ParsedSentence question, ParsedStatement statement, CorefMention corefMention) {

		if (corefMention.mentionSpan.isEmpty()) {
			return 0;
		}

		Optional<ParsedToken> whAdverbOpt = question.getTokens().stream().findFirst()
				.filter(p -> p.getPartOfSpeech().equals("WRB"));

		List<ParsedSentence> sentences = statement.getSentences();
		ParsedSentence sentence = sentences.get(corefMention.sentNum - 1);

		Optional<ParsedToken> tokenDate = sentence.getTokens().stream().filter(p -> p.getPartOfSpeech().equals("DATE"))
				.findFirst();

		int fromIndex = corefMention.startIndex - 1;
		int endIndex = corefMention.endIndex - 1;
		List<ParsedToken> relevantTokens = new ArrayList<ParsedToken>(
				sentence.getTokens().subList(fromIndex, endIndex));

		List<ParsedToken> containedTokens = new ArrayList<ParsedToken>();

		for (ParsedToken token : relevantTokens) {
			Optional<ParsedToken> tok = sentence.getTokens().stream()
					.filter(p -> p.getLemmatization().equals(token.getLemmatization())).findFirst();
			if (tok.isPresent()) {
				containedTokens.add(tok.get());
			}
		}

		boolean whAdverbDoesMatch = false;

		if (whAdverbOpt.isPresent()) {
			String whAdverbLemma = whAdverbOpt.get().getLemmatization();
			if (whAdverbLemma.equals("when")) {
				if (tokenDate.isPresent()) {
					whAdverbDoesMatch = true;
				}
			}
		}

		int base = relevantTokens.size() - 2;
		int cont = containedTokens.size();

		if (base < 1) {
			return 0;
		} else {
			double rate = cont * 100 / base;
			if (whAdverbDoesMatch == true) {
				rate = rate - 10;
			}
			return (int) rate;

		}
	}

}
