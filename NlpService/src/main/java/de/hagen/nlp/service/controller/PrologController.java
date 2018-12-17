package de.hagen.nlp.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hagen.nlp.backend.core.IExchangeSentimentAnalyzer;
import de.hagen.nlp.backend.core.IexSentimentEntity;

@RestController
@RequestMapping("/api/v1/prolog")
@CrossOrigin(origins = "*")
public class PrologController {

//	@Autowired
//	private IExchangeSentimentAnalyzer analyzer;

//	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<IexSentimentEntity> validate(
//			@RequestParam(value = "sentence", defaultValue = "") String sentence) {
//
//		// validate
//		boolean isValid = false;
//
//		if (isValid == true) {
//			// return ResponseEntity.ok(analyzer.getStockSentiment(tickerSymbol));
//
//		} else {
//			// return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE);
//		}
//
//		return null;
//
//	}
//
//	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> executePrologInterpreter(@RequestBody SentimentEntity entry) {
//
//		return ResponseEntity.ok(null);
//
//	}

}
