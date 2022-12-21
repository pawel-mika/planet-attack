package pl.wcja;

import pl.wcja.g2d.Screen2D;

public interface IScreenManager {

	/**
	 * Push screen to view in a stack and view it.
	 * 
	 * @param screen
	 */
	public abstract void pushScreen(Screen2D screen);
	
	/**
	 * Pop to previous screen in a stack.
	 */
	public abstract void popScreen();
	
//	/**
//	 * Set a viewing screen without pushing it on a stack.
//	 * @param screen
//	 */
//	public abstract void setScreen(Screen2D screen);
	
	/**
	 * Get the currently viewed screen (no matter if it a screen from pushScreen/popScreen or from setScreen)
	 * @return
	 */
	public abstract Screen2D getScreen();
}
