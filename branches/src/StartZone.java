public class StartZone extends TrackElement
	{
		AngleXYCoordinate prevPos, currentPos;

		/*
		 * PosXDir i.e. from left to right check if car is inside element and previous position closer begin 
		 * NegXDir i.e. from right to left
		 * PosYDir i.e. from top to bottom
		 * NegYDir i.e. from bottom to top
		 */
		public static enum FlowDirection {PosXDir, NegXDir, PosYDir, NegYDir}
		FlowDirection flowDir;
		
		public StartZone(int xPos0, int yPos0, int xPos1, int yPos1, int width)
		{
			super(xPos0, yPos0, xPos1, yPos1, width);
		}
		
		public StartZone(VSquare barrier0, VSquare barrier1)
		{
			super(barrier0, barrier1);
		}
		
		public AngleXYCoordinate getStartPosition()
		{
			int xpos = (trackElements[0].getX0() + trackElements[1].getX3())/2;
			int ypos = (trackElements[0].getY0() + trackElements[1].getY0())/2;
			AngleXYCoordinate startPos = new AngleXYCoordinate(xpos, ypos, 0);
			return startPos;
		}
		
		
		public boolean hasCrossed(AngleXYCoordinate xytheta)
		{
			return false;
		}
		
//		public void update(AngleXYCoordinate xytheta)
//		{
//			prevPos = currentPos;
//			currentPos = xytheta;
//		}
	}	