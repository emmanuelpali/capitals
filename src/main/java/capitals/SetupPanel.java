package capitals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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


//This class simply collects the data neede to start the game
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
		startButton.setVisible(false);
		
		startButton.setFont(new Font("Boli", Font.BOLD, 18));
		startButton.setBackground(Color.black);
		startButton.setForeground(Color.WHITE);

		
		setTitle("World Capitals Setup");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// add to the setup panel container
		JPanel panel = new JPanel();
		panel.setFont(new Font("Arial", Font.ITALIC, 20));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createCenteredLabel("How many questions would You Like to answer?"));
		questionCountGroup = new ButtonGroup();
		addRadioButton(panel, questionCountGroup, "6");
		addRadioButton(panel, questionCountGroup, "10");
		panel.add(Box.createVerticalStrut(10));
		panel.add(createCenteredLabel("Select the continent you want to explore"));
		continentGroup = new ButtonGroup();
		for (Continent continent : Continent.values()) {
			addRadioButton(panel, continentGroup, continent.name());
		}
		panel.add(Box.createVerticalStrut(10));
		panel.add(createCenteredComponent(startButton));
		panel.add(Box.createHorizontalStrut(10));
		add(panel);

		startButton.addActionListener(new StartButtonListener());

		for (AbstractButton button : getAllElements(questionCountGroup)) {
			button.addActionListener(e -> updateStartButtonVisibility());
		}
		for (AbstractButton button : getAllElements(continentGroup)) {
			button.addActionListener(e -> updateStartButtonVisibility());
		}
	}

	private void updateStartButtonVisibility() {
		boolean isQuestionCountSelected = getSelectedButtonText(questionCountGroup) != null;
		boolean isContinentCountSelected = getSelectedButtonText(continentGroup) != null;
		startButton.setVisible(isContinentCountSelected && isQuestionCountSelected);
	}

	private String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return button.getText();
			}

		}
		return null;
	}

	private List<AbstractButton> getAllElements(ButtonGroup group) {
		List<AbstractButton> buttons = new ArrayList<>();
		for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
			buttons.add(e.nextElement());
		}
		return buttons;
	}

	private void addRadioButton(JPanel panel, ButtonGroup group, String string) {
		JRadioButton radioButton = new JRadioButton(string);
		group.add(radioButton);
		panel.add(createCenteredComponent(radioButton));
	}

	private Component createCenteredComponent(JComponent component) {
		JPanel panel = new JPanel();
		//panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalGlue());
		component.setFont(new Font("Boli", Font.ITALIC, 16));
		panel.add(component);
		panel.add(Box.createHorizontalGlue());
		return panel;
	}

	private Component createCenteredLabel(String string) {
		JLabel label = new JLabel(string);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setFont(new Font("Boli", Font.BOLD, 16));
		return label;
	}

	private class StartButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int numberOfQuestions = Integer.parseInt(getSelectedButtonText(questionCountGroup));
			Continent selectedContinent = Continent.valueOf(getSelectedButtonText(continentGroup));

			game.initializeGame(numberOfQuestions, selectedContinent);

			GameView gameView = new GameView(game, team1, team2);
			gameView.setVisible(true);
			setVisible(false);
		}
	}
}
