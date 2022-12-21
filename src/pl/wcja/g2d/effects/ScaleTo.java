package pl.wcja.g2d.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 *
 */
public class ScaleTo extends SpriteEffect {

	protected float scaleX;
	protected float scaleY;
	protected float startScaleX;
	protected float startScaleY;
	protected float deltaScaleX;
	protected float deltaScaleY;
	
	public ScaleTo() {
	}

	public static ScaleTo $(float scaleX, float scaleY, float duration) {
		ScaleTo action = (ScaleTo) pool.obtain();
		action.scaleX = scaleX;
		action.scaleY = scaleY;
		action.duration = duration;
		action.invDuration = 1.0F / duration;
		return action;
	}

	public void setTarget(Sprite actor) {
		target = actor;
		startScaleX = target.getScaleX();
		deltaScaleX = scaleX - target.getScaleX();
		startScaleY = target.getScaleY();
		deltaScaleY = scaleY - target.getScaleY();
		taken = 0.0F;
		done = false;
	}

	public void act(float delta) {
		float alpha = createInterpolatedAlpha(delta);
		if (done) {
			target.setScale(scaleX, scaleY);
		} else {
			target.setScale(startScaleX + deltaScaleX * alpha, startScaleY + deltaScaleY * alpha);
		}
	}

	public void finish() {
		super.finish();
		pool.free(this);
	}

	public SpriteEffect copy() {
		ScaleTo scaleTo = $(scaleX, scaleY, duration);
		if (interpolator != null)
			scaleTo.setInterpolator(interpolator.copy());
		return scaleTo;
	}

	private static final EffectResetingPool pool = new EffectResetingPool(4, 100) {
		protected RotateBy newObject() {
			return new RotateBy();
		}
	};

	@Override
	public boolean isDone() {
		return done;
	}

}
