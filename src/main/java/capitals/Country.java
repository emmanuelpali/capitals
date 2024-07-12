package capitals;

public class Country {
	private String name;
	private String capital;
	private Continent continent;

	public Country(String name, String capital, Continent continent) {
		this.name = name;
		this.capital = capital;
		this.continent = continent;
	}

	public String getName() {
		return name;
	}

	public String getCapital() {
		return capital;
	}

	public Continent getContinent() {
		return continent;
	}

}
