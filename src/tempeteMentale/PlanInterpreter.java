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
	private boolean requestNewPlan;
	
	public PlanInterpreter() {
		p = null;
		requestNewPlan = false;
	}
	
	public void interpreter(Sailor s) {
		try {
			p.newPlan();
			requestNewPlan = false;
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
		
		s.setGoal(new Waypoint(goal.getCoord1(), goal.getCoord2()));
		
		try {
			String result = p.getNextOperation();
			while (result != null && !requestNewPlan) {
				String nextOp =  p.getNextOperation();
			    parse = result.split("\\s+");
			    action = parse[0];
			    actual = parse[1];
			    destination = parse[2];
			    String[] parseNext = nextOp.split("\\s+");
			    String actionNext = parseNext[0];
			    String actualNext = parseNext[1];
			    String destinationNext = parseNext[2];
			    switch(action) {
				    case "pick-up":
				    	break;
				    case "move":
				    	if (actionNext.equals("pick-up")) {
				    		s.moveTo(new Waypoint(p.getPositionsFromPuck(destination).getCoord1(),p.getPositionsFromPuck(destination).getCoord2()));
				    	} else {
				    		s.getNavigator().goTo(new Waypoint(p.getPositionsFromNode(destination).getCoord1(),p.getPositionsFromNode(destination).getCoord2()));
				    	}
				    	break;
				    case "drop-down": 
				    	break;
				    default: 
				    	break;   
			    }
			    result = nextOp;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		interpreter(s);
	}
	
	public void setPlanGenerator(PlanGeneratorInterface planGeneratorInterface) {
		this.p = planGeneratorInterface;
	}
	
	public void requestNewPlan() {
		requestNewPlan = true;
	}
}
