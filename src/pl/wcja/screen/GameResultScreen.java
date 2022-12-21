package pl.wcja.screen;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Screen2D;
import pl.wcja.game.Settings;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class GameResultScreen extends Screen2D implements TextInputListener {

	private boolean win = false;
	private ExtSprite resultSprite = null;
	Texture tWin = null, tLose = null;
	private boolean soundPlayed = false;
	private int totalScore = 0;
	
	public GameResultScreen(IMainFrame mf, boolean win) {
		super(mf);
		this.win = win;
		Texture t = mf.getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		setBackgroundImage(t);
		tWin = mf.getCachedTexture("gfx/img_win.png", FileType.Internal, true);
		tWin.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		tLose = mf.getCachedTexture("gfx/img_lose.png", FileType.Internal, true);
		TextureRegion trResult = new TextureRegion(win ? tWin : tLose);
		tLose.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		resultSprite = new ExtSprite(mf, trResult, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1.0f);
		addTouchable(resultSprite);
	}

	public void setResult(boolean win, int score) {
		removeTouchable(resultSprite);
		this.win = win;
		this.totalScore = score;
		if(win) {
			resultSprite = new ExtSprite(mf, new TextureRegion(tWin), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1.0f);
			if(mf.getSettings().qualifiedForHiScore(totalScore)) {
				Gdx.input.getTextInput(this, String.format("Your score is %s", totalScore), mf.getSettings().getLastEnteredName());
				//a moze liczy punkty poprzez np. czas + liczba pokonanych statków + 
				//liczba wyprodukowanych surowców + albo * coœtam jeszcze?
			}
		} else {
			resultSprite = new ExtSprite(mf, new TextureRegion(tLose), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1.0f);
		}
		addTouchable(resultSprite);
	}
	
	@Override
	public void render(float f) {
		super.render(f);
		resultSprite.draw(mf.getSpriteBatch());
		//on first render - play sound
		if(!soundPlayed && win) {
			soundPlayed = true;
			mf.getCachedSound("snd/snd_win.ogg", FileType.Internal).play(mf.getSettings().getSoundVolume());
		} else if(!soundPlayed && !win) {
			soundPlayed = true;
			mf.getCachedSound("snd/snd_lose.ogg", FileType.Internal).play(mf.getSettings().getSoundVolume());
		}
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == resultSprite) {
			//mf.endGame();
			mf.pushScreen(new HiScoreScreen(mf));
		}
	}

	@Override
	public void input(String s) {
		if(!mf.getSettings().isDefaultEmptyName(s)) {
			mf.getSettings().addHiScore(new Settings.HiScoreEntry(s, totalScore));
			mf.getSettings().saveSettings();
			mf.pushScreen(new HiScoreScreen(mf));
		}
	}

	@Override
	public void cancled() {
		mf.pushScreen(new HiScoreScreen(mf));
	}
	
	@Override
	public void backKeyPressed() {
		mf.pushScreen(new HiScoreScreen(mf));
	}
}