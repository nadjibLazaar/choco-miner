package core.constraints.Frequent;

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
 * FrequentPattern constraint
 * frequency constraint
 * 
 *******************************************/
public class Frequent extends Constraint {

	/*******************************************
	 * 
	 * @param X binary item variables
	 * @param teta a relative frequency (%)
	 * @param d  Datset
	 * 
	 *******************************************/
	public Frequent(BoolVar[] X, double teta, Dataset d) {
		super("FrequentPattern", new PropFrequentBacktrackable(X, teta, d));
	}

}
