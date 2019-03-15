package tempeteMentale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.parser.Domain;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.parser.Parser;
import fr.uga.pddl4j.parser.Problem;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.Plan;

public class PlanGenerator {
	private Domain domain;
	private Problem problem;
	//private Plan plan;
	
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
	}
	
	/**
	 * Generate the problem file depending on the status of the field
	 */
	public void GenerateProblem() {
		// Parse the "base problem" file to build upon
		File base_problem_file = new File("pddl/puckretriever/problem_sample.pddl");
		//Problem tmp_problem;
		try {
			pddl_parser.parseProblem(base_problem_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		problem = pddl_parser.getProblem();
		
		//TODO field info
	}
	
	public void GeneratePlan() {
		final ProblemFactory factory = ProblemFactory.getInstance();
		ErrorManager errorManager = null;
		try {
			errorManager = factory.parse(new File("pddl/puckretriever/domain.pddl"), new File("pddl/puckretriever/problem_sample.pddl"));
		} catch (IOException e) {
			System.out.println("Unexpected error when parsing the PDDL planning problem description.");
			System.exit(0);
		}
		if (!errorManager.isEmpty()) {
			errorManager.printAll();
			System.exit(0);
		} else {
			System.out.println("Parsing domain file and problem file done successfully");
		}
		final CodedProblem pb = factory.encode();
		System.out.println("Encoding problem done successfully (" 
				+ pb.getOperators().size() + " ops, "
				+ pb.getRelevantFacts().size() + " facts).");

		if (!pb.isSolvable()) {
			System.out.println("Goal can be simplified to FALSE. No search will solve it.");
			System.exit(0);
		}

		final HSP planner = new HSP(2000, HSP.DEFAULT_HEURISTIC, HSP.DEFAULT_WEIGHT, HSP.DEFAULT_STATISTICS, HSP.DEFAULT_TRACE_LEVEL);

		final Plan plan = planner.search(pb);
		if (plan != null) {
		    System.out.println("Found plan as follows:");
		    System.out.println(pb.toString(plan));
		} else {
		    System.out.println("No plan found.");
		}
	}
	
	public static void main(String args[]) {
		PlanGenerator p = new PlanGenerator();
		//p.GenerateProblem();
		p.GeneratePlan();
	}

}

