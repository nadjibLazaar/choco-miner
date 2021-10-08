package core.constraints.closedpattern;

/*******************************************************************
 * This file is part of CPMiner project.
 *
 * 2018, COCONUT Team, LIRMM, Montpellier.
 *
 *******************************************************************/

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import dataset.parsers.Dataset;

/********************************************
 * 
 * ClosedPatternWC constraint
 * maintaining a weak consistency
 * 
 *******************************************/
public class ClosedPatternWC extends Constraint {

	/*******************************************
	 * 
	 * @param X binary item variables
	 * @param teta a relative frequency (%)
	 * @param d  Datset
	 * 
	 *******************************************/
	public ClosedPatternWC(BoolVar[] X, double teta, Dataset d) {
		super("ClosedPattern", new PropClosedPatternBacktrackableWC(X, teta, d));
	}

}
