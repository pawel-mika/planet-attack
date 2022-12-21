package pl.wcja.game.bonus;

import pl.wcja.IMainFrame;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class ShipBonus extends Bonus {
	
	private float productionSpeedMultiplier = 1.0f;
	private float speedMultiplier = 1.0f;
	private float powerMultiplier = 1.0f;
	
	public ShipBonus(IMainFrame mf) {
		super(mf);
		hiddenResource = mf.getRandom().nextInt(100);
		int m = mf.getRandom().nextInt(10);
		productionSpeedMultiplier = 1.0f + (m != 0 ? (float)m / 10 : 0.1f);
		m = mf.getRandom().nextInt(10);
		speedMultiplier = 1.0f + (m != 0 ? (float)m / 10 : 0.1f);
		m = mf.getRandom().nextInt(10);
		powerMultiplier = 1.0f + (m != 0 ? (float)m / 10 : 0.1f);
	}

	public float getProductionSpeedMultiplier() {
		return productionSpeedMultiplier;
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}
	
	public float getPowerMultiplier() {
		return powerMultiplier;
	}
}
