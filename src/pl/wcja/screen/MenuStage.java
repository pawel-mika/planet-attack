package pl.wcja.screen;

import java.util.ArrayList;

import pl.wcja.IMainFrame;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.RotateBy;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateInterpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.DecelerateInterpolator;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class MenuStage extends Stage {
	protected IMainFrame mf = null;
	
	private Color bgColor = new Color(0.65f, 0.25f, 0.55f, 0.0f);
	private TextureRegion bgImage = null;
	
	private Button bStart = null;
	private Button bHiScore = null;
	private Button bOptions = null;
	private Button bManual = null;
	
	private Image iLogo = null;
	private Image iCopyright = null;
	
	public MenuStage(IMainFrame mf, float width, float height, boolean stretch) {
		super(width, height, stretch);
		this.mf = mf;
		initialize();
	}

	private void initialize() {
		bStart = new Button("start", mf.getTexturePack("buttons").findRegion("btn_start"), mf.getTexturePack("buttons").findRegion("btn_start_p"));
		addActor(bStart);
		bHiScore = new Button("hiscore", mf.getTexturePack("buttons").findRegion("btn_hiscore"), mf.getTexturePack("buttons").findRegion("btn_hiscore_p"));
		addActor(bHiScore);
		bOptions = new Button("options", mf.getTexturePack("buttons").findRegion("btn_options"), mf.getTexturePack("buttons").findRegion("btn_options_p"));
		addActor(bOptions);
		bManual= new Button("manual", mf.getTexturePack("buttons").findRegion("btn_manual"), mf.getTexturePack("buttons").findRegion("btn_manual_p"));
		addActor(bManual);
		repositionButtons(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		iLogo = new Image("logo", mf.getTexturePack("title").findRegion("title_logo"));
		iCopyright = new Image("credits", mf.getTexturePack("title").findRegion("title_copyright"));
		
		Texture t = mf.getCachedTexture("gfx/bg_main.jpg", FileType.Internal, true);
		t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearLinear);
		//zakladamy ze tekstura ma boki o rownych rozmiarach! np 512x512
		float prop = t.getWidth() / width;
		prop = t.getWidth() / height < prop ? t.getWidth() / height : prop;
		int nw = (int) (width * prop);
		int nh = (int) (height * prop);
		bgImage = new TextureRegion(t, 0, 0, nw, nh);
		
		iLogo.x = (width - iLogo.width) / 2;
		iLogo.y = height - iLogo.height - 8 - 60;
		addActor(iLogo);
		
		iCopyright.x = (width - iCopyright.width) / 2;
		iCopyright.y = 4;
		addActor(iCopyright);
	}

	public void showStage() {
		repositionButtons(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		iLogo.x = (width - iLogo.width) / 2;
		iLogo.y = height - iLogo.height - 16;
		iCopyright.x = (width - iCopyright.width) / 2;
		iCopyright.y = 4;
		float delay = 0.0f, yStep = Gdx.graphics.getHeight() / 2;
		for(Actor a : getRoot().getActors()) {
			a.rotation = 0;
			a.scaleX = 0.01f;
			a.scaleY = 0.01f;
			a.action(ScaleTo.$(1, 1, 1.25f + delay).setInterpolator(DecelerateInterpolator.$(2.0f)));
			a.action(RotateBy.$(360 * 2, 1.25f + delay).setInterpolator(DecelerateInterpolator.$(1.5f)));
			float road = a.y - yStep;
			a.y = yStep;
			a.action(MoveBy.$(0, road, 0.9f + delay).setInterpolator(DecelerateInterpolator.$(1.5f)));
			delay += 0.1f;
		}
	}
	
	public void hideStage() {
		float delay = 0.0f, yMiddle = Gdx.graphics.getHeight() / 2;
		for(Actor a : getRoot().getActors()) {
			a.rotation = 0;
			a.action(ScaleTo.$(0.01f, 0.01f, 1.0f + delay).setInterpolator(AccelerateInterpolator.$(2.0f)));
			a.action(RotateBy.$(-360 * 2, 1.25f + delay).setInterpolator(AccelerateInterpolator.$(1.5f)));
			float road = yMiddle - a.y;
			if(stageHiddenListener != null && getRoot().getActors().lastIndexOf(a) == getRoot().getActors().size() - 1) {
				a.action(MoveBy.$(0, road, 0.9f + delay).setInterpolator(AccelerateInterpolator.$(1.5f)).setCompletionListener(stageHiddenListener));	
			} else {
				a.action(MoveBy.$(0, road, 0.9f + delay).setInterpolator(AccelerateInterpolator.$(1.5f)));	
			}			
			delay += 0.1f;
		}
	}
	
	private OnActionCompleted stageHiddenListener = null;
	
	public void setOnStageHidden(OnActionCompleted listener) {
		this.stageHiddenListener = listener;
	}
	
	@Override
	public void draw() {
		renderBackground();
		super.draw();
	}

	@Override
	public void setViewport(float width, float height, boolean strech) {
		super.setViewport(width, height, false);
		repositionButtons((int)width, (int)height);
	}

	private void repositionButtons(int screenWidth, int screenHeight) {
		//liczymy batony i ich rozmiary aby ³adnie wyposrodkowac...
		ArrayList<Button> buttons = new ArrayList<Button>();
		float margin = 4;
		float bw = 0, bh = 0;
		for(Actor a : getRoot().getActors()) {
			if(a instanceof Button) {
				buttons.add((Button)a);
				bw = bw < a.width ? a.width : bw;
				bh += a.height + margin;
			}
		}
		float bx = (screenWidth - bw) / 2;
		float by = (screenHeight / 2) + ((bh / 2) - (bh / buttons.size()));
		for(Button b : buttons) {
			b.x = bx;
			b.y = by;
			by -= b.height + margin;
		}
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int newParam) {
		boolean touched = super.touchUp(x, y, pointer, newParam);
		if (touched) {
			Actor ltc = getLastTouchedChild();
			if (ltc == bOptions) {
				OptionsScreen os = new OptionsScreen(mf);
				mf.pushScreen(os);
			} else if (ltc == bHiScore) {
				HiScoreScreen hsc = new HiScoreScreen(mf);
				mf.pushScreen(hsc);
			} else if (ltc == bStart) {
				mf.startGame();
			} else if (ltc == bManual) {
//				ManualScreen ms = new ManualScreen(mf);
				//test
				SpectacularManual ms = new SpectacularManual(mf);
				mf.pushScreen(ms);
			}
		}
		getRoot().focus(null, pointer);
		return touched;
	}
	
	private void renderBackground() {
		if(bgColor != null) {
			Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
		if(bgImage != null) {
			batch.begin();
			batch.draw(bgImage, 0, 0, width, height);
			batch.end();
		}
	}
}