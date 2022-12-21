package pl.wcja.game;

import java.util.Calendar;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.Planet;
import pl.wcja.g2d.Planet.Owner;
import pl.wcja.screen.GameMainScreen;

import com.badlogic.gdx.math.Vector2;

public abstract class InterplanetaryAction implements TickListener {

	protected IMainFrame mf = null;
	protected GameMainScreen gms = null;
	protected Planet srcPlanet = null;
	protected Vector2 srcPoint = null;
	protected Planet dstPlanet = null;
	protected Vector2 dstPoint = null;
	protected int actionLength = 5000; //5000ms = 5s
//	protected TickEvent firstTick = null;
	protected long actionStartTime = -1;
	protected float actionProgress = 0.0f;
	private boolean finished = false;
	protected pl.wcja.g2d.Planet.Owner actionOwner = Owner.UNDEFINED;
	private float distance = 0;
	private boolean isPaused = false;
	private long pauseLength = 0, lastPauseStartTime = 0;
	
	
	/**
	 * 
	 * @param mf
	 * @param gms
	 * @param speed in ms
	 */
	public InterplanetaryAction(IMainFrame mf, GameMainScreen gms, Planet srcPlanet, Planet dstPlanet, int speed) {
		super();
		this.mf = mf;
		this.gms = gms;
		this.srcPlanet = srcPlanet;
		this.dstPlanet = dstPlanet;
		this.actionOwner = srcPlanet.getOwner();
		srcPoint = new Vector2(srcPlanet.getXCenter(), srcPlanet.getYCenter());
		dstPoint = new Vector2(dstPlanet.getXCenter(), dstPlanet.getYCenter());
		distance = dstPoint.dst(srcPoint);
		this.actionLength = (int)(distance / speed) * 100;	//obliczamy ile czasu zajmie droga...
	}

	public abstract void drawAction();
	
	public abstract void finishAction();
	
	protected void progressAction(long currentTime) {
		if(actionStartTime >= 0) {
			long r = currentTime - actionStartTime - pauseLength;
			actionProgress = (float)r / actionLength;
		}
		if(actionProgress >= 1.0f) {
			finished = true;
			finishAction();
		}
	}

	@Override
	public void tick(TickEvent e) {
		if(actionStartTime < 0 ) {
			actionStartTime = Calendar.getInstance().getTimeInMillis();
		}
		progressAction(Calendar.getInstance().getTimeInMillis());
	}
	
	@Override
	public void disposeTickListener() {
	
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public Owner getActionOwner() {
		return actionOwner;
	}

	public Planet getDstPlanet() {
		return dstPlanet;
	}

	public Planet getSrcPlanet() {
		return srcPlanet;
	}
	
	public void setPaused(boolean isPaused) {
		if(isPaused) {
			lastPauseStartTime = Calendar.getInstance().getTimeInMillis();
		} else {
			pauseLength += Calendar.getInstance().getTimeInMillis() - lastPauseStartTime;
		}
		this.isPaused = isPaused;
	}
}