package pl.wcja.g2d.controls;

import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public interface Control {
	
	public enum TextAlignment {
		MANUAL,
		LEFT,
		CENTER,
		RIGHT,
		OVER,
		UNDER
	}

	public void draw(SpriteBatch batch);
	
	public float getX();
	
	public float getY();
	
	public float getHeightScaled();
	
	public float getWidthScaled();
	
	public Rectangle getRectangle();
	
	public void setPosition(float x, float y);
	
	public void addEffect(SpriteEffect spriteEffect);
		
}
