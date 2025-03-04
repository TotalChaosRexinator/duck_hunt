package duck_hunt.libs.score;

public class Score {
	String name;
	int points;

	public Score() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Score(String name, int points) {
		this.name = name;
		this.points = points;
	}

	@Override
	public String toString() {
		return this.name + ";" + Integer.toString(this.points);
	}
}
