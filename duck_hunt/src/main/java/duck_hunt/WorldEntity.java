package duck_hunt;

import javafx.scene.canvas.GraphicsContext;

public abstract class WorldEntity implements Drawable, Collisionable {
	@Override
	public final void draw(GraphicsContext gc) {
		gc.save();
		drawInternal(gc);
		gc.restore();
	}

	protected abstract void drawInternal(GraphicsContext gc);
}