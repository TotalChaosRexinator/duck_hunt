package duck_hunt;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class scoreController implements Controller {
	private int playerScore;
	private MediaPlayer mediaPlayer;
	private final Image goodScore = new Image(this.getClass().getResourceAsStream("/UI/goodscoredog.gif"));
	private final Image badScore = new Image(this.getClass().getResourceAsStream("/UI/badscoredog.gif"));
	private final Image recordScore = new Image(this.getClass().getResourceAsStream("/UI/recordscoredog.gif"));
	private final Image duckDead = new Image(this.getClass().getResourceAsStream("/duck_sprites/duckdead.png"));
	private AudioClip quack = new AudioClip(getClass().getResource("/sounds/ducks/quack.wav").toExternalForm());
	private final Media winTheme = new Media(getClass().getResource("/sounds/UI/successtheme.mp3").toExternalForm());
	private final Media looseTheme = new Media(getClass().getResource("/sounds/dog/doglaugh.wav").toExternalForm());;
	private Main main;

	private enum ScoreStatus {
		RECORD, BELOWAVG, ABOVEAVG
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ImageView dog;

	@FXML
	private Pane duckPile;

	@FXML
	private Button noButton;

	@FXML
	private Text saveHint;

	@FXML
	private Text textScore;

	@FXML
	private Button yesButton;

	@FXML
	void goBack(ActionEvent event) {
		try {
			mediaPlayer.stop();
			this.main.changeTo(GameScene.MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openPopWindow(ActionEvent event) {
		TextInputDialog nameWindow = new TextInputDialog("Player");
		nameWindow.setHeaderText("Please enter your name:");
		nameWindow.setTitle(" ");
		String playerName;
		boolean isValidName;
		do {
			Optional<String> result = nameWindow.showAndWait();
			if (result.isPresent()) {
				playerName = result.get();
				isValidName = playerName.matches("^\\w{1,25}$");
				if (!isValidName) {
					nameWindow
							.setHeaderText("Error: Please enter a name with 1-25 characters, special underscore only.");
				}
			} else {
				return;
			}

		} while (!isValidName);

		try {
			ScoreFileHelper.saveScoreToFile("scores.json", new Score(playerName, playerScore));
		} catch (IOException e) {
			System.err.print("Couldnt save to file");
		}
		mediaPlayer.stop();
		this.main.changeTo(GameScene.MENU);
	}

	@FXML
	void initialize() {
		assert dog != null : "fx:id=\"dog\" was not injected: check your FXML file 'scoreWindow.fxml'.";
		assert duckPile != null : "fx:id=\"duckPile\" was not injected: check your FXML file 'scoreWindow.fxml'.";
		assert noButton != null : "fx:id=\"noButton\" was not injected: check your FXML file 'scoreWindow.fxml'.";
		assert saveHint != null : "fx:id=\"saveHint\" was not injected: check your FXML file 'scoreWindow.fxml'.";
		assert textScore != null : "fx:id=\"textScore\" was not injected: check your FXML file 'scoreWindow.fxml'.";
		assert yesButton != null : "fx:id=\"yesButton\" was not injected: check your FXML file 'scoreWindow.fxml'.";
	}

	@Override
	public void setMain(Main main) {
		this.main = main;
		this.quack.setVolume(this.main.getVolume());
	}

	public void updateScoreAndDisplay(int score) {
		ScoreList list = new ScoreList();
		this.playerScore = score;
		try {
			list = ScoreFileHelper.getScoresFromFile("scores.json");
		} catch (IOException e) {
			System.err.print("Couldnt load scores.");
			list.setScores(Collections.emptyList());
		}
		mediaPlayer = new MediaPlayer(winTheme);
		ScoreStatus scoreResult = checkScore(this.playerScore, list);
		switch (scoreResult) {
		case ScoreStatus.RECORD:
			dog.setImage(recordScore);
			break;
		case ScoreStatus.ABOVEAVG:
			dog.setImage(goodScore);
			break;
		default:
			mediaPlayer = new MediaPlayer(looseTheme);
			dog.setImage(badScore);
			break;
		}

		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		duckPile.getChildren().clear();
		if (score == 0) {
			textScore.setText(String.valueOf(playerScore));
			this.dog.setVisible(true);
			this.yesButton.setVisible(true);
			this.noButton.setVisible(true);
			this.saveHint.setVisible(true);
			this.mediaPlayer.setVolume(this.main.getVolume());
			mediaPlayer.play();
			return;
		}

		int ducksToSpawn = this.playerScore / 500;
		Timeline timeline = new Timeline();
		timeline.setRate(1);
		for (int i = 0; i < ducksToSpawn; i++) {
			int index = i;
			KeyFrame keyframe = new KeyFrame(Duration.millis(200f * (index + 1)), e -> {
				quack.play();
				ImageView duckCorpse = new ImageView(this.duckDead);
				duckCorpse.setFitWidth(154);
				duckCorpse.setFitHeight(43);
				duckCorpse.setLayoutX((index % 8) * 50f);
				duckCorpse.setLayoutY(this.duckPile.getHeight() - (index / 8) * 30);
				textScore.setText(String.valueOf((index + 1) * 500));
				this.duckPile.getChildren().add(duckCorpse);
			});

			timeline.getKeyFrames().add(keyframe);
		}
		timeline.setOnFinished(e -> {
			this.dog.setVisible(true);
			this.yesButton.setVisible(true);
			this.noButton.setVisible(true);
			this.saveHint.setVisible(true);
			textScore.setText(String.valueOf(playerScore));
			this.mediaPlayer.setVolume(this.main.getVolume());
			mediaPlayer.play();
		});
		timeline.play();
	}

	private ScoreStatus checkScore(int score, ScoreList list) {
		int sumPoints = 0;
		if (list.getScores().isEmpty()) {
			return ScoreStatus.RECORD;
		} else if (list.getScores().getFirst().getPoints() <= score) {
			return ScoreStatus.RECORD;
		} else {
			sumPoints = list.getScores().stream().mapToInt(Score::getPoints).sum();
			if (score >= sumPoints / list.getScores().size()) {
				return ScoreStatus.ABOVEAVG;
			} else {
				return ScoreStatus.BELOWAVG;
			}

		}

	}

}
