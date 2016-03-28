package team5243.ex.sim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SimPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	public BufferedImage bi = null;
	public int iw = 0;
	public int ih = 0;
	
	public double inchesPerPixel = 0;
	public double pixelsPerInch = 0;
	
	public int robotXvals[] = null;
	public int robotYvals[] = null;
	public int robotWidth = 0;
	public int robotHeight = 0;
	
	public double robotXFeet = 23.09;
	public double robotYFeet = 0;//7.0833;
	
	public int oLx = 0;  // starting x of launcher
	public int oLy = 0;  // starting y of launcher
	
	public double ballX0 = 0;
	public double ballY0 = 0;
	
	public List<double[]> trajectory1 = null;
	public List<double[]> trajectory2 = null;
	
	public int traj1Max = 0;
	public int traj2Max = 0;
	
	public boolean doAnimate = false;
	
	public SimPanel()
    {
    	super();
    	
    	try
    	{
    		bi = ImageIO.read(new java.io.File("data/half.png"));
    		iw = bi.getWidth();
    		ih = bi.getHeight();
    
    		// inches for half of the field
    		inchesPerPixel = 340.11/(double)iw;
    		pixelsPerInch = ((double)iw)/340.11;
    		
    		inchesPerPixel = 325.11/576.0d;
    		pixelsPerInch = 576.0d/325.11d;
    		
    				
        	Dimension dim = new Dimension(iw,400);
        	setPreferredSize(dim);
        	setSize(dim);
        	setMinimumSize(dim);
        	setMaximumSize(dim);
        	
        	initRobot();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
	
	public void paintComponent(Graphics gr)
	{
		Graphics2D g = (Graphics2D)gr;
		
		int h = getHeight();
		int w = getWidth();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.drawImage(bi, 0, h-ih, iw, ih,null);
		
		int x = (int)(12.0d*robotXFeet*pixelsPerInch);
		int y = h-(int)(12.0d*robotYFeet*pixelsPerInch);
		
		if(!doAnimate)
		{
			drawBoulder(g, x+oLx,y+oLy);
		}
		
		drawRobot(g,x,y);
		
		if(trajectory1 != null)
		{
			g.setColor(Color.GREEN);
			drawTrajectory(g,trajectory1,w,h,traj1Max);
		}
		if(trajectory2 != null)
		{
			g.setColor(Color.RED);
			drawTrajectory(g,trajectory2,w,h,traj2Max);
		}
	}
	
	public void drawTrajectory(Graphics2D g, List<double[]> vals, int width, int height, int trajMax)
	{
		int size = vals.size();
		if(size < 2) return;
		
		double px1 = 0;
		double py1 = 0;
		double px2 = 0;
		double py2 = 0;
		
		double c = 12.0d*pixelsPerInch;
		
		double da[] = vals.get(0);
		px2 = da[1]*c;
		py2 = height - da[2]*c;
		int skip = size/1001;
		if(skip < 1) skip = 1;
		
		
		for(int i=skip; i<trajMax; i+=skip)
		{
			px1 = px2;
			py1 = py2;
			da = vals.get(i);
			px2 = da[1]*c;
			py2 = height - da[2]*c;
			g.drawLine((int)px1, (int)py1, (int)px2, (int)py2);	
			if(px2 > width) break;
		}
		
		if(trajMax > 1)
		{
		px1 = px2;
		py1 = py2;
		da = vals.get(trajMax-1);
		px2 = da[1]*c;
		py2 = height - da[2]*c;
		g.drawLine((int)px1, (int)py1, (int)px2, (int)py2);	
		}
		drawBoulder(g,(int)px2, (int)py2);
	}
	
	public void drawBoulder(Graphics2D g, int x, int y)
	{
		double d = 10*pixelsPerInch; // 10 inch diameter
		double r = 0.5*d;
		double px = x-r;
		double py = y-r;
		
		g.setColor(Color.gray);
		g.fillArc((int)px, (int)py, (int)d, (int)d, 0, 360);
	}
	
	public void drawRobot(Graphics2D g, int x, int y)
	{
		g.translate(x, y);
		g.setColor(Color.black);
		g.fillPolygon(robotXvals, robotYvals, 5);
		g.setColor(Color.blue);
		Stroke s = g.getStroke();
		BasicStroke bs = new BasicStroke(2f);
		g.setStroke(bs);
		g.drawPolygon(robotXvals, robotYvals, 5);
		g.setStroke(s);
		g.setColor(Color.yellow);
		
		g.setFont(g.getFont().deriveFont(8f));
		int xo = robotWidth/4;
		g.drawString("5243", xo, -5);
		g.translate(-x, -y);
	}
	
	public void initRobot()
	{
		robotXvals = new int[5];
		robotYvals = new int[5];
		
		//first and last points are 0,0
		
		double ppi = pixelsPerInch;
		robotXvals[1]=(int)(21.84*ppi);
		robotYvals[1]=0;
		
		robotXvals[2]=(int)(30.44*ppi);
		robotYvals[2]=-(int)(9.482*ppi);
		
		robotXvals[3]=0;
		robotYvals[3]=-(int)(9.482*ppi);
		
		robotWidth = robotXvals[2];
		robotHeight = Math.abs(robotYvals[2]);
		
		oLx = robotXvals[1];
		oLy = robotYvals[2];
		
		ballY0 = 9.482/12.0d;
		ballX0 = 21.84/12.0d;
	}
	
	/**
	 * 
	 * @param xFeet
	 * @param yFeet
	 * @param height
	 * @param pxy
	 */
	public void convertToPixels(double xFeet, double yFeet, int height, int pxy[], int ox, int oy)
	{
		double x = xFeet*12.0d*pixelsPerInch;
		double y = yFeet*12.0d*pixelsPerInch;
		y=height-y+oy;
		x=x+ox;
		pxy[0]=(int)x;
		pxy[1]=(int)y;
	}
}
