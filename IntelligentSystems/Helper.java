import java.util.*;

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
    
    // Testing function :
    public static void main(String[] args) {
    	SimulatedPlanetWars simpw = fakePW();
        // MyNode root = new MyNode(simpw);
        // root.createSons();
        // for (MyNode node : root.getSons())
        // 	System.out.println(node);

        // BeamsearchBot.DoTurn(simpw); => not working (cf signature of the function) :

  		int testIndex = 0;
		Planet source = null;
		Planet dest = null;

		MyNode root = new MyNode(simpw);

		ArrayList<MyNode> beam = new ArrayList<MyNode>(3);
		beam.add(root);
		// for (MyNode node : root.getSons())
		// 	System.out.println("I'm a node of " + node);

		source = root.getSource();
		dest = root.getDest();
		//While there is still some time, we go through the tree of possibilities
		while(testIndex < 100){
			for (int i = 0; i<beam.size();++i){
				MyNode node = beam.get(i);
				beam.remove(i);		//When it has been treated, we take it off the list
				node.createSons();
				System.out.println(node);
				for (MyNode son : node.getSons()){
					son.conditionnalAdd(3,beam);
				}
			}
			++ testIndex;
		}

		// Choosing the maximum value 

		int max = beam.get(0).getValue();
		int index = 0;
		for (int i = 1;i<beam.size();++i){
			MyNode node = beam.get(i);
			if (node.getValue()>max) {
				index = i;
				max = node.getValue();
			}
		}
    }
}
