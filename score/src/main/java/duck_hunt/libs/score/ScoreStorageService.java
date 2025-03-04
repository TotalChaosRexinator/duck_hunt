package duck_hunt.libs.score;

public interface ScoreStorageService {
	ScoreList getScoresFromFile() throws Exception;
	void init();
	void saveScoresToFile(ScoreList scoreList) throws Exception;
	void saveScoreToFile(Score score) throws Exception;

}
