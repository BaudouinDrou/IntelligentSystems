import java.util.*;

/*
 RandomBot - an example bot that picks up one of his planets and send half of the ships 
 from that planet to a random target planet.

 Not a very clever bot, but showcases the functions that can be used.
 Overcommented for educational purposes.
 */
public class BeamsearchBot {

  /*
	 * Function that gets called every turn. This is where to implement the strategies.
	 */

	public static void DoTurn(PlanetWars pw) {
		// long endingTime = System.currentTimeMillis() + 5;
		int testIndex = 0;
		Planet source = null;
		Planet dest = null;

		MyNode root = new MyNode(new SimulatedPlanetWars(pw));

		ArrayList<MyNode> beam = new ArrayList<MyNode>(3);
		beam.add(root);
		// for (MyNode node : root.getSons())
		// 	System.out.println("I'm a node of " + node);

		source = root.getSource();
		dest = root.getDest();
		//While there is still some time, we go through the tree of possibilities
		while(testIndex < 10){
			for (int i = 0; i<beam.size();++i){	// Cannot do (MyNode node : beam) because beam is modified with time going on
				MyNode node = beam.get(i);
				node.createSons();
				if (!node.isLeave()){
					beam.remove(i);		//When it has been treated, we take it off the list	only if it not a leave (prevent thus an empty bem array)				
				}
				// System.out.println("Beam size 1 : " + beam.size());
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

		// Choosing destination and source from the maximum value of D found in the beam array+
		source = beam.get(index).getSource();
		dest = beam.get(index).getDest();

		// (3) Attack!
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
