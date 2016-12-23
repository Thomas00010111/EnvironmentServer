/**
 * 
 * @author Thomas Fischle
 *
 */

public class Car {
	
	Action actions = new Action();
	Environment environment;
	EnvironmentServer environmentServer;
	private final double ANGLEFRONTCAR = Math.PI/8;		/** PI/8 => 22,5�, angle between height and one side of triangle */ 
	private final int NUMBEROFEDGES = 3;
	private final int LENGTHEDGE = 20;
	
	//private Square[] MyObstacles = new Square[2]; 
	private XYCoordinate[][] borders = new XYCoordinate[3][LENGTHEDGE]; 

	public Car( Environment env) {
		// TODO Auto-generated constructor stub
		this.environment = env;
	}
	
	/**
	 * Execute an Action. The action starts at the current position of the car in the environment.
	 * @param 	a action to be executed
	 * @return  effect the action has to the environment, i.e. returned result has to added to current position and angel    
	 */
	public AngleXYCoordinate executeAction(int a)
	{
		AngleXYCoordinate deltaEnvironment;
		deltaEnvironment = actions.execute(a,environment.getOrientation());
		environment.paintComponent();
		return deltaEnvironment;
	}
	/**
	 * Position of the car is described by x and y, and an angel to describe in which direction the car drives. 
	 * Angle is given with respect to the x-axis.
	 * @return  Returns the angle/driving direction of the car in the environment     
	 */
	public double getOrientation(){
		System.out.println();
		System.out.println("orientation: " + environment.getOrientation());
		return environment.getOrientation();
	}
	/**
	 * Get borders of car. The boarders are calculated from the xy-coordinates and the
	 * angle given in the passed argument. The current xy-coordinates and the current angle
	 * of the environment are NOT used, only the values of the passed object.
	 *    
	 * @param 	currentPosAgent Current xy-position of the agent
	 * @return  the coordinates are given in absolute coordinates, so no further calculation is necessary.
	 * 			For every edge of the car one array with x and y values is returned.    
	 */
	public XYCoordinate[][] getBorders(AngleXYCoordinate currentPosAgent)
	{

		for(int j = 0; j < LENGTHEDGE; j++)
		{
			borders[0][j] = new XYCoordinate(	currentPosAgent.x - Math.cos(currentPosAgent.angle-ANGLEFRONTCAR) * j,		 
												currentPosAgent.y - Math.sin(currentPosAgent.angle-ANGLEFRONTCAR) * j );
			borders[1][j] = new XYCoordinate(	currentPosAgent.x + Math.cos(Math.PI -currentPosAgent.angle-ANGLEFRONTCAR) * j,
												currentPosAgent.y - Math.sin(Math.PI -currentPosAgent.angle-ANGLEFRONTCAR) * j );
			borders[2][j] = new XYCoordinate(1,1);		//create and initialize with 1, otherwise null pointer exception or point ii always on border of environment

		}
		
		return borders;
	}
	
	public XYCoordinate[] getCorners(AngleXYCoordinate currentPosAgent)
	{
		XYCoordinate[] corners = new XYCoordinate[NUMBEROFEDGES];
		
		//front
		corners[0] = new XYCoordinate(	currentPosAgent.x, currentPosAgent.y );
		
		//side
		corners[1] = new XYCoordinate(	currentPosAgent.x - Math.cos(currentPosAgent.angle-ANGLEFRONTCAR) * (LENGTHEDGE-1), 
										currentPosAgent.y - Math.sin(currentPosAgent.angle-ANGLEFRONTCAR) * (LENGTHEDGE-1) );
		
		//side
		corners[2] = new XYCoordinate(	currentPosAgent.x + Math.cos(Math.PI -currentPosAgent.angle-ANGLEFRONTCAR) * (LENGTHEDGE-1),
										currentPosAgent.y - Math.sin(Math.PI -currentPosAgent.angle-ANGLEFRONTCAR) * (LENGTHEDGE-1) );
		
		return corners;
	}
	
	public double getAngleFront()
	{
		return ANGLEFRONTCAR;
	}

}
