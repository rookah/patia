package leJOS;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;

public class ForwardThenGrip {
	
	static EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
	static EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.D);
	static EV3LargeRegulatedMotor PINCE_MOTOR = new EV3LargeRegulatedMotor(MotorPort.B);
	static Port PINCE_PORT = LocalEV3.get().getPort("S2");
	static EV3TouchSensor BUMPER_SENSOR = new EV3TouchSensor(PINCE_PORT);

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.5).offset(-6.9);
		Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.5).offset(6.9);
		
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		
		MovePilot pilot = new MovePilot(chassis);
		Navigator navtest = new Navigator(pilot);
		
		SensorMode toucher = BUMPER_SENSOR.getTouchMode();
		float[] sample = new float[toucher.sampleSize()];
		
		//A completer
		
		
	}

}
