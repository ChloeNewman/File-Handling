import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.AccessMode.*;

public class CleaningUp {
	int cvcounter = 0;
	
	public void cleanUpFile() {

		//get the dirty file path
		Path dirtyFilePath = Paths.get("dirtycv.txt");
		String dirtyFile = dirtyFilePath.toString();
		
		try {
			dirtyFilePath.getFileSystem().provider().checkAccess(dirtyFilePath, READ);	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//read the dirty file
		try {
			BufferedReader cvreader = new BufferedReader(new FileReader(dirtyFile));
			
			try (BufferedWriter cleanedFile = new BufferedWriter(new FileWriter("cleancv.txt"))) {	
			
				try {
					String cvline = null;
			
					while ((cvline = cvreader.readLine()) != null) {
						
						if (cvline.contains("CV")) {
							cvcounter = cvcounter + 1;
					
						} else if(cvline.contains(":")) {
						
							if (cvline.startsWith("Surname")) {
								String[] splitCvline = cvline.split(":");
								cleanedFile.write(splitCvline[1] + "000" + cvcounter + ",");
						
							} else if (cvline.contains("First") || cvline.contains("Address")) {
								
							} else {
								String[] splitCvline = cvline.split(":");
								cleanedFile.write(splitCvline[1] + ",");
							}
					
						} else if (cvline.contains("End")) {
							cleanedFile.newLine();
							
						}
					}
				
				} catch (IOException e) {
					e.printStackTrace();
			
				} finally {
					try {
						cvreader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
			} catch (IOException e) {
				System.out.println("clean created: computer says no");
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
