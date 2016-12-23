	/**
	 * Extends class Square by the possibility to assign a value to the 
	 * defined area, e.g. a reward. 
	 */
public class SpeedSquare extends Square{
		
	double speedFactor;
	public SpeedSquare(int x0, int y0, int x3, int y3, double speedFactor){
		super( x0, y0, x3, y3);
		
	this.speedFactor =speedFactor;
		
	}
	/**
	 * checks if one point of the car is above the action square. If the car is on the
	 * action square, than call the action that to should be done with the car (e.g. refill).
	 *  
	 * @param car car driving in the environment
	 * @param currentCoordinates current coordinates of the car
	 * @return 1 if car is on action square else -1
	 */
	public double getSpeedFactor()
	{
		return this.speedFactor;
	}
}
