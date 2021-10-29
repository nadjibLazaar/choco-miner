package core.Itemsets.measures;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

import dataset.parsers.Dataset;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;

import org.chocosolver.*;

/**
 * Propagator of the checker of the constraint AllConfidence
 * 
 * @author Nassim
 * @since 28/10/2021
 */
public class PropAllConfidence extends Propagator<BoolVar> {
	BoolVar[] vars;
	static BoolVar[] vars3;
	double beta = 0.0;
	Dataset d;
	int UB;
	int LB;
	int previousWorld;
	BitSet Instanciation;
	ArrayList<Integer> zeros;
	HashMap<Integer, BitSet> History = new HashMap<>();
	HashMap<Integer, ArrayList<Integer>> ZerosHistory = new HashMap<>();

	public PropAllConfidence(BoolVar[] vars, double beta, Dataset d) {
		super(vars, PropagatorPriority.LINEAR, false);

		this.vars = vars;
		this.d = d;
		this.beta = (beta/100);
		this.UB = d.getNbTrans();
		this.LB = 0;
		this.Instanciation = (BitSet) d.complete.clone();
		History.put(0, Instanciation);
		History.put(1, Instanciation);
		previousWorld = 0;
		this.zeros = new ArrayList<Integer>();

	}

	/**
	 * Check AllConfidence
	 * 
	 * @author Nassim
	 * @since 28/10/2021
	 * @param vars the variables to check return true if all variables (vars) are
	 *             instantiated false otherwise
	 */
	public boolean AllInstantiated(BoolVar[] vars) {
		for (int i = 0; i < vars.length; i++)
			if (!vars[i].isInstantiated())
				return false;
		return true;
	}

	/**
	 * Check AllConfidence
	 * 
	 * @author Nassim
	 * @since 28/10/2021
	 * @param item the item to check
	 * @param X    BitSet representing the partial instantiation of the Antecedent
	 *             return true if the AllConfident is respected by adding item false
	 *             otherwise
	 
	private boolean AllConfidence(int item, double max, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		double allconf = 0.0;
		if (max > 0.0)
			allconf = (intersection.cardinality() / max);
		if (allconf < this.beta)
			return false;
		return true;
	}*/

	private double frequency_item(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		double supp = intersection.cardinality();
		return supp;
	}
	private double frequency_itemset( BitSet Instanciation) {
		BitSet dataset = (BitSet) d.complete.clone();
		dataset.and(Instanciation);
		double supp = dataset.cardinality();
		return supp;
	}
	/**
	 * Simple propagator
	 * 
	 * @author Nassim
	 * @since 28/10/2021
	 */
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		if (AllInstantiated(vars) && AtleastOneItem(vars)) {
			this.Instanciation = (BitSet) d.complete.clone();

			ArrayList<Double> supps = new ArrayList<Double>();
			double allconf = 0.0;
			for (int i = 0; i < vars.length; i++) 
				if(vars[i].isInstantiatedTo(1))
				Instanciation.and(d.DataBinary_H.get(i));
			for (int i = 0; i < vars.length; i++) {
				if(vars[i].isInstantiatedTo(1))
					supps.add(frequency_item(i, Instanciation));
			}
			double SuppX = frequency_itemset(Instanciation);
			allconf=SuppX / Collections.max(supps);
			if(Collections.max(supps)!=0) {
			if ( allconf < this.beta) {

				for (int i = 0; i < vars.length; i++) {
					vars[i].removeValue(1, null);
				}
				d.propagationCount++;

			}
			History.put(model.getEnvironment().getWorldIndex(), Instanciation);
			}else {
				for (int i = 0; i < vars.length; i++) {
					vars[i].removeValue(1, null);
				}
				d.propagationCount++;

			}
		}

	}

	private boolean AtleastOneItem(BoolVar[] vars2) {
		for (int i = 0; i < vars.length; i++)
			if (vars[i].isInstantiatedTo(1))
				return true;
		return false;
	}

	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED;
	}
}
