package duck_hunt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private Stage stage;
	private int currentScore;
	// settings
	private double volume = 1;
	private int startingLevel = 1;
	private boolean twoDucks = false;

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		this.currentScore = 0;
		this.changeTo(GameScene.MENU);
	}

	@Override
	public void stop() throws Exception {
		//nic neni potreba
	}

	private void exitProgram(WindowEvent evt) {
		System.exit(0);
	}

	public void changeTo(GameScene name) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(name.getFxmlFileName()));
			Parent root = fxmlLoader.load();
			Controller controller = fxmlLoader.getController();
			controller.setMain(this);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			switch (name) {
			case GameScene.GAME:
				scene.setCursor(javafx.scene.Cursor.NONE);
				this.currentScore = 0;
				break;
			case GameScene.SCORE:
				if (controller instanceof scoreController scoreController) {
					scoreController.updateScoreAndDisplay(currentScore);
				}
				break;
			default:
				break;
			}
			stage.resizableProperty().set(false);
			stage.setTitle("DUCK HUNT");
			stage.show();
			stage.setOnCloseRequest(this::exitProgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSaveScore(int score) {
		this.currentScore = score;
	}

	public double getVolume() {
		return volume;
	}

	public int getStartingLevel() {
		return startingLevel;
	}

	public boolean isTwoDucks() {
		return twoDucks;
	}

	public void setSettings(double volumePercent, int level, boolean twoDucksBool) {
		this.volume = volumePercent * 0.01f;
		this.startingLevel = level;
		this.twoDucks = twoDucksBool;

	}
}