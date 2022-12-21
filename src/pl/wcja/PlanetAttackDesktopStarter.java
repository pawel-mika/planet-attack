package pl.wcja;

import com.badlogic.gdx.backends.jogl.JoglApplication;

/**
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 *
 */
public class PlanetAttackDesktopStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlanetAttackApplication planetAttackApplication = new PlanetAttackApplication();
//		planetAttackApplication.setGameTimeCounter(new DesktopGameTimeCounter(planetAttackApplication, true));
//		new JoglApplication(planetAttackApplication, "Planet attack desktop", 320, 480, false);
		new JoglApplication(planetAttackApplication, "Planet attack desktop", 480, 800, false);
	}

}
