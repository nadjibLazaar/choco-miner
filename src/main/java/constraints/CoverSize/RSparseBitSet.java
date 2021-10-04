package constraints.CoverSize;

import java.awt.List;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
/**
 * 
 * @author bachir
 * @since 03/11/2017
 * RSparsBitSet Structure
 *
 */
public class RSparseBitSet{
	
	private BitSet bitset;
	private long[] words;
	private int[] index;
	private int limit;
	//private HashMap<Boolean, Long> testwords = new HashMap<Boolean, Long>();

	/**
	 * @author bachir
	 * @since 03/11/2017
	 * @param bitsetInit
	 * Initiate bitset to bitsetInit
	 */
	
	public RSparseBitSet(BitSet bitsetInit) {
		this.bitset = (BitSet) bitsetInit.clone();
		this.words = bitset.toLongArray();
		this.index = new int[words.length];
		for (int i = 0; i < words.length; i++)
			this.index[i] = i;
		this.limit = words.length - 1;
	}
	/**
	 * @author bachir
	 * @since 19/12/2017
	 * @param bitsetInit
	 * Initiate this to bitsetInit
	 */
	
	public RSparseBitSet(RSparseBitSet bitsetInit) {
		this.bitset = (BitSet) bitsetInit.getBitSet().clone();
		this.words = bitsetInit.getWords().clone();
		this.index = new int[words.length];
		this.index = bitsetInit.getIndexs().clone();
		this.limit = bitsetInit.getLimit();
	}
	/**
	 * Getter 
	 * @author bachir
	 * @since 03/11/2017
	 * @return bitset
	 */
	
	public BitSet getBitSet(){
		return this.bitset;
	}
	/**
	 * Getter 
	 * @author bachir
	 * @since 19/12/2017
	 * @return words
	 */
	
	public long[] getWords(){
		return this.words;
	}
	/**
	 * Getter 
	 * @author bachir
	 * @since 19/12/2017
	 * @return limit
	 */
	
	public int[] getIndexs(){
		return this.index;
	}
	/**
	 * Getter 
	 * @author bachir
	 * @since 19/12/2017
	 * @return limit
	 */
	
	public int getLimit(){
		return this.limit;
	}
	/**Logical AND between this.bitset and set 
	 * @author bachir
	 * @since 03/11/2017
	 * @param set 
	 */
	
	public void andSparse(BitSet set) {
		if (this.bitset == set)
			return;
		long[] m = set.toLongArray();
		int limit2 = Math.min(this.limit, m.length - 1);
		long w;
		for (int i = this.limit; i >= 0; i--) {
			int o = this.index[i];
			w = 0;
			if (o <= limit2 ) 
				w = this.words[o] & m[o];
			this.words[o] = w;
			if (w == 0) {
				this.index[i] = this.index[limit];
				this.index[limit] = o;
				this.limit--;
			}
		}
		//new RSparseBitSet(BitSet.valueOf(words));
		this.bitset = (BitSet) BitSet.valueOf(words).clone();
	}

}
