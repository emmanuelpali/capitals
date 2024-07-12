package capitals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//This class handles the logic of the game excluding the UI
public class Game {
	private List<Country> countries;
	private List<Question> questions;
	private Team team1;
	private Team team2;
	private int currentQuestionIndex;
	private int numberOfQuestions;
	private Continent selectedContinent;
	private boolean isTiebreaker;

	public Game(Team team1, Team team2) {
		this.team1 = team1;
		this.team2 = team2;
		this.currentQuestionIndex = 0;
		this.countries = loadCountriesFromFile();
		this.isTiebreaker = false;
	}

	// made this method public to be able to call it in test
	// this methods loads countries from a file
	public List<Country> loadCountriesFromFile() {
		List<Country> countries = new ArrayList<>();
		try (InputStream inputStream = Game.class.getResourceAsStream("/countries.csv")) {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
			if (inputStream == null) {
				throw new IOException("File not found");
			}
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] values = line.split(",");
				Continent continent = Continent.valueOf(values[0].toUpperCase());
				String name = values[1];
				String capital = values[2];
				countries.add(new Country(name, capital, continent));
			}
		} catch (IOException e) {
			System.out.println("exception while reading countries from file");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

	public Continent getSelectedContinent() {
		return selectedContinent;
	}

	public void setSelectedContinent(Continent selectedContinent) {
		this.selectedContinent = selectedContinent;
	}

	public void initializeGame(int numberOfQuestions, Continent continent) {
		this.numberOfQuestions = numberOfQuestions;
		this.selectedContinent = continent;
		this.questions = generateQuestions(numberOfQuestions, continent);
		this.currentQuestionIndex = 0;
	}

	public List<Question> generateQuestions(int numberOfQuestions, Continent continent) {
		List<Country> filteredCountries = new ArrayList<>();
		for (Country country : countries) {
			if (continent == Continent.WORLD || country.getContinent() == continent) {
				filteredCountries.add(country);
			}
		}
		Collections.shuffle(filteredCountries);
		List<Question> questions = new ArrayList<>();
		for (int i = 0; i < numberOfQuestions && i < filteredCountries.size(); i++) {
			Country country = filteredCountries.get(i);
			String question = "What is the capital of " + country.getName() + "?";
			String correctAnswer = country.getCapital();
			List<String> options = generateOptions(country);
			questions.add(new Question(country, question, correctAnswer, options));
		}
		return questions;
	}

	private List<String> generateOptions(Country country) {
		List<String> options = new ArrayList<>();
		options.add(country.getCapital());
		for (Country otherCountry : countries) {
			if (!otherCountry.equals(country) && otherCountry.getContinent() == country.getContinent()) {
				options.add(otherCountry.getCapital());
			}
			if (options.size() == 4) {
				break;
			}
		}
		Collections.shuffle(options);
		return options;
	}

	public Question getNextQuestion() {
		if (currentQuestionIndex < questions.size()) {
			return questions.get(currentQuestionIndex++);
		}
		return null;
	}

	public Question getCurrentQuestion() {
		if (currentQuestionIndex < questions.size()) {
			return questions.get(currentQuestionIndex);
		}
		return null;
	}

	public void incrementQuestionIndex() {
		this.currentQuestionIndex++;
	}

	public Team getWinner() {
		if (team1.getScore() > team2.getScore()) {
			return team1;
		} else if (team2.getScore() > team1.getScore()) {
			return team2;
		} else {
			return null; // tie
		}
	}

	public boolean isGameOver() {
		return currentQuestionIndex >= numberOfQuestions;
	}

	public void addPointToTeam(Team team) {
		team.incrementScore();
	}

	public boolean isTie() {
		return team1.getScore() == team2.getScore();
	}

	public void startTieBreaker() {
		this.isTiebreaker = true;
		this.questions = generateQuestions(4, Continent.WORLD);
		this.currentQuestionIndex = 0;
	}

	public boolean isTiebreaker() {
		return isTiebreaker;
	}
}
