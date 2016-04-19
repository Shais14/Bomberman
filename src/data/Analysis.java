package data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Shais Shaikh on 4/18/2016.
 */
public class Analysis {

    public static Scanner scanner;
    public static int lineNumber = 0;
    public static ArrayList<String> noSignal = new ArrayList<String>();
    public static ArrayList<String> precision = new ArrayList<String>();
    public static ArrayList<String> amplitude = new ArrayList<String>();
    int numSuccessA = 0, numfailsA = 0, bestTimeA = Integer.MAX_VALUE, worstTimeA = 0, avgTimeA = 0, bestBombA = Integer.MAX_VALUE, worstBombA = 0, avgBombA = 0;
    int countA ;
    int numSuccessP = 0;
    int numfailsP = 0;
    int bestTimeP = Integer.MAX_VALUE;
    int worstTimeP = 0;
    int avgTimeP = 0;
    int bestBombP = Integer.MAX_VALUE;
    int worstBombP = 0;
    int avgBombP = 0;
    int countP;
    int numSuccessNS = 0, numfailsNS = 0, bestTimeNS = Integer.MAX_VALUE, worstTimeNS = 0, avgTimeNS = 0, bestBombNS = Integer.MAX_VALUE, worstBombNS = 0, avgBombNS = 0;
    int countNS;
    int bestTimeAbomb, worstTimeAbomb, bestBombAtime, worstBombAtime;
    int bestTimePbomb, worstTimePbomb, bestBombPtime, worstBombPtime;
    int bestTimeNSbomb, worstTimeNSbomb, bestBombNStime, worstBombNStime;
    int reasonOfDeathNS[]= new int[4], reasonOfDeathP[]= new int[4], reasonOfDeathA[]= new int[4];



    public  String readNextLine() {
        lineNumber++;
        return scanner.nextLine();
    }

    public void readFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            scanner = new Scanner(path, StandardCharsets.UTF_8.name());
            while (scanner.hasNextLine()) {
                String nextLine = readNextLine();
                if (nextLine.charAt(0) == '1') {
                    noSignal.add(nextLine);
                } else if (nextLine.charAt(0) == '2') {
                    precision.add(nextLine);
                } else if (nextLine.charAt(0) == '3') {
                    amplitude.add(nextLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        countA = amplitude.size();
        countP = precision.size();
        countNS = noSignal.size();



        //TODO: record stats only for successful cases. (change the if else order in the process function of each.)
        processNoSignal();
        processPrecision();
        processAmplitude();
        generateReport();
    }

    public void generateReport() {

        String newFilePath = "debug" + File.separator + "Report.txt";
        String Temp = null;
        File newFileDir = new File("debug");
        if (!newFileDir.exists()) {
            if (!newFileDir.mkdirs()) {
                System.out.println("Error creating directories required to write the map to file");
                return;
            }
        }

        File newFile = new File(newFilePath);
        try {
            if (!newFile.isFile()) {
                newFile.createNewFile();
            }

            PrintWriter writer = new PrintWriter(newFile, "UTF-8");
            writer.println("RESULT OF THE EXPERIMENT");
            writer.println("Number of runs : " + Integer.toString(countNS));

            writer.println("FOR NO SIGNAL : ");
            writer.println("Number of successful runs:" + Integer.toString(numSuccessNS));
            writer.println("Number of failed runs:" + Integer.toString(numfailsNS));
            writer.println("Best case (success) time taken:" + Integer.toString(bestTimeNS) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(bestTimeNSbomb));
            writer.println("Worst case (success) time taken:" + Integer.toString(worstTimeNS) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(worstTimeNSbomb));
            writer.println("Average (success) time taken over all runs:" + Integer.toString(avgTimeNS) + " seconds");
            writer.println("Best case (success) number of bombs planted:" + Integer.toString(bestBombNS) + " seconds");
            writer.println("Time taken in this case:" + Integer.toString(bestBombNStime) + " seconds");
            writer.println("Worst case (success) number of bombs planted:" + Integer.toString(worstBombNS) + " seconds");
            writer.println("Time taken in this case:" + Integer.toString(worstBombNStime) + " seconds");
            writer.println("Average (success) number of bombs planted over all runs:" + Integer.toString(avgBombNS) + " seconds");

            for(int i = 0; i < 4; i++) {
                writer.println("Death by " + causeOfDeath(i) + " " +  Integer.toString(reasonOfDeathNS[i]));
            }

            writer.println();
            writer.println("FOR PRECISION : ");
            writer.println("Number of successful runs:" + Integer.toString(numSuccessP));
            writer.println("Number of failed runs:" + Integer.toString(numfailsP));
            writer.println("Best case (success) time taken:" + Integer.toString(bestTimeP) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(bestTimePbomb));
            writer.println("Worst case (success) time taken:" + Integer.toString(worstTimeP) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(worstTimePbomb));
            writer.println("Average (success) time taken over all runs:" + Integer.toString(avgTimeP) + " seconds");
            writer.println("Best case (success) number of bombs planted:" + Integer.toString(bestBombP) + " seconds");
            writer.println("Time taken in this case:" + Integer.toString(bestBombPtime) + " seconds");
            writer.println("Worst case (success) number of bombs planted:" + Integer.toString(worstBombP) + " seconds");
            writer.println("Time taken in this case:" + Integer.toString(worstBombPtime) + " seconds");
            writer.println("Average (success) number of bombs planted over all runs:" + Integer.toString(avgBombP) + " seconds");

            for(int i = 0; i < 4; i++) {
                writer.println("Death by " + causeOfDeath(i) + " " +  Integer.toString(reasonOfDeathP[i]));
            }

            writer.println();
            writer.println("FOR AMPLITUDE : ");
            writer.println("Number of successful runs:" + Integer.toString(numSuccessA));
            writer.println("Number of failed runs:" + Integer.toString(numfailsA));
            writer.println("Best case (success) time taken:" + Integer.toString(bestTimeA) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(bestTimeAbomb));
            writer.println("Worst case (success) time taken:" + Integer.toString(worstTimeA) + " seconds");
            writer.println("Bombs planted in this case:" + Integer.toString(worstTimeAbomb));
            writer.println("Average (success) time taken over all runs:" + Integer.toString(avgTimeA) + " seconds");
            writer.println("Best case (success) number of bombs planted:" + Integer.toString(bestBombA));
            writer.println("Time taken in this case:" + Integer.toString(bestBombAtime) + " seconds");
            writer.println("Worst case (success) number of bombs planted:" + Integer.toString(worstBombA));
            writer.println("Time taken in this case:" + Integer.toString(worstBombAtime) + " seconds");
            writer.println("Average (success) number of bombs planted over all runs:" + Integer.toString(avgBombA));

            for(int i = 0; i < 4; i++) {
                writer.println("Death by " + causeOfDeath(i) + " " +  Integer.toString(reasonOfDeathA[i]));
            }


            writer.println();
            if(bestTimeA < bestTimeNS && bestTimeA < bestTimeP){
                writer.println("Best case (success) time taken :" + Integer.toString(bestTimeA) + " seconds for AMPLITUDE");
            }
            else if(bestTimeP < bestTimeA && bestTimeP < bestTimeNS){
                writer.println("Best case (success) time taken :" + Integer.toString(bestTimeP) + " seconds for PRECISION");
            }

            else if(bestTimeNS < bestTimeA && bestTimeNS < bestTimeP){
                writer.println("Best case (success) time taken :" + Integer.toString(bestTimeNS) + " seconds for NO SIGNAL");
            }

            
            writer.println();
            if(bestBombA < bestBombNS && bestBombA < bestBombP){
                writer.println("Best case (success) number of bombs planted :" + Integer.toString(bestBombA) + " for AMPLITUDE");
            }
            else if(bestBombP < bestBombA && bestBombP < bestBombNS){
                writer.println("Best case (success) number of bombs planted :" + Integer.toString(bestBombP) + " for PRECISION");
            }

            else if(bestBombNS < bestBombA && bestBombNS < bestBombP){
                writer.println("Best case (success) number of bombs planted :" + Integer.toString(bestBombNS) + " for NO SIGNAL");
            }


            writer.println("Success Ratio for No Signal: " + Float.toString((numSuccessNS / countNS)*100)+"%");
            writer.println("Success Ratio for Precision: " + Float.toString((numSuccessP / countP) * 100)+"%");
            writer.println("Success Ratio for Amplitude: " + Float.toString((numSuccessA / countA) * 100)+"%");


            writer.println("Success Ratio : " + Float.toString((numSuccessA + numSuccessP + numSuccessNS / countNS + countP +  countA) * 100) +"%" );





            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private String causeOfDeath(int i) {
        switch (i){
            case(0):
                return "BOMB";
            case(1):
                return "ENEMY";
            case(2):
                return "TIMEOUT";
            case(3):
                return "EXCEPTION";
        }

        return null;
    }

    private void processNoSignal() {

        for (String temp : noSignal) {
            String tokens[] = temp.split(" ");

//            to calculate success and failures
            if (tokens[6] == "true") {
                numSuccessNS++;
            } else {
                numfailsNS++;
            }

//            to see best, worst, avg time;

                avgTimeNS += Integer.parseInt(tokens[10]);

                if (Integer.parseInt(tokens[10]) <= bestTimeNS) {
                    bestTimeNS = Integer.parseInt(tokens[10]);
                    bestTimeNSbomb = Integer.parseInt(tokens[8]);

                } else if (Integer.parseInt(tokens[10]) >= worstTimeNS) {
                    worstTimeNS = Integer.parseInt(tokens[10]);
                    worstTimeNSbomb = Integer.parseInt(tokens[8]);
                }

//            to see best, worst, avg number of bombs planted;

                avgBombNS += Integer.parseInt(tokens[8]);

                if (Integer.parseInt(tokens[8]) <= bestBombNS) {
                    bestBombNS = Integer.parseInt(tokens[8]);
                    bestBombNStime = Integer.parseInt(tokens[10]);
                } else if (Integer.parseInt(tokens[8]) >= worstBombNS) {
                    worstBombNS = Integer.parseInt(tokens[8]);
                    worstBombNStime = Integer.parseInt(tokens[10]);
                }


            reasonOfDeathNS[Integer.parseInt(tokens[12])]++;

        }

        avgTimeNS /= countNS;
        avgBombNS /= countNS;



    }


    private void processPrecision() {

        for (String temp : precision) {
            String tokens[] = temp.split(" ");

//            to calculate success and failures
            if (tokens[6] == "true") {
                numSuccessP++;
            }
            else {
                numfailsP++;
            }


//            to see best, worst, avg time;

                avgTimeP += Integer.parseInt(tokens[10]);

                if (Integer.parseInt(tokens[10]) <= bestTimeP) {
                    bestTimeP = Integer.parseInt(tokens[10]);
                    bestTimePbomb = Integer.parseInt(tokens[8]);
                } else if (Integer.parseInt(tokens[10]) >= worstTimeP) {
                    worstTimeP = Integer.parseInt(tokens[10]);
                    worstTimePbomb = Integer.parseInt(tokens[8]);
                }

//            to see best, worst, avg number of bombs planted;

                avgBombP += Integer.parseInt(tokens[8]);

                if (Integer.parseInt(tokens[8]) <= bestBombP) {
                    bestBombP = Integer.parseInt(tokens[8]);
                    bestBombPtime = Integer.parseInt(tokens[10]);
                } else if (Integer.parseInt(tokens[8]) >= worstBombP) {
                    worstBombP = Integer.parseInt(tokens[8]);
                    worstBombPtime = Integer.parseInt(tokens[10]);
                }
            reasonOfDeathP[Integer.parseInt(tokens[12])]++;

            }

        avgTimeP /= countP;
        avgBombP /= countP;

    }

    private void processAmplitude() {

        for (String temp : amplitude) {
            String tokens[] = temp.split(" ");

//            to calculate success and failures
            if (tokens[6] == "true") {
                numSuccessA++;
            }else {
                numfailsA++;
            }

//            to see best, worst, avg time;

                avgTimeA += Integer.parseInt(tokens[10]);

                if (Integer.parseInt(tokens[10]) <= bestTimeA) {
                    bestTimeA = Integer.parseInt(tokens[10]);
                    bestTimeAbomb = Integer.parseInt(tokens[8]);
                } else if (Integer.parseInt(tokens[10]) >= worstTimeA) {
                    worstTimeA = Integer.parseInt(tokens[10]);
                    worstTimeAbomb = Integer.parseInt(tokens[8]);
                }

//            to see best, worst, avg number of bombs planted;

                avgBombA += Integer.parseInt(tokens[8]);

                if (Integer.parseInt(tokens[8]) <= bestBombA) {
                    bestBombA = Integer.parseInt(tokens[8]);
                    bestBombAtime = Integer.parseInt(tokens[10]);
                } else if (Integer.parseInt(tokens[8]) >= worstBombA) {
                    worstBombA = Integer.parseInt(tokens[8]);
                    worstBombAtime = Integer.parseInt(tokens[10]);
                }
            reasonOfDeathA[Integer.parseInt(tokens[12])]++;
            }


        avgTimeA /= countA;
        avgBombA /= countA;

    }

public static void main(String args[]){
    Analysis anal = new Analysis();
    anal.readFile(Const.RECORDS_FILE_PATH);

}
}
