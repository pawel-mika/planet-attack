package pl.wcja.utils;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Rectangle;

import pl.wcja.g2d.ExtSprite;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class SpriteUtils {
	
	public static Collection<? extends ExtSprite> moveAllBy(Collection<? extends ExtSprite> sprites, float x, float y) {
		for(ExtSprite es : sprites) {
			es.setPosition(es.getX() + x, es.getY() + y);
		}
		return sprites;
	}
	
	public static ArrayList<? extends ExtSprite> arrangeHorizontal(ArrayList<? extends ExtSprite> sprites, float xSpacer) {
		float x = 0;
		for(ExtSprite es : sprites) {
			es.setPosition(es.getX() + x, es.getY());
			x += es.getWidthScaled();
		}
		return sprites;
	}
	
	/**
	 * 
	 * @param sprites
	 * @param xEdge x edge of arranged sprites
	 * @param ySpacer spacer for sprites
	 * @return
	 */
	public static ArrayList<? extends ExtSprite> arrangeVertical(ArrayList<? extends ExtSprite> sprites, float xEdge, float ySpacer) {
		float y = 0;
		for(ExtSprite es : sprites) {
			es.setPosition(xEdge, es.getY() + y);
			y += es.getHeightScaled() + ySpacer;
		}
		return sprites;
	}
	
	/**
	 * 
	 * @param sprites
	 * @param ySpacer
	 * @return
	 */
	public static ArrayList<? extends ExtSprite> arrangeVertical(ArrayList<? extends ExtSprite> sprites, float ySpacer) {
		float y = 0;
		for(ExtSprite es : sprites) {
			es.setPosition(es.getX(), es.getY() + y);
			y += es.getHeightScaled() + ySpacer;
		}
		return sprites;
	}
	
	public static Collection<? extends ExtSprite> centerHorizontal(Collection<? extends ExtSprite> sprites, int containerWidth) {
		for(ExtSprite es : sprites) {
			es.setCenterPosition(containerWidth / 2, es.getYCenter());
		}
		return sprites;
	}
	
	public static Collection<? extends ExtSprite> centerVertical(Collection<? extends ExtSprite> sprites, int containerHeight) {
		for(ExtSprite es : sprites) {
			es.setCenterPosition(es.getXCenter(), containerHeight / 2);
		}
		return sprites;
	}
	
	public static Collection<? extends ExtSprite> centerVerticalGroup(Collection<? extends ExtSprite> sprites, int containerHeight) {
		float maxY = 0, minY = 0;
		for(ExtSprite es : sprites) {
			float top = es.getY() + es.getHeightScaled();
			float bottom = es.getY();
			if(top > maxY) {
				maxY = top;
			}
			if(bottom < minY) {
				minY = bottom;
			}
			
		}
		
		return sprites;
	}
	
	public static Rectangle getGroupSize(Collection<? extends ExtSprite> sprites) {
		ExtSprite first = sprites.iterator().next();
		float maxY = 0, minY = 0, minX = 0, maxX = 0;
		if(first != null) {
			minY = maxY = first.getY();
			minX = maxX = first.getX();
			for(ExtSprite es : sprites) {
				float top = es.getY() + es.getHeightScaled();
				float bottom = es.getY();
				float left = es.getX();
				float right = es.getX() + es.getWidthScaled();
				maxY = top > maxY ? top : maxY;
				minY = bottom < minY ? bottom : minY;
				maxX = right > maxX ? right : maxX;
				minX = left < minX ? left : minX;				
			}			
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}
}