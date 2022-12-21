package pl.wcja.game;

import pl.wcja.IMainFrame;
import pl.wcja.screen.GameMainScreen;

/**
 * Base winContition class - checks if one of the player does not posses any more planets
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class WinContition {

	protected IMainFrame mf = null;
	protected GameMainScreen gms = null;
	
	public WinContition(IMainFrame mf, GameMainScreen gms) {
		this.mf = mf;
		this.gms = gms;
	}
	
	public boolean didPlayerWin() {
		return false;
	}
	
	public boolean didComputerWin() {
		return false;
	}
	
}
