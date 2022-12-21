package pl.wcja.g2d.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class FadeOut extends SpriteEffect {

	protected float startAlpha;
	protected float deltaAlpha;

	public FadeOut() {
		startAlpha = 0.0F;
		deltaAlpha = 0.0F;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	public static FadeOut $(float duration) {
		FadeOut action = (FadeOut) pool.obtain();
		action.duration = duration;
		action.invDuration = 1.0F / duration;
		return action;
	}

	public void setTarget(Sprite actor) {
		target = actor;
		target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, 1.0f);
		startAlpha = 1.0F;
		deltaAlpha = -1F;
		taken = 0.0F;
		done = false;
	}

	public void act(float delta) {
		float alpha = createInterpolatedAlpha(delta);
		if (done)
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, 0.0f);
		else
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, startAlpha + deltaAlpha * alpha);
	}

	public void finish() {
		super.finish();
		pool.free(this);
	}

	public SpriteEffect copy() {
		FadeOut fadeOut = $(duration);
		if (interpolator != null)
			fadeOut.setInterpolator(interpolator.copy());
		return fadeOut;
	}

	private static final EffectResetingPool pool = new EffectResetingPool(4, 100) {
		protected FadeOut newObject() {
			return new FadeOut();
		}
	};
}