//import static org.junit.Assert.*;

import org.junit.Test;


public class TestCar {


//	@Test
//	public void testExecuteActionIntDouble() {
//		
//		for(int actionId = 0; actionId < 4; actionId++)
//		{
//			Environment env1 = new Environment(false);
//			Car car1 = new Car(env1);
//			
//			//10 moves with a step size of 4
//			for(int i =0; i<10; i++)
//			{
//				AngleXYCoordinate position_delta = car1.executeAction(actionId, 4.0);
//	    		env1.addDeltaPosition(position_delta);
//			}
//			//System.out.println("------");
//			//4 moves with a step size of 10
//			Environment env2 = new Environment(false);
//			Car car2 = new Car(env2);
//			for(int i =0; i<4; i++)
//			{
//				AngleXYCoordinate position_delta = car2.executeAction(actionId, 10.0);
//	    		env2.addDeltaPosition(position_delta);
//			}
//			assertEquals("x must be equal:", env1.myCoordinates.x, env2.myCoordinates.x, 0.00001 );
//			assertEquals("y must be equal:", env1.myCoordinates.y, env2.myCoordinates.y, 0.00001 );
//			assertEquals("angles must be equal:", env1.myCoordinates.angle, env2.myCoordinates.angle, 0.00001 );
//		}
//	}
	
	@Test
	public void testExecuteAction() 
	{		
		Environment env1 = new Environment(false);
		Car car1 = new Car(env1);
		
		int actionId = 1;
		
		AngleXYCoordinate position_delta = car1.executeAction(actionId, 4.0);
		env1.addDeltaPosition(position_delta);
		
		System.out.println(position_delta);
	}

}
