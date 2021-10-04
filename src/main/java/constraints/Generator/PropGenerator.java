package constraints.Generator;


import org.chocosolver.solver.Identity;
import org.chocosolver.solver.Model;
import gnu.trove.stack.array.TIntArrayStack;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorList;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.solver.variables.events.PropagatorEventType;
import org.chocosolver.util.ESat;
import org.chocosolver.util.objects.graphs.DirectedGraph;
import org.chocosolver.util.objects.setDataStructures.SetType;

import dataset.parsers.Dataset;

import java.awt.Desktop.Action;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.chocosolver.*;
import org.chocosolver.memory.IEnvironment;
import org.chocosolver.memory.IStateBitSet;
import org.chocosolver.memory.trailing.EnvironmentTrailing;
import org.chocosolver.memory.trailing.trail.IStoredBoolTrail;
import org.chocosolver.memory.trailing.trail.IStoredIntTrail;
import org.chocosolver.memory.trailing.trail.chunck.ChunckedBoolTrail;

/**
 * Propagator of the Global constraint ClosedPattern
 * 
 * @author bachir 12/04/2017
 */
public class PropGenerator extends Propagator<BoolVar> {
	protected static class FastResetArrayStack extends TIntArrayStack{
	        public void resetQuick(){
	            this._list.resetQuick();
	        }
	    }
	BoolVar[] vars;
	double relative_teta;
	double effective_teta;
	Dataset d;
	protected FastResetArrayStack toCheck = new FastResetArrayStack();
	BitSet Instanciation;
	ArrayList<Integer> ones;
	BoolVar history;
	int previousWorld;
	HashMap<Integer, HashMap<ArrayList<Integer>, BitSet>> HistorySubs = new HashMap<>();
	HashMap<Integer, BitSet> History = new HashMap<>();
	HashMap<Integer, ArrayList<Integer>> HistoryOnes = new HashMap<>();
	HashMap<ArrayList<Integer>, BitSet> subs = new HashMap<>();
	
	public PropGenerator(BoolVar[] vars, double relative_teta, Dataset d) {
		super(vars, PropagatorPriority.BINARY, false);
		//super(vars);
		this.vars = vars;
		this.d = d;
		this.relative_teta = relative_teta;
		this.effective_teta = relative_teta * d.getNbTrans() / 100;
		this.ones = new ArrayList<Integer>();
		this.Instanciation = (BitSet) d.complete.clone();
		History.put(0, (BitSet) Instanciation.clone());
		History.put(1, (BitSet) Instanciation.clone());
		subs = new HashMap<>();
		subs.put(ones, (BitSet) Instanciation.clone());
		HistorySubs.put(0, (HashMap<ArrayList<Integer>, BitSet>) subs.clone());
		HistorySubs.put(1, (HashMap<ArrayList<Integer>, BitSet>) subs.clone());
		HistoryOnes.put(0, (ArrayList<Integer>) ones.clone());
		HistoryOnes.put(1, (ArrayList<Integer>) ones.clone());
	}

	/**
	 * Filtering rule number 1 
	 * 
	 * @author bachir 
	 * @since 09/10/2017
	 * @param item
	 *            the item to check
	 * @param Instanciation
	 * 				BitSet representing the partial instantiation 
	 * @param ones
	 * 				Integer vector containing the indexes of the variables instantiated to 1             
	 * @return true if by adding the item <b>item</b> we keep the same support a one of the direct sub-items,
	 *         false otherwise
	 *
	 */

	private boolean Keep_frequency(int item, BitSet Instanciation, ArrayList<Integer> ones) {
		BitSet intersection = (BitSet) Instanciation.clone();
		BitSet intersectionSubs = (BitSet) d.complete.clone();

		BitSet keep = (BitSet) intersection.clone();
		intersection.and(d.DataBinary_H.get(item));
		if ((keep.equals(intersection)))
			return true;
		ArrayList<Integer> key = new ArrayList<Integer>();
		for (Integer m : ones) {
			key = (ArrayList<Integer>) ones.clone();
			key.remove(m);
			if(subs.containsKey(key))
				intersectionSubs = (BitSet) subs.get(key).clone();
			intersectionSubs.and(d.DataBinary_H.get(item));
			if ((intersectionSubs.equals(intersection)))
				return true;
		}

		return false;
	}

	/**
	 * Filtering rule number 2
	 * 
	 * @author bachir 
	 * @since 09/10/2017
	 * @param item
	 *            the item to check
	 * @param Instanciation
	 * 				BitSet representing the partial instantiation             
	 * @return true if the frequency constraints is lost adding the item
	 *         <b>item</b> to the current instantiation, false otherwise
	 *
	 */
	private boolean lose_frequency(int item, BitSet Instanciation) {
		BitSet intersection = (BitSet) Instanciation.clone();
		intersection.and(d.DataBinary_H.get(item));
		if (intersection.cardinality() < Math.round(effective_teta))
			return true;
		return false;
	}
	public boolean AllInstantiated(BoolVar[] vars){
		for (int i = 0; i < vars.length; i++) 
			if (!vars[i].isInstantiated())
				return false;
		return true;
	}

	/**
	 * Simple propagator
	 * 
	 * @author bachir 
	 * @since 10/10/2017
	 */
	@Override
	public void propagate(int evtmask) throws ContradictionException {
		BoolVar var = (BoolVar) model.getSolver().getDecisionPath().getLastDecision().getDecisionVariable();
		if(var != null && var.isInstantiatedTo(0)){
			History.put(model.getEnvironment().getWorldIndex(), (BitSet) Instanciation.clone());
			HistoryOnes.put(model.getEnvironment().getWorldIndex(), (ArrayList<Integer>) ones.clone());
			HistorySubs.put(model.getEnvironment().getWorldIndex(), (HashMap<ArrayList<Integer>, BitSet>) subs.clone());
			return;
		}
		BitSet instanciationsubs = (BitSet) d.complete.clone();
		int WorldIndex = model.getEnvironment().getWorldIndex();
		if(WorldIndex == 4)
			previousWorld = WorldIndex - 3;
		else 
			previousWorld = WorldIndex - 1;
		if(History.containsKey(previousWorld))
			Instanciation = (BitSet) History.get(previousWorld).clone();
		if(HistoryOnes.containsKey(previousWorld))
			ones = (ArrayList<Integer>) HistoryOnes.get(previousWorld).clone();
		if(HistorySubs.containsKey(previousWorld))
			subs = (HashMap<ArrayList<Integer>, BitSet>) HistorySubs.get(previousWorld).clone();
		if(var != null && var.isInstantiatedTo(1)){
			subs.put(ones, (BitSet) Instanciation.clone());
			Instanciation.and(d.DataBinary_H.get(var.getId()-1));
			for(Integer i : ones){
			    	ArrayList<Integer> key = (ArrayList<Integer>) ones.clone();
					key.remove(i);
					instanciationsubs = (BitSet) d.complete.clone();
					instanciationsubs = (BitSet) subs.get(key).clone();
					instanciationsubs.and(d.DataBinary_H.get(var.getId()-1));
					key.add(var.getId()-1);
					subs.put(key, (BitSet) instanciationsubs.clone());
			    }
			ones.add(var.getId()-1);
		}
	
		subs.put(ones, (BitSet) Instanciation.clone());
		History.put(model.getEnvironment().getWorldIndex(), (BitSet) Instanciation.clone());
		HistoryOnes.put(model.getEnvironment().getWorldIndex(), (ArrayList<Integer>) ones.clone());
		HistorySubs.put(model.getEnvironment().getWorldIndex(), (HashMap<ArrayList<Integer>, BitSet>) subs.clone()); 
		
		for (int i = 0; i < vars.length; i++) {
			if (!vars[i].isInstantiated() && Keep_frequency(i, Instanciation, ones)) {																	// &&
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
			if (!vars[i].isInstantiated() && lose_frequency(i, Instanciation)) {
				vars[i].removeValue(1, null);
				d.propagationCount++;
			}
		}
	}

	/**
	 * Incremental Propagator
	 * 
	 * @author bachir 13/04/2017
	 */
	public void propagate(int varIdx, int mask) throws ContradictionException {
		//System.out.println(vars[varIdx]);
	}
	 

	@Override
	public ESat isEntailed() {
		return ESat.UNDEFINED;
	}
}
