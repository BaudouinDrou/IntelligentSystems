import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/** Another smarter kind of bot, which implements a minimax algorithm with look-ahead of two turns.
 * It simulates the opponent using the BullyBot strategy and simulates the possible outcomes for any
 * choice of source and destination planets in the attack. The simulated outcome states are ranked by
 * the evaluation function, which returns the most promising one.
 * 
 * Try to improve this bot. For example, you can try to answer some of this questions. 
 * Can you come up with smarter heuristics/scores for the evaluation function? 
 * What happens if you run this bot against your bot from week1? 
 * How can you change this bot to beat your week1 bot? 
 * Can you extend the bot to look ahead more than two turns? How many turns do you want to look ahead?
 * Is there a smart way to make this more efficient?
 */

public class LookaheadBot {

	public static void DoTurn(PlanetWars pw) {
		
		double score = Double.MIN_VALUE;		
		Planet source = null;
		Planet dest = null;
	
		
		// We try to simulate each possible action and its outcome after two turns
		// considering each of my planets as a possible source 
		// and each enemy planet as a possible destination
		for (Planet myPlanet: pw.MyPlanets()){
			
			//avoid planets with only one ship
			if (myPlanet.NumShips() <= 1)
				continue;		
			
			for (Planet notMyPlanet: pw.NotMyPlanets()){

				// Create simulation environment - need to create one for each simulation
				SimulatedPlanetWars simpw = new SimulatedPlanetWars(pw);
				
				// (1) simulate my turn with the current couple of source and destination
				simpw.simulateAttack(myPlanet, notMyPlanet);
				// (2) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();

				// (3) simulate the opponent's turn, assuming that the opponent is the BullyBot		
				//     here you can add other opponents
				simpw.simulateBullyBotAttack();
				// (4) simulate the growth of ships that happens in each turn
				simpw.simulateGrowth();
			
				
				// (5) evaluate how the current simulated state is
				//     here you can change how a state is evaluated as good
				double scoreMax = evaluateState(simpw);
				
				// (6) find the planet with the maximum evaluated score
				//     this is the most promising future state
				if (scoreMax > score) {					
					score = scoreMax;
					source = myPlanet;
					dest = notMyPlanet;
					
				}
				
			}
		}
		
		
			
		// Attack using the source and destinations that lead to the most promising state in the simulation
		if (source != null && dest != null) {
			pw.IssueOrder(source, dest);
		}
		

	}
	
	
	/**
	 * This function evaluates how promising a simulated state is.
	 * You can change it to anything that makes sense, using combinations 
	 * of number of planets, ships or growth rate.
	 * @param SimulatedPlanetWars pw
	 * @return score of the final state of the simulation
	 */
	public static double evaluateState(SimulatedPlanetWars pw){
		
		// CHANGE HERE
		
		double enemyShips = 1.0;
		double myShips = 1.0;
		
		for (Planet planet: pw.EnemyPlanets()){
			enemyShips += planet.NumShips();
		}
		
		for (Planet planet: pw.MyPlanets()){
			myShips += planet.NumShips();
		}
		
		return myShips/enemyShips;
	}
	

	
	// don't change this
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
