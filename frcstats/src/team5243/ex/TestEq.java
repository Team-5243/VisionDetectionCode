package team5243.ex;

import team5243.ProjectileInfo;

public class TestEq 
{
    public static void main(String args[])
    {
    	ProjectileInfo info = new SamplePIImpl();
    	
    	double h = 0;
    	double ang = 0;
    	double r = 0;
    	double v = 30;
    	double t = 0;
    	for(int i=1; i<90; i++)
    	{
    		ang = i;
    		t = info.computeTimeSecs(v, ang);
    		h = info.computeMaxHeightFeet(v, ang);
    		r = info.computeRangeFeet(v, ang);
    		System.out.println(ang + "\t" + t + "\t" + h + "\t" + r + "\t" + Math.toDegrees(Math.atan(4*h/r)));
    	}
    	System.exit(0);
    }
}
