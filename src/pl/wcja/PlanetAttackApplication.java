package pl.wcja;

import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import pl.wcja.g2d.Screen2D;
import pl.wcja.g2d.SpectacularScreen2d;
import pl.wcja.g2d.effects.EffectCompletedEvent;
import pl.wcja.g2d.effects.EffectCompletedListener;
import pl.wcja.game.DesktopGameTimeCounter;
import pl.wcja.game.GameLevel;
import pl.wcja.game.GameTimeCounter;
import pl.wcja.game.Settings;
import pl.wcja.game.ai.AI;
import pl.wcja.screen.GameMainScreen;
import pl.wcja.screen.MenuStage;
import pl.wcja.screen.PauseScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class PlanetAttackApplication implements ApplicationListener, IMainFrame {

	private enum GameState {
		MAIN_MENU,
		PAUSED,
		PLAYING
	};
	
	private Settings settings = new Settings();
	private boolean isLoading = false;
	private Sprite sLoading = null;
	private BitmapFontCache bfcLoading = null;
	private GameState gameState = GameState.MAIN_MENU;
	private MenuStage menuStage = null;
	private Stack<Screen2D> screenStack = new Stack<Screen2D>();
	private GameTimeCounter gameTimeCounter = null;
	private SpriteBatch batch = null;
	private Random random = new Random(Calendar.getInstance().getTimeInMillis());
	private Map<String, BitmapFont> fontCache = new HashMap<String, BitmapFont>();
	private Map<String, Texture> textureCache = new HashMap<String, Texture>();
	private Map<String, TextureAtlas> texturePacks = new HashMap<String, TextureAtlas>();
	private Map<String, Sound> soundCache = new HashMap<String, Sound>();
	private Map<String, ParticleEffect> particleEffectCache = new HashMap<String, ParticleEffect>();
	private Music mainMusic = null;
	private AI ai = null;
	private GameLevel gameLevel = GameLevel.LEVEL1;
	private Queue<Loadable> loadables = new ConcurrentLinkedQueue<Loadable>();
	private PauseScreen pauseScreen = null;
	
	/**
	 * 
	 */
	public PlanetAttackApplication() {
		setLoading(true);
	}
	
	private void queueSoundsInitialization() {
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("60%");
				mf.getCachedSound("snd/snd_win.ogg", FileType.Internal);
				mf.getCachedSound("snd/snd1.ogg", FileType.Internal);
				mf.getCachedSound("snd/bonus.ogg", FileType.Internal);
				mf.getCachedSound("snd/attack.ogg", FileType.Internal);
			}
		});
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("70%");
				mf.getCachedSound("snd/transfer.ogg", FileType.Internal);
				mf.getCachedSound("snd/snd_lose.ogg", FileType.Internal);
			}
		});
	}

	private void queueFontInitialization() {
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("0%");
				getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal);
			}
		});
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("5%");
				getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal);
			}
		});
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("10%");
				getCachedBitmapFont("gfx/font/bonus_16.fnt", FileType.Internal);
			}
		});
	}
	
	private void queueTexturePacksInitializaion() {
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("30%");
				texturePacks.put("main", new TextureAtlas(Gdx.files.getFileHandle("gfx/pack_main.txt", FileType.Internal)));
			}
		});
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("40%");
				texturePacks.put("buttons", new TextureAtlas(Gdx.files.getFileHandle("gfx/pack_buttons.txt", FileType.Internal)));
			}
		});
		loadables.add(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("50%");
				texturePacks.put("title", new TextureAtlas(Gdx.files.getFileHandle("gfx/pack_title.txt", FileType.Internal)));
			}
		});
	}
	
	private void queueTextureCachePreload() {
		addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("15%");
				getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
			}
		});
		addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("20%");
				getCachedTexture("gfx/bg_manual.jpg", FileType.Internal, true);
				getCachedTexture("gfx/manual.png", FileType.Internal, true);
			}
		});
	}
	
	private void queuePatricleEffectPreload() {
		addLoadable(new Loadable() {
			@Override
			public void load(IMainFrame mf) {
				setLoadingDescription("80%");
				getCachedParticleEffect("gfx/particles", FileType.Internal, "gfx");
			}
		});
	}
	
	@Override
	public void addLoadable(Loadable loadable) {
		loadables.add(loadable);
	}
	
	@Override
	public void create() {
		if(getScreen() == null) {
			setGameTimeCounter(new DesktopGameTimeCounter(this, true));
			batch = new SpriteBatch(1024 * 4);
			setLoading(true);
			texturePacks.put("hud", new TextureAtlas(Gdx.files.getFileHandle("gfx/pack_hud.txt", FileType.Internal)));
			AtlasRegion trLoading = texturePacks.get("hud").findRegion("loading");
			sLoading = new Sprite(trLoading);
			sLoading.setPosition((Gdx.graphics.getWidth() - trLoading.getRegionWidth()) / 2, trLoading.getRegionHeight());
			bfcLoading = new BitmapFontCache(getCachedBitmapFont("gfx/font/trebuchet_32.fnt", FileType.Internal));
			bfcLoading.setPosition(sLoading.getX(), sLoading.getY() + sLoading.getHeight());
			settings.readSettings();
			queueFontInitialization();
			queueTextureCachePreload();
			queueTexturePacksInitializaion();
			queueSoundsInitialization();
			queuePatricleEffectPreload();
			loadables.add(new Loadable() {
				@Override
				public void load(IMainFrame mf) {
					setLoadingDescription("90%");
					pauseScreen = new PauseScreen(mf);
					menuStage = new MenuStage(PlanetAttackApplication.this, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
					Gdx.input.setInputProcessor(menuStage);
					Gdx.input.setCatchBackKey(true);
					setLoading(false);
					menuStage.showStage();
				}
			});
//			mainMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("snd/james_wars.ogg", FileType.Internal));
//			mainMusic.setLooping(true);
//			mainMusic.setVolume(getVolumeMusic());
//			mainMusic.play();
		} else {
			gameState = GameState.PLAYING;
		}
	}

	@Override
	public void resume() {
		switch (gameState) {
		case MAIN_MENU:
			if (menuStage != null) {
			}
			break;
		case PLAYING:
		case PAUSED:
			if (!screenStack.isEmpty()) {
				getScreen().resume();
			}
			break;
		}
	}

	@Override
	public void render() {
		if(getGameTimeCounter() != null) {
			gameTimeCounter.act(Gdx.graphics.getDeltaTime());
		}
		if(loadables != null && !loadables.isEmpty()) {
			performOneLoadingActionFromQueue();
		}
		switch (gameState) {
		case MAIN_MENU:
			if(isLoading) {
				Gdx.gl.glClearColor(0,0,0,0);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			}
			if (menuStage != null) {
				menuStage.act(Gdx.graphics.getDeltaTime());
				menuStage.draw();
			}
			break;
		case PLAYING:
		case PAUSED:
			if (!screenStack.isEmpty()) {
				try {
					getSpriteBatch().begin();
					getScreen().render(Gdx.graphics.getDeltaTime());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					getSpriteBatch().end();
				}
			}
			break;
		}
		getSpriteBatch().begin();
		renderHud();
		renderLoading();
		getSpriteBatch().end();
	}
	
	@Override
	public void resize(int i, int j) {
		if(getSpriteBatch() != null) {
			getSpriteBatch().getProjectionMatrix().setToOrtho2D(0, 0, i, j);
		}
		switch (gameState) {
		case MAIN_MENU:
			if (menuStage != null) {
				menuStage.setViewport(i, j, true);			
			}
			break;
		case PLAYING:
		case PAUSED:
			if (!screenStack.isEmpty()) {
				for(Screen2D s : screenStack) {
					s.resize(i, j);	
				}				
			}
			break;
		}
	}

	@Override
	public void pause() {
		settings.saveSettings();	//TODO is this good place for saving settings?
		switch (gameState) {
		case MAIN_MENU:
			if (menuStage != null) {
			
			}
			break;
		case PLAYING:
		case PAUSED:
			if (!screenStack.isEmpty()) {
				getScreen().pause();
			}
			break;
		}
	}

	@Override
	public void dispose() {
		switch (gameState) {
		case MAIN_MENU:
			if (menuStage != null) {
				menuStage.dispose();
			}
			break;
		case PLAYING:
		case PAUSED:
			if (!screenStack.isEmpty()) {
				screenStack.peek().hide();
				screenStack.peek().dispose();//TODO ??
				//TODO moze przeiterowa po wszystkich z dispose?
			}
			break;
		}
		gameTimeCounter.dispose();
	}
		
	private void performOneLoadingActionFromQueue() {
		loadables.poll().load(this);
	}

	private void renderHud() {
		if(getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal) != null) {
			getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal).draw(getSpriteBatch(),
					"FPS: " + Gdx.graphics.getFramesPerSecond(), 0,
					getCachedBitmapFont("gfx/font/estrangelo_16.fnt", FileType.Internal).getCapHeight());
		}
	}

	private void renderLoading() {
		if(isLoading && sLoading != null && getSpriteBatch() != null) {
			sLoading.draw(getSpriteBatch());
			if(bfcLoading != null) {
				bfcLoading.draw(getSpriteBatch());
			}
		}
	}

	@Override
	public void startGame() {
		menuStage.setOnStageHidden(new OnActionCompleted() {
			@Override
			public void completed(Action action) {
				setLoading(true);
				loadables.add(new Loadable() {
					@Override
					public void load(IMainFrame mf) {
						Gdx.input.setInputProcessor(null);
						clearScreenStack();
					}
				});
				loadables.add(new Loadable() {
					@Override
					public void load(IMainFrame mf) {
						GameMainScreen gms = new GameMainScreen(PlanetAttackApplication.this);
						gms.reGeneratePlanets();
						gms.randomizeStart();
						pushScreen(gms);
						gameState = GameState.PLAYING;
						setLoading(false);
					}
				});
			}
		});
		menuStage.hideStage();
	}
	
	@Override
	public void pauseGame(GameMainScreen gameMainScreen) {
		gameState = GameState.PAUSED;
		if(getScreen() != null) {
			getScreen().pause();
		}
		pauseScreen.setGameMainScreen(gameMainScreen);
		pushScreen(pauseScreen);
	}

	@Override
	public void continueGame() {
		popScreen();
		if(getScreen() != null) {
			getScreen().resume();
		}
		gameState = GameState.PLAYING;
	}

	@Override
	public void endGame() {
		gameState = GameState.MAIN_MENU;
		Gdx.input.setInputProcessor(menuStage);
		clearScreenStack();
		menuStage.showStage();
	}
		
	@Override
	public void quitApplication() {
//		dispose();
//		disposeScreenStack();
//		System.exit(0);
		//TODO zaimplementowac jakos sensownie?
	}
	
	@Override
	public void pushScreen(final Screen2D screen) {
		synchronized (screenStack) {
			gameState = GameState.PAUSED;
			screenStack.push(screen);
			Gdx.input.setInputProcessor(screen);
			if(screen instanceof SpectacularScreen2d) {
				((SpectacularScreen2d) screen).setupSpectacularInEffect();
			}
			gameState = GameState.PLAYING;
		}
	}

	@Override
	public void popScreen() {
		synchronized (screenStack) {
			if(screenStack.peek() instanceof SpectacularScreen2d) {
				//if spectacular screen - setup out effect and wait until it's finished
				//to do some screen poping stuff...
				SpectacularScreen2d ss2d = (SpectacularScreen2d) screenStack.peek();
				ss2d.setupSpectacularOutEffect();
				ss2d.addOutEffectCompletedListener(new EffectCompletedListener() {
					@Override
					public void completed(EffectCompletedEvent evt) {
						try {
							screenStack.pop();
						} catch(EmptyStackException e) {
							
						}
						if(screenStack.isEmpty()) {
							gameState = GameState.MAIN_MENU;
							Gdx.input.setInputProcessor(menuStage);
							menuStage.showStage();
						} else {
							Gdx.input.setInputProcessor(screenStack.peek());
						}
					}
				});
			} else {
				try {
					screenStack.pop();
				} catch(EmptyStackException e) {
					
				}
				if(screenStack.isEmpty()) {
					gameState = GameState.MAIN_MENU;
					Gdx.input.setInputProcessor(menuStage);
					menuStage.showStage();
				} else {
					Gdx.input.setInputProcessor(screenStack.peek());
				}
			}
		}
	}

	@Override
	public Screen2D getScreen() {
		return screenStack.isEmpty() ? null : screenStack.peek();	
	}
	
	private void clearScreenStack() {
		synchronized (screenStack) {
			screenStack.clear();
		}
	}
	
	private void disposeScreenStack() {
		synchronized (screenStack) {
			for(Screen2D s : screenStack) {
				s.dispose();
			}
			screenStack.clear();
		}
	}
	
	@Override
	public GameTimeCounter getGameTimeCounter() {
		return gameTimeCounter;
	}
	
	@Override 
	public void setGameTimeCounter(GameTimeCounter gtc) {
		if(gameTimeCounter != null) {
			gameTimeCounter.dispose();
		}
		gameTimeCounter = gtc;
	}
	
	@Override
	public Random getRandom() {
		return random;
	}
	
	@Override
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	@Override
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}
	
	@Override
	public TextureAtlas getTexturePack(String name) {
		return texturePacks.get(name);
	}

	@Override
	public Sound getCachedSound(String filePath, FileType fileType) {
		Sound s = soundCache.get(filePath);
		if(s == null) {
			s = Gdx.audio.newSound(Gdx.files.getFileHandle(filePath, fileType));
			soundCache.put(filePath, s);
		}
		return s;
	};
	
	@Override
	public void setAI(AI ai) {
		this.ai = ai;
	}

	@Override
	public AI getAI() {
		return ai;
	}

	@Override
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
		setLoadingDescription(null);
	}
	
	@Override
	public void setLoadingDescription(String desc) {
		if(bfcLoading != null && sLoading != null) {
			bfcLoading.setText(desc != null ? desc : "", sLoading.getX(), sLoading.getY() + sLoading.getHeight());
		}
	}
	
	@Override
	public Texture getCachedTexture(String filePath, FileType filetype, boolean mipmap) {
		Texture t = textureCache.get(filePath);
		if(t == null) {
			t = new Texture(Gdx.files.getFileHandle(filePath, FileType.Internal), mipmap);
			textureCache.put(filePath, t);
		}
		return t;
	}
	
	@Override
	public BitmapFont getCachedBitmapFont(String filePath, FileType filetype) {
		BitmapFont t = fontCache.get(filePath);
		if(t == null) {
			t = new BitmapFont(Gdx.files.getFileHandle(filePath, filetype), false);
			fontCache.put(filePath, t);
		}
		return t;
	}
	
	@Override
	public ParticleEffect getCachedParticleEffect(String particleFilePath, FileType filetype, String imagesDir) { 
		ParticleEffect pe = particleEffectCache.get(particleFilePath);
		if(pe == null) {
			pe = new ParticleEffect();
			pe.load(Gdx.files.getFileHandle(particleFilePath, filetype), Gdx.files.getFileHandle(imagesDir, filetype));
			particleEffectCache.put(particleFilePath, pe);
		}
		return pe;
	}
	
	@Override
	public ParticleEffect getCachedParticleEffect(String particleFilePath, FileType filetype, TextureAtlas textureAtlas) { 
		ParticleEffect pe = particleEffectCache.get(particleFilePath);
		if(pe == null) {
			pe = new ParticleEffect();
			pe.load(Gdx.files.getFileHandle(particleFilePath, filetype), textureAtlas);
			particleEffectCache.put(particleFilePath, pe);
		}
		return pe;
	}
	
	@Override
	public Settings getSettings() {
		return this.settings;
	}

	@Override
	public GameLevel getGameLevel() {
		return gameLevel;
	}

	@Override
	public void setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
	}	
}