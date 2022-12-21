package pl.wcja.g2d.effects;

public class EffectCompletedEvent {

	private SpriteEffect effect = null;
	
	public EffectCompletedEvent(SpriteEffect effect) {
		this.effect = effect;
	}
	
	public SpriteEffect getEffect() {
		return effect;
	}
}