package pl.wcja.g2d;


import java.util.LinkedList;
import java.util.List;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.controls.Label;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.g2d.effects.MoveBy;
import pl.wcja.g2d.effects.SpriteEffect;
import pl.wcja.game.TickEvent;
import pl.wcja.game.TickEvent.TickType;
import pl.wcja.game.TickListener;
import pl.wcja.game.bonus.Bonus;
import pl.wcja.game.bonus.FoodBonus;
import pl.wcja.game.bonus.MineralsBonus;
import pl.wcja.game.bonus.ShipBonus;
import pl.wcja.game.resources.Food;
import pl.wcja.game.resources.Minerals;
import pl.wcja.game.resources.Ships;
import pl.wcja.screen.GameMainScreen;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;
import com.badlogic.gdx.utils.Disposable;

/**
 * If the bonus is found it's transferred to appropriate resource. 
 * The hiddenResources are transferred to main resources
 * and the bonus multiplier is working in resource computation.
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Planet extends ExtSprite implements TickListener, Disposable {

	private boolean initialized = false;
	
	public enum Owner {
		UNDEFINED,
		PLAYER,
		COMPUTER
	}
	private Owner owner = Owner.UNDEFINED;
	private ExtSprite bgCircleActive = null;
	private ExtSprite bgCirclePlayer = null;
	private ExtSprite bgCircleComputer = null;
	private float bgCircleMul = 1.1f;
	private boolean active = false;
	private Minerals minerals = null;
	private Food food = null;
	private Ships ships = null;
	private List<ResourceSprite> resources = null;
	private int shipMineralsCost = 5 + mf.getRandom().nextInt(5);
	private int shipFoodCost = 5 + mf.getRandom().nextInt(5);
	private int radiusAddition = 0;
	private Color activeColor = Color.WHITE;
	private List<Bonus> bonuses = new LinkedList<Bonus>();
	private Label bonusLabel = null;
	private ParticleEffect particleEffect = null;
	private GameMainScreen gameMainScreen = null;
	
	/**
	 * 
	 * @param mf
	 * @param region
	 * @param centerPosX
	 * @param centerPosY
	 * @param scaleXY
	 */
	public Planet(IMainFrame mf, GameMainScreen gms, TextureRegion region, float centerPosX, float centerPosY, float scaleXY) {
		super(mf, region, centerPosX, centerPosY, scaleXY);
		this.gameMainScreen = gms;
		resources = new LinkedList<ResourceSprite>();
		bgCircleActive = new ExtSprite(mf, 
				mf.getTexturePack("main").findRegion("circle_white"),
				centerPosX, 
				centerPosY, 
				scaleXY);
		bgCircleActive.setScale(bgCircleActive.getScaleX() * bgCircleMul);
		bgCircleActive.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		
		bgCirclePlayer = new ExtSprite(mf, 
				mf.getTexturePack("main").findRegion("circle_blue"),
				centerPosX, 
				centerPosY, 
				scaleXY);
		bgCirclePlayer.setScale(bgCirclePlayer.getScaleX() * bgCircleMul);
		bgCirclePlayer.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);

		bgCircleComputer = new ExtSprite(mf, 
				mf.getTexturePack("main").findRegion("circle_red"),
				centerPosX, 
				centerPosY, 
				scaleXY);
		bgCircleComputer.setScale(bgCircleComputer.getScaleX() * bgCircleMul);
		bgCircleComputer.getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		
		bonusLabel = new Label(mf, mf.getCachedBitmapFont("gfx/font/bonus_16.fnt", FileType.Internal), "bonus", getX(), getY() + getHeightScaled());
		
		particleEffect = new ParticleEffect(mf.getCachedParticleEffect("gfx/particles", FileType.Internal, "gfx"));
		particleEffect.setPosition(getXCenter(), getYCenter());
		
		createResources();
		
		mf.getGameTimeCounter().addSecondsListener(this);
//		mf.getGameTimeCounter().addTickListener(this);
		initialized = true;
	}
	
	/**
	 * 
	 */
	private void createResources() {
		int maxShips = 0;
		switch(mf.getAI().getAILevel()) {
		case EASY:
			maxShips = 10;
			break;
		case NORMAL:
			maxShips = 25;
			break;
		case HARD:
			maxShips = 50;
			break;
		case MEGAMIND:
			maxShips = 75;
			break;
		}

		ships = new Ships(mf, (int)getScaledX(), (int)(getScaledY() + getHeightScaled()), mf.getRandom().nextInt(maxShips), 0.0f);
		ships.setPosition(getScaledX(), (getScaledY() + getHeightScaled()) - ships.getRegionHeight());
		resources.add(0, ships);
		minerals = new Minerals(mf, (int)ships.getX(), (int)ships.getY() - ships.getRegionHeight(), mf.getRandom().nextInt(1000), mf.getRandom().nextFloat());
		resources.add(minerals);
		food = new Food(mf, (int)minerals.getX(), (int)minerals.getY() - minerals.getRegionHeight(), mf.getRandom().nextInt(1000), mf.getRandom().nextFloat());
		resources.add(food);
	}
	
	public void disposeTickListener() {
		mf.getGameTimeCounter().removeSecondsListener(this);
//		mf.getGameTimeCounter().removeTickListener(this);
		for(ResourceSprite rs : resources) {
			rs.disposeTickListener();
		}
	}
	
	public void dispose() {
		disposeTickListener();
		for(ResourceSprite rs : resources) {
			rs.dispose();
		}
		bgCircleActive.getTexture().dispose();
		bgCircleComputer.getTexture().dispose();
		bgCirclePlayer.getTexture().dispose();
	}

	public void setShips(int shipCount) {
		this.ships.setQuantity(shipCount);
	}
	
	public void attack(int aShipsCount, Owner attackingShipsOwner) {
		getShips().substract(aShipsCount);
		if(getShips().getQuantity() <= 0) {
			Owner prevOwner = getOwner();
			ships.setQuantity(Math.abs(ships.getQuantity()));
			setOwner(attackingShipsOwner);
			if(getOwner() != Owner.PLAYER) {
				setActive(false);
			}
			if(prevOwner != getOwner()) {
				performBonusScan();
			}
			particleEffect.start();
		}
		mf.getCachedSound("snd/attack.ogg", FileType.Internal).play(mf.getSettings().getSoundVolume());
	};
	
	private void performBonusScan() {
		boolean bonusFound = false;
		String bonusMsg = "Bonus:\r\n";
		for(Bonus b : getBonuses()) {
			if(b instanceof ShipBonus) {
				ShipBonus sb = (ShipBonus)b;
				int hr = sb.getHiddenResource();
				ships.setBonus(sb);
				ships.add(hr);
				if(hr != 0) {
					bonusMsg += String.format("Ships +%s, prod. x%s\r\n", hr, sb.getProductionSpeedMultiplier());
				} else {
					bonusMsg += String.format("Ships prod. x%s\r\n", sb.getProductionSpeedMultiplier());
				}
			} else if(b instanceof FoodBonus) {
				FoodBonus fb = (FoodBonus)b;
				int hr = fb.getHiddenResource();
				food.setBonus(fb);
				food.add(hr);
				if(hr != 0) {
					bonusMsg += String.format("Food +%s, prod. x%s\r\n", hr, fb.getProductionSpeedMultiplier());
				} else {
					bonusMsg += String.format("Food prod. x%s\r\n", fb.getProductionSpeedMultiplier());
				}
			} else if(b instanceof MineralsBonus) {
				MineralsBonus mb = (MineralsBonus)b;
				int hr = mb.getHiddenResource();
				minerals.setBonus(mb);
				minerals.add(hr);
				if(hr != 0) {
					bonusMsg += String.format("Minerals +%s, prod. x%s\r\n", hr, mb.getProductionSpeedMultiplier());
				} else {
					bonusMsg += String.format("Minerals prod. x%s\r\n", mb.getProductionSpeedMultiplier());
				}
			}
			bonusFound = true;
		}
		//if bonus is found calculate and andd label to show
		if(bonusFound) {
			//if previous lavel is present - remove it!
			if(bonusLabel != null) {
				gameMainScreen.removeBonusInformerLabel(bonusLabel);
				bonusLabel = new Label(mf, mf.getCachedBitmapFont("gfx/font/bonus_16.fnt", FileType.Internal), "bonus", getX(), getY() + getHeightScaled()); 
			}
			int sw = Gdx.graphics.getWidth();
			int sh = Gdx.graphics.getHeight();
			float tx = getScaledX();
			float ty = getScaledY();
			float ths = getHeightScaled();
			float tws = getWidthScaled();
			bonusLabel.setMultilineText(bonusMsg, tx, ty, 1.0f, HAlignment.LEFT);
			bonusLabel.setPosition(tx, ty);
			float bx = bonusLabel.getX();
			float by = bonusLabel.getY();
			float bw = bonusLabel.getWidth();
			float bh = bonusLabel.getHeight();
			
			if(bx + bw > sw) {
				bonusLabel.setPosition(sw - bw, by);
			}
			float nMove = ths;
			float move = by + nMove > sh ? sh - (by + nMove) : nMove; 
			SpriteEffect eff = MoveBy.$(0, move, 6.0f).setInterpolator(DecelerateInterpolator.$(6.0f));
			bonusLabel.addEffect(eff);
			eff.setCompletionListener(new EffectCompletedListener() {
				@Override
				public void completed(EffectCompletedEvent evt) {
					gameMainScreen.removeBonusInformerLabel(bonusLabel);
				}
			});
			gameMainScreen.addBonusInformerLabel(bonusLabel);
			mf.getCachedSound("snd/bonus.ogg", FileType.Internal).play(mf.getSettings().getSoundVolume());
		}
	}
	
	public void setActive(boolean active) {
		this.active = active;
		if(!active) {
			radiusAddition = 0;
		}
	}

	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		if(initialized) {
			bgCircleActive.setPosition(x, y);
			bgCirclePlayer.setPosition(x, y);
			bgCircleComputer.setPosition(x, y);
			int i = 0;
			for (ResourceSprite rs : resources) {
				rs.setPosition((int) getScaledX(), (int) getScaledY() + getHeightScaled() - (rs.getRegionHeight() * ++i));
			}
		}
	}

	@Override
	public void setScale(float scaleXY) {
		super.setScale(scaleXY);
		if(initialized) {
			bgCircleActive.setScale(scaleXY * bgCircleMul);
			bgCirclePlayer.setScale(scaleXY * bgCircleMul);
			bgCircleComputer.setScale(scaleXY * bgCircleMul);
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		particleEffect.draw(spriteBatch, Gdx.graphics.getDeltaTime());
		switch (owner) {
			case PLAYER:
				bgCirclePlayer.draw(spriteBatch);//moze dorobic jakiœ cache dla tych kó³ek co by nie liczyæ ich co ka¿d¹ klatkê?
//				mf.getScreen().drawCircle(getXCenter(), getYCenter(), getWidthScaled() / 2 , 3, 12, Color.BLUE);
				break;
			case COMPUTER:
				bgCircleComputer.draw(spriteBatch);
//				mf.getScreen().drawCircle(getXCenter(), getYCenter(), getWidthScaled() / 2 , 3, 12, Color.RED);
				break;
		}
		if(active) {
			if(mf.getGameTimeCounter().getTicks() % 4 == 0) {
				bgCircleActive.draw(spriteBatch);
			}
		}
		super.draw(spriteBatch);
		//rysowanie kó³ka jednak troche zwalnia.. awruk!
//		if(active) {
//			mf.getScreen().drawCircle(getXCenter(), getYCenter(), (getWidthScaled() / 2) + radiusAddition, 2, 20, activeColor);
//		}		
		for(ResourceSprite rs : resources) {
			rs.draw(spriteBatch);
		}
	}
	
	public void draw(SpriteBatch spriteBatch, int layerIndex) {
		super.draw(spriteBatch, layerIndex);
	}
	
	@Override
	public void tick(TickEvent e) {
		if(e.getTickType() == TickType.SECOND) {
			recalculateGrowth();	
		} 
	}

	private void recalculateGrowth() {
		if(minerals.getQuantity() > shipMineralsCost &&
			food.getQuantity() > shipFoodCost) {
			minerals.substract(shipMineralsCost);
			food.substract(shipFoodCost);
			ships.add(1 * (ships.getBonus() != null ? ships.getBonus().getProductionSpeedMultiplier() : 1));
		}
	}

	public Ships getShips() {
		return ships;
	}
	
	public ResourceSprite getMinerals() {
		return minerals;
	}
	
	public ResourceSprite getFood() {
		return food;
	}
	
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void attack(Ships ships) {
		
	}
	
	public List<Bonus> getBonuses() {
		return bonuses;
	}

	public void setBonuses(List<Bonus> bonuses) {
		this.bonuses = bonuses;
	}

	@Override
	public String toString() {
		return String.format("%s; owner: %s", super.toString(), owner.toString());
	}
}