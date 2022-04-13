/**
 * Instances of this class represent nodes in the tree mapping the search space.
 */
public class GameStateNode {
	
	//private values
	private GameState gs;
	private GameStateNode parent;
	private GameStateNode[] children;
	private int numberOfChildren;
	private Double distance;
	private int depth;
	private HeuristicMethod heuristic;
	private GameState lastTargetGS;
	
	/**
	 * Private constructor.
	 * Initiliases the internal values.
	 */
	private GameStateNode(){
		parent = null;
		distance = null;
		depth = 0;
		heuristic = (GameState g, GameState target) -> 0;
		lastTargetGS = null;
		children = new GameStateNode[4];
		numberOfChildren = -1;
	}
	
	/**
	 * Simple public constructor.
	 * @param gs, the Game state contained.
	 */
	GameStateNode(GameState gs){
		this();
		
		this.gs = gs;
	}
	/**
	 * Private partial constructor.
	 * @param parent
	 * @param gs
	 */
	private GameStateNode(GameStateNode parent, GameState gs) {
		this(gs);
		this.parent = parent;
		depth = parent.getSteps()+1;
	}
	/**
	 * Private partial constructor.
	 * @param gs
	 * @param heuristic
	 */
	private GameStateNode(GameState gs, HeuristicMethod heuristic) {
		this(gs);
		this.heuristic = heuristic;
	}
	
	/**
	 * Simple public constructor. Used for the root of the tree.
	 * @param gs, the Game state contained.
	 * @param target, the GameState the tree is searching for.
	 * The trivial heuristic is going to be used.
	 */
	GameStateNode(GameState gs, GameState target){
		this(gs);
		getDistance(target);
	}
	/**
	 * Complex root node constructor.
	 * @param gs, the starting game state.
	 * @param target, the target game state.
	 * @param heuristic, HeuristicMethod to be used for the informed search seeking through the tree.
	 */
	GameStateNode(GameState gs, GameState target, HeuristicMethod heuristic){
		this(gs, heuristic);
		getDistance(target);
	}
	/**
	 * Child node generator.
	 * @param parent
	 * @param gs
	 * @param heuristic
	 */
	GameStateNode(GameStateNode parent, GameState gs, HeuristicMethod heuristic) {
		this(parent, gs);
		this.heuristic = heuristic;
	}
	/**
	 * Private method.
	 * Calculates the distance value of this node.
	 * @param target, the Target to be compared to.
	 */
	private void measure(GameState target) {
		distance = (double) 0;
		distance += heuristic.compare(gs, target);
		distance += depth;
	}
	
	/**
	 * Public method that makes the node create all viable children.
	 * @return int representing the number of the children created.
	 */
	public int makeChildren() {
		numberOfChildren = 0;
		GameState childGS;
		
		try {
			childGS = gs.clone();
			childGS.moveDown();
			children[numberOfChildren] = new GameStateNode(this, childGS, heuristic);
			numberOfChildren++;
		} catch(CannotExecuteException e) {
			
		}
		try {
			childGS = gs.clone();
			childGS.moveLeft();
			children[numberOfChildren] = new GameStateNode(this, childGS, heuristic);
			numberOfChildren++;
		} catch(CannotExecuteException e) {
			
		}
		try {
			childGS = gs.clone();
			childGS.moveRight();
			children[numberOfChildren] = new GameStateNode(this, childGS, heuristic);
			numberOfChildren++;
		} catch(CannotExecuteException e) {
			
		}
		try {
			childGS = gs.clone();
			childGS.moveUp();
			children[numberOfChildren] = new GameStateNode(this, childGS, heuristic);
			numberOfChildren++;
		} catch(CannotExecuteException e) {
			
		}
		return numberOfChildren;
	}

	/**
	 * Calculate the distance to a specific target.
	 * @param target, GameState target to measure distance against.
	 * @return, double the distance from the target.
	 */
	public double getDistance(GameState target) {
		if (!target.equals(lastTargetGS)) {
			this.measure(target);
			lastTargetGS = target;
		}
		return distance;
	}
	/**
	 * Get distance against the parent's target, or the last target submitted.
	 * @return
	 */
	public double getDistance() {
		if(parent==null) {
			return distance;
		}
		if(lastTargetGS == null) {
			this.measure(parent.lastTargetGS);
			lastTargetGS = parent.lastTargetGS;
		}
		if(!lastTargetGS.equals(parent.lastTargetGS)) {
			this.measure(parent.lastTargetGS);
			lastTargetGS = parent.lastTargetGS;
		}
		return distance;
	}
	/**
	 * @return int representing the node's depth.
	 */
	public int getSteps() {
		return depth;
	}
	
	/**
	 * Get node's parent.
	 * @return GameStateNode parent of this node.
	 */
	public GameStateNode getParent() {
		return parent;
	}
	/**
	 * Get a child with a specific index.
	 * @param i, index of the child to be returned.
	 * @return, a child, return null if the child does not exist.
	 */
	public GameStateNode getChild(int i) {
		if(numberOfChildren==-1) makeChildren();
		if(i<0) return null;
		if(i>=numberOfChildren) {
			return null;
		} else {
			return children[i];
		}
	}
	public String toString() {
		return gs.toString();
	}
	/**
	 * Checks whether the node has a parent.
	 * @return
	 */
	public boolean hasParent() {
		if(parent==null) {
			return false;
			
		} else {
			return true;
		}
	}

	/**
	 * Checks whether the node's GameState configuration is identical to the last target's configuration.
	 * @return
	 */
	public boolean isSolution() {
		if(gs.equals(lastTargetGS)) {
			return true;
		}
		return false;
	}
}
