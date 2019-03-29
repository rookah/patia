package tempeteMentale;

public class PlanInterpreter {
	
	private PlanGenerator p;
	
	public PlanInterpreter() {
		p = new PlanGenerator();
	}
	
	public void interpreter() {
		p.newPlan();
		String action ;
		String actual; 
		String  destination; 
		String[] parse = null; //tableau de chaînes
		
		while (p.getNextOperation() != null) {
			String result =  p.getNextOperation();
		    parse = result.split("\\s+");
		    action = parse[0];
		    actual = parse[1];
		    destination = parse[2];
		    switch(action) {
			    case "pick-up":
			    	break ;
			    case "move": 
			    	break; 
			    case "drop-down": 
			    	break; 
			    default: 
			    	break;   
		    }
		}
	}
	
	public static void main(String args[]) {
		PlanInterpreter planInterpreter = new PlanInterpreter(); 
		planInterpreter.interpreter();
	}
}
