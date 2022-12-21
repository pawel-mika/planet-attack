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
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class AttackPlanetAction extends InterplanetaryAction {

	private ExtSprite actionSprite = null;
	private Color actionColor = Color.RED;
	private int attackingShips = 0;
	private float amountPercent = 0.5f;
	
	public AttackPlanetAction(IMainFrame mf, GameMainScreen gms, Planet srcPlanet, Planet dstPlanet, int actionSpeed, Color actionColor) {
		super(mf, gms, srcPlanet, dstPlanet, actionSpeed);
		this.actionColor = actionColor;
		actionSprite = new ExtSprite(mf, mf.getTexturePack("main").findRegion("ico_attack"), srcPlanet.getXCenter(), srcPlanet.getYCenter(), 1.0f);
		this.attackingShips = (int)(srcPlanet.getShips().getQuantity() * amountPercent);
		srcPlanet.getShips().substract(attackingShips);
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
		if(actionOwner != dstPlanet.getOwner()) {
			dstPlanet.attack(attackingShips, actionOwner);
		} else {
			dstPlanet.getShips().add(attackingShips);
		}
	}	
}