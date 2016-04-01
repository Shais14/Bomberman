package data;

/**
 * Created by Anand on 4/1/2016.
 */
public class Node {
    public String name;
    public float cost, estimateTotal;

    public Node(String s) {
        name = s;
    }

    public Node(String s, float c, float e) {
        name = s;
        cost = c;
        estimateTotal = e;
    }
}
