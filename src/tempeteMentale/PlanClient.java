package tempeteMentale;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.GraphicsLCD;

public class PlanClient {

	public static void main(String[] args) {
		EV3 ev3brick = (EV3) BrickFinder.getLocal();
		GraphicsLCD lcd = ev3brick.getGraphicsLCD();

		Keys buttons = ev3brick.getKeys();

		PlanInterpreter planInterpreter = new PlanInterpreter();
		Sailor s = new Sailor(planInterpreter);
		String host = "192.168.1.40";
		System.setProperty("java.rmi.server.hostname", host);
		buttons.waitForAnyPress();
		try {
			PlanGeneratorInterface pgi = (PlanGeneratorInterface) Naming.lookup("rmi://192.168.1.40:8006/PlanService");
			planInterpreter.setPlanGenerator(pgi);

			buttons.waitForAnyPress();
			Point testGoal = pgi.getGoalNode();
			String testAff = "" + testGoal.coord1 + ", " + testGoal.coord2;
			lcd.drawString(testAff, 40, 40, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buttons.waitForAnyPress();
		planInterpreter.interpreter(s); 
		buttons.waitForAnyPress();
	}
}
