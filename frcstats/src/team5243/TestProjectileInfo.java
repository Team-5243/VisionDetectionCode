package team5243;

import team5243.ex.SamplePIImpl;

public class TestProjectileInfo 
{
	/**
	 * Determines if d2 matches d1 within the given relative error.
	 * |d1-d2|/|d1| < tol
	 * @param d1
	 * @param d2
	 * @param tol
	 * @return
	 */
	public static boolean equal(double d1, double d2, double tol)
	{
		boolean flag = false;
		
		if(d1 == 0) flag = Math.abs(d2)<tol;
		else
		{
			double diff = Math.abs(d1-d2)/Math.abs(d1);
			if(diff < tol) flag = true;
		}
		
		if(!flag)
		{
			System.err.println("Expected " + d1 + " but found " + d2);
		}
		
		return flag;
	}
	
	public static void testInfo(ProjectileInfo info)
	{
		boolean flag = false;
		
		flag = testVelocity(info);
		if(flag)
		{
			System.out.println("Passed velocity test");
		}
		else
		{
			System.out.println("Failed velocity test");
		}

		flag = testRange(info);
		if(flag)
		{
			System.out.println("Passed range test");
		}
		else
		{
			System.out.println("Failed range test");
		}

		
		flag = testAngle(info);
		if(flag)
		{
			System.out.println("Passed angle test");
		}
		else
		{
			System.out.println("Failed angle test");
		}

		flag = testHighGoal(info);
		if(flag)
		{
			System.out.println("Passed high goal test");
		}
		else
		{
			System.out.println("Failed high goal test");
		}
	}
	
    public static boolean testVelocity(ProjectileInfo info)
    {
    	double val = 0;
    	double exp = 0;
    	
    	exp = 43.633;
    	val = info.computeVelocityFPS(5,1000);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed velocity test");
    		return false;
    	}
    	
    	exp = 52.3598;
    	val = info.computeVelocityFPS(ProjectileInfo.FLYWHEEL_RADIUS_INCHES, ProjectileInfo.FLYWHEEL_UNLOADED_ROTATION_RATE_RPM);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed velocity test");
    		return false;
    	}
    	
    	exp = 1432.4;
    	val = info.computeRotationRPM(50, 4);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed velocity test");
    		return false;
    	}
    	return true;
    }
    
    public static boolean testRange(ProjectileInfo info)
    {
    	double val = 0;
    	double exp = 0;
 
    	exp = 4.419;
    	val = info.computeTimeSecs(100,45);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test for time");
    		return false;
    	}

    	exp = 1.4142;
    	val = info.computeTimeSecs(32,45);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test for time");
    		return false;
    	}


    	exp = 2;
    	val = info.computeTimeSecs(32,90);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test for time");
    		return false;
    	}

    	exp = 78.125;
    	val = info.computeMaxRangeFeet(50);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test");
    		return false;
    	}
    	
    	exp = 67.658;
    	val = info.computeRangeFeet(50,30);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test");
    		return false;
    	}
    	
    	exp = 7.03125;
    	val = info.computeMaxHeightFeet(30, 45);
    	if(!equal(exp,val,0.005))
    	{
    		System.err.println("Failed range test for max height");
    		return false;
    	}
    	return true;
    }
    
    public static boolean testAngle(ProjectileInfo info)
    {
    	double val = 0;
    	double exp = 0;

    	try
    	{
    		exp = 30.0d;
    		val = info.computeAngleDeg(50, 67.658);
    		if(!equal(exp,val,0.005))
        	{
        		System.err.println("Failed angle test");
        		return false;
        	}

    		exp = 45.0d;
    		val = info.computeAngleDeg(50, 78.1249);
    		if(!equal(exp,val,0.005))
        	{
        		System.err.println("Failed angle test");
        		return false;
        	}

    	}
    	catch(TrajectoryException ex)
    	{
    		ex.printStackTrace();
    		System.err.println("Failed angle calculation");
    		return false;
    	}
    	
    	// try to cause exception
    	try
    	{
    		val = info.computeAngleDeg(50, 100);
    	}
    	catch(TrajectoryException ex)
    	{
    		if(!ex.isTooFar()) 
			{
    			System.err.println("Was expecting too far");
    			return false;
			}
    		// this is the expected result
    	}
    	
    	return true;
    }
    
    public static boolean testHighGoal(ProjectileInfo info)
    {
    	double val = 0;
    	double exp = 0;
    	double vel = 34;
    	double angle = 45;
    	
    	
    	try
    	{
    		exp = 18.0625;
        	val = info.computeMaxHighGoalRangeFeet(vel);
        	if(!equal(exp,val,0.005))
        	{
        		System.err.println("Failed high goal angle test");
        		return false;
        	}
        	
    	}
    	catch(Exception ex)
    	{
    		System.err.println(ex.getMessage());
    		return false;
    	}
    	
    	try
    	{
    		angle = 7;
    		val = info.computeHighGoalRangeFeet(vel, angle);
        	System.err.println("Failed high goal angle test");
        	return false;
    	}
    	catch(Exception ex)
    	{
    		// we expect to be too low
    	}
    	
    	return true;
    }
    
    public static void main(String args[])
    {
    	ProjectileInfo info = null;
    	
    	// test initial impl
    	info = new SamplePIImpl();
    	System.out.println("Testing SamplePIImpl");
    	testInfo(info);
    	
    	for(int i=0; i<90; i++)
    	{
    		try
    		{
    		System.out.println(i + "\t" + info.computeHighGoalRangeFeet(34, i)+ "\t" + info.computeMaxHeightFeet(27, i));
    		}
    		catch(Exception ex)
    		{
    			System.out.println(ex.getMessage());
    		}
    	}
    	
    	System.out.println("\n\n");

    	
    	// test team impl
    	info = new ProjectileInfoImpl();
    	System.out.println("Testing ProjectileInfoImpl");
    	testInfo(info);
    	
    	System.exit(0);
    }
}
