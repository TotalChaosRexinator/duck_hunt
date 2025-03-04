module duck_hunt.libs.score {
	requires static lombok;
	requires jakarta.json.bind;
	requires jakarta.json;
	requires org.apache.logging.log4j;
	exports duck_hunt.libs.score;
	provides duck_hunt.libs.score.ScoreStorageService with duck_hunt.libs.score.ScoreFileStorage;
}