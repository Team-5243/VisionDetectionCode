package team5243.ex.sim;

public class Integrator 
{
	public static final double onesixth = 1.0d/6.0d;
	
	public static double[] singleStep(double[] y0, double h, Func ode) 
	{
		int size = y0.length;
		double halfH = h*0.5;
		
		// temporarily hold dydt, later used for y1
		double y1[] = new double[size];
		
		double k1[] = ode.deriv(y0);
		
		for(int i=0; i<size; i++)
		{
			y1[i] = y0[i]+halfH*k1[i];
		}
		
		double k2[] = ode.deriv(y1);
		
		for(int i=0; i<size; i++)
		{
			y1[i] = y0[i]+halfH*k2[i];
		}
		
		double k3[] = ode.deriv(y1);
		
		for(int i=0; i<size; i++)
		{
			y1[i] = y0[i]+h*k3[i];
		}
		
		double k4[] = ode.deriv(y1);
		
		y1[0] = y0[0]+h;		
		for(int i=1; i<size; i++)
		{
			y1[i] = y0[i]+h*onesixth*(k1[i]+2.0*k2[i]+2.0*k3[i]+k4[i]);
		}
		
		return y1;		
	}
	
	public static interface Func
	{
		public double[] deriv(double vals[]);
	}
	
}
