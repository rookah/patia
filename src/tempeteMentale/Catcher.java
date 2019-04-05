/**
 * Classe de gestion de la manipulation de palets
 * @author bozzo1337 Xaalan
 */

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
	
	/**
	 * Ferme les pinces du robot pour attrapper un palet
	 */
	public void catchPuck(){		
		gripMotor.setSpeed(600);
		gripMotor.backward();
		Delay.msDelay(1600);
		gripMotor.stop();
	}
	/**
	 * Ouvre les pinces du robots pour relâcher un palet
	 */
	public void releasePuck(){
		gripMotor.setSpeed(600);
		gripMotor.forward();
		Delay.msDelay(1600);
		gripMotor.stop();
	}
	/**
	 * Récupère le capteur de contact du robot
	 * @return retourne le capteur de contact
	 */
	public EV3TouchSensor getBumperSensor(){
		return bumperSensor;
	}
}
