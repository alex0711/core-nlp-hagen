package de.hagen.nlp.backend.core;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExchangeSentimentAnalyzer implements IExchangeSentimentAnalyzer {

	private static IEXAccessApi iex = new IEXNewsApi();
	private static SentimentAnalyzer analyzer = new SentimentAnalyzer();

	public IexSentimentEntity getStockSentiment(String tickerSymbol) {
		List<IEXNews> news = iex.getStockNews(tickerSymbol);
		IexSentimentEntity result = analyzeSentiment(news);
		result.setTickerSymbol(tickerSymbol);
		return result;
	}

	private IexSentimentEntity analyzeSentiment(List<IEXNews> news) {
		IexSentimentEntity result = new IexSentimentEntity();
		result.setDate(new Date());
		result.setNumNews(news.size());

		for (Iterator<IEXNews> iterator = news.iterator(); iterator.hasNext();) {

			IEXNews iexNews = iterator.next();
			int sentiment = analyzer.getSentiment(iexNews.toString());
			if (sentiment == -1) {
				continue;
			}
			
			int length = iexNews.toString().length();

			if (result.getMaxSentiment() < sentiment && sentiment > 2) {
				result.setMaxSentiment(sentiment);
				result.setMostPositiveNews(iexNews.getHeadline());
			}

			if ((result.isMinSentSet() && sentiment <= result.getMinSentiment())
					|| (!result.isMinSentSet() && sentiment < 2)) {
				result.setMinSentiment(sentiment);
				result.setMostNegativeNews(iexNews.getHeadline());
			}

			int currentAverage = result.getAverageSentiment();
			int currentSumChars = result.getSumNewsLength();

			int avgSentiment = 0;
			int sumChars = 0;

			if (currentAverage == 0) {
				avgSentiment = sentiment;
				sumChars = length;
			} else {
				avgSentiment = (currentAverage * currentSumChars + sentiment / length) / (currentSumChars + length);
				sumChars = currentSumChars + length;
			}

			result.setAverageSentiment(avgSentiment);
			result.setSumNewsLength(sumChars);

		}

		if (news.size() > 0) {
			persistResult(result);
		}

		return result;
	}

	private void persistResult(IexSentimentEntity result) {
		// File file = new File("stockSent.xml");
		// JAXBContext jaxbContext;
		// try {
		// jaxbContext = JAXBContext.newInstance(IexSentimentEntity.class);
		//
		// Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// List<IexSentimentEntity> persState = (List<IexSentimentEntity>)
		// unmarshaller.unmarshal(file);
		// persState.add(result);
		//
		// Marshaller marshaller = jaxbContext.createMarshaller();
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// marshaller.marshal(persState, file);
		// } catch (JAXBException e) {
		// e.printStackTrace();
		// }

	}

	public IexSentimentEntity getMarketSentiment() {
		List<IEXNews> news = iex.getMarketNews();
		IexSentimentEntity result = analyzeSentiment(news);
		result.setTickerSymbol("");
		return result;
	}

}
