package de.hagen.nlp.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hagen.nlp.backend.core.SentimentAnalyzer;
import de.hagen.nlp.service.controller.models.SentimentEntity;

@RestController
@RequestMapping("/api/v1/sentiment")
@CrossOrigin(origins = "*")
public class SentimentController {

	private SentimentAnalyzer analyzer = new SentimentAnalyzer();

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody SentimentEntity entry) {

		int sentiment = analyzer.getSentiment(entry.getText());
		entry.setSentiment(sentiment);
		return ResponseEntity.ok(entry);

	}
}
