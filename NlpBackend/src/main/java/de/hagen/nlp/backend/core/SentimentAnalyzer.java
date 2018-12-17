package de.hagen.nlp.backend.core;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {

	private StanfordCoreNLP pipeline;

	public SentimentAnalyzer() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		props.put("threads", 10);
		pipeline = new StanfordCoreNLP(props);
		System.out.println("NLP Init done");
	}

	public int getSentiment(String line) {
		if (line == null || line.length() == 0) {
			return 2;
		}

		int mainSentiment = 0;
		int sentences = 0;

		Annotation annotation = pipeline.process(line);

		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentAnnotatedTree.class);
			if (sentence.toString().length() < 20) {
				continue;
			}
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			mainSentiment += sentiment;
			sentences++;
		}
		if (sentences > 0) {
			return (int) mainSentiment / sentences;
		}
		return -1;
	}

}
