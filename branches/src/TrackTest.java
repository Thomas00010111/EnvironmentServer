import static org.junit.Assert.*;

import org.junit.Test;


public class TrackTest {

	@Test
	public void testCheckCrashedInObstacle() {
		Track track = new Track(20, Track.TrackType.ROUND_TRACK);
		
		XYCoordinate xycoord1 = new XYCoordinate(122.06764831237714, 39.19459119424428);
		int count = 0;
		for(VSquare o : track.getObstacles())
		{
			if(o.checkIfPointInSquare(xycoord1))
					count++;
		}
		System.out.println("count:" + count);
		assertEquals("Must be inside square:", 1,count);

		XYCoordinate xycoord = new XYCoordinate(122.06764831237714, 41.19459119424428);
		for(VSquare o : track.getObstacles())
		{
			//o.checkIfPointInSquare(xycoord);
			assertEquals("Must be outside square:", false, o.checkIfPointInSquare(xycoord));
		}
	}
	
	@Test
	public void testCarInElement() {
		Track track = new Track();
		
		AngleXYCoordinate xycoord1_left = new AngleXYCoordinate(20, 100, 0.0);		// 0th
		AngleXYCoordinate xycoord1_bottom = new AngleXYCoordinate(100, 180, 0.0);	// 1st
		AngleXYCoordinate xycoord1_right = new AngleXYCoordinate(180, 100, 0.0);	// 2nd
		AngleXYCoordinate xycoord1_top = new AngleXYCoordinate(100, 20, 0.0);		// 3rd
		
		TrackElement te1 = track.getTrackElements().get(0);
		System.out.println(te1.description);
		boolean inElement0 = te1.isInElement(xycoord1_left);
		assertEquals("Element left:", true,inElement0);
		
		assertEquals("Element bottom:", true, track.getTrackElements().get(1).isInElement(xycoord1_bottom));
		assertEquals("Element right:", true,track.getTrackElements().get(2).isInElement(xycoord1_right));
		assertEquals("Element top:", true,track.getTrackElements().get(3).isInElement(xycoord1_top));
		
	}
	
	@Test
	public void testGoalBehaviour_crossed_wrong_direction()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);
		
		//move in positive y-direction(down)
		AngleXYCoordinate nextPos = new AngleXYCoordinate(20, 101, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(nextPos));
		
		//move in neg y-direction(up)
		AngleXYCoordinate backwardsPos = new AngleXYCoordinate(20, 99, 0.0);
		assertEquals("valid action return CROSSED_GOAL_WRONG_DIRECTION", Track.TrackStates.CROSSED_GOAL_WRONG_DIRECTION, track.update(backwardsPos));
	}
	
	@Test
	public void testGoalBehaviour_crossed_wrong_direction_smallStep()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);
		
		//move in positive y-direction
		AngleXYCoordinate moveForward = new AngleXYCoordinate(20, 101, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(moveForward));
		
		//move in neg y-direction
		AngleXYCoordinate moveBackward = new AngleXYCoordinate(20, 100, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(moveBackward));
		
		//cross goal in neg y-direction
		AngleXYCoordinate crossGoal = new AngleXYCoordinate(20, 99, 0.0);
		assertEquals("valid action return CROSSED_GOAL_WRONG_DIRECTION", Track.TrackStates.CROSSED_GOAL_WRONG_DIRECTION, track.update(crossGoal));
	}
		
	@Test
	public void testGoalBehaviour_crossed_goal()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);	
		
		//move away from goal line
		AngleXYCoordinate stepOverGoalPos = new AngleXYCoordinate(20, 105, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(stepOverGoalPos));
		
		//move to new Track Element 
		AngleXYCoordinate stepNewTrackElement = new AngleXYCoordinate(100, 20, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(stepNewTrackElement));
		
		//move towards goal line
		AngleXYCoordinate justBeforeGoal = new AngleXYCoordinate(20, 99, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(justBeforeGoal));

		//cross goal line
		AngleXYCoordinate crossedGoal = new AngleXYCoordinate(20, 101, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.CROSSED, track.update(crossedGoal));

	}
	
	@Test
	public void testGoalBehaviour_hit_goal()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);	
		
		//move away from goal line
		AngleXYCoordinate stepOverGoalPos = new AngleXYCoordinate(20, 105, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(stepOverGoalPos));
		
		//move to new Track Element 
		AngleXYCoordinate stepNewTrackElement = new AngleXYCoordinate(100, 20, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(stepNewTrackElement));
		
		//move towards goal line
		AngleXYCoordinate justBeforeGoal = new AngleXYCoordinate(20, 99, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(justBeforeGoal));

		//hit goal line
		AngleXYCoordinate hitGoal = new AngleXYCoordinate(20, 100, 0.0);
		assertEquals("valid action return CROSSED", Track.TrackStates.CROSSED, track.update(hitGoal));
	}
		
	@Test
	public void testGoalBehaviour_immediate_crossed_goal_wrong_direction()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);
		
		//immediately do a step in wrong direction
		AngleXYCoordinate nextPosWrongDir = new AngleXYCoordinate(20, 99, 0.0);
		assertEquals("valid action return CROSSED_GOAL_WRONG_DIRECTION", Track.TrackStates.CROSSED_GOAL_WRONG_DIRECTION, track.update(nextPosWrongDir));
	}
	
	@Test
	public void testGoalBehaviour_immediate_step_parallel_to_line()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);
		
		//immediately do a step in wrong direction
		AngleXYCoordinate nextPosInLineDir = new AngleXYCoordinate(21, 100, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(nextPosInLineDir));
	}
	
	@Test
	public void testGoalBehaviour_step_on_line()
	{
		Track track = new Track();
		//start position in the middle of the leftmost track
		AngleXYCoordinate initPos = new AngleXYCoordinate(20, 100, 0.0);
		track.setStartGoalPosition(initPos);
		
		//immediately do a step in wrong direction
		AngleXYCoordinate Pos1 = new AngleXYCoordinate(28.0, 102.0, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(Pos1));
		
		//immediately do a step in wrong direction
		AngleXYCoordinate Pos2 = new AngleXYCoordinate(28.0, 100.0, 0.0);
		assertEquals("valid action return OK", Track.TrackStates.OK, track.update(Pos2));
		
		//immediately do a step in wrong direction
		AngleXYCoordinate Pos3 = new AngleXYCoordinate(28.0, 98.0, 0.0);
		assertEquals("valid action return CROSSED_GOAL_WRONG_DIRECTION", Track.TrackStates.CROSSED_GOAL_WRONG_DIRECTION, track.update(Pos3));
	}
	
	@Test
	public void testRandomPositionGeneration()
	{
		Track track = new Track();
		
		System.out.println("testRandomPositionGeneration: " +  track.getStartingPosition(20,-1));
		System.out.println("testRandomPositionGeneration: " +  track.getStartingPosition(-1,-1));
		
	}

}
