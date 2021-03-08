package constraints.closedpattern;
/*******************************************************************
 * This file is part of CPMiner project.
 *
 * 2018, COCONUT Team, LIRMM, Montpellier.
 *
 *******************************************************************/

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

/********************************************
 * 
 * ClosedPattern Constraint 
 * maintaining Domain Consistency
 * 
 *******************************************/


public class ClosedPatternDC extends Constraint {

	/*******************************************
	 * 
	 * @param X binary item variables
	 * @param teta a relative frequency (%)
	 * @param d  Datset
	 * 
	 *******************************************/
	
	public ClosedPatternDC(BoolVar[] X, double teta, Dataset d) {
		super("ClosedPattern", new PropClosedPatternBacktrackable(X, teta, d));
	}

}
