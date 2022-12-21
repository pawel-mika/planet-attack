package pl.wcja.game;

import java.util.EventObject;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class TickEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TickType {
		UNDEFINED,
		TICK,
		SECOND,
		OTHER
	}
	private TickType tickType = TickType.UNDEFINED;
	private long tick = 0;
	private long second = 0;
	
	public TickEvent(Object source, long tick, long second, TickType type) {
		super(source);
		this.tick = tick;
		this.second = second;
		this.tickType = type;
	}
	
//	public TickEvent(Object source, long tick, long second) {
//		super(source);
//		this.tick = tick;
//		this.second = second;
//	}

	public long getTick() {
		return tick;
	}
	
	public long getSecond() {
		return second;
	}

	public TickType getTickType() {
		return tickType;
	}
	
}