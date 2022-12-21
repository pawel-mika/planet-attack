package pl.wcja.g2d.controls;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Group implements Control {

	private Color bgColor = null;
	private List<Control> controls = new LinkedList<Control>();
	
	public void addControl(Control c) {
		controls.add(c);
	}
	
	public void addControls(Collection<? extends Control> controls) {
		this.controls.addAll(controls);
	}
	
	public void removeControl(Control c) {
		synchronized (controls) {
			controls.remove(c);
		}
	}
	
	@Override
	public Rectangle getRectangle() {
		Control first = controls.iterator().next();
		float maxY = 0, minY = 0, minX = 0, maxX = 0;
		if(first != null) {
			minY = maxY = first.getY();
			minX = maxX = first.getX();
			for(Control c : controls) {
				float top = c.getY() + c.getHeightScaled();
				float bottom = c.getY();
				float left = c.getX();
				float right = c.getX() + c.getWidthScaled();
				maxY = top > maxY ? top : maxY;
				minY = bottom < minY ? bottom : minY;
				maxX = right > maxX ? right : maxX;
				minX = left < minX ? left : minX;				
			}			
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(bgColor != null) {
			
		}
		for(Control c : controls) {
			c.draw(batch);
		}
	}

	@Override
	public void setPosition(float x, float y) {
		Rectangle r = getRectangle();
		float rx = r.x - x;
		float ry = r.y - y;
		for(Control c : controls) {
			c.setPosition(c.getX() + rx, c.getY() + ry);
		}
	}

	@Override
	public float getX() {
		return getRectangle().x;
	}

	@Override
	public float getY() {
		return getRectangle().y;
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
	public void addEffect(SpriteEffect spriteEffect) {
	}
}
