package pl.wcja.g2d;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.wcja.IMainFrame;
import pl.wcja.utils.IntersectUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Simple 2D screen implementation.
 * This clas DOES NOT implement:
 * - rendering of a sprites (touchable, draggable etc);
 * - interaction with sprites (however it will inform subclass of a sprite touched, dragged etc);
 * You MUST implement it in you custom, desired way. 
 * 
 * TODO finish implementation of draggableSprites!!
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Screen2D implements Screen, InputProcessor {

	protected IMainFrame mf = null;
	protected OrthographicCamera camera = null;
	private Color bgColor = new Color(0.0f, 0.0f, 0.5f, 0.5f);
	private Texture bgTexture = null;
	private TextureRegion bgImage = null;
	private ExtSprite touchedDownSprite = null;
	private boolean isDragging = false;
	private int dragSourceX = 0, dragSourceY = 0, dragTargetX = 0, dragTargetY = 0;
	private int lastPointer = 0;
	private ArrayList<ExtSprite> touchableSprites = new ArrayList<ExtSprite>();
	private ArrayList<ExtSprite> draggableSprites = new ArrayList<ExtSprite>();//TODO to be implemented?
	private ExtSprite doubleTouchFirstSprite = null;
	private long lastTouchTime = 0;
	private int doubleTouchLen = 500; //500ms for doubletap!

	public Screen2D(IMainFrame mf) {
		this.mf = mf;
		enableMeshAntialias();
//		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		mf.getSpriteBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	}
	
	public void addTouchable(ExtSprite s) {
		touchableSprites.add(s);
	}
		
	public void removeTouchable(ExtSprite s) {
		synchronized (touchableSprites) {
			touchableSprites.remove(s);
		}
	}
	
	public void clearTouchables() {
		synchronized (touchableSprites) {
			touchableSprites.clear();
		}
	}	

	public void addDraggable(ExtSprite s) {
		draggableSprites.add(s);
	}
		
	public void removeDraggable(ExtSprite s) {
		synchronized (draggableSprites) {
			draggableSprites.remove(s);
		}
	}
	
	public void clearDraggable() {
		synchronized (draggableSprites) {
			draggableSprites.clear();
		}
	}
	
	/**
	 * this render is called between begin() and end()!!
	 */
	@Override
	public void render(float f) {
		renderBackground();
	}

	@Override
	public void resize(int i, int j) {
		if (camera != null) {
			camera = new OrthographicCamera(i, j);
		}
		fitTextureToBackground();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		mf.getGameTimeCounter().pauseCounter(true);
	}

	@Override
	public void resume() {
		mf.getGameTimeCounter().pauseCounter(false);
	}

	@Override
	public void dispose() {
		for(ExtSprite es : touchableSprites) {
			es.getTexture().dispose();
		}
		if(bgImage != null ) {
			bgImage.getTexture().dispose();
		}
	}

	protected void renderBackground() {
		if (bgImage == null) {
			Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		} else if (bgImage != null) {
//			mf.getSpriteBatch().begin();
			mf.getSpriteBatch().draw(bgImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//			mf.getSpriteBatch().end();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK) {
			backKeyPressed();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		y = Gdx.graphics.getHeight() - y;
		dragSourceX = x;
		dragSourceY = y;
//		System.out.println(String.format("Testing point %s, %s in %s touchables", x, y, touchableSprites.size()));
		for (ExtSprite s : touchableSprites) {
			if (IntersectUtils.pointInRectangle(x, y, s.getRectangle())) {
				touchedDownSprite = s;
				lastPointer = pointer; 
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		y = Gdx.graphics.getHeight() - y;
		if (lastPointer == pointer) {
			for (ExtSprite s : touchableSprites) {
				if (IntersectUtils.pointInRectangle(x, y, s.getRectangle())) {
					//if touched up the same planet like touched down
					if(touchedDownSprite == s) {
						long tapTime = Calendar.getInstance().getTimeInMillis() - lastTouchTime;
						lastTouchTime = Calendar.getInstance().getTimeInMillis();
						if(tapTime <= doubleTouchLen && doubleTouchFirstSprite == s) {
							spriteDoubleTouched(s);
							doubleTouchFirstSprite = null;
							touchedDownSprite = null;
							isDragging = false;
							return true;
						} else {
							spriteTouched(s);
							doubleTouchFirstSprite = s;
							touchedDownSprite = null;
							isDragging = false;
							return true;
						}
					} else if(touchedDownSprite != null && touchedDownSprite != s) {
					//else there was a drag to another planet...
						if (isDragging) {
							touchDragged(touchedDownSprite, s);
							touchedDownSprite = null;
							isDragging = false;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(lastPointer == pointer && touchedDownSprite != null) {
			dragTargetX = x;
			dragTargetY = Gdx.graphics.getHeight() - y;
			touchDragged(dragSourceX, dragSourceY, dragTargetX, dragTargetY);
			isDragging = true;
			return true;
		}
		return false;
	}
	
	protected boolean isDragging() {
		return this.isDragging;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	/**
	 * Touched (touched down and then touched up) a sprite
	 * @param s
	 */
	protected void spriteTouched(ExtSprite s) {
	}
	
	/**
	 * Double touched the sprite
	 * @param s
	 */
	protected void spriteDoubleTouched(ExtSprite s) {
	}
	
	/**
	 * Dragged (touched down and then dragged) a sprite
	 * @param s
	 */
	protected void spriteDragged(ExtSprite s) {
	}
	
	/**
	 * Dragged a touch from one sprite to another (without dragging a sprite) 
	 * @param sFrom
	 * @param sTo
	 */
	protected void touchDragged(ExtSprite sFrom, ExtSprite sTo) {		
	}
	
	/**
	 * Touch dragged from a point x1,y1 to point x2,y2
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	protected void touchDragged(int x1, int y1, int x2, int y2) {
	}
	
	/**
	 * 
	 */
	public void backKeyPressed() {
		mf.popScreen();
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setBackgroundColor(Color color) {
		this.bgColor = color;
	}

	/**
	 * 
	 * @return
	 */
	public TextureRegion getBackgroudImage() {
		return bgImage;
	}
	
	/**
	 * Sets the fixedsize background
	 * @param bgImage
	 */
	public void setBackgroundImage(TextureRegion bgImage) {
		if(bgImage == null) {
			this.bgImage.getTexture().dispose();
		}
		this.bgImage = bgImage;
	}
	
	/**
	 * Sets the background from texture automatically
	 * resizing it to fit screen if screen size exceeds the
	 * texture size.
	 * 
	 * @param t
	 */
	public void setBackgroundImage(Texture t) {
		this.bgTexture = t;
		fitTextureToBackground();
	}
	
	/**
	 * 
	 */
	private void fitTextureToBackground() {
		if (bgTexture != null) {
			int w = Gdx.graphics.getWidth();
			int h = Gdx.graphics.getHeight();
			// zakladamy ze tekstura ma boki o rownych rozmiarach! np 512x512
			float prop = (float) bgTexture.getWidth() / w;
			prop = (float) bgTexture.getWidth() / h < prop ? (float) bgTexture.getWidth() / h : prop;
			int nw = (int) (w * prop);
			int nh = (int) (h * prop);
			bgImage = new TextureRegion(bgTexture, 0, 0, nw, nh);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ExtSprite> getTouchableSprites() {
		return touchableSprites;
	}
	
	public void enableMeshAntialias() {
		Gdx.gl10.glEnable (GL10.GL_LINE_SMOOTH);
		Gdx.gl10.glEnable (GL10.GL_BLEND);
		Gdx.gl10.glBlendFunc (GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl10.glHint (GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
	}
	
	public void disableMeshAntialias() {
		
	}
	
	/**
	 * Draw a line between 2 points
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param width
	 * @param color
	 */
	protected void drawLine(float x1, float y1, float x2, float y2, float width, Color color) {
		mf.getSpriteBatch().end();
		Mesh lineMesh = new Mesh(true, 2, 0, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Color, 4, "a_color"));
		lineMesh.setVertices(new float[] { x1, y1, 0, color.r, color.g, color.b, color.a, x2, y2, 0, color.r, color.g, color.b, color.a });
//		Gdx.gl10.glLineWidth(width);//czemu to tak spowalnia?
		lineMesh.render(GL10.GL_LINES, 0, 2);
		lineMesh.dispose();
		mf.getSpriteBatch().begin();
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param width
	 * @param color
	 */
	public void drawLine(Vector2 p1, Vector2 p2, float width, Color color) {
		drawLine(p1.x, p1.y, p2.x, p2.y, width, color);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @param lineWidth
	 * @param subdivisions
	 * @param color
	 */
	public void drawCircle(float x, float y, float r, float lineWidth, int subdivisions, Color color) {
		if(subdivisions < 5) {
			subdivisions = 5;
		} else if(subdivisions > 360) {
			subdivisions = 360;
		}
		mf.getSpriteBatch().end();
		Mesh lineMesh = new Mesh(true, subdivisions + 1, 0, new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Color, 4, "a_color"));
		float[] vert = new float[7 * (subdivisions + 1)];
		int c = 0;
		for (int i = 0; i <= 360; i += (360 / subdivisions)) {
			vert[c] = x + (float) Math.sin(i * Math.PI / 180) * r;
			vert[c + 1] = y + (float) Math.cos(i * Math.PI / 180) * r;
			vert[c + 2] = 0.0f;
			vert[c + 3] = color.r;
			vert[c + 4] = color.g;
			vert[c + 5] = color.b;
			vert[c + 6] = color.a;
			c += 7;
		}
		lineMesh.setVertices(vert);
		Gdx.gl10.glLineWidth(lineWidth);
		lineMesh.render(GL10.GL_LINE_LOOP, 0, subdivisions);
		lineMesh.dispose();
		mf.getSpriteBatch().begin();
	}
}