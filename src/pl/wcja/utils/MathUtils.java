package pl.wcja.utils;

import com.badlogic.gdx.math.Vector2;

public class MathUtils {

	/**
	 * 
	 * @param lineStart
	 * @param lineEnd
	 * @param distPercent percent of distance from startPoint for result point (up to 1.0)
	 * @return
	 */
	public static Vector2 getPointOnLine(Vector2 p1, Vector2 p2, float distPercent) {
		if(distPercent < 0 || distPercent > 1) {
			return null;
		} else if(distPercent == 0) {
			return p1;
		} else if(distPercent == 1) {
			return p2;
		} else {
//			double len = lineStart.distance(lineEnd);
//			Vector2 p12 = new Vector2(p1.x, p2.y);	//3ci punkt do trójk¹ta p1 <-> p2 <-> p12
			float dist = p1.dst(p2);
			float dt = dist * distPercent;
			Vector2 p3 = new Vector2(p2).sub(p1);
			return new Vector2(p1).add(p3.mul(distPercent));
//			float rx = ((p2.x - p1.x) * distPercent);
//			float ry = ((p2.y - p1.y) * distPercent);
//			
//			return new Vector2(rx, ry);
		}
	}
	
	public static double tan(Vector2 p1, Vector2 p2) {
		return Math.tan((p2.y - p1.y) / (p2.x - p1.x));
	}
}