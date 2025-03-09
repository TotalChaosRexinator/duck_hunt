package duck_hunt;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.geometry.Point2D;

public class GameLoop extends AnimationTimer {

	private final int canvasWidth = 1280;
	private final int canvasHeight = 720;
	private final int duckScore = 500;

	private final Canvas canvas;
	private final GraphicsContext gc;
	private final GameController controller;

	private final Image background = new Image(this.getClass().getResourceAsStream("/stages/background.png"));
	private final Image foreground = new Image(this.getClass().getResourceAsStream("/stages/foreground.gif"));
	private final Image pause = new Image(this.getClass().getResourceAsStream("/stages/pause.png"));
	private AudioClip jingle = new AudioClip(getClass().getResource("/sounds/UI/jingle.wav").toExternalForm());

	private long lastTime;
	private int duckPerWave = 2;
	private Player player;
	private int score;
	private double gameVolume = 100;
	// level logic
	private int level = 1;
	private int ducksShot = 0;
	private int ducksSpawned = 0;
	private int ducksToSpawn = 10;
	private int ducksNeeded = 1;
	private boolean isPaused;
	private double flyAwayTimer = 0;
	private final double flyAwayTime = 5f;
	private List<WorldEntity> entities;
	private List<WorldEntity> entitiesToAdd;
	private List<WorldEntity> entitiesToRemove;

	public GameLoop(Canvas canvas, GameController c) {

		this.canvas = canvas;
		this.controller = c;
		this.gc = canvas.getGraphicsContext2D();
		entities = new ArrayList<>();
		entitiesToAdd = new ArrayList<>();
		entitiesToRemove = new ArrayList<>();

		score = 0;
		try {
			this.duckPerWave = controller.isTwoDucksGame() ? 2 : 1;
			this.level = controller.getStartingLevel();
			this.gameVolume = controller.getGameVolume();
		} catch (Exception e) {
			System.err.print("Couldnt load settings, using defaults");
		}
		player = new Player(this.gameVolume);
		jingle.setVolume(this.gameVolume);
		this.controller.updateDucksUI(ducksShot, 10, ducksNeeded, level);
		lastTime = System.nanoTime();

		this.canvas.setOnKeyPressed(this::handleKeyPress);
		this.canvas.setOnMouseDragged(this::handleMouseDrag);
		this.canvas.setOnMousePressed(this::handleMousePress);
		this.canvas.setOnMouseMoved(event -> player.setPosition(new Point2D(event.getX(), event.getY())));
		this.canvas.setOnMouseReleased(event -> controller.setShotgunAnimation(1));
	}

	/**
	 * Draws objects into the canvas. Put you code here.
	 */
	@Override
	public void handle(long now) {

		if (isPaused) {
			this.gc.drawImage(pause, (canvas.getWidth() / 2) - (pause.getWidth() / 2),
					(canvas.getHeight() / 2) - (pause.getHeight() / 2));
			lastTime = now;
			return;
		}
		double deltaT = (now - lastTime) / 1e9;
		lastTime = now;
		this.updateGameState(deltaT);
		this.updateUI();
		this.renderer();
		entities.removeAll(entitiesToRemove);
		entities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
		entitiesToRemove.clear();

	}

	private void renderer() {
		this.gc.clearRect(0, 0, canvasWidth, canvasHeight); // clear canvas
		this.gc.drawImage(background, 0, 0, canvasWidth, canvasHeight); // render background
		for (WorldEntity x : entities) { // render ducks
			x.draw(gc);
		}
		this.gc.drawImage(foreground, 0, 0, canvasWidth, canvasHeight); // render foreground
		this.player.draw(gc); // render player
	}

	private void updateUI() {
		controller.updateSlugs(player.getAmmo());
		controller.updateScoreboard(score);
		controller.updateDucksUI(ducksShot, 10, ducksNeeded, level);
	}

	private void updateGameState(double deltaT) {
		for (WorldEntity x : entities) { // simulate ducks
			if (x instanceof Duck duck) {
				duck.simulate(deltaT, (int) this.canvas.getWidth(), (int) this.canvas.getHeight() - 200);
			}
		}
		this.flyAwayTimer += 1 * deltaT;
		if (this.flyAwayTimer >= this.flyAwayTime && !entities.stream().filter(Duck.class::isInstance)
				.allMatch(duck -> ((Duck) duck).isShot())) {
			this.controller.showFlyAway();
			this.freeAllDucks();
		}

		if (entities.stream().anyMatch(Duck.class::isInstance)) { // there are no more ducks
			return;
		}
		controller.hideFlyAway();

		if (ducksSpawned >= ducksToSpawn) { // marks the end of the level

			if (ducksShot < ducksNeeded) { // marks loosing state

				controller.goToScore(score); // change to score
			}

			if (ducksShot == ducksToSpawn) { // bonus if all ducks are shot
				score += 1000;
			}

			jingle.play();
			ducksShot = 0;
			ducksSpawned = 0;
			if (ducksNeeded < ducksToSpawn) {
				ducksNeeded += 1;
			}
			level += 1;
		}
		// reload player and spawn ducks
		this.flyAwayTimer = 0;
		player.reload(3);
		for (int i = 0; i < duckPerWave; i++) {
			entities.add(new Duck((int) this.canvas.getWidth(), (int) this.canvas.getHeight() - 200, this, this.level,
					this.gameVolume));
			ducksSpawned++;
		}

	}

	private void handleKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			isPaused = !isPaused;
		}
		if (event.getCode() == KeyCode.END && isPaused) {
			controller.goToScore(score);
		}
	}

	private void handleMouseDrag(MouseEvent event) {
		if (isPaused) {
			return;
		}
		if (event.isSecondaryButtonDown()) {
			player.checkRackBack(new Point2D(event.getX(), event.getY()));
			if (player.isRackedBack()) {
				controller.setShotgunAnimation(2);
			} else {
				controller.setShotgunAnimation(3);
			}

		} else {
			player.setPosition(new Point2D(event.getX(), event.getY()));
		}

	}

	private void handleMousePress(MouseEvent event) {

		if (isPaused) {
			return;
		}

		if (event.isSecondaryButtonDown()) {
			player.startRacking(new Point2D(event.getX(), event.getY()));
			controller.setShotgunAnimation(2);
		} else if (event.isPrimaryButtonDown()) {
			this.handleShooting();
		}

	}

	private void handleShooting() {
		if (!player.canShoot()) {
			return;
		}
		player.shootGun();
		controller.setShotgunAnimation(4);
		for (WorldEntity x : entities) {
			if (x instanceof Collisionable collisionable && x instanceof Duck duck) {
				if (collisionable.intersects(player.getBoundingBox())) {
					duck.hit();
				}
			}
		}

		if (player.getAmmo() <= 0) {
			if (!entities.stream().filter(Duck.class::isInstance).allMatch(duck -> ((Duck) duck).isShot())) {
				controller.showFlyAway();
			}
			this.freeAllDucks();
		}
		controller.updateScoreboard(score);
		controller.updateDucksUI(ducksShot, 10, ducksNeeded, level);
	}

	public void add(WorldEntity entity)

	{
		entitiesToAdd.add(entity);
	}

	public void remove(WorldEntity entity)

	{
		entitiesToRemove.add(entity);
	}

	public void duckShot() {
		score += duckScore;
		ducksShot++;
	}

	public void freeAllDucks() {

		for (WorldEntity x : entities) {
			if (x instanceof Duck duck) {
				duck.flyOff();
			}
		}
	}

}
