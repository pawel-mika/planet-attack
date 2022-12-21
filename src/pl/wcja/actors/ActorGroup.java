package pl.wcja.actors;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 * ee olac to narazie...
 */
public class ActorGroup extends Actor {

	public ActorGroup(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private List<Actor> actors = new LinkedList<Actor>();
	private double x = 0.0d, y = 0.0d;
	
	public void addActor(Actor actor) {
		synchronized(actors) {
			actors.add(actor);
		}
	}
	
	public void removeActor(Actor actor) {
		synchronized(actors) {
			actors.remove(actor);
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	protected boolean touchDown(float f, float f1, int i) {
		return false;
	}

	@Override
	protected boolean touchUp(float f, float f1, int i) {
		return false;
	}

	@Override
	protected boolean touchDragged(float f, float f1, int i) {
		return false;
	}

	@Override
	public Actor hit(float f, float f1) {
		return null;
	}

	@Override
	protected void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		
	}
	
}