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
    
    public static int Dcalculation(Planet pA, Planet pB) {
        // The next values are only relatives value to the global values of grothrates and number of ships
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
			myLoss = pA.NumShips()/2;
			if (pB.Owner()!=0)
				hisLoss = pA.NumShips()/2;
			myGrowth = 0;
			hisGrowth = 0;
		}
		return hisLoss - myLoss + myGrowth - hisGrowth;
	}
    
    public static SimulatedPlanetWars fakePW(){
    	List<Planet> asw = new ArrayList<Planet>(6);
    	for (int i = 0; i<3; ++i){
    		Planet a = new Planet(i*2,i,3,5);
    		Planet b = new Planet(i*2+1,i,10,4);
    		asw.add(a);
    		asw.add(b);
    	}
    	return new SimulatedPlanetWars(asw);
    }
    
    public static void main(String[] args) {
    	SimulatedPlanetWars simpw = Helper.fakePW();
    }
	
}
