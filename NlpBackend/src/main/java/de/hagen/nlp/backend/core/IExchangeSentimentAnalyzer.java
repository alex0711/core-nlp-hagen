package de.hagen.nlp.backend.core;

public interface IExchangeSentimentAnalyzer {
	
	public IexSentimentEntity getStockSentiment(String tickerSymbol);
	
	public IexSentimentEntity getMarketSentiment();

}
