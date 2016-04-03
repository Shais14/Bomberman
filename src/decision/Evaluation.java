package decision;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/2/2016.
 */
public abstract class Evaluation extends DTreeNode{
    ArrayList<DTreeNode> children;
    public abstract DTreeNode evaluate(HashMap<Integer, Object> paramMap);
}
