package pl.wcja.g2d;

import pl.wcja.IMainFrame;
import pl.wcja.game.TickEvent;
import pl.wcja.game.TickEvent.TickType;
import pl.wcja.game.TickListener;
import pl.wcja.game.bonus.Bonus;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class ResourceSprite extends ExtSprite implements TickListener, Disposable {

	private float quantity = 0;
	private float maxQuantity = 0;
	private float growRate = 1.0f;
	private BitmapFontCache bfc = null;
	private String lastCachedValue = "0";
	private Bonus bonus = null;

	/**
	 * 
	 * @param mf
	 * @param region
	 * @param posX
	 * @param posY
	 * @param initialQuantity
	 * @param growRatePerSecond if == 0 - do not grow, if > 0 && < 0.1 -> set to min 0.1
	 */
	public ResourceSprite(IMainFrame mf, TextureRegion region, int posX, int posY, int initialQuantity, float growRatePerSecond) {
		super(mf, region, posX, posY);
		bfc = new BitmapFontCache(mf.getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal));
		setQuantity(initialQuantity);
		this.growRate = growRatePerSecond;
		if (growRate > 0) {
			while (growRate < 0.1) {
				growRate *= 1.1; // increase 10% while < 0.1
			}
		}
		mf.getGameTimeCounter().addTickListener(this);
		bfc.setText("" + (int)quantity, getX() + getWidthScaled(), getYCenter() + bfc.getFont().getLineHeight() / 2);//TODO poprawic t¹ poczatkowa pozycje...
	}

	public void disposeTickListener() {
		mf.getGameTimeCounter().removeTickListener(this);
	}
	
	public void dispose() {
		getTexture().dispose();
		disposeTickListener();
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
		updateQuantityStringIfNeeded();
		bfc.draw(spriteBatch);
	}

	public int getQuantity() {
		return (int)quantity;
	}
	
	/**
	 * Metoda która zmienia bitmapFontCache tylko gdy siê zmieni³a wyswietlana wartosc
	 */
	protected void updateQuantityStringIfNeeded() {
		String currVal = String.format("%s", (int) quantity);
		if(bfc != null && !currVal.equalsIgnoreCase(lastCachedValue)) {
			bfc.setText(currVal, getX() + getWidthScaled(), getYCenter() + bfc.getFont().getCapHeight() / 2);
			lastCachedValue = currVal;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Bonus> T getBonus() {
		return (T)bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	protected void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public void tick(TickEvent e) {
		if(e.getTickType() == TickType.TICK) {
			setQuantity(quantity += computeGrowth());// computeGrowth());
		} 
	}
	
	protected float computeGrowth() {
		return (growRate / 10);
	}

	public void add(float quantity) {
		setQuantity(this.quantity += quantity);
	}
	
	public void add(int quantity) {
		setQuantity(this.quantity += quantity);
	}
	
	public void substract(int quantity) {
		setQuantity(this.quantity -= quantity); 
	}
}