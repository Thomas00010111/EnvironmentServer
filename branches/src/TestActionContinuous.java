import static org.junit.Assert.*;

import org.junit.Test;


public class TestActionContinuous {
	
	int TurnRight=0, TurnLeft=1, MoveForward = 2, MoveBackward = 3;
	enum actions {TurnRight, TurnLeft, MoveForward, MoveBackward}

	@Test
	public void testExecuteIntDoubleDouble() {
		/*
		 * execute() return a delta,i.e. how much the car moves
		 */
		ActionContinuous action = new ActionContinuous();
		
		//left turn,  nose is pointing to the right, car moves in positive x and negative y direction 
		AngleXYCoordinate position1 = action.execute(TurnLeft, 0.0);
		assertTrue("x must increase:", position1.x > 0.0 );
		assertTrue("y must increase:", position1.y < 0.0 );
		//System.out.println(position1);
		
		//right turn,  nose is pointing to the right, car moves in positive x and positive y direction
		AngleXYCoordinate position2 = action.execute(TurnRight, 0.0);
		assertTrue("x must increase:", position2.x > 0.0 );
		assertTrue("y must increase:", position2.y > 0.0 );
		//System.out.println(position2);
		
		//forward,  nose is pointing to the right
		AngleXYCoordinate position3 = action.execute(MoveForward,0.0);
		assertTrue("x must increase:", position3.x > 0.0 );
		assertTrue("y must not increase:", position3.y < 0.0001 && position3.y > -0.0001 );
		//System.out.println(position3);
		
		//backward, nose is pointing to the right
		AngleXYCoordinate position4 = action.execute(MoveBackward,0.0);
		assertTrue("x must decrease:", position4.x < 0.0 );
		assertTrue("y must not increase:", position4.y < 0.0001 && position4.y > -0.0001 );
		//System.out.println(position4);
		
		
		//forward,  nose is pointing down (pos y direction)
		AngleXYCoordinate position5 = action.execute(2,1.0,Math.PI/2);
		//System.out.println(position5);
	}
	
	@Test
	public void testCheckActionsStep() {
		
		for(int actionId = 0; actionId < 4; actionId++)
		{
			/*
			 * get x,y stepwise to plot in Excel for instance.
			 */
			ActionContinuous action = new ActionContinuous();
			//forward,  nose is pointing to the right
			AngleXYCoordinate position = new AngleXYCoordinate(0,0,0);
			
			//10 moves with a step size of 4
			for(int i =0; i<10; i++)
			{
				AngleXYCoordinate position_delta = action.execute(actionId, position.angle, 4.0);
				position.add(position_delta);
				//System.out.println(position);
			}
			//System.out.println("------");
			//4 moves with a step size of 10
			AngleXYCoordinate position2 = new AngleXYCoordinate(0,0,0);
			for(int i =0; i<4; i++)
			{
				AngleXYCoordinate position_delta = action.execute(actionId, position2.angle, 10.0);
				position2.add(position_delta);
				//System.out.println(position2);
			}
			assertEquals("x must be equal:", position.x, position2.x, 0.00001 );
			assertEquals("y must be equal:", position.y, position2.y, 0.00001 );
			assertEquals("angles must be equal:", position.angle, position2.angle, 0.00001 );
		}
		
	}

}
