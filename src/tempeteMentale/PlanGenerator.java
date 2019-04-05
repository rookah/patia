package tempeteMentale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.HashMap;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.parser.Domain;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.parser.Parser;
import fr.uga.pddl4j.parser.Problem;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.Plan;

public class PlanGenerator implements PlanGeneratorInterface {
	private Domain domain;
	private Problem problem;
	private CodedProblem currentPb;
	private Plan currentPlan;
	private PuckPosition puckPosition;

	private int opIndex;

	/**
	 * Strings for problem generation
	 */
	private final String object_string = "(:objects\n start - node\n goal - node\n a1 - node\n a2 - node\n" +
			" a3 - node \n b1 - node \n b2 - node \n b3 - node \n c1 - node \n c2 - node \n c3 - node \n";
	private final String link_string = "(link start a1)\n  (link start a2)\n (link start a3)\n" +
			"(link a1 b1)\n (link b1 a1)\n (link a2 b2)\n (link b2 a2)\n (link a3 b3)\n (link b3 a3)\n" +
			" (link b1 c1)\n (link c1 b1)\n (link b2 c2)\n (link c2 b2)\n (link b3 c3)\n" +
			" (link c3 b3)\n (link c1 goal)\n (link goal c1)\n (link c2 goal)\n (link goal c2)\n (link c3 goal)\n (link goal c3)\n"; 

	/**
	 * Instance of Parser to parse domain/problem files
	 */
	private final Parser pddl_parser = new Parser();

	/**
	 * Generic constructor
	 */
	public PlanGenerator() {
		File domain_file = new File("pddl/puckretriever/domain.pddl");
		try {
			pddl_parser.parseDomain(domain_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		domain = pddl_parser.getDomain();
		opIndex = 0;
		puckPosition = new PuckPosition();
	}

	/**
	 * Generate the problem file depending on the status of the field (puck position)
	 */
	private void GenerateProblem() {
		puckPosition.updatePos();
		String problem_str = "(define (problem puck-retrieving-problem)\n";
		problem_str = problem_str.concat("(:domain puck-retriever)\n");
		problem_str = problem_str.concat("(:requirements :strips :typing)\n");
		problem_str = problem_str.concat(object_string);
		for (String p : puckPosition.getPucks().keySet()) {
			problem_str = problem_str.concat("p" + p + " - puck\n");
		}
		problem_str = problem_str.concat(")\n");
		problem_str = problem_str.concat("(:init \n");
		problem_str = problem_str.concat("(gripper-empty)\n(goal-node goal)\n" + "(at start)\n");
		problem_str = problem_str.concat(link_string);
		for (String p : puckPosition.getPucks().keySet()) {
			problem_str = problem_str.concat("(puck-at " + "p" + p + " " + p + ")\n"); 
		}
		problem_str = problem_str.concat(")\n");
		problem_str = problem_str.concat("(:goal \n(and \n");
		for (String p : puckPosition.getPucks().keySet()) {
			problem_str = problem_str.concat("(puck-at " + "p" + p + " goal)\n"); 
		}
		problem_str = problem_str.concat(")))");

		File tmp = new File("pddl/puckretriever/problem.pddl");
		PrintWriter pw;
		try {
			pw = new PrintWriter(tmp);
			pw.print(problem_str);
			pw.close();
			pddl_parser.parseProblem(tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}

		problem = pddl_parser.getProblem();
	}

	/**
	 * Generate the PDDL encoded problem and plan
	 */
	void GeneratePlan() {
		final ProblemFactory factory = ProblemFactory.getInstance();
		ErrorManager errorManager = null;
		try {
			errorManager = factory.parseFromString(domain.toString(), problem.toString());
		} catch (IOException e) {
			System.out.println("Unexpected error when parsing the PDDL planning problem description.");
			System.exit(0);
		}
		if (!errorManager.isEmpty()) {
			errorManager.printAll();
			System.exit(0);
		}

		final CodedProblem pb = factory.encode();
		if (pb == null) {
			return;
		}
		if (!pb.isSolvable()) {
			System.out.println("Goal can be simplified to FALSE. No search will solve it.");
			return;
		}

		final HSP planner = new HSP(2500, HSP.DEFAULT_HEURISTIC, HSP.DEFAULT_WEIGHT, HSP.DEFAULT_STATISTICS, HSP.DEFAULT_TRACE_LEVEL);
		Plan plan = planner.search(pb);

		currentPb = pb;
		currentPlan = plan;
		opIndex = 0;
	}
	
	/**
	 * Update the problem
	 */
	public void newPlan() throws RemoteException{
		GenerateProblem();
		GeneratePlan();
	}
	
	/**
	 * Get the next action to do
	 * @return ret Action to do (string)
	 */
	public String getNextOperation() throws RemoteException {
		if (opIndex >= currentPlan.actions().size())
			return null;
		String ret = currentPb.toShortString((BitOp) currentPlan.actions().get(opIndex));
		opIndex++;
		return ret;
	}
	
	/**
	 * Get position on the board of a puck from its label
	 */
	public Point getPositionsFromPuck(String puckLabel) throws RemoteException{
		return this.puckPosition.getPucks().get(puckLabel);
	}
	
	public Point getGoalNode() throws RemoteException {
		return this.puckPosition.getGoalNode();
	}
}

