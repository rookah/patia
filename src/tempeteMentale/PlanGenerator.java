package tempeteMentale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.parser.Domain;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.parser.Parser;
import fr.uga.pddl4j.parser.Problem;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.Plan;

public class PlanGenerator {
	private Domain domain;
	private Problem problem;
	private CodedProblem currentPb;
	private Plan currentPlan;
	
	private int opIndex;
	
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
	}
	
	/**
	 * Generate the problem file depending on the status of the field (puck position)
	 */
	private void GenerateProblem() {
		// Parse the "base problem" file to build upon
		File base_problem_file = new File("pddl/puckretriever/problem_sample.pddl");
		try {
			pddl_parser.parseProblem(base_problem_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		problem = pddl_parser.getProblem();
	}
	
	/**
	 * Generate the PDDL encoded problem and plan
	 */
	private void GeneratePlan() {
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
	 * Return the list of operations to do.
	 * @param plan the plan to extract actions from
	 * @return
	 */
	public List getPlanOperations(Plan plan) {
		return plan.actions();
	}
	
	public String getNextOperation() {
		if (opIndex > currentPlan.actions().size())
			return null;
		String ret = currentPb.toShortString((BitOp) currentPlan.actions().get(opIndex));
		opIndex++;
		return ret;
	}

	public static void main(String args[]) {
		PlanGenerator p = new PlanGenerator();
		p.GenerateProblem();
		p.GeneratePlan();
		System.out.println(p.getNextOperation());
		System.out.println(p.getNextOperation());
	}
}

