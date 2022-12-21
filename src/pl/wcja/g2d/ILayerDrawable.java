package pl.wcja.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Interface for drawing some items on layers.
 * Layer index should be in range -128...128.
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public interface ILayerDrawable {
	
	public abstract void draw(SpriteBatch spriteBatch, int layerIndex) throws IndexOutOfBoundsException;

}
