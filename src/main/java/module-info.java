module duck_hunt {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
	requires jakarta.json.bind;
	requires jakarta.json;
    opens duck_hunt to javafx.fxml;
    exports duck_hunt;
}