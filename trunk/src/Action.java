/**
 * 
 * @author Thomas Fischle
 *
 */

public class Action {
	private final int NUMBEROFACTIONS = 4; 
	private final int STEP  = 1;
	double start_position_x;
	double start_position_y;
	private double newCenterCircle_x;
	private double newCenterCircle_y;
	double angleShift;
	double angle;
	double arc = 0;

	public Action() {
		// TODO Auto-generated constructor stub
	}
	
	double getRandomAngle()
	{
		double randNumber = Math.random();
		double randAngle = randNumber * 360;
		return randAngle;
	}
	
	public AngleXYCoordinate execute(int a, double angle)
	{
		double radius = 20;
		AngleXYCoordinate deltaPosition = new AngleXYCoordinate(); 
		
		this.angle = angle;
		angleShift = Math.PI/2 - angle; //refer to drawing: alpha = 90°-fi
		
		switch(a)
		{
			case 0:
				//turn left
				newCenterCircle_x = - Math.sin(angle)*radius;
				newCenterCircle_y = Math.cos(angle)*radius;
				deltaPosition.x = radius * Math.cos((double)STEP/10.0 - angleShift) + newCenterCircle_x;
				deltaPosition.y = radius * Math.sin((double)STEP/10.0 - angleShift) + newCenterCircle_y;
				
				//calculate length of step
				arc = radius * 1/10;
				//calculate new angle
				deltaPosition.angle = arc /radius; 
				break;
			case 1:
				//turn right
				newCenterCircle_x =  Math.sin(angle)*radius;
				newCenterCircle_y =  - Math.cos(angle)*radius;

				deltaPosition.x = radius * Math.cos(Math.PI + (double)STEP/10.0 + angleShift)  + newCenterCircle_x;
				deltaPosition.y = radius * -Math.sin(Math.PI + (double)STEP/10.0 + angleShift) + newCenterCircle_y;
				
				//calculate length of step
				arc = radius * 1/10;
				//calculate new angle
				deltaPosition.angle = - arc /radius; 	//global angle, pointing in same direction 
				break;
			case 2: 
				deltaPosition.x = 1 * Math.cos(angle);
				deltaPosition.y = 1 * Math.sin(angle);
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
	
	/*
	 public boolean execute(int a, Environment environment)
	{
		double radius = 30;
		double x, y;
		
		if(!actionInProgress)
		{
			angle = environment.getOrientation();
			start_position_x = environment.getCurrentXposition();
			start_position_y = environment.getCurrentYposition();
			angleShift = Math.PI/2 - environment.getOrientation(); //refer to drawing: alpha = 90ï¿½-fi
			//actionInProgress = true;
		}
		
		switch(a)
		{
			case 0:
				//turn left
				if(!actionInProgress)
				{
					newCenterCircle_x = start_position_x - Math.sin(environment.getOrientation())*radius;
					newCenterCircle_y = start_position_y + Math.cos(environment.getOrientation())*radius;
					actionInProgress = true;
				}
				x = radius * Math.cos((double)step/10.0 - angleShift) + newCenterCircle_x;
				y = radius * Math.sin((double)step/10.0 - angleShift) + newCenterCircle_y;
				
				//calculate length of step
				arc = radius * 1/10;
				//calculate new angle
				angle = arc /radius + angle; 
				break;
			case 1:
				//turn right
				if(!actionInProgress)
				{
					newCenterCircle_x = start_position_x + Math.sin(environment.getOrientation())*radius;
					newCenterCircle_y = start_position_y - Math.cos(environment.getOrientation())*radius;
					actionInProgress = true;
				}
				x = radius * Math.cos(Math.PI + (double)step/10.0 + angleShift)  + newCenterCircle_x;
				y = radius * -Math.sin(Math.PI + (double)step/10.0 + angleShift) + newCenterCircle_y;
				
				//calculate length of step
				arc = radius * 1/10;
				//calculate new angle
				angle = angle - arc /radius; 	//global angle, pointing in same direction 
				break;
			case 2: return false;
			default:return false;
		}
		
		environment.setEnvironment(x,y,angle);		//change environment
		System.out.print("x: "+ x + "    y:" + y);
		if( step == 15 )
		{
			step = 1;
			actionInProgress = false;
			return false;
		}
		step++;		//0.1 + 0.1+ .. gibt z.B. irgendwann 0.299999999999
		return true;
	}
	 */
}
