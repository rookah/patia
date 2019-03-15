package tests;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.robotics.navigation.Waypoint;
import tempeteMentale.*;

public class TestEvitement {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sailor sailor = new Sailor();
		EV3 ev3brick = (EV3) BrickFinder.getLocal();

		Keys buttons = ev3brick.getKeys();
		if (sailor.pinceFermee) {
			buttons.waitForAnyPress();
			sailor.getCatcher().releasePuck();
		}
		buttons.waitForAnyPress();
		sailor.moveTo(new Waypoint(100, 0));
		buttons.waitForAnyPress();
	}

}
