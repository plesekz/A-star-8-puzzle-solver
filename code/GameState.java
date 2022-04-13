/**
 * Instances of this class represent a specific puzzle configuration.
 * The configuration can be changed by method .move[Direction]() executed on the instance.
 */
public class GameState {
	
	/**
	 * tiles[x][y]
	 *  y
	 * 2 | 1 2 3
	 * 1 | 4 5 6
	 * 0 | 7 8 0
	 *      
	 *   * - - - x
	 *     0 1 2
	 * 0 represents the blank tile
	 */
	private int tiles[][];
	
	/**
	 * Private Location object describing current location of the blank tile.
	 */
	private Location blank_loc;
	
	/**
	 * Constructor.
	 * @param tiles, int[][] initial puzzle configuration.
	 */
	GameState(int[][] tiles){
		this.tiles = tiles.clone();
		
		searching:
		for(int i = 0; i<tiles.length;i++) {
			for(int j = 0; j<tiles[i].length; j++) {
				if (tiles[i][j] == 0) {
					blank_loc = new Location(i,j);
					break searching;
				}
			}
		}
	}
	/**
	 * Move the blank tile down.
	 * @throws CannotExecuteException if the tile cannot move down.
	 */
	void moveDown() throws CannotExecuteException {
		if (blank_loc.y == 0) throw new CannotExecuteException("Goes off the map");
		
		tiles[blank_loc.x][blank_loc.y] = tiles[blank_loc.x][blank_loc.y-1];
		tiles[blank_loc.x][blank_loc.y-1] = 0; 
		blank_loc.y--;
	}
	/**
	 * Move the blank tile up.
	 * @throws CannotExecuteException if the tile cannot move up.
	 */
	void moveUp() throws CannotExecuteException {
		if (blank_loc.y+1 == tiles[blank_loc.x].length) throw new CannotExecuteException("Goes off the map");
		
		tiles[blank_loc.x][blank_loc.y] = tiles[blank_loc.x][blank_loc.y+1];
		tiles[blank_loc.x][blank_loc.y+1] = 0;
		blank_loc.y++;
	}
	/**
	 * Move the blank tile left.
	 * @throws CannotExecuteException if the tile cannot move left.
	 */
	void moveLeft() throws CannotExecuteException {
		if (blank_loc.x == 0) throw new CannotExecuteException("Goes off the map");
		
		tiles[blank_loc.x][blank_loc.y] = tiles[blank_loc.x-1][blank_loc.y];
		tiles[blank_loc.x-1][blank_loc.y] = 0;
		blank_loc.x--;
	}
	/**
	 * Move the blank tile right.
	 * @throws CannotExecuteException if the tile cannot move right.
	 */
	void moveRight() throws CannotExecuteException {
		if (blank_loc.x+1 == tiles.length) throw new CannotExecuteException("Goes off the map");
		
		tiles[blank_loc.x][blank_loc.y] = tiles[blank_loc.x+1][blank_loc.y];
		tiles[blank_loc.x+1][blank_loc.y] = 0;
		blank_loc.x++;		
	}
	
	/**
	 * Return a pointer to the tiles representing the internal configuration.
	 * @return int[][]
	 */
	public int[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Get location a tile with a matching ID.
	 * @param t, tiles ID
	 * @return location of the tile. If the tile is not withing the puzzle the location(-1,-1) is returned.
	 */
	public Location getTileLocation(int t) {
		for(int i = 0; i<tiles.length; i++) {
			for(int j = 0; j<tiles[i].length; j++) {
				if(tiles[i][j]==t) return new Location(i,j);
			}
		}
		return new Location(-1,-1);
	}
	
	//overriden methods
	
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		int[][] oTiles = ((GameState) obj).getTiles();
		
		if(tiles.length != oTiles.length) return false;
		
		for(int i = 0; i<tiles.length;i++) {
			if(tiles[i].length != oTiles[i].length) return false;
			for(int j = 0; j<tiles[i].length; j++) {
				if (tiles[i][j] != oTiles[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		
		sb.append("\t[");
		sb.append(tiles[0][2]);
		sb.append(",");
		sb.append(tiles[1][2]);
		sb.append(",");
		sb.append(tiles[2][2]);
		sb.append("],\n");
		
		sb.append("\t[");
		sb.append(tiles[0][1]);
		sb.append(",");
		sb.append(tiles[1][1]);
		sb.append(",");
		sb.append(tiles[2][1]);
		sb.append("],\n");
		
		sb.append("\t[");
		sb.append(tiles[0][0]);
		sb.append(",");
		sb.append(tiles[1][0]);
		sb.append(",");
		sb.append(tiles[2][0]);
		sb.append("]\n");
		
		sb.append("]");
		
		return sb.toString();
	}
	
	public GameState clone() {
		int[][] newtiles = new int[tiles.length][tiles[0].length];
		for(int i = 0; i< tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				newtiles[i][j] = tiles[i][j];
			}
		}
		return new GameState(newtiles);
	}
}
