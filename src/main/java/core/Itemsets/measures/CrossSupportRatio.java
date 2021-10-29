package core.Itemsets.measures;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import dataset.parsers.Dataset;
/**
 * 
 * @author bachir
 *12/04/2017
 */
public class CrossSupportRatio extends Constraint {
	
	/*******************************************
	 * 
	 * @author Nassim
	 * @since 10/2011
	 * @param X binary itemsets variables 
	 * @param beta  a relative frequency (%)
	 * @param d	a Datset 
	 * 
	 */
	public CrossSupportRatio(BoolVar[] X, double beta, Dataset d) {
		super("CrossSupportRatio", new PropAllConfidence(X,beta,d));
	}

}

