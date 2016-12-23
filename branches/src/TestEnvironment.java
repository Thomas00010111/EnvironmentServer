import static org.junit.Assert.*;

import org.junit.Test;


public class TestEnvironment {

	@Test
	public void testCrashedIntoObstacle() {
		Environment env = new Environment("Test_defaultProperties", false);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(122.06764831237714, 39.19459119424428, 0.0);
		env.crashedIntoObstacle(currentPosAgent);
		
		assertEquals("Must be outside square:", true, env.crashedIntoObstacle(currentPosAgent));
		
		
		AngleXYCoordinate currentPosAgent1 = new AngleXYCoordinate(122.06764831237714, 40.19459119424428, 0.0);
		env.crashedIntoObstacle(currentPosAgent1);
		
		assertEquals("Must be outside square:", false, env.crashedIntoObstacle(currentPosAgent1));
		
		env.dispose();
	}
	
	@Test
	public void testLaserObstacle_AllLasersMeassureEqualDistance() {
		Environment env = new Environment("Test_defaultProperties1", false);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(122.06764831237714, 39.19459119424428, 0.0);
		env.setEnvironment(currentPosAgent);
		
		double[] laserAngles = {0.0, 0.0, 0.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserObstacles();
		
		env.paintComponent();
		
		for(int i = 0; i<laser.length; i++)
		{
			assertEquals(laser[i], laser[(i+1)%laser.length], 0.0);
		}
		env.dispose();
	}
	
	@Test
	public void testLaserOpponent_AllLasersMeassureEqualMaxDistance() {
		Environment env = new Environment("Test_defaultProperties1", false);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(122.06764831237714, 39.19459119424428, 0.0);
		env.setEnvironment(currentPosAgent);
		
		double[] laserAngles = {0.0, 0.0, 0.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserOpponents();
		
		for(int i = 0; i<laser.length; i++)
		{
			assertEquals(laser[i], laser[(i+1)%laser.length], 0.0);
		}
		env.dispose();
	}
	
	@Test
	public void testLaserOpponent_betweenTwoOpponents() {
		Environment env = new Environment("Test_defaultProperties2", false);
		
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(70, 70, 0.0);
		env.setEnvironment(currentPosAgent);
		
		//Laser: back, forward, left, right
		double[] laserAngles = {Math.PI, 0.0, -Math.PI/2.0, Math.PI/2.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserOpponents();
		
		env.paintComponent();
		
		assertEquals(laser[0], env.LASER_MAX, 0.0);
		assertEquals(laser[1], env.LASER_MAX, 0.0);
		assertEquals(laser[2], 20, 0.0);
		assertEquals(laser[3], 30, 0.0);
		env.dispose();
	}

	@Test
	public void testLaser_Rot0deg() {
		Environment env = new Environment("Test_defaultProperties1", false);
		
		int posX = 50;
		int posY = 30;
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(posX, posY, 0.0);
		env.setEnvironment(currentPosAgent);
		
		double[] laserAngles = {Math.PI, 0.0, -Math.PI/2.0, Math.PI/2.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserObstacles();

		//Adding both must result in width-border, border is 10
		assertEquals(laser[0], posX-10, 0.0);
		assertEquals(laser[1], env.ENVIRONMENT_WIDTH-posX-10, 0.0);
		assertEquals(laser[2], posY-10, 0.0);
		assertEquals(laser[3], env.ENVIRONMENT_HEIGHT-posY-10, 0.0);
		
		env.dispose();
	}
	
	@Test
	public void testLaser_Rot90deg() {
		Environment env = new Environment("Test_defaultProperties1", false);
		
		int posX = 50;
		int posY = 30;
		AngleXYCoordinate currentPosAgent = new AngleXYCoordinate(posX, posY, Math.PI/2.0);
		env.setEnvironment(currentPosAgent);
		
		double[] laserAngles = {Math.PI, 0.0, -Math.PI/2.0, Math.PI/2.0};
		env.setLaserAngles(laserAngles);
		double[] laser = env.triggerLaserObstacles();

//		//Adding both must result in width-border, border is 10
		assertEquals(laser[0], posY-10, 0.0);
		assertEquals(laser[1], env.ENVIRONMENT_HEIGHT-posY-10, 0.0);
		assertEquals(laser[2], env.ENVIRONMENT_WIDTH-posX-10, 0.0);
		assertEquals(laser[3], posX-10, 0.0);
		
		env.dispose();
	}
	
}
