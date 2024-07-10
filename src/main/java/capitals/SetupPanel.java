package capitals;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SetupPanel extends JFrame {
	private final Game game;
	private final Team team1;
	private final Team team2;
	private final JComboBox<Integer> questionCountBox;
	private final JComboBox<Continent> continentBox;
	private final JButton startButton;
	
	public SetupPanel(Game game, Team team1, Team team2) {
		this.game = game;
		this.team1 = team1;
		this.team2 = team2;
		this.continentBox = new JComboBox<>(Continent.values());
		this.questionCountBox = new JComboBox<>(new Integer[]{6,10,12});
		this.startButton = new JButton("Start Game");
		
		
		setTitle("World Capitals Setup");
		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(3,2));
		//add to the setup panel container
		add(new JLabel("Number of Questions"));
		add(questionCountBox);
		add(new JLabel("What continent do you want to Explore?"));
		add(continentBox);
		add(startButton);
		
		startButton.addActionListener(new StartButtonListener());
	}
	
	private class StartButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int numberOfQuestions = (Integer) questionCountBox.getSelectedItem();
			Continent selectedContinent = (Continent) continentBox.getSelectedItem();
			
			game.initializeGame(numberOfQuestions, selectedContinent);
			
			GameView gameView = new GameView(game, team1, team2);
			gameView.setVisible(true);
			setVisible(false);
		}
	}
}
