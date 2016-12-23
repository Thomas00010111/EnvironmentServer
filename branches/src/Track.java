import java.util.Vector;
//import java.math.*;

/** 
 * Definition of rectangle square, defined by the upper left and the 
 * lower right corner.
 */

public class Track 
{
	public static enum TrackStates {NONE, OK, ERROR, CROSSED, CROSSED_GOAL_WRONG_DIRECTION} 
	public static enum TrackType {ROUND_TRACK, OPEN_TRACK, STRAIGHT_TRACK} 
	
	private static enum CrossedGoalLine {CROSSED, NOT_CROSSED, CROSSED_WRONG_DIRECTION}
	private final double RewardCrash = -1.0;
	private int BarrierWidth = 10, max =200, ObstacleWidth = 10;
	private int trackwidth = -1;
	
	private Vector<TrackElement> track = new Vector<TrackElement>();
	Vector<VSquare> trackObstacles = new Vector<VSquare>();
	private int  initPositionTrackElement = -1;
	private AngleXYCoordinate startGoalPosition, prevPosition;

	
	public Track()
	{
		//Trackwidth = 50
		this(50, TrackType.ROUND_TRACK);
	}
	
	public Track(TrackType tracktype)
	{
		//Trackwidth = 50
		this(50, tracktype);
	}
	
	public Track(int Trackwidth, TrackType tracktype)
	{
		if (tracktype == TrackType.ROUND_TRACK)
		{
			track =  generateRoundTrack(Trackwidth);
		}
		else if(tracktype == TrackType.OPEN_TRACK)
		{
			track =  generateOpenTrack();
		}
		else if(tracktype == TrackType.STRAIGHT_TRACK)
		{
			track =  generateStraightTrack();
		}
		
//		VSquare obstacle1 = new VSquare(BarrierWidth, BarrierWidth, BarrierWidth + ObstacleWidth, BarrierWidth + ObstacleWidth, RewardCrash);
//		trackObstacles.add(obstacle1);
//		
//		VSquare obstacle2 = new VSquare(BarrierWidth, max-(BarrierWidth+ObstacleWidth), BarrierWidth + ObstacleWidth, max-BarrierWidth, RewardCrash);
//		trackObstacles.add(obstacle2);
//		
//		VSquare obstacle3 = new VSquare(max-(BarrierWidth + ObstacleWidth),BarrierWidth ,max-BarrierWidth, BarrierWidth+ObstacleWidth, RewardCrash);
//		trackObstacles.add(obstacle3);
//		
//		VSquare obstacle4 = new VSquare(max-(BarrierWidth + ObstacleWidth), max-(BarrierWidth + ObstacleWidth), max-BarrierWidth, max-BarrierWidth, RewardCrash);
//		trackObstacles.add(obstacle4);

	}
	
	private Vector<TrackElement> generateRoundTrack(int Trackwidth)
	{
		trackwidth = Trackwidth;
		Vector<TrackElement> track = new Vector<TrackElement>();
		//left vertical
		VSquare barrier2 = new VSquare(0, 0, BarrierWidth, max, RewardCrash);
		VSquare barrier3 = new VSquare(BarrierWidth + Trackwidth, BarrierWidth + Trackwidth, BarrierWidth + Trackwidth + BarrierWidth, max - (BarrierWidth + Trackwidth), RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier2, barrier3, "left vertical"));
		
		//bottom
		VSquare barrier4 = new VSquare(BarrierWidth + Trackwidth, max - (BarrierWidth + BarrierWidth + Trackwidth), max - ( BarrierWidth + Trackwidth), max - (BarrierWidth + Trackwidth), RewardCrash);
		VSquare barrier5 = new VSquare(0, max-BarrierWidth, max, max, RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier4, barrier5));
		
		//right vertical
		VSquare barrier6 = new VSquare(max - (Trackwidth+BarrierWidth+BarrierWidth), BarrierWidth + Trackwidth, max - ( BarrierWidth + Trackwidth), max - (BarrierWidth + Trackwidth), RewardCrash);
		VSquare barrier7 = new VSquare(max-BarrierWidth, 0, max, max, RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier6, barrier7));
		
		//top horizontal
		VSquare barrier0 = new VSquare(0, 0, max, BarrierWidth, RewardCrash);
		VSquare barrier1 = new VSquare(BarrierWidth + Trackwidth, BarrierWidth + Trackwidth, max - (BarrierWidth + Trackwidth), BarrierWidth + Trackwidth + BarrierWidth, RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier0, barrier1));
		
		return track;
	}
	
	private Vector<TrackElement> generateOpenTrack()
	{
		trackwidth = max - BarrierWidth;
		Vector<TrackElement> track = new Vector<TrackElement>();
		//left vertical
		VSquare barrier2 = new VSquare(0, 0, BarrierWidth, max, RewardCrash);
		VSquare barrier3 = new VSquare(max-BarrierWidth, 0, max, max, RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier2, barrier3));
		

		//bottom
		VSquare barrier0 = new VSquare(0, max-BarrierWidth, max, max, RewardCrash);
		VSquare barrier1 = new VSquare(0, 0, max, BarrierWidth, RewardCrash);
		track.add(new TrackElement(barrier0, barrier1));

		
		return track;
	}
	
	private Vector<TrackElement> generateStraightTrack()
	{
		trackwidth = max - BarrierWidth;
		Vector<TrackElement> track = new Vector<TrackElement>();
		//left vertical
		VSquare barrier2 = new VSquare(0, 0, BarrierWidth, max, RewardCrash);
		VSquare barrier3 = new VSquare(max-BarrierWidth, 0, max, max, RewardCrash);
		// We need four track elements
		track.add(new TrackElement(barrier2, barrier3));
		

		//bottom
		VSquare barrier0 = new VSquare(0, max-BarrierWidth, max, max, RewardCrash);
		VSquare barrier1 = new VSquare(0, 0, max, BarrierWidth, RewardCrash);
		track.add(new TrackElement(barrier0, barrier1));
		
		
		//a vertical barrier
		int startHorizontalB4 = 60, endVerticalB4 = 110;
		int startHorizontalB5 = 120, startVerticalB5 = 90, endVerticalB5 = max-BarrierWidth;
		VSquare barrier4 = new VSquare(startHorizontalB4, BarrierWidth, startHorizontalB4+BarrierWidth, endVerticalB4, RewardCrash);
		VSquare barrier5 = new VSquare(startHorizontalB5, startVerticalB5, startHorizontalB5+BarrierWidth, max-BarrierWidth, RewardCrash);
		track.add(new TrackElement(barrier4, barrier5));
		
		return track;
	}
	
	private AngleXYCoordinate getRandomRestartPos()
	{
		return getRandomRestartPos(-1, -1);
	}
	
	private AngleXYCoordinate getRandomRestartPos(int x, int y)
	{
		AngleXYCoordinate randomPosition  = new AngleXYCoordinate();
		//Actually we have to distinguish between open and round track
		boolean notOnTrackInnerX, notOnTrackInnerY;
		boolean notOnTrackOuterX, notOnTrackOuterY;
		
		if(x < 0)
		{
			do
			{
				randomPosition.x = Math.random() * max;
				notOnTrackOuterX = randomPosition.x < BarrierWidth || randomPosition.x > max-BarrierWidth;
				boolean first = randomPosition.x < (max-BarrierWidth - trackwidth);
				boolean second = (randomPosition.x > (BarrierWidth + trackwidth));
				notOnTrackInnerX = first && second;
			}while(notOnTrackInnerX || notOnTrackOuterX); 
		}
		else
		{
			randomPosition.x = x;
		}
		
		if(y < 0)
		{
			do
			{
				randomPosition.y = Math.random() * max;
				notOnTrackOuterY = randomPosition.y < BarrierWidth || randomPosition.y > max-BarrierWidth;
				notOnTrackInnerY = (randomPosition.y < (max-BarrierWidth - trackwidth)) && (randomPosition.y > (BarrierWidth + trackwidth));
			}while(notOnTrackInnerY || notOnTrackOuterY); 	
		}
		else
		{
			randomPosition.y = y;
		}
		
		return randomPosition;
	}
	
	public Vector<TrackElement> getTrackElements()
	{
		return track;
	}
	
	public Vector<VSquare> getObstacles()
	{
		Vector<VSquare> obstacles = new Vector<VSquare>();
		
		for(VSquare o : trackObstacles)
		{
			obstacles.add(o);
		}
		
		for(TrackElement t : track)
		{
			for(VSquare v : t.getTrackElements())
				obstacles.add(v);
		}
		
		return obstacles;
	}
	
	public AngleXYCoordinate getStartingPosition()
	{
		return getStartingPosition(true);
	}
	
	public AngleXYCoordinate getStartingPosition(boolean random)
	{
		if (random)
			return getRandomRestartPos();
		else
			return startGoalPosition;
	}
	
	public AngleXYCoordinate getStartingPosition(int x, int y)
	{
			return getRandomRestartPos(x, y);
	}
	

	
	public TrackStates restart()
	{		
		prevPosition.x = startGoalPosition.x;
		prevPosition.y = startGoalPosition.y;
		prevPosition.angle = startGoalPosition.angle;

		initPositionTrackElement = getTrackElement(startGoalPosition);
		if(initPositionTrackElement == -1)
		{			
			System.out.println("Track:restart: ERROR");
			return TrackStates.ERROR;
		}
		
		return TrackStates.OK;
	}
	
	public TrackStates setStartGoalPosition(AngleXYCoordinate xytheta)
	{
		//we use this position as start/goal position		
		startGoalPosition = new AngleXYCoordinate();
		startGoalPosition.x=	xytheta.x;
		startGoalPosition.y=	xytheta.y;
		startGoalPosition.angle=	xytheta.angle;
		
		prevPosition = new AngleXYCoordinate();
		prevPosition.x = startGoalPosition.x;
		prevPosition.y = startGoalPosition.y;
		prevPosition.angle = startGoalPosition.angle;

		initPositionTrackElement = getTrackElement(xytheta);
		if(initPositionTrackElement == -1)
		{
			return TrackStates.ERROR;
		}
		
		return TrackStates.OK;
	}
	
	private int getTrackElement(AngleXYCoordinate xytheta)
	{
		int trackElement = -1;
		for(int i = 0; i < track.size(); i++)
		{
			if(track.get(i).isInElement(xytheta))
			{
				trackElement = i;
				break;
			}
		}
		return trackElement;
	}
	
	private CrossedGoalLine hasCrossedGoalLine(AngleXYCoordinate prev, AngleXYCoordinate current)
	{
		int currentElement = getTrackElement(current);
		System.out.println("hasCrossedGoalLine: initPositionTrackElement: " + initPositionTrackElement + "     currentElement: " + currentElement + "  prev:" + prev + "    current:" + current);
		
		if(currentElement == initPositionTrackElement)
		{
			boolean crossedWrongDirection = (prev.y >= startGoalPosition.y && current.y < startGoalPosition.y );
			if(crossedWrongDirection) 
				return CrossedGoalLine.CROSSED_WRONG_DIRECTION;
			
			boolean crossed = (prev.y < startGoalPosition.y && current.y >= startGoalPosition.y );
			if(crossed) 
				return CrossedGoalLine.CROSSED;
		}
		
		return CrossedGoalLine.NOT_CROSSED;
	}
	
	public TrackStates update(AngleXYCoordinate xytheta)
	{
		TrackStates state = TrackStates.OK;
		switch(hasCrossedGoalLine(prevPosition, xytheta))
		{
		case CROSSED:
			state = TrackStates.CROSSED;
			break;
		case CROSSED_WRONG_DIRECTION:
			state = TrackStates.CROSSED_GOAL_WRONG_DIRECTION;
			break;
		default:
			//nothing
		}

		prevPosition.x = xytheta.x;
		prevPosition.y = xytheta.y;
		prevPosition.angle = xytheta.angle;
		return state;
	}
}