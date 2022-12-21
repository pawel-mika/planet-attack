package pl.wcja;

import java.util.Random;

import pl.wcja.game.GameLevel;
import pl.wcja.game.GameTimeCounter;
import pl.wcja.game.Settings;
import pl.wcja.game.ai.AI;
import pl.wcja.screen.GameMainScreen;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public interface IMainFrame extends IScreenManager {
	
	public abstract Settings getSettings();
	
	public abstract void setLoading(boolean isLoading);

	public abstract void quitApplication();

	public abstract void startGame();

	public abstract void endGame();

	public abstract void continueGame();

	public abstract void pauseGame(GameMainScreen gameMainScreen);

	public abstract void setGameTimeCounter(GameTimeCounter gtc);
	
	public abstract GameTimeCounter getGameTimeCounter();

	public abstract Random getRandom();

	public abstract SpriteBatch getSpriteBatch();
	
	public abstract void setSpriteBatch(SpriteBatch batch);

	public abstract TextureAtlas getTexturePack(String name);
		
	public abstract void setAI(AI ai);
	
	public abstract AI getAI();
	
	public abstract void addLoadable(Loadable loadable);

	public abstract BitmapFont getCachedBitmapFont(String filePath, FileType fileType);
	
	public abstract Sound getCachedSound(String filePath, FileType fileType);
	
	public abstract Texture getCachedTexture(String filePath, FileType filetype, boolean mipmap);

	public abstract void setLoadingDescription(String desc);

	public abstract void setGameLevel(GameLevel gameLevel);

	public abstract GameLevel getGameLevel();

	public abstract ParticleEffect getCachedParticleEffect(String particleFilePath, FileType filetype, TextureAtlas textureAtlas);

	public abstract ParticleEffect getCachedParticleEffect(String particleFilePath, FileType filetype, String imagesDir);

}
