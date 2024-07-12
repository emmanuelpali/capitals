package capitals;

import java.util.List;

public class Question {
	private final Country country;

	private final String question;
	private final String correctAnswer;
	private final List<String> options;

	public Question(Country country, String question, String correctAnswer, List<String> options) {
		this.country = country;
		this.question = question;
		this.correctAnswer = correctAnswer;
		this.options = options;
	}

	public String getQuestion() {
		return question;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public List<String> getOptions() {
		return options;
	}

	public Country getCountry() {
		return country;
	}

	public boolean isCorrect(String capital) {
		return correctAnswer.equalsIgnoreCase(capital);
	}
}
