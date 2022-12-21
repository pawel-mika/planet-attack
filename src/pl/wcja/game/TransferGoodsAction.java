package pl.wcja.game;

import pl.wcja.IMainFrame;
import pl.wcja.g2d.ExtSprite;
import pl.wcja.g2d.Planet;
import pl.wcja.screen.GameMainScreen;
import pl.wcja.utils.MathUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author <a href="mailto:pawel.mika@geomar.pl">Pawe³ Mika</a>, Geomar SA
 *
 */
public class TransferGoodsAction extends InterplanetaryAction {

	private ExtSprite actionSprite = null;
	private Color actionColor = Color.GREEN;
	private int minerals = 0;
	private int food = 0;
	private int ships = 0;
	private float amountPercent = 0.5f;
	
	public TransferGoodsAction(IMainFrame mf, GameMainScreen gms, Planet srcPlanet, Planet dstPlanet, int speed, Color actionColor) {
		super(mf, gms, srcPlanet, dstPlanet, speed);
		this.actionColor = actionColor;
		actionSprite = new ExtSprite(mf, mf.getTexturePack("main").findRegion("ico_transfer"), srcPlanet.getXCenter(), srcPlanet.getYCenter(), 1.0f);
		minerals = (int)(srcPlanet.getMinerals().getQuantity() * amountPercent);
		srcPlanet.getMinerals().substract(minerals);
		food = (int)(srcPlanet.getFood().getQuantity() * amountPercent);
		srcPlanet.getFood().substract(food);
		ships = (int)(srcPlanet.getShips().getQuantity() * amountPercent);
		srcPlanet.getShips().substract(ships);
	}

	@Override
	public void drawAction() {
		if(!isFinished()) {
			Vector2 p = MathUtils.getPointOnLine(srcPoint, dstPoint, actionProgress);
			actionSprite.setCenterPosition(p.x, p.y);
			gms.drawLine(srcPoint, dstPoint, 3, actionColor);
//			mf.getSpriteBatch().begin();
			actionSprite.draw(mf.getSpriteBatch());
//			mf.getSpriteBatch().end();
		}
	}

	@Override
	public void finishAction() {
		if(actionOwner == dstPlanet.getOwner()) {
			dstPlanet.getFood().add(food);
			dstPlanet.getMinerals().add(minerals);
			dstPlanet.getShips().add(ships);
		} else {
			dstPlanet.attack(ships, actionOwner);
			//inaczej atakowaæ?
			dstPlanet.getFood().add(food);
			dstPlanet.getMinerals().add(minerals);
//			dstPlanet.atta
//			gms.performAttack(Arrays.asList(srcPlanet), dstPlanet);
		}
	}
}