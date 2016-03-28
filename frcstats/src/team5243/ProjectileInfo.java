package team5243;

/**
 * This interface declares useful methods for computing velocity, launch
 * angle, and range for projectiles in the game.  All calculations ignore
 * the effect of air resistance.
 * 
 * 0 degrees is horizontal towards the front of the robot.  90 degrees is
 * vertical, straight up.
 * 
 * https://en.wikipedia.org/wiki/Projectile_motion
 * 
 * @author 5243
 *
 */
public interface ProjectileInfo 
{
	public static final double FEETPERINCH = 1.0d/12.0d;
	public static final double INCHESPERFEET = 12.0d;
	public static final double SECONDSPERMINUTE = 60.0d;
	public static final double MINUTESPERSECOND = 1.0d/60.0d;
	
	/** The acceleration due to gravity in feet per second per second */
	public static final double g = 32.0d;
	
	public static final double FLYWHEEL_RADIUS_INCHES = 2.0d;
	/** 
	 * This is how fast the wheel turns. 
	 * It might be slower when in contact with the ball. 
	 * The CIM_MOTOR_UNLOADED is 5310 rpm.  So this should be lower.
	 * http://www.andymark.com/Motor-p/am-0255.htm
	 */
	public static final double FLYWHEEL_UNLOADED_ROTATION_RATE_RPM = 3000.0d;
	
	/** The height from the floor to the bottom of the high goal */
	public static final double HIGH_GOAL_BOTTOM_HEIGHT_FEET = 7.0d + 1.0d/12.0d; // 7ft 1in
	
	/** The height from the floor to the center of the high goal */
	public static final double HIGH_GOAL_CENTER_HEIGHT_FEET = 8.0d + 1.0d/12.0d; // 8ft 1 in
	
	/**
	 * Return the current expected launch velocity in feet per second.
	 * This method may call the computeVelocityFPS method or it might
	 * return a calibrated constant.  The computeVelocityFPS can be used
	 * if the rotation rate of the wheel slows down during competition as
	 * the battery starts to go.
	 * 
	 * @return velocity in feet per second
	 */
    public double getVelocityFPS();
    
    /**
     * For the provided velocity and range, recommend an angle and a range to hit the
     * low goal.  Most of the time the range will be the same.
     * 
     * @param velocityFPS
     * @param rangeFeet
     * @return array where 0 is angle in degrees, and 1 is range in feet.
     */
    public double[] recommendLowGoalAngleAndRange(double velocityFPS, double rangeFeet);
    
    /**
     * For the provided velocity and range, recommend an angle and a range to hit the
     * high goal.  To hit the high goal at the top of the arc, range may need
     * to be adjusted.
     * 
     * @param velocityFPS
     * @param rangeFeet
     * @return array where 0 is angle in degrees, and 1 is range in feet.
     */
    public double[] recommendHighGoalAngleAndRange(double velocityFPS, double rangeFeet);
    
    /**
     * Compute the velocity based on the radius of the flywheel in inches and
     * its rotation rate in rotations per minute.
     * 
     * @param radiusInches - flywheel radius
     * @param omegaRPM - rotational speed in rotations per minute
     * @return velocity in feet per second
     */
    public double computeVelocityFPS(double radiusInches, double omegaRPM);
    
    /**
     * Compute the rotation rate needed to produce the provided velocity for a wheel
     * of the given radius.
     * 
     * @param velocityFPS
     * @param radiusInches
     * @return
     */
    public double computeRotationRPM(double velocityFPS, double radiusInches);

    /**
     * Compute the time of flight for the given initial velocity and launch angle.
     * 
     * @param velocityFPS - launch velocity in feet per second
     * @param angleDeg - launch angle in degrees
     * @return time of flight in seconds
     */
    public double computeTimeSecs(double velocityFPS, double angleDeg);
    
    /**
     * Compute the maximum height the ball will reach before falling back down.
     * 
     * @param velocityFPS
     * @param angleDeg
     * @return
     */
    public double computeMaxHeightFeet(double velocityFPS, double angleDeg);
    
    /**
     * Compute the range for a projectile launched with the given velocity
     * and angle.  0 is horizontal, 90 is straight up.
     * 
     * @param velocityFPS - velocity of ball in feet per second
     * @param angleDeg - angle of launcher in degrees
     * @return
     */
    public double computeRangeFeet(double velocityFPS, double angleDeg);
    
    /**
     * Compute the maximum range in feet that ball can go with the given
     * initial velocity.
     * 
     * @param velocityFPS - velocity in feet per second
     * @return
     */
    public double computeMaxRangeFeet(double velocityFPS);
    
    /**
     * Computes the launch angle in degrees to achieve the desired range give an
     * initial velocity.  Some trajectories can be achieved with two angles, this
     * should return the lower of the two.
     * 
     * @param velocityFPS - launch velocity in feet per second
     * @param rangeFeet - desired range in feet
     * @return
     * @throws TrajectoryException - throws exception if range can't be met with given velocity
     */
    public double computeAngleDeg(double velocityFPS, double rangeFeet) throws TrajectoryException;
    
    /**
     * Compute the range for a projectile launched with the given velocity
     * and angle.  0 is horizontal, 90 is straight up. This is to hit the
     * high goal at the top of the arc.
     * 
     * @param velocityFPS - velocity of ball in feet per second
     * @param angleDeg - angle of launcher in degrees
     * @throws TrajectoryException - throws exception if range can't be met with given velocity
     * @return
     */
    public double computeHighGoalRangeFeet(double velocityFPS, double angleDeg) throws TrajectoryException;
    
    /**
     * Compute the maximum range in feet that ball can go with the given
     * initial velocity.  This is to hit the high goal at the top of the arc.
     * 
     * @param velocityFPS - velocity in feet per second
     * @throws TrajectoryException - throws exception if range can't be met with given velocity
     * @return
     */
    public double computeMaxHighGoalRangeFeet(double velocityFPS) throws TrajectoryException;
    
    /**
     * Computes the launch angle in degrees to achieve the desired range give an
     * initial velocity.  Some trajectories can be achieved with two angles, this
     * should return the lower of the two.  This is to hit the high goal at the
     * top of the arc.
     * 
     * @param velocityFPS - launch velocity in feet per second
     * @param rangeFeet - desired range in feet
     * @return
     * @throws TrajectoryException - throws exception if range can't be met with given velocity
     */
    public double computeHighGoalAngleDeg(double velocityFPS, double rangeFeet) throws TrajectoryException;
    
    /**
     * Determine the min and max angle that can be used to hit the high goal.
     * [-1,-1] or [0,0] mean no valid angles.
     * 
     * @param velocityFPS
     * @return
     */
    public double[] computeHighGoalMinMaxAngleDeg(double velocityFPS);
    
    /**
     * Determine the min and max range that can be used to hit the high goal.
     * [-1,-1] or [0,0] mean no valid ranges.
     * 
     * @param velocityFPS
     * @return
     */
    public double[] computeHighGoalMinMaxRangeFeet(double velocityFPS);
}
