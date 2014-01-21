import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class FirstparallelBot {

  /*
	 * Function that gets called every turn. This is where to implement the strategies.
	 */

  	public static List<Planet> interestingPlanets(PlanetWars pw){
        List<Planet> asw = new ArrayList<Planet>();
        for (Planet p : pw.NotMyPlanets()){
            if (p.NumShips()<p.GrowthRate()*2)	// We've got 2 turns before an eventual attack; that's why rentability is calculated that way
                asw.add(p);
        }
        for (Planet p : pw.EnemyPlanets()){
            asw.add(p);
        }
  		return asw;
  	}

	public static void DoTurn(PlanetWars pw) {
    
    //notice that a PlanetWars object called pw is passed as a parameter which you could use
    //if you want to know what this object does, then read PlanetWars.java

    //create a source planet, if you want to know what this object does, then read Planet.java
		Planet source = null;

    //create a destination planet
		Planet dest = null;

    //(1) send ships from the more powerfull planet
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

    //(2) send ships to the optimum available planet
    	List<Planet> sortedPlanets = interestingPlanets(pw);
        for (Planet p : sortedPlanets){
            if (Helper.WillCapture(source,p)){
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
