package pl.wcja.game.ai;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public enum AILevel {

	EASY(0.15f, 8, 8, 0.4f, 0.6f, 0.5f, 0.5f),
	NORMAL(0.25f, 7, 7, 0.4f, 0.6f, 0.5f, 0.5f),
	HARD(0.5f, 6, 6, 0.45f, 0.55f, 0.55f, 0.45f),
	MEGAMIND(1f, 5, 5, 0.5f, 0.5f, 0.6f, 0.4f);	
	
	private float decisionsPerSecond;
	private int attackSpeed;
	private int transferSpeed;
	private float attackThreshold;
	private float transferThreshold;
	private float attackWeight;
	private float transferWeight;
	
	/**
	 * @param decisionsPerSecond
	 * @param attackSpeed
	 * @param transferSpeed
	 * @param attackThreshold
	 * @param transferThreshold
	 * @param attackWeight
	 * @param transferWeight
	 */
	private AILevel(float decisionsPerSecond, int attackSpeed, int transferSpeed, float attackThreshold,
			float transferThreshold, float attackWeight, float transferWeight) {
		this.decisionsPerSecond = decisionsPerSecond;
		this.attackSpeed = attackSpeed;
		this.transferSpeed = transferSpeed;
		this.attackThreshold = attackThreshold;
		this.transferThreshold = transferThreshold;
		this.attackWeight = attackWeight;
		this.transferWeight = transferWeight;
	}
	public float getDecisionsPerSecond() {
		return decisionsPerSecond;
	}
	public int getAttackSpeed() {
		return attackSpeed;
	}
	public int getTransferSpeed() {
		return transferSpeed;
	}
	public float getAttackThreshold() {
		return attackThreshold;
	}
	public float getTransferThreshold() {
		return transferThreshold;
	}
	public float getAttackWeight() {
		return attackWeight;
	}
	public float getTransferWeight() {
		return transferWeight;
	}
}