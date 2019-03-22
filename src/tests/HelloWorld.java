package tests;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import tempeteMentale.EV3Client;

public class HelloWorld {
	public static void main(String[] args) {
		EV3Client clientPalets = new EV3Client();
		clientPalets.getPalets();		
	}
}