package data;

import processing.core.PConstants;

import java.util.Random;

/**
 * This class contains utility methods that can be used anywhere in the project.
 *
 * Created by Anand on 2/13/2016.
 */
public class Helper {
    /**
     * Method to bring the value of an angle to the range, (-PI, PI]
     * @param angle The angle to be normalized (in radians)
     * @return An angle in the range (-PI, PI]
     */
    public static float normalizeAngle(float angle) {
        return (angle > PConstants.PI) ?
                (angle - (2 * PConstants.PI)) :
                (angle < -PConstants.PI) ?
                        (angle + (2 * PConstants.PI)) :
                        angle;
    }

    /**
     * Method to check if two angles are equal
     * @param angle1 An angle, specified in radians
     * @param angle2 Another angle, specified in radians
     * @return Whether or not the two given angles are equal
     */
    public static boolean checkAngleEquality(float angle1, float angle2) {
        return ((Math.abs(mapToRange(angle1) - mapToRange(angle2))) % (2 * PConstants.PI) < Const.ANGLE_EPSILON) ||
                ((Math.abs(mapToRange(angle1) - mapToRange(angle2))) % (2 * PConstants.PI) >= (2 * PConstants.PI) - Const.ANGLE_EPSILON);
    }

    public static float randomBinomial() {
        Random r = new Random();
        return r.nextFloat() - r.nextFloat();
    }

    public static float mapToRange(float angle) {
        float r = angle % (2* PConstants.PI);
        if (Math.abs(r) <= PConstants.PI) {
            return r;
        } else {
            int mult = (r < PConstants.PI) ? 1 : -1;
            return r + mult * (2 * PConstants.PI);
        }
    }
}
