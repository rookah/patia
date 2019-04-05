package tempeteMentale;

import lejos.robotics.navigation.Waypoint;

public class PlanInterpreter {
	
	private PlanGenerator p;
	
	public PlanInterpreter() {
		p = new PlanGenerator();
	}
	
	public void interpreter(Sailor s) {
		p.newPlan();
		String action ;
		String actual; 
		String  destination; 
		String[] parse = null; //tableau de chaînes
		
		Point goal = p.getGoalNode();
		boolean firstMove = true;
		
		s.setGoal(new Waypoint(goal.getCoord1(), goal.getCoord2()));
		
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
	}
	
	public static void main(String args[]) {
		PlanInterpreter planInterpreter = new PlanInterpreter();
		Sailor s = new Sailor();
		planInterpreter.interpreter(s);
	}
}
