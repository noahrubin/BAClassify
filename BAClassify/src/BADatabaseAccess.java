import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;

public abstract class BADatabaseAccess {
	
	/**Class required for accessing or modifying BA Database
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * database: Database of cluster peaks and associated aal:BA lists
	 * ------------ Methods --------------------
	 * binLogic: calculates proper bin for given cluster
	 * buildDatabase: builds ArrayList database from text file 
	 * writeToDatabase: writes database information to text file
	 * Note --> data in text file must be stored in form:
	 * x-coord y-coord z-coord label BA label BA ... label BA
	 */
	
	public static ArrayList<ArrayList<Comp>> database = new ArrayList<ArrayList<Comp>>();
	
	protected static int binLogic(Comp cluster){
		/**
		 * Returns: Proper bin in database for cluster
		 * @param cluster: cluster in 3 space
		 * bin 0: x > 0, y > 0, z > 0
		 * bin 1: x > 0, y > 0, z < 0
		 * bin 2: x > 0, y < 0, z > 0
		 * bin 3: x > 0, y < 0, z < 0
		 * bin 4: x < 0, y < 0, z < 0
		 * bin 5: x < 0, y < 0, z > 0
		 * bin 6: x < 0, y > 0, z < 0
		 * bin 7: x < 0, y > 0, z > 0
		 * Precondition: cluster is Comp object with valid coordinates
		 */
		if (cluster.getX() >= 0){
			if (cluster.getY() >= 0){
				if (cluster.getZ() >= 0){
					return 0;
				}
				else {
					return 1;
				}
			}
			else {
				if (cluster.getZ() >= 0){
					return 2;
				}
				else {
					return 3;
				}
			}
		}
		else {
			if (cluster.getY() < 0){
				if (cluster.getZ() < 0){
					return 4;
				}
				else {
					return 5;
				}
			}
			else {
				if (cluster.getZ() < 0){
					return 6;
				}
				else {
					return 7;
				}
			}
		}
	}
	
	public static void buildDatabase(String file_path) throws FileNotFoundException, IOException{
		/**Procedure: Builds database with 8 bins from text file
		 * See binLogic for bin information\
		 * @param file_path: file path for text file database
		 * Precondition: file_path end point is text file with data in form:
		 * x y z label1 BA label2 BA ... LabelN BA
		 * **If data in text file breaks this format, build will fail**
		 */
		
		//Construct 8 bins of database
		database.add(new ArrayList<Comp>()); //bin 0
		database.add(new ArrayList<Comp>()); //bin 1
		database.add(new ArrayList<Comp>()); //bin 2
		database.add(new ArrayList<Comp>()); //bin 3
		database.add(new ArrayList<Comp>()); //bin 4
		database.add(new ArrayList<Comp>()); //bin 5
		database.add(new ArrayList<Comp>()); //bin 6
		database.add(new ArrayList<Comp>()); //bin 7
		
		//Fetch file from path, insert into Scanner
		FileInputStream fis = new FileInputStream(file_path);
		Scanner scanFile = new Scanner(fis);
		
		//Set delimiter for split
		String delim  = " ";
		
		//While data to be parsed
		while (scanFile.hasNextLine()){
			
			//Items in line separated to array
			String line = scanFile.nextLine();
			String[] info = line.split(delim);
			
			//Create new cluster, set coordinates
			Comp cluster = new Comp(Integer.parseInt(info[0]), 
						   Integer.parseInt(info[1]), 
						   Integer.parseInt(info[2]));
			
			//Insert anatomical labels and BAs into search
			for (int i = 3; i < info.length; i += 2){
				cluster.search.put(info[i], info[i+1]);
			}
			
			//Calculate and insert into proper bin
			int bin = binLogic(cluster);
			switch (bin) {
				case 0: database.get(0).add(cluster);
					break;
				case 1: database.get(1).add(cluster);
					break;
				case 2: database.get(2).add(cluster);
					break;
				case 3: database.get(3).add(cluster);
					break;
				case 4: database.get(4).add(cluster);
					break;
				case 5: database.get(5).add(cluster);
					break;
				case 6: database.get(6).add(cluster);
					break;
				case 7: database.get(7).add(cluster);
					break;
			}
			
			/*---------------------- Testing --------------------------
			System.out.println(Integer.toString(cluster.getX()) + ", " +
							   Integer.toString(cluster.getY()) + ", " +
							   Integer.toString(cluster.getZ()));
			
			for (Map.Entry<String, String> entry : cluster.search.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + " : " + value);
			} 
			System.out.println(Integer.toString(bin));
			 ----------------------------------------------------------*/
		}
		//Close scanner and FIS
		scanFile.close();
		fis.close();
	}
	
	public static void writeToDatabase(String file_path) throws FileNotFoundException, IOException {
		/**
		 * Procedure: Writes all data in BA database to text file database 
		 * @param file_path: file path for text file datbase
		 * Writes data to text file for each cluster in following format:
		 * x y z label BA label BA (follows convention detailed in class spec)
		 * Precondition: file_path is path to text file database
		 */
		
		//Create FileWriter with input text file
		File input = new File(file_path);
		FileWriter textFile = new FileWriter(input);
		
		//Set space variable for code cleanliness
		String space = " ";
		
		//For each bin in database
		for (int i = 0; i < database.size(); i++){
			//For each cluster in bin
			for (int j = 0; j < database.get(i).size(); j++){
				//Set up information gathering
				String clusterInfo = "";
				Comp cluster = database.get(i).get(j);
				
				//Gather coordinate info
				clusterInfo += Integer.toString(cluster.getX()) + space;
				clusterInfo += Integer.toString(cluster.getY()) + space;
				clusterInfo += Integer.toString(cluster.getZ()) + space;
				
				//Gather anatomical labels and BAs
				for (Map.Entry<String, String> entry : cluster.search.entrySet()){
					String key = entry.getKey();
					String value = entry.getValue();
					clusterInfo += key + space + value + space;
				}
				
				//Write cluster information to file
				textFile.write(clusterInfo.trim() + "\n");
			}
		}
		//Close output stream
		textFile.close();
	}
	
	/* ---------------------- Testing ------------------------- 
	public static void main(String[] args){
		
		//Check for file path argument
		if (args.length < 1){
			System.out.println("Error: Please include file path");
			System.exit(1);
		}
		
		//Make sure proper file path
		try {
			buildDatabase(args[0]);
			writeToDatabase(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} -----------------------------------------------------------*/

}
