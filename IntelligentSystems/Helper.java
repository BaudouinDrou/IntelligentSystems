import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class Helper {
	
	public static boolean WillCapture(Planet s,Planet d) {
		// Planet d = destination
		// Planet c = source
		return s.NumShips()/2 > d.NumShips();
	}
	
	public static void futureFunction() {
	
	}
	
}
