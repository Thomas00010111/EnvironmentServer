import static org.junit.Assert.*;

import org.junit.Test;


public class TrackElementTest {

	@Test
	public void testTrackElementVSquareVSquare() {
		
		//bottom
		VSquare barrier4 = new VSquare(1, 2, 3, 4, 5);
		VSquare barrier5 = new VSquare(6, 7, 8, 9, 10);
		// We need four track elements
		TrackElement elem = new TrackElement(barrier4, barrier5);
		
		assertEquals("Must be equal:", 1, elem.trackElements[0].getX0());
		assertEquals("Must be equal:", 2, elem.trackElements[0].getY0());
		assertEquals("Must be equal:", 3, elem.trackElements[0].getX3());
		assertEquals("Must be equal:", 4, elem.trackElements[0].getY3());
		
		assertEquals("Must be equal:", 6, elem.trackElements[1].getX0());
		assertEquals("Must be equal:", 7, elem.trackElements[1].getY0());
		assertEquals("Must be equal:", 8, elem.trackElements[1].getX3());
		assertEquals("Must be equal:", 9, elem.trackElements[1].getY3());
		
	}
}
