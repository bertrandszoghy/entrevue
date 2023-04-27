package ca.levio.entrevue;

public class SeniorInterviewer extends Handler {
	
	@Override
	public void handleRequest(Request request) {
		if((request.getExpertiseType() == ExpertiseType.INTERMEDIAIRE) || (request.getExpertiseType() == ExpertiseType.JUNIOR)){
			System.out.println("This interviewer can interview an INTERMEDIAIRE or JUNIOR");
		}
		else {
			successor.handleRequest(request);
		}
	}
}
