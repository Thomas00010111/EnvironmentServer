import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Arrays;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.WindowConstants;

/**
 * @author Thomas Fischle
 */

/**
 * Class implements the GUI and specifies the environment. Size and number of reward
 * and action areas can be defined in this class. Strings sent from the client and 
 * received by the server are interpreted in his class.  
 */
public class Environment extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;	//otherwise we get an warning here (why?)
	
	//size and position of reward area
	public final int X0_REWARD = 50, Y0_REWARD = 130, X3_REWARD = 100, Y3_REWARD = 150;

	//due to decoration of frame, location of environment has to be moved
	private final int SHIFT_X = 20, SHIFT_Y = 30;
		
	public final int ENVIRONMENT_HEIGHT = 200, ENVIRONMENT_WIDTH = 200;	//size of environment
	
	public final int NUMBEROFOBSTACLES = 0;	//Number of obstacles in environment
	public final int NUMBEROFREWARDAREAS = 1;	//Number of ares with reward in environment
	public final int NUMBEROFACTIONSQUARES = 1;	//Number of ares with reward in environment
	public final int NUMBEROFMAXLINEARREWARDAREAS = 0;	//Number of points where agents get maximum of linear reward function
	
	public final int POSITIONMAXLINEARREWARD_X = ENVIRONMENT_WIDTH/2; 
	public final int POSITIONMAXLINEARREWARD_Y = ENVIRONMENT_HEIGHT/2 + 20;
	
	//ToDo: find a better max value, e.g. max diagonal?
	public final int LASER_MAX = 200; // Actually it is the number of steps the laser ray does
	public final int LASER_MAX_OPPONENT = 150;
	
	//private double rewardMoveForeward=0.0;
	
	private double steps = -1;
	private long ticks = 0;
	
//	private class LaserObstacleConnection
//	{
//		public LaserObstacleConnection(Laser l, Vector<VSquare> o){this.laser=l; this.obstacle = o;}
//		public Laser laser;
//		public Vector<VSquare> obstacle;
//	}
		
	
	int prev_x=0, prev_y=0;
	
	// We want to model the time it takes to cross a cell. For some cells it should take longer
	// to pass them. Agent has its own time, so timeTicks are actually not needed.
	private double distanceTravelled = 0;
	
	Track.TrackStates trackstate = Track.TrackStates.OK;
	
	private JButton buttonStart;
	private JButton buttonSetEnergyCostsPerAction;
	private JTextField ActionTextField0;
	private JTextField ActionTextField1;
	private JCheckBox updateDrawing;
	
	//ICar car = new Car(this); //Eistuete
	ICar car = new CarPoint(this); //Simple dot
	
	//private Laser laser = new Laser(LASER_MAX);
	//private Laser laserOppononets = new Laser(LASER_MAX)
	
	
	
	/*
	 * BE CAREFULL WITH THE DIRECTION OF THE LASER!!
	 * Especially when the q function should be displayed in a plot. 
	 * laser and x-axis must point in opposite direction.
	 * Same is true for the y-axis. So, actually x,y position is the same as laser measurements.
	 */
	//double [] laserAngles = {Math.PI, 0.0, Math.PI/2.0}; //180deg, 0deg, 90deg
	//double [] laserAngles = {-Math.PI/2.0 ,0.0, Math.PI/2.0}; //-90deg, 0deg, 90deg
	//double [] laserAngles_opponent = {-Math.PI/4.0, -Math.PI/16.0, Math.PI/16.0, Math.PI/4.0};
	double [] laserAngles_opponent = {-Math.PI/2.0, -Math.PI/8, 0.0,  Math.PI/8, Math.PI/2.0}; //5D
	//double [] laserAngles = {-Math.PI/4.0 ,0.0, Math.PI/4.0}; //-45deg, 0deg, 45deg
//	double [] laserAngles_opponent = {}; // use this for a 2d laser only screenshot
	double [] laserAngles = {Math.PI, -Math.PI/2.0}; //-90deg, 0deg
	private Laser[] lasers = {new Laser(LASER_MAX, laserAngles), new Laser(LASER_MAX_OPPONENT, laserAngles_opponent)};  // 3D and 5Dim Opponent
	
//	private LaserObstacleConnection[] laserObstacleConnections = {	new LaserObstacleConnection(new Laser(LASER_MAX, laserAngles), myObstacles),
//														new LaserObstacleConnection(new Laser(LASER_MAX_OPPONENT, laserAngles_opponent), myObstacles) };  // 3D and 5Dim Opponent

	private String laserDistancesObstacles = "", laserDistancesOpponents = "";
	private double [] distancesObstacles, distancesOpponents;
	
	private Square[] linearRewardArea;
	private Vector<VSquare> myObstacles = new Vector<VSquare>();
	private Vector<VSquare> myRewardAreas = new Vector<VSquare>();
	private Vector<VSquare> myOpponents = new Vector<VSquare>();
	//private VSquare[] rewardAreas;
	private ActionSquare[] actionArea;
	private SpeedSquare[] speedSquare;
	private Track track;

	
	/** myCoordinates are the real coordinates, i.e. the actual position,
	 *  tempCoordinates are potential new coordinates. The become real/actual
	 *  coordinates, if the move is allowed. tempCoordinates are also the basis
	 *  for rewards. 
	 */
	public AngleXYCoordinate myCoordinates = new AngleXYCoordinate();
	private AngleXYCoordinate tempCoordinates = new AngleXYCoordinate();
	
	private BufferedImage image;
	
	/* 1: linear reward      2: only reward on square	3:Opponent
	 * Also NUMBEROFMAXLINEARREWARDAREAS and NUMBEROFREWARDAREAS have to be
	 * set correctly, i.e. one of them MUST BE ZERO */
	private final int CURRENTREWARD = 2;
	
	/**
	 * Constructor 
	 */
	public Environment(boolean showGraphics) {
		this("defaultProperties", showGraphics);		
	}
	
	public Environment(String propertiesFile, boolean showGraphics) {
		// create and load default properties
		try {
			//String propertiesFile = "defaultProperties";
			System.out.println("Trying to read properties from: " + System.getProperty("user.dir") + "/" + propertiesFile);
			FileInputStream in = new FileInputStream(propertiesFile);
			Properties defaultProps = new Properties();
			defaultProps.load(in);
			in.close();
			
			if(defaultProps.getProperty("TRACKTYPE").equals("ROUND_TRACK"))
				track = new Track(Track.TrackType.ROUND_TRACK);
			else if (defaultProps.getProperty("TRACKTYPE").equals("OPEN_TRACK"))
				track = new Track(Track.TrackType.OPEN_TRACK);
			else //(defaultProps.getProperty("TRACKTYPE").equals("STRAIGHT_TRACK"))
				track = new Track(Track.TrackType.STRAIGHT_TRACK);
//			else
//				System.out.println("This track does not exist");
//				System.exit(-1);
			
			int numberOfObstacles =Integer.parseInt(defaultProps.getProperty("NUMBEROFOBSTACLES").trim());
			int numberOfRewardAreas = Integer.parseInt(defaultProps.getProperty("NUMBEROFREWARDAREAS").trim());
			int numberOfOpponents =Integer.parseInt(defaultProps.getProperty("NUMBEROFOPPONENTS").trim());
			
			linearRewardArea = new Square[Integer.parseInt(defaultProps.getProperty("NUMBEROFMAXLINEARREWARDAREAS").trim())];
			actionArea = new ActionSquare[Integer.parseInt(defaultProps.getProperty("NUMBEROFACTIONSQUARES").trim())];
			speedSquare = new SpeedSquare[Integer.parseInt(defaultProps.getProperty("NUMBEROFSPEEDSQUARES").trim())];
			
			for (int i = 0; myObstacles != null && i < numberOfObstacles; i++)
			{
				String xyz[] = defaultProps.getProperty("OBSTACLES_XYZ"+Integer.toString(i)).split(",");
				int[] intArray = new int[xyz.length];
				for(int j = 0; j < xyz.length; j++) {
				    intArray[j] = Integer.parseInt(xyz[j].trim());
				}
				myObstacles.add(new VSquare(intArray[0],intArray[1], intArray[2], intArray[3],intArray[4]));
			}
			
			for (int i = 0; myRewardAreas != null && i < numberOfRewardAreas; i++)
			{
				String xyz[] = defaultProps.getProperty("REWARDAREAS_XYZ"+Integer.toString(i)).split(",");
				double[] doubleArray = new double[xyz.length];
				for(int j = 0; j < xyz.length; j++) {
				    doubleArray[j] = Double.parseDouble(xyz[j].trim());
				}
				myRewardAreas.add(new VSquare((int)doubleArray[0],(int)doubleArray[1], (int)doubleArray[2], (int)doubleArray[3],doubleArray[4]));
			}
			
			for (int i = 0; myOpponents != null && i < numberOfOpponents; i++)
			{
				String xyz[] = defaultProps.getProperty("OPPONENTS_XYZ"+Integer.toString(i)).split(",");
				int[] intArray = new int[xyz.length];
				for(int j = 0; j < xyz.length; j++) {
				    intArray[j] = Integer.parseInt(xyz[j].trim());
				}
				myOpponents.add(new VSquare(intArray[0],intArray[1], intArray[2], intArray[3],intArray[4]));
			}
			
			//speedSquare[0] = new SpeedSquare(100,100, 170, 170, 0.5);
			
			//Also read car type
			if(defaultProps.getProperty("CARTYPE").equals("EISTUETE"))
				car = new Car(this); //Eistuete
			else
				car = new CarPoint(this); //Simple dot
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("No properies loaded!!");
			e.printStackTrace();
			System.exit(-1);
			
			track = new Track(Track.TrackType.OPEN_TRACK);  
			
			linearRewardArea = new Square[NUMBEROFMAXLINEARREWARDAREAS];
			//myObstacles = new VSquare[NUMBEROFOBSTACLES];
			//rewardAreas = new VSquare[NUMBEROFREWARDAREAS];
			actionArea = new ActionSquare[NUMBEROFACTIONSQUARES];
			speedSquare = new SpeedSquare[1];
			
			//Obstacles
			if(NUMBEROFOBSTACLES == 1)
				myObstacles.add(new VSquare(30, 50, 70, 100,-1));
			if(NUMBEROFOBSTACLES == 2)
				myObstacles.add(new VSquare(80, 100, 120, 150,-1));
			
			myRewardAreas.add(new VSquare(X0_REWARD, Y0_REWARD, X3_REWARD, Y3_REWARD, 0.1));
			actionArea[0] = new ActionSquare(20, 30, 70, 70, 0);
			speedSquare[0] = new SpeedSquare(100,100, 170, 170, 0.5);
			//linearRewardArea[0] = new Square(POSITIONMAXLINEARREWARD_X-5, POSITIONMAXLINEARREWARD_Y-5, POSITIONMAXLINEARREWARD_X+5, POSITIONMAXLINEARREWARD_Y+5);

		}
		
		track.setStartGoalPosition(new AngleXYCoordinate(20, 100, 0)); //5D opponent
		//track.setStartGoalPosition(new AngleXYCoordinate(170, 160, 0)); //2D
		
		//add track to obstacles
		for(VSquare o : track.getObstacles())
		{
			myObstacles.add(o);
		}
		
				
		//TODO: Think about how to disable the GUI
		//if(showGraphics){}
		
		InitGui();
		setVisible(true);
		paintComponent();

    	
		// Start: place car in the middle of a state/square
		// if Width/STEPSIZE even number then add half the distance
    	// start position has to chosen so, that discrete action in all directions
    	// are possible. Consider the size of the car!
		myCoordinates.x=(ENVIRONMENT_WIDTH + SHIFT_X)/2 + 80;
		myCoordinates.y=(ENVIRONMENT_WIDTH + SHIFT_Y)/2; // + STEPSIZE/2;	
		tempCoordinates.init(myCoordinates);	//initialize tempCoordinates with myCoordinates
		
		distancesObstacles = lasers[0].meassure(myCoordinates, laserAngles, myObstacles);
		
//		distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myOpponents);
		distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myObstacles);
		
		for(int i =0; i<distancesObstacles.length; i++)
    	{
			laserDistancesObstacles = laserDistancesObstacles + ":" + String.valueOf(distancesObstacles[i]);
    	}
		
		for(int i =0; i<distancesOpponents.length; i++)
    	{
			laserDistancesOpponents = laserDistancesOpponents + ":" + String.valueOf(distancesOpponents[i]);
    	}
		
	}
	
	public double getReward(AngleXYCoordinate currentCoordinates) throws Exception
	{
		switch( CURRENTREWARD )
		{
		case 1:
			return getRewardLinear(currentCoordinates);
		case 2:
			return getRewardRewardArea(currentCoordinates);
		case 3:
			return getRewardOpponent(currentCoordinates);
		}
		throw new Exception("no reward type given");
	}
	
	public double getReward(Track.TrackStates tr)
	{
		switch( tr )
		{
		case CROSSED:
			return 2.0;
		case CROSSED_GOAL_WRONG_DIRECTION:
			return 0.0;
		default:
			return 0.0;
		}
	}
	
	/**
	 * 
	 * Reward returned by this method is a function of the distance to the maximal reward and
	 * the energy level of the cart. If the energy level is 0 then a reward of -1 is returned
	 *  
	 * @param currentCoordinates current coordinates of the car
	 * @return reward as a function of distance to the aim. If transition to currentCoordniates
	 * is not possible or energy level of cart is smaller or equal 0, return -1.
	 */
	public double getRewardLinear(AngleXYCoordinate currentCoordinates)
	{
		////System.out.println("get Reward?");
		//Am currently not sure why a validTransition is checked here
		//maybe to make method more reliable and independent of calling order
		
		//if(validTransition(currentCoordinates) && car.getEneryLevel() > 0)
		if( validTransition(currentCoordinates) )
		{
			//calculate distance to maximal reward
			double dx = currentCoordinates.x - POSITIONMAXLINEARREWARD_X;
			double dy = currentCoordinates.y - POSITIONMAXLINEARREWARD_Y;
			double distance = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));

			//calculate maximal distance from reward, maximal distance is in one of the four corners
			double dx_temp, dy_temp, distance_temp, distance_max = 0;
			
			//(0,0)
			dx_temp = POSITIONMAXLINEARREWARD_X - 0.0;
			dy_temp = POSITIONMAXLINEARREWARD_Y - 0.0;
			distance_temp = Math.sqrt(Math.pow(dx_temp,2)+Math.pow(dy_temp,2));
			distance_max = distance_temp;
			
			//(ENVIRONMENT_WIDTH,0)
			dx_temp = POSITIONMAXLINEARREWARD_X - ENVIRONMENT_WIDTH;
			dy_temp = POSITIONMAXLINEARREWARD_Y - 0.0;
			distance_temp = Math.sqrt(Math.pow(dx_temp,2)+Math.pow(dy_temp,2));
			if(distance_temp > distance_max)
			{
				distance_max = distance_temp;
			}
			
			//(0,ENVIRONMENT_HEIGHT)
			dx_temp = POSITIONMAXLINEARREWARD_X - 0.0;
			dy_temp = POSITIONMAXLINEARREWARD_Y - ENVIRONMENT_HEIGHT;
			distance_temp = Math.sqrt(Math.pow(dx_temp,2)+Math.pow(dy_temp,2));
			if(distance_temp > distance_max)
			{
				distance_max = distance_temp;
			}
			
			//(ENVIRONMENT_WIDTH,ENVIRONMENT_HEIGHT)
			dx_temp = POSITIONMAXLINEARREWARD_X - ENVIRONMENT_WIDTH;
			dy_temp = POSITIONMAXLINEARREWARD_Y - ENVIRONMENT_HEIGHT;
			distance_temp = Math.sqrt(Math.pow(dx_temp,2)+Math.pow(dy_temp,2));
			if(distance_temp > distance_max)
			{
				distance_max = distance_temp;
			}
			
			// if distance == distance_max, return 0
			// if distance == 0, 			return 1
			return (1-distance/distance_max);		
		}
		
		//System.out.println("Reward = -1.0");
		return -1.0;	//negative reward
	}

	/**
	 *  Method returns the reward for entering/trying to enter a new state
	 *  
	 * @param currentCoordinates
	 * @return
	 */
	public double getRewardRewardArea(AngleXYCoordinate currentCoordinates)
	{
		////System.out.println("get Reward?");
		//Am currently not sure why a validTransition is checked here
		//maybe to make method more reliable and independent of calling order
		if(validTransition(currentCoordinates))
		{

			for( int i = 0; i <myRewardAreas.size(); i++ )
	    	{
				System.out.println("i:" + i +" : " + currentCoordinates.toString());
				if(myRewardAreas.get(i).pointInSquare(car.getBorders(currentCoordinates)))
	    		{
	    			System.out.println("i:" + i +" : " + myRewardAreas.get(i).getValue());
	    			return myRewardAreas.get(i).getValue();
	    		}
	    	}
			//			for( int i = 0; rewardAreas != null && i <rewardAreas.length; i++ )
//	    	{
//				// check if reward area was reached
//				// car can only be in one area, thus returning value in for-loop is ok here
//				if(rewardAreas[i].pointInSquare(car.getBorders(currentCoordinates)))
//				{
//					return rewardAreas[i].getValue();							
//				}											
//	    	}
		}
		////System.out.println("Reward = 0.0");	//no reward
		return 0.0; //no reward
	}
	
	public double getRewardOpponent(AngleXYCoordinate currentCoordinates)
	{
		////System.out.println("get Reward?");
		//Am currently not sure why a validTransition is checked here
		//maybe to make method more reliable and independent of calling order
		if(validTransition(currentCoordinates))
		{

			for( int i = 0; i <myOpponents.size(); i++ )
	    	{
				System.out.println("i:" + i +" : " + currentCoordinates.toString());
				if(myOpponents.get(i).pointInSquare(car.getBorders(currentCoordinates)))
	    		{
	    			System.out.println("i:" + i +" : " + myOpponents.get(i).getValue());
	    			return myOpponents.get(i).getValue();
	    		}
	    	}
		}
		////System.out.println("Reward = 0.0");	//no reward
		return 0.0; //no reward
	}
	
	
	
	
	/**
	 * Check if car has entered an action square, e.g. is refueled
	 * 
	 * @param currentCoordinates
	 * @param car
	 * @return
	 */
	public void doActionOnSquares(AngleXYCoordinate currentCoordinates, ICar car)
	{
		if(validTransition(currentCoordinates))
		{
			//check if the action square has to do something with the car
			for( int i = 0; actionArea != null && i <actionArea.length; i++ )
	    	{
				actionArea[i].doActionIfInSquare(car, currentCoordinates);							
	    	}
		}
	}
	
	
	/**
	 * Returns a value not equal to zero, if agent is in a state with a reward > 0 
	 * @param 	AngleXYCoordinate currentCoordinates current position in environment
	 * @return 	1 if getReward(currentCoordinates) > 0 else false
	 */
	public int aimReached(AngleXYCoordinate currentCoordinates)
	{
		return (getRewardRewardArea(currentCoordinates) > 0)?1:0;
	}
	
	/**
	 * Get x-coordinate of car  
	 * @return 	x-coordinate 
	 */
	public int getCurrentXposition(){
		////System.out.println("Environment current_x: "CURRENTREWARD+ current_x);
		return (int)this.myCoordinates.x;
	}
	
	/**
	 * Get y-coordinate of car  
	 * @return 	y-coordinate 
	 */
	public int getCurrentYposition(){
		////System.out.println("Environment current_y: "+ current_y);
		return (int) this.myCoordinates.y;
	}
	
	/**
	 * Set Environment to a defined state.  
	 * @param 	x x-coordinate
	 * @param 	y y-coordinate
	 * @param 	radius direction of car relative to x-axis, e.g. radius = PI/2 vertical in positive y-direction; radius = 0, horizontal in x-positive direction 
	 */
	public void setEnvironment(AngleXYCoordinate axyCoordinate)
	{	
		myCoordinates.x=axyCoordinate.x;
		myCoordinates.y=axyCoordinate.y;
		myCoordinates.angle=axyCoordinate.angle;
	}
	
	public void addDeltaPosition(AngleXYCoordinate axyCoordinate)
	{	
		myCoordinates.add(axyCoordinate);
	}

	/*
	public void setToRandomState()CURRENTREWARD
	{
		double randNumber = Math.random();
		//setEnvironment(randNumber * WIDTH, randNumber * HIGHT,0);
	}
	*/
	public boolean validTransition(AngleXYCoordinate currentPosAgent)
	{
		//check if current move is possible
		if(outsideEnvironment(currentPosAgent) || crashedIntoObstacle(currentPosAgent))
		{
			//System.out.println("******************* M O V E   N O T   P O S S I B L E *****************");
			return false;
		}
		return true;
	}
	
	public boolean outsideEnvironment(AngleXYCoordinate currentPosAgent)
	{
		XYCoordinate[][] bordersCar = car.getBorders(currentPosAgent);	//pointer to border of car
		
		for( int i = 0; i <bordersCar.length; i++ ){ //check if any point of the car borders is outside 
    		for(int j = 0; j<bordersCar[i].length; j++)
    		{
    			if( bordersCar[i][j].x <= 0 || bordersCar[i][j].x >= ENVIRONMENT_WIDTH ||
    				bordersCar[i][j].y <= 0 || bordersCar[i][j].y >= ENVIRONMENT_HEIGHT)
    			{
    				//System.out.println("------------------ O U T S I D E --------------------");    				
    				return true;	
    			}
    			////System.out.println("bordersCar[" + i + "][" + j + "].x:" + bordersCar[i][j].x + "  bordersCar[" + i + "][" + j + "].y:" + bordersCar[i][j].y);
    		}
    	}
		return false;		//no point of the car border is outside  
	}
	
	public boolean crashedIntoObstacle(AngleXYCoordinate currentPosAgent)
	{
		for( int i = 0; i <myObstacles.size(); i++ )
    	{
    		if(myObstacles.get(i).pointInSquare(car.getBorders(currentPosAgent)))
    		{
    			System.out.println("------------C R A S H E D  I N  O B S T A C L E --------------");
    			return true;
    		}
    	}
		return false; //not crashed into obstacle
	}
	
	
	 public String EnvironmentCommunication(String inputLine) 
	 {            	    	

		String outputLine;

		outputLine = processInput(inputLine);
		System.out.println("["+ System.currentTimeMillis() +"] outputLine:"  + outputLine);
		
		//sendData(outputLine);
		if (outputLine.equals("Bye."))
		{
			return null;
		}
		if (outputLine.equals("Restart"))
		{
			return processInput("CURPOS");			//restart client
		}
		if ( outputLine == "CarRan" || outputLine == "CarRam" || outputLine == "Restart" || outputLine == "CarSta")
		{
			return processInput("CURPOS");
		}
		return outputLine;
		        
	 }
	 
	 /************** only for testing *******************/
	 /**
	  * Use settings/objects from current environment to update laser measurements 
	  */
	 
	 public double[] triggerLaserObstacles()
	 {
		 return lasers[0].meassure(myCoordinates, myObstacles);
	 }
	
	 /**
	  * Use settings/objects from current environment to update laser measurements 
	  */
	 public double[] triggerLaserOpponents()
	 {
//		 return lasers[1].meassure(myCoordinates, myOpponents);
		 return lasers[1].meassure(myCoordinates, myObstacles);
	 }
	
	 
	 
	 /**
	 * Method which processes the string theInput which is usually received by the server.
	 * An output string  with the following format is returned:
	 * x-coordinates[0..maxX]:y-coordinates[0..maxY]:orientation[0..2pi]:reward[0..1]:energy[0..1]   
	 * 
	 * @param 	theInput received String.
	 * @see 	Environment()
	 * @return 	String containing information from the environment. Parameters are seperated by ":"'s
	 * x-coordinates[0..maxX]:y-coordinates[0..maxY]:orientation[0..2pi]:reward[0..1]:energy[0..1]
	 */
	public String processInput(String theInput) {
//		System.out.println("Environment Command: " + theInput);
		String theOutput = null;
		ticks++;

		theOutput = "unknown command";
		try
		{			 
			if(theInput != null)	//check, otherwise server can crash
			{	   
			    String[] SplitCommand = new String[6];
			    SplitCommand = theInput.split(":");
				
				try{
				    if(SplitCommand[0].equals("ACTION"))	//action received
					{
						//execute IAction
				    	AngleXYCoordinate deltaEnvironment;
				    	int selectedAction = Integer.decode(SplitCommand[1]).intValue();
				    	steps = Double.parseDouble(SplitCommand[2]);

//				    	double speedFactor = 1.0;
//						for (SpeedSquare ss : speedSquare)
//						{
//							if (ss.checkIfPointInSquare(myCoordinates))
//							{
//								speedFactor = ss.getSpeedFactor();
//							}
//						}
				    	
				    	deltaEnvironment = car.executeAction(selectedAction, steps);	//changes due to action				    
				    	
				    	tempCoordinates.init(myCoordinates);	//initialize tempCoordinates with myCoordinates
				    	tempCoordinates.add(deltaEnvironment);	//is move possible?
				    	

				    	System.out.println("from: "+ myCoordinates.toString() + " to:" + tempCoordinates.toString() + "    with steps: " + steps);
				    	
				    	double reward = -1;
				    	if(validTransition(tempCoordinates))
				    	{			    				    		
				    		distanceTravelled = distanceTravelled + deltaEnvironment.getLength();
				    		myCoordinates.add(deltaEnvironment);
				    		setEnvironment(	myCoordinates);
				    		doActionOnSquares(myCoordinates, car);
				    		
				    		trackstate = track.update(myCoordinates);
				    		
				    		//Comment if crossing of goal line is unimportant
				    		//trackstate = Track.TrackStates.OK;
				    		
				    		reward = getReward(tempCoordinates);
				    		//reward = getReward(trackstate) + getReward(tempCoordinates);
				    		distancesObstacles = lasers[0].meassure(myCoordinates, laserAngles, myObstacles);
//				    		distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myOpponents);
				    		distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myObstacles);
				    		laserDistancesObstacles="";
				    		laserDistancesOpponents="";
				    		for(int i =0; i<distancesObstacles.length; i++)
					    	{
				    			laserDistancesObstacles = laserDistancesObstacles + ":" + String.valueOf(distancesObstacles[i]);
					    	}
				    		for(int i =0; i<distancesOpponents.length; i++)
				        	{
				    			laserDistancesOpponents = laserDistancesOpponents + ":" + String.valueOf(distancesOpponents[i]);
				        	}
				    		
//				    		double[] testDistancesObstacles0 = laserObstacleConnections[0].laser.meassure(myCoordinates, laserAngles, laserObstacleConnections[0].obstacle);
//				    		double[] testDistancesObstacles1 = laserObstacleConnections[1].laser.meassure(myCoordinates, laserAngles, laserObstacleConnections[1].obstacle);
//				    		assert Arrays.equals(testDistancesObstacles0, distancesObstacles);	
//				    		assert Arrays.equals(testDistancesObstacles1, distancesOpponents);	
				    		
				    	}
				    	// the (real)current position of the car is a state of the environment
				    	// the energy level is a property of the car
				    	//theOutput = myCoordinates.x + ":" + myCoordinates.y + ":"+ getOrientation()+ ":" + getRewardSquare(tempCoordinates) + ":" + car.getEneryLevel();
				    	theOutput = myCoordinates.x + ":" + myCoordinates.y + ":"
				    				+ getOrientation()+ ":" + reward + ":" + car.getEneryLevel() 
				    				+ ":" + distanceTravelled + ":" + trackstate + ":" + steps+ ":" + ticks + laserDistancesObstacles + laserDistancesOpponents;
				    	
				    	
				    	
					}
				    else if(SplitCommand[0].equals("CURPOS"))	//action received
					{					
				    	//return current position and reward
				    	//theOutput = myCoordinates.x + ":" + myCoordinates.y + ":" 
				    	//			+ getOrientation() + ":" + getRewardSquare(tempCoordinates) 
				    	//			+ ":" + car.getEneryLevel();
				    	trackstate = track.update(myCoordinates);
				    	
				    	distancesObstacles = lasers[0].meassure(myCoordinates, laserAngles, myObstacles);
//				    	distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myOpponents);
				    	distancesOpponents = lasers[1].meassure(myCoordinates, laserAngles_opponent, myObstacles);
			    		laserDistancesObstacles="";
			    		laserDistancesOpponents="";
			    		for(int i =0; i<distancesObstacles.length; i++)
				    	{
			    			laserDistancesObstacles = laserDistancesObstacles + ":" + String.valueOf(distancesObstacles[i]);
				    	}
			    		for(int i =0; i<distancesOpponents.length; i++)
			        	{
			    			laserDistancesOpponents = laserDistancesOpponents + ":" + String.valueOf(distancesOpponents[i]);
			        	}
			    		
			    		//14.01.2016 using tempCoordinate for reward does not make sense, was there a reason?
				    	theOutput = myCoordinates.x + ":" + myCoordinates.y + ":" 
//	    							+ getOrientation() + ":" + getReward(tempCoordinates)
    								+ getOrientation() + ":" + getReward(myCoordinates)
	    							+ ":" + car.getEneryLevel()  + ":" + distanceTravelled + ":" + trackstate + ":" + steps + ":" + ticks+ laserDistancesObstacles + laserDistancesOpponents;
				    	//System.out.println("CURPOS:" + theOutput );
					}
				    else if(SplitCommand[0].equals("CARPOS"))	//action received
					{					
				    	//set car to position
				    	AngleXYCoordinate axy = new AngleXYCoordinate(
				    			Double.valueOf(SplitCommand[1]).doubleValue(),
				    			Double.valueOf(SplitCommand[2]).doubleValue(),
				    			Double.valueOf(SplitCommand[3]).doubleValue()
				    			);
				    	setEnvironment(axy);
				    	//return current position and reward
				    	//theOutput = myCoordinates.x + ":" + myCoordinates.y + ":" + getOrientation()+ ":" + getRewardSquare(tempCoordinates)+ ":" + car.getEneryLevel();
				    	theOutput = myCoordinates.x + ":" + myCoordinates.y + ":"
//14.01.2016 using tempCoordinate for reward does not make sense, was there a reason?				    			
//			    				+ getOrientation()+ ":" + getReward(tempCoordinates) + ":"
								+ getOrientation()+ ":" + getReward(axy) + ":"
			    				+ car.getEneryLevel() + ":" + distanceTravelled + ":" + trackstate+ ":" + steps +":" + ticks + laserDistancesObstacles + laserDistancesOpponents;

				    	paint(this.getGraphics());
					}
				    else if(SplitCommand[0].equals("RESTAR"))	//action received
					{
				    	theOutput = "Restart";
					}
				    else if(SplitCommand[0].equals("CARSTA"))	//action received
					{
				    	//set car to starting position
				    	//rewardMoveForeward = 0.0;
				    	System.out.println("setEnvironment From: x:" + myCoordinates.x + "  y:" + myCoordinates.y + "  angle:" + myCoordinates.angle);
				    	myCoordinates.x=ENVIRONMENT_WIDTH/2+10; // + STEPSIZE/2;
						myCoordinates.y=ENVIRONMENT_WIDTH/2-50; // + STEPSIZE/2;
						myCoordinates.angle = Math.PI/2;
						System.out.println("setEnvironment To: x:" + myCoordinates.x + "  y:" + myCoordinates.y + "  angle:" + myCoordinates.angle);
				    	setEnvironment(	myCoordinates );
						theOutput = "CarSta";
						paint(this.getGraphics());
					}
				    else if(SplitCommand[0].equals("CARRAM"))	//action received
					{
				    	//set car to random starting position in the middle of the environment
				    	//Car should not be set close to the boarders
				    	System.out.println("setEnvironment From: x:" + myCoordinates.x + "  y:" + myCoordinates.y + "  angle:" + myCoordinates.angle);
				    	double high = ENVIRONMENT_WIDTH * 0.8;
				    	double low = ENVIRONMENT_WIDTH * 0.2;
				    	myCoordinates.x= Math.random() * (high - low) + low;
						myCoordinates.y= Math.random() * (high - low) + low;
						myCoordinates.angle = 2*Math.PI * Math.random();
						System.out.println("setEnvironment To: x:" + myCoordinates.x + "  y:" + myCoordinates.y + "  angle:" + myCoordinates.angle);
						
				    	setEnvironment(	myCoordinates );
						theOutput = "CarRam";
						paint(this.getGraphics());
					}
				    else if(SplitCommand[0].equals("RACRES"))	//action received
					{
				    	//set car to starting position 
				    	//rewardMoveForeward = 0.0;
				    	System.out.println("setEnvironment From: x:" + myCoordinates.x + "  y:" + myCoordinates.y + "  angle:" + myCoordinates.angle);
				    	//setEnvironment(	track.getStartingPosition(false) );
				    	setEnvironment(	track.getStartingPosition(30, -1) );
				    	track.restart();
				    	System.out.println("setEnvironment To: x:" + track.getStartingPosition().x + "  y:" + track.getStartingPosition().y + "  angle:" + track.getStartingPosition().angle);
				    	theOutput = "CarRam";
						paint(this.getGraphics());
					}
				    else if(SplitCommand[0].equals("CARRAN"))	//action received
					{
				    	//set car to random starting position
				    	myCoordinates.x=ENVIRONMENT_WIDTH * Math.random(); 
						myCoordinates.y=ENVIRONMENT_WIDTH * Math.random();
						myCoordinates.angle = 2*Math.PI * Math.random();
				    	
				    	setEnvironment(	myCoordinates );
						theOutput = "CarRan";
						paint(this.getGraphics());
					}
					else if(SplitCommand[0].equals("BYEBYE"))	//action received
					{
						//return current position
						theOutput = "Bye.";
					}
			    }
			    catch(IndexOutOfBoundsException e)
			    {
			    	System.out.println("Command triggered exception" + e.toString());
			    	theOutput = "Command triggered exception";
			    	//Where does execution continue? is the Output returned?
			    }
			}
		}
		catch(Exception e)
		{
			System.out.print(e.toString());
			System.exit(-1);
		}
		//System.out.println("theOutput: " + theOutput);
		return theOutput;

	}
	
	/**
	 * @return angle describing the direction of car relative to x-axis, e.g. radius = PI/2 vertical in positive y-direction; radius = 0, horizontal in x-positive direction
	 */
	public double getOrientation(){
		if(myCoordinates.angle > 2*Math.PI)
			myCoordinates.angle = myCoordinates.angle - 2*Math.PI;
		if(myCoordinates.angle < 0)
			myCoordinates.angle = myCoordinates.angle + 2*Math.PI;
		return myCoordinates.angle;
	}
	
	public void InitGui()
	{			
		//initialize current situation  
		prev_x = getCurrentXposition();
		prev_y = getCurrentYposition();	
		
		this.setSize(ENVIRONMENT_WIDTH+50, ENVIRONMENT_HEIGHT + 120);
		this.image = new BufferedImage(ENVIRONMENT_WIDTH+50, 
				ENVIRONMENT_HEIGHT + 120,BufferedImage.TYPE_INT_RGB);
		
		this.setAlwaysOnTop(true);	//Frame is always on top
		this.setLocationRelativeTo(null);
		//this.setUndecorated(false);
		this.setLayout(new BorderLayout());
        //setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
     
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(3,2));
		
		//add buttons and TextFields to jPanel
		{
			ActionTextField0 = new JTextField();
			jPanel.add(ActionTextField0);			
			ActionTextField0.setText("energyCosts");	//spaces to increase size of text field in GUI
		}
		{
			buttonSetEnergyCostsPerAction = new JButton();
			jPanel.add(buttonSetEnergyCostsPerAction);
			//buttonStart.setHorizontalAlignment(SwingConstants.LEFT);
			buttonSetEnergyCostsPerAction.setText("Set energy cost");
			buttonSetEnergyCostsPerAction.setBounds(20, 20, 50, 50);	//Bounds are used when position in BorderLayout is already occupied
			buttonSetEnergyCostsPerAction.setActionCommand("Set energy cost");
			buttonSetEnergyCostsPerAction.addActionListener(this);
		}
		{
			ActionTextField1 = new JTextField();
			jPanel.add(ActionTextField1);			
			ActionTextField1.setText("1");	//spaces to increase size of text field in GUI
			//iterTextField.setBounds(25, 50, 50, 10);
			//angleTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		//Start Button zum JFrame hinzugef���gt und das TextFeld zum JPanel
		// scheint zu klappen (?)
		{
			buttonStart = new JButton();
			jPanel.add(buttonStart);
			//buttonStart.setHorizontalAlignment(SwingConstants.LEFT);
			buttonStart.setText("Test");
			buttonStart.setBounds(20, 20, 50, 50);	//Bounds are used when position in BorderLayout is already occupied
			buttonStart.setActionCommand("Start");
			buttonStart.addActionListener(this);
		}
		{
			this.updateDrawing = new JCheckBox();
			jPanel.add(updateDrawing);
			updateDrawing.setText("Update drawing");
			updateDrawing.setSelected(true);
			updateDrawing.addActionListener(this);
		}
		this.getContentPane().add(jPanel, BorderLayout.SOUTH);
	}
   
    public void actionPerformed(ActionEvent evt)
    {
    	paint(this.getGraphics());
    	Car car = new Car(this);
    	
    	if (evt.getActionCommand().equals("Start")) 
		{
    		car.executeAction(0);
		}  
    	
    	if (evt.getActionCommand().equals("Set energy cost")) 
		{
    		double value=Double.valueOf(ActionTextField0.getText().trim());
    		car.setEneryCosts(value);
    		//System.out.println("Set energy costs to: " + value);
		}
	}
    
    public void setLaserAngles(double[] laserAngles)
    {
    	this.laserAngles = laserAngles;
    }
    
    public void paintComponent()
	{
		//System.out.println("paintComponent");
    	paint(this.getGraphics());
	}
    
	public void paint(Graphics g)
    {        
		if(this.updateDrawing.isSelected())
		{
		//only called if window has to be redrawn, e.g. if window is moved
		Graphics g2 = image.getGraphics();		
    	super.paint(g2);  //repaints complete frame
        this.draw(g2);
        g.drawImage(image, 0,0,null);
		}
    }
	
	private void draw(Graphics g)
    {
		g.setColor(Color.black);		
		g.drawRect (SHIFT_X,SHIFT_Y,ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT);
        PlotAreas(g);	//first, Plot Obstacles and Reward areas
        PlotCar(g);		//then plot car
        PlotLaser(g);	// plot laser beams
    }
	
	private void PlotLaser(Graphics g)
	{					
		////System.out.println("drawing line from: " + prev_x + ", "+ prev_y +  " to: " + getCurrentXposition() + ", "+ getCurrentYposition());
		Color[] color = {Color.RED, Color.GREEN};
		int usedColor = 0;

		for(Laser l: lasers)
		{
			//copy array, it seems laser data can get changed while plotting -> ConcurrenyException
			Vector<XYCoordinate> laserBeam_temp = new Vector<XYCoordinate>(l.laserBeam);
			g.setColor(color[usedColor]);
			for(XYCoordinate point: laserBeam_temp)
			{
				g.drawOval(SHIFT_X +(int)point.x, SHIFT_Y+(int)point.y,1,1);
			}
			usedColor++;
			
		}
//		Vector<XYCoordinate> laserBeam1_temp = new Vector<XYCoordinate>(lasers[0].laserBeam);
//		Vector<XYCoordinate> laserBeam2_temp = new Vector<XYCoordinate>(lasers[1].laserBeam);
//		g.setColor(Color.RED);
//		for(XYCoordinate point: laserBeam1_temp)
//		{
//			g.drawOval(SHIFT_X +(int)point.x, SHIFT_Y+(int)point.y,1,1);
//		}
//		
//		g.setColor(Color.GREEN);
//		for(XYCoordinate point: laserBeam2_temp)
//		{
//			g.drawOval(SHIFT_X +(int)point.x, SHIFT_Y+(int)point.y,1,1);
//		}
		
		//Vector<XYCoordinate> laserBeams = new Vector<XYCoordinate>();
		//laserBeams.addAll(lasers[0].laserBeam);
		//laserBeams.addAll(lasers[1].laserBeam); 
	}
	
	private void PlotCar(Graphics g)
	{					
		int currentX = getCurrentXposition();
		int currentY = getCurrentYposition();
		////System.out.println("drawing line from: " + prev_x + ", "+ prev_y +  " to: " + getCurrentXposition() + ", "+ getCurrentYposition());
		g.setColor(Color.BLACK);
		g.drawLine(SHIFT_X + prev_x,SHIFT_Y + prev_y,SHIFT_X + currentX, SHIFT_Y + currentY);
		g.drawOval(SHIFT_X + currentX-1,SHIFT_Y + currentY-1, 2, 2);
		g.drawOval(SHIFT_X + prev_x -2,SHIFT_Y + prev_y -2,4,4);
		
		/*XYCoordinate[][] bordersCar = car.getBorders(myCoordinates); //Get coordinates of car edges
		
		//plot coordinates/points; result is the plotted car
		for(int i = 0; i<bordersCar[0].length; i++)
		{
			g.drawLine(SHIFT_X + (int)bordersCar[0][i].x, SHIFT_Y + (int)bordersCar[0][i].y, SHIFT_X + (int)bordersCar[0][i].x, SHIFT_Y + (int)bordersCar[0][i].y );
			g.drawLine(SHIFT_X + (int)bordersCar[1][i].x, SHIFT_Y + (int)bordersCar[1][i].y, SHIFT_X + (int)bordersCar[1][i].x, SHIFT_Y + (int)bordersCar[1][i].y );
		}
		*/
		XYCoordinate[] cornersCar = car.getCorners(myCoordinates); //Get corners of car
		g.drawLine(SHIFT_X + (int)cornersCar[0].x, SHIFT_Y + (int)cornersCar[0].y,SHIFT_X + (int)cornersCar[1].x, SHIFT_Y + (int)cornersCar[1].y);
		g.drawLine(SHIFT_X + (int)cornersCar[1].x, SHIFT_Y + (int)cornersCar[1].y,SHIFT_X + (int)cornersCar[2].x, SHIFT_Y + (int)cornersCar[2].y);
		g.drawLine(SHIFT_X + (int)cornersCar[2].x, SHIFT_Y + (int)cornersCar[2].y,SHIFT_X + (int)cornersCar[0].x, SHIFT_Y + (int)cornersCar[0].y);
		
		prev_x = currentX;
		prev_y = getCurrentYposition();				
	}
	
	private void PlotAreas(Graphics g)
	{
//		//draw track
//		g.setColor(Color.RED);
//		for(TrackElement t : track.getTrack())
//		{
//			for(VSquare v: t.getTrackElements())
//				g.fill3DRect(	SHIFT_X + (int)v.getX0(), SHIFT_Y + (int)v.getY0() ,
//								(int)v.getWidth(), (int)v.getHeight(),true	);
//
//		}
		//draw obstacles
		g.setColor(Color.GRAY);
		for(int i = 0; i < myObstacles.size(); i++)
			g.fill3DRect(	SHIFT_X + (int)myObstacles.get(i).getX0(), SHIFT_Y + (int)myObstacles.get(i).getY0() ,
							(int)myObstacles.get(i).getWidth(), (int)myObstacles.get(i).getHeight(),true	);
		
		//draw obstacles
		g.setColor(Color.YELLOW);
		for(int i = 0; i < myOpponents.size(); i++)
			g.fill3DRect(	SHIFT_X + (int)myOpponents.get(i).getX0(), SHIFT_Y + (int)myOpponents.get(i).getY0() ,
							(int)myOpponents.get(i).getWidth(), (int)myOpponents.get(i).getHeight(),true	);
		
		//draw reward areas
		g.setColor(Color.BLUE);		
		for( int i = 0; i <myRewardAreas.size(); i++ )
    	{
			//plot reward areas
			g.fill3DRect(	SHIFT_X + (int)myRewardAreas.get(i).getX0(), SHIFT_Y + (int)myRewardAreas.get(i).getY0() ,
						(int)myRewardAreas.get(i).getWidth(), (int)myRewardAreas.get(i).getHeight(),true	);
    	}
		
		//draw action areas
		g.setColor(Color.GREEN);		
		for( int i = 0; i <actionArea.length; i++ )
    	{
			//plot reward areas
			g.fill3DRect(	SHIFT_X + (int)actionArea[i].getX0(), SHIFT_Y + (int)actionArea[i].getY0() ,
						(int)actionArea[i].getWidth(), (int)actionArea[i].getHeight(),true	);
    	}
		
		//draw speedSquare
		g.setColor(Color.YELLOW);		
		for( int i = 0; i < speedSquare.length; i++ )
    	{
			//plot reward areas
			g.fill3DRect(	SHIFT_X + (int)speedSquare[i].getX0(), SHIFT_Y + (int)speedSquare[i].getY0() ,
						(int)speedSquare[i].getWidth(), (int)speedSquare[i].getHeight(),true	);
    	}
	}
}
