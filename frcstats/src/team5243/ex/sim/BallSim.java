package team5243.ex.sim;

import java.util.ArrayList;
import java.util.List;

import team5243.ex.SamplePIImpl;

public class BallSim 
{
	public static final double BALL_RADIUS = 5; // 10 inch diameter
	public static final double BALL_MASS = 0.65/32.0d; // 0.65 pounds divided by acceleration due to gravity
	
	public static final double BALL_AREA_M2 = Math.PI*(BALL_RADIUS*0.0254)*(BALL_RADIUS*0.0254);
	public static final double BALL_MASS_KG = 0.65*0.453592;
	
	public static final double highGoalMinX = 24.7;
	public static final double highGoalMaxX = 25;
	public static final double highGoalMinY = 7.916;
	public static final double highGoalMaxY = 9.0833;
	public static final double lowGoalMinX = 24.8;
	public static final double lowGoalMaxX = 25.8;
	public static final double lowGoalMinY = 0.916;
	public static final double lowGoalMaxY = 2.0833;

	public List<double[]> simulateBall(double velocityFPS, double angleDeg, double x0, double y0, double dt, boolean useDrag, boolean hits[])
	{
		if(hits==null)hits = new boolean[2];
		hits[0]=false;
		hits[1]=false;
		
		List<double[]> pos = new ArrayList<double[]>();
		
		ProjectileMotion pm = new ProjectileMotion();
		pm.useDrag = useDrag;
		double vals[] = pm.init(velocityFPS, angleDeg, x0, y0);
		
		pos.add(vals);
		
		while(vals[2]>=0)
		//while(vals[2]>=0.41667)
		{
			vals = Integrator.singleStep(vals, dt, pm);
			if(vals[1]>=lowGoalMinX && vals[1]<=lowGoalMaxX && vals[2]>=lowGoalMinY && vals[2]<=lowGoalMaxY)
			{
				hits[0]=true;
			}
			if(vals[1]>=highGoalMinX && vals[1]<=highGoalMaxX && vals[2]>=highGoalMinY && vals[2]<=highGoalMaxY)
			{
				hits[1]=true;
			}
			pos.add(vals);			
		}
		return pos;
	}
	
    public static class ProjectileMotion implements Integrator.Func
    {
    	public boolean useDrag = false;
    	
    	public ProjectileMotion()
    	{
    		
    	}
    	
    	public double[] init(double velocityFPS, double angleDeg, double xFeet, double yFeet)
    	{
    		double vx = velocityFPS*Math.cos(Math.toRadians(angleDeg));
    		double vy = velocityFPS*Math.sin(Math.toRadians(angleDeg));;
    		
    		double vals[] = {0,xFeet,yFeet,vx,vy};
    		return vals;
    	}
    	
		@Override
		public double[] deriv(double[] vals)
		{
			double out[] = {1,vals[3],vals[4],0,-32.0d};
			if(useDrag)
			{
				double Cd = 0.7d;// 0.5; // 0.5 is ideal sphere, but this ball is bumpy
				double rho = 1.225d; // kg/m^3
				double v = Math.sqrt(vals[3]*vals[3]+vals[4]*vals[4])*0.0254;
				double drag = 0.5*rho*v*v*Cd*BALL_AREA_M2/BALL_MASS_KG;
				double ax = -drag*vals[3]/v; // don't convert vx to m because we have to convert ax back so leave off conversion
				double ay = -drag*vals[4]/v;
				
				out = new double[]{1,vals[3],vals[4],ax,ay-32.0d};
			}
			return out;
		}
    	
    }
    
    public static void main(String args[])
    {
    	double v0 = 34;
    	double angle = 45;
    	double x0 = 0;
    	double y0 = 0 ;
    	
    	v0 = 31.416;
    	angle = 54.782;
    	BallSim sim = new BallSim();
    	List<double[]> pos = sim.simulateBall(v0, angle, x0, y0, 0.001, false, null);
    	int size = pos.size();
    	double vals[] = null;
    	
    	for(int i=0; i<size; i++)
    	{
    		vals = pos.get(i);
    		
    		System.out.println(i + "\t" + vals[0] + "\t" + vals[1] + "\t" + vals[2] + "\t" + vals[3] + "\t" + vals[4]);
    	}
    	
    	SamplePIImpl info = new SamplePIImpl();
    	System.out.println(info.computeMaxHeightFeet(v0, angle));
    	System.out.println(info.computeTimeSecs(v0, angle));
    	System.out.println(info.computeRangeFeet(v0, angle));
    	System.exit(0);
    }
}
