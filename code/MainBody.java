import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;

/**
 * An instances of this class represent an attempt to solve a specific 8-puzzle.
 * Can be ran as a thread.
 */
public class MainBody implements Runnable {

	// for running
	
	/**
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Internal class for binding an Heuristic function with a name.
		 *
		 */
		class Approach {
			HeuristicMethod hm;
			String name;
			
			Approach (HeuristicMethod hm, String name){
				this.hm = hm;
				this.name = name;
			}
		}
		
		/* original one presented in the CA
		int[][] state = { 
				{8,5,7},
				{3,0,2},
				{1,6,4}
				};
		*/
		
		int[][] state = {
				{8,5,7},
				{3,0,2},
				{1,6,4}
				};
		
		// get the initial puzzle configuration
		int arg = -1;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-s"))
				arg = i;
		}
		if (arg == -1) {
			System.out.println("You can specify an initial state by the -s tag followed by a series of numbers starting with the first line, reading from left to right.");
		} else {
			String s = args[arg+1];
			
			//first column, up from bottom
			state[0][0] = getIntInString(s,6);
			state[0][1] = getIntInString(s,3);
			state[0][2] = getIntInString(s,0);
			
			//second column, up from bottom
			state[1][0] = getIntInString(s,7);
			state[1][1] = getIntInString(s,4);
			state[1][2] = getIntInString(s,1);
			
			//third column, up from bottom
			state[2][0] = getIntInString(s,8);
			state[2][1] = getIntInString(s,5);
			state[2][2] = getIntInString(s,2);
		}	
		
		int[][] targetState = {
				{6,3,0},
				{7,4,1},
				{8,5,2}
		};
		
		// get a target configuration
		arg = -1;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t"))
				arg = i;
		}
		if (arg == -1) {
			System.out.println("You can specify a target state by the -t tag followed by a series of numbers starting with the first line, reading from left to right.");
		} else {
			String s = args[arg+1];
			
			//first column, up from bottom
			targetState[0][0] = getIntInString(s,6);
			targetState[0][1] = getIntInString(s,3);
			targetState[0][2] = getIntInString(s,0);
			
			//second column, up from bottom
			targetState[1][0] = getIntInString(s,7);
			targetState[1][1] = getIntInString(s,4);
			targetState[1][2] = getIntInString(s,1);
			
			//third column, up from bottom
			targetState[2][0] = getIntInString(s,8);
			targetState[2][1] = getIntInString(s,5);
			targetState[2][2] = getIntInString(s,2);
		}
		
		GameState initialGS = new GameState(state);
		GameState target = new GameState(targetState);
		
		// get a depth limit
		int depth_limit = 0;
		arg = -1;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-d"))
				arg = i;
		}
		if (arg == -1) {
			System.out.println("You can specify a depth limit with -d tag by a number. Default = 14.");
			depth_limit = 14;
		} else {
			try {
				depth_limit = Integer.parseInt(args[arg+1]);
			} catch(NumberFormatException e) {
				int tm = arg+1;
				System.err.println("Argument number "+tm+" \""+args[arg+1]+"\" specified with the -t tag is not a number.");
				System.exit(1);
			}
		}
		
		// get methods to be used to solve the puzzle
		arg = -1;		
		boolean triv = false; //1
		boolean EcfZ = false; //2
		boolean MhfZ = false; //4
		boolean EcfA = false; //8
		boolean MhfA = false; //16
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-h"))
				arg = i;
		}
		if (arg == -1) {
			System.out.println("You can specify a heuristic to be used with the -h tag followed by a numer."
					+ "\nStart with 0."
					+ "\nAdd 1 to the number to use trivial f(n)=0 heuristic."
					+ "\nAdd 2 to use Euclidean distance of the empty tile as a heuristic."
					+ "\nAdd 4 to use Manhattan distance of the empty tile as a heuristic."
					+ "\nAdd 8 to use Euclidean distance of all tiles as a heuristic."
					+ "\nAdd 16 to use Manhattan distance of all tiles as a heuristic."
					+ "\n");
			MhfA = true;
		} else {
			int N = 0;
			try {
				 N = Integer.parseInt(args[arg+1]);
			} catch(NumberFormatException e) {
				int tm = arg+1;
				System.err.println("Argument number "+tm+" \""+args[arg+1]+"\" specified with the -h tag is not a number.");
				System.exit(1);
			}
			if(N>=16) {
				MhfA = true;
				N=N-16;
			}
			if(N>=8) {
				EcfA = true;
				N=N-8;
			}
			if(N>=4) {
				MhfZ = true;
				N=N-4;
			}
			if(N>=2) {
				EcfZ = true;
				N=N-2;
			}
			if(N>=1) {
				triv = true;
				N=N-1;
			}
		}
		
		LinkedList<Approach> apprs = new LinkedList<Approach>();
		if(triv) {
			final HeuristicMethod triv_tmp = (GameState g, GameState t) -> {
				return 0;
				};
			apprs.add(new Approach(triv_tmp, "Trivial f()=0 solution"));
		}
		if(EcfZ) {
			final HeuristicMethod EcfZ_tmp = (GameState g, GameState t) -> {
				return g.getTileLocation(0).Euclid(t.getTileLocation(0));
				};
			apprs.add(new Approach(EcfZ_tmp, "Euclid for tile 0"));
		}
		
		if(MhfZ) {
			final HeuristicMethod MhfZ_tmp = (GameState g, GameState t) -> {
				return g.getTileLocation(0).Manhattan(t.getTileLocation(0));
				};
			apprs.add(new Approach(MhfZ_tmp, "Manhattan for tile 0"));
		}
		
		if(EcfA) {
			final HeuristicMethod EcfA_tmp = (GameState g, GameState t) -> {
			Double d = (double) 0;
			for(int i = 0; i<8; i++)
				d+=g.getTileLocation(i).Euclid(t.getTileLocation(i));
			return d;
			};
			apprs.add(new Approach(EcfA_tmp, "Euclid for all tiles"));
		}
		if(MhfA) {
			final HeuristicMethod MhfA_tmp = (GameState g, GameState t) -> {
				Double d = (double) 0;
					for(int i = 0; i<8; i++)
						d+=g.getTileLocation(i).Manhattan(t.getTileLocation(i));
				return d;
				};
			apprs.add(new Approach(MhfA_tmp, "Manhattan for all tiles"));
		}
		
		// creating instances of this class
		
		LinkedList<MainBody> mbs = new LinkedList<MainBody>();
		GameStateNode start;
		
		for(Approach appr:apprs) {
			 start = new GameStateNode(initialGS, target, appr.hm);
			 mbs.add(new MainBody(start, appr.name, depth_limit));
		}
		
		// starting threads
		
		LinkedList<Thread> threads = new LinkedList<Thread>();
		for(MainBody mb:mbs) {
			Thread th = new Thread(mb);
			th.start();
			threads.add(th);
		}
		
		// waiting for all threads to finish
		
		try {
			for(Thread th:threads) {
				th.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Interrupted before finishing.");
			System.exit(1);
		}
		
		// reporting on each instance
		
		for(MainBody mb:mbs) {
			makeReport(mb);
		}
	}
	
	/**
	 * Internal helper method.
	 * @param s: string
	 * @param i: index
	 * @return String a single character in a String format at a given index
	 */
	private static int getIntInString(String s, int i) {
		return Integer.parseInt(s.substring(i,i+1));
	}
	
	/**
	 * Internal helper method for reporting on an instance.
	 * @param mb
	 */
	private static void makeReport(MainBody mb) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(Instant.now());
		sb.append("]\n");
		sb.append(mb.getName());
		
		GameStateNode currentNode = mb.getSolution();
		if (currentNode == null) {
			sb.append(" failed to find a solution in ");
			sb.append(mb.getDuration().toString());
			sb.append(".\n");
		} else {
			sb.append(":\n");
			sb.append("Duration: ");
			sb.append(mb.getDuration().toString());
			sb.append("\n");
			
			Stack<GameStateNode> q = new Stack<GameStateNode>();
			q.add(currentNode);
			while(currentNode.hasParent()) {
				currentNode = currentNode.getParent();
				q.add(currentNode);
			}	
			for(int i = 0; 0 < q.size(); i++) {
				sb.append(i);
				sb.append("\n");
				sb.append(q.pop().toString());
				sb.append("\n");
			}
		}
		try {
			FileWriter out = new FileWriter(mb.getName()+".log");
			out.append(sb.toString());
			out.close();
		} catch (IOException e) {
			System.out.println("Couldn't write to a file. Dumping to console:\n");
			System.out.println(sb.toString());
		}
	}
	
	// The dynamic part of the function
	
	private PriorityQueue<GameStateNode> q;
	private GameStateNode solution;
	private String name;
	private int depthLimit;
	private Instant startTime;
	private Duration d;
	
	/**
	 * Constructor.
	 * @param initialState, the initial state of the puzzle.
	 * Note that if you create multiple instances, the initial state serves as a root of the tree.
	 * @param name
	 */
	MainBody(GameStateNode initialState, String name){
		Comparator<GameStateNode> c = (GameStateNode g, GameStateNode h)->{
				Double d = g.getDistance()-h.getDistance();
				if(d > 0.1) return 1;
				if(d < -0.1) return -1;
				return 0;
			};
		q = new PriorityQueue<GameStateNode>(c);
		q.add(initialState);
		this.name = name;
		solution = null;
		depthLimit = 21;
	}
	/**
	 * More elaborate constructor allowing to set a depth limit.
	 * @param initialState
	 * @param name
	 * @param depthLimit
	 */
	MainBody(GameStateNode initialState, String name, int depthLimit){
		this(initialState, name);
		this.depthLimit = depthLimit;
	}
	
	public void run(){
		startTime = Instant.now();
		int depthWorkedOn = 0;
		int depthLoweredTimes = 0;
		System.out.println(""+ Instant.now().toString() +":\n\tThread "+name+" started.");
		GameStateNode gsn;
		int nOfChildren;
		while(!(q.peek().isSolution())) {
			gsn = q.poll();
			if(gsn.getSteps()<depthLimit) {
				nOfChildren = gsn.makeChildren();
				for(int i = 0; i<nOfChildren;i++) {
					q.add(gsn.getChild(i));
				}
			}
			int currentDepth = gsn.getSteps();
			if(currentDepth>depthWorkedOn) {
				System.out.println(""+ Instant.now().toString() +":\n\tThread "+name+" reached depth "+currentDepth+".");
				depthWorkedOn = currentDepth;
			}
			if(currentDepth<depthWorkedOn) {
				depthLoweredTimes++;
				if(depthLoweredTimes==4) {
					System.out.println(""+ Instant.now().toString() +":\n\tThread "+name+" returned to depth "+currentDepth+".");
					depthLoweredTimes=0;
					depthWorkedOn=currentDepth;
				}
			}
			if(q.isEmpty()) break;
		}
		d = Duration.between(startTime, Instant.now());
		System.out.println(""+ Instant.now().toString() +":\n\tThread "+name+" finished.");
		solution = q.poll();
	}
	
	/**
	 * @return a node of the tree identical to the target state.
	 */
	GameStateNode getSolution(){
		return solution;
	}
	/**
	 * Get the instances name.
	 * @return
	 */
	String getName() {
		return name;
	}
	/**
	 * Get the duration for which the instance ran.
	 * @return
	 */
	Duration getDuration() {
		return d;
	}
}
