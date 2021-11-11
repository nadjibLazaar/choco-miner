import org.junit.Test;
import util.DataObjectLine;
import util.TestUtil;

import java.io.IOException;
import java.util.ArrayList;

public class ClosedChecker {

    @Test
    public void closedCheckerTest() throws IOException {
        ArrayList<DataObjectLine> spmfResultLazaar = TestUtil.parser("spmf-outputs/lazaar-results");
        // TODO launch ChocoMiner to update result
        System.out.println(spmfResultLazaar); //Temporaire, verifie que le parser fonctionne
        //ArrayList<DataObjectLine> chocoMinerResultLazaar = TestUtil.parser(""); //TODO change to take instance of lazaar for chocominer

    }
}
