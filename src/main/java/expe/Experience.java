package expe;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.Option.Builder;

import core.ChocoMiner;
import core.enumtype.CM_Representation;
import core.enumtype.CM_Task;

public class Experience {

	private String task;
	private String rep;
	private String dataset;
	private String query;
	private long timeout;
	private int minsize;
	private int maxsize;
	private double minsup;
	private double minconf;
	private ArrayList<Integer> forbiddenI = new ArrayList<Integer>();
	private ArrayList<Integer> mandatoryI = new ArrayList<Integer>();
	private ArrayList<Integer> forbiddenIH = new ArrayList<Integer>();
	private ArrayList<Integer> mandatoryIH = new ArrayList<Integer>();
	private boolean verbose;
	private boolean gui;

	public Experience(ExpeBuilder expeBuilder) {
		this.task = expeBuilder.task;
		this.rep = expeBuilder.rep;
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
		this.gui = expeBuilder.gui;
	}

	public static class ExpeBuilder {

		private String task;
		private String rep;
		private String dataset;
		private String query;
		private long timeout;
		private int minsize;
		private int maxsize;
		private double minsup;
		private double minconf;
		private  ArrayList<Integer> forbiddenI = new ArrayList<Integer>();
		private  ArrayList<Integer> mandatoryI = new ArrayList<Integer>();
		private  ArrayList<Integer> forbiddenIH = new ArrayList<Integer>();
		private  ArrayList<Integer> mandatoryIH = new ArrayList<Integer>();
		private boolean verbose;
		private boolean gui;

		public ExpeBuilder setTask(String task) {
			this.task = task;
			return this;
		}

		public ExpeBuilder setRep(String rep) {
			this.rep = rep;
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

		public ExpeBuilder setMinconf(double minconf) {
			this.minconf = minconf;
			return this;
		}

		public ExpeBuilder setMinsize(int minsize) {
			this.minsize = minsize;
			return this;
		}

		public ExpeBuilder setMaxsize(int maxsize) {
			this.maxsize = maxsize;
			return this;
		}

		public ExpeBuilder setForbiddenI(ArrayList<Integer> forbiddenI) {
			this.forbiddenI.addAll(forbiddenI);
			return this;
		}

		public ExpeBuilder setMandatoryI(ArrayList<Integer> mandatoryI) {
			this.mandatoryI.addAll(mandatoryI);
			return this;
		}

		public ExpeBuilder setForbiddenIH(ArrayList<Integer> forbiddenIH) {
			this.forbiddenIH.addAll(forbiddenIH);
			return this;
		}

		public ExpeBuilder setMandatoryIH(ArrayList<Integer> mandatoryIH) {
			this.mandatoryIH.addAll(mandatoryIH);
			return this;
		}

		public ExpeBuilder setVerbose(boolean verbose) {
			this.verbose = verbose;
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

		ChocoMiner cminer = new ChocoMiner(this);

	}

	public CM_Task getTask() {
		return CM_Task.valueOf(task);
	}

	public CM_Representation getRep() {
		return CM_Representation.valueOf(rep);
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
	
	public int getMaxsize() {
		return maxsize;
	}

	public  ArrayList<Integer> getForbiddenI() {
		return forbiddenI;
	}

	public  ArrayList<Integer> getMandatoryI() {
		return mandatoryI;
	}

	public  ArrayList<Integer> getForbiddenIH() {
		return forbiddenIH;
	}

	public  ArrayList<Integer> getMandatoryIH() {
		return mandatoryIH;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public boolean isGui() {
		return gui;
	}

}
