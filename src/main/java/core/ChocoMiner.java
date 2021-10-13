package core;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.awt.Toolkit;
import java.io.IOException;
import java.text.DecimalFormat;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import core.constraints.closedpattern.ClosedPatternDC;
import core.constraints.frequentpattern.FrequentPattern;
import core.enumtype.CM_Representation;
import core.enumtype.CM_Task;
import core.tools.FileManager;
import dataset.parsers.Dataset;
import expe.Experience;
import util.Log;

public class ChocoMiner {
	private Experience experience;
	private Model model;
	private Dataset dataset;
	private BoolVar[] X;

	public ChocoMiner(Experience experience) throws IOException {
		this.experience = experience;
		dataset = new Dataset(FileManager.getDatasetDir() + experience.getDataset() + FileManager.getExtData());
		dataset.build_H();
		buildModel();

	}

	private void buildModel() {

		model = new Model(experience.getTask().toString());

		if (experience.getTask() == CM_Task.ItemsetMining)
			buildItemsetModel();
		else
			buildArModel();

	}

	private void buildItemsetModel() {

		// Declare variables
		X = model.boolVarArray(dataset.getNbItems()); // items

		switch (experience.getRep()) {
		case FIs: {
			FrequentPattern c1 = new FrequentPattern(X, experience.getMinsup(), dataset);
			model.post(c1);
			break;
		}
		case FCIs: {
			ClosedPatternDC c1 = new ClosedPatternDC(X, experience.getMinsup(), dataset);
			model.post(c1);
			break;
		}
		default: {
			System.err.println("Bad representation parameter: " + experience.getRep());
			System.exit(2);
		}

		}

		if (experience.getMinsize() != -1)
			model.sum(X, ">", experience.getMinsize() - 1).post();

		if (experience.getMaxsize() != -1)
			model.sum(X, "<", experience.getMinsize() + 1).post();
	}

	private void buildArModel() {
		// TODO Auto-generated method stub

	}

	private void configureSearch() {
//TODO
	}

	public void solve() throws SecurityException, IOException {

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

	private void printPatterns(BoolVar[] vars, int j) {
		// System.out.println("Solution " + j);
		String sol = "";
		for (int i = 0; i < vars.length; i++)
			if (vars[i].isInstantiatedTo(1))
				sol += (i + " ");
		System.out.println(sol);

	}

}
