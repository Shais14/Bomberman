package decision;

/**
 * Marker class to act as a parent for all nodes inside a decision tree
 * Lists the constants for each type of node in the decision tree, as read from the file (and later used by the learning algorithm)
 *
 * Created by Anand on 4/2/2016.
 */
public class DTreeNode {
    public static final int EVAL_SIGNAL_STR_VARY = 1;
    public static final int EVAL_NEXT_TILE_OBSTACLE = 2;
    public static final int EVAL_NEXT_TILE_EMPTY = 3;
    public static final int EVAL_KEY_FOUND = 4;
    public static final int EVAL_NEXT_TILE_HAS_MAX_GE = 5;

    public static final int ACT_PATH_FIND_AND_FOLLOW = 101;
    public static final int ACT_MOVE_NEXT_TILE = 102;
    public static final int ACT_PICK_NEW_DIR = 103;
    public static final int ACT_PLANT_AND_MOVE_UNEXPLORED = 104;
    public static final int ACT_PLANT_AND_MOVE_NEW = 105;
}
