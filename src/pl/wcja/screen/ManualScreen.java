package pl.wcja.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pl.wcja.IMainFrame;
import pl.wcja.Loadable;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Screen2D;

public class ManualScreen extends Screen2D {

	private ExtSprite manual1 = null;
	private boolean initialized = false;
	
	public ManualScreen(IMainFrame mf) {
		super(mf);
		mf.setLoading(true);
		mf.addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				Texture t = mf.getCachedTexture("gfx/bg_manual.jpg", FileType.Internal, true);
				t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
				setBackgroundImage(t);
			}
		});
		mf.addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				Texture t = mf.getCachedTexture("gfx/manual.png", FileType.Internal, true);
				t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
				TextureRegion tr = new TextureRegion(t, 0, 0, 320, 480);
				manual1= new ExtSprite(mf, tr, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1.0f);
				addTouchable(manual1);
				mf.setLoading(false);
				initialized = true;
			}
		});
	}
	
	@Override
	public void render(float f) {
		super.render(f);
		if(initialized) {
//			mf.getSpriteBatch().begin();
			manual1.draw(mf.getSpriteBatch());
//			mf.getSpriteBatch().end();
		}
	}
	
	@Override
	protected void spriteTouched(ExtSprite s) {
		super.spriteTouched(s);
		if(s == manual1) {
			mf.popScreen();
		}
	}
}
