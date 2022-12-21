package pl.wcja.g2d.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class MoveBy extends SpriteEffect {

	protected float initialX;
	protected float initialY;
	protected float x;
	protected float y;
	protected float startX;
	protected float startY;
	protected float deltaX;
	protected float deltaY;
	
	public boolean isDone() {
		return done;
	}

	public static MoveBy $(float x, float y, float duration) {
		MoveBy action = (MoveBy) pool.obtain();
		action.x = action.initialX = x;
		action.y = action.initialY = y;
		action.duration = duration;
		action.invDuration = 1.0F / duration;
		return action;
	}

	public void setTarget(Sprite sprite) {
		target = sprite;
		startX = target.getX();
		startY = target.getY();
		deltaX = x;
		deltaY = y;
		x = target.getX() + x;
		y = target.getY() + y;
		taken = 0.0F;
		done = false;
	}

	public void act(float delta) {
		float alpha = createInterpolatedAlpha(delta);
		if (done) {
			target.setPosition(x, y);
		} else {
			target.setPosition(startX + deltaX * alpha, startY + deltaY * alpha);
		}
	}

	public void finish() {
		super.finish();
		pool.free(this);
	}

	public SpriteEffect copy() {
		MoveBy moveBy = $(initialX, initialY, duration);
		if (interpolator != null)
			moveBy.setInterpolator(interpolator.copy());
		return moveBy;
	}

	private static final EffectResetingPool pool = new EffectResetingPool(4, 100) {
		protected MoveBy newObject() {
			return new MoveBy();
		}
	};
}