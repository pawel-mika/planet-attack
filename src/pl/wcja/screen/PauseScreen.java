package pl.wcja.screen;

import pl.wcja.IMainFrame;
import pl.wcja.Loadable;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Screen2D;
import pl.wcja.g2d.controls.Button;
import pl.wcja.g2d.controls.Control.TextAlignment;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class PauseScreen extends Screen2D {

	private GameMainScreen gms = null;
	private boolean initialized = false;
	private Button bContinue = null;
	private Button bEndGame = null;
	
	public PauseScreen(IMainFrame mf) {
		super(mf);
		mf.addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				Texture t = mf.getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
				t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
				setBackgroundImage(t);
			}
		});
		mf.addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				bContinue = new Button(mf, 
						mf.getTexturePack("buttons").findRegion("btn_empty_long"),
						mf.getTexturePack("buttons").findRegion("btn_empty_long_p"), 
						0, 0);
				bContinue.setText("CONTINUE");
				bContinue.setTextAlignment(TextAlignment.CENTER);
				addTouchable(bContinue);
				bEndGame = new Button(mf, 
						mf.getTexturePack("buttons").findRegion("btn_empty_long"), 
						mf.getTexturePack("buttons").findRegion("btn_empty_long_p"), 
						0,0);
				bEndGame.setText("END GAME");
				bEndGame.setTextAlignment(TextAlignment.CENTER);
				addTouchable(bEndGame);
				initialized = true;
				resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
		});
	}
	
	public void setGameMainScreen(GameMainScreen gms) {
		this.gms = gms;
	}
	
	@Override
	public void resize(int i, int j) {
		super.resize(i, j);
		if(initialized) {
			bContinue.setCenterPosition(i / 2, (j / 2) + (bContinue.getHeight() / 2));
			bEndGame.setCenterPosition(bContinue.getXCenter(), bContinue.getYCenter() + bEndGame.getHeight());
		}
	}

	@Override 
	public void backKeyPressed() {
		mf.continueGame();
	};

	@Override
	public void render(float f) {
		super.render(f);
		if(initialized) {
			bContinue.draw(mf.getSpriteBatch());
			bEndGame.draw(mf.getSpriteBatch());
		}
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == bContinue) {
			mf.continueGame();
		} else if(s == bEndGame) {
			if(gms != null) {
				gms.disposeTickListener();
			}
			mf.getAI().dispose();		
			mf.endGame();
		}
	}

}
