
public class  CarState extends AngleXYCoordinate{
	
	public double speed;
	
	public CarState()
	{
		super();
		this.speed = 0;
	}
	
	public CarState(double x, double y, double angle, double speed) {
		// TODO Auto-generated constructor stub
		super(x,y, angle); 
		this.speed = speed;
	}
	
	protected boolean equals(CarState compareTo)
    {
		if( super.equals((AngleXYCoordinate)compareTo) &&
        		this.speed == compareTo.speed)
        {
        	return true;
        }
        return false;
    }
	
	public CarState add(CarState that)
	{
		this.speed = this.speed+that.speed;
		super.add((AngleXYCoordinate)that);
		
		return this;
	}
	
	public void init(CarState that)
	{
		this.x = that.x;
		this.y = that.y;
		this.angle = that.angle;
		this.speed = that.speed;
	}
	
	public String toString()
	{
		return super.toString() + " speed[" + speed + "]";
	}
}
