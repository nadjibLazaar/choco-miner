package expe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import core.enumtype.CM_Dataset;
import core.enumtype.CM_Representation;
import core.enumtype.CM_Task;
import expe.Experience.ExpeBuilder;

public class ChocoMinerApp {

	private static String task;
	private static String rep;
	private static String dataset;
	private static String query;
	private static long timeout;
	private static int minsize, maxsize, nbpatterns;
	private static double minsup, minconf;
	private static ArrayList<Integer> forbiddenI = new ArrayList<Integer>();
	private static ArrayList<Integer> mandatoryI = new ArrayList<Integer>();
	private static ArrayList<Integer> forbiddenIH = new ArrayList<Integer>();
	private static ArrayList<Integer> mandatoryIH = new ArrayList<Integer>();
	private static boolean verbose, dc, gui;

	public static void main(String args[]) throws IOException, ParseException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		// print header
		printHeader();

		boolean helpMode = line.hasOption("help") || args.length == 0;
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("chocominer", options, true);
			System.exit(0);
		}

		// defaults options

		task = CM_Task.ItemsetMining.toString();
		rep = CM_Representation.FIs.toString();
		dataset = CM_Dataset.MUSHROOM.toString().toLowerCase();
		String query = "";
		timeout = 0;
		minsup = 10;
		minconf = 90;
		minsize = -1;
		maxsize = -1;
		verbose = false;
		gui = false;
		dc = false;
		nbpatterns = Integer.MAX_VALUE;

		///////////////////////

		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt());
		}
		// Build Experience
		Experience expe = new Experience.ExpeBuilder().setTask(task).setRep(rep).setDataset(dataset).setQuery(query)
				.setTimeout(timeout).setMinsize(minsize).setMaxsize(maxsize).setForbiddenI(forbiddenI)
				.setMandatoryI(mandatoryI).setForbiddenIH(forbiddenIH).setMandatoryIH(mandatoryIH).setVerbose(verbose)
				.setGui(gui).setDC(dc).setMinsup(minsup).setMinconf(minconf).setNbpatterns(nbpatterns).build();
		// Launch Experience
		expe.process();

	}

	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

		final Option taskOption = Option.builder("k").longOpt("task")
				.desc("Predefined task: Itemset Mining / Association Rules Mining").hasArg(true)
				.argName("pattern mining task").required(false).build();

		final Option repOption = Option.builder("r").longOpt("rep")
				.desc("Predefined representation: FIs / FCIs / FMIs / RIs / RGIs / RMIs").hasArg(true)
				.argName("pattern representation").required(false).build();

		final Option datasetOption = Option.builder("d").longOpt("dataset").desc("dataset file").hasArg(true)
				.argName("dataset").required(false).build();

		final Option queryfileOption = Option.builder("q").longOpt("query")
				.desc("Customized query file: queryFile (for queryFile*.q").hasArg(true).argName("query")
				.required(false).build();

		final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
				.desc("Set the timeout limit to the specified value").required(false).build();

		final Option solOption = Option.builder("p").longOpt("nbpatterns").hasArg(true)
				.desc("nb patterns to return (all by default)").required(false).build();

		final Option minsupOption = Option.builder("s").longOpt("minsup").hasArg(true)
				.desc("minimum support (relative frequency(%)").required(false).build();

		final Option minconfOption = Option.builder("c").longOpt("minconf").hasArg(true)
				.desc("minimum confidence (relative confidence(%)").required(false).build();

		final Option minsizeOption = Option.builder("sn").longOpt("minsize").hasArg(true)
				.desc("Patterns minimum size constraint").required(false).build();

		final Option maxsizeOption = Option.builder("sx").longOpt("maxsize").hasArg(true)
				.desc("Patterns maximum size constraint").required(false).build();

		final Option forbiddenOption = Option.builder("fi").longOpt("forbiddenitem").hasArg(true)
				.desc("Forbidden item (body part in case of ARs) (can be used several times)").required(false).build();

		final Option mandatoryOption = Option.builder("mi").longOpt("mandatoryitem").hasArg(true)
				.desc("Mandatory item (body part in case of ARs) (can be used several times)").required(false).build();

		final Option forbiddenhOption = Option.builder("fih").longOpt("forbiddenitemh").hasArg(true)
				.desc("Forbidden item in AR head (can be used several times)").required(false).build();

		final Option mandatoryhOption = Option.builder("mih").longOpt("mandatoryitemh").hasArg(true)
				.desc("Mandatory item in AR head (can be used several times)").required(false).build();

		final Option cdcOption = Option.builder("dc").longOpt("dc").hasArg(false)
				.desc("Specify this option to launch DC propagator for ClosedPattern global constraint").required(false)
				.build();

		final Option guiOption = Option.builder("g").longOpt("gui").hasArg(false)
				.desc("Specify this option to launch graphical user interface").required(false).build();

		final Option verboseOption = Option.builder("v").longOpt("verbose").hasArg(false).desc("verbose mode")
				.required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(taskOption);
		options.addOption(repOption);
		options.addOption(datasetOption);
		options.addOption(queryfileOption);
		options.addOption(guiOption);
		options.addOption(limitOption);
		options.addOption(minsupOption);
		options.addOption(minconfOption);
		options.addOption(minsizeOption);
		options.addOption(forbiddenOption);
		options.addOption(helpFileOption);
		options.addOption(mandatoryOption);
		options.addOption(forbiddenhOption);
		options.addOption(maxsizeOption);
		options.addOption(mandatoryhOption);
		options.addOption(verboseOption);
		options.addOption(cdcOption);
		options.addOption(solOption);

		return options;
	}

	// Check all parameters values
	public static void checkOption(CommandLine line, String option) {

		switch (option) {

		case "task":
			task = line.getOptionValue(option);
			break;
		case "rep":
			rep = line.getOptionValue(option);
			break;
		case "dataset":
			dataset = line.getOptionValue(option);
			break;
		case "query":
			query = line.getOptionValue(option);
			break;
		case "timeout":
			timeout = Long.parseLong(line.getOptionValue(option));
			break;
		case "nbpatterns":
			nbpatterns = Integer.parseInt(line.getOptionValue(option));
			break;
		case "minsup":
			minsup = Double.parseDouble(line.getOptionValue(option));
			break;
		case "minconf":
			minconf = Double.parseDouble(line.getOptionValue(option));
			break;
		case "minsize":
			System.out.println(line.getOptionValue(option));
			minsize = Integer.parseInt(line.getOptionValue(option));
			break;
		case "maxsize":
			maxsize = Integer.parseInt(line.getOptionValue(option));
			break;
		case "forbiddenitem": {
			String[] items = line.getOptionValue(option).split(" ");
			for (String s : items)
				forbiddenI.add(Integer.parseInt(s));
			break;
		}
		case "mandatoryitem": {
			String[] items = line.getOptionValue(option).split(" ");
			for (String s : items)
				mandatoryI.add(Integer.parseInt(s));
			break;
		}
		case "forbiddenitemh": {
			String[] items = line.getOptionValue(option).split(" ");
			for (String s : items)
				forbiddenIH.add(Integer.parseInt(s));
			break;
		}
		case "mandatoryitemh": {
			String[] items = line.getOptionValue(option).split(" ");
			for (String s : items)
				mandatoryIH.add(Integer.parseInt(s));
			break;
		}
		case "dc":
			dc = true;
			break;
		case "gui":
			gui = true;
			break;
		case "verbose":
			verbose = true;
			break;

		default: {
			System.err.println("Bad arg parameter: " + option);
			System.exit(2);
		}

		}

	}

	private static void printHeader() {

		String header = "--------------------------------------------------------------------------\n";
		header += "|                                                                         |\n";
		header += "|                      CHOCO MINER PLATEFORM                              |\n";
		header += "|                                                                         |\n";
		header += "--------------------------------------------------------------------------\n";
		System.out.println(header);

	}

}
