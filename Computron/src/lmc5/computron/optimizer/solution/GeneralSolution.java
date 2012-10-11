package lmc5.computron.optimizer.solution;

import java.util.ArrayList;
import java.util.List;

import com.oat.Solution;

/**
 * General solution that uses objects (eg. Double, Integer, Boolean)
 * 
 * 
 */
public class GeneralSolution extends Solution {
	/**
	 * Coordinate for the solution in an N-dimensional space
	 */
	protected final List<Number> coordinate;

	/**
	 * Copy Constructor Creates a new solution instance with a COPY of the
	 * provided solutions coordinates
	 * 
	 * @param parent
	 */
	public GeneralSolution(GeneralSolution parent) {
		this(new ArrayList<Number>(parent.coordinate));
	}

	/**
	 * Normal Constructor Creates a new function optimization solution with the
	 * provided coordinate
	 * 
	 * @param aCoord
	 */
	public GeneralSolution(List<Number> aCoord) {
		coordinate = aCoord;
	}

	public void evaluated(double aCost) {
		super.evaluated(aCost);
	}

	public void setNormalizedRelativeScore(double n) {
		super.setNormalizedRelativeScore(n);
	}

	public List<Number> getCoordinate() {
		return coordinate;
	}

	public boolean equals(Object o) {
		if (o instanceof GeneralSolution) {
			GeneralSolution s = (GeneralSolution) o;
			return coordinate.equals(s.coordinate);
		} else {
			return false;
		}
	}

	public String toString() {
		String s = "[";
		for (Number n : coordinate) {
			s += n + ",";
		}
		s = s.substring(0, s.length() - 1) + "]";
		return s;
	}

}
