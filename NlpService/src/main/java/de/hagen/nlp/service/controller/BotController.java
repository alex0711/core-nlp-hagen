package de.hagen.nlp.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hagen.nlp.backend.bot.Bot;
import de.hagen.nlp.service.controller.models.BotResponseEntity;

@RestController
@RequestMapping("/api/v1/bot")
@CrossOrigin(origins = "*")
public class BotController {

	private Bot bot = new Bot();

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BotResponseEntity> validate(
			@RequestParam(value = "message", defaultValue = "") String message) {

		String response = bot.respond(message);
		BotResponseEntity result = new BotResponseEntity();
		result.setResponse(response);
		return ResponseEntity.ok(result);

	}

}
