
public class XYCoordinate {
	public double x;
    public double y;
	
	public XYCoordinate() {
		// TODO Auto-generated constructor stub
		this.x = 0;
		this.y = 0;
	}
	
	public XYCoordinate(double x, double y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	
	public XYCoordinate(XYCoordinate xycoordinate) {
		this.x = xycoordinate.x;
		this.y = xycoordinate.y;
	}

	protected boolean equals(XYCoordinate compareTo)
    {
        //eigentlich return (this.x == compareTo.x && this.y == compareTo.y)
		if(this.x == compareTo.x && this.y == compareTo.y)
            return true;
    
        return false;
    }
	
	public String toString()
	{
		return "x[" + x + "] y[" + y + "]";  
	}
	
	public double getLength()
	{
		return java.lang.Math.sqrt(x*x + y*y);
		
	}
	
	public XYCoordinate add(XYCoordinate xyc)
	{
		this.x=this.x + xyc.x;
		this.y=this.y + xyc.y;
		
		return this;
	}
}
