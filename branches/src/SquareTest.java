import static org.junit.Assert.*;

import org.junit.Test;


public class SquareTest {

	@Test
	public void testCheckIfPointInSquare() {
		int BarrierWidth = 10;
		int Trackwidth = 20;
		
		Square square = new Square(0, 0, 200, BarrierWidth);
		Square square2 = new Square(BarrierWidth + Trackwidth, BarrierWidth + Trackwidth, 200 - (BarrierWidth + Trackwidth), BarrierWidth + Trackwidth + BarrierWidth);
		VSquare vsquare = new VSquare(10, 95, 60, 105, 2.0);
		
		XYCoordinate xycoord1 = new XYCoordinate(20,20);
		XYCoordinate xycoord2 = new XYCoordinate(5,5);
		XYCoordinate xycoord3 = new XYCoordinate(BarrierWidth + Trackwidth, BarrierWidth + Trackwidth); //on boarder
		
		assertEquals("Must be outside square:", false, square.checkIfPointInSquare(xycoord1));
		assertEquals("Must be inside square:", true, square.checkIfPointInSquare(xycoord2));
		assertEquals("Must be outside square2:", false, square2.checkIfPointInSquare(xycoord1));
		assertEquals("Must be inside square2:", true, square2.checkIfPointInSquare(xycoord3));
		
		assertEquals("Must be inside vsquare:", false, vsquare.checkIfPointInSquare(xycoord1));
		assertEquals("Must be 2.0:", true, vsquare.getValue()==2.0);
	}
	

	
	@Test
	public void testPointCarInSquare()
	{	
		VSquare vsquare = new VSquare(10, 95, 60, 105, 2);
		AngleXYCoordinate axyCoordinate = new AngleXYCoordinate(20,20,0.0);
		AngleXYCoordinate axyCoordinate2 = new AngleXYCoordinate(11,96,0.0);
		AngleXYCoordinate axyCoordinate3 = new AngleXYCoordinate(39.97855946364037, 102.93724008064785, 0.0);
		Environment env = new Environment(false);
		
		CarPoint car = new CarPoint(env); //Simple dot
		
		assertEquals("Must be outside vsquare:", false, vsquare.pointInSquare(car.getBorders(axyCoordinate)));
		assertEquals("Must be inside vsquare:", true, vsquare.pointInSquare(car.getBorders(axyCoordinate2)));
		assertEquals("Must be outside vsquare:", false, vsquare.pointInSquare(car.getBorders(axyCoordinate3)));

	}
	
	
	@Test
	public void testConstructingSquare() {
		int x0=1;
		int x3=3;
		int y0=11;
		int y3=13;
		
		Square square = new Square(x0,y0, x3, y3);
		VSquare vsquare = new VSquare(x0,y0, x3, y3, -1.0);
		
		assertEquals("Must be equal:", x0, square.getX0());
		assertEquals("Must be equal:", y0, square.getY0());
		assertEquals("Must be equal:", x3, square.getX3());
		assertEquals("Must be equal:", y3, square.getY3());
		
		assertEquals("Must be equal:", x0, vsquare.getX0());
		assertEquals("Must be equal:", y0, vsquare.getY0());
		assertEquals("Must be equal:", x3, vsquare.getX3());
		assertEquals("Must be equal:", y3, vsquare.getY3());
		
	}

}
