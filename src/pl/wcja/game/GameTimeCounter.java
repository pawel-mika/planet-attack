package pl.wcja.game;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import pl.wcja.IMainFrame;

/**
 * Game timer counter class - counts seconds and 1/10th of seconds
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 * 
 */
public abstract class GameTimeCounter {

	protected List<TickListener> tickListeners = new LinkedList<TickListener>();
	protected List<TickListener> secondListeners = new LinkedList<TickListener>();

	public void addTickListener(TickListener tl) {
		tickListeners.add(tl);
	}

	public void addSecondsListener(TickListener tl) {
		secondListeners.add(tl);
	}

	public void removeTickListener(final TickListener tl) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				tickListeners.remove(tl);
			}
		});
	}

	public void removeSecondsListener(final TickListener tl) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				secondListeners.remove(tl);
			}
		});
	}

	private IMainFrame mf = null;
	private Thread tickThread = null;
	private int tickLen = 100; // 100ms - 1 tick
	protected long seconds = 1;
	protected long ticks = 1;
	private boolean paused = true;
	private boolean disposed = false;

	public GameTimeCounter(IMainFrame mf, boolean paused) {
		this.mf = mf;
		this.paused = paused;
//
//		tickThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (!disposed) {
//					try {
//						if (!GameTimeCounter.this.paused) {
//							ticks++;
//							fireTickEvent();
//							if (ticks % 10 == 0) {
//								seconds++;
//								fireSecondEvent();
//							}
//						}
//					} catch(Exception e) {
//						e.printStackTrace();
//					}
//					try {
//						Thread.sleep(tickLen);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		tickThread.start();
	}
	
	private float timerDelta = 0;
	
	/**
	 * Act method to be called from main render loop
	 * measures the time and performs event triggering
	 * @param delta
	 */
	public void act(float delta) {
		if(!paused) {
			timerDelta += delta;
			if(timerDelta > 0.1) {
				timerDelta = 0 + (timerDelta - 0.1f);
				ticks++;
				fireTickEvent();
				if(ticks % 10 == 0) {
					seconds = (int)ticks / 10;
					fireSecondEvent();				
				}
			}
		}
	}

	public long getSeconds() {
		return seconds;
	}

	public long getTicks() {
		return ticks;
	}

	public void pauseCounter(boolean paused) {
		this.paused = paused;
	}

	public void resetConuter() {
		this.seconds = 0;
		this.ticks = 0;
	}

	public void dispose() {
		disposed = true;
	}

	protected abstract void fireTickEvent() ;

	protected abstract void fireSecondEvent();
}
