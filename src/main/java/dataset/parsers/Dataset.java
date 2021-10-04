package dataset.parsers;

/*******************************************************************
 * This file is part of CPMiner project.
 *
 * 2018, COCONUT Team, LIRMM, Montpellier.
 *
 *******************************************************************/




import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;



/*******************************************************************
 * 
 * Parse Datasets from files
 * 
 *******************************************************************/

public class Dataset {

	//////////////////////////////////////////////////////////////////////////
	//	
	//						ATTRIBUTES OF DATASET
	//
	//////////////////////////////////////////////////////////////////////////

	//
	// Binary representation (Vertical)
	public ArrayList<BitSet> DataBinary_V = new ArrayList<BitSet>();
	// Binary representation (Horizontal)
	public ArrayList<BitSet> DataBinary_H = new ArrayList<BitSet>();
	//If horizontal base is built
	public boolean horizontal;
	// Number of items 
	int nbItems;
	// Number of transactions
	int nbTrans;
	// Dynamic number of transaction 
	int Tran_maj;
	//Complete transaction
	public BitSet complete;
	//Partitions
	public ArrayList<ArrayList<Integer>> partition;
	
	
	//Number of propagations
	public int propagationCount = 0;
	
	
	//////////////////////////////////////////////////////////////////////////
	//	
	//						CONSTRUCTORS OF DATASET
	//
	//////////////////////////////////////////////////////////////////////////
	

	/********************************************************
	 * Dataset constructor parses the dataset file and builds
	 * by default a binary vertical representation (DataBinary_V)
	 * 
	 *  @see build_H for the horizontal representation
	 * 
	 * @param datasetPath
	 * @throws IOException
	 *******************************************************/

	public Dataset(String datasetPath) throws IOException {

		this.horizontal=false;
		int DataSize = 0;
		nbItems=1;
		// Map to count the support of each item
		Map<Integer, Integer> mapItemCount = new HashMap<Integer, Integer>();
		// Key: item Value : support
		 // to
		// count
		// the
		// support
		// of
		// each
		// item

		// scan the database to load it into memory and count the support of
		// each single item at the same time
		BufferedReader reader = new BufferedReader(new FileReader(datasetPath));
		String line;
		// for each line (transactions) until the end of the file
		while (((line = reader.readLine()) != null)) {
			// if the line is a comment, is empty or is a
			// kind of metadata
			if (line.isEmpty() == true || line.charAt(0) == '#' || line.charAt(0) == '%' || line.charAt(0) == '@') {
				continue;
			}
			// split the line according to spaces
			String[] lineSplited = line.split(" ");

			// create an array of int to store the items in this transaction
			int transaction[] = new int[lineSplited.length];

			// for each item in this line (transaction)
			for (int i = 0; i < lineSplited.length; i++) {
				// transform this item from a string to an integer
				Integer item = Integer.parseInt(lineSplited[i]);
				// store the item in the memory representation of the database
				transaction[i] = item;
				// increase the support count
				Integer count = mapItemCount.get(item);
				if (count == null) {
					mapItemCount.put(item, 1);
				} else {
					mapItemCount.put(item, ++count);
				}
			}
			// Compute the number of items
			if(transaction[transaction.length-1]> nbItems) 
				nbItems=transaction[transaction.length-1];

			// add the transaction to the database
			DataBinary_V.add(new BitSet());
			for (int j = 0; j < transaction.length; j++)
				DataBinary_V.get(DataSize).set(transaction[j], true);
	
			//update the nb of transactions
			DataSize++;
		}
		
		// close the input file
		reader.close();

		
		//number of transactions
		nbTrans=DataSize;
		//number of items
		nbItems++;
		//Init Complete transaction
		complete = new BitSet();
		for (int i = 0; i < nbTrans; i++)
			complete.set(i, true);


	}
	
	
	//////////////////////////////////////////////////////////////////////////
	//	
	//						MOTHODS ON DATASET
	//
	//////////////////////////////////////////////////////////////////////////


	/******************************************************
	 * 
	 * To build the horizontal representation if needed
	 * 
	 ******************************************************/

	public void build_H(){
		//Dynamic number of transaction 
				Tran_maj = nbTrans;
		// Initialize The binary representation (Horizontal)
		for (int i = 0; i <= nbItems; i++) {
			DataBinary_H.add(new BitSet());
			for (int j = 0; j < nbTrans; j++) {
				if (DataBinary_V.get(j).get(i))
					DataBinary_H.get(i).set(j, true);
			}
		}
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	//	
	//						GETTERS / SETTERS FOR DATASET
	//
	//////////////////////////////////////////////////////////////////////////

	public int getNbTrans() {
		return nbTrans;
	}

	public int getNbItems() {
		return nbItems;
	}

	public boolean isHorizontal() {
		return horizontal;
	}



}
