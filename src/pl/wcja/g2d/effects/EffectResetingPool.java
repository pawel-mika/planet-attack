package pl.wcja.g2d.effects;

import com.badlogic.gdx.utils.Pool;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public abstract class EffectResetingPool extends Pool<SpriteEffect> {
	
    public EffectResetingPool(int initialCapacity, int max)
    {
    	super(initialCapacity, max);
    }

    public SpriteEffect obtain()
    {
    	SpriteEffect elem = (SpriteEffect)super.obtain();
    	elem.reset();
    	return elem;
    }
//
//    public volatile Object obtain()
//    {
//    	return obtain();
//    }

}
