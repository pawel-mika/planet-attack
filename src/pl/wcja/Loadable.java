package pl.wcja;

/**
 * Interfejs do tworzenia akcji ³aduj¹cych ró¿ne zasoby.
 * Kolejkowane w kolejce FIFO (Queue) i wywo³ywane w g³ównej pêtli renderuj¹cej
 * po jednej akcji na kazde wejscie do render().
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public abstract class Loadable {
	
	public abstract void load(IMainFrame mf);
		
}
