module rexspecials.jsonscore {
	requires static lombok;
	requires jakarta.json.bind;
	requires jakarta.json;
	requires org.apache.logging.log4j;
	exports rexspecials.jsonscore;
	provides rexspecials.jsonscore.ScoreStorageService with rexspecials.jsonscore.ScoreFileStorage;
}