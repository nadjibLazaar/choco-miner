package expe;

import java.util.ArrayList;

import org.apache.commons.cli.Option.Builder;

public class Experience {

	public Experience(ExpeBuilder expeBuilder) {
		// TODO Auto-generated constructor stub
	}

	public static class ExpeBuilder {

		private String rep;
		private String dataset;
		private String query;
		private long timeout;
		private int minsize;
		private int maxsize;
		private static ArrayList<Integer> forbiddenI = new ArrayList<Integer>();
		private static ArrayList<Integer> mandatoryI = new ArrayList<Integer>();
		private static ArrayList<Integer> forbiddenIH = new ArrayList<Integer>();
		private static ArrayList<Integer> mandatoryIH = new ArrayList<Integer>();
		private boolean verbose;
		private boolean gui;

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

	public void process() {
		// TODO Auto-generated method stub

	}

}
