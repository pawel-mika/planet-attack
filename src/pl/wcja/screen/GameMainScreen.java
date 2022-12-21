package pl.wcja.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Planet;
import pl.wcja.g2d.Planet.Owner;
import pl.wcja.g2d.Screen2D;
import pl.wcja.g2d.controls.Label;
import pl.wcja.game.AttackPlanetAction;
import pl.wcja.game.InterplanetaryAction;
import pl.wcja.game.TickEvent;
import pl.wcja.game.TickEvent.TickType;
import pl.wcja.game.TickListener;
import pl.wcja.game.TransferGoodsAction;
import pl.wcja.game.ai.AI;
import pl.wcja.utils.GameUtils;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

/**
 * TODO dodaæ obs³uge (zdarzenia lub inne) akcji gdy transfer/atak na planete i ona w czasie tego zmienia ownera!
 * +EWentualnie w momencie kiedy zaczyna sie akcja - odjaæ resources z planety i zapamiêtaæ je w akcji!
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 * 
 */
public class GameMainScreen extends Screen2D implements TickListener {

	private int planetCount = 4;

	private ExtSprite esPause = null;
	private ArrayList<Planet> planets;
	private ArrayList<Planet> selectedPlayerPlanets;
	private Planet targetPlanet;
	private List<InterplanetaryAction> activeActions;
	private List<Label> bonusInformers;
	private float dragSourceX, dragSourceY;
	private float dragTargetX, dragTargetY;
	private boolean initialized = false;
	private GameResultScreen grs = null;
	private BitmapFontCache bfcTimer = null;
	private BitmapFontCache bfcPoints = null;
	private int initialScore = 0, score = 0;

	public GameMainScreen(IMainFrame mf) {
		super(mf);
		selectedPlayerPlanets = new ArrayList<Planet>();
		bonusInformers = new ArrayList<Label>();
		activeActions = new ArrayList<InterplanetaryAction>();
		//prepare result screen in THE SAME THREAD/CONTEXT
		grs = new GameResultScreen(mf, false);
		initialize();
		mf.getGameTimeCounter().resetConuter();
		mf.getGameTimeCounter().pauseCounter(false);
		initialized = true;
	}

	private void initialize() {
		Texture t = mf.getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		setBackgroundImage(t);
		TextureRegion trPause = new TextureRegion(mf.getTexturePack("hud").findRegion("ico_pause"));
		esPause = new ExtSprite(mf, trPause, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - (trPause.getRegionHeight() / 2), 0.5f);
		esPause.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - (esPause.getHeightScaled() / 2));
		addTouchable(esPause);
		bfcTimer = new BitmapFontCache(mf.getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal));
		bfcTimer.setText("00:00", 2, Gdx.graphics.getHeight() - 2);
		bfcPoints = new BitmapFontCache(mf.getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal));
		setScore(0);
	}

	/**
	 * re-generate map of planets (TODO reimplement map generation for better and faster algorithm)
	 */
	public void reGeneratePlanets() {
		TextureRegion trPlanetEarth = mf.getTexturePack("main").findRegion("planet_earth"); 
		TextureRegion trPlanetGlow = mf.getTexturePack("main").findRegion("planet_glow");
		TextureRegion trPlanetMars = mf.getTexturePack("main").findRegion("planet_mars");
		
		trPlanetEarth.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		trPlanetGlow.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		trPlanetMars.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		
		mf.setAI(new AI(mf, this));
		
		planets = GameUtils.generatePlanets(mf, this, planetCount, trPlanetEarth, null);
		planets = GameUtils.generatePlanets(mf, this, planetCount, trPlanetGlow, planets);
		planets = GameUtils.generatePlanets(mf, this, planetCount, trPlanetMars, planets);
		
		clearTouchables();
		for(Planet p : planets) {
			addTouchable(p);
		}
		addTouchable(esPause);
	}
	
	/**
	 * 
	 */
	public void randomizeStart() {
		//randomize start planets
		int playerPlanet = mf.getRandom().nextInt(planets.size());
		int computerPlanet = mf.getRandom().nextInt(planets.size());
		while(computerPlanet == playerPlanet) {
			computerPlanet = mf.getRandom().nextInt(planets.size());
		}
		planets.get(playerPlanet).setOwner(Owner.PLAYER);
		planets.get(playerPlanet).setShips(100);
		planets.get(computerPlanet).setOwner(Owner.COMPUTER);
		planets.get(computerPlanet).setShips(100);

		//randomize bonuses on planets
		int bonusPlanetsCount = (int)(planets.size() * mf.getGameLevel().getBonusProbability());
		if(bonusPlanetsCount == 0) {
			bonusPlanetsCount = 1; // lets giv'em one at least...
		}
		for(Planet p : planets) {
			if(p.getOwner() == Owner.UNDEFINED) {
				p.setBonuses(GameUtils.getRandomBonuses(mf));
			}
			bonusPlanetsCount--;
			if(bonusPlanetsCount < 1) {
				break;
			}
		}
		
		mf.getAI().setPaused(false);//RUN AI
		mf.getGameTimeCounter().addSecondsListener(this);
	}

	@Override
	public void render(float f) {
		super.render(f);
		if (isDragging() && selectedPlayerPlanets != null && !selectedPlayerPlanets.isEmpty()) {
//			synchronized (selectedPlayerPlanets) {
				for (Planet p : selectedPlayerPlanets) {
					drawLine(p.getXCenter(), p.getYCenter(), dragTargetX, dragTargetY, 2, Color.WHITE);
				}
//			}
		} else if (isDragging() && selectedPlayerPlanets != null && selectedPlayerPlanets.isEmpty()) {
			drawLine(dragSourceX, dragSourceY, dragTargetX, dragTargetY, 2, Color.WHITE);
		}
		renderPlanets();
		renderActions();
		if(initialized) {
			esPause.draw(mf.getSpriteBatch());
			bfcTimer.draw(mf.getSpriteBatch());
			bfcPoints.draw(mf.getSpriteBatch());
		}
		renderBonusInformers();
	};
	
	@Override
	public void resize(int i, int j) {
		super.resize(i, j);
		esPause.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - (esPause.getHeightScaled() / 2));
	};
	
	@Override
	public void pause() {
		super.pause();
		for(InterplanetaryAction a : activeActions) {
			a.setPaused(true);
		}
	}

	@Override
	public void resume() {
		super.resume();
		for(InterplanetaryAction a : activeActions) {
			a.setPaused(false);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		mf.getGameTimeCounter().removeSecondsListener(this);
		for(Planet p : planets) {
			p.dispose();
		}
		Gdx.input.setInputProcessor(null);
	};

	private void renderPlanets() {
		if (planets != null) {
			for (Planet p : planets) {
				p.draw(mf.getSpriteBatch());
			}
		}
	}
	
	/**
	 * 
	 */
	private void renderActions() {
		if (activeActions != null) {
			synchronized (activeActions) {
				Iterator<InterplanetaryAction> i = activeActions.iterator();
				while (i.hasNext()) {
					InterplanetaryAction a = i.next();
					Owner prevOwner = a.getDstPlanet().getOwner();
					a.tick(new TickEvent(this, mf.getGameTimeCounter().getTicks(), mf.getGameTimeCounter().getSeconds(), TickType.OTHER));
					if (a.isFinished()) {
						if (a instanceof AttackPlanetAction) {
							mf.getCachedSound("snd/snd1.ogg", FileType.Internal).play();
						} else if (a instanceof TransferGoodsAction) {
							mf.getCachedSound("snd/transfer.ogg", FileType.Internal).play();
						}
						if (prevOwner != a.getDstPlanet().getOwner()) {
							removeSelectedPlanet(a.getDstPlanet());
						}
						i.remove();
						continue;
					} else {
						a.drawAction();
					}
				}
			}
		}
	}
	
	/**
	 * Render labels informing of found bonuses...
	 */
	private void renderBonusInformers() {
		if(bonusInformers != null) {
			synchronized (GameMainScreen.this) {
				Iterator<Label> i = bonusInformers.iterator();
				while(i.hasNext()) {
					Label bl = i.next();
					bl.draw(mf.getSpriteBatch());
				}
			}
		}
	}
	
	@Override 
	protected void spriteTouched(pl.wcja.g2d.ExtSprite s) {
		if(s instanceof Planet) {
			touchedPlanet((Planet)s);
		} else if(s instanceof ExtSprite) {
			if(s == esPause) {
				mf.pauseGame(this);
			}
		}
	};
	
	@Override 
	protected void spriteDoubleTouched(pl.wcja.g2d.ExtSprite s) {
		if(s instanceof Planet) {
			doubleTouchedPlanet((Planet)s);
		}
	};
	
	@Override 
	protected void touchDragged(pl.wcja.g2d.ExtSprite sFrom, pl.wcja.g2d.ExtSprite sTo) {
		if(sFrom instanceof Planet && sTo instanceof Planet) {
			draggedToPlanet((Planet)sFrom, (Planet)sTo);
		}
	};
	
	@Override 
	protected void touchDragged(int x1, int y1, int x2, int y2) {
		dragSourceX = x1;
		dragSourceY = y1;
		dragTargetX = x2;
		dragTargetY = y2;
	};
	
	/**
	 * 
	 * @param p
	 */
	private void touchedPlanet(Planet p) {
		if ((p.getOwner() == Owner.COMPUTER || p.getOwner() == Owner.UNDEFINED)) {
			targetPlanet = p;
			// activeActions.put(activePlanets, targetPlanet);
			// activePlanets = new ArrayList<Planet>();
			addAttackActions(selectedPlayerPlanets, targetPlanet);
		} else if(p.getOwner() == Owner.PLAYER) {
			if(selectedPlayerPlanets.isEmpty()) {
				addSelectedPlanet(p);
			} else {
				if(selectedPlayerPlanets.contains(p)) {
					removeSelectedPlanet(p);
				} else {
					addSelectedPlanet(p);
				}
			}
		}
	}
	
	private void doubleTouchedPlanet(Planet p) {
		if(p.getOwner() == Owner.PLAYER) {
			//select all player planets
			for(Planet planet : getPlanets(Owner.PLAYER)) {
				addSelectedPlanet(planet);
			}
		} else {
			addAttackActions(getPlanets(Owner.PLAYER), targetPlanet);
		}
	}

	/**
	 * 
	 * @param pFrom
	 * @param pTo
	 */
	private void draggedToPlanet(Planet pFrom, Planet pTo) {
		if (initialized) {
			targetPlanet = pTo;
			if (selectedPlayerPlanets.isEmpty() && pFrom.getOwner() == Owner.PLAYER) {
				addSelectedPlanet(pFrom);
			}
			if (targetPlanet.getOwner() == Owner.UNDEFINED || targetPlanet.getOwner() == Owner.COMPUTER) {
				addAttackActions(selectedPlayerPlanets, targetPlanet);
				clearSelectedPlanets();
			} else {
				addTransferActions(selectedPlayerPlanets, targetPlanet);
				clearSelectedPlanets();
			}
		}
	}
	
	private void addSelectedPlanet(Planet p) {
		p.setActive(true);
		selectedPlayerPlanets.add(p);
	}
	
	private void removeSelectedPlanet(Planet p) {
		synchronized (selectedPlayerPlanets) {
			p.setActive(false);
			selectedPlayerPlanets.remove(p);
		}
	}
	
	private void clearSelectedPlanets() {
		synchronized(selectedPlayerPlanets) {
			for(Planet p : selectedPlayerPlanets) {
				p.setActive(false);
			}
			selectedPlayerPlanets.clear();
		}
	}
	
	public List<InterplanetaryAction> getActiveActions() {
		return activeActions;
	}

	/**
	 * Get the planets belonging to specified owner
	 * @param owner
	 * @return
	 */
	public ArrayList<Planet> getPlanets(Owner owner) {
		ArrayList<Planet> ownerPlanets = new ArrayList<Planet>();
		for(Planet p : planets) {
			if(p.getOwner() == owner) {
				ownerPlanets.add(p);
			}
		}
		return ownerPlanets;
	}

	/**
	 * 
	 * @param transferringPlanets
	 * @param target
	 */
	public void addTransferActions(List<Planet> transferringPlanets, Planet target) {
		for(Planet sp : transferringPlanets) {
			addInterplanetaryAction(new TransferGoodsAction(mf, this, sp, target, 8, sp.getOwner() == Owner.PLAYER ? Color.BLUE : Color.RED));
		}
	}

	/**
	 * 
	 * @param attackingPlanets
	 * @param target
	 */
	public void addAttackActions(List<Planet> attackingPlanets, Planet target) {
		for(Planet sp : attackingPlanets) {
			addInterplanetaryAction(new AttackPlanetAction(mf, this, sp, target, 6, sp.getOwner() == Owner.PLAYER ? Color.BLUE : Color.RED));
		}
	}

	/**
	 * 
	 * @param action
	 */
	public void addInterplanetaryAction(InterplanetaryAction action) {
//		synchronized (activeActions) {
			activeActions.add(action);
//		}
	}
	
	/**
	 * 
	 * @param action
	 */
	public void removeInterplanetaryAction(InterplanetaryAction action) {
		synchronized (activeActions) {
			activeActions.remove(action);
		}
	}
	
	/**
	 * Adds a label informing of fonud bonuses to the list of
	 * bonuses being rendered.
	 * 
	 * @param bonusLabel
	 */
	public void addBonusInformerLabel(Label bonusLabel) {
		bonusInformers.add(bonusLabel);
	}
	
	/**
	 * Removes bonus-label from render list
	 * 
	 * @param bonusLabel
	 */
	public void removeBonusInformerLabel(final Label bonusLabel) {
		synchronized (bonusInformers) {
			bonusInformers.remove(bonusLabel);
		}				
	}
	
	/**
	 * 
	 */
	private void updateScores() {
		if(initialScore == 0) {
			for(Planet p : getPlanets(Owner.PLAYER)) {
				//initialScore - do odjêcia od ca³ego wyniku (¿eby nie punktowaæ za wylosowane na pocz¹tku zasoby)
				initialScore += (p.getShips().getQuantity() * 4) + (p.getFood().getQuantity() + p.getMinerals().getQuantity());
			}
		}
		int tmpScore = 0;
		for(Planet p : getPlanets(Owner.PLAYER)) {
			tmpScore += (p.getShips().getQuantity() * 4) + (p.getFood().getQuantity() + p.getMinerals().getQuantity());
		}
		setScore(tmpScore/* - initialScore*/);
	}
	
	/**
	 * 
	 * @param score
	 */
	private void setScore(int score) {
		this.score = score;
		bfcPoints.setMultiLineText("SCORE:\r\n" + score, 
				Gdx.graphics.getWidth() - 2, 
				Gdx.graphics.getHeight() - 2, 1.0f, HAlignment.RIGHT); 
	}
	
	/**
	 * 
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * 
	 */
	private void checkWinConditions() {
		int computerPlanets = 0;
		int playerPlanets = 0;
		for(Planet p : planets) {
			if(p.getOwner() == Owner.COMPUTER) {
				computerPlanets++;
			} else if(p.getOwner() == Owner.PLAYER) {
				playerPlanets++;
			}
		}
		if(computerPlanets == 0 && playerPlanets > 0) {
			disposeTickListener();
			mf.getAI().dispose();
			grs.setResult(true, getScore());
			mf.pushScreen(grs);
		} else if(computerPlanets > 0 && playerPlanets == 0) {
			disposeTickListener();
			mf.getAI().dispose();
			grs.setResult(false, getScore());
			mf.pushScreen(grs);
		}
	}
	
	@Override 
	public void backKeyPressed() {
		mf.pauseGame(this);
	};
	
	/**
	 * 
	 */
	private void deleteListenersFromPlanets() {
		for(Planet p : planets) {
			p.disposeTickListener();
		}
	}
	
	@Override
	public void disposeTickListener() {
		mf.getGameTimeCounter().removeSecondsListener(this);
		deleteListenersFromPlanets();
	}

	@Override
	public void tick(TickEvent e) {
		int playerActions = 0, computerActions = 0;
		for(InterplanetaryAction a : activeActions) {
			if(a.getActionOwner() == Owner.PLAYER) {
				playerActions++;
			} else if(a.getActionOwner() == Owner.COMPUTER){
				computerActions++;
			}
		}
		int pp = getPlanets(Owner.PLAYER).size();
		int cp = getPlanets(Owner.COMPUTER).size();
		if((pp != 0 && cp == 0 && computerActions == 0)
				|| (pp == 0 && cp != 0 && playerActions == 0)) {
			checkWinConditions();
		}
		if(e.getTickType() == TickType.SECOND) {
			bfcTimer.setText(String.format("%02d:%02d", e.getSecond() / 60, e.getSecond() % 60), 2, Gdx.graphics.getHeight() - 2);
			updateScores();
		}
	}
}