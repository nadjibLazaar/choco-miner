package core.constraints.CoverSize;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import core.constraints.closedpattern.ClosedPatternDC;
import core.constraints.closedpattern.ClosedPatternWC;
import dataset.parsers.Dataset;
import util.Log;

public class CoverSize extends Constraint {
	
	/*******************************************
	 * 
	 * 
	 * @param vars binary item variables
	 * @param teta  a relative frequency (%)
	 * @param d	datset 
	 * 
	 */
	public CoverSize(BoolVar[] X, IntVar Frequency, double teta, Dataset d) {
		super("CoverSize", new PropCoverSize(X, Frequency, teta,d));
	}
	
	
}
