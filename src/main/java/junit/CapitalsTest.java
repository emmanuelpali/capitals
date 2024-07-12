package junit;

import org.junit.jupiter.api.Test;

import capitals.Continent;
import capitals.Country;
import capitals.Game;
import capitals.Question;
import capitals.Team;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class CapitalsTest {
	@Test
	public void testLoadCountriesFromFile() {
		Team team1 = new Team("Team 1");
		Team team2 = new Team("team 2");
		
		Game game = new Game(team1, team2);
		
		List<Country> countries = game.loadCountriesFromFile();
		
		// Assert that countries are loaded
        assertNotNull(countries, "Countries list should not be null");
        assertFalse(countries.isEmpty(), "Countries list should not be empty");

        // Assert a few more things, like the number of countries loaded
        // and the contents of the first country in the list
        assertEquals(195, countries.size(), "There should be 195 countries");
        Country firstCountry = countries.get(0);
        assertEquals("Algeria", firstCountry.getName(), "First country should be Afghanistan");
        assertEquals("Algiers", firstCountry.getCapital(), "Capital of Afghanistan should be Kabul");
        assertEquals(Continent.AFRICA, firstCountry.getContinent(), "Continent of Afghanistan should be Asia");

	}
	
	@Test
	public void testIsCorrect() {
		Country country = new Country("Canada", "Ottawa", Continent.NORTH_AMERICA);
		String question = "What is the capital of Canada";
		String correctAns = "Ottawa";
		List<String> options = Arrays.asList("Toronto","Vancouver", "Calgary", "Montreal");
		
		Question q = new Question(country, question, correctAns, options);
		
		assertTrue(q.isCorrect("Ottawa"));
		assertFalse(q.isCorrect("Toronto"));
		
	}
}
