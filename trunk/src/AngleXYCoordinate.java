
public class AngleXYCoordinate extends XYCoordinate{
	
	public double angle;
	
	public AngleXYCoordinate()
	{
		super();
		this.angle = 0;
	}
	
	public AngleXYCoordinate(double x, double y, double angle) {
		// TODO Auto-generated constructor stub
		super(x,y); 
		this.angle = angle;
	}
	
	protected boolean equals(AngleXYCoordinate compareTo)
    {
		if( super.equals((XYCoordinate)compareTo) &&
        		this.angle == compareTo.angle)
        {
        	return true;
        }
        return false;
		/*
		if(this.x == compareTo.x && this.y == compareTo.y && this.angle == compareTo.angle)
            return true;
        else    
        	return false;
        */
    }
	
	public AngleXYCoordinate add(AngleXYCoordinate that)
	{
		this.angle = this.angle+that.angle;
		super.add((XYCoordinate)that);
		
		return this;
	}
	
	public void init(AngleXYCoordinate that)
	{
		this.x = that.x;
		this.y = that.y;
		this.angle = that.angle;
	}
	
	public String toString()
	{
		return super.toString() + " angle[" + angle + "]";
	}
}
