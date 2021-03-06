/**
 * Class to run the robot's actions and deplacements
 * depending on its goal, position and environment 
 * @author bozzo1337 Xaalan rookah
 */


package tempeteMentale;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.hardware.Sound;

public class Sailor {
	private final EV3 ev3Brick;
	private final EV3UltrasonicSensor sonicSensor;
	private final Port sonicSensorPort;
	private final EV3ColorSensor colorSense;
	private final Port colorSensorPort;
	private final EV3LargeRegulatedMotor left_motor;
	private final EV3LargeRegulatedMotor right_motor;
	private final Wheel wheel_left;
	private final Wheel wheel_right;
	private final Chassis chassis;
	private final MovePilot pilot;
	private final Navigator navigator;
	private final PoseProvider posProv;
	private SampleProvider sonicDistance;
	private Catcher catcher;
	public boolean pinceFermee;
	private PlanInterpreter pi;

	
	public Sailor(PlanInterpreter pi){
		ev3Brick = (EV3) BrickFinder.getLocal();
		sonicSensorPort = ev3Brick.getPort("S3");
		colorSensorPort = ev3Brick.getPort("S1");
		sonicSensor = new EV3UltrasonicSensor(sonicSensorPort);
		colorSense = new EV3ColorSensor(colorSensorPort);
		left_motor = new EV3LargeRegulatedMotor(MotorPort.B);
		right_motor = new EV3LargeRegulatedMotor(MotorPort.C);
		wheel_left = WheeledChassis.modelWheel(left_motor, 5.5).offset(-6.9);
		wheel_right = WheeledChassis.modelWheel(right_motor, 5.5).offset(6.9);
		chassis = new WheeledChassis(new Wheel[] { wheel_left, wheel_right }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		navigator = new Navigator(pilot);
		posProv = navigator.getPoseProvider();
		posProv.setPose(new Pose(103,290,270)); //Position Robot ligne noire, roues derriere ligne blanche
		catcher = new Catcher();
		pinceFermee = false;
		this.pi = pi;
		pilot.setLinearSpeed(pilot.getLinearSpeed()/2);
		pilot.setAngularSpeed(pilot.getAngularSpeed()/3);
	}
	
	/**
	 * Move to a given waypoint and perform an action depending on the environment
	 * @param wp destination waypoint
	 */
	public void moveTo(Waypoint wp, boolean release){
		navigator.goTo(wp);
		SensorMode toucher = catcher.getBumperSensor().getTouchMode();
		float[] sample = new float[toucher.sampleSize()];
		while(!navigator.pathCompleted()){
			if(obstacleInFront()) {
				navigator.stop();
				pilot.travel(-10);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			navigator.followPath();
			toucher.fetchSample(sample, 0);
			if (sample[0] == 1 && !pinceFermee){
				navigator.stop();
				pilot.travel(2);
				catcher.catchPuck();
				pinceFermee = true;
			}
		}
		if (pinceFermee && release) {
			catcher.releasePuck();
			Sound.playTone(440, 100, 50);
			Sound.playTone(880, 100, 60);
			pilot.travel(-20);
			pinceFermee = false;
		} else if (!pinceFermee){
			if (recherchePalets()) { 
				catcher.catchPuck();
				pinceFermee = true;
			} else {
				pi.requestNewPlan();
			}
		}
	}
	
	/**
	 * Try to find a puck in the surroundings
	 * @return true if the puck was found, else false
	 */
	public boolean recherchePalets() {
		SensorMode toucher = catcher.getBumperSensor().getTouchMode();
		float[] sample = new float[toucher.sampleSize()];
		int cycle = 0;
		int travelStep = 0;
		double angle = 10;
		double initialSpeed = pilot.getLinearSpeed();
		boolean rotateRight = true;
		boolean paletFound = false;
		toucher.fetchSample(sample, 0);
		
		while (!paletFound && cycle < 4) {
			pilot.setLinearSpeed(initialSpeed);
			pilot.travel(-30);
			pilot.rotate(angle);
			while (!paletFound && travelStep < 3) {
				pilot.setLinearSpeed(8);
				pilot.travel(10);
				toucher.fetchSample(sample, 0);
				paletFound = sample[0] == 1;
				travelStep++;
			}
			if (rotateRight) {
				angle = -(angle + 10);
				rotateRight = false;
			} else {
				angle = -(angle - 10);
				rotateRight = true;
			}
			travelStep = 0;
			cycle++;
		}
		pilot.setLinearSpeed(initialSpeed);
		return paletFound;
	}
	
	/**
	 * Check if there's something in front of the robot
	 * @return true if something is in front, else false
	 */
	public boolean obstacleInFront(){
		sonicDistance = sonicSensor.getDistanceMode();
		float[] sample = new float[sonicDistance.sampleSize()];
		sonicDistance.fetchSample(sample, 0);
		return sample[0] < 0.1;
	}
	
	/**
	 * Follow a given color line and search it if the line is lost
	 * @param color 
	 */
	public void followColor(int color) {
		TextLCD lcddisplay = ev3Brick.getTextLCD();
		colorSense.setFloodlight(false);
		int follow, search;
		int rotation, cycles;
		follow = color;
		cycles = 0;
		while (cycles < 10) {
			rotation = 1;
			search = colorSense.getColorID(); //make sure we are still on the line
			//line is found continue forward
			pilot.forward();
			while (search == follow) {
				search = colorSense.getColorID();
			}
			//line lost
			while (search != follow) {
				pilot.stop();
				pilot.rotate(rotation); //rotate right
				search = colorSense.getColorID();
				if (search == follow) {
					break ; //found line again exit loop
				}
				else {
					pilot.rotate(-rotation * 2);
					search = colorSense.getColorID();
					if (search == follow) {
						break ;
					}
					//found line again exit loop
					pilot.rotate(rotation);
					//rotate back to center
				}
				rotation+=1;
			}
			cycles++;
		}
	}
	
	/**
	 * Return the status of the catcher
	 * @return catcther
	 */
	public Catcher getCatcher(){
		return catcher;
	}
	
	/**
	 * Add a waypoint to the navigator
	 * @param wp waypoint to add
	 */
	public void addWaypoint(Waypoint wp) {
		navigator.addWaypoint(wp);
	}
	
	public Navigator getNavigator() {
		return this.navigator;
	}
}
