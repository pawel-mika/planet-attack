package pl.wcja.g2d.controls;

import java.util.Iterator;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Label extends ExtSprite implements Control {

	private BitmapFontCache bfc = null;
	
	/**
	 * 
	 * @param mf
	 * @param bitmapFont
	 * @param text
	 * @param posX
	 * @param posY
	 */
	public Label(IMainFrame mf, BitmapFont bitmapFont, String text, float posX, float posY) {
		super(mf);
		bfc = new BitmapFontCache(bitmapFont);
		bfc.setText(text, 0, 0);
		bfc.setPosition(posX, posY);
	}
	
	/**
	 * 
	 * @param mf
	 * @param text
	 * @param posX
	 * @param posY
	 */
	public Label(IMainFrame mf, String text, float posX, float posY) {
		super(mf);
		bfc = new BitmapFontCache(mf.getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal));
		bfc.setText(text, 0, 0);
		bfc.setPosition(posX, posY);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		Iterator<SpriteEffect> i = effects.iterator();
		while(i.hasNext()) {
			SpriteEffect se = i.next();
			se.act(Gdx.graphics.getDeltaTime());
			if(se.isDone()) {
				se.finish();
				i.remove();
			}
		}
		bfc.draw(spriteBatch);
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch, float alphaModulation) {
		Iterator<SpriteEffect> i = effects.iterator();
		while(i.hasNext()) {
			SpriteEffect se = i.next();
			se.act(Gdx.graphics.getDeltaTime());
			if(se.isDone()) {
				se.finish();
				i.remove();
			}
		}
		bfc.draw(spriteBatch, alphaModulation);
	}
	
	@Override
	public void setPosition(float x, float y) {
		bfc.setPosition(x, y + bfc.getBounds().height);
	}

	public void setText(String text, float x, float y) {
		bfc.setText(text, 0, 0);
		bfc.setPosition(x, y);
	}
	
	public void setMultilineText(String text, float x, float y, float alignmentWidth, HAlignment hAlignment) {
		bfc.setMultiLineText(text, 0, 0, alignmentWidth, hAlignment);
		bfc.setPosition(x, y);
	}
	
	public BitmapFont getFont() {
		return bfc.getFont();
	}
	
	public TextBounds getBounds() {
		return bfc.getBounds();
	}
	
	@Override
	public float getX() {
		return bfc.getX();
	}
	
	@Override
	public float getY() {
		return bfc.getY() - bfc.getBounds().height;
	}
	
	@Override
	public float getWidth() {
		return bfc.getBounds().width;
	}
	
	@Override
	public float getHeight() {
		return bfc.getBounds().height;
	}

}
