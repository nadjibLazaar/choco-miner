package core.Itemsets.measures;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import dataset.parsers.Dataset;
/**
 * 
 * @author Nassim
 *29/10/2021
 */
public class AllConfidence extends Constraint {
	
	/*******************************************
	 * 
 * @author Nassim
	 * @since 10/2011
	 * @param X binary itemsets variables 
	 * @param beta  a relative frequency (%)
	 * @param d	a Datset 
	 */
	public AllConfidence(BoolVar[] X, double beta, Dataset d) {
		super("AllConfidence", new PropAllConfidence(X,beta,d));
	}

}

