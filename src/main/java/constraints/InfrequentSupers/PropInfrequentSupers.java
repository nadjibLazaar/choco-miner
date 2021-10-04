package constraints.InfrequentSupers;


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
import java.util.HashMap;

import org.chocosolver.*;

/**
 * Propagator of the global constraint InfrequentSupers
 * 
 * @author bachir
 * @since 28/11/2018
 */
public class PropInfrequentSupers extends Propagator<BoolVar> {
	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	BitSet cover;
	ArrayList<Integer> zeros;

	public PropInfrequentSupers(BoolVar[] vars, double relative_teta, Dataset d) {
		super(vars, PropagatorPriority.LINEAR, false);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta = relative_teta * d.getNbTrans() / 100;
		this.cover = (BitSet) d.complete.clone();
		this.zeros = new ArrayList<Integer>();
	}
	
	/**
	 * Filtering of FrequentSubs
	 * 
	 * @author bachir
	 * @since 05/12/2018
	 * @param item
	 *            the item to check
	 * @param cover
	 *            the cover of I\ (zeros U {item})
	 * @return true if the absence of the item <b>item</b> causes a upper-itemset with infrequent 
	 * 						a superset , false otherwise
	 *
	 */
	
	
	private boolean frequent_super(int item, BitSet cover) {
		if (cover.cardinality() < effective_teta)
			return false;
		BitSet cover2 = (BitSet) cover.clone();
		cover2.and(d.DataBinary_H.get(item));
		if (cover2.cardinality() >= effective_teta) {
			return true;
		}
		for (Integer i : zeros) {
			cover2 = (BitSet) cover.clone();
			cover2.and(d.DataBinary_H.get(i));
			if (cover2.cardinality() >= effective_teta) {
				return true;
			}
		}

		return false;

	}
	/**
	 * Simple propagator
	 * 
	 * @author bachir 28/11/2018
	 */
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		cover = (BitSet) d.complete.clone();
		zeros = new ArrayList<Integer>();
		vars[0].removeValue(1, null);
		for (int i = 1; i < vars.length; i++) {
			if (vars[i].isInstantiatedTo(1)) {
				cover.and(d.DataBinary_H.get(i));

			}
			if (vars[i].isInstantiatedTo(0))
				zeros.add(i);
		}

		for (int i = 1; i < vars.length; i++) {
			BitSet cover1 = (BitSet) cover.clone();
			
			for (int j = 0; j < vars.length; j++) {
				if (!vars[j].isInstantiated() &&  j!=i)
					cover1.and(d.DataBinary_H.get(j));
			}
			
			// Rule 3: if the upper-bound has a frequent superset --> x_i = 1
			if (frequent_super(i, cover1)) {//!vars[i].isInstantiated() && 
				vars[i].removeValue(0, null);
				cover.and(d.DataBinary_H.get(i));
			}
		}
	}

	@Override
	public ESat isEntailed() {

		return ESat.UNDEFINED;
	}
}
