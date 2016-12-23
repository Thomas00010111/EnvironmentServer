public class TrackElement
	{
		public VSquare[] trackElements;
		public String description = "";
		
		public TrackElement(int xPos0, int yPos0, int xPos1, int yPos1, int width)
		{
			trackElements = new VSquare[2];
			
			//double m = (xPos1-xPos0)/(yPos1-yPos0); 
			
			//int x0_0 = xPos0 + (int)( m*(double)width/2.0);
			//int x1_0 = xPos0 - (int)( m*(double)width/2.0);
			
			//trackElements[0] = new VSquare(x0_0, y0_0, x1_0, y1_0, RewardCrash);
			//trackElements[1] = new VSquare(x0_1, y0_1, x1_1, y1_1, RewardCrash);
		}
		
		/**
		 * A track element is a piece of a track with a left border and a right border.
		 * So each track element needs two VSquares to limit the track.
		 * @param barrier0
		 * @param barrier1
		 */
		public TrackElement(VSquare barrier0, VSquare barrier1)
		{
			trackElements = new VSquare[2];
			trackElements[0] = barrier0;
			trackElements[1] = barrier1;
		}
		
		public TrackElement(VSquare barrier0, VSquare barrier1, String description)
		{
			trackElements = new VSquare[2];
			trackElements[0] = barrier0;
			trackElements[1] = barrier1;
			this.description = description;
		}
		
		
		public VSquare[] getTrackElements()
		{
			return trackElements;
		}
		
		public boolean isInElement(AngleXYCoordinate xytheta)
		{	
			//it does not matter between which x-coordinate i.e. could be that we include the barrier but that's ok
			boolean betweenX = (trackElements[0].getX0() <= xytheta.x && xytheta.x <= trackElements[1].getX3())
					|| (trackElements[1].getX0() <= xytheta.x && xytheta.x <= trackElements[0].getX3()); 

			boolean betweenY = (trackElements[0].getY0() <= xytheta.y && xytheta.y <= trackElements[1].getY3())
					|| (trackElements[1].getY0() <= xytheta.y && xytheta.y <= trackElements[0].getY3()); 

			boolean inElement = (betweenX && betweenY);

			return inElement;
		}
	}	