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
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
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
	private SampleProvider sonicDistance;
	private Catcher catcher;
	public boolean pinceFermee;

	
	public Sailor(){
		ev3Brick = (EV3) BrickFinder.getLocal();
		sonicSensorPort = ev3Brick.getPort("S4");
		colorSensorPort = ev3Brick.getPort("S3");
		sonicSensor = new EV3UltrasonicSensor(sonicSensorPort);
		colorSense = new EV3ColorSensor(colorSensorPort);
		left_motor = new EV3LargeRegulatedMotor(MotorPort.A);
		right_motor = new EV3LargeRegulatedMotor(MotorPort.D);
		wheel_left = WheeledChassis.modelWheel(left_motor, 5.5).offset(-6.9);
		wheel_right = WheeledChassis.modelWheel(right_motor, 5.5).offset(6.9);
		chassis = new WheeledChassis(new Wheel[] { wheel_left, wheel_right }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		navigator = new Navigator(pilot);
		catcher = new Catcher();
		pinceFermee = false;
	}
	
	public void moveTo(Waypoint wp){
		navigator.goTo(wp);
		while(!navigator.pathCompleted()){
			if(obstacleInFront()) {
				navigator.stop();
				pilot.rotate(-90);
				pilot.travel(40);	
			}
			navigator.followPath();
			SensorMode toucher = catcher.getBumperSensor().getTouchMode();
			float[] sample = new float[toucher.sampleSize()];
			toucher.fetchSample(sample, 0);
			if (sample[0] == 1 && !pinceFermee){
				navigator.stop();
				pilot.travel(2);
				catcher.catchPuck();
				pinceFermee = true;
				//Request new plan
				navigator.clearPath();
				navigator.goTo(new Waypoint(30,30));
			}
		}
		if (pinceFermee) {
			catcher.releasePuck();
			/*Sound.playTone(220, 50, 30);
			Sound.playTone(220, 50, 30);
			Sound.playTone(220, 50, 30);
			Sound.playTone(220, 100, 30);
			Sound.playTone(170, 100, 30);
			Sound.playTone(195, 100, 30);
			Sound.playTone(220, 100, 30);
			Sound.playTone(195, 50, 50);
			Sound.playTone(220, 200, 50);*/
			Sound.playTone(440, 100, 50);
			Sound.playTone(880, 100, 60);
			pilot.travel(-20);
			pinceFermee = false;
			//Request new plan
		}
	}
	
	public boolean obstacleInFront(){
		//TextLCD lcddisplay = ev3Brick.getTextLCD();
		sonicDistance = sonicSensor.getDistanceMode();
		float[] sample = new float[sonicDistance.sampleSize()];
		sonicDistance.fetchSample(sample, 0);
		//lcddisplay.drawString("distance: " + sample[0], 0, 1);
		return sample[0] < 0.2;
		
	}
	
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
	
	public Catcher getCatcher(){
		return catcher;
	}
	
}
