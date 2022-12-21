package pl.wcja.screen;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.SpectacularScreen2d;
import pl.wcja.g2d.controls.Image;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.g2d.effects.MoveBy;
import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class SpectacularManual extends SpectacularScreen2d {

	private Image manual1 = null;
	private boolean initialized = false;
	
	public SpectacularManual(IMainFrame mf) {
		super(mf);
		Texture t = mf.getCachedTexture("gfx/bg_manual.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		setBackgroundImage(t);
		t = mf.getCachedTexture("gfx/manual.png", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		TextureRegion tr = new TextureRegion(t, 0, 0, 320, 480);
		manual1 = new Image(mf, tr, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		addTouchable(manual1);
		mf.setLoading(false);
		initialized = true;
	}
	
	@Override
	public void render(float f) {
		super.render(f);
		if(initialized) {
			manual1.draw(mf.getSpriteBatch());
		}
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == manual1) {
			mf.popScreen();
		}
	}

	@Override
	public void setupSpectacularInEffect() {
		manual1.setCenterPosition(Gdx.graphics.getWidth() / 2, 0 - manual1.getHeight() / 2);
		manual1.addEffect(MoveBy.$(0, Gdx.graphics.getHeight() / 2 + manual1.getHeight() / 2, 0.5f).setInterpolator(
				DecelerateInterpolator.$(1.0f)));
	}

	@Override
	public void setupSpectacularOutEffect() {
		SpriteEffect eff = MoveBy.$(0, Gdx.graphics.getHeight() / 2 + manual1.getHeight() / 2, 0.5f).setInterpolator(
				AccelerateInterpolator.$(1.0f));
		eff.setCompletionListener(new EffectCompletedListener() {
			@Override
			public void completed(EffectCompletedEvent evt) {
				fireOutEffectCompletedEvent(evt.getEffect());
			}
		});
		manual1.addEffect(eff);
	}
}