package duck_hunt;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Player extends WorldEntity {
	private final int size = 43;
	private final Image sprite = new Image(this.getClass().getResourceAsStream("/player/crosshair.png"));
	private AudioClip shotSound = new AudioClip(
			getClass().getResource("/sounds/shotgun/shtg_fire.mp3").toExternalForm());
	private AudioClip rackBack = new AudioClip(
			getClass().getResource("/sounds/shotgun/rack_back.mp3").toExternalForm());
	private final AudioClip rackForward = new AudioClip(
			getClass().getResource("/sounds/shotgun/rack_forward.mp3").toExternalForm());

	private Point2D position;
	private int slugs;
	private Point2D rackStart;
	private boolean racked;
	private boolean rackedBack;

	Player() {
		this.rackStart = new Point2D(0, 0);
		this.position = new Point2D(0, 0);
		this.slugs = 3;
	}

	Player(double volume) {
		this();
		this.shotSound.setVolume(volume);
		this.rackBack.setVolume(volume);
		this.rackForward.setVolume(volume);
	}

	public boolean canShoot() {
		if (slugs > 0 && racked) {
			return true;
		}
		return false;
	}

	public int getAmmo() {
		return this.slugs;
	}

	public void reload(int slugs) {
		this.slugs = slugs;
	}

	public void startRacking(Point2D start) {
		if (racked) {
			return;
		}
		this.rackStart = start;
	}

	public void setPosition(Point2D newPosition) {
		this.position = newPosition;
	}

	public void shootGun() {
		this.shotSound.play();
		this.slugs--;
		this.racked = false;
	}

	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(sprite, this.position.getX() - size / 2f, this.position.getY() - size / 2f, size, size);
	}

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(this.position.getX() - this.size / 4f, this.position.getY() - this.size / 4f,
				this.size / 2f, this.size / 2f);
	}

	public boolean intersects(Rectangle2D other) {
		return this.getBoundingBox().intersects(other);
	}

	public void checkRackBack(Point2D end) {
		if (racked) {
			return;
		}
		if (end.getY() - this.rackStart.getY() >= 200 && !this.rackedBack) {
			this.rackBack.play();
			this.rackStart = end;
			this.rackedBack = true;
		}
		if (this.rackedBack && this.rackStart.getY() - end.getY() >= 200) {
			this.rackForward.play();
			this.racked = true;
			this.rackedBack = false;
		}

	}
	public boolean isRackedBack()
	{
		return this.rackedBack;
	}

}
