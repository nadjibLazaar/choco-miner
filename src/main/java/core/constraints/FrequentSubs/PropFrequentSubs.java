package core.constraints.FrequentSubs;

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
 * Propagator of the global constraint FrequentSubs
 * 
 * @author bachir
 * @since 28/11/2018
 */
public class PropFrequentSubs extends Propagator<BoolVar> {
	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	BitSet cover;
	ArrayList<Integer> ones;

	public PropFrequentSubs(BoolVar[] vars, double relative_teta, Dataset d) {
		super(vars, PropagatorPriority.LINEAR, false);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta = relative_teta * d.getNbTrans() / 100;
		this.cover = (BitSet) d.complete.clone();
		this.ones = new ArrayList<Integer>();
	}

	/**
	 * Filtering rule number 3 + 4
	 * 
	 * @author bachir
	 * @since 27/02/2018
	 * @param item
	 *            the item to check
	 * @param cover
	 *            BitSet representing the partial instantiation
	 * @param ones
	 *            Integer vector containing the indexes of the variables
	 *            instantiated to 1
	 * @return true if the item <b>item</b> form with one of the sub-items an
	 *         infrequent pattern, false otherwise
	 *
	 */
	private boolean infrequent_sub(int item) {
		BitSet intersection = (BitSet) cover.clone();
		BitSet intersectionSubs = (BitSet) d.complete.clone();

		BitSet keep = (BitSet) intersection.clone();
		intersection.and(d.DataBinary_H.get(item));

		if (keep.cardinality() < effective_teta)
			return true;

		int c = 0;
		int s;
		for (Integer m : ones) {
			intersectionSubs = (BitSet) d.complete.clone();
			s = 0;
			for (Integer i : ones) {
				if (c == s) {
					s++;
					continue;
				}
				intersectionSubs.and(d.DataBinary_H.get(i));
				s++;
			}
			intersectionSubs.and(d.DataBinary_H.get(item));
			if ((intersectionSubs.cardinality() < effective_teta))
				return true;
			c++;
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
		ones = new ArrayList<Integer>();
		for (int i = 1; i < vars.length; i++) {
			if (vars[i].isInstantiatedTo(1)) {
				cover.and(d.DataBinary_H.get(i));
				ones.add(i);
			}
		}
		vars[0].removeValue(1, null);
		for (int i = 1; i < vars.length; i++) {

			if (!vars[i].isInstantiated() && infrequent_sub(i)) {
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
		}
	}

	@Override
	public ESat isEntailed() {
		
		return ESat.UNDEFINED;
	}
}
