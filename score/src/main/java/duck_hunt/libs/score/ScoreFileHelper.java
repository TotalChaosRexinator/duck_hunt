package duck_hunt.libs.score;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

public final class ScoreFileHelper {

	private ScoreFileHelper() {

	}

	static ScoreList getScoresFromFile(String fileName) throws IOException {
		try (Reader reader = new FileReader(fileName);
				Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));) {
			return jsonb.fromJson(reader, ScoreList.class);

		} catch (Exception e) {
			throw new IOException("Failed to parse JSON", e);
		}
	}

	static void saveScoresToFile(String fileName, ScoreList scoreList) throws IOException {
		try {
			scoreList.setScores(getScoresFromFile(fileName).getScores());
		} catch (IOException e) {
			System.err.print("File does not exist or is empty, rewriting.");
		}
		try (FileWriter writer = new FileWriter(fileName);
				Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));) {
			jsonb.toJson(scoreList, writer);
		} catch (IOException e) {
			throw new IOException("Failed to write to file: " + fileName, e);
		} catch (JsonbException e) {
			throw new IOException("Failed to serialize JSON", e);
		} catch (Exception e) {
			throw new IOException("Failed to close JSON", e);
		}
	}

	static void saveScoreToFile(String fileName, Score score) throws IOException {
		ScoreList scoreList = new ScoreList();
		scoreList.addScore(score);
		ScoreFileHelper.saveScoresToFile(fileName, scoreList);
	}
}
