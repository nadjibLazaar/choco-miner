import core.ChocoMiner;
import core.enumtype.CM_Dataset;
import core.enumtype.CM_Task;
import expe.Experience;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;
import util.DataObjectLine;
import util.TestUtil;

import java.io.IOException;
import java.util.ArrayList;

public class FrequentChecker {
    @Test
    public void frequentCheckerTest() {
        try {
            ArrayList<DataObjectLine> resultsSPMF = TestUtil.parser("spmf-outputs/lazaar-results");
            Experience experienceFrequent = new Experience.ExpeBuilder().
                    setDataset(""). // TODO Name of dataset
                    setTask(CM_Task.ItemsetMining.toString()). // TODO Task (either ItemsetMining or AssociationRuleMining
                    build(); // TODO All needed arguments need to be filled
            ChocoMiner miner = new ChocoMiner(experienceFrequent);
            miner.solve();
            ArrayList<DataObjectLine> resultsChocoMiner = TestUtil.parser(""); // TODO Path to choco miner results
            Assert.assertTrue(resultsChocoMiner.containsAll(resultsSPMF));
        } catch (IOException e) {
            throw new AssertionError("Error while trying to read a file");
        }
    }
}
