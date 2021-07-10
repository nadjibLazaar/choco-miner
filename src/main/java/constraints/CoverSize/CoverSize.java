import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

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
