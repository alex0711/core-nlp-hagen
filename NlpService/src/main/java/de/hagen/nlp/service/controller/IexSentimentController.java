package de.hagen.nlp.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hagen.nlp.backend.core.ExchangeSentimentAnalyzer;
import de.hagen.nlp.backend.core.IExchangeSentimentAnalyzer;
import de.hagen.nlp.backend.core.IexSentimentEntity;

@RestController
@RequestMapping("/api/v1/iex/sentiment")
@CrossOrigin(origins = "*")
public class IexSentimentController {

	private IExchangeSentimentAnalyzer analyzer = new ExchangeSentimentAnalyzer();

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IexSentimentEntity> getStockSentiment(
			@RequestParam(value = "tickerSymbol", defaultValue = "APPL") String tickerSymbol) {

		return ResponseEntity.ok(analyzer.getStockSentiment(tickerSymbol));

	}

//	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<IexSentimentEntity> getMarketSentiment() {
//
//		return ResponseEntity.ok(analyzer.getMarketSentiment());
//
//	}

}
