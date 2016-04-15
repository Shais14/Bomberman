package data;

import decision.Action;
import decision.DTreeNode;
import decision.Evaluation;
import movement.Align;
import movement.Arrive;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents any character in the game that can move, and can be rendered
 *
 * Created by Anand on 4/1/2016.
 */
public abstract class Character {
    public KinematicInfo kinematicInfo;
    public Arrive sArrive;
    public Align sAlign;
    public SteeringInfo steering;
    public BombermanMap map;

    public int fill;
    PShape mainShape, crumbShape;
    ArrayList<PVector> breadCrumbs;
    public PApplet parent;

    public DTreeNode decisionTreeHead;
    public boolean isPerformingAction;
    public Action currAction;

    long lastAIComputeTime, lastCrumbDrawTime;

    public Character() {

    }

    public Character(PApplet parent, BombermanMap map) {
        this.map = map;
        this.parent = parent;
    }

    public void setFill(int fill)
    {
        this.fill = fill;
    }

    public void initialize(PVector startingPoint) {
        PVector initialPos;
        if (startingPoint == null) {
            initialPos = new PVector();
        } else {
            initialPos = startingPoint.copy();
        }

        kinematicInfo = new KinematicInfo();
        kinematicInfo.setPosition(initialPos);
        kinematicInfo.setOrientation(0);
        kinematicInfo.setMaxSpeed(Const.MAX_SPEED);

        KinematicInfo target = new KinematicInfo();
        target.setPosition(initialPos);
        target.setOrientation(0);

        sArrive = new Arrive();
        sArrive.setMaxAcceleration(Const.MAX_LINEAR_ACCELERATION);
        sArrive.setPlayer(kinematicInfo);
        sArrive.setTarget(target);
        sArrive.setMaxSpeed(Const.MAX_SPEED);
        sArrive.setRadiusOfDeceleration(Const.LINEAR_RADIUS_DECELERATION);
        sArrive.setRadiusOfSatisfaction(Const.LINEAR_RADIUS_SATISFACTION);
        sArrive.setTimeToTarget(Const.TIME_TARGET_VELOCITY);

        sAlign = new Align();
        sAlign.setMaxRotation(Const.MAX_ROTATION);
        sAlign.setMaxAngularAcceleration(Const.MAX_ANGULAR_ACCELERATION);
        sAlign.setRadiusOfSatisfaction(Const.ANGULAR_RADIUS_SATISFACTION);
        sAlign.setRadiusOfDeceleration(Const.ANGULAR_RADIUS_DECELERATION);
        sAlign.setTimeToTarget(Const.TIME_TARGET_ROTATION);
        sAlign.setPlayer(kinematicInfo);
        sAlign.setTarget(target);

        steering = new SteeringInfo();

        PShape circleShape = parent.createShape(PApplet.ELLIPSE, 0, 0, 20, 20);
        circleShape.setFill(fill);

        PShape orientShape = parent.createShape(PApplet.TRIANGLE, 0, -10, 0, 10, 15, 0);
        orientShape.setFill(fill);

        mainShape = parent.createShape(PApplet.GROUP);
        mainShape.addChild(circleShape);
        mainShape.addChild(orientShape);
        mainShape.setStroke(fill);

        crumbShape = parent.createShape(PApplet.ELLIPSE, 0, 0, 2, 2);
        crumbShape.setFill(parent.color(64));
        breadCrumbs = new ArrayList<PVector>();

        lastAIComputeTime = System.currentTimeMillis();
        lastCrumbDrawTime = System.currentTimeMillis();
    }

    public void draw() {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastAIComputeTime > Const.AI_COMPUTE_TIME)) {
            computeAI();
            lastAIComputeTime = currentTime;
        }

        if ((currentTime - lastCrumbDrawTime > Const.CRUMB_DRAW_TIME)) {
            updateCrumbs();
            lastCrumbDrawTime = currentTime;
        }

        if (sAlign.checkOrientationReached()) {
            kinematicInfo.setRotation(0);
            steering.setAngular(0);
        }

        kinematicInfo.update(steering, currentTime);

        drawCrumbs();

        parent.pushMatrix();
        parent.translate(kinematicInfo.getPosition().x, kinematicInfo.getPosition().y);
        parent.rotate(kinematicInfo.getOrientation());
        parent.shape(mainShape);
        parent.popMatrix();
    }

    abstract public void die();

    public void move(PVector targetPos) {
        KinematicInfo target = new KinematicInfo();
        target.setPosition(targetPos.copy());

        sArrive.setTarget(target);
        sAlign.setTarget(target);

        PVector direction = PVector.sub(target.getPosition(), kinematicInfo.getPosition());
        kinematicInfo.setRotation(0);
        sAlign.getTarget().setOrientation(direction.heading());
    }

    public void turn(float newAngle) {
        KinematicInfo target = new KinematicInfo();
        target.setOrientation(newAngle);

        sAlign.setTarget(target);
    }

    public void turn(PVector targetPos) {
        KinematicInfo target = new KinematicInfo();
        target.setPosition(targetPos.copy());

        PVector direction = PVector.sub(target.getPosition(), kinematicInfo.getPosition());
        kinematicInfo.setRotation(0);
        sAlign.getTarget().setOrientation(direction.heading());
    }

    void updateCrumbs() {
        if (breadCrumbs.size() >= Const.BREADCRUMB_COUNT) {
            breadCrumbs.remove(0);
        }

        breadCrumbs.add(kinematicInfo.getPosition().copy());
    }

    void drawCrumbs() {
        for (PVector crumb : breadCrumbs) {
            parent.pushMatrix();
            parent.translate(crumb.x, crumb.y);
            parent.shape(crumbShape);
            parent.popMatrix();
        }
    }

    void computeAI() {
        steering = sArrive.getSteering();
        sAlign.getSteering(steering);
    }

    public Action evaluateDTree(HashMap<Integer, Object> paramMap) {
        return evaluateNode(decisionTreeHead, paramMap);
    }

    public Action evaluateNode(DTreeNode node, HashMap<Integer, Object> paramMap) {
        if (node instanceof Action) {
            return (Action) node;
        }
        if (node == null) {
            return null;
        }
        DTreeNode nextNode = ((Evaluation)node).evaluate(paramMap);
        return evaluateNode(nextNode, paramMap);
    }

    public boolean canSeeTargetInDir(Character target, PVector dir) {
        Tile nextTile = map.getTileAt(kinematicInfo.getPosition());
        Tile playerTile = map.getTileAt(target.kinematicInfo.getPosition());

        while (nextTile != null) {
            if (nextTile.ty != Tile.type.EMPTY) {
                return false;
            }
            if (nextTile == playerTile) {
                return true;
            }

            PVector nextTileCord = PVector.add(nextTile.posCord, dir);
            nextTile = map.getTileAt(nextTileCord);
        }

        // Will never reach here
        return false;
    }
}
