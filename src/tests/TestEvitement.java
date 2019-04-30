package tests;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.robotics.Color;
import lejos.robotics.navigation.Waypoint;

import tempeteMentale.*;

public class TestEvitement {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlanInterpreter pi = new PlanInterpreter();
		Sailor sailor = new Sailor(pi);
		EV3 ev3brick = (EV3) BrickFinder.getLocal();

		Keys buttons = ev3brick.getKeys();
		//buttons.waitForAnyPress();
		//sailor.getCatcher().releasePuck();
		while(true) {
			buttons.waitForAnyPress();
			sailor.getCatcher().catchPuck();
		}
		//sailor.moveTo(new Waypoint(90, 230));
		//buttons.waitForAnyPress();
	}

}
