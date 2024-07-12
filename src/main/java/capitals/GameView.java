package capitals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/*
 * This class handles the main game interface.
 * It displays questions, updates scores and handles user interactions
 * */
public class GameView extends JFrame {
	private final Game game;
	private final JLabel questionLabel;
	private final JButton submitButton;
	private final JTextField answerField;
	private final ButtonGroup optionButtonGroup;
	private final JPanel radioButtonPanel;
	private final JLabel scoreLabel;
	private final JLabel answerFeedBack;
	private final Team team1;
	private final Team team2;
	private boolean isTeam1Turn;
	private final JLabel turnLabel;

	public GameView(Game game, Team team1, Team team2) {
		this.game = game;
		this.team1 = team1;
		this.team2 = team2;
		this.isTeam1Turn = true;

		setTitle("Capitals Guessing Game");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		questionLabel = new JLabel();
		questionLabel.setFont(new Font("Boli", Font.BOLD,18));
		answerField = new JTextField(20);
		answerField.setMaximumSize(answerField.getPreferredSize());
		int fieldHeight = 20;
		answerField.setPreferredSize(new Dimension(answerField.getPreferredSize().width, fieldHeight));
		submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Boli", Font.BOLD, 18));
		submitButton.setBackground(Color.black);
		submitButton.setForeground(Color.WHITE);
		scoreLabel = new JLabel();
		answerFeedBack = new JLabel();
		optionButtonGroup = new ButtonGroup();
		radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		turnLabel = new JLabel("Turn: " + team1.getName());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(turnLabel);
		mainPanel.add(Box.createVerticalStrut(10)); // Add some space
		mainPanel.add(scoreLabel);
		mainPanel.add(Box.createVerticalStrut(10)); // Add some space
		mainPanel.add(questionLabel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(answerField);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(radioButtonPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(submitButton);
		mainPanel.add(Box.createVerticalStrut(10));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(answerFeedBack);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(Box.createGlue());

		// Use this to fill up space for the main panel to be centered
		Box outerBox = Box.createVerticalBox();
		outerBox.add(Box.createVerticalGlue());
		outerBox.add(mainPanel);
		outerBox.add(Box.createVerticalGlue());
		// this puts the outerBox in the middle of the panel
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
		outerPanel.add(Box.createHorizontalGlue());
		outerPanel.add(outerBox);
		outerPanel.add(Box.createHorizontalGlue());

		getContentPane().removeAll();
		add(outerPanel, BorderLayout.CENTER);
		revalidate();
		repaint();

		submitButton.addActionListener(new SubmitButtonListener());
		//set the enter key as a hotkey for the submit button
		getRootPane().setDefaultButton(submitButton);
		// add a listener to hear know when an option is selected to set the submit
		// button visible
		for (AbstractButton button : getAllElements(optionButtonGroup)) {
			button.addActionListener(e -> updateStartButtonVisibility());
		}

		displayNextQuestion();
	}

	private void updateStartButtonVisibility() {
		String selectedOption = getSelectedButtonText(optionButtonGroup);
		System.out.println(selectedOption);
		boolean isOptionSelected = selectedOption != null;
		submitButton.setVisible(isOptionSelected);
		System.out.println(isOptionSelected);
	}

	// this method gets all the radio buttons (options), it's used to add
	// updateStartButtonVisibility to each option
	private List<AbstractButton> getAllElements(ButtonGroup group) {
		List<AbstractButton> buttons = new ArrayList<>();
		for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
			buttons.add(e.nextElement());
		}
		return buttons;
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

	// this handles how questions are displayed to the user
	private void displayNextQuestion() {
		if (game.isGameOver() || (game.isTiebreaker() && game.getCurrentQuestion() == null)) {
			endGame();
			return;
		}

		Question question = game.getCurrentQuestion();
		if (question != null) {
			if (game.isTiebreaker()) {
				questionLabel.setText("Tiebreaker: What is the capital of " + question.getCountry().getName());
				answerField.setText("");
				answerField.setVisible(true);
				clearRadioButtons();
				setRadioButtonVisibility(false);
				submitButton.setVisible(true);
			} else {
				questionLabel.setText("What is the capital of " + question.getCountry().getName());
				answerField.setVisible(false);
				List<String> options = question.getOptions();
				displayOptions(options);
				setRadioButtonVisibility(true);
				updateStartButtonVisibility();
			}
		}
	}

	// this handles the display of the question options
	private void displayOptions(List<String> options) {
		clearRadioButtons();
		for (String option : options) {
			JRadioButton radioButton = new JRadioButton(option);
			optionButtonGroup.add(radioButton);
			radioButtonPanel.add(radioButton);
			radioButton.addActionListener(e -> updateStartButtonVisibility());
		}
		radioButtonPanel.revalidate();
		radioButtonPanel.repaint();
		updateStartButtonVisibility();
	}

	// this method makes the options visible for non tie tie breaker questions.
	// options are hidden during tiebreaker rounds
	private void setRadioButtonVisibility(boolean visible) {
		Enumeration<AbstractButton> buttons = optionButtonGroup.getElements();
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			button.setVisible(visible);
		}
	}

	private void clearRadioButtons() {
		optionButtonGroup.clearSelection();
		radioButtonPanel.removeAll();
		radioButtonPanel.revalidate();
		radioButtonPanel.repaint();
	}

	private void endGame() {
		Team winner = game.getWinner();
		if (winner == null) {
			scoreLabel.setText("Folks, we have a tie!");
			if (!game.isTiebreaker()) {
				game.startTieBreaker();
				displayNextQuestion();
				submitButton.setEnabled(true);
			} else {
				scoreLabel.setText("The tiebreaker ended in a tie. No winner!");
				submitButton.setEnabled(false);
				displayEndOptions(winner);
			}
		} else {
			scoreLabel.setText("The winner is " + winner.getName() + "!!");
			displayEndOptions(winner);
		}
	}

	private void displayEndOptions(Team winner) {
		questionLabel.setText("");
		answerField.setVisible(false);
		clearRadioButtons();

		JButton newGameButton = new JButton("New Game");
		newGameButton.setAlignmentX(CENTER_ALIGNMENT);
		newGameButton.addActionListener(e -> {
			SetupPanel setupPanel = new SetupPanel(game, team1, team2);
			setupPanel.setVisible(true);
			dispose();
		});

		JButton exitButton = new JButton("Exit Game");
		exitButton.setAlignmentX(CENTER_ALIGNMENT);
		exitButton.addActionListener(e -> System.exit(0));

		JPanel endGamePanel = new JPanel();

		endGamePanel.setLayout(new BoxLayout(endGamePanel, BoxLayout.Y_AXIS));
		endGamePanel.setBorder(new EmptyBorder(200, 10, 10, 10));
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		endGamePanel.add(scoreLabel);
		endGamePanel.add(Box.createVerticalStrut(10));
		endGamePanel.add(newGameButton);
		endGamePanel.add(Box.createVerticalStrut(10));
		endGamePanel.add(exitButton);

		getContentPane().removeAll();
		add(endGamePanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String answer;
			if (game.isTiebreaker()) {
				answer = answerField.getText();

				System.out.println("Answer: " + answer);
			} else {
				answer = getSelectedOption();
			}

			Question currentQuestion = game.getCurrentQuestion();
			System.out.println("Correct Answer: " + currentQuestion.getCorrectAnswer());
			if (currentQuestion != null && currentQuestion.isCorrect(answer)) {
				if (isTeam1Turn) {
					game.addPointToTeam(team1);
				} else {
					game.addPointToTeam(team2);
				}
				answerFeedBack.setText("Correct Answer: " + currentQuestion.getCorrectAnswer() + " Point added to "
						+ (isTeam1Turn ? team1.getName() : team2.getName()));
			} else {
				answerFeedBack.setText("Incorrect answer, no points added");
			}

			isTeam1Turn = !isTeam1Turn;
			turnLabel.setText("");
			turnLabel.setText((isTeam1Turn ? team1.getName() : team2.getName()) + "'s turn to play. ");
			scoreLabel.setText(
					team1.getName() + " : " + team1.getScore() + " | " + team2.getName() + " : " + team2.getScore());
			game.incrementQuestionIndex();
			displayNextQuestion();
			// updateStartButtonVisibility();

		}

		private String getSelectedOption() {
			Enumeration<AbstractButton> buttons = optionButtonGroup.getElements();
			while (buttons.hasMoreElements()) {
				AbstractButton button = buttons.nextElement();
				if (button.isSelected()) {
					return button.getText();
				}
			}
			return null;
		}
	}
}
