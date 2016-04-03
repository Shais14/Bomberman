package decision;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base abstract class to model the inner nodes of a decision tree.
 *
 * Created by Anand on 4/2/2016.
 */
public abstract class Evaluation extends DTreeNode{
    public static int TRUE_INDEX = 1;
    public static int FALSE_INDEX = 0;

    public ArrayList<DTreeNode> children;
    public abstract DTreeNode evaluate(HashMap<Integer, Object> paramMap);

    public Evaluation() {
        this.children = new ArrayList<>();
    }
}
