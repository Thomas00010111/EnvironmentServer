//import static org.junit.Assert.*;

import org.junit.Test;


public class CarPointTest {

	@Test
	public final void testGetBorders() {
		Environment env = new Environment(false);
		CarPoint carpoint = new CarPoint(env);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(20,20, 0.1); //x,y,angle
		XYCoordinate[][] posAgent;
		posAgent = carpoint.getBorders(currentPosAgent);
		
		//This test does not test anything yet
		//What should the 3 coordinates look like int his case?
	}

}
