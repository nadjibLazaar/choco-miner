package core.constraints.Infrequent;

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
public class Infrequent extends Constraint {
	
	/*******************************************
	 * 
	 * 
	 * @param vars binary item variables
	 * @param teta  a relative frequency (%)
	 * @param d	datset 
	 * 
	 */
	public Infrequent(BoolVar[] X,double teta, Dataset d) {
		super("Infrequent", new PropInfrequent(X,teta,d));
	}

}

