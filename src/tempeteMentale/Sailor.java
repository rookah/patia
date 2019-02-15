package tempeteMentale;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
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
	private float[] sample;

	
	public Sailor(){
		ev3Brick = (EV3) BrickFinder.getLocal();
		sonicSensorPort = ev3Brick.getPort("S4");
		sonicSensor = new EV3UltrasonicSensor(sonicSensorPort);
		sonicDistance = sonicSensor.getDistanceMode();
		float[] sample = new float[sonicDistance.sampleSize()];
		left_motor = new EV3LargeRegulatedMotor(MotorPort.A);
		right_motor = new EV3LargeRegulatedMotor(MotorPort.D);
		wheel_left = WheeledChassis.modelWheel(left_motor, 5.5).offset(-6.9);
		wheel_right = WheeledChassis.modelWheel(right_motor, 5.5).offset(6.9);
		chassis = new WheeledChassis(new Wheel[] { wheel_left, wheel_right }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		navigator = new Navigator(pilot);
	}
	
	public void moveTo(Waypoint wp){
		MouvementListener ml = new MouvementListener(wp.getX(), wp.getY(), navigator);
		pilot.addMoveListener(ml);
		navigator.goTo(wp);
		int nbRotate = 0;
		while(obstacleInFront()) {
			ml.setEvitement(true);
			navigator.rotateTo(90);
			nbRotate++;
			ml.setNbRotate(nbRotate);
			if(!obstacleInFront()){
				navigator.goTo(0, 20);
			}
		}
		ml.setEvitement(false);
		navigator.goTo(new Waypoint(ml.getWpX(), ml.getWpY()));
	}
	
	public boolean obstacleInFront(){
		if(sonicDistance != null){
			sonicDistance.fetchSample(sample, 0);
			return sample[0] < 20;
		}
		return false;
	}
	
}
