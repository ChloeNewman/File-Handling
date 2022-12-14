public class JobCandidatesMain {

	public static void main(String[] args) {
		CleaningUp file = new CleaningUp();
		file.cleanUpFile();
		
		CandidatesToInterview intfile = new CandidatesToInterview();
		intfile.findCandidates();
		intfile.candidatesWithExperience();
		intfile.createCSVFile();
		intfile.createReport();
	}
}
