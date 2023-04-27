package ca.levio.entrevue;

public class IntermediaireInterviewer extends Handler{
	
	@Override
	public void handleRequest(Request request) {
		if (request.getExpertiseType() == ExpertiseType.JUNIOR){
			System.out.println("This interviewer can interview a JUNIOR");
		}
		else {
			successor.handleRequest(request);
		}
	}

}
