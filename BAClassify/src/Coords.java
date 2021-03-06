import java.lang.Math;

public abstract class Coords {
	/**Instance is a coordinate triple in 3-space
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * x: |
	 * y: | --> Coordinates in 3 space
	 * Z: |
	 * ------------ Methods --------------------
	 * Getters and Setters for coordinates (getX, setX)
	 * equals: tells whether coordinates are equal or not
	 * distanceTo: gives Euclidean distance to a point in 3-space
	 **/
	
	//Instance fields
	int x; //-200 - 200
	int y; //-200 - 200
	int z; //-200 - 200
	
	//Getters
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getZ(){
		return this.z;
	}
	
	//Setters
	public void setX(int x){
		assert x <= 200;
		assert x >= -200;
		
		this.x = x;
	}
	
	public void setY(int y){
		assert y <= 200;
		assert y >= -200;
		
		this.y = y;
	}
	
	public void setZ(int z){
		assert z <= 200;
		assert z >= -200;
		
		this.z = z;
	}
	
	public Coords(int x, int y, int z){
		/** Constructor for Coords class
		 * @param x: x-coordinate of point 
		 * @param y: y-coordinate of point
		 * @param z: z-coordinate of point
		 * Note: Please check that x, y, and z are valid coordinates in 
		 * 3-space to ensure distanceTo works properly
		 **/
		
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public String toString(){
		/**Override java.lang.Object.toString
		 * Returns: Triple representation of coordinates
		 * Example: Coords(22,-24,77) --> (22, -24, 77)
		 **/
		String triple = "(" + Integer.toString(this.getX()) + ", " 
							+ Integer.toString(this.getY()) + ", "
							+ Integer.toString(this.getZ()) + ")";
		return triple;
	}
	
	public boolean equals(Object other){
		/**Override java.lang.Object.equals
		 * Returns: True if other has same coordinates,
		 * false otherwise.
		 * @param other: Object for comparison
		 * Note: Due to implementation standards, other
		 * need not be of type Coords, but equals will always return false 
		 * if type mismatch is found.
		 **/
		
		//Check if other is null or not the same class
		if((other == null) | (this.getClass() != other.getClass())){
			return false;
		}
		//Check if coordinates the same
		if(this.getX() != ((Coords)other).getX()){
			return false;
		}
		if(this.getY() != ((Coords)other).getY()){
			return false;
		}
		if(this.getZ() != ((Coords)other).getZ()){
			return false;
		}
		
		//If all coordinates the same, return true
		return true;
	}
	
	public float distanceTo(Coords P2){
		/**Returns: Euclidean distance from this cluster to cluster2
		 * @param cluster2: another point in 3-space
		 **/
		int Dx = this.getX()-P2.getX();
		int Dy = this.getY()-P2.getY();
		int Dz = this.getZ()-P2.getZ();
		double Dsquared = Math.pow(Dx, 2) + Math.pow(Dy, 2) + Math.pow(Dz, 2);
		return (float) Math.sqrt(Dsquared);
	}
	
}

