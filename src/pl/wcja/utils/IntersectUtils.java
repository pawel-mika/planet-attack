package pl.wcja.utils;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class IntersectUtils {

	public static boolean isPointInsideSprite(Sprite sprite, Vector2 point) {
//		PolygonShape ps = new PolygonShape();
		List<Vector2> points = new LinkedList<Vector2>();
		points.add(new Vector2(sprite.getVertices()[Sprite.X1], sprite.getVertices()[Sprite.Y1]));
		points.add(new Vector2(sprite.getVertices()[Sprite.X2], sprite.getVertices()[Sprite.Y2]));
		points.add(new Vector2(sprite.getVertices()[Sprite.X3], sprite.getVertices()[Sprite.Y3]));
		points.add(new Vector2(sprite.getVertices()[Sprite.X4], sprite.getVertices()[Sprite.Y4]));
		return Intersector.isPointInPolygon(points, point);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @return
	 */
	public static boolean pointInRectangle(float x, float y, Rectangle r) {
		return x >= r.getX() && y >= r.getY() && x <= (r.getX() + r.getWidth()) && y <= (r.getY() + r.getHeight()); 
	}
	
	/**
	 * Is rectangle inside inRectangle? (including borders)
	 * 
	 * @param rect
	 * @param inRect
	 * @return
	 */
	public static boolean isRectInRect(Rectangle rect, Rectangle inRect) {
		return rect.getX() >= inRect.getX() && rect.getY() >= inRect.getY() 
			&& inRect.getWidth() >= (rect.getX() + rect.getWidth()) && inRect.getHeight() >= (rect.getY() + rect.getHeight());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Rectangle getScreenRectangle() {
		return new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
}
