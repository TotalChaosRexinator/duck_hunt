package duck_hunt;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameController implements Controller {
	private Main main;

	private double gameVolume = 1;
	private int startingLevel = 1;
	private boolean twoDucksGame = false;
	private final Image shotgunIdle = new Image(
			this.getClass().getResourceAsStream("/shotgun_sprites/shotgun_idle.png"));
	private final Image shotgunShoot = new Image(
			this.getClass().getResourceAsStream("/shotgun_sprites/shotgun_shoot.gif"));
	private final Image shotgunRackBack = new Image(
			this.getClass().getResourceAsStream("/shotgun_sprites/shotgun_rackback.gif"));
	private final Image shotgunRackForw = new Image(
			this.getClass().getResourceAsStream("/shotgun_sprites/shotgun_rackforward.gif"));
	private AnimationTimer timer;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Text minDucks;

	@FXML
	private Canvas canvas;

	@FXML
	private Text hitOutOf;

	@FXML
	private Text levelInfo;

	@FXML
	private ImageView onScreenUI;

	@FXML
	private Text scoreBoard;

	@FXML
	private ImageView shotgunIcon;

	@FXML
	private Rectangle slug1;

	@FXML
	private Rectangle slug2;

	@FXML
	private Rectangle slug3;

	@FXML
	void initialize() {
		assert minDucks != null : "fx:id=\"minDucks\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert hitOutOf != null : "fx:id=\"hitOutOf\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert levelInfo != null : "fx:id=\"levelInfo\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert onScreenUI != null : "fx:id=\"onScreenUI\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert scoreBoard != null : "fx:id=\"scoreBoard\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert shotgunIcon != null : "fx:id=\"shotgunIcon\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert slug1 != null : "fx:id=\"slug1\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert slug2 != null : "fx:id=\"slug2\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert slug3 != null : "fx:id=\"slug3\" was not injected: check your FXML file 'gameWindow.fxml'.";
		
	}
	private void startGame()
	{
		shotgunIcon.setImage(shotgunIdle);
		timer = new GameLoop(canvas, this);
		canvas.requestFocus();
		canvas.setFocusTraversable(true);
		timer.start();
	}

	public void setMain(Main main) {
		this.main = main;
		this.gameVolume = this.main.getVolume();
		this.startingLevel = this.main.getStartingLevel();
		this.twoDucksGame = this.main.isTwoDucks();
		this.startGame();
	}

	public void updateScoreboard(int score) {
		scoreBoard.setText(String.valueOf(score));
	}

	public void setShotgunAnimation(int i) {

		switch (i) {
		case 1:
			shotgunIcon.setImage(shotgunIdle);
			break;
		case 2:
			shotgunIcon.setImage(shotgunRackBack);
			break;
		case 3:
			shotgunIcon.setImage(shotgunRackForw);
			break;
		case 4:
			shotgunIcon.setImage(shotgunShoot);
			break;
		default:
			shotgunIcon.setImage(shotgunIdle);
		}
	}

	public void updateSlugs(int s) {
		Rectangle[] slugs = { slug1, slug2, slug3 };
		for (int i = 0; i < slugs.length; i++) {
			if (i < s) {
				slugs[i].setVisible(true);
			} else {
				slugs[i].setVisible(false);
			}
		}
	}

	public void updateDucksUI(int shot, int range, int min, int level) {
		hitOutOf.setText(shot + "/" + range);
		minDucks.setText(String.valueOf(min));
		levelInfo.setText(String.valueOf(level));
	}

	public void showFlyAway() {
		if (!onScreenUI.isVisible()) {
			onScreenUI.setVisible(true);
		}
	}

	public void hideFlyAway() {
		if (onScreenUI.isVisible()) {
			onScreenUI.setVisible(false);
		}

	}

	public void goBack() {
		if (timer != null) {
			timer.stop();
		}
		try {
			this.main.changeTo(GameScene.MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goToScore(int score) {
		if (timer != null) {
			timer.stop();
		}
		try {
			this.main.setSaveScore(score);
			this.main.changeTo(GameScene.SCORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getGameVolume() {
		return gameVolume;
	}

	public int getStartingLevel() {
		return startingLevel;
	}

	public boolean isTwoDucksGame() {
		return twoDucksGame;
	}
}
