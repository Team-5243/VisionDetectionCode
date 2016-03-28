package team5243;

/**
 * 
 * @author 5243
 *
 */
public class TrajectoryException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public double preferredRange = 0d;
	public boolean isTooClose = false;
	public boolean isTooFar = false;
	public boolean isTooLow = false;
	public boolean isTooHigh = false;
	
	public TrajectoryException()
	{
		super();
	}
	
	public TrajectoryException(String msg)
	{
		super(msg);
	}
	
	public TrajectoryException(String msg, double range, boolean tooClose, boolean tooFar)
	{
		super(msg);
		preferredRange = range;
		isTooClose = tooClose;
		isTooFar = tooFar;
	}
	
	public void setTooHigh(boolean flag)
	{
		isTooHigh = flag;
	}
	
	public void setTooLow(boolean flag)
	{
		isTooLow = flag;
	}
	
	public double getPreferredRangeFeet()
	{
		return preferredRange;
	}
	
	public boolean isTooClose()
	{
		return isTooClose;
	}
	
	public boolean isTooFar()
	{
		return isTooFar;
	}
	
	public boolean isTooHigh()
	{
		return isTooHigh;
	}
	
	public boolean isTooLow()
	{
		return isTooLow;
	}
}
