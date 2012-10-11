/*
 Optimization Algorithm Toolkit (OAT)
 http://sourceforge.net/projects/optalgtoolkit
 Copyright (C) 2006  Jason Brownlee

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package lmc5.computron.optimizer.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import lmc5.computron.optimizer.dimension.Dimension;
import lmc5.computron.optimizer.problems.GeneralProblem;
import lmc5.computron.optimizer.solution.GeneralSolution;
import lmc5.computron.optimizer.util.GeneralAlgorithmUtils;
import lmc5.computron.optimizer.util.GeneralRandomUtils;

import com.oat.Algorithm;
import com.oat.InitialisationException;
import com.oat.InvalidConfigurationException;
import com.oat.Problem;

/**
 * Modified...
 * 
 * 
 * Type: ParallelHillclimbingAlgorithm<br/> Date: 15/03/2006<br/> <br/>
 * Description: A Comparison of Parallel and Sequential Niching Methods (1995)
 * <br/>
 * 
 * @author Jason Brownlee
 * 
 * <pre>
 *           Change History
 *           ----------------------------------------------------------------------------
 *           22/12/2006   JBrownlee   Random moved to method variable rather than instance variable
 *                                    Fixed bug after children evaluation where children population
 *                                    was not getting trimmed of un-evaluated solutions
 *           
 * </pre>
 */
public class GeneralParallelHillclimbingAlgorithm extends Algorithm {
	protected List<Number> stepSize = new ArrayList<Number>();

	// user parameters
	protected long seed = System.currentTimeMillis();
	protected int popsize = 100;
	protected double initialStepSizeRatio = 0.1;

	public String getDetails() {
		return "Parallel Hillclimbing Algorithm: "
				+ "as described in: Samir W. Mahfoud. A Comparison of Parallel and Sequential Niching Methods. Larry Eshelman. Proceedings of the Sixth International Conference on Genetic Algorithms San Francisco, CA, USA: Morgan Kaufmann Publishers Inc.; 1995: 136-143. "
				+ "the initialStepSizeRatio parameter is a ratio of the objective function range in each dimension.";
	}

	protected void internalExecuteAlgorithm(Problem p) {
		Random r = new Random(seed);
		GeneralProblem problem = (GeneralProblem) p;
		LinkedList<GeneralSolution> pop = new LinkedList<GeneralSolution>();
		// prepare initial population
		while (pop.size() < popsize) {
			List<Number> coord = GeneralRandomUtils.randomPointInRange(r, problem.getDimensions());
			pop.add(new GeneralSolution(coord));
		}
		// evaluate
		p.cost(pop);

		// run algorithm until there are no evaluations left
		int j = 0;
		int changes = 0;
		int direction = 0;
		while (p.canEvaluate()) {
			triggerIterationCompleteEvent(p, pop);
			LinkedList<GeneralSolution> children = generate(pop, direction, j, (GeneralProblem) p);
			p.cost(children);
			if (!p.canEvaluate()) {
				continue;
			}
			LinkedList<GeneralSolution> n = new LinkedList<GeneralSolution>();
			for (int i = 0; i < children.size(); i++) {
				if (p.isBetter(children.get(i), pop.get(i))) {
					// only accept improvements
					n.add(children.get(i));
					changes++;
				} else {
					n.add(pop.get(i));
				}
			}
			pop = n;

			// positive and negative for this axis
			if (++direction >= 2) {
				// both directions have been done for this axis
				direction = 0;
				// check if all axis have been processed
				if (++j >= problem.getDimensions().size()) {
					// check for any changes
					if (changes == 0) {
						// adjust step sizes - current has reached its
						// usefulness
						List<Number> newStepSize = new ArrayList<Number>();
						for (Number step : stepSize) {
							step = step.doubleValue() / 2.0;
							newStepSize.add(step);
						}
						stepSize = newStepSize;
					}
					j = 0;
					changes = 0;
				}
			}
		}
	}

	protected LinkedList<GeneralSolution> generate(LinkedList<GeneralSolution> pop, int dir, int axis, GeneralProblem p) {
		LinkedList<GeneralSolution> n = new LinkedList<GeneralSolution>();
		for (GeneralSolution s : pop) {   
			List<Number> nCoord = new ArrayList<Number>(s.getCoordinate());

			if (dir == 0) {
				Number newCoord = s.getCoordinate().get(axis).doubleValue() + stepSize.get(axis).doubleValue();
				nCoord.set(axis, newCoord);
			} else {
				Number newCoord = s.getCoordinate().get(axis).doubleValue() - stepSize.get(axis).doubleValue();
				nCoord.set(axis, newCoord);
			}
			
			GeneralAlgorithmUtils.fixCoordBounds(nCoord, p.getDimensions());
			GeneralSolution ns = new GeneralSolution(nCoord);
			n.add(ns);
		}

		return n;
	}

	protected void prepareStepSize(GeneralProblem p) {
		for (Dimension d : p.getDimensions()) {
			Double step = (d.getMin().doubleValue() - d.getMax().doubleValue()) * initialStepSizeRatio;
			stepSize.add(step);
		}
	}

	public String getName() {
		return "Modified Parallel Hillclimbing";
	}

	public void initialiseBeforeRun(Problem p) throws InitialisationException {
		// prepare the step size
		prepareStepSize((GeneralProblem) p);
	}

	public void validateConfiguration() throws InvalidConfigurationException {
		// popsize
		if (popsize <= 0) {
			throw new InvalidConfigurationException("Invalid popsize " + popsize);
		}
		// step size
		if (initialStepSizeRatio > 1 || initialStepSizeRatio < 0) {
			throw new InvalidConfigurationException("Invalid initialStepSizeRatio " + initialStepSizeRatio);
		}

	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getPopsize() {
		return popsize;
	}

	public void setPopsize(int popsize) {
		this.popsize = popsize;
	}

	public double getInitialStepSizeRatio() {
		return initialStepSizeRatio;
	}

	public void setInitialStepSizeRatio(double initialStepSizeRatio) {
		this.initialStepSizeRatio = initialStepSizeRatio;
	}

}
