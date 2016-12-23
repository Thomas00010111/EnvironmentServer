public interface ICar {
	
	public AngleXYCoordinate executeAction(int a);
	public AngleXYCoordinate executeAction(int a, double steps);
	public XYCoordinate[][] getBorders(AngleXYCoordinate currentPosAgent);
	public XYCoordinate[] getCorners(AngleXYCoordinate currentPosAgent);
	public void refillEneryLevel();
	public double getEneryLevel();
}
