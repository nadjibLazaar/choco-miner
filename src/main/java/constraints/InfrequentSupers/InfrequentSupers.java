
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/**
 * 
 * @author bachir 28/11/2018
 */
public class InfrequentSupers extends Constraint {

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
	public InfrequentSupers(BoolVar[] X, double s, Dataset d) {
		super("InfrequentSupers", new PropInfrequentSupers(X, s, d));
		//super("ClosedPattern", new PropClosedPatternSparse(Z, teta, d));
	}

}
