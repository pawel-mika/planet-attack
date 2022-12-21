package pl.wcja.g2d;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.effects.SpriteEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Extended sprite
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class ExtSprite extends Sprite implements ILayerDrawable {

	protected IMainFrame mf = null;
	protected List<SpriteEffect> effects = new LinkedList<SpriteEffect>();

	public ExtSprite(IMainFrame mf, TextureRegion region, float centerPosX, float centerPosY, float scaleXY) {
		super(region);
		this.mf = mf;
		setScale(scaleXY);
		setCenterPosition(centerPosX, centerPosY);
	}

	public ExtSprite(IMainFrame mf, TextureRegion region, float posX, float posY) {
		super(region);
		this.mf = mf;
		setPosition(posX, posY);
	}
	
	public ExtSprite(IMainFrame mf) {
		super();
		this.mf = mf;
	}
	
	public void addEffect(SpriteEffect effect) {
		effect.setTarget(this);
		effects.add(effect);
	}

	public void setCenterPosition(float cx, float cy) {
		setPosition(cx - (getWidth() / 2), cy - (getHeight() / 2));
	}
	
	public boolean collidesWith(ExtSprite sprite) {
		return Intersector.intersectRectangles(getRectangle(), sprite.getRectangle());
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(getScaledX(), getScaledY(), getWidthScaled(), getHeightScaled());
	}
	
	public float getWidthScaled() {
		return getWidth() * getScaleX();
	}
	
	public float getHeightScaled() {
		return getHeight() * getScaleY();
	}

	public float getScaledX() {
		return super.getX() + ((getWidth() - getWidthScaled()) / 2);
	}

	public float getScaledY() {
		return super.getY() + ((getHeight() - getHeightScaled()) / 2);
	}
	
	public float getXCenter() {
		return super.getX() + (getWidth() / 2);
	}
	
	public float getYCenter() {
		return super.getY() + (getHeight() / 2);
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
		super.draw(spriteBatch);
	};

	@Override
	public void draw(SpriteBatch spriteBatch, int layerIndex) {
		if(layerIndex < -128 || layerIndex > 128) {
			throw new IndexOutOfBoundsException("Layer index out od bounds. Should be in range -128...128!");
		}
	}
}