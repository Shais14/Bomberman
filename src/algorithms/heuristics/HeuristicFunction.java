package algorithms.heuristics;

import processing.core.PVector;

/**
 * Created by Anand on 3/5/2016.
 */
public interface HeuristicFunction {
    // Heuristic Functions implemented manhattan distance and Euclidean distance.
    // TODO: Implement an overestimating / inadmissible heuristic
    float getHeuristicValue(PVector startPoint, PVector goalPoint);
}
