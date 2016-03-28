package team5243.ex.sim;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import team5243.ProjectileInfo;
import team5243.ProjectileInfoImpl;
import team5243.ex.SamplePIImpl;

public class SimApp extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	public SimPanel simPanel = null;
	public JButton btnSetPosition = null;
	public JButton btnCalcVel = null;
	public JButton btnReset = null;
	public JButton btnLaunch = null;
	
	public JButton btnCalcTraj = null;
	public JButton btnLaunchLow = null;
	public JButton btnLaunchHigh = null;
	
	public JTextField txtRPM = null;
	public JTextField txtRadIn = null;
	public JTextField txtVel = null;
	public JTextField txtAng = null;
	public JTextField txtRange = null;

	public JTextField txtLowAng = null;
	public JTextField txtLowRange = null;
	public JTextField txtHighAng = null;
	public JTextField txtHighRange = null;

	public double rangeToTower = 22.8;
	public double robotRangeFt = 0;
	public double robotXft = 0;
	public double angDeg = 0;
	public double vel = 0;
	public double rpm = 0;
	public double radIn = 0;
	public double lowRng = 0;
	public double lowAng = 0;
	public double highRng = 0;
	public double highAng = 0;
	
	public ProjectileInfo info = new SamplePIImpl();
	
	public DecimalFormat df = new DecimalFormat("0.###");
	
	public BallSim ballSim = new BallSim();
	
	public boolean doAnimate = true;
	public boolean isRunning = false;
	
	public JRadioButton radI1 = new JRadioButton("Impl1");
	public JRadioButton radI2 = new JRadioButton("Impl2");
	public ButtonGroup bg = new ButtonGroup();
	
    public SimApp()
    {
    	initGUI();
    	setDefaults();
    }
    
    protected void initGUI()
    {
    	setLayout(new BorderLayout());
    
    	simPanel = new SimPanel();
    	add(simPanel,BorderLayout.CENTER);
    	
    	JPanel pnl = buildPanel();
    	add(pnl,BorderLayout.WEST);
    	
    	pnl = buildInfoPanel();
    	add(pnl,BorderLayout.EAST);
    }
    
    protected JPanel buildPanel()
    {
    	btnSetPosition = new JButton("Set Position");
    	btnCalcVel = new JButton("Calculate Velocity");
    	btnReset = new JButton("Reset");
    	btnLaunch = new JButton("Launch");
    	
    	btnSetPosition.addActionListener(this);
    	btnCalcVel.addActionListener(this);
    	btnReset.addActionListener(this);
    	btnLaunch.addActionListener(this);
    	
    	txtRPM = new JTextField(6);
    	txtRadIn = new JTextField(6);
    	txtVel = new JTextField(6);
    	txtAng = new JTextField(6);
    	txtRange = new JTextField(6);
    	
    	JPanel pnl = new JPanel(new GridLayout(3,1));
    	
    	JPanel tmp = null;
    	JPanel tmp2 = null;
    	
    	// range
    	tmp = new JPanel(new GridLayout(2,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("Range"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Range (feet):"));
    	tmp2.add(txtRange);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnSetPosition);
    	tmp.add(tmp2);
    	pnl.add(tmp);
    	
    	// flywheel
    	tmp = new JPanel(new GridLayout(3,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("Flywheels"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Radius (inches):"));
    	tmp2.add(txtRadIn);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Rotation (rpm):"));
    	tmp2.add(txtRPM);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnCalcVel);
    	tmp.add(tmp2);
    	pnl.add(tmp);

    	// trajectory
    	tmp = new JPanel(new GridLayout(3,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("Trajectory"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Velocity (fps):"));
    	tmp2.add(txtVel);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Angle (deg):"));
    	tmp2.add(txtAng);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnLaunch);
    	tmp2.add(btnReset);
    	tmp.add(tmp2);
    	pnl.add(tmp);
    	
    	return pnl;
    }
    
    protected JPanel buildInfoPanel()
    {
    	btnCalcTraj = new JButton("Calculate");
    	btnLaunchLow = new JButton("Launch");
    	btnLaunchHigh = new JButton("Launch");
    	
    	btnCalcTraj.addActionListener(this);
    	btnLaunchLow.addActionListener(this);
    	btnLaunchHigh.addActionListener(this);
    	
    	bg.add(radI1);
    	bg.add(radI2);
    	radI1.setSelected(true);
    	
    	txtLowAng = new JTextField(6);
    	txtLowRange = new JTextField(6);
    	
    	txtHighAng = new JTextField(6);
    	txtHighRange = new JTextField(6);

    	JPanel pnl =new JPanel(new GridLayout(3,1));
    	
    	JPanel tmp = null;
    	JPanel tmp2 = null;

    	tmp = new JPanel(new GridLayout(2,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("Calculate Trajectories:"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(radI1);
    	tmp2.add(radI2);
    	tmp.add(tmp2);
    	
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnCalcTraj);
    	tmp.add(tmp2);
    	
    	pnl.add(tmp);
    	
    	tmp = new JPanel(new GridLayout(3,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("High Goal:"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Range (feet):"));
    	tmp2.add(txtHighRange);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Angle (deg):"));
    	tmp2.add(txtHighAng);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnLaunchHigh);
    	tmp.add(tmp2);
    	pnl.add(tmp);

    	
    	tmp = new JPanel(new GridLayout(3,1));
    	tmp.setBorder(BorderFactory.createTitledBorder("Low Goal:"));
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Range (feet):"));
    	tmp2.add(txtLowRange);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new GridLayout(1,2));
    	tmp2.add(new JLabel("Angle (deg):"));
    	tmp2.add(txtLowAng);
    	tmp.add(tmp2);
    	tmp2 = new JPanel(new FlowLayout());
    	tmp2.add(btnLaunchLow);
    	tmp.add(tmp2);
    	pnl.add(tmp);

    	
    	return pnl;
    }
    

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		grabTextValues();
		Object src = e.getSource();
		if(src == btnSetPosition)
		{
			reset();
			setRobotPosition();
		}
		else if(src == btnCalcVel)
		{
			calculateVelocity();
		}
		else if(src == btnReset)
		{
			reset();
		}
		else if(src == btnLaunch)
		{
			spawnLaunch();
		}
		else if(src == btnCalcTraj)
		{
			calculateTrajectories();
		}
		else if(src == btnLaunchLow)
		{
			launchLow();
		}
		else if(src == btnLaunchHigh)
		{
			launchHigh();
		}
	}
	
	public void calculateTrajectories()
	{
		double velFPS = this.vel;
		double rangeFeet = this.robotRangeFt;
		double angDeg = 0;
		double out[] = null;
		
		ProjectileInfo info = null;
		if(radI1.isSelected())
		{
			info = new ProjectileInfoImpl();
		}
		else if(radI2.isSelected())
		{
			info = new SamplePIImpl();
		}
		
		try
		{
			out = info.recommendLowGoalAngleAndRange(velFPS, rangeFeet);
			angDeg = out[0];
			rangeFeet = out[1];
			
			System.out.println("\nLow Goal Recommendation");
			System.out.println("Vel = " + velFPS + " fps");
			System.out.println("Rng = " + rangeFeet + " feet");
			System.out.println("Ang = " + angDeg + " deg");
			txtLowAng.setText(df.format(angDeg));
			txtLowRange.setText(df.format(rangeFeet));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		try
		{
			out = info.recommendHighGoalAngleAndRange(velFPS, rangeFeet);
			angDeg = out[0];
			rangeFeet = out[1];
			System.out.println("\nHigh Goal Recommendation");
			System.out.println("Vel = " + velFPS + " fps");
			System.out.println("Rng = " + rangeFeet + " feet");
			System.out.println("Ang = " + angDeg + " deg");
			txtHighAng.setText(df.format(angDeg));
			txtHighRange.setText(df.format(rangeFeet));			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void launchLow()
	{
		boolean setPos = false;
		if(robotRangeFt != lowRng)
		{
			setPos = true;
		}
		txtRange.setText(df.format(lowRng));
		robotRangeFt = lowRng;
		txtAng.setText(df.format(lowAng));
		angDeg = lowAng;
		if(setPos)setRobotPosition();
		spawnLaunch();
	}
	
	public void launchHigh()
	{
		boolean setPos = false;
		if(robotRangeFt != highRng)
		{
			setPos = true;
		}
		txtRange.setText(df.format(highRng));
		robotRangeFt = highRng;
		txtAng.setText(df.format(highAng));
		angDeg = highAng;
		if(setPos)setRobotPosition();
		spawnLaunch();
		
	}
	
	public void reset()
	{
		isRunning = false;
		simPanel.trajectory1=null;
		simPanel.trajectory2=null;
		simPanel.repaint();
	}
	
	public void spawnLaunch()
	{
		if(isRunning) return;
		
		isRunning = true;
		Runnable r = new Runnable(){
			public void run()
			{
				try
				{
					launch();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					isRunning = false;
				}
			}
		};
		
		Thread t = new Thread(r);
		t.start();
	}
	
	public void launch()
	{
		double dt = 0.0001;
		boolean hits1[] = new boolean[2];
		boolean hits2[] = new boolean[2];
		
		System.out.println("Start x = " + (robotXft+simPanel.ballX0));
		System.out.println("Start y = " + (simPanel.ballY0));
		List<double[]> vals = ballSim.simulateBall(vel, angDeg, robotXft+simPanel.ballX0, simPanel.ballY0, dt, false, hits1);
		//List<double[]> vals = ballSim.simulateBall(vel, angDeg, 0,0, dt, false, hits1);
		simPanel.trajectory1 = vals;
		simPanel.traj1Max = vals.size();
		System.out.println(vals.get(vals.size()-1)[1]);
		vals = ballSim.simulateBall(vel, angDeg, robotXft+simPanel.ballX0, simPanel.ballY0, dt, true, hits2);
		simPanel.trajectory2 = vals;
		simPanel.traj2Max = vals.size();
		System.out.println(vals.get(vals.size()-1)[1]);
		
		simPanel.doAnimate = doAnimate;
		
		if(!doAnimate)
		{
			simPanel.repaint();
		}
		else
		{
			int size = simPanel.traj1Max;
			int skip = size/1001;
			if(skip < 1) skip = 1;
			
			for(int i=0; i<size; i+=skip)
			{
				if(!isRunning) break;
				simPanel.traj1Max = i;
				simPanel.repaint();
				if(simPanel.trajectory1.get(i)[1]>35)break;
				try{Thread.sleep(5);}catch(Exception ex){}
			}
		}
		
		String msg = null;
		if(hits1[0] || hits2[0])
		{
			msg = "Low Goal Hit!";
		}
		else if(hits1[1] || hits2[1])
		{
			msg = "High Goal Hit!";
		}
		
		if(msg != null)
		{
			JOptionPane.showMessageDialog(null, msg);
		}
	}
	
	public void calculateVelocity()
	{
		vel = info.computeVelocityFPS(radIn, rpm);
		txtVel.setText(df.format(vel));
	}
	
	public void setDefaults()
	{
		txtRadIn.setText("2");
		txtRPM.setText("1800");
		txtVel.setText("30");
		txtRange.setText("10");
		txtAng.setText("45");
		grabTextValues();
		setRobotPosition();
		calculateVelocity();
	}

	public void grabTextValues()
	{
		robotRangeFt = getNum(txtRange,20);
		robotXft = rangeToTower-robotRangeFt;
		angDeg = getNum(txtAng,45);
		vel = getNum(txtVel,30);
		rpm = getNum(txtRPM,3000);
		radIn = getNum(txtRadIn,2);
		lowRng = getNum(txtLowRange,robotRangeFt);
		lowAng = getNum(txtLowAng,0);
		highRng = getNum(txtHighRange,robotRangeFt);
		highAng = getNum(txtHighAng,0);
	}
	
	public void setRobotPosition()
	{
		simPanel.robotXFeet=robotXft;
		simPanel.repaint();
	}
	
	public double getNum(JTextField txt, double defNum)
	{
		double out = 0;
		try
		{
			out = Double.parseDouble(txt.getText().trim());
		}
		catch(Exception ex)
		{
			out = defNum;
		}
		return out;
	}
	
    public static void main(String args[])
    {
    	SimApp sa = new SimApp();
    	
    	JFrame f = new JFrame("FRC 2016 - Projectile Sim");
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.setSize(800,600);
    	f.getContentPane().add(sa);
    	f.pack();
    	f.setVisible(true);
    }
}
