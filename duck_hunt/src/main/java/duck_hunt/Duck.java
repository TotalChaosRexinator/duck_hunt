package duck_hunt;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.util.Random;

public class Duck extends WorldEntity {

	private AudioClip quack = new AudioClip(getClass().getResource("/sounds/ducks/quack.wav").toExternalForm());
	private AudioClip flap = new AudioClip(getClass().getResource("/sounds/ducks/flap.wav").toExternalForm());
	private AudioClip fall = new AudioClip(getClass().getResource("/sounds/ducks/fall.wav").toExternalForm());
	private AudioClip impact = new AudioClip(getClass().getResource("/sounds/ducks/impact.wav").toExternalForm());
	private final Image duckHorizontal = new Image(
			this.getClass().getResourceAsStream("/duck_sprites/duck_horizontal.gif"));
	private final Image duckFall = new Image(this.getClass().getResourceAsStream("/duck_sprites/duck_fall.gif"));
	private final Image duckShot = new Image(this.getClass().getResourceAsStream("/duck_sprites/duck_shot.gif"));
	private final Image duckDiagonal = new Image(
			this.getClass().getResourceAsStream("/duck_sprites/duck_diagonal.gif"));
	private final int size = 90;

	private Image sprite; // ducks current sprite
	private Point2D position; // ducks local position
	private Point2D speed; // ducks speed
	private double angle; // angle/direction of the ducks movement
	private boolean shot; // is duck dead?
	private boolean falling;// is duck falling?
	private boolean flyOff; // is duck leaving?
	private double helpTimer;// timer for randomness
	private double randomAngleTime; // when to change angle randomly in seconds?
	private double flapTimer;
	private int levelHardness; // how evasive is the duck?
	private GameLoop gameLoop;
	private Random random = new Random();
	public Duck() // duck default constructor
	{
		this.sprite = this.duckHorizontal;
		this.position = new Point2D(0, 0);
		this.speed = new Point2D(100, 100);
		this.shot = false;
		this.falling = false;
		this.flyOff = false;
		this.angle = 45;
		this.helpTimer = 0;
		this.flapTimer = 0;
		this.randomAngleTime = 2;
		this.changeSprite();
	}

	public Duck(int canvasWidth, int canvasHeight, GameLoop gl, int level, double volume) {
		this();
		this.gameLoop = gl;
		this.position = new Point2D(this.random.nextInt(canvasWidth), canvasHeight - this.size);
		this.speed = new Point2D(this.random.nextDouble(200) + 100 * level, this.random.nextDouble(200) + 100 * level);
		this.angle = random.nextDouble(100) + 45;
		this.levelHardness = level;
		this.randomAngleTime = Math.max(0.25f, random.nextDouble(2 - 0.1 * this.levelHardness));
		this.quack.setVolume(volume);
		this.flap.setVolume(volume);
		this.fall.setVolume(volume);
		this.impact.setVolume(volume);
		this.changeSprite();
	}

	public void drawInternal(GraphicsContext gc) { // draws the duck on canvas, but also checks the ducks direction and
													// flips him accordingly

		if (this.angle >= 90 && this.angle <= 270) {
			gc.drawImage(sprite, this.position.getX() - size / 2f + size, this.position.getY() - size / 2f, -size, size);
		} else {
			gc.drawImage(sprite, this.position.getX() - size / 2f, this.position.getY() - size / 2f, size, size);
		}
	}

	private void changeSprite() // changes sprite based on angle for better immersion
	{
		if (this.falling) {
			this.sprite = this.duckFall;
			return;
		}
		if (this.shot) {
			this.sprite = this.duckShot;
			return;
		}
		double tempSin = Math.sin(Math.toRadians(angle));
		if (Math.abs(tempSin) < 0.57) {

			this.sprite = this.duckHorizontal;
		} else {
			this.sprite = this.duckDiagonal;
		}

	}

	private double angleNormalize(double angle) // helper function to normalize generated angles
	{
		angle = angle % 360;
		return angle < 0 ? angle + 360 : angle;
	}

	public void simulate(double deltaT, int canvasWidth, int canvasHeight) {// simulates ducks movement randomly, changing his
																		// direction based on hitting the wall or
		if (this.flyOff || this.shot) { // if duck is shot, or falling, just check if the duck is out of bounds and
										// delete it
			if ((this.position.getX() >= canvasWidth || this.position.getX() <= 0)
					|| (this.position.getY() <= 0 || this.position.getY() >= canvasHeight)) {
				fall.stop();
				if (this.shot) {
					impact.play();
				}
				this.gameLoop.remove(this);
			}
		}
		this.helpTimer += 1 * deltaT;// timer incrementation
		this.flapTimer += 1 * deltaT;
		if (this.shot) { // check if duck is is shot, if it is, use the helptimer, which has been
							// resetted to hold it in place for 0.5 seconds, then make it fall
			if (helpTimer > 0.5) {
				if (this.falling == false) {
					this.falling = true;
					fall.play();
					this.changeSprite();
				}
				this.position = new Point2D(this.position.getX(), this.position.getY() + 500 * deltaT);
			}
			return;
		}
		// duck position calculation
		double tempX = this.position.getX() + (this.speed.getX() * Math.cos(Math.toRadians(angle)) * deltaT);
		double tempY = this.position.getY() - (this.speed.getY() * Math.sin(Math.toRadians(angle)) * deltaT);
		if (this.flapTimer >= 0.25) {// flap timer so the flap is not every miliseconds
			flap.play();
			this.flapTimer = 0; // Reset the timer
		}
		// main simulation code
		if (!this.flyOff) { // skip if is flying off
			if (tempX >= canvasWidth || tempX <= 0) { // checks vertical borders
				this.angle = 180 - this.angle;
				this.angle += random.nextDouble(161) - 80;
				this.angle = this.angleNormalize(this.angle);
				tempX = this.position.getX() + ((this.speed.getX() * Math.cos(Math.toRadians(angle)) * deltaT));
				tempX = Math.clamp(tempX, 0,canvasWidth);
				this.changeSprite();
			}
			if (tempY <= 0 || tempY >= canvasHeight) { // checks horizontal borders
				this.angle = -this.angle + 360;
				this.angle += random.nextDouble(161) - 80;
				this.angle = this.angleNormalize(this.angle);
				tempY = this.position.getY() - ((this.speed.getY() * Math.sin(Math.toRadians(angle)) * deltaT));
				tempY = Math.clamp(tempY,0,canvasHeight);
				this.changeSprite();
			} // this code below checks if the duck isnt too close to the borders, if not,
				// gives the duck random angle after random time
			if (this.position.getX() > size/2 && this.position.getX() < canvasWidth - size/2 && this.position.getY() > size/2
					&& this.position.getY() < canvasHeight - size/2) {
				if (this.helpTimer >= this.randomAngleTime) {
					this.quack.play();
					this.angle = random.nextDouble(360);
					this.helpTimer = 0;
					this.randomAngleTime = Math.max(0.25f, random.nextDouble(2 - 0.1 * this.levelHardness));
					this.changeSprite();
				}
			}
		}

		this.position = new Point2D(tempX, tempY); // finally, update ducks current position
	}

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(this.position.getX() - this.size / 2f, this.position.getY() - this.size / 2f, this.size,
				this.size);
	}

	public boolean intersects(Rectangle2D other) {
		return this.getBoundingBox().intersects(other);
	}

	public void flyOff() {// makes the duck leave the canvas
		this.flyOff = true;
		this.angle = 90;
		this.changeSprite();
	}

	public void hit() { // calls gameloops method to handle score and game logic accordingly
		if (!this.shot) {
			this.gameLoop.duckShot();
			this.shot = true;
			this.helpTimer = 0;
			this.changeSprite();
		}

	}

	public boolean isShot() { // checker for ducks if they are shot
		return this.shot;
	}

}
