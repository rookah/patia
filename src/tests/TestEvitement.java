package tests;

import lejos.robotics.navigation.Waypoint;
import tempeteMentale.*;

public class TestEvitement {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Catcher catcher = new Catcher();
		Sailor sailor = new Sailor();
		
		sailor.moveTo(new Waypoint(0, 50));

	}

}
