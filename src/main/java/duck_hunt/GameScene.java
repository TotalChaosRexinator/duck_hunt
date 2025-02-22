package duck_hunt;

public enum GameScene {
	MENU("/menuWindow.fxml"),
    GAME("/gameWindow.fxml"),
    SCORE("/scoreWindow.fxml"),
    HIGHSCORE("/highscoreWindow.fxml"),
    OPTIONS("/optionsWindow.fxml");

    private final String fxmlFile;

    GameScene(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFileName() {
        return fxmlFile;
    }
}