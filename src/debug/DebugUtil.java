package debug;

import data.BombermanMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/1/2016.
 */
public class DebugUtil {
    public static void printEdges(BombermanMap bombermanMap) {
        HashMap<String, ArrayList<String>> edges = bombermanMap.edges;
        for (String currNode: edges.keySet()) {
            System.out.print(" --- " + currNode + " ---> ");

            ArrayList<String> adjList = edges.get(currNode);
            for (int i = 0; i < adjList.size(); i+=2) {
                System.out.print("{ " + adjList.get(i) + " , " + adjList.get(i+1) + " }, ");
            }
            System.out.println();
        }
    }
}
