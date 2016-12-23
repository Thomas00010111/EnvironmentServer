
public interface IAction {
	
	double start_position_x = 0;
	double start_position_y = 0;
	double angleShift = 0;
	double angle = 0;
	double arc = 0;
	
	public double getRandomAngle();
	
	public AngleXYCoordinate execute(int a, double angle);
	public AngleXYCoordinate execute(int a, double angle, double steps);

	public int getNumberOfActions();
}
