/** 
 * @author Thomas Fischle
 * Definition of rectangular obstacles.
 * An obstacle is a rectangular square defined by the upper left and the 
 * lower right corner.
 */
public class Square {
	private XYCoordinate x0_y0Pos = new XYCoordinate(0,0); 	//Position (x0,y0), upper left corner
	private XYCoordinate x3_y3Pos = new XYCoordinate(0,0);	//Position (x3,y3), lower right corner
	
	public Square(){
	}
	
	public Square(int x0, int y0, int x3, int y3)
	{
		this.x0_y0Pos.x = x0;
		this.x0_y0Pos.y = y0;
		this.x3_y3Pos.x = x3; 
		this.x3_y3Pos.y = y3;
	}
	
	/**
	 * Method which checks if point at coordinate x,y is within square  
	 * @param 	x x-coordinate
	 * @param 	y y-coordinate
	 * @return 	true: point is in square
	 * 			false: point is not in square
	 */
	public boolean checkIfPointInSquare(XYCoordinate[][] coordinates)
	{
		for(int i = 0; i < coordinates.length; i++)
			for(int j = 0; j < coordinates[i].length; j++)
				if(checkIfPointInSquare(coordinates[i][j]))
					return true;
		return false;
	}
	
	/**
	 * Method which checks if point at coordinate x,y is within square  
	 * @param 	x x-coordinate
	 * @param 	y y-coordinate
	 * @return 	true: point is in square
	 * 			false: point is not in square
	 */
	public boolean checkIfPointInSquare(XYCoordinate xycoord)
	{
		return checkIfPointInSquare(xycoord.x, xycoord.y);
	}
	
	/**
	 * Method which checks if point at coordinate x,y is within square  
	 * @param 	x x-coordinate
	 * @param 	y y-coordinate
	 * @return 	true: point is in square
	 * 			false: point is not in square
	 */
	public boolean checkIfPointInSquare(double x, double  y)
	{
		if( (x >= x0_y0Pos.x && x <= x3_y3Pos.x ) && (y >= x0_y0Pos.y && y <= x3_y3Pos.y) )
			return true;
		else
			return false;
	}
	
	/**
	 * return x0-coordinate
	 * @return x0-coordinate
	 */
	double getX0(){
		return x0_y0Pos.x;
	}
	
	/**
	 * return x3-coordinate
	 * @return x3-coordinate
	 */
	double getX3(){
		return x3_y3Pos.x;
	}
	
	/**
	 * return y0-coordinate
	 * @return y0-coordinate
	 */
	double getY0(){
		return x0_y0Pos.y;
	}
	
	/**
	 * return y3-coordinate
	 * @return y3-coordinate
	 */
	double getY3(){
		return x3_y3Pos.y;
	}
	
	/**
	 * return Width
	 * @return Width
	 */
	double getWidth(){
		return (x3_y3Pos.x - x0_y0Pos.x);
	}
	
	/**
	 * return Height
	 * @return Height
	 */
	double getHeight(){
		return (x3_y3Pos.y - x0_y0Pos.y);
	}
}
