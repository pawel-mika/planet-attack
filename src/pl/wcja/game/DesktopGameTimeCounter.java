package pl.wcja.game;

import pl.wcja.IMainFrame;
import pl.wcja.game.TickEvent.TickType;

import com.badlogic.gdx.Gdx;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class DesktopGameTimeCounter extends GameTimeCounter {

	public DesktopGameTimeCounter(IMainFrame mf, boolean paused) {
		super(mf, paused);
	}

	protected void fireTickEvent() {
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				for (final TickListener tl : tickListeners) {
					tl.tick(new TickEvent(this, ticks, seconds, TickType.TICK));	
				}
			}
		});
		
	}

	protected void fireSecondEvent() {
		Gdx.app.postRunnable(new Runnable() {
			public void run() {
				for (final TickListener tl : secondListeners) {
					tl.tick(new TickEvent(this, ticks, seconds, TickType.SECOND));	
				}
			}
		});
	}
}