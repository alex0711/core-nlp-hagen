package de.hagen.ps.nlp.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.hagen.nlp.backend.core.ExchangeSentimentAnalyzer;
import de.hagen.nlp.backend.core.IExchangeSentimentAnalyzer;
import de.hagen.nlp.backend.core.IexSentimentEntity;

public class TestIexSentiment {

	private IExchangeSentimentAnalyzer analyzer = new ExchangeSentimentAnalyzer();

	@Test
	public void testMarketSentiment() {
		IexSentimentEntity sentiment = analyzer.getMarketSentiment();
		assertTrue(sentiment != null);
	}

	@Test
	public void testStockSentiment() {
		IexSentimentEntity sentiment = analyzer.getStockSentiment("GOOG");
		assertTrue(sentiment != null);
	}

}
