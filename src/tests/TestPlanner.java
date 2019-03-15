package tests;

import java.io.File;
import java.io.IOException;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.statespace.StateSpacePlanner;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.Plan;

public class TestPlanner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ProblemFactory factory = ProblemFactory.getInstance();
		ErrorManager errorManager = null;
		try {
			errorManager = factory.parse(new File("pddl/puckretriever/domain.pddl"), new File("pddl/puckretriever/problem_sample.pddl"));
			//errorManager = factory.parse(new File("src/tests/logistics.pddl"), new File("src/tests/problem.pddl"));
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
		Plan plan = planner.search(pb);
		if (plan != null) {
			System.out.println("Found plan as follows:");
			System.out.println(pb.toString(plan));
		} else {
			System.out.println("No plan found.");
		}
	}

}
