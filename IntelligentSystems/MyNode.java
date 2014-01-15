// This class is made to get a structure for our nodes in our trees

public class MyNode {
	private MyNode father;
	private List<MyNode> sons;

	private int value;
	private Planet source;
	private Planet dest;

	public MyNode(MyNode father; List<MyNode> sons; int value; Planet source; Planet dest){
		this.father = father;
		this.sons = sons;
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

	public voide setDest(Planet dest){
		this.dest = dest;
	}

	
}