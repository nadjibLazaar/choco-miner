package expe;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ChocoMinerApp {

	private static String rep;
	private static String dataset;
	private static String query;
	private static long timeout;
	private static boolean normalizedCSP;
	private static boolean shuffle;
	private static int minsize, maxsize, forbiddenI, mandatoryI;
	private static int maxqueries;
	private static String instance;
	private static String vls;
	private static String vrs;
	private static boolean verbose;
	private static boolean log_queries;
	private static boolean gui;

	public static void main(String args[]) throws IOException, ParseException {

		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		// print header
		printHeader();

		boolean helpMode = line.hasOption("help") || args.length == 0;
		if (helpMode) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("puacq", options, true);
			System.exit(0);
		}

		// defaults options

		///////////////////////

		// Check arguments and options
		for (Option opt : line.getOptions()) {
			checkOption(line, opt.getLongOpt());
		}
		// Build Experience
		IExperience expe = new ExpeBuilder().setExpe(rep).setFile(dataset).setAlgo(mode).setMaxqueries(maxqueries)
				.setExamplesFile(query).setPartition(partition).setNbThreads(nb_threads).setInstance(instance)
				.setNormalizedCSP(normalizedCSP).setShuffle(shuffle).setTimeout(timeout).setHeuristic(heuristic)
				.setVarSelector(vrs).setValSelector(vls).setVerbose(verbose).setPartition(partition)
				.setDirectory(new File("src/fr/lirmm/coconut/quacq/bench/")).setQueries(log_queries).setGui(gui)
				.build();
		// Launch Experience
		expe.process();

	}

	// Add options here
	private static Options configParameters() {

		final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

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

		final Option minsizeOption = Option.builder("smin").longOpt("minsize").hasArg(false)
				.desc("Patterns minimum size constraint").required(false).build();

		final Option maxsizeOption = Option.builder("smax").longOpt("maxsize").hasArg(false)
				.desc("Patterns maximum size constraint").required(false).build();

		final Option forbiddenOption = Option.builder("fi").longOpt("forbiddenitem").hasArg(false)
				.desc("Forbidden item (body part in case of ARs)").required(false).build();

		final Option mandatoryOption = Option.builder("mi").longOpt("mandatoryitem").hasArg(false)
				.desc("Mandatory item (body part in case of ARs)").required(false).build();

		final Option forbiddenhOption = Option.builder("fih").longOpt("forbiddenitemh").hasArg(false)
				.desc("Forbidden item in AR head").required(false).build();

		final Option mandatoryhOption = Option.builder("mih").longOpt("mandatoryitemh").hasArg(false)
				.desc("Mandatory item in AR head").required(false).build();

	
		final Option guiOption = Option.builder("g").longOpt("gui").hasArg(false)
				.desc("Specify this option to launch graphical user interface").required(false).build();

		final Option verboseOption = Option.builder("v").longOpt("verbose").hasArg(false).desc("verbose mode")
				.required(false).build();

		// Create the options list
		final Options options = new Options();
		options.addOption(repOption);
		options.addOption(datasetOption);
		options.addOption(queryfileOption);
		options.addOption(guiOption);
		options.addOption(limitOption);
		options.addOption(minsizeOption);
		options.addOption(forbiddenOption);
		options.addOption(helpFileOption);
		options.addOption(mandatoryOption);
		options.addOption(forbiddenhOption);
		options.addOption(maxsizeOption);
		options.addOption(mandatoryhOption);
		options.addOption(verboseOption);

		return options;
	}

	// Check all parameters values
	public static void checkOption(CommandLine line, String option) {

		switch (option) {

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
		case "minsize":
			minsize = Integer.parseInt(line.getOptionValue(option));
			break;
		case "maxsize":
			maxsize = Integer.parseInt(line.getOptionValue(option));
			break;
		case "forbiddenitem":
			forbiddenI = Integer.parseInt(line.getOptionValue(option));
			break;
		case "mandatoryitem":
			mandatoryI = Integer.parseInt(line.getOptionValue(option));
			break;
	
		
		case "heuristic":
			heuristic = getHeuristic(line.getOptionValue(option));
			break;
		case "notnormalized":
			normalizedCSP = false;
			break;
		case "shuffle":
			shuffle = true;
			break;
		case "partition":

			partition = getPartition(line.getOptionValue(option));
			break;
		case "algo":
			mode = getMode(line.getOptionValue(option));
			break;
		case "threads":
			nb_threads = Integer.parseInt(line.getOptionValue(option));
			break;
		case "instance":
			instance = line.getOptionValue(option);
			break;
		case "Vrs":
			vrs = line.getOptionValue(option);
			break;
		case "Vls":
			vls = line.getOptionValue(option);
			break;
		case "verbose":
			verbose = true;
			break;
		case "queries":
			log_queries = true;
			break;

		case "gui":
			gui = true;
			break;
		default: {
			System.err.println("Bad arg parameter: " + option);
			System.exit(2);
		}

		}

	}

	public static ACQ_Partition getPartition(String name) {

		switch (name) {
		case "rand":
			return ACQ_Partition.RANDOM;
		case "scope":
			return ACQ_Partition.SCOPEBASED;
		case "neigh":
			return ACQ_Partition.NEIGHBORHOOD;
		case "neg":
			return ACQ_Partition.NEGATIONBASED;
		case "rel":
			return ACQ_Partition.RELATIONBASED;
		case "relneg":
			return ACQ_Partition.RELATION_NEGATIONBASED;
		case "rule":
			return ACQ_Partition.RULESBASED;
		default: {

			System.err.println("Bad partition parameter: " + name);
			System.exit(2);
		}

		}
		return null;
	}

	public static ACQ_Algorithm getMode(String name) {

		return ACQ_Algorithm.valueOf(name.toUpperCase());

	}

	public static ACQ_Heuristic getHeuristic(String name) {

		switch (name) {
		case "sol":
			return ACQ_Heuristic.SOL;
		case "max":
			return ACQ_Heuristic.MAX;
		case "min":
			return ACQ_Heuristic.MIN;
		default: {
			System.err.println("Bad heuristic parameter: " + name);
			System.exit(2);
		}

		}
		return null;
	}

	private static void printHeader() {

		String header = "--------------------------------------------------------------------------\n";
		header += "|                                                                         |\n";
		header += "|                 CHOCO MINER PLATEFORM                                   |\n";
		header += "|                                                                         |\n";
		header += "--------------------------------------------------------------------------\n";
		System.out.println(header);

	}

}
