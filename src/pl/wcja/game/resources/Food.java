package pl.wcja.game.resources;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ResourceSprite;
import pl.wcja.game.bonus.FoodBonus;
import pl.wcja.game.bonus.MineralsBonus;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Food extends ResourceSprite {

	public Food(IMainFrame mf, int posX, int posY, int initialQuantity, float growRatePerSecond) {
		super(mf, mf.getTexturePack("main").findRegion("ico_food"), posX, posY, initialQuantity, growRatePerSecond);
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public FoodBonus getBonus() {
		return super.getBonus();
	};
	
	@Override
	protected float computeGrowth() {
		return getBonus() != null ? 
				super.computeGrowth() * ((FoodBonus)getBonus()).getProductionSpeedMultiplier() :
				super.computeGrowth();
	}
	
}
