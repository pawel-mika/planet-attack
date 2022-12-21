package pl.wcja.g2d;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.g2d.effects.SpriteEffect;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public abstract class SpectacularScreen2d extends Screen2D {

	private List<SpriteEffect> inEffects = new LinkedList<SpriteEffect>();
	private List<SpriteEffect> outEffects = new LinkedList<SpriteEffect>();
	private List<EffectCompletedListener> listeners = new LinkedList<EffectCompletedListener>();
	
	public SpectacularScreen2d(IMainFrame mf) {
		super(mf);
	}
	
	public void addOutEffectCompletedListener(EffectCompletedListener l) {
		listeners.add(l);
	}
	
	public void removeOutEffectCompletedListener(EffectCompletedListener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}
	
	protected void fireOutEffectCompletedEvent(final SpriteEffect effect) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				for(EffectCompletedListener l : listeners) {
					l.completed(new EffectCompletedEvent(effect));
				}
			}
		});
//		for(EffectCompletedListener l : listeners) {
//			l.completed(new EffectCompletedEvent(effect));
//		}
	}
	
	public abstract void setupSpectacularInEffect();
	
	public abstract void setupSpectacularOutEffect();
}
