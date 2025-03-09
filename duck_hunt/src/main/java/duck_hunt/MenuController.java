package duck_hunt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MenuController implements Controller {
	private MediaPlayer mediaPlayer;
	private Main main;

	@FXML
	private Button exitButton;

	@FXML
	private Button highscoreButton;

	@FXML
	private Button optionsButton;

	@FXML
	private Button startButton;

	@FXML
	void initialize() {
		assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert startButton != null
				: "fx:id=\"startButton\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert optionsButton != null
				: "fx:id=\"optionsButton\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert highscoreButton != null
				: "fx:id=\"highscoresButton\" was not injected: check your FXML file 'gameWindow.fxml'.";
		Media menuTheme = new Media(getClass().getResource("/sounds/UI/menu_theme.mp3").toExternalForm());
		mediaPlayer = new MediaPlayer(menuTheme);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}

	public void setMain(Main main) {
		this.main = main;
		this.mediaPlayer.setVolume(this.main.getVolume());
	}

	@FXML
	void exit(ActionEvent event) {
		mediaPlayer.stop();
		System.exit(0);
	}

	@FXML
	void highscores(ActionEvent event) {
		try {
			mediaPlayer.stop();
			main.changeTo(GameScene.HIGHSCORE);
		} catch (Exception e) {
			System.out.println("Couldnt change to Highscore");
		}
	}

	@FXML
	void options(ActionEvent event) {
		try {
			mediaPlayer.stop();
			main.changeTo(GameScene.OPTIONS);
		} catch (Exception e) {
			System.out.println("Couldnt change to options");
		}
	}

	@FXML
	void start(ActionEvent event) {
		try {
			mediaPlayer.stop();
			main.changeTo(GameScene.GAME);
		} catch (Exception e) {
			System.out.println("Couldnt change to Game");
		}

	}

}
