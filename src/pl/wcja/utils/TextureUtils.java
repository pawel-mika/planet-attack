package pl.wcja.utils;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 * 
 */
public class TextureUtils {

	/**
	 * 
	 * @param file
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 */
	public static TextureRegion getTTextureRegionFromFile(String file, FileType type, int width, int height) {
		double c = Math.log(width) / Math.log(2);
		int tw = (int) Math.pow(2, Math.round(c + 0.5f));
		c = Math.log(height) / Math.log(2);
		int th = (int) Math.pow(2, Math.round(c + 0.5f));
		Pixmap p = new Pixmap(Gdx.files.getFileHandle(file, type));
		Texture t = new Texture(tw, th, p.getFormat());
		t.draw(p, 0, 0);
		t.bind();
		p.dispose();
		TextureRegion tr = new TextureRegion(t, 0, 0, width, height);
		return tr;
	}
}