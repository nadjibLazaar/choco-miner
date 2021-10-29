package expe;

import java.io.IOException;
import java.util.ArrayList;


import core.ChocoMiner;
import core.enumtype.CM_Measures;

import core.enumtype.CM_Representation;
import core.enumtype.CM_Task;

public class Experience {


	private String task, rep, dataset, query, measure;
	private long timeout;
	private int minsize, maxsize, nbpatterns;
	private double minsup, minconf,threshold;
	private ArrayList<ArrayList<Integer>> forbiddenI = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> mandatoryI = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>>  forbiddenIH = new ArrayList<ArrayList<Integer>> ();
	private ArrayList<ArrayList<Integer>>  mandatoryIH = new ArrayList<ArrayList<Integer>> ();
	private boolean verbose, dc, gui;

	public Experience(ExpeBuilder expeBuilder) {
		this.task = expeBuilder.task;
		this.rep = expeBuilder.rep;
		this.measure = expeBuilder.measure;
		this.threshold=expeBuilder.threshold;
		this.dataset = expeBuilder.dataset;
		this.query = expeBuilder.query;
		this.timeout = expeBuilder.timeout;
		this.maxsize = expeBuilder.maxsize;
		this.minsize = expeBuilder.minsize;
		this.forbiddenI.addAll(expeBuilder.forbiddenI);
		this.mandatoryI.addAll(expeBuilder.mandatoryI);
		this.forbiddenIH.addAll(expeBuilder.forbiddenIH);
		this.mandatoryIH.addAll(expeBuilder.mandatoryIH);
		this.verbose = expeBuilder.verbose;
		this.dc = expeBuilder.dc;
		this.gui = expeBuilder.gui;
		this.nbpatterns = expeBuilder.nbpatterns;
		this.minsup = expeBuilder.minsup;
		this.minconf = expeBuilder.minconf;

	}

	public static class ExpeBuilder {


		private String task, rep, measure, dataset, query;
		private long timeout;
		private int minsize, maxsize, nbpatterns;
		private double minsup, minconf,threshold;
		private ArrayList<ArrayList<Integer>> forbiddenI = new ArrayList<ArrayList<Integer>>();
		private ArrayList<ArrayList<Integer>> mandatoryI = new ArrayList<ArrayList<Integer>>();
		private ArrayList<ArrayList<Integer>>   forbiddenIH = new ArrayList<ArrayList<Integer>> ();
		private ArrayList<ArrayList<Integer>>  mandatoryIH = new ArrayList<ArrayList<Integer>>();
		private boolean verbose, dc, gui;

		public ExpeBuilder setTask(String task) {
			this.task = task;
			return this;
		}

		public ExpeBuilder setRep(String rep) {
			this.rep = rep;
			return this;
		}


		public ExpeBuilder setMeasure(String measure) {
			this.measure = measure;
			return this;
		}

		public ExpeBuilder setDataset(String dataset) {
			this.dataset = dataset;
			return this;
		}

		public ExpeBuilder setQuery(String query) {
			this.query = query;
			return this;
		}

		public ExpeBuilder setTimeout(long timeout) {
			this.timeout = timeout;
			return this;
		}

		public ExpeBuilder setMinsup(double minsup) {
			this.minsup = minsup;
			return this;
		}
		public ExpeBuilder setMeasureThresholdValue(double threshold) {
			this.threshold = threshold;
			return this;
		}
		public ExpeBuilder setMinconf(double minconf) {
			this.minconf = minconf;
			return this;
		}

		public ExpeBuilder setMinsize(int minsize) {
			this.minsize = minsize;
			return this;
		}

		public ExpeBuilder setNbpatterns(int nbpatterns) {
			this.nbpatterns = nbpatterns;
			return this;
		}

		public ExpeBuilder setMaxsize(int maxsize) {
			this.maxsize = maxsize;
			return this;
		}

		public ExpeBuilder setForbiddenI(ArrayList<ArrayList<Integer>> forbiddenI) {
			this.forbiddenI.addAll(forbiddenI);
			return this;
		}

		public ExpeBuilder setMandatoryI(ArrayList<ArrayList<Integer>> mandatoryI) {
			this.mandatoryI.addAll(mandatoryI);
			return this;
		}

		public ExpeBuilder setForbiddenIH(ArrayList<ArrayList<Integer>>  forbiddenIH) {
			this.forbiddenIH.addAll(forbiddenIH);
			return this;
		}

		public ExpeBuilder setMandatoryIH(ArrayList<ArrayList<Integer>>  mandatoryIH) {
			this.mandatoryIH.addAll(mandatoryIH);
			return this;
		}

		public ExpeBuilder setVerbose(boolean verbose) {
			this.verbose = verbose;
			return this;
		}

		public ExpeBuilder setDC(boolean dc) {
			this.dc = dc;
			return this;
		}

		public ExpeBuilder setGui(boolean gui) {
			this.gui = gui;
			return this;
		}

		public Experience build() {
			return new Experience(this);
		}

	}

	public void process() throws IOException {

		new ChocoMiner(this).solve();

		/*/ -------------- Interactive part
		Options options = configParameters();
		CommandLineParser parser = new DefaultParser();
		Console console = System.console();
		while (console != null) {
			System.out.print("Enter your username: ");
			String[] revision = console.readLine().split(" ");

			CommandLine line = parser.parse(options, args);

			// print header
			printHeader();

			boolean helpMode = line.hasOption("help") || args.length == 0;
			if (helpMode) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("chocominer", options, true);
				System.exit(0);
			}

		}*/

	}

	public CM_Task getTask() {
		return CM_Task.valueOf(task);
	}

	public CM_Representation getRep() {
		return CM_Representation.valueOf(rep);
	}

	public CM_Measures getMeasure() {
		return CM_Measures.valueOf(measure);
	}

	public String getDataset() {
		return dataset;
	}

	public String getQuery() {
		return query;
	}

	public long getTimeout() {
		return timeout;
	}

	public int getMinsize() {
		return minsize;
	}

	public double getMinsup() {
		return minsup;
	}

	public double getMinconf() {
		return minconf;
	}
	public double getThreshold() {
		return threshold;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public ArrayList<ArrayList<Integer>> getForbiddenI() {
		return forbiddenI;
	}

	public ArrayList<ArrayList<Integer>> getMandatoryI() {
		return mandatoryI;
	}

	public ArrayList<ArrayList<Integer>> getForbiddenIH() {
		return forbiddenIH;
	}

	public ArrayList<ArrayList<Integer>>  getMandatoryIH() {
		return mandatoryIH;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public boolean isGui() {
		return gui;
	}

	public boolean isDC() {
		return dc;
	}

	public int getNbpatterns() {
		return nbpatterns;
	}

	public double getMeasureThreshold() {
		return threshold;
	}

}
