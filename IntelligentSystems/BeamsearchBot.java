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

public class BeamsearchBot {

	public static void DoTurn(PlanetWars pw) {
		
		SimulatedPlanetWars simpw = createSimulation(pw);
		MyNode root = new MyNode(simpw);
		root.createSons();

		// This is the array containing the 3 best options
		ArrayList<MyNode> beam = new ArrayList<MyNode>();
		beam.add(root);
		Planet source = null;
		Planet dest = null;

		int iter = 0;
		while(iter < 150) {
			++ iter;
		}
		go(beam,pw);
	}

	// Analyse the situation and act belonging to the current situation
	public static void go(ArrayList<MyNode> beam, PlanetWars pw){
		MyNode max = beam.get(0);

		for (int i = 1; i<beam.size(); ++i){
			if (beam.get(i).getValue()>max.getValue()){
				max = beam.get(i);
			}
		}
		pw.IssueOrder(max.getSource(),max.getDest());
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
	
	/**
	 * Create the simulation environment. Returns a SimulatedPlanetWars instance.
	 * Call every time you want a new simulation environment.
	 * @param The original PlanetWars object
	 * @return SimulatedPlanetWars instance on which to simulate your attacks. Create a new one everytime you want to try alternative simulations.
	 */
	public static SimulatedPlanetWars createSimulation(PlanetWars pw){
		return new SimulatedPlanetWars(pw);
	}
	
	
	/**
	 * Static LookaheadBot, used only to access SimulatedPlanetWars (DON'T CHANGE)
	 */
	static LookaheadBot dummyBot = new LookaheadBot();
	
}

