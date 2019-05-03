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
		String actual = "start"; 
		String  destination; 
		String[] parse = null; //tableau de chaînes

		try {
			String result = p.getNextOperation();
			String nextOp;
			while (result != null) {
				while (result != null && !requestNewPlan && (nextOp =  p.getNextOperation()) != null) {
					parse = result.split("\\s+");
					action = parse[0];
					actual = parse[1];
					destination = parse[2];
					String[] parseNext = nextOp.split("\\s+");
					String actionNext = parseNext[0];
					switch(action) {
					case "pick-up":
						break;
					case "move":
						if (actionNext.equals("pick-up")) {
							Point puckPos = p.getPositionsFromPuck(destination);
							s.moveTo(new Waypoint(puckPos.getCoord1(), puckPos.getCoord2()), false);
						} else if (actionNext.equals("drop-down")){
							s.moveTo(new Waypoint(p.getPositionsFromNode(destination).getCoord1(),p.getPositionsFromNode(destination).getCoord2()), true);
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
				if (requestNewPlan) {
					p.newPlan(actual);
					requestNewPlan = false;
					result = p.getNextOperation();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void setPlanGenerator(PlanGeneratorInterface planGeneratorInterface) {
		this.p = planGeneratorInterface;
	}

	public void requestNewPlan() {
		requestNewPlan = true;
	}
}
