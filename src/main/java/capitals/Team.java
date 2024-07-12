package capitals;

public class Team {
	private String name;
	private Integer score;

	public Team(String name) {
		this.name = name;
		this.score = 0;
	}

	public String getName() {
		return name;
	}

	public Integer getScore() {
		return score;
	}

	public void incrementScore() {
		this.score++;
	}

}
