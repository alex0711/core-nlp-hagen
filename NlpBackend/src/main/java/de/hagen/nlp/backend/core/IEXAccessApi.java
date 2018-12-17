package de.hagen.nlp.backend.core;

import java.util.List;

public interface IEXAccessApi {

	public List<IEXNews> getStockNews(String tickerSymbol);
	public List<IEXNews> getMarketNews();
	
}
