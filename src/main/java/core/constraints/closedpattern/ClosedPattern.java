package core.constraints.closedpattern;

/*******************************************************************
 * This file is part of CPMiner project.
 *
 * 2018, COCONUT Team, LIRMM, Montpellier.
 *
 *******************************************************************/



import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.Random;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.BoolVar;

import core.tools.FileManager;
import dataset.parsers.Dataset;
import util.Log;


/************************
 * 
 * ClosedPattern Tester
 * 
 ************************/


public class ClosedPattern {

	private static int timeout=3600;
	private static DecimalFormat df2 = new DecimalFormat(".##");

	public static void main(String[] args) throws ContradictionException, IOException {
		
		// cleaning repertory
		File file = new File("MiningFCI");
    	file.delete();
    	
		
		// 1.Create the instance
		String instance="";
		double min_supp=90;		//relative minsupp(%)
		boolean printPatterns=true;
		boolean weak_consistency=false;

		String help="Syntaxe : java [-options] -jar closedpattern.jar [args...]\n"+
				"(to execute closedpattern JAR)\n"+
				"where options are:\n"+
				"-h or --help:\t\t for help\n"+
				"-d or --dataset:\t to specify the dataset\n"+
				"-m or --minsup:\t\t to specify the minimum support (relative frequency(%)\n"+
				"-wc:\t\t\t to maintain a weak consistency (DC is maintained by default)\n"+
				"-p or -- print:\t\t to print the patterns\n"+
				"-t or -timeout:\t\t to specify a timeout resolution in seconds (one hour is allocated by default)\n";

		int i=0;
		while(i<args.length)
		{
			switch(args[i]){
			case "-h":
				System.out.println(help);
				System.exit(0);
				break;
			case "--help":
				System.out.println(help);
				System.exit(0);
				break;
			case "-d":
				instance =  args[i+1];
				i=i+2;	
				break;
			case "--dataset":
				instance =  args[i+1];
				i=i+2;	
				break;
			case "-m":
				min_supp =  Double.parseDouble(args[i+1]);
				i=i+2;	
				break;
			case "--minsup":
				min_supp =  Double.parseDouble(args[i+1]);
				i=i+2;	
				break;
			case "-wc":
				weak_consistency = true;
				i++;	
				break;
			case "-p":
				printPatterns = true;
				i++;	
				break;
			case "--print":
				printPatterns = true;
				i++;	
				break;
			case "-t":
				timeout =  Integer.parseInt(args[i+1]);
				i=i+2;	
				break;
			case "--timeout":
				timeout =  Integer.parseInt(args[i+1]);
				i=i+2;	
				break;	
			default:
				throw new IllegalArgumentException("Not a valid argument: "+args[i]);
			}

		}

		if(args.length==0){
			instance = "./datasets/" + "lazaar.data";
			min_supp = 90; 
			printPatterns = true;
			weak_consistency = true;
		}

		Dataset dataset = new Dataset(instance);
		dataset.build_H();

		// 2. Declare a Model
		Model model = new Model("MiningFCI");

		// 3. Declare variables
		BoolVar[] X = model.boolVarArray(dataset.getNbItems()); // items

		// 4. Post Constraints


		//  ClosedPattern
		if(weak_consistency){
			ClosedPatternWC c = new ClosedPatternWC(X, min_supp, dataset);
			model.post(c);
		}
		else{
			ClosedPatternDC c1 = new ClosedPatternDC(X, min_supp, dataset);
			model.post(c1);
		}

		// 6. Create solver
		Solver solver = model.getSolver();


		// 8. Solve and print
		int j = 2;
		System.out.println("Solving .....");
		solver.limitTime(timeout*1000);
		solver.solve();
		float FirstSolution = solver.getTimeCount();
		if (printPatterns)
			printPatterns(X, 1);
		while (solver.solve()) {
			if (printPatterns)
				printPatterns(X, j);
			j++;
		}
		solver.printStatistics();
		// Print Statistics
		Log my_log = new Log("FCI_Results");
		my_log.logger.info("Instance  = " + instance + "\n| Min_supp = " + min_supp
				+ "% " + "\n| Number of solutions = " + solver.getSolutionCount()
				+ "\n| Time for the First solution = " + df2.format(FirstSolution)+ " s " + "\n| Time = " + df2.format(solver.getTimeCount()) 
				+ " s " + "\n| Nodes = " + solver.getNodeCount() + "\n| Memory = "
				+ df2.format(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024.0 * 1024.0))) + " MB"
				+ "\n| Propagations = " + dataset.propagationCount + "\n| Backtracks = " + solver.getBackTrackCount()
				+ "\n| Fails = " + solver.getFailCount());
		Toolkit.getDefaultToolkit().beep();

	}

	private static void printRules(BoolVar[] vars, BoolVar[] vars2, int j) {
		System.out.println("Solution " + j);
		for (int i = 0; i < vars.length; i++)
			if (vars[i].isInstantiatedTo(1))
				System.out.print(i + " ");
		System.out.print("==> ");
		for (int i = 0; i < vars.length; i++)
			if (vars2[i].isInstantiatedTo(1))
				System.out.print(i + " ");
		System.out.println();

	}

	private static void printPatterns(BoolVar[] vars, int j) {
	//	System.out.println("Solution " + j);
	String sol= "";
		for (int i = 0; i < vars.length; i++)
			if (vars[i].isInstantiatedTo(1))
				sol+=(i + " ");
		FileManager.printFile(sol, "FCs");

	}
	private static  float frequency(BoolVar[] vars, Dataset dataset) {
		BitSet Instanciation = (BitSet) dataset.complete.clone();
		for (int i = 0; i < vars.length; i++){
			if (vars[i].isInstantiatedTo(1)){
				Instanciation.and(dataset.DataBinary_H.get(i));
			}
		}
		return (float) Instanciation.cardinality();
	}
	public static int random_between(int a, int b) {
		Random rn = new Random();
		int n = b - a + 1;
		int i = rn.nextInt(999999999) % n;
		return a + i;
	}

}
