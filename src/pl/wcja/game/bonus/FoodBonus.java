package pl.wcja.game.bonus;

import pl.wcja.IMainFrame;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class FoodBonus extends Bonus {

	private float productionSpeedMultiplier = 1.0f;
	private float transferSpeedMultiplier = 1.0f;

	public FoodBonus(IMainFrame mf) {
		super(mf);
		int m = mf.getRandom().nextInt(10);
		productionSpeedMultiplier = 1.0f + (m != 0 ? (float)m / 10 : 0.1f);
		m = mf.getRandom().nextInt(10);
		transferSpeedMultiplier = 1.0f + (m != 0 ? (float)m / 10 : 0.1f);
	}

	public float getProductionSpeedMultiplier() {
		return productionSpeedMultiplier;
	}

	public float getTransferSpeedMultiplier() {
		return transferSpeedMultiplier;
	}
}
