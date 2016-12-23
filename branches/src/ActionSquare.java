	/**
	 * Extends class Square by the possibility to assign a value to the 
	 * defined area, e.g. a reward. 
	 */
public class ActionSquare extends Square{
		
	int myType;
	
	public ActionSquare(int x0, int y0, int x3, int y3,int type){
		super( x0, y0, x3, y3);
		this.myType = type;
	}
	/**
	 * checks if one point of the car is above the action square. If the car is on the
	 * action square, than call the action that to should be done with the car (e.g. refill).
	 *  
	 * @param car car driving in the environment
	 * @param currentCoordinates current coordinates of the car
	 * @return 1 if car is on action square else -1
	 */
	public int doActionIfInSquare(ICar car, AngleXYCoordinate currentCoordinates)
	{
		//check if car is in ActionSquare
		if(pointInSquare(car.getBorders(currentCoordinates)))
		{
			switch(myType)
			{
				case 0:
					car.refillEneryLevel();
					break;
				case 1:
					//do something else
					break;
			}
			return 1;	//updated state of car
		}
		return -1;	//not in ActionSquare
	}
}
