package constraints.Generator;

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
public class Generator extends Constraint {
	
	/*******************************************
	 * 
	 * 
	 * @param vars binary item variables
	 * @param teta  a relative frequency (%)
	 * @param d	datset 
	 * 
	 */
	public Generator(BoolVar[] X,double teta, Dataset d) {
		super("Generator", new PropGenerator(X,teta,d));
		//super("Generator", new PropGeneratorBacktrackableV2(X,teta,d));
		//super("Generator", new PropGenerator(X,teta,d));
		//super("Generator", new PropGeneratorV1Plus(X,teta,d));
		//super("Generator", new PropGeneratorMemory(X,teta,d));
		//super("Generator", new PropGeneratorInverted(X,teta,d));
		//super("Generator", new PropGeneratorSparse(X,teta,d));
		
		
	}

}

