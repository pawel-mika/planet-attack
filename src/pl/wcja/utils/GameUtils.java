package pl.wcja.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.Planet;
import pl.wcja.game.GameLevel;
import pl.wcja.game.bonus.Bonus;
import pl.wcja.game.bonus.FoodBonus;
import pl.wcja.game.bonus.MineralsBonus;
import pl.wcja.game.bonus.ShipBonus;
import pl.wcja.screen.GameMainScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class GameUtils {
	
	/**
	 * IT TAKES 
	 * TOO FUCKING LONG
	 * !!!
	 * 
	 * @param mf
	 * @param planetCount
	 * @param minDistance
	 * @param minScale
	 * @param trPlanet
	 * @param previousPlanets
	 * @return
	 */
	public static ArrayList<Planet> generatePlanets(IMainFrame mf, GameMainScreen gms, int planetCount, float minDistance, float minScale,
			TextureRegion trPlanet, ArrayList<Planet> previousPlanets) {
		if (previousPlanets == null) {
			previousPlanets = new ArrayList<Planet>();
		}
		if (previousPlanets != null && planetCount == 0) {
			return previousPlanets;
		}
		Planet p = null;
		int colisions = previousPlanets.size();
		do {
			colisions = previousPlanets.size();
			float scale = mf.getRandom().nextFloat();
			while (scale < minScale) {
				scale = mf.getRandom().nextFloat();
			}
			float size = scale * trPlanet.getRegionWidth();
			int x = mf.getRandom().nextInt(Gdx.graphics.getWidth());
			int y = mf.getRandom().nextInt(Gdx.graphics.getHeight());
			p = new Planet(mf, gms, trPlanet, x, y, scale);
			for (Planet pp : previousPlanets) {
				if (!p.collidesWith(pp) && IntersectUtils.isRectInRect(p.getRectangle(), IntersectUtils.getScreenRectangle())) {
					colisions--;
				}
			}
			// check if first doesn't exceed screen bounds...
			if (previousPlanets.isEmpty() && IntersectUtils.isRectInRect(p.getRectangle(), IntersectUtils.getScreenRectangle())) {
				previousPlanets.add(p);
			} else {
			}
		} while (colisions > 0);
		if (!previousPlanets.contains(p)) {
			previousPlanets.add(p);
		}
		return generatePlanets(mf, gms, planetCount - 1, minDistance, minScale, trPlanet, previousPlanets);
	}
	
	private static boolean[][] occupied = null;
	private static int topMargin = 32; //top margin reserved for hud...
	
	/**
	 * 
	 * @param mf
	 * @param planetCount
	 * @param trPlanet
	 * @param previousPlanets
	 * @return
	 */
	public static ArrayList<Planet> generatePlanets(IMainFrame mf, GameMainScreen gms, int planetCount, TextureRegion trPlanet, ArrayList<Planet> previousPlanets) {
		int tileSize = 24 * 3; //3 x resource po 24px
		int worldTilesX = Gdx.graphics.getWidth() / tileSize; 
		int worldTilesY = (Gdx.graphics.getHeight() - topMargin) / tileSize;
		int minSize = 1, maxSize = 2;
		int trSize = trPlanet.getRegionWidth();	//zakladamy 128px...?
		
		if (previousPlanets == null) {
			previousPlanets = new ArrayList<Planet>();
			occupied = new boolean[worldTilesX][worldTilesY];
			for(int i = 0; i < worldTilesX; i++) {
				for(int j = 0; j < worldTilesY; j++) {
					occupied[i][j] = false;
				}
			}
		}
		if (previousPlanets != null && planetCount == 0) {
			return previousPlanets;
		}
		Planet p = null;
		int sizeMul = mf.getRandom().nextInt(maxSize - minSize) + minSize;
		float size = (float) (sizeMul * tileSize) / trSize;
		int x = mf.getRandom().nextInt(worldTilesX) * tileSize;
		int y = mf.getRandom().nextInt(worldTilesY) * tileSize;
		while(occupied[x / tileSize][y / tileSize]) {
			x = mf.getRandom().nextInt(worldTilesX) * tileSize;
			y = mf.getRandom().nextInt(worldTilesY) * tileSize;
		}
		p = new Planet(mf, gms, trPlanet, x + (tileSize / 2), y + (tileSize / 2), size);
//		for (Planet pp : previousPlanets) {
//			
//		}
		// check if first doesn't exceed screen bounds...
		if (previousPlanets.isEmpty()) {
			previousPlanets.add(p);
			occupied[x / tileSize][y / tileSize] = true;
		} 
		if (!previousPlanets.contains(p)) {
			previousPlanets.add(p);
			occupied[x / tileSize][y / tileSize] = true;
		}
		return generatePlanets(mf, gms, planetCount - 1, trPlanet, previousPlanets);
	}
	
	/**
	 * 
	 * @param mf
	 * @param planetCount
	 * @param trPlanet
	 * @param tileSize
	 * @param tilesX
	 * @param tilesY
	 * @param minSize
	 * @param maxSize
	 * @param previousPlanets
	 * @return
	 */
	public static Planet[][] generatePlanets(IMainFrame mf, int planetCount, TextureRegion trPlanet, int tileSize,
			int tilesX, int tilesY, int minSize, int maxSize, Planet[][] previousPlanets) {
		if(previousPlanets == null) {
			previousPlanets = new Planet[tilesX][tilesY];
		}
		if(previousPlanets != null && planetCount == 0) {
			return previousPlanets;
		}
		boolean collision = false;
		do{
			int tx = mf.getRandom().nextInt(tilesX);
			int ty = mf.getRandom().nextInt(tilesY);
			
		}while(collision);
		
		return previousPlanets;
	}
	
	/**
	 * 
	 * @param mf
	 * @return
	 */
	public static List<Bonus> getRandomBonuses(IMainFrame mf) {
		List<Bonus> bonuses = new LinkedList<Bonus>();
		float fr = mf.getRandom().nextFloat();
		if(mf.getGameLevel().getBonusProbability() > fr) {
			bonuses.add(new ShipBonus(mf));
		}
		fr = mf.getRandom().nextFloat();
		if(mf.getGameLevel().getBonusProbability() > fr) {
			bonuses.add(new MineralsBonus(mf));
		}
		fr = mf.getRandom().nextFloat();
		if(mf.getGameLevel().getBonusProbability() > fr) {
			bonuses.add(new FoodBonus(mf));
		}
		return bonuses;
	}
	
}
