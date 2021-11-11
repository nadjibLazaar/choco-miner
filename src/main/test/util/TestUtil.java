package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TestUtil {
    public TestUtil() {}

    //------------------------------ Le parser ---------------------------------------
    public static ArrayList<DataObjectLine> parser(String datasetPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(datasetPath));
        String line;
        ArrayList<DataObjectLine> result = new ArrayList<>();
        while (((line = reader.readLine()) != null)) {

            if (line.isEmpty()) {
                continue;
            }
            String[] lineSplited = line.split(" ");

            DataObjectLine currentDataObjectLine = new DataObjectLine();

            for (int i = 0; i < lineSplited.length; i++) {
                String s = lineSplited[i];
                if (shouldIgnoreLine(s.charAt(0))) continue;
                if (i == lineSplited.length - 1) currentDataObjectLine.setOccurence(Integer.parseInt(s));
                else {
                    int item = Integer.parseInt(s);
                    currentDataObjectLine.addData(item);
                }
            }
            result.add(currentDataObjectLine);
        }
        reader.close();
        return result;
    }

    private static boolean shouldIgnoreLine(char charIn) {
        char[] toIgnore = "@#%|".toCharArray();
        for (char c : toIgnore) if (c == charIn) return true;
        return false;
    }
}
