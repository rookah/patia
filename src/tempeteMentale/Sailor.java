package tempeteMentale;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;

public class Sailor {
	private final EV3 ev3Brick;
	private final EV3UltrasonicSensor sonicSensor;
	private final Port sonicSensorPort;
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
		sonicSensor = new EV3UltrasonicSensor(sonicSensorPort);
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
				catcher.catchPuck();
				pinceFermee = true;
				//Request new plan
			}
		}
		if (pinceFermee) {
			catcher.releasePuck();
			pilot.travel(-40);
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
	
	public Catcher getCatcher(){
		return catcher;
	}
	
}
