import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author Thomas Fischle
 */

public class Environment extends JFrame implements ActionListener{
	
	//size and position of reward area
	public final int X_REWARD = 50, Y_REWARD = 30, WIDTH_REWARD = 100, HIGHT_REWARD = 50;
	
	//due to decoration of frame, location of environment has to be moved
	private final int SHIFT_X = 20, SHIFT_Y = 30;
		
	public final int ENVIRONMENT_HIGHT = 200, ENVIRONMENT_WIDTH = 200;	//size of environment
	
	public final int NUMBEROFOBSTACLES = 0;	//Number of obstacles in environment
	
	public final int STEPSIZE = 40;			//length of one step
	double delta_x, delta_y;			//change in environment due to action
	
	int prev_x=0, prev_y=0;
	
	private JButton buttonStart;
	
	Car car = new Car(this);
	
	private Square[] myObstacles = new Square[NUMBEROFOBSTACLES];
	private VSquare rewardArea;
	
	private AngleXYCoordinate myCoordinates = new AngleXYCoordinate();
	private AngleXYCoordinate tempCoordinates = new AngleXYCoordinate();
	
	
	public Environment() {
		//Obstacles
		if(NUMBEROFOBSTACLES == 1)
			myObstacles[0]= new Square(30, 50, 70, 100);
		if(NUMBEROFOBSTACLES == 2)
			myObstacles[1]= new Square(80, 100, 120, 150);
		
		//reward area with xy-coordinates and reward 
		rewardArea = new VSquare(50, 120, 160, 150, 1.0);

		InitGui();
    	setVisible(true);
    	paintComponent();
    	
		// Start: place car in the middle of a state/square
		//if Width/STEPSIZE even number then add half the distance		
		myCoordinates.x=ENVIRONMENT_WIDTH/2+10; // + STEPSIZE/2;
		myCoordinates.y=ENVIRONMENT_WIDTH/2-50; // + STEPSIZE/2;
	}	

	/*
	 * Method returns the reward for entering/trying to enter a new state 
	 */
	public double getReward(AngleXYCoordinate currentCoordinates)
	{
		//System.out.println("get Reward?");
		if(validTransition(currentCoordinates))
		{
			//check if reward area was reached
			if(rewardArea.checkIfPointInSquare(car.getBorders(currentCoordinates)))
			{
				//System.out.println("------------------ R E W A R D --------------------");
				return rewardArea.getValue();
			}
			System.out.println("Reward = 0.0");
			return 0.0; //not reward
		}
		else
		{
			System.out.println("Reward = -1.0");
			return -1.0;	//negative reward
		}
	
	}
	
	/**
	 * Returns a value not equal to zero, if agent is in a state with a reward > 0 
	 * @param 	AngleXYCoordinate currentCoordinates current position in environment
	 * @return 	1 if getReward(currentCoordinates) > 0 else false
	 */
	public int aimReached(AngleXYCoordinate currentCoordinates)
	{
		return (getReward(currentCoordinates) > 0)?1:0;
	}
	
	/**
	 * Get x-coordinate of car  
	 * @return 	x-coordinate 
	 */
	public int getCurrentXposition(){
		//System.out.println("Environment current_x: "+ current_x);
		return (int)this.myCoordinates.x;
	}
	
	/**
	 * Get y-coordinate of car  
	 * @return 	y-coordinate 
	 */
	public int getCurrentYposition(){
		//System.out.println("Environment current_y: "+ current_y);
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

	/*
	public void setToRandomState()
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
			System.out.println("******************* M O V E   N O T   P O S S I B L E *****************");
			return false;
		}		
		else
			return true;
	}
	
	public boolean outsideEnvironment(AngleXYCoordinate currentPosAgent)
	{
		XYCoordinate[][] bordersCar = car.getBorders(currentPosAgent);	//pointer to border of car
		
		for( int i = 0; i <bordersCar.length; i++ ){ //check if any point of the car borders is outside 
    		for(int j = 0; j<bordersCar[i].length; j++)
    		{
    			if( bordersCar[i][j].x <= 0 || bordersCar[i][j].x >= ENVIRONMENT_WIDTH ||
    				bordersCar[i][j].y <= 0 || bordersCar[i][j].y >= ENVIRONMENT_HIGHT)
    			{
    				System.out.println("------------------ O U T S I D E --------------------");    				
    				return true;	
    			}
    			//System.out.println("bordersCar[" + i + "][" + j + "].x:" + bordersCar[i][j].x + "  bordersCar[" + i + "][" + j + "].y:" + bordersCar[i][j].y);
    		}
    	}
		return false;		//no point of the car border is outside  
	}
	
	public boolean crashedIntoObstacle(AngleXYCoordinate currentPosAgent)
	{
		for( int i = 0; i <myObstacles.length; i++ )
    	{
    		if(myObstacles[i].checkIfPointInSquare(car.getBorders(myCoordinates)))
    		{
    			System.out.println("------------------ C R A S H E D --------------------");
    			return true;
    		}
    	}
		return false; //not crashed into obstacle
	}
	
	 /**
	 * Method which processes the string received by the server.  
	 * @param 	theInput received String.
	 * @see 	Environment()
	 * @return 	String that should be returned to the client and/or and
	 *  		action can be taken based on this String.
	 */
	public String processInput(String theInput) {
		String theOutput = null;

		theOutput = "Knock! Knock!";
		 
		if(theInput != null)	//check, otherwise server can crash
		{	   
		    String[] SplitCommand = new String[3];
		    SplitCommand = theInput.split(":");
		    
			
			try{
			    if(SplitCommand[0].equals("ACTION"))	//action received
				{
					//execute Action
			    	AngleXYCoordinate deltaEnvironment;
			    	int selectedAction = Integer.decode(SplitCommand[1]).intValue();
			    	deltaEnvironment = car.executeAction(selectedAction);	//changes due to action
			    	
			    	tempCoordinates.init(myCoordinates);	//initialize tempCoordinates with myCoordinates
			    	tempCoordinates.add(deltaEnvironment);	//is move possible?
			    	System.out.println("from: "+ myCoordinates.toString() + " to:" + tempCoordinates.toString());
			    	
			    	if(validTransition(tempCoordinates))
			    	{			    				    		
			    		myCoordinates.add(deltaEnvironment);
			    		setEnvironment(	myCoordinates);
			    	}
			    		    			
			    	theOutput = myCoordinates.x + ":" + myCoordinates.y + ":" + getReward(tempCoordinates) + ":0";
			    	paint(this.getGraphics());
				}
			    else if(SplitCommand[0].equals("CURPOS"))	//action received
				{					
			    	//return current position and reward
			    	theOutput = myCoordinates.x + ":" + myCoordinates.y + ":" + getReward(tempCoordinates) + ":" + aimReached(tempCoordinates);			 
				}
			    else if(SplitCommand[0].equals("RESTAR"))	//action received
				{
					//return current position
					theOutput = "Restart";
				}
			    else if(SplitCommand[0].equals("CARSTA"))	//action received
				{
			    	//set car to starting position
			    	myCoordinates.x=ENVIRONMENT_WIDTH/2+10; // + STEPSIZE/2;
					myCoordinates.y=ENVIRONMENT_WIDTH/2-50; // + STEPSIZE/2;
					myCoordinates.angle = Math.PI/2;
			    	
			    	setEnvironment(	myCoordinates );
					theOutput = "CarSta";
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
		    	theOutput = "Wrong Command";
		    }
		}
		return theOutput;

	}
	
	/**
	 * @return angle describing the direction of car relative to x-axis, e.g. radius = PI/2 vertical in positive y-direction; radius = 0, horizontal in x-positive direction
	 */
	public double getOrientation(){
		return myCoordinates.angle;
	}
	
	public void InitGui()
	{			
		this.setSize(ENVIRONMENT_WIDTH+50, ENVIRONMENT_HIGHT + 120);
		this.setLocationRelativeTo(null);
		//this.setUndecorated(false);
		this.setLayout(new BorderLayout());
        //setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
     
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());			
		
		//initialize current situation  
		prev_x = getCurrentXposition();
		prev_y = getCurrentYposition();			

		//Start Button zum JFrame hinzugef�gt und das TextFeld zum JPanel
		// scheint zu klappen (?)
		{
			buttonStart = new JButton();
			jPanel.add(buttonStart, BorderLayout.SOUTH);
			//buttonStart.setHorizontalAlignment(SwingConstants.LEFT);
			buttonStart.setText("Test");
			buttonStart.setBounds(20, 20, 50, 50);	//Bounds are used when position in BorderLayout is already occupied
			buttonStart.setActionCommand("Start");
			buttonStart.addActionListener(this);
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
	}
    
    public void paintComponent()
	{
		//System.out.println("paintComponent");
    	paint(this.getGraphics());
	}
    
	public void paint(Graphics g)
    {        
    	//only called if window has to be redrawn, e.g. if window is moved
		//System.out.println("paint()");
    	super.paint(g);  //repaints complete frame
        this.draw(g);
    }
	
	private void draw(Graphics g)
    {
		g.setColor(Color.black);		
		g.drawRect (SHIFT_X,SHIFT_Y,ENVIRONMENT_WIDTH, ENVIRONMENT_HIGHT);
		//PlotRewardArea(g);        
        PlotAreas(g);	//first, Plot Obstacles and Reward areas
        PlotCar(g);		//then plot car
    }
	
	private void PlotCar(Graphics g)
	{					
		int currentX = getCurrentXposition();
		int currentY = getCurrentYposition();
		//System.out.println("drawing line from: " + prev_x + ", "+ prev_y +  " to: " + getCurrentXposition() + ", "+ getCurrentYposition());
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
		g.setColor(Color.GRAY);
		for(int i = 0; i < myObstacles.length; i++)
			g.fill3DRect(	SHIFT_X + (int)myObstacles[i].getX0(), SHIFT_Y + (int)myObstacles[i].getY0() ,
							(int)myObstacles[i].getWidth(), (int)myObstacles[i].getHeight(),true	);
		
		g.setColor(Color.BLUE);
		g.fill3DRect(	SHIFT_X + (int)rewardArea.getX0(), SHIFT_Y + (int)rewardArea.getY0() ,
						(int)rewardArea.getWidth(), (int)rewardArea.getHeight(),true	);
	}
}
