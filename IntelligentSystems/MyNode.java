// This class is made to get a structure for our nodes in our trees

public class MyNode {
	private MyNode father;
	private List<MyNode> sons;
	private SimulatedPlanetWars simpw;

	private int value;
	private Planet source;
	private Planet dest;

	public MyNode(SimulatedPlanetWars simpw){	//Usefull to define the root
		this.father = null;
		this.sons = new List<MyNode>();
		this.simpw = simpw;
		this.value = 0;
		this.source = null;
		this.dest = null;
	}

	public MyNode(MyNode father, SimulatedPlanetWars simpw, int value, Planet source, Planet dest){	//Usefull to instanciate the sons of the current node
		this.father = father;
		this.sons = new List<MyNode>();
		this.simpw = simpw;
		this.value = value;
		this.source = source;
		this.dest = dest;
	}

	public MyNode getFather(){
		return this.father;
	}

	public List<MyNode> getSons(){
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

	public void setSons(List<MyNode> sons){
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
				SimulatedPlanetWars simpw2 = createSimulation(simpw);
				int value = Helper.Dcalculation(myPlanet, notMyPlanet);
				simpw2.issueOrder(myPlanet, notMyPlanet);
				simpw2.simulateGroth();
				simpw2.simulateFirstBotAttack();
				simpw2.simulateGroth();

				if (this.getFather().isRoot()) {
					MyNode son = new MyNode(this, simpw2, value, myPlanet, notMyPlanet);
				}
				else {
					MyNode son = new MyNode(this, simpw2, value, this.getSource(), this.getDest());	// We only need to know from where to where we want to sen our ships to get the best turn
				}
				this.addSon(son);
			}
		}
	}
}