package duck_hunt;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
	public Rectangle2D getBoundingBox();
	public boolean intersects(Rectangle2D other);
}