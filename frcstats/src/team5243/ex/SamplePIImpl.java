package team5243.ex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team5243.ProjectileInfo;
import team5243.TrajectoryException;

/**
 * I wrote this one.  Write your own in ProjectileInfoImpl
 * 
 * @author anthony
 *
 */
public class SamplePIImpl implements ProjectileInfo
{

	@Override
	public double getVelocityFPS() 
	{
		double r = FLYWHEEL_RADIUS_INCHES;
		double omega = FLYWHEEL_UNLOADED_ROTATION_RATE_RPM;
		// I could see if a voltage drop on the battery means a slower rotation
		// or if the mass of the ball slows it down and adjust this rate.
		
		return computeVelocityFPS(r,omega);
	}

	@Override
	public double computeVelocityFPS(double radiusInches, double omegaRPM) 
	{
		// convert to rotations per second
		double omegaRPS = omegaRPM * MINUTESPERSECOND;
		// compute v = r*w where rotation is in radians
		double vel = radiusInches * omegaRPS * 2.0d * Math.PI;
		
		// convert to feet
		vel *= FEETPERINCH;
		
		return vel;
	}

	@Override
	public double computeRangeFeet(double velocityFPS, double angleDeg) 
	{
		double t = computeTimeSecs(velocityFPS,angleDeg);
		double vx = velocityFPS*Math.cos(Math.toRadians(angleDeg));
		double r = vx*t;
		return r;
	}

	@Override
	public double computeMaxRangeFeet(double velocityFPS) 
	{
		return computeRangeFeet(velocityFPS,45);
	}

	@Override
	public double computeAngleDeg(double velocityFPS, double rangeFeet)
			throws TrajectoryException 
	{
		double maxRange = computeMaxRangeFeet(velocityFPS);
		
		if(maxRange < rangeFeet)
		{
			TrajectoryException ex = new TrajectoryException("Range exceeds max range",maxRange,false,true);
			throw ex;
		}
		
		double sin2a = rangeFeet * g/(velocityFPS * velocityFPS);
		
		double ang = Math.asin(sin2a)/2.0d;
		ang = Math.toDegrees(ang);
		return ang;
	}
	@Override
	public double computeRotationRPM(double velocityFPS, double radiusInches) 
	{
		double vel = velocityFPS*INCHESPERFEET;
		
		double rot = vel/(2.0d*Math.PI*radiusInches);
		
		rot *= SECONDSPERMINUTE;
		
		return rot;
	}

	@Override
	public double computeTimeSecs(double velocityFPS, double angleDeg) 
	{
		double t = 2.0d*velocityFPS*Math.sin(Math.toRadians(angleDeg));
		t = t/g;
		return t;
	}
	
	@Override
	public double computeMaxHeightFeet(double velocityFPS, double angleDeg)
	{
		double h = velocityFPS* Math.sin((Math.toRadians(angleDeg)));
		h = h * h;
		h = h /(2*g);
		return h;
	}

	@Override
	public double computeHighGoalRangeFeet(double velocityFPS, double angleDeg)
			throws TrajectoryException 
	{
		double maxHeight = computeMaxHeightFeet(velocityFPS,angleDeg);
		if(maxHeight < (ProjectileInfo.HIGH_GOAL_BOTTOM_HEIGHT_FEET+0.5))
		{
			TrajectoryException ex = new TrajectoryException("Angle too low");
			ex.setTooLow(true);
			throw ex;
		}
		
		if(maxHeight > (ProjectileInfo.HIGH_GOAL_CENTER_HEIGHT_FEET+1))
		{
			TrajectoryException ex = new TrajectoryException("Angle too high");
			ex.setTooHigh(true);
			throw ex;			
		}
		// take half of the range so we can hit at high point
		double range = computeRangeFeet(velocityFPS,angleDeg)*0.5;
		
		if(range < 4)//any closer and you're on the BATTER
		{
			TrajectoryException ex = new TrajectoryException("Too close",2,true,false);
			throw ex;			
		}
		return range;
	}

	@Override
	public double computeMaxHighGoalRangeFeet(double velocityFPS)
			throws TrajectoryException 
	{
		return computeHighGoalRangeFeet(velocityFPS,45);
	}

	@Override
	public double computeHighGoalAngleDeg(double velocityFPS, double rangeFeet)
			throws TrajectoryException 
	{
		double maxRange = computeMaxHighGoalRangeFeet(velocityFPS);
		
		if(maxRange < rangeFeet)
		{
			TrajectoryException ex = new TrajectoryException("Range exceeds max range",maxRange,false,true);
			throw ex;
		}
		/*
		// include extra factor of two because high goal only has half the range
		double sin2a = 2*rangeFeet * g/(velocityFPS * velocityFPS);
		
		double ang = Math.asin(sin2a)/2.0d;
		ang = Math.toDegrees(ang);
		*/
		double ang = 4*(ProjectileInfo.HIGH_GOAL_BOTTOM_HEIGHT_FEET)/(2.0*rangeFeet);
		ang = Math.atan(ang);
		ang = Math.toDegrees(ang);
		return ang;
	}

	@Override
	public double[] computeHighGoalMinMaxAngleDeg(double velocityFPS) 
	{
		List<Double> angs = new ArrayList<Double>();
		
		double ang = 0;
		for(int i=0; i<90; i++)
		{
			ang = i;
			try
			{
				computeHighGoalRangeFeet(velocityFPS, ang);
				angs.add(ang);
			}
			catch(TrajectoryException ex)
			{
				// ok for now
			}
		}
		
		double out[] = {-1,-1};
		if(angs.size() > 0)
		{
			Collections.sort(angs);
			out[0]=angs.get(0);
			out[1]=angs.get(angs.size()-1);
		}
		return out;
	}

	@Override
	public double[] computeHighGoalMinMaxRangeFeet(double velocityFPS) {
		List<Double> ranges = new ArrayList<Double>();
		
		double ang = 0;
		double range = 0;
		for(int i=0; i<90; i++)
		{
			ang = i;
			try
			{
				range = computeHighGoalRangeFeet(velocityFPS, ang);
				ranges.add(range);
			}
			catch(TrajectoryException ex)
			{
				// ok for now
			}
		}
		
		double out[] = {-1,-1};
		if(ranges.size() > 0)
		{
			Collections.sort(ranges);
			out[0]=ranges.get(0);
			out[1]=ranges.get(ranges.size()-1);
		}
		return out;
	}

	@Override
	public double[] recommendLowGoalAngleAndRange(double velocityFPS,
			double rangeFeet) 
	{
		// keep it simple and just return the same range.
		double out[] = new double[2];
		
		double angDeg = 0;
		try
		{
			angDeg = computeAngleDeg(velocityFPS, rangeFeet);
		}
		catch(Exception ex)
		{
			// is actually a good default for the low goal
			ex.printStackTrace();
		}
		out[0] = angDeg;
		out[1]=rangeFeet;
		return out;
	}

	@Override
	public double[] recommendHighGoalAngleAndRange(double velocityFPS,
			double rangeFeet) 
	{
		double out[] = new double[2];
		
		boolean maxHeightOK = false;
		// check maxHeight for velocity
		double maxHeight = 0;
		try
		{
			maxHeight = this.computeMaxHeightFeet(velocityFPS, 85);
			if(maxHeight >= ProjectileInfo.HIGH_GOAL_BOTTOM_HEIGHT_FEET)
			{
				maxHeightOK = true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			// Oh no! we can't hit the high goal
		}
		
		if(maxHeightOK)
		{
			double angDeg = 0;
			double h = 0;
			double r = 0;
			double delta = 5.0d/12.0d;
	    	for(int i=90; i>0; i--)
	    	{
	    		angDeg = i;
	    		h = computeMaxHeightFeet(velocityFPS, angDeg)-0.7;//subtract height ball is launched from
	    		r = computeRangeFeet(velocityFPS, angDeg);
	    		if(Math.abs(h-ProjectileInfo.HIGH_GOAL_CENTER_HEIGHT_FEET)<delta )
	    		{
	    			rangeFeet = r/2.0d;
	    			break;
	    		}
	    	}

	    	/*
			// try to get an angle for this height
			try
			{
				angDeg = computeHighGoalAngleDeg(velocityFPS, rangeFeet);
			}
			catch(Exception ex)
			{
				if(ex instanceof TrajectoryException)
				{
					TrajectoryException te = (TrajectoryException)ex;
					
					if(te.isTooClose)
					{
						while(te.isTooClose && rangeFeet < 100)
						{
							// move back 6 inches and try again
							rangeFeet+=0.5;
							try
							{
								angDeg = this.computeHighGoalAngleDeg(velocityFPS, rangeFeet); 
								break;
							}
							catch(Exception e)
							{
								te = (TrajectoryException)e;
							}
						}
					}
					else if(te.isTooFar || te.isTooHigh)
					{
						while(te.isTooFar || te.isTooHigh && rangeFeet >0)
						{
							// move forward 6 inches and try again
							rangeFeet-=0.5;
							try
							{
								angDeg = this.computeHighGoalAngleDeg(velocityFPS, rangeFeet); 
								break;
							}
							catch(Exception e)
							{
								te = (TrajectoryException)e;
							}
						}
					}

				}
			}
			*/
			
			out[0]=angDeg;
			out[1]=rangeFeet;
		}
		return out;
	}

}
