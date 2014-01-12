import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class HillclimbingBot {

  /*
	 * Function that gets called every turn. This is where to implement the strategies.
	 */
	 
	public static int Dcalculation(Planet pA, Planet pB) {
		int hisLoss = 0;
		int myLoss = 0;
		int hisGrowth = 0;
		int myGrowth = 0;
		if (Helper.WillCapture(pA,pB)){
			/* if A capture B */
			myLoss = pB.NumShips();
			myGrowth = pB.GrowthRate();
			if (pB.Owner()!=0) {
				/* if it is an enemy planet (not neutral)*/
				hisLoss = pB.NumShips();
				hisGrowth = - pB.GrowthRate();
			}
		}
		else {
			/* if A do not win against B */
			myLoss = pA.NumShips();
			if (pB.Owner()!=0)
				hisLoss = pA.NumShips();
			myGrowth = 0;
			hisGrowth = 0;
		}
		return hisLoss - myLoss + myGrowth - hisGrowth;
	}

	public static void DoTurn(PlanetWars pw) {
    
    //notice that a PlanetWars object called pw is passed as a parameter which you could use
    //if you want to know what this object does, then read PlanetWars.java

    //create a source planet, if you want to know what this object does, then read Planet.java
		Planet source = null;

    //create a destination planet
		Planet dest = null;
		int minD = Integer.MIN_VALUE;
		for (Planet pA : pw.MyPlanets()) {
			for (Planet pB : pw.NotMyPlanets()) {
				if (Dcalculation(pA,pB)>minD) {
					source = pA;
					dest = pB;
				}
			}
		}
	
    //(1) implement an algorithm to determine the source planet to send your ships from
    //... code here

    //(2) implement an algorithm to deterimen the destination planet to send your ships to
    //... code here

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
