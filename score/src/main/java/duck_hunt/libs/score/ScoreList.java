package duck_hunt.libs.score;

import java.util.ArrayList;
import java.util.List;

public class ScoreList {

	private List<Score> scores;

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		if (this.scores != null) {
			this.scores.addAll(scores);
			this.sortScoresDescending();
		} else {
			this.scores = new ArrayList<>();
			this.scores.addAll(scores);
			this.sortScoresDescending();
		}
	}

	public ScoreList(List<Score> scores) {
		this.scores = scores;
		this.sortScoresDescending();
	}

	public ScoreList() {
		this.scores = new ArrayList<>();
	}

	public void addScore(Score score) {
		if (this.scores != null) {
			this.scores.add(score);
			this.sortScoresDescending();
		} else {
			this.scores = new ArrayList<>();
			this.scores.add(score);
			this.sortScoresDescending();
		}
	}

	private void sortScoresDescending() {
		scores.sort((s1, s2) -> Integer.compare(s2.getPoints(), s1.getPoints()));
	}

}