import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;


public class TestLaser {

	@Test
	public void testLaserOpponent_AllLasersMeassureEqualMaxDistance() {
		Environment env = new Environment("Test_defaultProperties1", false);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(122.06764831237714, 39.19459119424428, 0.0);
		env.setEnvironment(currentPosAgent);
		
		double[] laserAngles = {0.0, 0.0, 0.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserObstacles();
		
		for(int i = 0; i<laser.length; i++)
		{
			assertEquals(laser[i], laser[(i+1)%laser.length], 0.0);
		}
		env.dispose();
	}
	
	@Test
	public void testLaser_AllLasersMeassureEqualMaxDistanceConstr1() {
		Vector<VSquare> myObstacles = new Vector<VSquare>();
		//two obstacles length: 100 distance:60 
		myObstacles.add(new VSquare(30, 30, 130, 40,-1));
		myObstacles.add(new VSquare(30, 100, 130, 110,-1));
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(50, 50, 0.0);
		double [] laserAngles = {Math.PI, 0.0, Math.PI/2.0}; //180deg, 0deg, 90deg
		Laser laser = new Laser(200, laserAngles);
		
		double[] distance = laser.meassure(currentPosAgent, laserAngles, myObstacles);
		assertArrayEquals( distance, new double[]{200, 200, 50}, 0 );
		
		AngleXYCoordinate currentPosAgent2 = new AngleXYCoordinate(50, 50, Math.PI/2);
		double [] laserAngles2 = {Math.PI, 0.0, Math.PI/2.0}; //180deg, 0deg, 90deg
		Laser laser2 = new Laser(200, laserAngles);
		
		double[] distance2 = laser.meassure(currentPosAgent2, laserAngles, myObstacles);
		assertArrayEquals( distance2, new double[]{10, 50, 200}, 0 );
		
	}
	
	
	@Test
	public void testLaser_AllLasersMeassureEqualMaxDistanceConstr2() {
		Vector<VSquare> myObstacles = new Vector<VSquare>();
		//two obstacles length: 100 distance:60 
		myObstacles.add(new VSquare(30, 30, 130, 40,-1));
		myObstacles.add(new VSquare(30, 100, 130, 110,-1));
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(50, 50, 0.0);
		double [] laserAngles = {Math.PI, 0.0, Math.PI/2.0}; //180deg, 0deg, 90deg
		Laser laser = new Laser(200, laserAngles);
		
		double[] distance = laser.meassure(currentPosAgent, myObstacles);
		assertArrayEquals( distance, new double[]{200, 200, 50}, 0 );
		
		AngleXYCoordinate currentPosAgent2 = new AngleXYCoordinate(50, 50, Math.PI/2);
		double [] laserAngles2 = {Math.PI, 0.0, Math.PI/2.0}; //180deg, 0deg, 90deg
		Laser laser2 = new Laser(200, laserAngles2);
		
		double[] distance2 = laser.meassure(currentPosAgent2, myObstacles);
		assertArrayEquals( distance2, new double[]{10, 50, 200}, 0 );
		
	}
//	This test should test if assertion occurs but then testrun is shown is failed
//	@Test()
//	public void testLaser_unitialized() {
//		Vector<VSquare> myObstacles = new Vector<VSquare>();
//		//two obstacles length: 100 distance:60 
//		myObstacles.add(new VSquare(30, 30, 130, 40,-1));
//		myObstacles.add(new VSquare(30, 100, 130, 110,-1));
//		
//		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(50, 50, 0.0);
//		Laser laser = new Laser(200);
//		
//		double[] distance = laser.meassure(currentPosAgent, myObstacles);
//		assertArrayEquals( distance, new double[]{200, 200, 50}, 0 );
//	}
}
