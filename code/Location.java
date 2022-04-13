/**
 * Helper class. Instances of this class represent location on a 2D grid.
 */
class Location{
		int x;
		int y;
		
		/**
		 * Constructor
		 * @param x
		 * @param y
		 */
		Location(int x, int y){
			this.x=x;
			this.y=y;
		}
		/**
		 * Returns an Euclidean distance from Location t
		 * @param t
		 * @return
		 */
		public double Euclid(Location t) {
			return Location.Euclid(this, t);
		}
		
		/**
		 * Returns a Manhattan distance from Location t
		 * @param t
		 * @return
		 */
		public int Manhattan(Location t) {
			return Location.Manhattan(this, t);
		}
		

		/**
		 * Returns an Euclidean distance between Location a and b
		 * @param a, Location
		 * @param b, Location
		 * @return
		 */
		public static double Euclid(Location a, Location b) {
			float deltaX = (a.x - b.x);
			float deltaY = (a.x - b.x);
			
			return Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY,2));
		}
		
		/**
		 * Returns a Manhattan distance between Location a and b
		 * @param a, Location
		 * @param b, Location
		 * @return
		 */
		public static int Manhattan(Location a, Location b) {
			int deltaX = Math.abs(a.x - b.x);
			int deltaY = Math.abs(a.x - b.x);
			
			return deltaX+deltaY;
		}
	}