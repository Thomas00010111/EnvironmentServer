/**
 * 
 * @author Thomas Fischle
 *
 */

public class ActionDiscreteAction implements IAction{
	private final int NUMBEROFACTIONS = 4; 
	double start_position_x;
	double start_position_y;

	double angleShift;
	double angle;
	double arc = 0;
	
	//That is not nice, but actually we would need information from 
	//the abstract environment
	private int SIZEOFABSTRACTSQUARE = 50;
	public final int ENVIRONMENT_HIGHT = 200, ENVIRONMENT_WIDTH = 200;

	public ActionDiscreteAction() {
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
		System.out.println("speed Factor noch nicht implementiert!!!");
		
		AngleXYCoordinate deltaPosition = new AngleXYCoordinate(); 
		
		this.angle = angle;
		angleShift = 0;
		
		switch(a)
		{
			case 0:
				// go left
				deltaPosition.x = -SIZEOFABSTRACTSQUARE;
				deltaPosition.y = 0;
				break;
			case 1:
				//go right
				deltaPosition.x =  SIZEOFABSTRACTSQUARE;
				deltaPosition.y =  0; 
				break;
			case 2:
				//go up
				deltaPosition.x = 0;
				deltaPosition.y = -SIZEOFABSTRACTSQUARE;
				break;
			case 3:
				//drive backwards
				deltaPosition.x = 0;
				deltaPosition.y = SIZEOFABSTRACTSQUARE;
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
