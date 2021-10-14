
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

import java.util.ArrayList;
import java.util.BitSet;

import org.chocosolver.*;

/**
 * Propagator of the Global constraint MinimalRareItemset
 * 
 * @author bachir 12/04/2017
 */
public class PropInfrequent extends Propagator<BoolVar> {
	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	public PropInfrequent(BoolVar[] vars, double relative_teta, Dataset d) {
		super(vars);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta= relative_teta*d.getNbTrans()/100;
	}


	/**
	 * Filtering rule number 1
	 * 
	 * @author bachir 
	 * 16/05/2017
	 * @param item 
	 * 				the item to check 
	 * @return true if the frequency constraint is maintain, false otherwise
	 *
	 */
	private boolean is_frequent(int item, BitSet Instanciation, ArrayList<Integer> NotIns) {
		BitSet intersection = (BitSet) Instanciation.clone();
		for (Integer i : NotIns)
			if (!vars[i].isInstantiatedTo(0) && i!=item)
				intersection.and(d.DataBinary_H.get(i));
		if(intersection.cardinality() >= effective_teta) return true;
		return false;
	}
	/**
	 * Filtering rule number 2
	 * 
	 * @author bachir 
	 * 16/05/2017
	 * @param item 
	 * 				the item to check 
	 * @return true if the item <b>item</b> force frequency to 0, false otherwise
	 *
	 */
	private boolean Zero_frequency(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		if (intersection.cardinality() == 0)
					return true;
		return false;
		
	}

	/**
	 * Simple propagator
	 * 
	 * @author bachir 12/04/2017
	 */
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		//System.out.println(evtmask);
		vars[0].removeValue(1,null);
		BitSet Instanciation = (BitSet) d.complete.clone();
		ArrayList<Integer> ones = new ArrayList<Integer>();
		ArrayList<Integer> NotIns = new ArrayList<Integer>();
		for (int i = 1; i < vars.length; i++){
			if (vars[i].isInstantiatedTo(1)) {
				Instanciation.and(d.DataBinary_H.get(i));
				ones.add(i);
			}
			if (!vars[i].isInstantiated()){
				NotIns.add(i);
			}
		}
		
		for (int i = 1; i < vars.length; i++){
			
			if(is_frequent(i, Instanciation, NotIns)&&!vars[i].isInstantiated()){//!vars[i].isInstantiated()&&
				vars[i].removeValue(0, null);
				d.propagationCount++;
			}
			
			
			//Weak Propagator
			/*if(!vars[i].isInstantiated()&&infrequent_subs(i, Instanciation, ones)){ 
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
			if(is_frequent(i, Instanciation, NotIns)&&infrequent_subs(i, Instanciation,ones)){//!vars[i].isInstantiated()&&
				vars[i].removeValue(0, null);
				d.propagationCount++;
			}*/
			
			//Zero Rule
			/*if(Zero_frequency(i, Instanciation)){ 
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}*/
			
		}
	}

	/**
	 * Incremental Propagator
	 * 
	 * @author bachir 13/04/2017
	 */
	/*public void propagate(int varIdx, int mask) throws ContradictionException {
		if (IntEventType.isRemove(mask)) vars[varIdx].removeValue(1, null);
		System.out.print(vars.length);
	}*/

	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED;			//NL::20-04-2017 UNDIFINED instead of TRUE
	}
}

