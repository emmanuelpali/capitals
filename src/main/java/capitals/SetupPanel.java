package capitals;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SetupPanel extends JFrame {
	private final Game game;
	private final Team team1;
	private final Team team2;
	private final ButtonGroup questionCountGroup;
	private final ButtonGroup continentGroup;
	private final JButton startButton;
	
	public SetupPanel(Game game, Team team1, Team team2) {
		this.game = game;
		this.team1 = team1;
		this.team2 = team2;
		this.startButton = new JButton("Start Game");
		
		
		setTitle("World Capitals Setup");
		setSize(400,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		//add to the setup panel container
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createCenteredLabel("How many questions would You Like to answer?"));
		questionCountGroup = new ButtonGroup();
		addRadioButton(panel, questionCountGroup, "6");
		addRadioButton(panel, questionCountGroup, "10");
		panel.add(Box.createVerticalStrut(10));
		panel.add(createCenteredLabel("Select the continent to explore:"));
		continentGroup = new ButtonGroup();
		for(Continent continent: Continent.values()) {
			addRadioButton(panel, continentGroup, continent.name());
		}
		panel.add(Box.createVerticalStrut(10));
		panel.add(createCenteredComponent(startButton));
		panel.add(Box.createVerticalStrut(10));
		
		add(panel);
		startButton.addActionListener(new StartButtonListener());
	}
	
	private void addRadioButton(JPanel panel, ButtonGroup group, String string) {
		JRadioButton radioButton = new JRadioButton(string);
		group.add(radioButton);
		panel.add(createCenteredComponent(radioButton));
	}

	private Component createCenteredComponent(JComponent component) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(component);
		panel.add(Box.createHorizontalGlue());
		return panel;
	}

	private Component createCenteredLabel(String string) {
		JLabel label = new JLabel(string);
//		label.setAlignmentX(CENTER_ALIGNMENT);
		return label;
	}

	private class StartButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Integer numberOfQuestions = Integer.parseInt(getSelectedButtonText(questionCountGroup));
			Continent selectedContinent = Continent.valueOf(getSelectedButtonText(continentGroup));
			game.initializeGame(numberOfQuestions, selectedContinent);
			
			GameView gameView = new GameView(game, team1, team2);
			gameView.setVisible(true);
			setVisible(false);
		}

		private String getSelectedButtonText(ButtonGroup buttonGroup) {
			for(java.util.Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				if(button.isSelected()) {
					return button.getText();
				}
			}
			return null;
		}
	}
}
