package de.hagen.nlp.service.controller.models;

import javax.persistence.*;

@Entity
public class SentimentEntity {

	@Id
	@GeneratedValue
	private Long id;
	private int sentiment;
	private String text;

	public int getSentiment() {
		return this.sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
