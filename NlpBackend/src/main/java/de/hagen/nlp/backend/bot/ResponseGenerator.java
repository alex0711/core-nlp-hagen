package de.hagen.nlp.backend.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;

public class ResponseGenerator {

	List<ParsedStatement> statements = new ArrayList<ParsedStatement>();
	private boolean greeted;
	private boolean explainedIdentity;
	private String name;

	public String getResponse(ParsedStatement parsedStatement) {

		String response = "";

		boolean isGreeting = parsedStatement.isGreeting();
		if (isGreeting) {
			response = getGreetingResponse(parsedStatement);
		}

		boolean isGoodbye = parsedStatement.isGoodbye();
		if (isGoodbye) {
			response = getFarewellResponse(parsedStatement);
		}

		boolean isQuestion = parsedStatement.isQuestion();
		if (isQuestion) {
			response = answerQuestion(parsedStatement);
		}

		if (response.isEmpty() && parsedStatement.getCorefs().size() == 0) {
			String question = createQuestion(parsedStatement);
			if (!question.isEmpty()) {
				response = question;
			}
		}

		if (response.isEmpty()) {
			response = DefaultResponses.getQuestionResponse();
		}

		statements.add(parsedStatement);
		return response;
	}

	private String createQuestion(ParsedStatement statement) {

		List<ParsedSentence> sentences = statement.getSentences();
		String mainSubject = "";

		for (ParsedSentence sentence : sentences) {
			List<ParsedToken> tokens = sentence.getTokens();
			List<ParsedToken> nerTokens = tokens.stream()
					.filter(t -> !t.getNamedEntityRecognition().isEmpty() && !t.getNamedEntityRecognition().equals("O"))
					.collect(Collectors.toList());

			Collection<CorefChain> corefsQuestion = statement.getCorefs();
			List<SemanticGraphEdge> edges = sentence.getSetSemanticGraph().edgeListSorted();

			Collection<IndexedWord> roots = sentence.getSetSemanticGraph().getRoots();
			for (IndexedWord root : roots) {
				List<SemanticGraphEdge> rootEdges = sentence.getSetSemanticGraph().getOutEdgesSorted(root);
				boolean isTalkingAboutSelf = false;
				String adjective = "";
				String verb = "";
				List<IndexedWord> verbWords = new ArrayList<IndexedWord>();
				List<IndexedWord> adjWords = new ArrayList<IndexedWord>();

				for (SemanticGraphEdge edge : rootEdges) {

					IndexedWord governor = edge.getGovernor();

					String posGov = governor.get(PartOfSpeechAnnotation.class);
					String lemmaGov = governor.get(LemmaAnnotation.class);

					GrammaticalRelation relation = edge.getRelation();
					String relName = relation.getLongName();

					IndexedWord target = edge.getTarget();

					String posTarget = target.get(PartOfSpeechAnnotation.class);
					String lemmaTarget = target.get(LemmaAnnotation.class);

					if (posGov.startsWith("VB")) {
						verb = lemmaGov;
						verbWords = sentence.getSetSemanticGraph().getChildList(target);
					}

					if (posTarget.startsWith("JJ") || posTarget.startsWith("VB")) {
						adjective = lemmaTarget;
						adjWords = sentence.getSetSemanticGraph().getChildList(target);

					}

					if (posTarget.equals("PRP") && lemmaTarget.equals("I")) {
						isTalkingAboutSelf = true;
					}

				}

				if (isTalkingAboutSelf && !verb.isEmpty() && !adjective.isEmpty()) {
					if (verbWords.stream().anyMatch(p -> p.get(PartOfSpeechAnnotation.class).equals("TO"))) {
						verb = verb + " to";
					}
					for (IndexedWord word : adjWords) {
						if (!word.get(PartOfSpeechAnnotation.class).equals("PRP")
								&& !word.get(PartOfSpeechAnnotation.class).equals("TO")) {
							if (word.get(PartOfSpeechAnnotation.class).startsWith("RB")) {// wenn prev mit NN startet
																							// dann nicht vorher
								adjective = word.get(TextAnnotation.class) + " " + adjective;
							} else {
								adjective = adjective + " " + word.get(TextAnnotation.class);
							}
						}
					}

					return "Why do you " + verb + " " + adjective + "?";
				}
			}

			String verbSubject = "";
			if (sentence.getSentiment() > 2) {
				verbSubject = "like";
			}
			if (sentence.getSentiment() < 2) {
				verbSubject = "dislike";
			}

			if (nerTokens.size() > 0) {
				mainSubject = nerTokens.get(0).getWord();
			}

			Optional<ParsedToken> nountoken = tokens.stream().filter(p -> p.getPartOfSpeech().startsWith("NN"))
					.findFirst();
			if (nountoken.isPresent()) {
				mainSubject = nountoken.get().getWord();
			}

			return "Do you " + verbSubject + " " + mainSubject + "?";
		}
		return "";
	}

	private String answerQuestion(ParsedStatement parsedStatement) {

		if (parsedStatement.isAskingAboutBot()) {
			return getIdentityResponse();
		}

		String response = resolveQuestion(parsedStatement);

		if (response.isEmpty()) {
			return "You haven't told me that yet.";
		} else {
			return "You told me, it's " + response;
		}
	}

	private String getIdentityResponse() {
		if (explainedIdentity) {
			return DefaultResponses.getToldIdentityResponse();
		} else {
			explainedIdentity = true;
			return DefaultResponses.getIdentityResponse();
		}
	}

	private String resolveQuestion(ParsedStatement parsedStatement) {

		QuestionResponseGen gen = new QuestionResponseGen(parsedStatement);
		return gen.respondToQuestion(statements);

	}

	private String getFarewellResponse(ParsedStatement parsedStatement) {
		return DefaultResponses.getFarewell();
	}

	private String getGreetingResponse(ParsedStatement parsedStatement) {
		String name = parsedStatement.getGreeting().getNer();

		if (!name.isEmpty()) {
			this.name = name;
		}

		if (greeted) {
			return DefaultResponses.getRepeatedStatementResponse(parsedStatement.getGreeting().getNer());
		}

		greeted = true;
		if (!this.name.isEmpty()) {
			return DefaultResponses.getGreetingResponse(this.name);
		} else {
			return DefaultResponses.getAskForNameGreeting();
		}
	}

}
