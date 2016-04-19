package debug;

import data.Analysis;
import data.BombermanMap;
import data.Const;
import data.Record;
import decision.DTreeNode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Anand on 4/1/2016.
 */
public class DebugUtil {
    public static void printEdges(BombermanMap bombermanMap) {
        if (Const.USE_LOGS > 0) {
            HashMap<String, ArrayList<String>> edges = bombermanMap.edges;
            for (String currNode : edges.keySet()) {
                System.out.print(" --- " + currNode + " ---> ");

                ArrayList<String> adjList = edges.get(currNode);
                for (int i = 0; i < adjList.size(); i += 2) {
                    System.out.print("{ " + adjList.get(i) + " , " + adjList.get(i + 1) + " }, ");
                }
                System.out.println();
            }
        }
    }

    public static void printSignalStrength(BombermanMap bombermanMap){
        if (Const.USE_LOGS > 0) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    System.out.print(bombermanMap.tiles[i][j].signal + "     ");
                }
                System.out.println();
                System.out.println();
            }
        }
    }

    public static void printDecisionTreeNode(DTreeNode node) {
        if (Const.USE_LOGS > 0) {
            System.out.println(node);
        }
    }

    public static void printDebugString(String str) {
        if (Const.USE_LOGS > 0) {
            System.out.println(str);
        }
    }

    public static String saveMapConfig(BombermanMap bombermanMap) {
        String newFilePath = "debug" + File.separator + System.currentTimeMillis() + ".txt";

        File newFileDir = new File("debug");
        if (!newFileDir.exists()) {
            if (!newFileDir.mkdirs()) {
                System.out.println("Error creating directories required to write the map to file");
                return null;
            }
        }

        File newFile = new File(newFilePath);
        try {
            if (!newFile.isFile()) {
                newFile.createNewFile();
            }

            PrintWriter writer = new PrintWriter(newFile, "UTF-8");
            writer.println(bombermanMap.Treasure);
            for (String currBrick : bombermanMap.bricks) {
                writer.println(currBrick);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newFile.getAbsolutePath();
    }

    public static void printRecords(Record record) {
        System.out.println("Run Count :" + record.runCount);
        System.out.println("For Algorithm: No Signal");
        System.out.println("Score = " + record.score[0]);
        System.out.println("Success = " + record.success[0]);
        System.out.println("Bombs Planted = " + record.bombs[0]);
        System.out.println("Time Taken = " + record.timeInSeconds[0]);
        System.out.println("Reason of Death " + record.death[0]);
        System.out.println();
        System.out.println("Algorithm: Precision");
        System.out.println("Score = " + record.score[1]);
        System.out.println("Success = " + record.success[1]);
        System.out.println("Bombs Planted = " + record.bombs[1]);
        System.out.println("Time Taken = " + record.timeInSeconds[1]);
        System.out.println("Reason of Death " + record.death[1]);
        System.out.println();
        System.out.println("Algorithm: Amplitude");
        System.out.println("Score = " + record.score[2]);
        System.out.println("Success = " + record.success[2]);
        System.out.println("Bombs Planted = " + record.bombs[2]);
        System.out.println("Time Taken = " + record.timeInSeconds[2]);
        System.out.println("Reason of Death " + record.death[2]);
    }

    public static void saveRecords(ArrayList<Record> records){
        String newFilePath = "debug" + File.separator + "Records" + Integer.toString(Const.NUMBER_OF_ITERATIONS) + ".txt" ;

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
            writer.println("Algorithm - Score - Success - Bombs Planted - Time Taken(seconds)- Deaths by");
            for (Record rec: records) {
                writer.println(rec.toString(0));
                writer.println(rec.toString(1));
                writer.println(rec.toString(2));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
