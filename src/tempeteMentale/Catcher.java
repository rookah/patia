package tempeteMentale;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class Catcher {
	private final EV3LargeRegulatedMotor gripMotor;
	private final Port gripMotorPort;
	private final Port gripSensorPort;
	private final EV3TouchSensor bumperSensor;
	
	public Catcher(){
		gripMotorPort = MotorPort.B;
		gripMotor = new EV3LargeRegulatedMotor(gripMotorPort);
		gripSensorPort = LocalEV3.get().getPort("S2");
		bumperSensor = new EV3TouchSensor(gripSensorPort);
	}
	
	public void catchPuck(){		
		gripMotor.setSpeed(600);
		gripMotor.backward();
		Delay.msDelay(100);
		gripMotor.stop();
	}
	
	public void releasePuck(){
		gripMotor.setSpeed(600);
		gripMotor.forward();
		Delay.msDelay(100);
		gripMotor.stop();
	}
	
	public EV3TouchSensor getBumperSensor(){
		return bumperSensor;
	}
}
