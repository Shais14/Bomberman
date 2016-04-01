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

    public static  void printSignalStrength(BombermanMap bombermanMap){


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


        for(int i =0; i< 15; i++){
            for(int j=0; j < 15; j++){
                System.out.print(bombermanMap.tiles[i][j].signal + "     ");
            }
            System.out.println();
            System.out.println();
        }
    }

}
