package com.example.simulation;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Stores a 2D Vector consisting of integer values.
 */
public class IntVector2 implements Serializable, Vector<IntVector2> {

    /**
     * The values are protected on purpose. Do not lower their protection level.
     * Since these constants store objects, a malicious
     * bot could read the object and alter one or multiple of its attributes.
     * For this reason, do not use any of the constant objects of the library anywhere in the project.
     */
    protected final static IntVector2 X = new IntVector2(1, 0);
    protected final static IntVector2 Y = new IntVector2(0, 1);
    protected final static IntVector2 Zero = new IntVector2(0, 0);


    /**
     * the x-component of this vector
     **/
    public int x;

    /**
     * the y-component of this vector
     **/
    public int y;

    /**
     * Constructs a new Vector at the given Coordinates
     *
     * @param x the x-component
     * @param y the y-component
     */
    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a vector from the given vector
     *
     * @param v The vector
     */
    public IntVector2(IntVector2 v) {
        set(v);
    }

    /**
     * @return a new instance with the same attributes
     */
    @Override
    public IntVector2 cpy() {
        return new IntVector2(x, y);
    }

    /**
     * @return the magnitude of this vector in euclidean space
     */
    @Override
    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * This method is faster than {@link #len()} as it avoids calculating a square root.
     * It might be used for length comparison of two vectors.
     *
     * @return the squared euclidean length
     */
    @Override
    public float len2() {
        return x * x + y * y;
    }

    /**
     * Limits the length of this vector, based on the desired maximum length.
     * It will be rounded to the next integer closest to zero in both dimensions independently, if necessary.
     *
     * @param limit desired maximum length for this vector
     * @return this vector for chaining
     */
    @Override
    public IntVector2 limit(float limit) {
        if (this.len() > limit) {
            if ((x | y) == 0) return this;
            float scl = limit / len();
            this.x = (int) (x * scl);
            this.y = (int) (y * scl);
        }
        return this;
    }

    /**
     * Limits the length of this vector, based on the square of the desired maximum length.
     * It will be rounded to the next integer closest to zero in both dimensions independently, if necessary.
     *
     * @param limit2 desired squared maximum length for this vector
     * @return this vector for chaining
     */
    @Override
    public IntVector2 limit2(float limit2) {
        if (this.len2() > limit2) {
            if ((x | y) == 0) return this;
            float scl = limit2 / len2();
            this.x = (int) (x * scl);
            this.y = (int) (y * scl);
        }
        return this;
    }

    /**
     * Alters the magnitude of this vector in euclidean space
     * It will be rounded to the nearest integer values in both dimensions independently.
     * Does nothing if the current length is zero.
     *
     * @param len desired magnitude in euclidean space
     * @return this vector for chaining
     */
    @Override
    public IntVector2 setLength(float len) {
        if ((x | y) == 0) return this;
        return scl(len / len());
    }

    /**
     * Alters the magnitude of this vector in euclidean space
     * It will be rounded to the nearest integer values in both dimensions independently.
     * Does nothing if the current length is zero.
     *
     * @param len2 desired squared magnitude in euclidean space
     * @return this vector for chaining
     */
    @Override
    public IntVector2 setLength2(float len2) {
        if ((x | y) == 0) return this;
        return scl((float) Math.sqrt(len2 / len2()));
    }

    /**
     * Alters the magnitude of this vector to be between min and max
     * It will be rounded to the nearest integer values in both dimensions independently.
     * Does nothing if the current length is zero.
     *
     * @param min Min length
     * @param max Max length
     * @return this vector for chaining
     */
    @Override
    public IntVector2 clamp(float min, float max) {
        final float len = len();
        if (len == 0f) return this;
        if (len > max) return scl(max / len);
        if (len < min) return scl(min / len);
        return this;
    }

    /**
     * Copies the attributes from another instance
     *
     * @param v The vector
     * @return this vector for chaining
     */
    @Override
    public IntVector2 set(IntVector2 v) {
        x = v.x;
        y = v.y;
        return this;
    }

    /**
     * Alters the attributes of this instance.
     *
     * @param x x-component of this vector
     * @param y y-component of this vector
     * @return this vector for chaining
     */
    public IntVector2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Subtracts another vector
     *
     * @param v The other vector
     * @return this vector for chaining
     */
    @Override
    public IntVector2 sub(IntVector2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    /**
     * Sets the length of this vector to 1.
     * Will result in a unit vector, that most closely retains this vectors direction.
     * Prioritises (-1,0) and (1,0) if angle is 45 degree (tied).
     * Does nothing if this vector is zero.
     *
     * @return this vector for chaining
     */
    @Override
    public IntVector2 nor() {
        if ((x | y) == 0) return this;
        int signX = x < 0 ? -1 : 1;
        int signY = y < 0 ? -1 : 1;
        int absX = signX * x;
        int absY = signY * y;

        if (absY > absX) {
            x = 0;
            y = 1;
        } else {
            x = 1;
            y = 0;
        }

        x *= signX;
        y *= signY;

        return this;
    }

    /**
     * Adds the specified vector to this vector.
     * Will alter the attributes of this instance.
     *
     * @param v vector to add
     * @return this vector for chaining
     */
    @Override
    public IntVector2 add(IntVector2 v) {
        x += v.x;
        y += v.y;
        return this;
    }

    /**
     * Calculates the dot product between this vector and the specified vector.
     *
     * @param v vector to multiply with
     * @return the dot product
     */
    @Override
    public float dot(IntVector2 v) {
        return x * v.x + y * v.y;
    }

    /**
     * Multiplies this vector with the specified scalar.
     * It will be rounded to the nearest integer values in both dimensions independently.
     *
     * @param scalar amount to scale
     * @return this vector for chaining
     */
    @Override
    public IntVector2 scl(float scalar) {
        x = Math.round(x * scalar);
        y = Math.round(y * scalar);
        return this;
    }

    /**
     * Scales each dimension of this instance by the equivalent value of the specified vector
     *
     * @param v amount to scale in each dimension
     * @return this vector for chaining
     */
    @Override
    public IntVector2 scl(IntVector2 v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    /**
     * Returns the euclidean distance between this vector and the specified vector.
     *
     * @param v the other vector
     * @return euclidean distance
     */
    @Override
    public float dst(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return (float) Math.sqrt(dx * dx + y_d * y_d);
    }

    /**
     * Returns the square of the euclidean distance between this vector and the specified vector.
     *
     * @param v the other vector
     * @return squared euclidean distance
     */
    @Override
    public float dst2(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return dx * dx + y_d * y_d;
    }

    /**
     * Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1].
     * Will alter the attributes of this instance.
     * It will be rounded to the nearest integer values in both dimensions independently.
     *
     * @param target The target vector
     * @param alpha  The interpolation coefficient
     * @return This vector for chaining.
     */
    @Override
    public IntVector2 lerp(IntVector2 target, float alpha) {
        final float invAlpha = 1.0f - alpha;
        this.x = Math.round((x * invAlpha) + (target.x * alpha));
        this.y = Math.round((y * invAlpha) + (target.y * alpha));
        return this;
    }

    /**
     * Interpolates between this vector and the given target vector by alpha (within range [0,1]) using the given Interpolation
     * method.
     * Will alter the attributes of this instance.
     * It will be rounded to the nearest integer values in both dimensions independently.
     *
     * @param target        The target vector
     * @param alpha         The interpolation coefficient
     * @param interpolation An Interpolation object describing the used interpolation method
     * @return This vector for chaining.
     */
    @Override
    public IntVector2 interpolate(IntVector2 target, float alpha, Interpolation interpolation) {
        return lerp(target, interpolation.apply(alpha));
    }

    /**
     * Sets this vector to the unit vector with a random direction.
     * Since this is an integer vector, unit vectors only exist along the axis.
     *
     * @return This vector for chaining
     */
    @Override
    public IntVector2 setToRandomDirection() {
        int r = MathUtils.random(3);
        int x = 0;
        int y = 0;
        switch (r) {
            case 0:
                x = 1;
                break;
            case 1:
                x = -1;
                break;
            case 2:
                y = 1;
                break;
            case 3:
                y = -1;
                break;
        }
        return this.set(x, y);
    }

    /**
     * @return True if this is a unit vector
     */
    @Override
    public boolean isUnit() {
        return len2() == 1;
    }

    /**
     * Calculates whether this vector is a unit vector within the specified margin.
     *
     * @return True if this is a unit vector
     */
    @Override
    public boolean isUnit(final float margin) {
        return Math.abs(len2() - 1f) < margin;
    }

    /**
     * @return True if this is a zero vector
     */
    @Override
    public boolean isZero() {
        return (x | y) == 0;
    }

    /**
     * Calculates whether this vector is a zero vector within the specified margin.
     *
     * @return True if this is a zero vector
     */
    @Override
    public boolean isZero(float margin) {
        return len2() < margin;
    }

    /**
     * @return true if this vector is in line with the other vector (either in the same or the opposite direction)
     */
    @Override
    public boolean isOnLine(IntVector2 other) {
        return MathUtils.isZero(x * other.y - y * other.x);
    }

    /**
     * @return true if this vector is in line with the other vector (either in the same or the opposite direction)
     */
    @Override
    public boolean isOnLine(IntVector2 other, float epsilon) {
        return MathUtils.isZero(x * other.y - y * other.x, epsilon);
    }

    /**
     * @return true if this vector is collinear with the other vector ({@link #isOnLine(IntVector2, float)} &&
     * {@link #hasSameDirection(IntVector2)}).
     */
    @Override
    public boolean isCollinear(IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) > 0f;
    }

    /**
     * @return true if this vector is collinear with the other vector ({@link #isOnLine(IntVector2)} &&
     * {@link #hasSameDirection(IntVector2)}).
     */
    @Override
    public boolean isCollinear(IntVector2 other) {
        return isOnLine(other) && dot(other) > 0f;
    }

    /**
     * @return true if this vector is opposite collinear with the other vector ({@link #isOnLine(IntVector2, float)} &&
     * {@link #hasOppositeDirection(IntVector2)}).
     */
    @Override
    public boolean isCollinearOpposite(IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) < 0f;
    }

    /**
     * @return true if this vector is opposite collinear with the other vector ({@link #isOnLine(IntVector2)} &&
     * {@link #hasOppositeDirection(IntVector2)}).
     */
    @Override
    public boolean isCollinearOpposite(IntVector2 other) {
        return isOnLine(other) && dot(other) < 0f;
    }

    /**
     * @return Whether this vector is perpendicular with the other vector. True if the dot product is 0.
     */
    @Override
    public boolean isPerpendicular(IntVector2 vector) {
        return MathUtils.isZero(dot(vector));
    }

    /**
     * @param epsilon a positive small number close to zero
     * @return Whether this vector is perpendicular with the other vector. True if the dot product is 0.
     */
    @Override
    public boolean isPerpendicular(IntVector2 vector, float epsilon) {
        return MathUtils.isZero(dot(vector), epsilon);
    }

    /** @return Whether this vector has similar direction compared to the other vector. True if the normalized dot product is >
     *         0. */
    @Override
    public boolean hasSameDirection(IntVector2 vector) {
        return dot(vector) > 0;
    }

    /** @return Whether this vector has opposite direction compared to the other vector. True if the normalized dot product is <
     *         0. */
    @Override
    public boolean hasOppositeDirection(IntVector2 vector) {
        return dot(vector) < 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IntVector2 other = (IntVector2) obj;
        if (x != other.x) return false;
        return y == other.y;
    }

    /** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     * @param other the other vector
     * @param epsilon margin
     * @return whether the vectors have fuzzy equality. */
    @Override
    public boolean epsilonEquals(IntVector2 other, float epsilon) {
        if (other == null) return false;
        if (Math.abs(other.x - x) > epsilon) return false;
        return !(Math.abs(other.y - y) > epsilon);
    }

    /**
     * Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     *
     * @return whether the vectors are the same.
     */
    public boolean epsilonEquals(float x, float y, float epsilon) {
        if (Math.abs(x - this.x) > epsilon) return false;
        return !(Math.abs(y - this.y) > epsilon);
    }

    /**
     * Compares this vector with the other vector using MathUtils.FLOAT_ROUNDING_ERROR for fuzzy equality testing
     *
     * @param other other vector to compare
     * @return true if vector are equal, otherwise false
     */
    public boolean epsilonEquals(final IntVector2 other) {
        return epsilonEquals(other, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    /**
     * Compares this vector with the other vector using MathUtils.FLOAT_ROUNDING_ERROR for fuzzy equality testing
     *
     * @param x x component of the other vector to compare
     * @param y y component of the other vector to compare
     * @return true if vector are equal, otherwise false
     */
    public boolean epsilonEquals(float x, float y) {
        return epsilonEquals(x, y, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    /** First scale a supplied vector, then add it to this vector.
     * @param v addition vector
     * @param scalar for scaling the addition vector */
    @Override
    public IntVector2 mulAdd(IntVector2 v, float scalar) {
        this.x += v.x * scalar;
        this.y += v.y * scalar;
        return this;
    }

    /** First scale a supplied vector, then add it to this vector.
     * Alters only this instance.
     * @param v addition vector
     * @param mulVec vector by whose values the addition vector will be scaled */
    @Override
    public IntVector2 mulAdd(IntVector2 v, IntVector2 mulVec) {
        this.x += v.x * mulVec.x;
        this.y += v.y * mulVec.y;
        return this;
    }

    /** Sets the components of this vector to 0
     * @return This vector for chaining */
    @Override
    public IntVector2 setZero() {
        return set(0, 0);
    }

    /**
     * @return An equivalent float vector
     */
    public Vector2 toFloat() {
        return new Vector2(x, y);
    }

    /**
     * Adds the specified values to this vector.
     * Will alter the attributes of this instance.
     *
     * @return this vector for chaining
     */
    public IntVector2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
