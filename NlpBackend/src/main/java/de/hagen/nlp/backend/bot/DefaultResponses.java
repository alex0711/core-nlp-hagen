package de.hagen.nlp.backend.bot;

import java.util.Random;

public class DefaultResponses {

	static Random random = new Random();

	final static String QUESTION_1 = "Tell me more about that";
	final static String QUESTION_2 = "Explain it";

	final static String STATEMENT = "I don't understand.";

	final static String FAREWELL = "Goodbye.";

	final static String GREETING_1 = "Hello NAME, how are you doing?";

	final static String ASK_NAME_1 = "Hello, what's your name? I'm Tux";

	final static String REPEAT_1 = "You are kind of repeating yourself NAME";

	final static String REPEAT_2 = "You already told me that NAME";

	final static String REPEAT_3 = "Oh, something really new...";

	final static String[] REPEAT_RESPONSES = { REPEAT_1, REPEAT_2, REPEAT_3 };

	final static String[] QUESTION_RESPONSES = { QUESTION_1, QUESTION_2 };

	final static String[] STATEMENT_RESPONSES = { STATEMENT };

	final static String[] FAREWELL_RESPONSES = { FAREWELL };

	final static String[] GREETINGS_WITH_NAME = { GREETING_1 };

	final static String[] GREETINGS_ASK_FOR_NAME = { ASK_NAME_1 };

	static String getFarewell() {
		return getRandomMessage(FAREWELL_RESPONSES);
	}

	private static String getRandomMessage(String[] strings) {
		int r = random.nextInt(strings.length);
		return strings[r];
	}

	static String getQuestionResponse() {
		return getRandomMessage(QUESTION_RESPONSES);
	}

	static String getStatementResponse() {
		return getRandomMessage(STATEMENT_RESPONSES);
	}

	static String getGreetingResponse(String nameOfPerson) {
		return getRandomMessage(GREETINGS_WITH_NAME).replaceFirst("NAME", nameOfPerson);
	}

	public static String getAskForNameGreeting() {
		return getRandomMessage(GREETINGS_ASK_FOR_NAME);
	}

	public static String getRepeatedStatementResponse(String nameOfPerson) {
		return getRandomMessage(REPEAT_RESPONSES).replaceFirst("NAME", nameOfPerson);
	}

	public static String getIdentityResponse() {
		return "People call me Tux.";
	}

	public static String getToldIdentityResponse() {
		return "I already told you, I'm Tux.";
	}

}
