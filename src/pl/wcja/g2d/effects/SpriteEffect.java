package pl.wcja.g2d.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Interpolator;

/**
 * Base sprite effect class
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public abstract class SpriteEffect {

	protected EffectCompletedListener listener;
	protected float duration;
	protected float invDuration;
	protected float taken;
	protected Sprite target;
	protected boolean done;
	protected Interpolator interpolator;

	public abstract void setTarget(Sprite sprite);

	public abstract void act(float f);

	public abstract boolean isDone();

	public void finish() {
		if (listener != null) {
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					listener.completed(new EffectCompletedEvent(SpriteEffect.this));
				}
			});
		}
		if (interpolator != null) {
			interpolator.finished();
		}
	}

	public SpriteEffect setInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	protected float createInterpolatedAlpha(float delta) {
		taken += delta;
		if (taken >= duration) {
			taken = duration;
			done = true;
			return taken;
		}
		if (interpolator == null) {
			return taken * invDuration;
		} else {
			float interpolatedTime = interpolator.getInterpolation(taken / duration) * duration;
			return interpolatedTime * invDuration;
		}
	}

	public void reset() {
		this.listener = null;
	}

	public SpriteEffect setCompletionListener(EffectCompletedListener listener) {
		this.listener = listener;
		return this;
	}

	public EffectCompletedListener getCompletionListener() {
		return listener;
	}
}
