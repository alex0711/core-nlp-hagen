package de.hagen.nlp.backend.core;

import java.util.Date;
import javax.persistence.*;

@Entity
public class IexSentimentEntity {

	@Id
	private Date date;
	@Id
	private String tickerSymbol;

	private int minSentiment;
	private boolean minSentSet;
	private int maxSentiment;
	private String mostPositiveNews;
	private String mostNegativeNews;
	private int averageSentiment;
	private int numNews;
	private int sumNewsLength;

	public int getNumNews() {
		return numNews;
	}

	public void setNumNews(int numNews) {
		this.numNews = numNews;
	}

	public int getAverageSentiment() {
		return averageSentiment;
	}

	public void setAverageSentiment(int averageSentiment) {
		this.averageSentiment = averageSentiment;
	}

	public String getMostNegativeNews() {
		return mostNegativeNews;
	}

	public void setMostNegativeNews(String mostNegativeNews) {
		this.mostNegativeNews = mostNegativeNews;
	}

	public String getMostPositiveNews() {
		return mostPositiveNews;
	}

	public void setMostPositiveNews(String mostPositiveNews) {
		this.mostPositiveNews = mostPositiveNews;
	}

	public int getMaxSentiment() {
		return maxSentiment;
	}

	public void setMaxSentiment(int maxSentiment) {
		this.maxSentiment = maxSentiment;
	}

	public int getMinSentiment() {
		return minSentiment;
	}

	public void setMinSentiment(int minSentiment) {
		this.minSentiment = minSentiment;
		this.setMinSentSet(true);
	}

	public int getSumNewsLength() {
		return sumNewsLength;
	}

	public void setSumNewsLength(int sumNewsLength) {
		this.sumNewsLength = sumNewsLength;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public boolean isMinSentSet() {
		return minSentSet;
	}

	public void setMinSentSet(boolean minSentSet) {
		this.minSentSet = minSentSet;
	}

}
