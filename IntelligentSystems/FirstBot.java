import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class FirstBot {

  /*
	 * Function that gets called every turn. This is where to implement the strategies.
	 */


  	public static boolean willCapture(Planet s,Planet d) {
  		// Planet d = destination
  		// Planet c = source
  		return s.NumShips()/2 > d.NumShips();
  	}

  	public static List<Planet> interestingPlanets(PlanetWars pw){
  		return pw.EnemyPlanets();
  	}


	public static void DoTurn(PlanetWars pw) {
    
    //notice that a PlanetWars object called pw is passed as a parameter which you could use
    //if you want to know what this object does, then read PlanetWars.java

    //create a source planet, if you want to know what this object does, then read Planet.java
		Planet source = null;

    //create a destination planet
		Planet dest = null;

    //(1) implement an algorithm to determine the source planet to send your ships from
    //Here, we choose the planet with the maximum of ships to send the fleet (not the best strategy)
    int maxShips = 0;
    for (Planet p : pw.MyPlanets()){
        if (p.NumShips() > maxShips){
            maxShips = p.NumShips();
            source = p;
        }
    }
    if (source==null){
        source = pw.MyPlanets().get(0);
    }

    //(2) implement an algorithm to deterimen the destination planet to send your ships to
    //Here, we'll pick the first available (=capturable) planet in the list of interesting planets
    List<Planet> sortedPlanets = interestingPlanets(pw);
    for (Planet p : sortedPlanets){
        if (willCapture(source,p)){
            dest = p;
            break;
        }
    }
    if (dest==null){
        dest = sortedPlanets.get(0);
    }

		//(3) Attack
		if (source != null && dest != null) {
			pw.IssueOrder(source, dest);
		}
		
	}

	public static void main(String[] args) {
		String line = "";
		String message = "";
		int c;
		try {
			while ((c = System.in.read()) >= 0) {
				switch (c) {
				case '\n':
					if (line.equals("go")) {
						PlanetWars pw = new PlanetWars(message);
						DoTurn(pw);
						pw.FinishTurn();
						message = "";
					} else {
						message += line + "\n";
					}
					line = "";
					break;
				default:
					line += (char) c;
					break;
				}
			}
		} catch (Exception e) {

		}
	}
}
