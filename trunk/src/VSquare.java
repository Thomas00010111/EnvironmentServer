	/**
	 * Extends class Square by the possibility to assign a value to the 
	 * defined area, e.g. a reward. 
	 */
public class VSquare extends Square{
		
	private double value;	//value of this area, can be negative or positive

	public VSquare(double v){
		this.value = v;
	}
	
	public VSquare(int x0, int y0, int x3, int y3, double value){
		super( x0, y0, x3, y3);
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}
	
	public void setValue(double v){
		this.value = v;
	}
}
