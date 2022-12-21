package pl.wcja.game;

/**
 * Description of a subsequent levels available during the game. 
 * Each next level is becoming available after winnig a game in a lower level.
 * (maybe winning it with at least XXX points or some other conitions?)
 *   
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public enum GameLevel {
	LEVEL1(Rank.ENSIGN, 0.8f, true, true),
	LEVEL2(Rank.LIEUTENANT_JUNIOR_GRADE, 0.75f, true, true),
	LEVEL3(Rank.LIEUTENANT, 0.7f, true, true),
	LEVEL4(Rank.LIEUTENANT_COMMANDER, 0.6f, true, true),
	LEVEL5(Rank.COMMANDER, 0.5f, true, true),
	LEVEL6(Rank.CAPTAIN, 0.45f, true, false),
	LEVEL7(Rank.COMMODORE, 0.4f, true, false),
	LEVEL8(Rank.REAR_ADMIRAL, 0.35f, true, false),
	LEVEL9(Rank.VICE_ADMIRAL, 0.3f, true, false),
	LEVEL10(Rank.ADMIRAL, 0.2f, false, false),
	LEVEL11(Rank.FLEET_ADMIRAL, 0.1f, false, false);
	
	public enum Rank {
		ENSIGN("Ensign"),
		LIEUTENANT_JUNIOR_GRADE("Lieutenant junior grade"),
		LIEUTENANT("Lieutenant"),
		LIEUTENANT_COMMANDER("Lieutenant commander"),
		COMMANDER("Commander"),
		CAPTAIN("Captain"),
		COMMODORE("Commodore"),
		REAR_ADMIRAL("Rear admiral"),
		VICE_ADMIRAL("Vice admiral"),
		ADMIRAL("Admiral"),
		FLEET_ADMIRAL("Fleet admiral");
		
		private String name = null;
		
		private Rank(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	private Rank rank = Rank.ENSIGN;
	private float bonusProbability = 0.25f;	//default 25% of bonus probability
	private boolean compResVisible = true;
	private boolean undefResVisible = true;
	
	private GameLevel(Rank rank, float bonusProbability, boolean computerResourceVisible, boolean undefResourceVisible) {
		this.rank = rank;
		this.bonusProbability = bonusProbability;
		this.compResVisible = computerResourceVisible;
		this.undefResVisible = undefResourceVisible;
	}

	public Rank getRank() {
		return rank;
	}

	public float getBonusProbability() {
		return bonusProbability;
	}

	public boolean isCompResVisible() {
		return compResVisible;
	}

	public boolean isUndefResVisible() {
		return undefResVisible;
	}
	
}
