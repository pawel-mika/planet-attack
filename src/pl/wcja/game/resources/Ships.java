package pl.wcja.game.resources;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ResourceSprite;
import pl.wcja.game.bonus.FoodBonus;
import pl.wcja.game.bonus.MineralsBonus;
import pl.wcja.game.bonus.ShipBonus;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Ships extends ResourceSprite {
	
	public Ships(IMainFrame mf, int posX, int posY, int initialQuantity, float growRatePerSecond) {
		super(mf, mf.getTexturePack("main").findRegion("ico_ships"), posX, posY, initialQuantity, growRatePerSecond);
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public ShipBonus getBonus() {
		return super.getBonus();
	};
	
	@Override
	protected float computeGrowth() {
		return getBonus() != null ? 
				super.computeGrowth() * ((ShipBonus)getBonus()).getProductionSpeedMultiplier() :
				super.computeGrowth();
	}
}