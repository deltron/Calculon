package lmc5.computron.optimizer.problems;

import java.util.ArrayList;
import java.util.List;

import lmc5.computron.optimizer.dimension.Dimension;
import lmc5.computron.optimizer.solution.GeneralSolution;
import lmc5.computron.optimizer.util.GeneralAlgorithmUtils;

import com.oat.AlgorithmRunException;
import com.oat.Problem;
import com.oat.Solution;
import com.oat.SolutionEvaluationException;

public abstract class GeneralProblem extends Problem {

	/**
	 * dimensionality used (number of parameters)
	 */
	protected List<Dimension> dimensions = new ArrayList<Dimension>();

	/**
	 * Default Constructor
	 */
	public GeneralProblem() {
	}

	protected abstract void setupDimensions();

	public  List<Dimension> getDimensions() {
		return dimensions;
	}

	@Override
	public void checkSolutionForSafety(Solution solution) throws SolutionEvaluationException {
		if (solution instanceof GeneralSolution) {
			GeneralSolution gs = (GeneralSolution) solution;

			if (gs.getCoordinate().size() < dimensions.size()) {
				throw new AlgorithmRunException("Solution coordinate " + gs.getCoordinate() + " doesnt contain the desired number of dimensions " + dimensions);
			}

			for (int i = 0; i < dimensions.size(); i++) {
				if (!GeneralAlgorithmUtils.inBounds(gs.getCoordinate().get(i), dimensions.get(i).getMin(), dimensions.get(i).getMax())) {
					throw new AlgorithmRunException("Unable to evaluate, coordinate is out of function bounds (dimension [" + i + "])" + " val[" + gs.getCoordinate().get(i)
							+ "] max[" + dimensions.get(i).getMin() + "], val[" + gs.getCoordinate().get(i) + "], max[" + dimensions.get(i).getMax() + "].");
				}
			}
		} else {
			throw new SolutionEvaluationException();
		}
	}

	@Override
	public abstract boolean isMinimization();

	@Override
	protected double problemSpecificCost(Solution solution) throws SolutionEvaluationException {
		if (solution instanceof GeneralSolution) {
			return problemSpecificCost((GeneralSolution) solution);
		}

		throw new SolutionEvaluationException();
	}

	protected abstract double problemSpecificCost(GeneralSolution solution) throws SolutionEvaluationException;

	public abstract String getName();
}
