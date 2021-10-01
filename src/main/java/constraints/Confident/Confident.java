
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
/**
 * 
 * @author bachir
 *12/04/2017
 */
public class Confident extends Constraint {
	
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
	public Confident(BoolVar[] X, BoolVar[] Y, double beta, Dataset d) {
		super("Confident", new PropConfident(X,Y,beta,d));
	}

}

