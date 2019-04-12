package tempeteMentale;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.robotics.navigation.Waypoint;

public class PlanInterpreter {
	
	private PlanGeneratorInterface p;
	
	public PlanInterpreter() {
		p = null;
	}
	
	public void interpreter(Sailor s) {
		try {
			p.newPlan();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		String action ;
		String actual; 
		String  destination; 
		String[] parse = null; //tableau de chaînes
		
		Point goal;
		goal = null;
		try {
			goal = p.getGoalNode();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean firstMove = true;
		
		s.setGoal(new Waypoint(goal.getCoord1(), goal.getCoord2()));
		
		try {
			while (p.getNextOperation() != null) {
				String result =  p.getNextOperation();
			    parse = result.split("\\s+");
			    action = parse[0];
			    actual = parse[1];
			    destination = parse[2];
			    switch(action) {
				    case "pick-up":
				    	firstMove = true;
				    	break;
				    case "move":
				    	if (firstMove) {
				    		s.moveTo(new Waypoint(p.getPositionsFromPuck(destination).getCoord1(),p.getPositionsFromPuck(destination).getCoord2()));
				    		firstMove = false;
				    	} else {
				    		s.addWaypoint(new Waypoint(p.getPositionsFromPuck(destination).getCoord1(),p.getPositionsFromPuck(destination).getCoord2()));
				    	}
				    	break;
				    case "drop-down": 
				    	firstMove = true;
				    	break;
				    default: 
				    	break;   
			    }
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlanGenerator(PlanGeneratorInterface planGeneratorInterface) {
		this.p = planGeneratorInterface;
	}
}
