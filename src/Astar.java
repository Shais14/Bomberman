import processing.core.PVector;

import java.util.*;

/**
 * Created by jeevan on 31-Mar-16.
 */
class Node
{
    String name;
    float cost, estimateTotal;

    public Node(String s)
    {
        name = s;
    }


    public Node(String s, float c, float e)
    {
        name = s;
        cost = c;
        estimateTotal = e;
    }

}

public class Astar {

    static HashMap<String, float[]> costHeur = new HashMap<String, float[]>();

    static class PQsort implements Comparator<Node> {
        public int compare(Node one, Node two) {
            return (int) (one.estimateTotal - two.estimateTotal);
        }
    }

    public static void calHeuristic(String s, String e, String h)
    {
        ArrayList<String> str, strEnd;
        String brick;
        Tile tile;
        int i;
        float x1, y1, x2, y2;
        float value;

        Tile endTile = Tile.toTile(e);

        PVector pos = endTile.posCord;
        x1 = pos.x;
        y1 = pos.y;

        for (i = 0; i<Graph.bricks.size(); i++)
        {
            brick = Graph.bricks.get(i);
            tile = Tile.toTile(brick);
            x2 = tile.posCord.x;
            y2 = tile.posCord.y;
            value = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            if(brick.equals(s))
                costHeur.put(brick, new float[]{0, value});
            else
                costHeur.put(brick, new float[]{Integer.MAX_VALUE, value});

        }

//        if (h.equals("E"))
//        {
//            Iterator<String> itr = set.iterator();
//            while(itr.hasNext())
//            {
//                name = itr.next();
//                str = edges.get(name);
//                x2 = Integer.parseInt(str[0]);
//                y2 = Integer.parseInt(str[1]);
//                value = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//                if(name.equals(s))
//                    costHeur.put(name, new float[]{0, value});
//                else
//                    costHeur.put(name, new float[]{Integer.MAX_VALUE, value});
//            }
//
//        }
//        else if(h.equals("M"))
//        {
//            Iterator<String> itr = set.iterator();
//            while(itr.hasNext())
//            {
//                name = itr.next();
//                str = edges.get(name);
//                x2 = Integer.parseInt(str[0]);
//                y2 = Integer.parseInt(str[1]);
//                value = (float) Math.abs(x1 - x2) + Math.abs(y1 - y2);
//                if(name.equals(s))
//                    costHeur.put(name, new float[]{0, value});
//                else
//                    costHeur.put(name, new float[]{Integer.MAX_VALUE, value});
//            }
//
//        }
//        else if(h.equals("EM"))
//        {
//            Iterator<String> itr = set.iterator();
//            while(itr.hasNext())
//            {
//                name = itr.next();
//                str = edges.get(name);
//                x2 = Integer.parseInt(str[0]);
//                y2 = Integer.parseInt(str[1]);
//                value = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))
//                        + Math.abs(x1 - x2) + Math.abs(y1 - y2);
//                value = value*3;
//                if(name.equals(s))
//                    costHeur.put(name, new float[]{0, value});
//                else
//                    costHeur.put(name, new float[]{Integer.MAX_VALUE, value});
//            }
//
//        }


    }

    public static ArrayList<String> pathAstar(String s, String end, String h)
    {
        HashSet<String> visited = new HashSet<String>();
        PQsort pqs = new PQsort();
        PriorityQueue<Node> open = new PriorityQueue<Node>(10, pqs);
        HashMap<String, String> pred = new HashMap<String, String>();
        Node start, node, current = new Node("");
        float cost, oldestimate, newestimate, costsofar, heur, edgecost, oldcostsofar, newcostsofar, values[];
        int n;
        String predsor, temp;
        ArrayList<String> list;
        ArrayList<String> path = new ArrayList<String>();
        ArrayList<String> expand = new ArrayList<String>();
        calHeuristic(s, end, h);

        start = new Node(s, 0, costHeur.get(s)[1]);
        open.offer(start);
        pred.put(s, "");

        while(!open.isEmpty())
        {
            current = open.poll();
            costsofar = current.cost;

            visited.add(current.name);

            expand.add(current.name);

            if (current.name.equals(end))
                break;

            list = Graph.edges.get(current.name);
            n = list.size();

            for (int i = 0; i<n; i+=2)
            {
                temp = list.get(i);
                if (!visited.contains(temp))
                {
                    edgecost = Float.parseFloat(list.get(i+1));
                    values = costHeur.get(temp);
                    oldcostsofar = values[0];
                    heur = values[1];
                    oldestimate = oldcostsofar + heur;
                    newcostsofar = costsofar + edgecost;
                    if (newcostsofar < oldcostsofar)
                    {
                        open.remove(new Node(temp, oldcostsofar, oldestimate));
                        newestimate = newcostsofar + heur;
                        open.offer(new Node(temp, newcostsofar,newestimate));
                        costHeur.put(temp, new float[]{newcostsofar, heur});
                        pred.put(temp, current.name);

                    }

                }
            }

        }

        if(!(current.name.equals(end)))
        {
            System.out.println(end + " is not reachable from " + s);
            return path;
        }

        predsor = end;
        while(predsor!="")
        {
            path.add(0, predsor);
            predsor = pred.get(predsor);
        }


        System.out.println("Path: " + Arrays.toString(path.toArray()));
        System.out.println("Cost: " + costHeur.get(end)[0]);
        System.out.println("Nodes visited: " + expand.toString());
        return path;
    }

}
