package pl.wcja;

/**
 * Interfejs do tworzenia akcji �aduj�cych r�ne zasoby.
 * Kolejkowane w kolejce FIFO (Queue) i wywo�ywane w g��wnej p�tli renderuj�cej
 * po jednej akcji na kazde wejscie do render().
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public abstract class Loadable {
	
	public abstract void load(IMainFrame mf);
		
}
