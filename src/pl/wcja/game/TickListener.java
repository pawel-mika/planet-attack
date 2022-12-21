package pl.wcja.game;

public interface TickListener {

	public abstract void tick(TickEvent e);
	
	public abstract void disposeTickListener();
	
}
