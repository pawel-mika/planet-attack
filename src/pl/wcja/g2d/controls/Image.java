package pl.wcja.g2d.controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Image extends ExtSprite implements Control {

	public Image(IMainFrame mf, TextureRegion region, int posX, int posY) {
		super(mf, region, posX, posY);
	}


}
