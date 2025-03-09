import rexspecials.jsonscore.ScoreStorageService;

module duck_hunt {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
	requires jakarta.json.bind;
	requires jakarta.json;
	requires rexspecials.jsonscore;
	requires org.apache.logging.log4j;
	requires org.apache.logging.log4j.core;
    opens duck_hunt to javafx.fxml;
    exports duck_hunt;
    uses ScoreStorageService;
}