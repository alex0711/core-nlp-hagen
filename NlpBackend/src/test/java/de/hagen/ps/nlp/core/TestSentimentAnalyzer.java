package de.hagen.ps.nlp.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.hagen.nlp.backend.core.SentimentAnalyzer;

public class TestSentimentAnalyzer {

	@Test
	public void testGoodSentiment() {
		String text = "That's been by far the greatest news on this topic for a long time";
		SentimentAnalyzer analyzer = new SentimentAnalyzer();
		int result = analyzer.getSentiment(text);
		assertTrue("is positive", result > 2);

	}

	@Test
	public void testBadSentiment() {
		String text = "Donald Trump is stupid and by far the worst US President in history";
		SentimentAnalyzer analyzer = new SentimentAnalyzer();
		int result = analyzer.getSentiment(text);
		assertTrue("is negative", result <= 0);

	}
	
}
