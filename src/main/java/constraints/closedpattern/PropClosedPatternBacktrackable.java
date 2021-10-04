package constraints.closedpattern;

/*******************************************************************
 * This file is part of CPMiner project.
 *
 * 2018, COCONUT Team, LIRMM, Montpellier.
 *
 *******************************************************************/


import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.util.ESat;

import dataset.parsers.Dataset;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;


/***********************************************
 * 
 * Propagator of the Global constraint 
 * ClosedPattern (DC version)
 * 
 *************************************************/

public class PropClosedPatternBacktrackable extends Propagator<BoolVar> {
	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	int UB;
	int LB;
	int previousWorld;
	BitSet Instanciation;
	ArrayList<Integer> zeros ;
	HashMap<Integer, BitSet> History = new HashMap<>();
	HashMap<Integer, ArrayList<Integer>> ZerosHistory = new HashMap<>();
	public PropClosedPatternBacktrackable(BoolVar[] vars, double relative_teta, Dataset d) {
		super(vars, PropagatorPriority.LINEAR, false);
		//super(vars);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta = relative_teta * d.getNbTrans() / 100;
		this.UB = d.getNbTrans();
		this.LB = 0;
		this.Instanciation = (BitSet) d.complete.clone();
		History.put(0, Instanciation);
		History.put(1, Instanciation);
		previousWorld = 0;
		this.zeros = new ArrayList<Integer>();
		ZerosHistory.put(0, zeros);
		ZerosHistory.put(1, zeros);
				
	}

	/**********************************************************************
	 * Filtering rule number 1 (full extension of a partial instantiation)
	 * 
	 * @param item
	 *            the item to check
	 * @param Instanciation
	 * 				BitSet representing the partial instantiation 
	 * @return true if the item <b>item</b> is an extension of the current
	 *         instantiation, false otherwise
	 *
	 *********************************************************************/
	
	private boolean extension(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		BitSet keep = (BitSet) intersection.clone();
		intersection.and(d.DataBinary_H.get(item));
		if (keep.equals(intersection))
			return true;
		return false;
	}

	/**********************************************************************
	 * Filtering rule number 2
	 * 
	 * @param item
	 *            the item to check
	 @param Instanciation
	 * 				BitSet representing the partial instantiation           
	 * @return true if the frequency constraints is lost adding the item
	 *         <b>item</b> to the current instantiation, false otherwise
	 *
	 *********************************************************************/
	private boolean lose_frequency(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		if (intersection.cardinality() < Math.round(effective_teta))
			return true;
		return false;
	}

	/**********************************************************************
	 * Filtering rule number 3
	 * 
	 * @param item
	 *            the item to check
	 * @param Instanciation
	 * 				BitSet representing the partial instantiation 
	 * @param Zeros
	 * 				Integer vector containing the indexes of the variables instantiated to 0           
	 * @return true if the item <b>item</b> is an extension of an absent pattern
	 *         in the current instantiation, false otherwise
	 *
	 *********************************************************************/
	private boolean extension_OfLack(int item, BitSet Instanciation, ArrayList<Integer> zeros) {
		BitSet intersection = new BitSet();
		BitSet intersection2 = (BitSet) Instanciation.clone();
		intersection2.and(d.DataBinary_H.get(item));

		for (Integer i : zeros) {
			intersection = new BitSet();
			intersection = (BitSet) d.DataBinary_H.get(i).clone();
			intersection.and(intersection2);
			if (intersection.equals(intersection2))
					return true;
			}
			
		return false;

	}
	

	

	/**********************************************************************
	 * 
	 * Simple propagator
	 * 
	 *********************************************************************/
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		BoolVar var = (BoolVar) model.getSolver().getDecisionPath().getLastDecision().getDecisionVariable();
		int WorldIndex = model.getEnvironment().getWorldIndex();
		if(WorldIndex==4)
			previousWorld = WorldIndex - 3;
		else 
			previousWorld = WorldIndex - 1;
		if(History.containsKey(previousWorld))
			Instanciation = (BitSet) History.get(previousWorld).clone();
		if(ZerosHistory.containsKey(previousWorld))
			zeros = (ArrayList<Integer>) ZerosHistory.get(previousWorld).clone(); 
		if(var != null && var.isInstantiatedTo(0)){
			zeros.add(var.getId()-1);  
		}
		if(var != null && var.isInstantiatedTo(1)){
			Instanciation.and(d.DataBinary_H.get(var.getId()-1));
		}
		for (int i = 0; i < vars.length; i++) {
			if (!vars[i].isInstantiated()&&extension(i, Instanciation)) {  // !vars[i].isInstantiated()&&
				vars[i].removeValue(0, null);
				d.propagationCount++;
			}
			if (!vars[i].isInstantiated()&&lose_frequency(i, Instanciation)) {
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
			if (!vars[i].isInstantiated()&&extension_OfLack(i, Instanciation, zeros)) {
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
		}

		History.put(model.getEnvironment().getWorldIndex(), Instanciation);
		ZerosHistory.put(model.getEnvironment().getWorldIndex(), zeros);
	}

	/**********************************************************************
	 * 
	 * Incremental Propagator
	 * 
	 *********************************************************************/
	
	  public void propagate(int varIdx, int mask) throws ContradictionException{ 
		  if (IntEventType.isRemove(mask)) 
			  vars[varIdx].removeValue(1, null);
		  System.out.print("Salam");
	}
	 

	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED; // NL::20-04-2017 UNDIFINED instead of TRUE
	}
}
