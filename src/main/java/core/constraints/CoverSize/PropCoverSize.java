package core.constraints.CoverSize;

import java.util.ArrayList;
import java.util.BitSet;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

import dataset.parsers.Dataset;

public class PropCoverSize extends Propagator<IntVar> {

	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	IntVar Frequency;
	int UB;
	int LB;

	public PropCoverSize(BoolVar[] vars, IntVar Frequency ,double relative_teta, Dataset d) {
		super(vars, PropagatorPriority.LINEAR, false);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta = relative_teta * d.getNbTrans() / 100;
		this.Frequency = Frequency;
		this.UB = d.getNbTrans();
		this.LB = 0;
	}
	private boolean lose_frequency(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		if (intersection.cardinality() < Math.round(effective_teta))
			return true;
		return false;
	}
	private boolean max_size(int item, BitSet Instanciation, int newUb, int newLb, ArrayList<Integer> NotIns){
		BitSet lowerCover = (BitSet) Instanciation.clone();
		if(newLb < newUb && newLb == this.UB){
			for (Integer i: NotIns)
				if(i != item)
					lowerCover.and(d.DataBinary_H.get(i));
			if(!d.DataBinary_H.get(item).intersects(lowerCover))
				return true;
		}
		return false;
	}
	@Override
	public void propagate(int arg0) throws ContradictionException {
		BitSet Instanciation = (BitSet) d.complete.clone();
		BitSet LowerInstanciation = (BitSet) d.complete.clone();
		ArrayList<Integer> zeros = new ArrayList<Integer>();
		ArrayList<Integer> NotIns = new ArrayList<Integer>();
		for (int i = 0; i < vars.length; i++){
			if (vars[i].isInstantiatedTo(1)){
				Instanciation.and(d.DataBinary_H.get(i));
			}
			if (!vars[i].isInstantiated()){
				LowerInstanciation.and(d.DataBinary_H.get(i));
				NotIns.add(i);
			}
			if (vars[i].isInstantiatedTo(0))
				zeros.add(i);
		}
		int newUb = Instanciation.cardinality();
		int newLb = LowerInstanciation.cardinality();
		this.UB = Math.min(this.UB, newUb);
		this.LB = Math.max(this.LB, newLb);
		if(!Frequency.isInstantiated())
			Frequency.updateBounds(this.LB, this.UB, null);
		
		for (int i = 0; i < vars.length; i++) {
			if (lose_frequency(i, Instanciation)) { //!vars[i].isInstantiated()&&
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
			if (max_size(i, Instanciation, newUb, newLb, NotIns)) {
				vars[i].removeValue(0, null);
				d.propagationCount++;
			}
		}
	}
	@Override
	public ESat isEntailed() {
		// TODO Auto-generated method stub
		return ESat.UNDEFINED;
	}

}
