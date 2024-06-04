
//Acknowledgements:
//CIS CS Department, StackOverflow contributors: , GitHub Copilot, OpenAI ChatGPT

//Audio credits to Micheal McClean for Fluffing a Duck (Background Music)


package edu.cis;

import acm.graphics.GObject;
import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class Breakout extends GraphicsProgram implements ActionListener {

	// Dimensions of the canvas, in pixels
	// These should be used when setting up the initial size of the game,
	// but in later calculations you should use getWidth() and getHeight()
	// rather than these constants for accurate size information.

	public static final double CANVAS_WIDTH = 420;
	public static final double CANVAS_HEIGHT = 600;

	// Number of bricks in each row
	public static final int NBRICK_COLUMNS = 10;

	// Number of rows of bricks
	public static final int NBRICK_ROWS = 10;

	// Separation between neighboring bricks, in pixels
	public static final double BRICK_SEP = 4;

	// Width of each brick, in pixels
	public static final double BRICK_WIDTH = Math.floor(
			(CANVAS_WIDTH - (NBRICK_COLUMNS + 1.0) * BRICK_SEP) / NBRICK_COLUMNS);

	// Height of each brick, in pixels
	public static final double BRICK_HEIGHT = 8;

	// Offset of the top brick row from the top, in pixels
	public static final double BRICK_Y_OFFSET = 40;

	// Total Brick Count used to determine when the game ends
	private int brickCount = NBRICK_ROWS * NBRICK_COLUMNS;

	// Dimensions of the paddle
	public static final double PADDLE_WIDTH = 60;
	public static final double PADDLE_HEIGHT = 10;

	// Radius of the ball in pixels
	public static final double BALL_RADIUS = 10;

	// The ball's vertical velocity.
	public static final double easyVELOCITY_Y = 1.0;
	public static final double medVELOCITY_Y = 2.0;
	public static final double hardVELOCITY_Y = 3.0;

	// The ball's minimum and maximum horizontal velocity; the bounds of the
	// initial random velocity that you should choose (randomly +/-).
	public static final double VELOCITY_X_MIN = 1.0;
	public static final double VELOCITY_X_MAX = 3.0;

	// Number of turns 
	public static int NTURNS = 3;

	// Paddle Color Choice
	public static Color paddleColor = Color.BLACK;

	GRect paddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
	GOval ball = new GOval(CANVAS_WIDTH/2, CANVAS_HEIGHT/2 - 70, BALL_RADIUS,BALL_RADIUS);

	private double vx,vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();

	JLabel firstLine;
	JLabel secondLine;
	JLabel thirdLine;
	JLabel fourthLine;
	JLabel fifthLine;

	JButton option1;
	JButton option2;
	JButton option3;
	JButton option4;

	//ALL menu buttons/fields

	private boolean musicValue = true;

	public int score = 0;

	public static void main(String[] args) {
		new Breakout().start();
	}

	public void run(){
		playBackgroundMusic();
		setupGame2();
		//openScreen();
		/** Hide openScreen() when running only game as well
		 */
	}
	//Breakaout constructor

	public void setupGame(){
		removeAll();

		difficulty();
	}

	public void setupGame2(){
		/*remove(firstLine);
		remove(secondLine);
		remove(option1);
		remove(option2);
		remove(option3);
		removeAll();*/

		/** REMOVE ABOVE WHEN TRYING TO RUN ONLY THE GAME
		 */

		disappearMouse();

		setSize((int) CANVAS_WIDTH, (int)CANVAS_HEIGHT);

		setupBricks();
		setupPaddle();
		setupBall();
		play();
	}

	public String difficultyValue = "Easy";
	public boolean receivedAnswer = false;

	public String[] easyFile;
	public String[] mediumFile;
	public String[] hardFile;

	public String easyScores;
	public String mediumScores;
	public String hardScores;

	public void openScreen() throws FileNotFoundException {
		getGCanvas().setSize((int) CANVAS_WIDTH, (int) CANVAS_HEIGHT);

		//messages on screen
		firstLine = new JLabel(  "Welcome to Sander's Breakout!");
		firstLine.setSize(100,100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("<html>To continue, please click one of the buttons.");
		secondLine.setSize(10,10);
		secondLine.setLocation(0, 180);

		// Does not work as nicely as would like, the formatting is off, and does not copy/paste the write info

//		easyFile = readFile("/Users/sander/IdeaProjects/Breakout/src/easy.txt");
//		mediumFile = readFile("/Users/sander/IdeaProjects/Breakout/src/medium.txt");
//		hardFile = readFile("/Users/sander/IdeaProjects/Breakout/src/hard.txt");

		//easyScores = String.join(" ", easyFile);
		//mediumScores = String.join(" ", mediumFile);
		//hardScores = String.join(" ", hardFile);

		thirdLine = new JLabel(""); //(easyScores);
		fourthLine = new JLabel(""); //(mediumScores);
		fifthLine = new JLabel(""); //(hardScores);

		 //

		//buttons
		option1 = new JButton("Play Game");
		option1.setSize(60,30);
		option1.setLocation(20, 10);
		option1.addActionListener(this);

		option2 = new JButton("Settings Menu");
		option2.setSize(60,30);
		option2.setLocation(40, 10);
		option2.addActionListener(this);

		option3 = new JButton("Quit Game");
		option3.setSize(60,30);
		option3.setLocation(60, 10);
		option3.addActionListener(this);

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(option3);
		add(firstLine);
		add(secondLine);

		add(thirdLine);
		add(fourthLine);
		add(fifthLine);
		//last three for the high scores

		pack();
	}

	public void saveScore(){
		firstLine = new JLabel("Welcome to Sander's Breakout!");
		firstLine.setSize(100,100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("Please choose to save the score of this game.");
		secondLine.setSize(100,100);
		secondLine.setLocation(0, 180);

		option1 = new JButton("Save");
		option1.setSize(60,30);
		option1.setLocation(20, 10);
		option1.addActionListener(this);

		option2 = new JButton("Do not save");
		option2.setSize(60,30);
		option2.setLocation(40, 10);
		option2.addActionListener(this);

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(firstLine);
		add(secondLine);

		pack();
	}

	public void difficulty(){
		firstLine = new JLabel("Welcome to Sander's Breakout!");
		firstLine.setSize(100,100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("To continue, please click one of the buttons.");
		secondLine.setSize(100,100);
		secondLine.setLocation(0, 180);

		option1 = new JButton("Easy");
		option1.setSize(60,30);
		option1.setLocation(20, 10);
		option1.addActionListener(this);

		option2 = new JButton("Medium");
		option2.setSize(60,30);
		option2.setLocation(40, 10);
		option2.addActionListener(this);

		option3 = new JButton("Hard");
		option3.setSize(60,30);
		option3.setLocation(60, 10);
		option3.addActionListener(this);

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(option3);
		add(firstLine);
		add(secondLine);

		pack();

	}

	public void setupBricks() {
		double x = BRICK_SEP; // Start x position with the separation
		double y = BRICK_Y_OFFSET;

		for (int col = 0; col < NBRICK_COLUMNS; col++) {
			for (int row = 0; row < NBRICK_ROWS; row++) {
				GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
				switch (row / 2) {
					case 0:
						brick.setFillColor(Color.RED);
						break;
					case 1:
						brick.setFillColor(Color.ORANGE);
						break;
					case 2:
						brick.setFillColor(Color.YELLOW);
						break;
					case 3:
						brick.setFillColor(Color.GREEN);
						break;
					case 4:
						brick.setFillColor(Color.BLUE);
						break;
				}
				brick.setFilled(true);
				add(brick);
				y += BRICK_HEIGHT + BRICK_SEP; // Adjust x for next brick
			}
			x += BRICK_WIDTH + BRICK_SEP; // Move to next row
			y = BRICK_Y_OFFSET; // Reset x for next row
		}
		setLayout(new FlowLayout());

		pack();
	}

	public void setupPaddle(){
		paddle.setFillColor(paddleColor);
		paddle.setFilled(true);
		paddle.setLocation(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
		add(paddle);
	}

	public void disappearMouse(){
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		setCursor(blankCursor);

		//TODO CODE BORROWED + Adapted from StackOverflow
	}

	public void setupBall(){
		ball.setFillColor(Color.BLACK);
		ball.setFilled(true);
		add(ball);
	}

	private boolean gameOverValue = false;

	public void play(){
		infoDisplay();

		vx = rgen.nextDouble(VELOCITY_X_MIN, VELOCITY_X_MAX);
		if (rgen.nextBoolean(0.5)) vx = -vx;

		/*if(difficultyValue == "Easy"){
			vy = easyVELOCITY_Y;
		}if(difficultyValue == "Medium"){
			vy = medVELOCITY_Y;
		}if(difficultyValue == "Hard"){
			vy = hardVELOCITY_Y;
		}*/

		vy = hardVELOCITY_Y;


		while (!gameOverValue) {
			try {
				moveBall();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			pause(10);
			//pause allows for smooth ball movement

		}

		try {
			gameOver();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	GLabel scoreLabel = new GLabel("Score:" + 0);
	GLabel livesLabel = new GLabel("Lives left: 0");

	private boolean userHasMovedMouse = false;

	public void infoDisplay(){
		remove(scoreLabel);//.setVisible(false);
		remove(livesLabel);//.setVisible(false);

		int score = 100 - brickCount;
		double x = 10;  // x-coordinate of label
		double y = 10;  // y-coordinate of label

		this.scoreLabel = new GLabel("Score: " + score);
		scoreLabel.setFont("Arial-12");  // Set the font size (optional)
		scoreLabel.setColor(Color.BLACK);  // Set the color of the text (optional)

		this.livesLabel = new GLabel("Lives left:" + NTURNS);
		livesLabel.setFont("Arial-12");  // Set the font size (optional)
		livesLabel.setColor(Color.BLACK);  // Set the color of the text (optional)

		add(scoreLabel, 5, 20);
		add(livesLabel, 5,35);
	}

	private void moveBall() throws IOException {
		if(NTURNS <= 0){
			gameOver();
		}
		if (!userHasMovedMouse) {
			return;
		}

		infoDisplay();

		ball.move(vx, vy);

		// Ball collision with walls
		if (ball.getX() <= 0 || ball.getX() >= CANVAS_WIDTH - BALL_RADIUS) {
			vx = -vx;
		}
		if (ball.getY() <= 0) {
			vy = -vy;
		} else if (ball.getY() >= 350) {
			NTURNS--;
			setBall();
			return;
		}

		GObject collider = getCollidingObject();

		if (collider == paddle) {
			vy = -vy;
			ball.setLocation(ball.getX(), paddle.getY() - BALL_RADIUS - 1);
		} else if (collider != null) {
			remove(collider);
			vy = -vy;
			brickCount--;

			if (brickCount == 0) {
				gameOverValue = true;
			}
		}
	}

	public void setBall(){
		ball.setLocation(CANVAS_WIDTH/2, CANVAS_HEIGHT/2 - 70);
		ball.move(0,0);
		pause(3000);
	}

	private GObject getCollidingObject() {
		if((getElementAt(ball.getX(), ball.getY())) != null) {
			return getElementAt(ball.getX(), ball.getY());
		}
		else if((getElementAt(ball.getX() + BALL_RADIUS, ball.getY())) != null) {
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
		}
		else if((getElementAt(ball.getX(), ball.getY() + BALL_RADIUS)) != null) {
			return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
		}
		else if((getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS)) != null) {
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
		}
		// no object present
		return null;
	}

	public String easyPathFile = "/Users/sander/IdeaProjects/Breakout/src/easy.txt";
	public String medPathFile = "/Users/sander/IdeaProjects/Breakout/src/medium.txt";
	public String hardPathFile = "/Users/sander/IdeaProjects/Breakout/src/hard.txt";


	public void gameOver() throws IOException {
		removeAll();

		if(difficultyValue == "Easy"){
			saveScore(easyScores);
		} else if(difficultyValue == "Medium"){
			saveScore(mediumScores);
		} else if(difficultyValue == "Hard"){
			saveScore(hardScores);
		}

		firstLine = new JLabel("GAME OVER");
		firstLine.setSize(100, 100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("How would you like to proceed?");
		secondLine.setSize(100, 100);
		secondLine.setLocation(20, 180);

		option1 = new JButton("Quit game");
		option2 = new JButton("Play again");
		option3 = new JButton("Return to Main Menu");

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(option3);

		add(firstLine);
		add(secondLine);

		pack();

		valueReceived = false;
		infiniteLoop();
	}

	public boolean valueReceived = false;

	public void infiniteLoop(){
		while(!valueReceived){

		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		paddle.setFillColor(Color.GRAY);
		paddle.setFilled(true);

		int mouseX = e.getX();
		int constY = 300;

		if(paddle.isVisible()){
			paddle.setLocation(mouseX,constY);
		}

		userHasMovedMouse = true;
	}

	public boolean saveScoreValue;

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if ("Play Game".equals(command)) {
			valueReceived = true;
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			remove(thirdLine);
			remove(fourthLine);
			remove(fifthLine);
			setupGame();
		} else if ("Settings Menu".equals(command)) {
			settingsMenu();
		} else if ("Quit Game".equals(command)) {
			valueReceived = true;
			System.exit(0);
		} else if ("Color of Bar".equals(command)) {
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			colorOfBar();
		} else if("Background Music".equals(command)){
			backgroundMusic();
		} else if("Black".equals(command)){
			paddleColor = Color.BLACK;
		} else if("Blue".equals(command)){
			paddleColor = Color.BLUE;
		} else if("Red".equals(command)){
			paddleColor = Color.RED;
		} else if("Yellow".equals(command)){
			paddleColor = Color.YELLOW;
		} else if("On".equals(command)){
			playBackgroundMusic();
		} else if("Off".equals(command)){
			stopBackgroundMusic();
		} else if("Easy".equals(command)){
			difficultyValue = "Easy";
			receivedAnswer = true;
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			saveScore();
		} else if("Medium".equals(command)){
			difficultyValue = "Medium";
			receivedAnswer = true;
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			saveScore();
		} else if("Hard".equals(command)){
			difficultyValue = "Hard";
			receivedAnswer = true;
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			saveScore();
		} else if("Do not save".equals(command)){
			saveScoreValue = false;
			removeAll();
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			setupGame2();
		} else if("Save".equals(command)){
			saveScoreValue = true;
			removeAll();
			remove(option1);
			remove(option2);
			remove(option3);
			remove(firstLine);
			remove(secondLine);
			setupGame2();
		} if (event.getSource() == option1) {
			System.out.println("Save button pressed");
			setupGame2();
		} else if (event.getSource() == option2) {
			System.out.println("Do not save button pressed");
			setupGame2();
		} else if("Go Back to Main Menu".equals(command) || "Return to Main Menu".equals(command) || "Play again".equals(command)){
			valueReceived = true;
			System.out.println("Go to Main Menu");
			setupGame();
		}
	}

	//TODO Finish the save score method

	public void saveScore(String arrayName) throws IOException {
		String filename = easyPathFile;
		if(difficultyValue == "easy"){
			filename = easyPathFile;
		} else if(difficultyValue == "medium"){
			filename = medPathFile;
		} else if(difficultyValue == "hard"){
			filename = hardPathFile;
		}

		int score = 100-brickCount;

		try (FileReader fileReader = new FileReader(filename);
			 BufferedReader reader = new BufferedReader(fileReader)) {
			try (FileWriter fileWriter = new FileWriter(filename, true);
				 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(String.valueOf(score));
				bufferedWriter.newLine();
				bufferedWriter.close();
				System.out.println("Your score has been saved in the file: " + filename);
			}
		}
	}

	public void settingsMenu() {
		removeAll();
		remove(firstLine);
		remove(secondLine);
		remove(thirdLine);
		remove(fourthLine);
		remove(fifthLine);
		remove(option1);
		remove(option2);
		remove(option3);

		firstLine = new JLabel("Welcome to the Settings Menu.");
		firstLine.setSize(100, 100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("Please select a choice of what you would " +
				"like to modify for your customized gameplay experience.");
		secondLine.setSize(100, 100);
		secondLine.setLocation(20, 180);

		option1 = new JButton("Color of Bar");

		option2 = new JButton("Background Music");

		option3 = new JButton("Go Back to Main Menu");

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(option3);

		add(firstLine);
		add(secondLine);

		pack();
	}

	public void colorOfBar() {
		removeAll();
		remove(option1);
		remove(option2);
		remove(option3);
		remove(option4);
		remove(firstLine);
		remove(secondLine);

		firstLine = new JLabel("Please adjust the color as you see fit.");
		firstLine.setSize(100, 100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("Default color is: Black" + "Current color is:" + paddleColor);
		secondLine.setSize(100, 100);
		secondLine.setLocation(20, 180);

		option1 = new JButton("Black");
		option2 = new JButton("Blue");
		option3 = new JButton("Red");
		option4 = new JButton("Yellow");

		JButton goToMainMenu = new JButton("Go Back to Main Menu");
		goToMainMenu.setSize(100, 100);
		goToMainMenu.setLocation(20, 180);
		goToMainMenu.addActionListener(this);

		setLayout(new FlowLayout());

		add(option1);
		add(option2);
		add(option3);
		add(option4);

		add(firstLine);
		add(secondLine);
		add(goToMainMenu);

		pack();
	}

	public void backgroundMusic() {
		removeAll();
		remove(option1);
		remove(option2);
		remove(option3);
		remove(option4);
		remove(firstLine);
		remove(secondLine);

		firstLine = new JLabel("Please adjust the background music as you see fit.");
		firstLine.setSize(100, 100);
		firstLine.setLocation(20, 80);

		secondLine = new JLabel("Default setting: ON current setting:" + musicValue);
		secondLine.setSize(100, 100);
		secondLine.setLocation(20, 180);

		option1 = new JButton("On");
		option2 = new JButton("Off");

		JButton goToMainMenu = new JButton("Go Back to Main Menu");
		goToMainMenu.setSize(100, 100);
		goToMainMenu.setLocation(20, 180);
		goToMainMenu.addActionListener(this);

		setLayout(new FlowLayout());

		add(option1);
		add(option2);

		add(firstLine);
		add(secondLine);
		add(goToMainMenu);

		pack();
	}

	public Clip clip;

	public void playBackgroundMusic() {
		try {
			URL url = this.getClass().getClassLoader().getResource("fluffingaduck.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void stopBackgroundMusic() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
		}
	}


}
