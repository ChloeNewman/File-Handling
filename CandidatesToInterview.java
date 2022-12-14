import static java.nio.file.AccessMode.READ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CandidatesToInterview {
	boolean validDegree = false;
	boolean validExperience  = false;
	int pplcounter = 0;
	String[][] cvdata;
	String[][] experiencearray;
	int yearsExperience = 0;
	int experienced;
	int totalexperience;

	//output candidates that fit requirements in file called to-interview.txt
	public void findCandidates() {
		experienced = 0;
		cvdata = new String[10][10];
		experiencearray = new String[10][2];
		
		String[] keywordsDegree = {"Degree in Computer Science", "Masters in Computer Science"};
		String[] keywordsExperience = {"Data Analyst", "Programmer", "Computer programmer", "Operator"};
		
		//get the cleancv.txt file
		Path cleanFilePath = Paths.get("cleancv.txt");
		String cleanFile = cleanFilePath.toString();
		
		try {
			cleanFilePath.getFileSystem().provider().checkAccess(cleanFilePath, READ);	
			
		} catch (IOException e) {
			System.out.println("clean read: computer says no");
			e.printStackTrace();
		}

		//create new file for candidates that will be invited to interview
		try {
			BufferedReader cleancvreader = new BufferedReader(new FileReader(cleanFile));
			
			try (BufferedWriter interviewriter = new BufferedWriter(new FileWriter("to-interview.txt"))) {			
			
				try {
				String intcvline = null;
					
					while ((intcvline = cleancvreader.readLine()) != null) {
						int experiencecounter = 0;
						String comma = ",";
					
						//check against the arrays
						for (int d = 0; d < keywordsDegree.length; d++) {
							if (intcvline.contains(keywordsDegree[d])) {
								validDegree = true;									
									
								for (int e = 0; e < keywordsExperience.length; e++) {
									if (intcvline.contains(keywordsExperience[e])) {
										validExperience = true;
										
										experiencecounter = experiencecounter + 1;
										
										if (experiencecounter == 1) {
											//split the line and put all the bits into an array
											String[] experience = intcvline.split(comma);
											
											//parse into int and check its over 5
											yearsExperience = Integer.parseInt(experience[3]);
											
											if (yearsExperience > 5) {
												
												//add to new experiencearray
												for (int i = 0; i < experience.length; i++) {
													if (i == 0) {
														experiencearray[experienced][0] = experience[i];
													}
													
													if (i == 3) {
														experiencearray[experienced][1] = experience[i];
													}
												}
												
												experienced = experienced + 1;												
											}	
										}
									}
								}
							}	
						}
									
						if (validDegree == true && validExperience == true) {
							//cvdata array
							String[] forarray = intcvline.split(comma);						
							
							for (int i = 0; i < forarray.length; i++) {
								cvdata[pplcounter][i] = forarray[i];
							}
								pplcounter = pplcounter + 1;	

							//add the line into new txt file
							intcvline = intcvline.replace(',', ' ');
							
							interviewriter.write(intcvline);
							interviewriter.newLine();
													
							validDegree = false;
							validExperience = false;
						}
					}
					
				} catch (IOException e) {
					System.out.println("to-interview.txt created: computer says no");
					e.printStackTrace();
				
				} finally {
					try {
						cleancvreader.close();
						interviewriter.close();
					} catch (IOException e) {
						e.printStackTrace();        
					}
				}
				
			} catch (IOException e) {
				System.out.println("interview.txt created: computer says no");
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}	

	public void candidatesWithExperience() {
		
		try (BufferedWriter experiencedwriter = new BufferedWriter(new FileWriter("to-interview-experience.txt"))) {
			
			try {					

				for (int i = 0; i < experiencearray.length; i++) {
						
					if (experiencearray[i][0] == null) {
							
					} else {
						
						experiencedwriter.write(experiencearray[i][0] + " " + experiencearray[i][1]);
						experiencedwriter.newLine();
						//System.out.println(experiencearray[i][0] + " " + experiencearray[i][1]);
					}
				}

			} finally {
				try {
					experiencedwriter.close();
				} catch (IOException e) {
					e.printStackTrace();        
				}
			}
				
		} catch (IOException e) {
			System.out.println("to-interview-experience.txt created: computer says no");
			e.printStackTrace();
		}
	}

	public void createCSVFile() {
		
		try (BufferedWriter csvfile = new BufferedWriter(new FileWriter("to-interview-table-format.csv"))) {
			
			try {
				
				//make headings for the table
				String headings = "Identifier,Qualification,Position1,Experience1,Position2,Experience2,eMail";
				csvfile.write(headings);
				csvfile.newLine();
								
				//get array and make it a string with commas to separate
				for (int i = 0; i < cvdata.length; i++) {
					int commacount = 0;
					int fourthcomma = 0;
					String line = "";	
						
					if (cvdata[i][0] == null) {
						//do nothing
								
					} else {		
						//find the length of the line							
						for (int a = 0; a < cvdata[i].length; a++) {
							if (cvdata[i][a] != null) {
							
								line = line + cvdata[i][a] + ",";
							}							
						}
							
						//find number of commas
						for (int c = 0; c < line.length(); c++) {
								
							if (line.charAt(c) == ',') {
								commacount++;	
									
								if (commacount == 4) {
									fourthcomma = c;	
								}
							}
						}

						//if 7 bits of data (comma)
						if (commacount == 7) {
							csvfile.write(line);
							csvfile.newLine();
								
						//if 5 bits of data (comma)	
						} else if (commacount == 5) {
							String lineFormatted = new String();
							String extracommas = ",,";
								
							for (int x = 0; x < line.length(); x++) {
								lineFormatted += line.charAt(x);
							
								if (x == fourthcomma) {
									lineFormatted += extracommas;
								}
							}
								
							csvfile.write(lineFormatted);
							csvfile.newLine();	
						}				
					}	
				}
					
			} catch (IOException e) {
				e.printStackTrace();
					
			} finally {
				try {
					csvfile.close();
				} catch (IOException e) {
					e.printStackTrace();        
				}
			}
							
		} catch (IOException e) {
			System.out.println("csv created: computer says no");
			e.printStackTrace();
		}
	}
	
	public void createReport() {
		//reads records from the to-interview-table-format.csv file and prints contents to the console
		String comma = ",";
		
		try (BufferedReader readcsvtable = new BufferedReader(new FileReader("to-interview-table-format.csv"))) {
			
			String csvline;
			
			while ((csvline = readcsvtable.readLine()) != null) {
				
				if (csvline.startsWith("Identifier")) {
					System.out.println("Identifier" + "\t" + "Qualification" + "\t\t\t" + "Position" + "    " + "Experience" + "\t" + "eMail");
					
				} else {
				
					String[] report = csvline.split(comma);
					System.out.println(report[0] + "\t" + report[1] + "\t" + report[2] + "\t" + report[3] + "\t" + report[6]);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
