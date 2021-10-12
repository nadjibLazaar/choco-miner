package core;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

import java.io.IOException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import core.constraints.closedpattern.ClosedPatternDC;
import core.enumtype.CM_Representation;
import core.enumtype.CM_Task;
import core.tools.FileManager;
import dataset.parsers.Dataset;
import expe.Experience;

public class ChocoMiner {
	private Experience experience;
	private Model model;
	private Dataset dataset;

	public ChocoMiner(Experience experience) throws IOException {
		this.experience = experience;
		dataset = new Dataset(FileManager.getDatasetDir() + experience.getDataset() + FileManager.getExtData());
		dataset.build_H();

	}

	public void buildModel() {

		model = new Model(experience.getTask().toString());

		if (experience.getTask().equals(CM_Task.ItemsetMining.toString()))
			buildItemsetModel();
		else
			buildArModel();

	}

	private void buildItemsetModel() {

		// Declare variables
		BoolVar[] X = model.boolVarArray(dataset.getNbItems()); // items

		switch (experience.getRep()) {
		case FCIs: {
			ClosedPatternDC c1 = new ClosedPatternDC(X, experience.getMinsup(), dataset);
			model.post(c1);
		}
			break;
		default: {
			System.err.println("Bad representation parameter: " + experience.getRep());
			System.exit(2);
		}

		}

	}

	private void buildArModel() {
		// TODO Auto-generated method stub

	}

	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(rows)));

	}

	public void solve() {

		buildModel();
		model.getSolver().showStatistics();
		model.getSolver().solve();

		StringBuilder st = new StringBuilder(String.format("Sudoku -- %s\n", instance, " X ", instance));
		st.append("\t");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				st.append(rows[i][j]).append("\t\t\t");
			}
			st.append("\n\t");
		}

		System.out.println(st.toString());
	}

}
