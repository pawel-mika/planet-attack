package pl.wcja.g2d.controls;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class BitmapFontCacheLabel extends BitmapFontCache implements Control {
	private TextAlignment textAlignment = TextAlignment.CENTER;
	private String text = "";
	private BitmapFontCache bfc = null;
	
	public BitmapFontCacheLabel(IMainFrame mf, String text, int posX, int posY) {
		super(mf.getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal));
		setText(text, posX, posY);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}

	@Override
	public float getHeightScaled() {
		return getRectangle().height;
	}

	@Override
	public float getWidthScaled() {
		return getRectangle().width;
	}

	@Override
	public Rectangle getRectangle() {
		return getRectangle();
	}

	@Override
	public void addEffect(SpriteEffect spriteEffect) {
	}
}