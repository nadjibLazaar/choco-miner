package core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;

import core.Itemsets.measures.AllConfidence;
import core.Itemsets.measures.CrossSupportRatio;
import core.constraints.Frequent.Frequent;
import core.constraints.FrequentSubs.FrequentSubs;
import core.constraints.Generator.Generator;
import core.constraints.Infrequent.Infrequent;
import core.constraints.InfrequentSupers.InfrequentSupers;
import core.constraints.closedpattern.ClosedPatternDC;
import core.constraints.closedpattern.ClosedPatternWC;
import core.enumtype.CM_Task;
import core.tools.FileManager;
import dataset.parsers.Dataset;
import expe.Experience;
import util.Log;

public class ChocoMiner {
	private Experience experience;
	private Model model;
	private ArrayList<Constraint> constraints = new ArrayList<>();
	private Dataset dataset;
	private BoolVar[] X;
	private Query query;

	public ChocoMiner(Experience experience) throws IOException {
		this.experience = experience;
		dataset = new Dataset(FileManager.getDatasetDir() + experience.getDataset() + FileManager.getExtData());
		dataset.build_H();
		model = new Model(experience.getTask().toString());
		buildQuery();

	}

	private void queryRevision() {
		// TODO
	}

	private void buildQuery() {

		query = new Query();
		if (experience.getTask() == CM_Task.ItemsetMining)
			buildItemsetQuery();
		else
			buildARQuery();

	}

	private void buildItemsetQuery() {

		// Declare variables
		X = model.boolVarArray(dataset.getNbItems()); // items
		switch (experience.getMeasure()) {
		case AllConfidenceIs: {
			AllConfidence c1 = new AllConfidence(X, experience.getMeasureThreshold(), dataset);
			query.add(c1);
			break;
		}
		case CrossSupportIs: {
			CrossSupportRatio c1 = new CrossSupportRatio(X, experience.getMinsup(), dataset);
			query.add(c1);
			break;
		}
		
		}
		switch (experience.getRep()) {
		case FIs: {
			Frequent c1 = new Frequent(X, experience.getMinsup(), dataset);
			query.add(c1);
			break;
		}
		case FCIs: {
			Constraint c1;
			if (experience.isDC())

				c1 = new ClosedPatternDC(X, experience.getMinsup(), dataset);

			else
				c1 = new ClosedPatternWC(X, experience.getMinsup(), dataset);
			query.add(c1);
			break;
		}
		case FMIs: {
			InfrequentSupers c1 = new InfrequentSupers(X, experience.getMinsup(), dataset);
			Frequent c2 = new Frequent(X, experience.getMinsup(), dataset);
			query.add(c1, c2);
			break;
		}
		case RIs: {
			Infrequent c1 = new Infrequent(X, experience.getMinsup(), dataset);
			query.add(c1);
			break;
		}
		case RGIs: {
			Generator c1 = new Generator(X, experience.getMinsup(), dataset);
			query.add(c1);
			break;
		}
		case RMIs: {
			FrequentSubs c1 = new FrequentSubs(X, experience.getMinsup(), dataset);
			Infrequent c2 = new Infrequent(X, experience.getMinsup(), dataset);
			query.add(c1, c2);
			break;
		}
		default: {
			System.err.println("Bad representation parameter: " + experience.getRep());
			System.exit(2);
		}

		}

		if (experience.getMinsize() != -1) {
			{
				Constraint c1 = model.sum(X, ">", experience.getMinsize() - 1);
				constraints.add(c1);
				query.add(c1);

			}
		}

		if (experience.getMaxsize() != -1) {
			Constraint c1 = model.sum(X, "<", experience.getMinsize() + 1);
			constraints.add(c1);
			query.add(c1);

		}

		if (!experience.getForbiddenI().isEmpty()) {
			for (ArrayList<Integer> a : experience.getForbiddenI()) {
				for(int i : a) {
				Constraint c1 = model.arithm(X[i], "=", 0);
				query.add(c1);}
			}
			}
			
			if (!experience.getMandatoryI().isEmpty()) {
				for (ArrayList<Integer> a : experience.getMandatoryI()) {
					for(int i : a) {
					Constraint c1 = model.arithm(X[i], "=", 1);
					query.add(c1);}
				}

		}
	}

	private void buildARQuery() {
		// TODO Auto-generated method stub

	}

	private void configureSearch() {
//TODO
	}

	public void solve() throws SecurityException, IOException {

		post(query);
		// 8. Solve and print
		int nb_solutions = 2;
		DecimalFormat df2 = new DecimalFormat(".##");

		System.out.println("Solving .....");

		if (experience.getTimeout() != 0)
			model.getSolver().limitTime(experience.getTimeout() * 1000);

		model.getSolver().solve();

		float FirstSolution = model.getSolver().getTimeCount();

		printPatterns(X, 1);
		while (nb_solutions < experience.getNbpatterns() && model.getSolver().solve()) {
			printPatterns(X, nb_solutions);
			nb_solutions++;
		}
		model.getSolver().printStatistics();
		// Print Statistics
		Log my_log = new Log("Results");
		my_log.logger.info("Experience  = " + experience + "\n| Number of solutions = "
				+ model.getSolver().getSolutionCount() + "\n| Time for the First solution = "
				+ df2.format(FirstSolution) + " s " + "\n| Time = " + df2.format(model.getSolver().getTimeCount())
				+ " s " + "\n| Nodes = " + model.getSolver().getNodeCount() + "\n| Memory = "
				+ df2.format(
						((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024.0 * 1024.0)))
				+ " MB" + "\n| Propagations = " + dataset.propagationCount + "\n| Backtracks = "
				+ model.getSolver().getBackTrackCount() + "\n| Fails = " + model.getSolver().getFailCount());

	}

	private void post(Query query) {

		for (Constraint c : query.getConstraints())
			model.post(c);

	}

	private void printPatterns(BoolVar[] vars, int j) {
		// System.out.println("Solution " + j);
		String sol = "s" + j + "::\t";
		for (int i = 0; i < vars.length; i++)
			if (vars[i].isInstantiatedTo(1))
				sol += (i + " ");
		System.out.println(sol);

	}

}
