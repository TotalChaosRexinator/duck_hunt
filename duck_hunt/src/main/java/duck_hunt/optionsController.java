package duck_hunt;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class optionsController implements Controller {
	private Main main;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button backButton;

	@FXML
	private CheckBox duckCheckbox;

	@FXML
	private Slider levelSlider;

	@FXML
	private Slider volumeSlider;

	@FXML
	void goBack(ActionEvent event) {
		try {
			this.main.setSettings(this.volumeSlider.getValue(), (int) this.levelSlider.getValue(),
					this.duckCheckbox.isSelected());

			this.main.changeTo(GameScene.MENU);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void initialize() {
		assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'optionsWindow.fxml'.";
		assert duckCheckbox != null
				: "fx:id=\"duckCheckbox\" was not injected: check your FXML file 'optionsWindow.fxml'.";
		assert levelSlider != null
				: "fx:id=\"levelSlider\" was not injected: check your FXML file 'optionsWindow.fxml'.";
		assert volumeSlider != null
				: "fx:id=\"volumeSlider\" was not injected: check your FXML file 'optionsWindow.fxml'.";
		duckCheckbox.setIndeterminate(false);

	}

	@Override
	public void setMain(Main main) {
		this.main = main;
		this.volumeSlider.setValue(this.main.getVolume()*100);
		this.levelSlider.setValue(this.main.getStartingLevel());
		this.duckCheckbox.setSelected(this.main.isTwoDucks());
	}

}
