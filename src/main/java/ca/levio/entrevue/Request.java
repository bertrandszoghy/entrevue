package ca.levio.entrevue;

public class Request {

	private ExpertiseType expertiseType;
	private PositionType positionType;
		
	public Request(ExpertiseType expertiseType, PositionType positionType) {
		this.expertiseType = expertiseType;
		this.positionType = positionType;
	}
	
	public ExpertiseType getExpertiseType() {
		return expertiseType;
	}
	
	public PositionType getPositionType() {
		return positionType;
	}
	
	
}