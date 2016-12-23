/**
 * 
 * @author Thomas Fischle
 *
 */

public class ActionSimpleContinuous implements IAction{
	private final int NUMBEROFACTIONS = 4; 
	
	//That is not nice, but actually we would need information from 
	//the abstract environment
	private final int StepSize = 2;
	public final int ENVIRONMENT_HIGHT = 200, ENVIRONMENT_WIDTH = 200;

	public ActionSimpleContinuous() {
		// TODO Auto-generated constructor stub
	}
	
	public double getRandomAngle()
	{
		double randNumber = Math.random();
		double randAngle = randNumber * 360;
		return randAngle;
	}
	
	public AngleXYCoordinate execute(int a, double angle)
	{
		return execute(a, 1.0, angle);
	}
	
	public AngleXYCoordinate execute(int a, double speedFactor, double angle)
	{
		AngleXYCoordinate deltaPosition = new AngleXYCoordinate(); 
		
		switch(a)
		{
			case 0:
				// go left
				deltaPosition.x = -StepSize*speedFactor;
				deltaPosition.y = 0;
				break;
			case 1:
				//go right
				deltaPosition.x =  StepSize*speedFactor;
				deltaPosition.y =  0; 
				break;
			case 2:
				//go up
				deltaPosition.x = 0;
				deltaPosition.y = -StepSize*speedFactor;
				break;
			case 3:
				//drive backwards
				deltaPosition.x = 0;
				deltaPosition.y = StepSize*speedFactor;
				break;
			default:return deltaPosition;
		}
		
		//System.out.print("deltaPosition.x: "+ deltaPosition.x + "    deltaPosition.y:" + deltaPosition.y);
		return deltaPosition;
	}

	public int getNumberOfActions()
	{
		return NUMBEROFACTIONS;
	}
}
