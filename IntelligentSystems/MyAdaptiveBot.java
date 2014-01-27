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

public class MyAdaptiveBot {
	
	/**
	 * The main method for issuing your commands. Here, the best strategy is selected depending on the environment characteristics
	 * @param pw
	 */
	public static void DoTurn(PlanetWars pw) {
				
		//Retrieve environment characteristics
		//Are there characteristics you want to use instead, or are there more you'd like to use? Try it out!
		// int neutralPlanets = pw.NeutralPlanets().size();
		// int totalPlanetSize = 0;
		// for (Planet p : pw.NeutralPlanets()) {
		// 	totalPlanetSize += p.GrowthRate();
		// }
		// int averagePlanetSize = Math.round(totalPlanetSize/pw.NeutralPlanets().size());
		
		int planets = pw.MyPlanets().size() - pw.EnemyPlanets().size();	// myPlanets - EnemyPlanets => positive mean that I have more planets than the enemy
		int ships = Helper.shipsValue(pw);								// myShips - enemyShips
		int growth = Helper.growthValue(pw);							// myGrowth - enemyGrowth		
		int fleet = Helper.fleetValue(pw);								// The maximum fleet able to be send minus the average fleet on each planet.

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
				defend(pw);
			else
				attack(pw);
		}
		else {
			if (Helper.testRand(growth,growthL)){
				if (Helper.testRand(fleet,fleetL))
					defend(pw);
				else
					attack(pw);
			}
			else {
				if (Helper.testRand(ships,shipsL)){
					if (Helper.testRand(fleet,fleetL))
						defend(pw);
					else
						attack(pw);
				}
				else
					attack(pw);
			}
		}
	}
	
	public static void attack(PlanetWars pw) {
		Planet source = null;
		Planet dest = null;

		// Planet pGR = attackForGR();
		// Planet pDist = attackForDcalcDist();
		// Planet pShips = attackForShips();
		// Planet pEnemy = attackOnEnemy();

		source = pw.MyPlanets().get(0);
		dest = pw.NotMyPlanets().get(0);

		if (source != null && dest != null) {
			pw.IssueOrder(source, dest);
		}
	}

	public static void defend(PlanetWars pw) {	//send ships from the lowest growing planet to the best one
		int maxGR = 0;
		int minGR = 5;
		Planet source = null;
		Planet dest = null;
		for (Planet p : pw.MyPlanets()){
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
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String stackTrace = writer.toString();
			System.err.println(stackTrace);
			System.exit(1); //just stop now. we've got a problem
		}
	}
}
