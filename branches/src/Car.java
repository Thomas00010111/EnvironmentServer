/**
 * 
 * @author Thomas Fischle
 *
 */

public class Car implements ICar{
	
	IAction actions = new ActionContinuous();
	//IAction actions = new ActionDiscreteAction();
	Environment environment;
	private final double ANGLEFRONTCAR = Math.PI/8;		/** PI/8 => 22,5deg angle between height and one side of triangle */ 
	private final int NUMBEROFEDGES = 3;
	private final int LENGTHEDGE = 20;
	
	/** Energy level of full tank*/
	private final double FULLENERYLEVEL = 1;
	
	/** At what energy level is tank considered as empty?*/
	private final double EMPTYTANK = 0;
	
	/** Reward if energy reaches EMPTYTANK level */
	private final double REWARDEMPTYTANK = -1;
	
	/**costs of a basic step, usually one action consists of about 10 steps!!*/
	private double costsOneStep = 0.005;	
	
	private double energyLevel;	//current amount of petrol
	
	//private Square[] MyObstacles = new Square[2]; 
	
	//reduced from 3 to 2 see comment in getBorders()
	private XYCoordinate[][] borders = new XYCoordinate[2][LENGTHEDGE]; 

	public Car( Environment env) 
	{
		energyLevel = FULLENERYLEVEL;
		this.environment = env;
	}
	
	/**
	 * Execute an IAction and decrement the energy level of the car by <i>costsOneStep</i>. The action starts
	 * at the current position of the car in the environment.
	 * @param 	action to be executed
	 * @return  effect the action has to the environment, i.e. returned result has to added to current position and angel    
	 */
	public AngleXYCoordinate executeAction(int a)
	{
		return executeAction(a, 1.0);
	}
	
	public AngleXYCoordinate executeAction(int a, double steps)
	{
		energyLevel = energyLevel - costsOneStep;	//every step costs energy
		
		if(energyLevel < EMPTYTANK)				//minimal energy is 0  
			energyLevel = EMPTYTANK;
		
		AngleXYCoordinate deltaEnvironment;
		deltaEnvironment = actions.execute(a,environment.getOrientation(), steps);
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
			//Commented out 28.06.2015 
			//if obstacle starts at 0,0 this leads to "car in obstacle"
			//NOT TESTED WITH THIS CAR YET!!!
			//borders[2][j] = new XYCoordinate(1,1);		//create and initialize with 1, otherwise null pointer exception or point ii always on border of environment
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
	
	/**
	 * Return the current energy level of the cart. Do not calculate anything here, all calculations 
	 * like scaling or discretization should be done in class AbstractEnvironment on the client.
	 * 
	 * @return current energy of the car
	 */
	public double getEneryLevel()
	{
		//double temp_energy = (double)energyLevel/(double)FULLENERYLEVEL; 
		
		if (energyLevel <= 0)
			return EMPTYTANK;
		
		return (double)energyLevel;
	}
	
	
	public void refillEneryLevel()
	{
		energyLevel = FULLENERYLEVEL;
	}
	
	public void setEneryCosts(double costs)
	{
		costsOneStep = costs;
	}
	
	/**
	 * We make a difference between the current energy the car has and the reward
	 * which is connected to this energy. Current energy cannot be smaller than 0
	 * but the reward is -1 if the energy reaches 0 
	 * @return
	 */
	
	public double getEneryLevelReward()
	{
		if (energyLevel <= EMPTYTANK)
			return REWARDEMPTYTANK;
		
		return energyLevel;
	}
}
