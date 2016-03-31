package algorithms.heuristics;

import processing.core.PVector;

/**
 * Created by Anand on 3/8/2016.
 */
public class EuclideanDistance implements HeuristicFunction {
    @Override
    public float getHeuristicValue(PVector startPoint, PVector goalPoint) {
        if (startPoint == null || goalPoint == null) {
            throw new IllegalArgumentException("Positions not specified for starting and/or goal nodes");
        }

        float xDiff, yDiff, zDiff;
        xDiff = goalPoint.x - startPoint.x;
        yDiff = goalPoint.y - startPoint.y;
        zDiff = goalPoint.z - startPoint.z;

        float distSq = (xDiff * xDiff) + (yDiff * yDiff) + (zDiff * zDiff);
        return (float) Math.sqrt(distSq);
    }
}
