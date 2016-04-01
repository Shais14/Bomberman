package algorithms;

import data.BombermanMap;
import data.Node;
import data.Tile;
import processing.core.PVector;

import java.util.*;

/**
 * Created by Anand on 4/1/2016.
 */
public class Astar {
    public static BombermanMap bombermanMap;

    static HashMap<String, float[]> costHeur = new HashMap<String, float[]>();

    static class PQsort implements Comparator<Node> {
        public int compare(Node one, Node two) {
            return (int) (one.estimateTotal - two.estimateTotal);
        }
    }

    public static void calHeuristic(String s, String e, String h) {
        ArrayList<String> str, strEnd;
        String name;
        Tile tile;
        int i, j;
        float x1, y1, x2, y2;
        float value;

        Tile endTile = Tile.toTile(e, bombermanMap);

        PVector pos = endTile.posCord;
        x1 = pos.x;
        y1 = pos.y;

        for (i = 1; i < bombermanMap.row - 1; i++)
            for (j = 1; j<bombermanMap.col - 1; j++)
        {
            tile = bombermanMap.tiles[i][j];
            if (tile.ty == Tile.type.OBSTACLE)
                continue;
//            tile = Tile.toTile(brick, bombermanMap);
            name = tile.toString();
            x2 = tile.posCord.x;
            y2 = tile.posCord.y;
            value = (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            if (name.equals(s))
                costHeur.put(name, new float[]{0, value});
            else
                costHeur.put(name, new float[]{Integer.MAX_VALUE, value});

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

    public static ArrayList<String> pathAstar(String s, String end, String h, BombermanMap map) {
        bombermanMap = map;
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
        calHeuristic(s, end, h);

        start = new Node(s, 0, costHeur.get(s)[1]);
        open.offer(start);
        pred.put(s, "");

        while (!open.isEmpty()) {
            current = open.poll();
            costsofar = current.cost;

            visited.add(current.name);


            if (current.name.equals(end))
                break;

            list = bombermanMap.edges.get(current.name);
            n = list.size();

            for (int i = 0; i < n; i += 2) {
                temp = list.get(i);
                if (!visited.contains(temp)) {
                    edgecost = Float.parseFloat(list.get(i + 1));
                    values = costHeur.get(temp);
                    oldcostsofar = values[0];
                    heur = values[1];
                    oldestimate = oldcostsofar + heur;
                    newcostsofar = costsofar + edgecost;
                    if (newcostsofar < oldcostsofar) {
                        open.remove(new Node(temp, oldcostsofar, oldestimate));
                        newestimate = newcostsofar + heur;
                        open.offer(new Node(temp, newcostsofar, newestimate));
                        costHeur.put(temp, new float[]{newcostsofar, heur});
                        pred.put(temp, current.name);

                    }

                }
            }

        }

        if (!(current.name.equals(end))) {
            System.out.println(end + " is not reachable from " + s);
            return path;
        }

        predsor = end;
        while (predsor != "") {
            path.add(0, predsor);
            predsor = pred.get(predsor);
        }


        System.out.println("Path: " + Arrays.toString(path.toArray()));
        System.out.println("Cost: " + costHeur.get(end)[0]);
        return path;
    }

}
