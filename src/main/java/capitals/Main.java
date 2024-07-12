package capitals;

import javax.swing.SwingUtilities;


//This just initializes the game
public class Main {

	public static void main(String[] args) {
		Team team1 = new Team("Team 1");
		Team team2 = new Team("Team 2");

		SwingUtilities.invokeLater(() -> {
			Game game = new Game(team1, team2);
			SetupPanel setupPanel = new SetupPanel(game, team1, team2);
			setupPanel.setVisible(true);
		});
	}

}
