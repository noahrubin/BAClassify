import java.util.HashMap;
import java.util.Map;

public class Comp extends Coords{
	/**Instance is comparable object for KNN algorithm
	 * Represents a cluster with peak coordinate
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * coordinates x, y, z: peak coordinate of cluster in 3-space
	 * search: HashMap of structure {anatomical label : BA label}
	 * ------------ Methods --------------------
	 * Getters and Setters for coordinates
	 * equals: see Coords class
	 * distanceTo: see Coords class 
	 **/
	
	//Instance fields
	Map<String, String> search;
	
	public Comp(int x, int y, int z){
		/** Constructor for Comp object
		 * See Coords documentation for coordinate spec
		 * and note on x, y, and z coordinates
		 */
		super(x, y, z);
		search = new HashMap<String, String>();
	}

}