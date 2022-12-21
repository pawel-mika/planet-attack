package pl.wcja.g2d.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 * 
 */
public class RotateBy extends SpriteEffect {

	protected float rotation;
	protected float startRotation;
	protected float deltaRotation;

	@Override
	public boolean isDone() {
		return done;
	}

	public RotateBy() {
	}

	public static RotateBy $(float rotation, float duration) {
		RotateBy action = (RotateBy) pool.obtain();
		action.rotation = rotation;
		action.duration = duration;
		action.invDuration = 1.0F / duration;
		return action;
	}

	public void setTarget(Sprite sprite) {
		target = sprite;
		startRotation = target.getRotation();
		deltaRotation = rotation;
		taken = 0.0F;
		done = false;
	}

	public void act(float delta) {
		float alpha = createInterpolatedAlpha(delta);
		if (done)
			target.setRotation(startRotation + rotation);
		else
			target.setRotation(startRotation + deltaRotation * alpha);
	}

	public void finish() {
		super.finish();
		pool.free(this);
	}

	public SpriteEffect copy() {
		RotateBy rotateBy = $(rotation, duration);
		if (interpolator != null)
			rotateBy.setInterpolator(interpolator.copy());
		return rotateBy;
	}

	private static final EffectResetingPool pool = new EffectResetingPool(4, 100) {
		protected RotateBy newObject() {
			return new RotateBy();
		}
	};

}
