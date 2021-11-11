import org.junit.Test;
import util.DataObjectLine;
import util.TestUtil;

import java.io.IOException;
import java.util.ArrayList;

public class ClosedChecker {

    @Test
    public void closedCheckerTest() throws IOException {
        ArrayList<DataObjectLine> spmfResultLazaar = TestUtil.parser("spmf-outputs/lazaar-results");
        System.out.println(spmfResultLazaar); //Temporaire, verifie que le parser fonctionne
        /*
        TODO launch ChocoMiner to update result
        ArrayList<DataObjectLine> chocoMinerResultLazaar = TestUtil.parser("");
        assertTrue(condition);
         */
    }
}
