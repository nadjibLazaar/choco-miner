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
public class AllConfidence extends Constraint {
	
	/*******************************************
	 * 
	 * @author bachir
	 * @since 10/2017
	 * @param X binary item variables representing the Antecedent 
	 * @param Y binary item variables representing the Consequent 
	 * @param beta  a relative frequency (%)
	 * @param d	a Datset 
	 * 
	 */
	public AllConfidence(BoolVar[] X, double beta, Dataset d) {
		super("AllConfidence", new PropAllConfidence(X,beta,d));
	}

}

