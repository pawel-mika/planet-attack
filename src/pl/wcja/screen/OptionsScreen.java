package pl.wcja.screen;

import java.util.ArrayList;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.SpectacularScreen2d;
import pl.wcja.g2d.controls.Button;
import pl.wcja.g2d.controls.Checkbox;
import pl.wcja.g2d.controls.Control;
import pl.wcja.g2d.controls.Control.TextAlignment;
import pl.wcja.g2d.controls.Group;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.g2d.effects.MoveBy;
import pl.wcja.g2d.effects.SpriteEffect;
import pl.wcja.game.ai.AILevel;
import pl.wcja.utils.SpriteUtils;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.OvershootInterpolator;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class OptionsScreen extends SpectacularScreen2d {
	
	private boolean initialized = false;
	private ArrayList<Button> buttons = new ArrayList<Button>(); 
	private Button bBack = null;
	private Checkbox cbEasy = null;
	private Checkbox cbNormal = null;
	private Checkbox cbHard = null;
	private Checkbox cbBrainiac = null;

	public OptionsScreen(IMainFrame mf) {
		super(mf);
		Texture t = mf.getCachedTexture("gfx/bg_manual.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		setBackgroundImage(t);

		bBack = new Button(mf, mf.getTexturePack("buttons").findRegion("btn_empty_long"), mf.getTexturePack("buttons").findRegion("btn_empty_long_p"), 0, 0);
		bBack.setText("BACK");
		bBack.setTextAlignment(TextAlignment.CENTER);
		addTouchable(bBack);
		buttons.add(bBack);
						
		cbEasy = createCheckbox("EASY");
		cbNormal = createCheckbox("NORMAL");
		cbHard = createCheckbox("HARD");
		cbBrainiac = createCheckbox("BRAINIAC");
		buttons.add(cbEasy);
		buttons.add(cbNormal);
		buttons.add(cbHard);
		buttons.add(cbBrainiac);
		
		Group levelGroup = new Group();
		levelGroup.addControl(cbEasy);
		levelGroup.addControl(cbNormal);
		levelGroup.addControl(cbHard);
		levelGroup.addControl(cbBrainiac);
						
		buttons = (ArrayList<Button>) SpriteUtils.arrangeVertical(buttons, 0, 8);
		Rectangle r = SpriteUtils.getGroupSize(buttons);
		buttons = (ArrayList<Button>) SpriteUtils.moveAllBy(buttons, (Gdx.graphics.getWidth() - r.width) / 2, (Gdx.graphics.getHeight() - r.height) / 2);
		
		levelGroup.setPosition(levelGroup.getX(), levelGroup.getY() - 32);
		
		mf.setLoading(false);
		initialized = true;
		
		setupCheckboxState();
	}
	
	private Checkbox createCheckbox(String text) {
		Checkbox cb = new Checkbox(mf, mf.getTexturePack("buttons").findRegion("checkbox"), mf.getTexturePack("buttons").findRegion("checkbox_p"), 0, 0);
		cb.setText(text);
		cb.setTextAlignment(TextAlignment.RIGHT);
		addTouchable(cb);
		return cb;
	}
	
	@Override
	public void render(float f) {
		super.render(f);
		if(initialized) {
			renderButtons();
		}
	}
	
	private void renderButtons() {
		for(Control b : buttons) {
			b.draw(mf.getSpriteBatch());
		}
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == bBack) {
			mf.popScreen();
		} else if(s instanceof Checkbox) {
			checkboxSelected((Checkbox)s);
		}
	}
	
	private void checkboxSelected(Checkbox cb) {
		for(Control b : buttons) {
			if(b instanceof Checkbox) {
				((Checkbox)b).setSelected(false);
			}
		}
		cb.setSelected(true);
		if(cbEasy.isSelected()) {
			mf.getSettings().setAiLevel(AILevel.EASY);
		} else if(cbNormal.isSelected()) {
			mf.getSettings().setAiLevel(AILevel.NORMAL);
		} else if(cbHard.isSelected()) {
			mf.getSettings().setAiLevel(AILevel.HARD);
		} else if(cbBrainiac.isSelected()) {
			mf.getSettings().setAiLevel(AILevel.MEGAMIND);
		}
	}
	
	private void setupCheckboxState() {
		switch(mf.getSettings().getAiLevel()) {
			case EASY:
				cbEasy.setSelected(true);
				break;
			case NORMAL:
				cbNormal.setSelected(true);
				break;
			case HARD:
				cbHard.setSelected(true);
				break;
			case MEGAMIND:
				cbBrainiac.setSelected(true);
				break;
		}
	}

	@Override
	public void setupSpectacularInEffect() {
//		bBack.setPosition(bBack.getX(), bBack.getY() - 256);
		float delay = 0.0f;
		for(Button b : buttons) {
			float x = b.getX();
			b.setPosition(-b.getWidthScaled(), b.getY());
			b.addEffect(MoveBy.$(x + b.getWidthScaled(), 0, 1.0f + delay).setInterpolator(OvershootInterpolator.$(2.0f)));
			delay += 0.1f;
		}	
	}

	@Override
	public void setupSpectacularOutEffect() {
		float delay = 0.0f;
		for(Button b : buttons) {
			float x = b.getX();
//			b.setPosition(-b.getWidthScaled(), b.getY());
			SpriteEffect eff = MoveBy.$(Gdx.graphics.getWidth() - x, 0, 0.9f + delay).setInterpolator(DecelerateInterpolator.$(2.0f));
			b.addEffect(eff);
			delay += 0.1f;
			if(buttons.get(buttons.size() - 1) == b) {
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
