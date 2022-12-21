package pl.wcja.screen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Screen2D;
import pl.wcja.g2d.SpectacularScreen2d;
import pl.wcja.g2d.controls.Button;
import pl.wcja.g2d.controls.Control;
import pl.wcja.g2d.controls.Control.TextAlignment;
import pl.wcja.g2d.controls.Label;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.g2d.effects.MoveBy;
import pl.wcja.g2d.effects.SpriteEffect;
import pl.wcja.game.Settings.HiScoreEntry;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.OvershootInterpolator;

/**
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 *
 */
public class HiScoreScreen extends SpectacularScreen2d {
	
	private Screen2D previousScreen = null;
	private ArrayList<Control> controls = new ArrayList<Control>();
	private Label lHiScoreTitle = null;
	private Label lHiScore = null;
	private Button bOk = null;
	
	public HiScoreScreen(IMainFrame mf) {
		super(mf);
		
		previousScreen = mf.getScreen();
		
		Texture t = mf.getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		setBackgroundImage(t);
		
		lHiScoreTitle = new Label(mf, "HiScore", 0, 0);
		lHiScoreTitle.setPosition(
				(Gdx.graphics.getWidth() - lHiScoreTitle.getBounds().width) / 2, 
				Gdx.graphics.getHeight() - lHiScoreTitle.getBounds().height - 8);
		
		lHiScore = new Label(mf, mf.getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal), "", 0, 0);
		String results = "";
		int place = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm/dd.MM.yy");
		for(HiScoreEntry hse : mf.getSettings().getHiScores()) {
			results += String.format("%s. %s %s (%s)\n", place++, hse.getScore(), hse.getName(), sdf.format(hse.getDate()));
		}
		lHiScore.setMultilineText(results, 0, 0, 1.0f, HAlignment.LEFT);
		lHiScore.setPosition(
				(Gdx.graphics.getWidth() - lHiScore.getBounds().width) / 2,
				(Gdx.graphics.getHeight() - lHiScore.getBounds().height) / 2);
		
		bOk = new Button(mf, mf.getTexturePack("buttons").findRegion("btn_empty_long"), mf.getTexturePack("buttons").findRegion("btn_empty_long_p"), 0, 0);
		bOk.setText("OK");
		bOk.setTextAlignment(TextAlignment.CENTER);
		addTouchable(bOk);
		bOk.setCenterPosition(Gdx.graphics.getWidth() / 2, 40);

		controls.add(lHiScoreTitle);
		controls.add(lHiScore);
		controls.add(bOk);
	}
	
	@Override
	public void render(float f) {
		super.render(f);
		lHiScoreTitle.draw(mf.getSpriteBatch());
		lHiScore.draw(mf.getSpriteBatch());
		bOk.draw(mf.getSpriteBatch());
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == bOk) {
			if(previousScreen == null) {
				mf.popScreen();
			} else {
				mf.endGame();	
			}
		}
	}
	
	@Override
	public void backKeyPressed() {
		if(previousScreen == null) {
			mf.popScreen();
		} else {
			mf.endGame();	
		}
	}


	@Override
	public void setupSpectacularInEffect() {
		float delay = 0.0f;
		for(Control b : controls) {
			float x = b.getX();
			b.setPosition(-b.getWidthScaled(), b.getY());
			b.addEffect(MoveBy.$(x + b.getWidthScaled(), 0, 1.0f + delay).setInterpolator(OvershootInterpolator.$(2.0f)));
			delay += 0.1f;
		}	
	}

	@Override
	public void setupSpectacularOutEffect() {
		float delay = 0.0f;
		for(Control b : controls) {
			float x = b.getX();
			SpriteEffect eff = MoveBy.$(Gdx.graphics.getWidth() - x, 0, 0.9f + delay).setInterpolator(DecelerateInterpolator.$(2.0f));
			b.addEffect(eff);
			delay += 0.1f;
			if(controls.get(controls.size() - 1) == b) {
				eff.setCompletionListener(new EffectCompletedListener() {
					@Override
					public void completed(EffectCompletedEvent evt) {
						fireOutEffectCompletedEvent(evt.getEffect());
					}
				});
			}
		}
	}
}