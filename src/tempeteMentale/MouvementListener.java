package tempeteMentale;

import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Move.MoveType;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;

public class MouvementListener implements MoveListener {

	boolean evitement;
	double wpX, wpY;
	Navigator nav;
	int nbRotate;
	
	public MouvementListener (double wpX, double wpY, Navigator nav){
		this.wpX = wpX;
		this.wpY = wpY;
		this.nav = nav;
		evitement = false;
		nbRotate = 0;
	}
	
	public void moveStarted(Move event, MoveProvider mp) {
		if(evitement){
			if(event.getMoveType() == MoveType.ROTATE){
				float angle = event.getAngleTurned();
				wpX = wpX * Math.cos(angle) - wpY * Math.sin(angle);
				wpY = wpX * Math.sin(angle) + wpY * Math.cos(angle);
			}
			if(event.getMoveType() == MoveType.TRAVEL){
				if(nbRotate %4 == 0){
					wpY = wpY - event.getDistanceTraveled();
				} else if(nbRotate%4 == 1){
					wpX = wpX + event.getDistanceTraveled();
				} else if(nbRotate%4 == 2){
					wpY = wpY + event.getDistanceTraveled();
				} else if(nbRotate%4 == 3){
					wpX = wpX - event.getDistanceTraveled();
				}
			}
		}	
	}

	public void moveStopped(Move event, MoveProvider mp) {

	}
	
	public void setEvitement(boolean b){
		this.evitement = b;
	}

	public double getWpX() {
		return wpX;
	}

	public double getWpY() {
		return wpY;
	}
	
	public void setNbRotate(int nbRotate){
		this.nbRotate = nbRotate;
	}
}
