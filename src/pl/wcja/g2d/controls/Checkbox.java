package pl.wcja.g2d.controls;

import pl.wcja.IMainFrame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Checkbox extends Button implements Control {

	private boolean isSelected = false;
	
	public Checkbox(IMainFrame mf, TextureRegion normal, TextureRegion checked, int posX, int posY) {
		super(mf, normal, checked, posX, posY);
	}

	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean b) {
		if(b) {
			isSelected = true;
			setRegion(textureActive);
		} else {
			isSelected = false;
			setRegion(textureNormal);
		}
	}

}
