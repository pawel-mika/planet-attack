package pl.wcja.g2d.controls;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Button extends ExtSprite implements Control {

	protected TextureRegion textureNormal = null;
	protected TextureRegion textureActive = null;
	
	private TextAlignment textAlignment = TextAlignment.CENTER;
	private String text = "";
	private BitmapFontCache bfc = null;
	
	public Button(IMainFrame mf, TextureRegion normal, TextureRegion active, int posX, int posY) {
		super(mf, normal, posX, posY);
		bfc = new BitmapFontCache(mf.getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal));
		this.textureNormal = normal;
		this.textureActive = active;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		if(bfc != null && text != null && text.length() > 0) {
			bfc.draw(spriteBatch);
		}
	}

	public String getText() {
		return text;
	}

	public BitmapFont getFont() {
		return bfc.getFont();
	}

	public void setText(String text) {
		this.text = text;
		bfc.setText(text, 0, 0);
		recalculateTextPosition();
	}

	public void setFont(BitmapFont font) {
		bfc = new BitmapFontCache(font);
	}
	
	public void setTextPosition(float x, float y) {
		bfc.setPosition(x, y);
		textAlignment = TextAlignment.MANUAL;
	}
	
	public void setTextAlignment(TextAlignment align) {
		this.textAlignment = align;
		recalculateTextPosition();
	}
	
	@Override 
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		if(bfc != null && text != null && text.length() > 0) {
			recalculateTextPosition();
		}
	};
	
	private void recalculateTextPosition() {
		float height = bfc.getFont().getCapHeight() + bfc.getFont().getAscent();
		float heightScaled = super.getHeightScaled();
		switch (textAlignment) {
		case OVER:
			break;
		case UNDER:
			break;
		case CENTER:
			bfc.setPosition(
					getX() + (super.getWidthScaled() / 2) - (bfc.getBounds().width / 2), 
					getY() + ((heightScaled +  height) / 2));
			break;
		case RIGHT:
			bfc.setPosition(
					getX() + super.getWidthScaled(), 
					getY() + ((heightScaled +  height) / 2));
			break;
		case LEFT:
			bfc.setPosition(
					getX() - bfc.getBounds().width, 
					getY() + ((heightScaled +  height) / 2));
			break;
		default:
			break;
		}
	}
	
	@Override
	public float getWidthScaled() {
		float org = super.getWidthScaled();
		if(bfc != null && text != null && text.length() > 0) {
			switch(textAlignment) {
			case CENTER:
				return org > bfc.getBounds().width ? org : bfc.getBounds().width;
			case RIGHT:
			case LEFT:
				return org > org + bfc.getBounds().width ? org : org + bfc.getBounds().width;
			default:
				break;
			}
		}
		return org;
	}
	
	@Override
	public float getHeightScaled() {
		float org = super.getHeightScaled();
		if(bfc != null && text != null && text.length() > 0) {
			float height = bfc.getFont().getCapHeight() + bfc.getFont().getAscent();
			switch(textAlignment) {
			case CENTER:
			case RIGHT:
			case LEFT:
				return org > height ? org : height;
			default:
				break;
			}
		}
		return org;
	}
	
	public void setCenterPosition(float cx, float cy) {
		setPosition(cx - (getWidthScaled() / 2), cy - (getHeight() / 2));
	}
	
	public float getScaledX() {
		float ssx = super.getScaledX() + ((getWidthScaled() - getWidth()) / 2);
		if(bfc != null && text != null && text.length() > 0) {
			return ssx < bfc.getX() ? ssx : bfc.getX();	
		} 
		return ssx;
	}

	public float getScaledY() {
		float ssy = super.getScaledY() + ((getHeightScaled() - getHeight()) / 2);
		if(bfc != null && text != null && text.length() > 0) {
			return ssy < bfc.getY() ? ssy : bfc.getY();	
		} 
		return ssy;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(getScaledX(), getScaledY(), getWidthScaled(), getHeightScaled());
	}
}
