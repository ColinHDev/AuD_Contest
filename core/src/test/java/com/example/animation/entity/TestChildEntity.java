package com.example.animation.entity;

import com.badlogic.gdx.math.Vector2;
import com.example.animation.entity.Entity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestChildEntity {


    Entity parent;
    Entity child;

    @Before
    public void setup() {
        parent = new Entity();
        child = new Entity();
        parent.add(child);
    }

    @Test
    public void testAdd(){
        Assert.assertTrue("Child should be contained in parents list of children", parent.getChildren().contains(child));
        Assert.assertEquals("Parent should only have a single child", 1, parent.getChildren().size());
    }

    @Test
    public void testParent(){
        Assert.assertEquals("Child should correctly return its parent object",parent, child.getParent());
    }

    @Test
    public void testRemove(){
        parent.remove(child);
        Assert.assertFalse("Child should no longer be contained in parents list of children", parent.getChildren().contains(child));
        Assert.assertEquals("Parent should have no children anymore", 0, parent.getChildren().size());

        parent.remove(child);
        Assert.assertFalse("Child should still not be contained in parents list of children", parent.getChildren().contains(child));
        Assert.assertEquals("Parent should still have no children", 0, parent.getChildren().size());

        parent.add(child);
        Assert.assertTrue("Child should be contained in parents list of children again", parent.getChildren().contains(child));
        Assert.assertEquals("Parent should now have a single child", 1, parent.getChildren().size());
    }

    @Test
    public void testParentPosTransfer(){
        float x = 1.5f;
        float y = -2.5f;
        Vector2 pos = new Vector2(x, y);
        parent.setPos(pos);
        Assert.assertEquals("Child's absolute X-Coordinate was not changed correctly", x, child.getPos().x, 0);
        Assert.assertEquals("Child's absolute Y-Coordinate was not changed correctly", y, child.getPos().y, 0);

        Assert.assertEquals("Child's relative X-Coordinate should be unaffected", 0, child.getRelPos().x, 0);
        Assert.assertEquals("Child's relative Y-Coordinate should be unaffected", 0, child.getRelPos().y, 0);
    }

    @Test
    public void testChildRelPos(){
        float x = 1.5f;
        float y = -2.5f;
        Vector2 pos = new Vector2(x, y);
        child.setRelPos(pos);
        Assert.assertEquals("Child's relative X-Coordinate was not changed correctly", x, child.getRelPos().x, 0);
        Assert.assertEquals("Child's relative Y-Coordinate was not changed correctly", y, child.getRelPos().y, 0);

        Assert.assertEquals("Child's absolute X-Coordinate was not changed correctly", x, child.getPos().x, 0);
        Assert.assertEquals("Child's absolute Y-Coordinate was not changed correctly", y, child.getPos().y, 0);

        Assert.assertEquals("Parent's absolute X-Coordinate should be unaffected", 0, parent.getPos().x, 0);
        Assert.assertEquals("Parent's absolute Y-Coordinate should be unaffected", 0, parent.getPos().y, 0);
    }

    @Test
    public void testAdditivePosition(){

        float x1 = 1.5f;
        float y1 = -2.5f;
        Vector2 pos1 = new Vector2(x1, y1);
        parent.setPos(pos1);

        float x2 = -3.5f;
        float y2 = 0.7f;
        Vector2 pos2 = new Vector2(x2, y2);
        child.setRelPos(pos2);

        Assert.assertEquals("Child's absolute X-Coordinate should be the sum of its relative position and its parents absolute position", x1 + x2, child.getPos().x, 0);
        Assert.assertEquals("Child's absolute Y-Coordinate should be the sum of its relative position and its parents absolute position", y1 + y2, child.getPos().y, 0);
    }

    @Test
    public void testParentAngleTransfer(){
        float angle = 15;
        parent.setAngle(angle);
        Assert.assertEquals("Child's absolute Angle was not changed correctly", angle, child.getAngle(), 0);

        Assert.assertEquals("Child's relative Angle should be unaffected", 0, child.getRelAngle(), 0);
    }

    @Test
    public void testChildRelAngle(){
        float angle = 15;
        child.setRelAngle(angle);
        Assert.assertEquals("Child's relative Angle was not changed correctly", angle, child.getRelAngle(), 0);

        Assert.assertEquals("Child's absolute Angle was not changed correctly", angle, child.getAngle(), 0);

        Assert.assertEquals("Parents's absolute Angle should be unaffected", 0, parent.getAngle(), 0);
    }

    @Test
    public void testAdditiveAngle(){

        float angle1 = 45;
        parent.setAngle(angle1);

        float angle2 = 15;
        child.setRelAngle(angle2);

        Assert.assertEquals("Child's absolute angle should be the sum of its relative angle and its parents absolute angle", angle1 + angle2, child.getAngle(), 0);
    }

    @Test
    public void testDetachedPosition(){

        parent.remove(child);

        float x1 = 1.5f;
        float y1 = -2.5f;
        Vector2 pos1 = new Vector2(x1, y1);
        parent.setPos(pos1);

        float x2 = -3.5f;
        float y2 = 0.7f;
        Vector2 pos2 = new Vector2(x2, y2);
        child.setRelPos(pos2);

        Assert.assertEquals("Child's absolute X-Coordinate should be its relative value since its detached", x2, child.getPos().x, 0);
        Assert.assertEquals("Child's absolute Y-Coordinate should be its relative value since its detached", y2, child.getPos().y, 0);

        parent.add(child);
        Assert.assertEquals("Child's absolute X-Coordinate should be the sum of its relative position and its parents absolute position after attachment", x1 + x2, child.getPos().x, 0);
        Assert.assertEquals("Child's absolute Y-Coordinate should be the sum of its relative position and its parents absolute position after attachment", y1 + y2, child.getPos().y, 0);

    }

    @Test
    public void testDetachedAngle(){

        parent.remove(child);

        float angle1 = 45;
        parent.setAngle(angle1);

        float angle2 = 15;
        child.setRelAngle(angle2);

        Assert.assertEquals("Child's absolute angle should be its relative value since its detached", angle2, child.getAngle(), 0);

        parent.add(child);
        Assert.assertEquals("Child's absolute angle should be the sum of its relative angle and its parents absolute angle after attachment", angle1 + angle2, child.getAngle(), 0);
    }

}
