
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
	
	protected boolean equals(XYCoordinate compareTo)
    {
        if(this.x == compareTo.x && this.y == compareTo.y)
            return true;
        else    
        	return false;
    }
	
	public String toString()
	{
		return "x[" + x + "] y[" + y + "]";  
	}
	
	public XYCoordinate add(XYCoordinate xyc)
	{
		this.x=this.x + xyc.x;
		this.y=this.y + xyc.y;
		
		return this;
	}
}
