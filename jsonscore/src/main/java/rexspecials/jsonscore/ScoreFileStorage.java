package rexspecials.jsonscore;

import java.io.File;
import java.io.IOException;

public class ScoreFileStorage implements ScoreStorageService {

	@Override
	public ScoreList getScoresFromFile() throws Exception {
		return ScoreFileHelper.getScoresFromFile("scores.json");
	}

	@Override
	public void init() {
		File file = new File("scores.json");
		if(!file.exists())
		{
			 System.out.println("Score file does not exist, creating... ");
			 try {
				ScoreFileHelper.saveScoresToFile("scores.json", new ScoreList());
			} catch (IOException e) {
				 System.out.println("Score file didnt create correctly, scores wont be saved.");
			}
		}
	}

	@Override
	public void saveScoresToFile(ScoreList scoreList) throws Exception {
		ScoreFileHelper.saveScoresToFile("scores.json", scoreList);
		
	}

	@Override
	public void saveScoreToFile(Score score) throws Exception {
		ScoreFileHelper.saveScoreToFile("scores.json", score);
		
	}

}
