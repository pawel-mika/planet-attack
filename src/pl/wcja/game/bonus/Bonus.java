package pl.wcja.game.bonus;

import pl.wcja.IMainFrame;

/**
 * Class describing hidden planetary bonus, for example:
 * - resource gathering multiplier (x1.5, x2 etc) for minerals and food
 * - better ships builder (faster and/or more powerfull) 
 * - resources hidden on a planet waiting to be found
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Bonus {

	protected IMainFrame mf = null;
	protected int hiddenResource = 0;
	
	
	public Bonus(IMainFrame mf) {
		this.mf = mf;
		hiddenResource = mf.getRandom().nextInt(1000);
	}

	/**
	 * Hidden bonus can be collected only once per object initialization.
	 * It's 'whoever gets its first' type;)
	 * @return
	 */
	public int getHiddenResource() {
		int tmp = hiddenResource;
		hiddenResource = 0;
		return tmp;
	}
	
}
