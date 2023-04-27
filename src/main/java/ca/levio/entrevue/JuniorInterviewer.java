package ca.levio.entrevue;
public class JuniorInterviewer extends Handler {
	
	@Override
	public void handleRequest(Request request) {
		successor.handleRequest(request);
	}
}
