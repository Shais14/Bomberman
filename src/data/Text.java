
package data;

import processing.core.PApplet;
import java.util.HashMap;

/**
 * Created by Shais Shaikh on 4/2/2016.
 */
public class Text {
    PApplet parent;

    public Text(PApplet parent) {
        this.parent = parent;
    }

    public void draw(HashMap<Integer, Object> paramMap, int iterationCount, boolean generateRandomMap, long gameTime) {
        drawTime(paramMap, gameTime);
        drawApproachUsed(paramMap, iterationCount);
        drawCurrAction(paramMap);
        drawNextTarget(paramMap);
        drawRandomized(generateRandomMap);
//        drawLife(player);
    }

//    public void drawLife(PlayerInfo player) {
//        parent.textSize(20);
//        parent.fill(255, 160);
//        String lives = "Lives : " + player.lives ;
//        parent.text(lives, 450, 38);
//
//    }

    public void drawTime(HashMap<Integer, Object> paramMap, long gameTime) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);

        gameTime = 180 - gameTime/1000;
        parent.textSize(14);
        parent.fill(255, 160);
        parent.text("Time : " + Long.toString(gameTime), 20, 38);
    }

    public void drawRandomized(boolean generateRandomMap) {
        parent.textSize(14);
        parent.fill(255, 255);
        parent.text("Randomize Map : " + generateRandomMap, 200, 38);
    }

    public void drawApproachUsed(HashMap<Integer, Object> paramMap, int iterationCount) {
        String approach = "Approach : ";
        switch (iterationCount % 3) {
            case Const.IGNORE:
                approach += "Ignore";
                break;
            case Const.PRESENCE:
                approach += "Presence";
                break;
            case Const.AMPLITUDE:
                approach += "Amplitude";
                break;
        }

        parent.textSize(14);
        parent.fill(255, 255);
        parent.text(approach, 460, 38);
    }

    public void drawNextTarget(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);

        String nextTargetStr = player.currAction.getNextTarget(paramMap);
        parent.textSize(14);
        parent.fill(255, 255);
        parent.text("Next Target : " + nextTargetStr, 425, 582);
    }

    public void drawCurrAction(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        String actionStr = player.currAction.toString();
        actionStr = actionStr.replace("--- ", "");
        if (actionStr.length() > 50) {
            actionStr = actionStr.substring(0, 50);
        }
        parent.textSize(14);
        parent.fill(255, 255);
        parent.text(actionStr, 20, 582);
    }

}