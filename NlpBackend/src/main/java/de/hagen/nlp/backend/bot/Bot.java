package de.hagen.nlp.backend.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.ejml.simple.SimpleMatrix;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.StemAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentClass;
import edu.stanford.nlp.sentiment.SentimentModel;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class Bot {

	private static Properties props;
	private static StanfordCoreNLP pipeline;
	private static boolean isInitialized;
	private ResponseGenerator responseGenerator = new ResponseGenerator();
	SentimentModel model = SentimentModel.loadSerialized("edu/stanford/nlp/models/sentiment/sentiment.ser.gz");

	public Bot() {
		if (isInitialized == false) {
			initialize();
			isInitialized = true;
		}
	}

	private static void initialize() {
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, pos, lemma, ner, sentiment, coref");
		props.put("threads", 10);
		props.put("ner.applyFineGrained", "0");

		// props.setProperty("ner.model",
		// "edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
		pipeline = new StanfordCoreNLP(props);
	}

	public String respond(String message) {
		String response = "";
		Annotation annotation = pipeline.process(message);
		ParsedStatement parsedStatement = parseMessage(message, annotation);
		response = responseGenerator.getResponse(parsedStatement);
		return response;

	}

	private ParsedStatement parseMessage(String message, Annotation annotation) {
		ParsedStatement parsedStatement = new ParsedStatement();

		parsedStatement.setText(message);

		for (CorefChain chain : annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
			CorefMention mention = chain.getRepresentativeMention();

			List<CorefMention> comentions = chain.getMentionsInTextualOrder();
			for (CorefMention corefMention : comentions) {
					System.out.println(corefMention);
			}

			// System.out.println("\t" + mention.toString());
			// System.out.println("\t" + comentions.toString());
		}

		Collection<CorefChain> corefs = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class).values();
		parsedStatement.setCorefs(corefs);

		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			String text = sentence.get(TextAnnotation.class);

			// System.out.println("---");
			// System.out.println("mentions");
			// for (Mention m :
			// sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
			// System.out.println("\t" + m);
			// }

			ParsedSentence parsedSentence = parsedStatement.addSentence();
			Tree tree = sentence.get(SentimentAnnotatedTree.class);

			parsedSentence.setText(sentence.toString());
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			parsedSentence.setSentiment(sentiment);

			List<Double> sentProbabilities = new ArrayList<Double>();
			{
				// System.out.println(RNNCoreAnnotations.getPredictionsAsStringList(tree));
				SimpleMatrix mat = RNNCoreAnnotations.getPredictions(tree);
				for (int i = 0; i < 4; ++i) {
					sentProbabilities.add(mat.get(i));
				}
			}

			SemanticGraph depGraph = sentence
					.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
			parsedSentence.setSemanticGraph(depGraph);

			for (SemanticGraphEdge edge : depGraph.edgeListSorted()) {

				IndexedWord word = edge.getGovernor();
				GrammaticalRelation rel = edge.getRelation();
				String pos = edge.getGovernor().get(PartOfSpeechAnnotation.class);

				String sent = edge.getGovernor().get(SentimentClass.class);
			}

			parsedSentence.setSentimentProbabilities(sentProbabilities);
		    
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);
				String ner = token.get(NamedEntityTagAnnotation.class);
				String stemma = token.getString(StemAnnotation.class);

				int end = token.get(CharacterOffsetEndAnnotation.class);
				int start = token.get(CharacterOffsetBeginAnnotation.class);

				if (!ner.isEmpty()) {
					word = word.replace(".", "");
				}

				ParsedToken parsedToken = parsedSentence.addToken();

				boolean knownWord = model.wordVectors.containsKey(token.word());
				if (knownWord) {
					SimpleMatrix res = model.wordVectors.get("good");
				}
				// Annotation wordAnnotation = pipeline.process(token.word());
				// for (CoreMap wordSent :
				// wordAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				// Tree wordTree = sentence.get(SentimentAnnotatedTree.class);
				// int tokenSentiment = RNNCoreAnnotations.getPredictedClass(wordTree);
				// System.out.println(tokenSentiment);
				// parsedToken.setSentiment(tokenSentiment);

				// SimpleMatrix res = model.wordVectors.get("good");
				// int tokenSentiment = (int) (res.elementSum() / res.getNumElements());
				//
				// StringBuilder output = new StringBuilder();
				// output.append("Word vectors\n");
				// for (Map.Entry<String, SimpleMatrix> dd : model.wordVectors.entrySet()) {
				// output.append("'" + dd.getKey() + "'");
				// output.append("\n");
				// output.append(NeuralUtils.toString(dd.getValue(), "%.8f"));
				// output.append("\n");
				// }
				//
				// System.out.println(output.toString());
				// }
				// }

				parsedToken.setWord(word);
				parsedToken.setNamedEntityRecognition(ner);
				parsedToken.setPartOfSpeech(pos);
				parsedToken.setLemmatization(lemma);
				parsedToken.setStemmatization(stemma);
				parsedToken.setEndIndex(end);
				parsedToken.setStartIndex(start);
			}
		}
		return parsedStatement;
	}

}
