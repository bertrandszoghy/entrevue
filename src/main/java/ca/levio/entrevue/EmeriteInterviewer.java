package ca.levio.entrevue;

public class EmeriteInterviewer extends Handler {

@Override
	public void handleRequest(Request request) {
		if((request.getExpertiseType() == ExpertiseType.EMERITE) || (request.getExpertiseType() == ExpertiseType.SENIOR)){
			System.out.println("This interviewer can interview an EMERITE");
		}
		else {
			successor.handleRequest(request);
		}
	}
}
