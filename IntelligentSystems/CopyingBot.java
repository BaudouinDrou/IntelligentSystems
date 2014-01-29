import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class CopyingBot {

  /*
	 * Function that gets called every turn. This is where to implement the strategies.
	 */
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

    //(2) send ships to the biggest ennemy planet
        int maxGR = 0;
        for (Planet p : pw.EnemyPlanets()){
            if (p.GrowthRate()>maxGR && Helper.WillCapture(source,p)){  // && Helper.WillCapture(source,p)
                dest = p;
                maxGR = p.GrowthRate();
            }
        }
        if (dest == null) {
            dest = source;
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
