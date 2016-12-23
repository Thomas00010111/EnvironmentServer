import java.util.Vector;
import java.lang.Math;
 
/**
 * 
 * @author Thomas Fischle
 *
 */
 
public class Laser
{
    private final double  STEP  = 1.0;
    public Vector<XYCoordinate> laserBeam = new Vector<XYCoordinate>();
    private double maxLaserDistance;
    double [] laserAngles=null;
 
    public Laser(double maxLaserDistance) {
        // TODO Auto-generated constructor stub
        this.maxLaserDistance = maxLaserDistance;
    }
 
    public Laser(double maxLaserDistance, double[] laserAngles) {
        // TODO Auto-generated constructor stub
        this(maxLaserDistance);
        this.laserAngles = laserAngles;
    }
     
    private double[] calcCurrentLaser(AngleXYCoordinate position, double[] angles)
    {
        double [] laserAngles = new double[angles.length]; 
         
        for (int i=0; i<angles.length; i++)
        {
            laserAngles[i]=position.angle + angles[i];
            if (laserAngles[i] > 2.0*Math.PI)
            {
                laserAngles[i] = laserAngles[i] - 2.0*Math.PI; 
            }
             
            if (laserAngles[i] < 0)
            {
                laserAngles[i] = laserAngles[i] + 2.0*Math.PI; 
            }
        }
        return laserAngles;
    }
     
    public double[] meassure(AngleXYCoordinate position, Vector<VSquare> obstacles)
    {
        assert this.laserAngles != null;
        return meassure(position, this.laserAngles, obstacles);
    }
     
     
    public double[] meassure(AngleXYCoordinate position, double[] angles, Vector<VSquare> obstacles)
    {
        double [] distances = new double[angles.length];
        double [] laserAngles;
        laserBeam.clear(); 
         
//      for (int i=0; i<angles.length; i++)
//      {
//          laserAngles[i]=position.angle + angles[i];
//          if (laserAngles[i] > 2.0*Math.PI)
//          {
//              laserAngles[i] = laserAngles[i] - 2.0*Math.PI; 
//          }
//          
//          if (laserAngles[i] < 0)
//          {
//              laserAngles[i] = laserAngles[i] + 2.0*Math.PI; 
//          }
//      }
         
        laserAngles = calcCurrentLaser(position, angles);
         
        for (int i =0; i< laserAngles.length; i++ )
        {
            double  dx = Math.cos(laserAngles[i]);
            double  dy = Math.sin(laserAngles[i]);
            int limitDistanceCounter = 0;
            XYCoordinate delta = new XYCoordinate(dx*STEP, dy*STEP);
            XYCoordinate laserPointPos = new XYCoordinate(position.x, position.y);
             
            // CHECK MAXIMAL DISTANCE FOR OPPONENTS !!!!!!
            while (!crashedIntoObstacle(laserPointPos, obstacles) && limitDistanceCounter < 2*maxLaserDistance)
            {
                laserPointPos=laserPointPos.add(delta);
                //System.out.println("laserPointPos: " + laserPointPos);
                laserBeam.add(new XYCoordinate(laserPointPos));
                limitDistanceCounter++;
            }
            double deltaX = laserPointPos.x-position.x;
            double deltaY = laserPointPos.y-position.y;
            distances[i]=Math.sqrt(deltaX*deltaX + deltaY*deltaY);
            if (distances[i] > maxLaserDistance) distances[i] =  maxLaserDistance;
        }
         
        return distances;
    }
     
     
    private  boolean crashedIntoObstacle(XYCoordinate point, Vector<VSquare> myObstacles)
    {
        for( int i = 0; i <myObstacles.size(); i++ )
        {
            if(myObstacles.get(i).checkIfPointInSquare(point))
            {
                System.out.println("------------Laserpoint crashed in Obstacle --------------");
                return true;
            }
        }
        return false; //not crashed into obstacle
    }
}