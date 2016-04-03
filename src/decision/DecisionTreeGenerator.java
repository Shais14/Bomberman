package decision;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class used to parse a file containing a decision tree (based on the codes given in DTreeNode.java
 *
 * Created by Anand on 4/2/2016.
 */
public class DecisionTreeGenerator {
    static DTreeNode decisionTreeHead;
    static int lineNumber = 0;
    static Scanner scanner;

    public static String readNextLine() {
        lineNumber++;
        return scanner.nextLine();
    }

    public static DTreeNode generateDecisionTree(String filePath) {
        decisionTreeHead = null;
        Path path = Paths.get(filePath);
        try {
            scanner = new Scanner(path, StandardCharsets.UTF_8.name());
            while (scanner.hasNextLine()) {
                String nextLine = readNextLine();
                parseLine(decisionTreeHead, nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decisionTreeHead;
    }

    public static void parseLine(DTreeNode parentNode, String line) {
        String tokens[] = line.split(" ");

        if (tokens[0].trim().isEmpty() || tokens[0].equals("#")) {
            return;
        }

        int nodeCode = Integer.parseInt(tokens[0]);

        DTreeNode currNode = generateDTreeNode(nodeCode);
        if (tokens.length > 1) {
            int childCount = Integer.parseInt(tokens[1]);
            if (currNode != null) {
                while (((Evaluation)currNode).children.size() < childCount) {
                    if (scanner.hasNextLine()) {
                        String nextLine = readNextLine();
                        parseLine(currNode, nextLine);
                    } else {
                        break;
                    }
                }
            }
        } else {
//            System.out.println(nodeCode);
        }

        if (parentNode == null) {
            decisionTreeHead = currNode;
        } else {
            ((Evaluation) parentNode).children.add(currNode);
        }
    }

    /**
     * Factory method to generate DTreeNodes according to the type of node required
     *
     * @param nodeCode code for a particular node (must be one of the types specified as static and final in DTreeNode.java
     * @return Instance of DTreeNode
     */
    public static DTreeNode generateDTreeNode(int nodeCode) {
        switch(nodeCode) {
            case DTreeNode.ACT_MOVE_NEXT_TILE:
                return new ActMoveNextTile();
            case DTreeNode.ACT_PATH_FIND_AND_FOLLOW:
                return new ActPathFindAndFollow();
            case DTreeNode.ACT_PICK_NEW_DIR:
                return new ActPickNewDir();
            case DTreeNode.ACT_PLANT_AND_MOVE_NEW:
                return new ActPlantAndMoveNew();
            case DTreeNode.ACT_PLANT_AND_MOVE_UNEXPLORED:
                return new ActPlantAndMoveUnexplored();

            case DTreeNode.EVAL_KEY_FOUND:
                return new EvalKeyFound();
            case DTreeNode.EVAL_NEXT_TILE_EMPTY:
                return new EvalNextTileEmpty();
            case DTreeNode.EVAL_NEXT_TILE_HAS_MAX_GE:
                return new EvalNextTileHasMaxGE();
            case DTreeNode.EVAL_NEXT_TILE_OBSTACLE:
                return new EvalNextTileObstacle();
            case DTreeNode.EVAL_SIGNAL_STR_VARY:
                return new EvalSignalStrVariation();
        }

        return null;
    }
}
