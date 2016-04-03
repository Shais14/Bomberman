package decision;

import java.util.HashMap;

/**
 * Created by Anand on 4/2/2016.
 */
public abstract class Action extends DTreeNode{
    public abstract void performAction(HashMap<Integer, Object> paramMap);
}
