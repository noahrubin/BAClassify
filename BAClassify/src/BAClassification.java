import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BAClassification extends BADatabaseAccess {
	
	/**Class for predicting BA classification of Anatomical Labels
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * database: see BADatabaseAccess
	 * ------------ Methods --------------------
	 * buildDatabase: see BADatabaseAccess 
	 * writeToDatabase: see BADatabaseAccess
	 * calculateNeighbors: helper method for classify
	 * classify: classifies BA labels given set of clusters with anatomical labels
	 */
	
	protected static Comp[] calculateNeighbors(Comp cluster, int K){
		//Array of 3 closest neighbors
		Comp[] neighbors = new Comp[K];
		
		//Compare cluster to each database entry
		for (int i = 0; i < database.size(); i++){
			//Set bin, check each cluster in bin
			ArrayList<Comp> bin = database.get(i);
			for (int j = 0; j < bin.size(); j++){
				//Calculate distance to cluster
				float distance = cluster.distanceTo(bin.get(j));
				//Insert into neighbors if distance less than any current neighbor
				//or if neighbors isn't full yet
				for (int k = 0; k < K; k++){
					if (distance < cluster.distanceTo(neighbors[k]) | neighbors[k] == null){
						neighbors[k] = bin.get(j);
						break;
					}
				}
			}
		}
		//return list of closest neighbors
		return neighbors;
	}
	
	/*protected static Map<String, int> findChoices(Comp cluster, Comp[] neighbors, int size){
	}*/

	public static void classify(String dbase_path, ArrayList<Comp> input, int K) throws FileNotFoundException, IOException {
		//Building the database for comparison
		try {
			buildDatabase(dbase_path);
		} catch (FileNotFoundException e) {
			System.out.println("Database file not found, please check file path");
			e.printStackTrace();
		} catch (IOException o){
			System.out.println("Input/Output issue, check stack trace");
			o.printStackTrace();
		}
		
		//options for cluster: in database already, not in database
		//	either way, still calculate neighbors
		//options for each anatomical label: at least one match in neighbors, no match in neighbors
		//	if no match in neighbors, leave blank
		//	else, predict the BA
		//options for match in neighbors: one match, more than one match that don't agree, all matches agree
		//	if one match, fill in that match
		//	if more than one match but not all agree, choose highest frequency match or first match
		//	else, fill in the match
		
		//For each cluster in input array
		for (int i = 0; i < input.size(); i++){
			Comp cluster = input.get(i);  //change from Comp to Cluster when get class from Alex
			Comp[] neighbors = calculateNeighbors(cluster, K);
			//if neighbors is empty, leave BAs labels blank
			if (neighbors.length < 1){
				break;
			}
			//Boolean same = true;
			for (String aal : cluster.search.keySet()){
				//String[] BA = new String[K]
				//int j = 0;
				for (Comp entry : neighbors){
					String suggBA = entry.search.get(aal);
					if (suggBA != null){
						/*BA[j] = entry.search.get(aal);
						j++;*/
						cluster.search.put(aal, suggBA);
						break;
					}
				}
				/*String base = BA[0];
				for (String label : BA){
					if (label != base){
						same = false;
						cluster.search.get(aal) = BA[0];
					}
				}*/
			}
			
			
		}
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
