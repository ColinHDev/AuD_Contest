package com.example.simulation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Stores the boundaries of a rectangle that is located in an array or grid.
 * As a result the minimum width and height of this rectangle will be 1, since even an IntRectangle going from the points
 * (0,0) to (0,0) will occupy at least the pixel/cell at (0,0).
 */
public class IntRectangle implements Serializable, Shape2D {

    public int x;
    public int y;
    public int width;
    public int height;

    /**
     * Creates a new IntRectangle with the dimensions:
     * X = Y = 0 and width = height = 1
     */
    public IntRectangle() {
        width = 1;
        height = 1;
    }

    /**
     * Creates a new IntRectangle with the specified dimensions.
     * The width and height have to be at least 1, since the rectangle
     * will at least occupy the pixel/cell at position (x,y).
     *
     * @param x      x-position of the lower left corner
     * @param y      y-position of the lower left corner
     * @param width  size of the rectangle in x-direction
     * @param height size of the rectangle in y-direction
     */
    public IntRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        if (width < 1) throw new IllegalArgumentException("An IntRectangle has to be at least of width 1");
        if (height < 0) throw new IllegalArgumentException("An IntRectangle has to be at least of height 1");
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a copy of another instance.
     * Will copy position and size of the specified rectangle.
     *
     * @param rect instance to copy from
     */
    public IntRectangle(IntRectangle rect) {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }

    /**
     * Alter the position and dimension of this instance.
     * The width and height have to be at least 1, since the rectangle
     * will at least occupy the pixel/cell at position (x,y).
     *
     * @param x      x-position of the lower left corner
     * @param y      y-position of the lower left corner
     * @param width  size of the rectangle in x-direction
     * @param height size of the rectangle in y-direction
     * @return this instance for chaining
     */
    public IntRectangle set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        if (width < 1) throw new IllegalArgumentException("An IntRectangle has to be at least of width 1");
        if (height < 0) throw new IllegalArgumentException("An IntRectangle has to be at least of height 1");
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Moves this rectangle by the specified amount and direction.
     *
     * @param vector2 amount and direction to move this rectangle in
     * @return this instance for chaining
     */
    public IntRectangle add(IntVector2 vector2) {
        x += vector2.x;
        y += vector2.y;
        return this;
    }

    /**
     * @return x-position of the lower left corner
     */
    public int getX() {
        return x;
    }

    /**
     * Alters the x-component of this rectangles position.
     *
     * @param x x-position of the lower left corner
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return y-position of the lower left corner
     */
    public int getY() {
        return y;
    }

    /**
     * Alters the y-component of this rectangles position.
     *
     * @param y y-position of the lower left corner
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return size of the rectangle in x-direction
     */
    public int getWidth() {
        return width;
    }

    /**
     * Alters the width of this rectangle.
     * The width has to be at least 1, since the rectangle
     * will at least occupy the pixel/cell at position (x,y).
     *
     * @param width size of the rectangle in x-direction
     */
    public void setWidth(int width) {
        if (width < 1) throw new IllegalArgumentException("An IntRectangle has to be at least of width 1");
        this.width = width;
    }

    /**
     * @return size of the rectangle in y-direction
     */
    public int getHeight() {
        return height;
    }

    /**
     * Alters the height of this rectangle.
     * The height has to be at least 1, since the rectangle
     * will at least occupy the pixel/cell at position (x,y).
     *
     * @param height size of the rectangle in y-direction
     */
    public void setHeight(int height) {
        if (height < 0) throw new IllegalArgumentException("An IntRectangle has to be at least of height 1");
        this.height = height;
    }

    /**
     * @return A rectangle that covers the same area in float coordinates
     */
    public Rectangle toFloat() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Tests whether the specified position is located within the rectangle defined by this instance.
     *
     * @param point the position to be tested as a 2D float vector
     * @return True, if point is located within this rectangle
     */

    //TODO: add IntVector Overload
    @Override
    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * Tests whether the specified position is located within the rectangle defined by this instance.
     *
     * @param x x-component of the position
     * @param y y-component of the position
     * @return True, if point is located within this rectangle
     */
    @Override
    public boolean contains(float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    /**
     * Gets the min point from a hitbox, to make it AABB compatible
     * @return minimum point from the hitbox
     */
    public IntVector2 min() {
        return new IntVector2(x, y);
    }

    /**
     * Gets the max point from a hitbox, to make it AABB compatible
     * @return maximum point from the hitbox
     */
    public IntVector2 max() {
        return new IntVector2(x + width, y + height);
    }

    public boolean intersects(Vector2 s, Vector2 e) {
        float tmin = Float.NEGATIVE_INFINITY;
        float tmax = Float.POSITIVE_INFINITY;

        Vector2 dir = e.cpy().sub(s);
        if (dir.x != 0f) {
            float tx1 = (this.min().x - s.x) / dir.x;
            float tx2 = (this.max().x - s.x) / dir.x;

            tmin = Float.max(tmin, Float.min(tx1, tx2));
            tmax = Float.min(tmax, Float.max(tx1, tx2));
        }
        if (dir.y != 0f) {
            float ty1 = (this.min().y - s.y) / dir.y;
            float ty2 = (this.max().y - s.y) / dir.y;

            tmin = Float.max(tmin, Float.min(ty1, ty2));
            tmax = Float.min(tmax, Float.max(ty1, ty2));
        }
        return tmax >= tmin;
    }

    public IntRectangle copy() {
        return new IntRectangle(this.x, this.y, this.width, this.height);
    }
}
