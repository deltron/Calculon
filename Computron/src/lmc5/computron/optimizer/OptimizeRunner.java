package lmc5.computron.optimizer;

import java.util.LinkedList;

import lmc5.computron.optimizer.algorithm.GeneralParallelHillclimbingAlgorithm;
import lmc5.computron.optimizer.problems.TradeSystemProblem;

import com.oat.Algorithm;
import com.oat.AlgorithmExecutor;
import com.oat.RunProbe;
import com.oat.StopCondition;
import com.oat.domains.cfo.CFODomain;
import com.oat.stopcondition.EvaluationsStopCondition;

public class OptimizeRunner {
	private TradeSystemProblem problem;
	private StopCondition stopCondition;
	private Algorithm algorithm;

	public OptimizeRunner(TradeSystemProblem problem, StopCondition stopCondition, Algorithm algorithm) {
		this.problem = problem;
		this.stopCondition = stopCondition;
		this.algorithm = algorithm;
	}

	/**
	 * Default runner
	 */
	public OptimizeRunner(TradeSystemProblem problem) {
		this(problem, new EvaluationsStopCondition(5000), new GeneralParallelHillclimbingAlgorithm());
	}

	public void run() {
		CFODomain domain = new CFODomain();

		// run the algorithm
		AlgorithmExecutor executor = new AlgorithmExecutor(problem, algorithm, stopCondition);
		LinkedList<RunProbe> probes = domain.loadDomainRunProbes();
		executor.addRunProbes(probes);
		try {
			executor.executeAndWait();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		// problem details
		System.out.println("Problem Details");
		System.out.println(" >Problem: " + problem.getName());
		System.out.println(" >Problem Details: " + problem.getDetails());
		System.out.println(" >Problem Configuration: " + problem.getConfigurationDetails());
		// algorithm details
		System.out.println("Algorithm Details");
		System.out.println(" >Algorithm: " + algorithm.getName());
		System.out.println(" >Algorithm Details: " + algorithm.getDetails());
		System.out.println(" >Algorithm Configuration: " + algorithm.getConfigurationDetails());
		// run details
		System.out.println("Run Details");
		for (RunProbe probe : probes) {
			System.out.println(" >" + probe.getName() + ": " + probe.getProbeObservation());
		}
	}
}
