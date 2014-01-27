import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.charset.Charset;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class Helper {
	
	public static boolean WillCapture(Planet s,Planet d) {	// Tested
		// Planet d = destination
		// Planet c = source
		if (s!=null&&d!=null)
			return s.NumShips()/2 > d.NumShips();
		return false;
	}
    
    public static int Dcalculation(Planet pA, Planet pB) {	// Tested
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
    
    public static SimulatedPlanetWars fakePW(){	// Tested
    	List<Planet> asw = new ArrayList<Planet>(6);
    	double x = -5;
    	double y = -10;
    	for (int i = 0; i<3; ++i){
    		Planet a = new Planet(i*2,i,3,5,x*i,y*i);
    		Planet b = new Planet(i*2+1,i,10,4,-x*i,-y*i);
    		asw.add(a);
    		asw.add(b);
    		x *= -1;
    		y *= -1;
    	}
    	return new SimulatedPlanetWars(asw);
    }

    public static double distance(Planet a,Planet b){
    	// return the square of the distance (too expensive to calculate the square root)
    	return Math.pow((a.X()-b.X()),2) + Math.pow((a.Y()-b.Y()),2);
    }

    public static int shipsValue(PlanetWars pw){
    	// return the difference between the enemy and our own number of ships in total
    	int myShips = 0;
    	int enemyShips = 0;
    	for (Planet p : pw.Planets()){
    		if(p.Owner()==1)
    			myShips += p.NumShips();
    		if(p.Owner()==2)
    			enemyShips += p.NumShips();
    	}
    	return myShips - enemyShips;
    }

    public static int growthValue(PlanetWars pw){
    	// return the difference between the enemy and our own groth rate
    	int myGrowth = 0;
    	int enemyGrowth = 0;
    	for (Planet p : pw.Planets()){
    		if(p.Owner()==1)
    			myGrowth += p.GrowthRate();
    		if(p.Owner()==2)
    			enemyGrowth += p.GrowthRate();
    	}
    	return myGrowth - enemyGrowth;
    }

    public static int fleetValue(PlanetWars pw){
    	// return the maximum fleet able to be send minus the average fleet on each planet.
    	// if aswer > 0 means that our biggest fleet is twice as big as the average number of ships on our planets.
		int maxShips = 0;
		int fleet = 0;
		for (Planet p : pw.MyPlanets()){
			if (p.NumShips() > maxShips){
				maxShips = p.NumShips();
			}
			fleet += p.NumShips();
		}
		return maxShips/2 - fleet/pw.MyPlanets().size();
    }

    public static boolean testRand(int a, int b){
    	if (a==b){
	    	if(Math.random()<0.5)	// randomly choose another difference
				++ a;
			else
				-- a;
		}
		return a<b;
    }
    
    // Testing function :
    public static void main(String[] args) {
		SimulatedPlanetWars simpw = fakePW();
		// // MyNode class tests :
		// MyNode root = new MyNode(simpw);
		// root.createSons();
		// for (MyNode node : root.getSons())
		// 	System.out.println(node);

		// // BeamsearchBot.DoTurn(simpw); => not working (cf signature of the function) :

		// int testIndex = 0;
		// Planet source = null;
		// Planet dest = null;

		// MyNode root = new MyNode(simpw);

		// ArrayList<MyNode> beam = new ArrayList<MyNode>(3);
		// beam.add(root);
		// // for (MyNode node : root.getSons())
		// // 	System.out.println("I'm a node of " + node);

		// source = root.getSource();
		// dest = root.getDest();
		// //While there is still some time, we go through the tree of possibilities
		// while(testIndex < 400){
		// 	for (int i = 0; i<beam.size();++i){
		// 		MyNode node = beam.get(i);
		// 		node.createSons();
		// 		if (!node.isLeave()){
		// 			beam.remove(i);		//When it has been treated, we take it off the list	only if it not a leave (prevent thus an empty bem array)				
		// 		}
		// 		// System.out.println("Beam size 1 : " + beam.size());
		// 		for (MyNode son : node.getSons()){
		// 			son.conditionnalAdd(5,beam);
		// 		}
		// 	}
		// 	++ testIndex;
		// }

		// // Choosing the maximum value 

		// int max = beam.get(0).getValue();
		// int index = 0;
		// for (int i = 1;i<beam.size();++i){
		// 	MyNode node = beam.get(i);
		// 	if (node.getValue()>max) {
		// 		index = i;
		// 		max = node.getValue();
		// 	}
		// }

		// // distance test
		// System.out.println(Helper.distance(simpw.Planets().get(2),simpw.Planets().get(5)));
    }
}
