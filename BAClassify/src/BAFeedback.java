import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.util.ArrayList;

public class BAFeedback extends BADatabaseAccess {
	/**Class for accessing database and running feedback control
	 * @author Noah Rubin
	 * ------------ Fields ---------------------
	 * database: Database of cluster peaks and associated aal:BA lists (Comp objects)
	 * ------------ Methods --------------------
	 * binLogic: calculates proper bin for given cluster
	 * buildDatabase: builds ArrayList database from text file 
	 * writeToDatabase: writes database information to text file
	 * feedbackControl: Feedback control/algorithm training
	 */
	
	public static void feedbackControl(String text_path, String excel_path, Integer sheetNumber) throws FileNotFoundException, IOException{
		/**
		 * Procedure: Run feedback on BA classification from completed whole brain table
		 * @param text_path: file path for text file database
		 * @param excel_path: file path for Excel file containing table
		 * @param sheetNumber: sheet in Excel file containing table (indexed from 0)
		 * 1. Parses input excel file to extract cluster information
		 * 	a. coordinates
		 * 	b. Dict of key = anatomical labels, value = BA
		 * 2. Compares complete cluster input information to each database entry, 
		 * 	  changes BA if incorrect
		 * Fundamental Assumptions for Whole Brain Table Parsing:
		 * A. Columns have following order:
		 *  Column 4 --> Human Atlas AAL
		 *  Column 6 --> Brodmann Area
		 *  Columns 8, 9, 10 -->  Peak Coordinates x, y, z
		 * B. Header row(s) ends at or before row 2
		 * C. No blank rows in between contrasts and clusters
		 * Note: if file paths point to non-existant resource or there is read/write issues, 
		 * method will throw exception and give back the stack trace
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
			//Get x coordinate
			Row cRow = table.getRow(i);
			Cell cellX = cRow.getCell(7);
			
			//If x coordinate not empty
			if (cellX != null && cellX.getCellType() == 0){
				//Parse out and grab coordinates
				int x = (int) cellX.getNumericCellValue();
				int y = (int) cRow.getCell(8).getNumericCellValue();
				int z = (int) cRow.getCell(9).getNumericCellValue();
				Comp cluster = new Comp(x,y,z);
				int j = i+1; //row number
				
				//Set anatomical label cell, grab AAL and BAs
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
				//Add cluster to clusters list
				clusters.add(cluster);
			}
		}
		
		//Compare all clusters to database entries
		for (int k = 0; k < clusters.size(); k++){
			Comp compare = clusters.get(k);
			Boolean contains = false;
			int bin = binLogic(compare);
			//If bin is empty, add to bin
			if (database.get(bin).size() < 1){
				database.get(bin).add(compare);
				continue;
			}
			//If not, compare to clusters in bin
			for (Comp entry : database.get(bin)){
				//If cluster in bin matches compare look at search
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
		
		//Write updated database to text file
		writeToDatabase(text_path);
	}
	
	public static void main(String[] args){
		//Check for file path argument
			if (args.length < 2){
				System.out.println("Error: Please include text and Excel file paths in that order");
				System.exit(1);
			}
			else if (args.length < 3){
				System.out.println("Error: Please include table sheet number. Note: sheets are indexed from 0");
				System.exit(1);
			}
			//Make sure proper file paths
			try {
				feedbackControl(args[0], args[1], Integer.valueOf(args[2]));
			} catch (FileNotFoundException f){
				System.out.println("Error: Problem with one of or both file paths, please check and re-run");
				System.exit(1);
			} catch (IOException io){
				System.out.println("Error: Problem with file stream, check the stack trace");
				io.printStackTrace();
				System.exit(1);
			} catch (Exception e){
				System.out.println("Error: Unknown exception, check stack trace");
				e.printStackTrace();
			}			
			
	}
}
