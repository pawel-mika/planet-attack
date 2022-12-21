package pl.wcja.game.ai;

import java.util.ArrayList;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.Planet;
import pl.wcja.g2d.Planet.Owner;
import pl.wcja.game.TickEvent;
import pl.wcja.game.TickListener;
import pl.wcja.screen.GameMainScreen;

import com.badlogic.gdx.utils.Disposable;

/**
 * TODO
 * AILevel - im wy¿szy tym czêœciej sprawdzac warunki i brac wyzsze progi procentowe
 * sprawdzen z kalkulacji potrzeb jako najlepszy wybor (przy nizszych moze pierwszy spelniajacy
 * prog brac jako decyzje?)
 * 
 * czy jesli nic nie spelnia naszego progu to brac najblizszy czy moze czekac do 
 * nastepnego ticku az cos spelni?
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class AI implements TickListener, Disposable {
	
	private boolean isPaused = true;
	private AILevel level = AILevel.EASY;
	private IMainFrame mf = null;
	private GameMainScreen gms = null;
	
	public AI(IMainFrame mf, GameMainScreen gms) {
		this.mf = mf;
		this.gms = gms;
		setAILevel(mf.getSettings().getAiLevel());
		mf.getGameTimeCounter().addSecondsListener(this);
	}

	@Override
	public void dispose() {
		disposeTickListener();
	}
	
	public void setAILevel(AILevel level) {
		this.level = level;
	}
	
	public AILevel getAILevel() {
		return level;
	}

	/**
	 * Jesli zaatakowano - wróc, jesli nie - wykonaj transfer
	 */
	private void act() {
		if(tryToAttack()) {
			return;
		}
		if(tryToTransferGoods()) {
			return;
		}
	}
	
	private boolean tryToAttack() {
		float totalNeedForMinerals = calculateTotalNeedForMinerals();
		float totalNeedForFood = calculateTotalNeedForFood();
		
		//jesli needy powyzej progow to gdzie je zdobyc?
		//undef czy player?
		ArrayList<Planet> compPlanets = gms.getPlanets(Owner.COMPUTER);
		ArrayList<Planet> attackingPlanets = new ArrayList<Planet>();
		Planet possibleTarget = null;
		float targetWinningProbability = 0;
		if(totalNeedForMinerals > level.getTransferThreshold()) {
			System.out.println(String.format("Considering need for minerals: %s (transfer threshold: %s)", totalNeedForMinerals, level.getTransferThreshold()));
			for(Planet p : compPlanets) {
				for(Planet up : gms.getPlanets(Owner.UNDEFINED)) {
					if(calculateChancesAgainst(p, up) > targetWinningProbability) {
						if(attackingPlanets.size() == 0) {
							attackingPlanets.add(0, p);							
						} else {
							attackingPlanets.set(0, p);	
						}
						targetWinningProbability = calculateChancesAgainst(p, up);
						possibleTarget = up;
						System.out.println(String.format("Considering need for minerals - chances of %s vs %s: %s", p, up, targetWinningProbability));
					}
				}
				for(Planet pp : gms.getPlanets(Owner.PLAYER)) {
					if(calculateChancesAgainst(p, pp) > targetWinningProbability) {
						if(attackingPlanets.size() == 0) {
							attackingPlanets.add(0, p);							
						} else {
							attackingPlanets.set(0, p);	
						}
						targetWinningProbability = calculateChancesAgainst(p, pp);
						possibleTarget = pp;
						System.out.println(String.format("Considering need for minerals - chances of %s vs %s: %s", p, pp, targetWinningProbability));
					}
				}
			}
		} else if(totalNeedForFood > level.getTransferThreshold()) {
			System.out.println(String.format("Considering need for food: %s (transfer threshold: %s)", totalNeedForFood, level.getTransferThreshold()));
			for(Planet p : compPlanets) {
				for(Planet up : gms.getPlanets(Owner.UNDEFINED)) {
					if(calculateChancesAgainst(p, up) > targetWinningProbability) {
						if(attackingPlanets.size() == 0) {
							attackingPlanets.add(0, p);							
						} else {
							attackingPlanets.set(0, p);	
						}
						targetWinningProbability = calculateChancesAgainst(p, up);
						possibleTarget = up;
						System.out.println(String.format("Considering need for food - chances of %s vs %s: %s", p, up, targetWinningProbability));
					}
				}
				for(Planet pp : gms.getPlanets(Owner.PLAYER)) {
					if(calculateChancesAgainst(p, pp) > targetWinningProbability) {
						if(attackingPlanets.size() == 0) {
							attackingPlanets.add(0, p);							
						} else {
							attackingPlanets.set(0, p);	
						}
						targetWinningProbability = calculateChancesAgainst(p, pp);
						possibleTarget = pp;
						System.out.println(String.format("Considering need for food - chances of %s vs %s: %s", p, pp, targetWinningProbability));
					}
				}
			}
		} else {
			//moze w takim razie zdominowac plansze?
			System.out.println(String.format("Considering attack:"));
			for (Planet p : compPlanets) {
				for (Planet pp : gms.getPlanets(Owner.PLAYER)) {
					if (calculateChancesAgainst(p, pp) > targetWinningProbability) {
						if (attackingPlanets.size() == 0) {
							attackingPlanets.add(0, p);
						} else {
							attackingPlanets.set(0, p);
						}
						targetWinningProbability = calculateChancesAgainst(p, pp);
						possibleTarget = pp;
						System.out.println(String.format("Considering attack - chances of %s vs %s: %s", p, pp, targetWinningProbability));
					}
				}
			}
		}
		if(possibleTarget != null) {
			gms.addAttackActions(attackingPlanets, possibleTarget);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean tryToTransferGoods() {
		if(gms.getPlanets(Owner.COMPUTER).size() <=1) {
			return false;
		}
		ArrayList<Planet> src = new ArrayList<Planet>();
		Planet dst = null;
		int foodMinLevel = 50, mineralMinLevel = 50;
		for(Planet p : gms.getPlanets(Owner.COMPUTER)) {
			if(p.getMinerals().getQuantity() <= mineralMinLevel || p.getFood().getQuantity() <= foodMinLevel) {
				dst = p;
			}
			if(p.getMinerals().getQuantity() <= mineralMinLevel && p.getFood().getQuantity() <= foodMinLevel) {
				dst = p;
			}
		}
		if(dst != null) {
			src = new ArrayList<Planet>(gms.getPlanets(Owner.COMPUTER));
			src.remove(dst);
		}
		if(!src.isEmpty() && dst != null) {
			gms.addTransferActions(src, dst);
			System.out.println(String.format("Transferring goods to %s", dst));
			return true;
		}
		return false;
	}
	
	/**
	 * Liczy procentowe zapotrzebowanie na minera³y wzglêdem puli z³o¿onej z 
	 * minera³ów nale¿¹cych do playera i compa
	 * @return
	 */
	private float calculateTotalNeedForMinerals() {
		//policzyc swoje, playera i undef i okreslic...
		//potrzeba bedzie odwrotn¹ proporcjonaln¹ do udzia³u w puli
		float comp = 0, player = 0, undef = 0;
		for(Planet p : gms.getPlanets(Owner.COMPUTER)) {
			comp += p.getMinerals().getQuantity();
		}
//		for(Planet p : gms.getPlanets(Owner.UNDEFINED)) {
//			undef += p.getMinerals().getQuantity();
//		}
		for(Planet p : gms.getPlanets(Owner.PLAYER)) {
			player += p.getMinerals().getQuantity();
		}
		float sum = comp + player + undef;
		return 1 - (comp / sum);
	}
	
	/**
	 * TODO dorobi kalkulowanie potrzeb dla poszczegolnych planet aby mozna decydowa gdzie transferowac...
	 * @return
	 */
	private float calculateTotalNeedForFood() {
		float comp = 0, player = 0, undef = 0;
		for(Planet p : gms.getPlanets(Owner.COMPUTER)) {
			comp += p.getFood().getQuantity();
		}
//		for(Planet p : gms.getPlanets(Owner.UNDEFINED)) {
//			undef += p.getFood().getQuantity();
//		}
		for(Planet p : gms.getPlanets(Owner.PLAYER)) {
			player += p.getFood().getQuantity();
		}
		float sum = comp + player + undef;
		return 1 - (comp / sum);
	}
	
	/**
	 * przelicz szanse laczne wszystkich planet komputera przeciw planecie 
	 * pewne zwyciestwo - 1.0 i wyzej
	 * 
	 * @param otherPlanet
	 * @return
	 */
	private float calculateChancesAgainst(Planet otherPlanet) {
		int compsum = 0, other = otherPlanet.getShips().getQuantity();
		for(Planet p : gms.getPlanets(Owner.COMPUTER)) {
			compsum += p.getShips().getQuantity();
		}
		if(other > 0) {
			return compsum / other;
		} else {
			return compsum;
		}
	}
	
	/**
	 * przelicz szanse konkretnej planety komputera przeciw planecie
	 * pewne zwyciestwo - 1.0 i wyzej
	 * 
	 * @param computerPlanet
	 * @param otherPlanet
	 * @return
	 */
	private float calculateChancesAgainst(Planet computerPlanet, Planet otherPlanet) {
		int cs = computerPlanet.getShips().getQuantity();
		int os = otherPlanet.getShips().getQuantity();
		if(os > 0) {
			return cs / os;
		} else {
			return cs;
		}
	}
		
	/**
	 * pause/unpause AI and its actions...
	 * @param paused
	 */
	public void setPaused(boolean paused) {
		this.isPaused = paused;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * podejmowanie decyzji tym czesciej im wyzszy poziom
	 */
	@Override
	public void tick(TickEvent e) {
		if (!isPaused) {
			int s = (int)(1 / level.getDecisionsPerSecond());
			if (mf.getGameTimeCounter().getSeconds() % s == 0) {
				act();
			}
		}
	}
	
	@Override
	public void disposeTickListener() {
		mf.getGameTimeCounter().removeSecondsListener(this);
	}
}