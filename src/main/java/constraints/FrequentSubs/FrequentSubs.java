package constraints.FrequentSubs;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import dataset.parsers.Dataset;

/**
 * 
 * @author bachir 28/11/2018
 */
public class FrequentSubs extends Constraint {

	/*******************************************
	 * 
	 * @author bachir
	 * @since 04/2017
	 * @param X
	 *            binary item variables
	 * @param s
	 *            a relative frequency (%)
	 * @param d
	 *            Datset
	 * 
	 */
	public FrequentSubs(BoolVar[] X, double s, Dataset d) {
		super("FrequentSubs", new PropFrequentSubs(X, s, d));
		//super("ClosedPattern", new PropClosedPatternSparse(Z, teta, d));
	}

}
