import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/* A bot which adapts its behaviour according to the environment characteristics.
 * It changes its strategy, based on the current environment (e.g. number of neutral planets in the map,
 * number of ships, etc.). Knowing which strategy to use has to be collected beforehand.
 * This requires running a number of games of your bots, and evaluate which bot performs best for a certain environment.
 * You should then add this to the data structure (in AdaptivityMap.java). 
 * The DoTurn method can then query this data structure to know what strategy should be used for this turn. 
 * This example provides two environment variables: the number of neutral planets on the map, and the average growth
 * ratio of these neutral planets.
 * 
 * We provide a possible implementation using the hash adaptivityMap, which maps lists of integers (representing 
 * the environment) with names of bots. See AdaptivityMap.java
 * 
 * Interesting questions (you can probably come up with other questions yourself as well):
 * 1. Can you modify or extend the environment variables we use? Maybe other things are interesting other than the number of neutral planets, and the average planet size of these neutral planets.
 * 2. The table in AdaptivityMap.java is filled by us (randomly) with only two simple bots. But how should the table really look like? 
 * This means you should collect data on how all your previous bots (BullyBot, RandomBot, HillclimbingBot, LookaheadBot and/or others) perform in different environments
 * 3. Can you implement your other bot implementations in AdaptiveBot.java? Currently the only strategies are BullyBot ('DoBullyBotTurn') and RandomBot ('DoRandomBotTurn').
 * Implement the bot strategies you used to fill AdaptivityMap.java here as well.
 */

public class SimMyAdaptiveBot {
	
	/**
	 * The main method for issuing your commands. Here, the best strategy is selected depending on the environment characteristics
	 * @param simpw
	 */
	public static void DoTurn(SimulatedPlanetWars simpw) {
				
		//Retrieve environment characteristics
		//Are there characteristics you want to use instead, or are there more you'd like to use? Try it out!
		// int neutralPlanets = pw.NeutralPlanets().size();
		// int totalPlanetSize = 0;
		// for (Planet p : pw.NeutralPlanets()) {
		// 	totalPlanetSize += p.GrowthRate();
		// }
		// int averagePlanetSize = Math.round(totalPlanetSize/pw.NeutralPlanets().size());
		
		int planets = simpw.MyPlanets().size() - simpw.EnemyPlanets().size();	// myPlanets - EnemyPlanets => positive mean that I have more planets than the enemy
		int ships = Helper.shipsValue(simpw);								// myShips - enemyShips
		int growth = Helper.growthValue(simpw);							// myGrowth - enemyGrowth		
		int fleet = Helper.fleetValue(simpw);								// The maximum fleet able to be send minus the average fleet on each planet.

		int planetsL = 0;	// L is for learn (to modify the if depending on victories or defeats)
		int shipsL = 0;
		int growthL = 0;		
		int fleetL = 0;

		try {	// Try to read values from the file
			BufferedReader reader = new BufferedReader(new FileReader("learn.txt"));
			String line = null;

			line = reader.readLine();
			planetsL = Integer.valueOf(line);
			line = reader.readLine();
			shipsL = Integer.valueOf(line);
			line = reader.readLine();
			growthL = Integer.valueOf(line);
			line = reader.readLine();
			fleetL = Integer.valueOf(line);
		}
		catch (Exception e){
			// File must not exist or being inconsistent so we create it
			try {
				PrintWriter writer = new PrintWriter("learn.txt", "UTF-8");
				writer.println(0);
				writer.println(0);
				writer.println(0);
				writer.println(0);
				writer.close();
			}
			catch (Exception ee){
				System.out.println(ee);
			}
			// Finally throw an exception : 
			System.out.println(e);
		}

		// Tree selection :
		if (Helper.testRand(planets,planetsL))	{
			// Learn values are at 0 by default, but can be changed when stored
			// Here if planets < planetsL (= 0 if nothing learned yet)
			// In case of equal the value is choosen randomly
			if (Helper.testRand(ships,shipsL))
				defend(simpw);
			else
				attack(simpw,1);	// 1 is for "planet attack"
		}
		else {
			if (Helper.testRand(growth,growthL)){
				if (Helper.testRand(fleet,fleetL))
					defend(simpw);
				else
					attack(simpw,0);	// 0 is for "GR attack"
			}
			else {
				if (Helper.testRand(ships,shipsL)){
					if (Helper.testRand(fleet,fleetL))
						defend(simpw);
					else
						attack(simpw,2);	// 2 is for destroying the ennemy ships to prevent him to conquer any of our planets
				}
				else
					attack(simpw,3);	// 3 is for an attack that makes an overall winning (Dcalculation + distance)
			}
		}
	}
	
	public static void attack(SimulatedPlanetWars simpw, int objective) {	// Objective is 0 GR, 1 planet, 2 ships, 3 Dcal + distance
		Planet source = null;
		Planet dest = null;
		List<Planet> asw;

		switch (objective){
			case 0 : asw = forGR(simpw);
				break;
			case 1 : asw = forPlanet(simpw);
				break;
			case 2 : asw = forShips(simpw);
				break;
			default : asw = forDcalcDist(simpw);
				break;
		}
		source = asw.get(0);
		dest = asw.get(1);

		if (source != null && dest != null) {
			simpw.IssueOrder(source, dest);
		}

		updateLearning(objective);
	}

	// Those four next functions are build to generate a source and a destination for a special kind of attack

	public static ArrayList<Planet> forGR(SimulatedPlanetWars simpw){
		ArrayList<Planet> asw = new ArrayList<Planet>(2);
		// Find the biggest fleet to attack with
		int maxShips = 0;
		for (Planet p : simpw.MyPlanets()){
			if (p.NumShips() > maxShips){
				maxShips = p.NumShips();
				asw.add(0,p);
			}
		}
		// Find the destination with best growthRate to be captured
		int maxGR = 0;
		for (Planet p : simpw.NotMyPlanets()){
			if (p.GrowthRate() > maxGR && Helper.WillCapture(asw.get(0),p)) {
				maxGR = p.GrowthRate();
				asw.add(1,p);
			}
		}

		return asw;
	}

	public static ArrayList<Planet> forPlanet(SimulatedPlanetWars simpw){
		ArrayList<Planet> asw = new ArrayList<Planet>(2);
		// Find the biggest fleet to attack with 
		int maxShips = 0;
		for (Planet p : simpw.MyPlanets()){
			if (p.NumShips() > maxShips){
				maxShips = p.NumShips();
				asw.add(0,p);
			}
		}
		// Find the destination with best distance to be captured (distance help to be sure to capture the planet on neutral ones)
		int maxDist = 0;
		for (Planet p : simpw.NotMyPlanets()){
			int dist = (int) Helper.distance(asw.get(0),p);
			if (dist > maxDist && Helper.WillCapture(asw.get(0),p)) {
				maxDist = dist;
				asw.add(1,p);
			}
		}
		return asw;
	}

	public static ArrayList<Planet> forShips(SimulatedPlanetWars simpw){
		ArrayList<Planet> asw = new ArrayList<Planet>(2);
		// Find the biggest fleet to attack with and our weakest planet to know what is our goal of fleet to be destroyed in the enemey
		int maxShips = 0;
		int minShips = 1000;
		for (Planet p : simpw.MyPlanets()){
			int ships = p.NumShips();
			if (ships > maxShips){
				maxShips = ships;
				asw.add(0,p);
			}
			if (ships< minShips)
				minShips = ships;
		}
		// Find the destination with a dangerous fleet
		int maxDist = 0;
		maxShips = 0;
		for (Planet p : simpw.NotMyPlanets()){
			int ships = p.NumShips();
			if ((ships/2)>minShips && ships>maxShips) {
				// We choose the biggest Enemy to attack to defend more our planets
				maxShips = ships;
			}
		}
		return asw;
	}

	public static ArrayList<Planet> forDcalcDist(SimulatedPlanetWars simpw){
		ArrayList<Planet> asw = new ArrayList<Planet>(2);

		// Find the best couple source/destination relying on Dcalculation + distance (our heurisctic function)
		int heurisctic = -1024;
		for (Planet s : simpw.MyPlanets()) {
			for (Planet d : simpw.NotMyPlanets()){
				int dist = (int) Helper.distance(s,d);
				int dCalc = Helper.Dcalculation(s,d);
				if (dist + dCalc > heurisctic) {
					heurisctic = dist + dCalc;
					asw.add(0,s);
					asw.add(1,d);
				}
			}
		}
		return asw;
	}

	public static void defend(SimulatedPlanetWars simpw) {	//send ships from the lowest growing planet to the best one
		// Tested
		int maxGR = 0;
		int minGR = 5;
		Planet source = null;
		Planet dest = null;
		for (Planet p : simpw.MyPlanets()){
			if (p.GrowthRate()>maxGR){
				maxGR = p.GrowthRate();
				dest = p;
			}
			if (p.GrowthRate()<minGR){
				minGR = p.GrowthRate();
				source = p;
			}
		}

		if (source != null && dest != null) {
			simpw.IssueOrder(source, dest);
		}

		updateLearning(-1);
	}

	public static void updateLearning(int action){
		try {	// Try to read values from the file
			BufferedReader reader = new BufferedReader(new FileReader("learn.txt"));
			String line = null;

			line = reader.readLine();
			int planetsL = Integer.valueOf(line);
			line = reader.readLine();
			int shipsL = Integer.valueOf(line);
			line = reader.readLine();
			int growthL = Integer.valueOf(line);
			line = reader.readLine();
			int fleetL = Integer.valueOf(line);

			switch (action){
				case -1 : --planetsL;
					break;
				case 0 : --growthL;
					break;
				case 1 : ++planetsL;
					break;
				case 2 : --shipsL;
					break;
				case 3 : ++fleetL;
					break;
				default : break;
			}
			try {
				PrintWriter writer = new PrintWriter("learn.txt", "UTF-8");
				writer.println(planetsL);
				writer.println(shipsL);
				writer.println(growthL);
				writer.println(fleetL);
				writer.close();
			}
			catch (Exception ee){
				System.out.println(ee);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
