import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.util.ArrayList;

public class BAFeedback extends BADatabaseAccess {
	/**Class for accessing database and running feedback control
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * database: see BADatabaseAccess
	 * ------------ Methods --------------------
	 * buildDatabase: see BADatabaseAccess 
	 * writeToDatabase: see BADatabaseAccess
	 * feedbackControl: Runs feedback loop on database
	 */
	
	public static void feedbackControl(String text_path, String excel_path, int sheetNumber) throws FileNotFoundException, IOException{
		/**
		 * Procedure: Run feedback on BA classification from completed whole brain table
		 * 1. Parses input excel file to extract cluster information
		 * 	a. coordinates
		 * 	b. Dict of key = anatomical labels, value = BA
		 * 2. Compares complete cluster input information to each database entry, 
		 * 	  changes BA if incorrect
		 * Fundamental Assumptions for Whole Brain Table Formatting:
		 * A. Column order is as follows:
		 * 	1. Contrast
		 *  2. Lobe
		 *  3. Hemisphere
		 *  4. AAL
		 *  5. Label
		 *  6. Brodmann Area
		 *  7. Voxels
		 *  8, 9, 10. Peak Coordinates
		 * B. Header row(s) ends at or before row 2
		 * C. No blank rows in between contrasts and clusters
		 * Precondition: text_path is file path for database text file
		 * 				 excel_path is path to whole brain excel file 
		 * 				 sheetNumber is sheet to parse in excel_path file (indexed from 0)
		 */
		
		//Build database
		buildDatabase(text_path);
		
		//List of clusters for comparison with database entries
		ArrayList<Comp> clusters = new ArrayList<Comp>();
		
		//Create fileinputstream for excel file
		File excel = new File(excel_path);
		FileInputStream WBFile = new FileInputStream(excel);
		Workbook wb = new XSSFWorkbook(WBFile);
		
		//Set up parsing
		Sheet table = wb.getSheetAt(sheetNumber);
		
		//Iterate through Whole Brain Table, adding clusters to list
		for (int i = 2; i < table.getLastRowNum(); i++){
			Row cRow = table.getRow(i);
			Cell cellX = cRow.getCell(7);
			if (cellX != null && cellX.getCellType() == 0){
				int x = (int) cellX.getNumericCellValue();
				int y = (int) cRow.getCell(8).getNumericCellValue();
				int z = (int) cRow.getCell(9).getNumericCellValue();
				Comp cluster = new Comp(x,y,z);
				int j = i+1; //row number
				Cell labelCell = table.getRow(j).getCell(3);
				while (labelCell != null && labelCell.getCellType() == 1) {
					String label = labelCell.getStringCellValue();
					String BA = "0";
					if (labelCell.getRow().getCell(5) != null){
						if (labelCell.getRow().getCell(5).getCellType() == 1){
							BA = labelCell.getRow().getCell(5).getStringCellValue();
						}
						else {
							BA = Integer.toString((int) labelCell.getRow().getCell(5).getNumericCellValue());
						}
					}
					cluster.search.put(label, BA);
					j++;
					if (j > table.getLastRowNum()){
						break;
					}
					labelCell = table.getRow(j).getCell(3);
				}
				clusters.add(cluster);
			}
		}
		
		for (int k = 0; k < clusters.size(); k++){
			Comp compare = clusters.get(k);
			Boolean contains = false;
			int bin = binLogic(compare);
			//Check compare to clusters in proper bin
			if (database.get(bin).size() < 1){
				database.get(bin).add(compare);
				continue;
			}
			for (Comp entry : database.get(bin)){
				//If cluster in bin matches compare, look at search, change contains to True
				if (entry.equals(compare)){
					for (String label : compare.search.keySet()){
						entry.search.put(label, compare.search.get(label));
					}
					contains = true;
				}
			}
			//If compare not in database, add
			if (!contains){
				database.get(bin).add(compare);
			}
		}
		//close file and input stream
		wb.close();
		WBFile.close();
		
		//Write to text file
		writeToDatabase(text_path);
	}
	
	public static void main(String[] args){
		//Check for file path argument
			if (args.length < 2){
				System.out.println("Error: Please include file paths");
				System.exit(1);
			}
			//Make sure proper file path
			try {
				feedbackControl(args[0], args[1], 1);
			} catch (Exception e){
				e.printStackTrace();
			}
	}
}
