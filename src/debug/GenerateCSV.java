package debug; /**
 * Created by Shais Shaikh on 4/19/2016.
 */

import data.Analysis;
import data.Record;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GenerateCSV {


    public static void generateCsvFile(String sFileName, ArrayList<Record> records) {
        try {
            FileWriter writer = new FileWriter(sFileName);

//            writer.println("Algorithm - Score - Success - Bombs Planted - Time Taken(seconds)- Deaths by");


            writer.append("Algorithm number");
            writer.append(',');
            writer.append("Algorithm");
            writer.append(',');
            writer.append("Score");
            writer.append(',');
            writer.append("Success");
            writer.append(',');
            writer.append("Bombs Planted");
            writer.append(',');
            writer.append("Time Taken(seconds)");
            writer.append(',');
            writer.append("Death by");

            writer.append('\n');


            for (Record rec : records) {
                appendtoCSV(0, rec, writer);
                appendtoCSV(1, rec, writer);
                appendtoCSV(2, rec, writer);

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendtoCSV(int i, Record rec, FileWriter writer) {
        String tokens[] = rec.toString(i).split(" ");


        try {
            writer.append(tokens[0]);
            writer.append(',');
            writer.append(tokens[2]);
            writer.append(',');
            writer.append(tokens[4]);
            writer.append(',');
            writer.append(tokens[6]);
            writer.append(',');
            writer.append(tokens[8]);
            writer.append(',');
            writer.append(tokens[10]);
            writer.append(',');
            writer.append(tokens[12]);

            writer.append('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void compareCsvFile(String s, ArrayList<Record> records, Analysis anal) {

        try {
            FileWriter writer = new FileWriter(s);

//            writer.println("Algorithm - Score - Success - Bombs Planted - Time Taken(seconds)- Deaths by");


            writer.append("Algorithm");
            writer.append(',');
            writer.append("Success");
            writer.append(',');
            writer.append("Deaths by Bomb");
            writer.append(',');
            writer.append("Deaths by Enemy");
            writer.append(',');
            writer.append("Deaths by Timeout");
            writer.append(',');
            writer.append("Deaths by Exception");
            writer.append(',');
            writer.append('\n');



                appendToComparison(0, writer, anal);
                appendToComparison(1, writer, anal);
                appendToComparison(2, writer, anal);
             writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void appendToComparison(int i,  FileWriter writer, Analysis anal) throws IOException {


        switch (i) {
            case (0):
                writer.append("No signal");
                writer.append(",");
                writer.append(Integer.toString(anal.numSuccessNS));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathNS[1], anal.countNS, anal.numfailsNS));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathNS[2], anal.countNS, anal.numfailsNS));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathNS[3], anal.countNS, anal.numfailsNS));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathNS[4], anal.countNS, anal.numfailsNS));
                writer.append(",");

                writer.append("\n");

                break;
            case (1):
                writer.append("Presence");
                writer.append(",");
                writer.append(Integer.toString(anal.numSuccessP));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathP[1], anal.countP, anal.numfailsP));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathP[2], anal.countP, anal.numfailsP));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathP[3], anal.countP, anal.numfailsP));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathP[4], anal.countP, anal.numfailsP));
                writer.append(",");

                writer.append("\n");

                break;
            case (2):
                writer.append("Amplitude");
                writer.append(",");
                writer.append(Integer.toString(anal.numSuccessA));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathA[1], anal.countA, anal.numfailsA));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathA[2], anal.countA, anal.numfailsA));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathA[3], anal.countA, anal.numfailsA));
                writer.append(",");
                writer.append(findString(anal.reasonOfDeathA[4], anal.countA, anal.numfailsA));
                writer.append(",");

                writer.append("\n");

                break;
        }
    }

    private static String findString(int i, int j, int k) {
        if (k != 0 && j != 0) {
            return "(" + Float.toString(((float)i) /((float) j) * 100) + "%)" + i + "( " + Float.toString(((float) i) /((float) k) * 100) + "%)";
        } else {
            return " - ";
        }
    }


    public static void successCsvFile(String s, ArrayList<Record> records, Analysis anal) {

        try {
            FileWriter writer = new FileWriter(s);

//            writer.println("Algorithm - Score - Success - Bombs Planted - Time Taken(seconds)- Deaths by");


            writer.append("Algorithm");
            writer.append(',');
            writer.append("Average Time");
            writer.append(',');
            writer.append("Average Bombs placed");
            writer.append(',');
            writer.append("Best case time(bombs)");
            writer.append(',');
            writer.append("Worst case time (bombs)");
            writer.append(',');
            writer.append("Best case bombs placed (time)");
            writer.append(',');
            writer.append("Worst case bmbs placed (time)");
            writer.append(',');
            writer.append('\n');


                appendToSuccess(0, writer, anal);
                appendToSuccess(1, writer, anal);
                appendToSuccess(2, writer, anal);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void appendToSuccess(int i, FileWriter writer, Analysis anal) throws IOException {


        switch (i) {
            case (0):
                writer.append("No signal");
                writer.append(",");
                writer.append(Integer.toString(anal.avgTimeNS));
                writer.append(",");
                writer.append(Integer.toString(anal.avgBombNS));
                writer.append(",");
                writer.append(findString(anal.bestTimeNS, anal.bestTimeNSbomb));
                writer.append(",");
                writer.append(findString(anal.worstTimeNS, anal.worstTimeNSbomb));
                writer.append(",");
                writer.append(findString(anal.bestBombNS, anal.bestBombNStime));
                writer.append(",");
                writer.append(findString(anal.worstBombNS, anal.worstBombNStime));
                writer.append(",");

                writer.append("\n");

                break;
            case (1):
                writer.append("Presence");
                writer.append(",");
                writer.append(Integer.toString(anal.avgTimeP));
                writer.append(",");
                writer.append(Integer.toString(anal.avgBombP));
                writer.append(",");
                writer.append(findString(anal.bestTimeP, anal.bestTimePbomb));
                writer.append(",");
                writer.append(findString(anal.worstTimeP, anal.worstTimePbomb));
                writer.append(",");
                writer.append(findString(anal.bestBombP, anal.bestBombPtime));
                writer.append(",");
                writer.append(findString(anal.worstBombP, anal.worstBombPtime));
                writer.append(",");

                writer.append("\n");

                break;
            case (2):
                writer.append("Amplitude");
                writer.append(",");
                writer.append(Integer.toString(anal.avgTimeA));
                writer.append(",");
                writer.append(Integer.toString(anal.avgBombA));
                writer.append(",");
                writer.append(findString(anal.bestTimeA, anal.bestTimeAbomb));
                writer.append(",");
                writer.append(findString(anal.worstTimeA, anal.worstTimeAbomb));
                writer.append(",");
                writer.append(findString(anal.bestBombA, anal.bestBombAtime));
                writer.append(",");
                writer.append(findString(anal.worstBombA, anal.worstBombAtime));
                writer.append(",");

                writer.append("\n");

                break;
        }
    }

    public static String findString(int i, int j) {

        return Integer.toString(i) + " ( " + Integer.toString(j)+ " ) ";
    }
}