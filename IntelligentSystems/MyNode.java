import java.util.*;

// This class is made to get a structure for our nodes in our trees

public class MyNode {
	private MyNode father;
	private ArrayList<MyNode> sons;
	private SimulatedPlanetWars simpw;

	private int value;
	private Planet source;
	private Planet dest;

	public MyNode(SimulatedPlanetWars simpw){	//Usefull to define the root
		this.father = null;
		this.sons = new ArrayList<MyNode>();
		this.simpw = simpw;
		this.value = 0;
		this.source = null;
		this.dest = null;
	}

	public MyNode(MyNode father, SimulatedPlanetWars simpw, int value, Planet source, Planet dest){	//Usefull to instanciate the sons of the current node
		this.father = father;
		this.sons = new ArrayList<MyNode>();
		this.simpw = simpw;
		this.value = value;
		this.source = source;
		this.dest = dest;
	}

	public MyNode getFather(){
		return this.father;
	}

	public ArrayList<MyNode> getSons(){
		return this.sons;
	}

	public SimulatedPlanetWars getSim(){
		return this.simpw;
	}

	public int getValue(){
		return this.value;
	}

	public Planet getSource(){
		return this.source;
	}

	public Planet getDest(){
		return this.dest;
	}

	public void setSons(ArrayList<MyNode> sons){
		this.sons = sons;
	}

	public void setValue(int value){
		this.value = value;
	}

	public void setSource(Planet source){
		this.source = source;
	}

	public void setDest(Planet dest){
		this.dest = dest;
	}

	// Other get/set function, in case of
	public void addSon(MyNode son){
		this.sons.add(son);
	}

	public boolean isRoot(){
		return father==null;
	}

	public boolean isLeave(){
		return sons.isEmpty();
	}

	// Add each possible state in the currentNode.sons list.
	public void createSons(){
		for (Planet myPlanet: this.getSim().MyPlanets()){
			
			//avoid planets with only one ship
			if (myPlanet.NumShips() <= 1)
				continue;
			
			// We create a son for each of the possible situations
			for (Planet notMyPlanet: this.getSim().NotMyPlanets()){

				// Create simulation environment for this son
				SimulatedPlanetWars simpw2 = new SimulatedPlanetWars(simpw);
				int value = Helper.Dcalculation(myPlanet, notMyPlanet);
				simpw2.IssueOrder(myPlanet, notMyPlanet);
				simpw2.simulateGrowth();
				simpw2.simulateFirstBotAttack();
				simpw2.simulateGrowth();

				MyNode son;
				if (this.getFather().isRoot()) {
					son = new MyNode(this, simpw2, value, myPlanet, notMyPlanet);
				}
				else {
					son = new MyNode(this, simpw2, value, this.getSource(), this.getDest());	// We only need to know from where to where we want to send our ships to get the best turn
				}
				this.addSon(son);
			}
		}
	}

	public void conditionnalAdd(int n, ArrayList<MyNode> list){
		if (list.size()<n)
			list.add(this);
		else {
			int min = list.get(0).getValue();
			int index = 0;
			for (int i = 0;i<list.size();++i){
				MyNode node = list.get(i);
				if (node.getValue()<min) {
					index = i;
					min = node.getValue();
				}
			}
			if (this.getValue()>min) {
				list.remove(index);
				list.add(this);
			}
		}
	}
}