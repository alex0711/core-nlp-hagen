package de.hagen.nlp.backend.bot.test;

import org.junit.Test;

import de.hagen.nlp.backend.bot.Bot;

public class BotTest {

	private Bot bot = new Bot();

	@Test
	public void testGreeting() {
		// String response = bot.respond("Hi, I'm Alex. Nice to meet you.");

	}

	@Test
	public void testCreateQuestion() {
//		String s1 = "I'm feeling really tired";
//		String response = bot.respond(s1);
//		System.out.println(s1 + " --> " + response);
//		
//		String s2 = "I have to work long hours lately";
//		response = bot.respond(s2);
//		System.out.println(s2 + " --> " + response);
//		
//		String s3 = "Soccer is a great game!";
//		response = bot.respond(s3);
//		System.out.println(s3 + " --> " + response);
//		
//		String s4 = "I am a programmer";
//		response = bot.respond(s4);
//		System.out.println(s4 + " --> " + response);
	}

	@Test
	public void testCoref() {
		 String response = bot.respond("The Revolutionary War occurred during the "
		 + "1700s. It started in Boston and it was the first war in "
		 + "the United States.");
		
		 String q1 = "Which was the first war in the United States?";
		 response = bot.respond(q1);
		 System.out.println(q1 + " --> " + response);
		
		 String q2 = "When was the first war in the United States?";
		 response = bot.respond(q2);
		 System.out.println(q2 + " --> " + response);
		 
		 String q3 = "Where started the Revolutionary War?";
		 response = bot.respond(q3);
		 System.out.println(q3 + " --> " + response);
	}

	@Test
	public void testQestionWhoIsBot() {

		// String response = bot.respond("Who are you?");
		// System.out.println("Who are you?" + " --> " + response);
		// response = bot.respond("What's your name?");
		// System.out.println("What is your name?" + " --> " + response);

	}

}
