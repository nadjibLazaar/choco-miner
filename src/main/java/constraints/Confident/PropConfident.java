package constraints.Confident;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

import dataset.parsers.Dataset;

import java.util.BitSet;

import org.chocosolver.*;

/**
 * Propagator of the checker of the constraint Confident
 * 
 * @author bachir 
 * @since 11/10/2017
 */
public class PropConfident extends Propagator<BoolVar> {
	BoolVar[] vars;
	BoolVar[] vars2;
	static BoolVar[] vars3;
	double beta;
	Dataset d;
	
	public PropConfident(BoolVar[] vars, BoolVar[] vars2, double beta, Dataset d) {
		super(vars2, PropagatorPriority.LINEAR, false);
		//System.arraycopy(vars, 0, vars3, 0, vars.length);
		//System.arraycopy(vars2, 0, vars3, vars3.length+1, vars2.length);
		
		//super(vars);
		this.vars = vars;
		this.vars2 = vars2;
		this.d = d;
		this.beta = beta/100;
	}

	/**
	 * Check if all variables (vars) are instantiated
	 * @author bachir
	 * @since 11/10/2017
	 * @param vars the variables to check
	 * return true if all variables (vars) are instantiated false otherwise
	 */
	public boolean AllInstantiated(BoolVar[] vars){
		for (int i = 0; i < vars.length; i++) 
			if (!vars[i].isInstantiated())
				return false;
		return true;
	}
	/**
	 * Check Confident
	 * @author bachir
	 * @since 11/10/2017
	 * @param item the item to check
	 * @param X BitSet representing the partial instantiation of the Antecedent 
	 * @param XY BitSet representing the partial instantiation of Antecedent union Consequent
	 * return true if the Confident is respected by adding item false otherwise
	 */
	public boolean Confidant(int item, BitSet X, BitSet XY){
		BitSet intersectionXY = (BitSet) XY.clone();
		
		intersectionXY.and(d.DataBinary_H.get(item));
		double supportX = (double) X.cardinality();
		double supportXY = (double) intersectionXY.cardinality();
		double conf = supportXY/supportX;
		if(conf >= beta) return true;
		return false;
	}
	
	/**
	 * Simple propagator
	 * 
	 * @author bachir 
	 * @since 11/10/2017
	 */
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		BitSet X = (BitSet) d.complete.clone();
		BitSet XY = (BitSet) d.complete.clone();
		
		
		if(AllInstantiated(vars)){
			for (int i = 0; i < vars.length; i++){
				if (vars[i].isInstantiatedTo(1)||vars2[i].isInstantiatedTo(1))
					XY.and(d.DataBinary_H.get(i));
				if (vars[i].isInstantiatedTo(1))
					X.and(d.DataBinary_H.get(i));
			}
			for(int i = 0; i < vars2.length; i++)
				if(!Confidant(i, X, XY)) {
					vars2[i].removeValue(1, null);
					d.propagationCount++;
				}
		}

	}


	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED;
	}
}
