package com.example.animation.entity;

import com.badlogic.gdx.math.Vector2;
import com.example.animation.entity.Entity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSingleEntity {

    Entity entity;

    @Before
    public void setup() {
        entity = new Entity();
    }

    @Test
    public void testEmptyConstructor() {

        Assert.assertEquals("Initial absolute X-Coordinate should be 0", 0, entity.getPos().x, 0);
        Assert.assertEquals("Initial relative X-Coordinate should be 0", 0, entity.getRelPos().x, 0);

        Assert.assertEquals("Initial absolute Y-Coordinate should be 0", 0, entity.getPos().y, 0);
        Assert.assertEquals("Initial relative Y-Coordinate should be 0", 0, entity.getRelPos().y, 0);

        Assert.assertEquals("Initial absolute rotation should be 0", 0, entity.getAngle(), 0);
        Assert.assertEquals("Initial relative rotation should be 0", 0, entity.getRelAngle(), 0);

        Assert.assertFalse("Initial absolute flip should be false", entity.isFlipped());
        Assert.assertFalse("Initial relative flip should be false", entity.isRelFlipped());

        Assert.assertNull("Initially no parent should exist", entity.getParent());
        Assert.assertEquals("Initially no children should exist", 0, entity.getChildren().size());

        Assert.assertEquals("Initial X-Scale should be 1", 1, entity.getScale().x, 0);
        Assert.assertEquals("Initial Y-Scale should be 1", 1, entity.getScale().y, 0);
    }

    @Test
    public void testSetPos() {
        float x = 1.5f;
        float y = -2.5f;
        Vector2 pos = new Vector2(x, y);
        entity.setPos(pos);
        Assert.assertEquals("Absolute X-Coordinate was not changed correctly", x, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate was not changed correctly", y, entity.getPos().y, 0);

        Assert.assertEquals("Relative X-Coordinate should be unaffected", 0, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate should be unaffected", 0, entity.getRelPos().y, 0);

        pos.set(0, 0);
        Assert.assertEquals("Absolute X-Coordinate should stay the same, even if vector in argument was changed after the call", x, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate should stay the same, even if vector in argument was changed after the call", y, entity.getPos().y, 0);

        Assert.assertEquals("Relative X-Coordinate should be unaffected", 0, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate should be unaffected", 0, entity.getRelPos().y, 0);
    }

    @Test
    public void testSetRelPosition() {
        float x = 1.5f;
        float y = -2.5f;
        Vector2 pos = new Vector2(x, y);
        entity.setRelPos(pos);
        Assert.assertEquals("Absolute X-Coordinate was not changed correctly", x, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate was not changed correctly", y, entity.getPos().y, 0);
        Assert.assertEquals("Relative X-Coordinate was not changed correctly", x, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate was not changed correctly", y, entity.getRelPos().y, 0);

        pos.set(0, 0);
        Assert.assertEquals("Absolute X-Coordinate should stay the same, even if vector in argument was changed after the call", x, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate should stay the same, even if vector in argument was changed after the call", y, entity.getPos().y, 0);

        Assert.assertEquals("Relative X-Coordinate should stay the same, even if vector in argument was changed after the call", x, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate should stay the same, even if vector in argument was changed after the call", y, entity.getRelPos().y, 0);
    }

    @Test
    public void testAbsoluteRelativePosition() {
        float x1 = 1.5f;
        float y1 = -2.5f;
        Vector2 pos1 = new Vector2(x1, y1);
        entity.setPos(pos1);

        float x2 = 1.5f;
        float y2 = -2.5f;
        Vector2 pos2 = new Vector2(x2, y2);
        entity.setRelPos(pos2);

        Assert.assertEquals("Absolute X-Coordinate was not changed correctly", x1, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate was not changed correctly", y1, entity.getPos().y, 0);

        Assert.assertEquals("Relative X-Coordinate was not changed correctly", x2, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate was not changed correctly", y2, entity.getRelPos().y, 0);

    }

    @Test
    public void testRelativeAbsolutePosition() {
        float x1 = 1.5f;
        float y1 = -2.5f;
        Vector2 pos1 = new Vector2(x1, y1);
        entity.setRelPos(pos1);

        float x2 = 1.5f;
        float y2 = -2.5f;
        Vector2 pos2 = new Vector2(x2, y2);
        entity.setPos(pos2);

        Assert.assertEquals("Relative X-Coordinate was not changed correctly", x1, entity.getRelPos().x, 0);
        Assert.assertEquals("Relative Y-Coordinate was not changed correctly", y1, entity.getRelPos().y, 0);

        Assert.assertEquals("Absolute X-Coordinate was not changed correctly", x2, entity.getPos().x, 0);
        Assert.assertEquals("Absolute Y-Coordinate was not changed correctly", y2, entity.getPos().y, 0);

    }

    @Test
    public void testSetAngle() {
        float angle = 15;
        entity.setAngle(angle);
        Assert.assertEquals("Absolute Angle was not changed correctly", angle, entity.getAngle(), 0);

        Assert.assertEquals("Relative Angle should be unaffected", 0, entity.getRelAngle(), 0);
    }

    @Test
    public void testSetRelAngle() {
        float angle = 15;
        entity.setRelAngle(angle);
        Assert.assertEquals("Absolute Angle was not changed correctly", angle, entity.getAngle(), 0);

        Assert.assertEquals("Relative Angle was not changed correctly", angle, entity.getRelAngle(), 0);
    }


    @Test
    public void testAbsoluteRelativeAngle() {
        float angle1 = 45;
        entity.setAngle(angle1);
        float angle2 = 15;
        entity.setRelAngle(angle2);

        Assert.assertEquals("Absolute Angle should have been overridden", angle2, entity.getAngle(), 0);

        Assert.assertEquals("Relative Angle was not changed correctly", angle2, entity.getRelAngle(), 0);

    }

    @Test
    public void testRelativeAbsoluteAngle() {
        float angle1 = 45;
        entity.setRelAngle(angle1);
        float angle2 = 15;
        entity.setAngle(angle2);

        Assert.assertEquals("Absolute Angle was not changed correctly", angle1, entity.getRelAngle(), 0);

        Assert.assertEquals("Relative Angle was not changed correctly", angle2, entity.getAngle(), 0);

    }

}
