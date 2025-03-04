package duck_hunt;

import java.net.URL;
import java.util.ResourceBundle;

import duck_hunt.libs.score.Score;
import duck_hunt.libs.score.ScoreFileStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HighscoreController implements Controller {
	private Main main;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button backButton;

	@FXML
	private TableColumn<Score, String> nickColumn;

	@FXML
	private TableColumn<Score, Integer> pointsColumn;

	@FXML
	private TableView<Score> score;

	@FXML

	void goBack(ActionEvent event) {
		try {
			this.main.changeTo(GameScene.MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		assert backButton != null
				: "fx:id=\"backButton\" was not injected: check your FXML file 'highscoreWindow.fxml'.";
		assert nickColumn != null
				: "fx:id=\"nickColumn\" was not injected: check your FXML file 'highscoreWindow.fxml'.";
		assert pointsColumn != null
				: "fx:id=\"pointsColumn\" was not injected: check your FXML file 'highscoreWindow.fxml'.";
		assert score != null : "fx:id=\"score\" was not injected: check your FXML file 'highscoreWindow.fxml'.";
		ScoreFileStorage scoreStorageService = new ScoreFileStorage();
		scoreStorageService.init();
		nickColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

		try {
			score.getItems().addAll(scoreStorageService.getScoresFromFile().getScores());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setMain(Main main) {
		this.main = main;
	}

}
