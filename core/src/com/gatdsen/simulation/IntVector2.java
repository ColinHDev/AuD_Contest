package com.gatdsen.simulation;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Speichert einen 2D-Vektor, der aus ganzzahligen Werten besteht.
 */
public class IntVector2 implements Serializable, Vector<IntVector2> {

    /**
     * Die Werte sind absichtlich protected. Nicht die Sichtbarkeit erhöhen.
     * Da diese Konstanten Objekte speichern, könnte ein bösartiger Bot
     * das Objekt lesen und eine oder mehrere seiner Attribute verändern.
     * Aus diesem Grund dürfen keine der Konstantenobjekte der Bibliothek irgendwo im Projekt verwendet werden.
     */
    protected final static IntVector2 X = new IntVector2(1, 0);
    protected final static IntVector2 Y = new IntVector2(0, 1);
    protected final static IntVector2 Zero = new IntVector2(0, 0);

    public int x;
    public int y;

    /**
     * Konstruiert einen neuen Vektor mit den angegebenen Koordinaten.
     *
     * @param x x-Komponente
     * @param y y-Komponente
     */
    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Konstruiert einen Vector als Kopie eines anderen Vektors.
     *
     * @param v Vektor, von dem kopiert werden soll
     */
    public IntVector2(IntVector2 v) {
        set(v);
    }

    /**
     * @return eine Kopie dieses Vektors
     */
    @Override
    public IntVector2 cpy() {
        return new IntVector2(x, y);
    }

    /**
     * @return Die Länge dieses Vektors im euklidischen Raum
     */
    @Override
    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Diese Methode ist schneller als {@link #len()}, da sie keine Wurzel berechnet.
     * Sie kann für die Längenvergleich von zwei Vektoren verwendet werden.
     *
     * @return Das Quadrat der Länge dieses Vektors im euklidischen Raum
     */
    @Override
    public float len2() {
        return x * x + y * y;
    }

    /**
     * Limitiert die Länge dieses Vektors auf den angegebenen Wert.
     * Die Länge wird auf die nächste ganze Zahl in Richtung null gerundet.
     *
     * @param limit gewünschte maximale Länge
     * @return dieser Vektor zum Verketten
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
     * Limitiert die Länge dieses Vektors auf das Quadrat des angegebenen Werts.
     * Die Länge wird auf die nächste ganze Zahl in Richtung null gerundet.
     *
     * @param limit2 gewünschte maximale Länge im Quadrat
     * @return dieser Vektor zum Verketten
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
     * Ändert die Länge dieses Vektors im euklidischen Raum.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     * Macht nichts, wenn die aktuelle Länge null ist.
     *
     * @param len gewünschte Größe im euklidischen Raum
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 setLength(float len) {
        if ((x | y) == 0) return this;
        return scl(len / len());
    }

    /**
     * Ändert die Länge dieses Vektors im euklidischen Raum.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     * Macht nichts, wenn die aktuelle Länge null ist.
     *
     * @param len2 gewünschte Größe im Quadrat im euklidischen Raum
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 setLength2(float len2) {
        if ((x | y) == 0) return this;
        return scl((float) Math.sqrt(len2 / len2()));
    }

    /**
     * Ändert die Länge dieses Vektors im euklidischen Raum auf einen Wert zwischen min und max.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     * Macht nichts, wenn die aktuelle Länge null ist.
     *
     * @param min Minimale Länge
     * @param max Maximale Länge
     * @return dieser Vektor zur Verkettung
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
     * Kopiert die Attribute von einem anderen Vektor.
     *
     * @param v der Vektor, von dem kopiert werden soll
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 set(IntVector2 v) {
        x = v.x;
        y = v.y;
        return this;
    }

    /**
     * Setzt die Attribute dieses Vektors auf die angegebenen Werte.
     *
     * @param x x-Komponente
     * @param y y-Komponente
     * @return dieser Vektor zur Verkettung
     */
    public IntVector2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Subtrahiert einen anderen Vektor von diesem Vektor.
     *
     * @param v der andere Vektor
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 sub(IntVector2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    /**
     * Setzt die Länge dieses Vektors auf 1.
     * Das Ergebnis ist ein Einheitsvektor, der die Richtung dieses Vektors möglichst genau beibehält.
     * Priorisiert (-1,0) und (1,0), wenn der Winkel 45 Grad beträgt (unentschieden).
     * Macht nichts, wenn dieser Vektor null ist.
     *
     * @return dieser Vektor zur Verkettung
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
     * Addiert einen anderen Vektor zu diesem Vektor.
     *
     * @param v der andere Vektor
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 add(IntVector2 v) {
        x += v.x;
        y += v.y;
        return this;
    }

    /**
     * Berechnet das Skalarprodukt zwischen diesem Vektor und dem angegebenen Vektor.
     *
     * @param v der andere Vektor
     * @return das Skalarprodukt
     */
    @Override
    public float dot(IntVector2 v) {
        return x * v.x + y * v.y;
    }

    /**
     * Multipliziert diesen Vektor mit dem angegebenen Skalar.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     *
     * @param scalar Skalar
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 scl(float scalar) {
        x = Math.round(x * scalar);
        y = Math.round(y * scalar);
        return this;
    }

    /**
     * Skaliert diesen Vektor mit dem angegebenen Vektor.
     *
     * @param v der andere Vektor
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 scl(IntVector2 v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    /**
     * Gib die euklidische Distanz zwischen diesem Vektor und dem angegebenen Vektor zurück.
     *
     * @param v der andere Vektor
     * @return euklidische Distanz
     */
    @Override
    public float dst(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return (float) Math.sqrt(dx * dx + y_d * y_d);
    }

    /**
     * Gib das Quadrat der euklidischen Distanz zwischen diesem Vektor und dem angegebenen Vektor zurück.
     * Diese Methode ist schneller als {@link #dst(IntVector2)}, da sie keine Wurzel berechnet.
     * Sie kann für die Distanzvergleich von zwei Vektoren verwendet werden.
     *
     * @param v der andere Vektor
     * @return Quadrat der euklidischen Distanz
     */
    @Override
    public float dst2(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return dx * dx + y_d * y_d;
    }

    /**
     * Interpoliert zwischen diesem Vektor und dem angegebenen Vektor mit dem angegebenen Interpolationskoeffizienten,
     * welcher einen Werteberich von 0 bis 1 hat.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     *
     * @param target der Zielvektor
     * @param alpha  der Interpolationskoeffizient
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 lerp(IntVector2 target, float alpha) {
        final float invAlpha = 1.0f - alpha;
        this.x = Math.round((x * invAlpha) + (target.x * alpha));
        this.y = Math.round((y * invAlpha) + (target.y * alpha));
        return this;
    }

    /**
     * Interpoliert mithilfe der angegeben Interpolationsmethode zwischen diesem Vektor und dem angegebenen
     * Vektor mit dem angegebenen Interpolationskoeffizienten, welcher einen Werteberich von 0 bis 1 hat.
     * Er wird in beiden Dimensionen unabhängig voneinander auf die nächstliegenden ganzzahligen Werte gerundet.
     *
     * @param target        der Zielvektor
     * @param alpha         der Interpolationskoeffizient
     * @param interpolation das Interpolationsobjekt welches die Interpolationsmethode beschreibt
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 interpolate(IntVector2 target, float alpha, Interpolation interpolation) {
        return lerp(target, interpolation.apply(alpha));
    }

    /**
     * Setzt diesen Vektor auf den Einheitsvektor mit einer zufälligen Richtung.
     * Da dies ein ganzzahliger Vektor ist, existieren Einheitsvektoren nur entlang der Achsen.
     *
     * @return dieser Vektor zur Verkettung
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
     * @return True wenn dieser Vektor ein Einheitsvektor ist
     */
    @Override
    public boolean isUnit() {
        return len2() == 1;
    }

    /**
     * Berechnet, ob dieser Vektor ein Einheitsvektor mit Abweichung ist.
     *
     * @param margin erlaubte Abweichung
     * @return True wenn dieser Vektor ein Einheitsvektor ist
     */
    @Override
    public boolean isUnit(final float margin) {
        return Math.abs(len2() - 1f) < margin;
    }

    /**
     * @return True wenn dieser Vektor ein Nullvektor ist
     */
    @Override
    public boolean isZero() {
        return (x | y) == 0;
    }

    /**
     * Berechnet, ob dieser Vektor ein Nullvektor mit Abweichung ist.
     *
     * @param margin erlaubte Abweichung
     * @return True wenn dieser Vektor ein Nullvektor ist
     */
    @Override
    public boolean isZero(float margin) {
        return len2() < margin;
    }

    /**
     * @param other der andere Vektor
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt (unabhängig von der Richtung)
     */
    @Override
    public boolean isOnLine(IntVector2 other) {
        return MathUtils.isZero(x * other.y - y * other.x);
    }

    /**
     * @param other   der andere Vektor
     * @param epsilon erlaubte Abweichung
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt (unabhängig von der Richtung)
     */
    @Override
    public boolean isOnLine(IntVector2 other, float epsilon) {
        return MathUtils.isZero(x * other.y - y * other.x, epsilon);
    }

    /**
     * @param other   der andere Vektor
     * @param epsilon erlaubte Abweichung
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt und die gleiche Richtung hat
     */
    @Override
    public boolean isCollinear(IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) > 0f;
    }

    /**
     * @param other der andere Vektor
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt und die gleiche Richtung hat
     */
    @Override
    public boolean isCollinear(IntVector2 other) {
        return isOnLine(other) && dot(other) > 0f;
    }

    /**
     * @param other   der andere Vektor
     * @param epsilon erlaubte Abweichung
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt und die entgegengesetzte Richtung hat
     */
    @Override
    public boolean isCollinearOpposite(IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) < 0f;
    }

    /**
     * @param other der andere Vektor
     * @return True wenn dieser Vektor auf der selben Linie wie der andere Vektor liegt und die entgegengesetzte Richtung hat
     */
    @Override
    public boolean isCollinearOpposite(IntVector2 other) {
        return isOnLine(other) && dot(other) < 0f;
    }

    /**
     * @param vector der andere Vektor
     * @return True wenn dieser Vektor senkrecht zum anderen Vektor ist
     */
    @Override
    public boolean isPerpendicular(IntVector2 vector) {
        return MathUtils.isZero(dot(vector));
    }

    /**
     * @param vector  der andere Vektor
     * @param epsilon erlaubte Abweichung
     * @return True wenn dieser Vektor senkrecht zum anderen Vektor ist
     */
    @Override
    public boolean isPerpendicular(IntVector2 vector, float epsilon) {
        return MathUtils.isZero(dot(vector), epsilon);
    }

    /**
     * @param vector der andere Vektor
     * @return True wenn dieser Vektor die gleiche Richtung wie der andere Vektor hat
     */
    @Override
    public boolean hasSameDirection(IntVector2 vector) {
        return dot(vector) > 0;
    }

    /**
     * @param vector der andere Vektor
     * @return True wenn dieser Vektor die entgegengesetzte Richtung wie der andere Vektor hat
     */
    @Override
    public boolean hasOppositeDirection(IntVector2 vector) {
        return dot(vector) < 0;
    }

    /**
     * Berechnet den Hashcode dieses Vektors.
     *
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * Vergleicht diesen Vektor mit einem anderen Objekt.
     *
     * @param obj das andere Objekt
     * @return True wenn das andere Objekt ein Vektor ist und die gleichen Werte hat
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IntVector2 other = (IntVector2) obj;
        if (x != other.x) return false;
        return y == other.y;
    }

    /**
     * Vergleicht diesen Vektor mit einem anderen Vektor und einer erlaubten Abweichung.
     *
     * @param other   der andere Vektor
     * @param epsilon erlaubte Abweichung
     * @return True wenn die Vektoren die gleichen Werte haben (innerhalb der erlaubten Abweichung)
     */
    @Override
    public boolean epsilonEquals(IntVector2 other, float epsilon) {
        if (other == null) return false;
        if (Math.abs(other.x - x) > epsilon) return false;
        return !(Math.abs(other.y - y) > epsilon);
    }

    /**
     * Vergleicht diesen Vektor mit einem anderen Vektor und einer erlaubten Abweichung.
     *
     * @param x       x-Komponente des anderen Vektors
     * @param y       y-Komponente des anderen Vektors
     * @param epsilon erlaubte Abweichung
     * @return True wenn die Vektoren die gleichen Werte haben (innerhalb der erlaubten Abweichung)
     */
    public boolean epsilonEquals(float x, float y, float epsilon) {
        if (Math.abs(x - this.x) > epsilon) return false;
        return !(Math.abs(y - this.y) > epsilon);
    }

    /**
     * Vergleicht diesen Vektor mit einem anderen Vektor mithilfe von MathUtils.FLOAT_ROUNDING_ERROR als erlaubte Abweichung.
     *
     * @param other der andere Vektor
     * @return True wenn die Vektoren die gleichen Werte haben (innerhalb der erlaubten Abweichung)
     */
    public boolean epsilonEquals(final IntVector2 other) {
        return epsilonEquals(other, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    /**
     * Vergleicht diesen Vektor mit einem anderen Vektor mithilfe von MathUtils.FLOAT_ROUNDING_ERROR als erlaubte Abweichung.
     *
     * @param x x-Komponente des anderen Vektors
     * @param y y-Komponente des anderen Vektors
     * @return True wenn die Vektoren die gleichen Werte haben (innerhalb der erlaubten Abweichung)
     */
    public boolean epsilonEquals(float x, float y) {
        return epsilonEquals(x, y, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    /**
     * Erst wird der angegebene Vektor skaliert, dann zu diesem Vektor addiert.
     *
     * @param v      der Vektor, der addiert werden soll
     * @param scalar Skalar, mit dem der Vektor skaliert werden soll
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 mulAdd(IntVector2 v, float scalar) {
        this.x += v.x * scalar;
        this.y += v.y * scalar;
        return this;
    }

    /**
     * Erst wird der angegebene Vektor skaliert, dann zu diesem Vektor addiert.
     * Verändert nur diese Instanz.
     *
     * @param v      der Vektor, der addiert werden soll
     * @param mulVec Vektor, mit dem der Vektor skaliert werden soll
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 mulAdd(IntVector2 v, IntVector2 mulVec) {
        this.x += v.x * mulVec.x;
        this.y += v.y * mulVec.y;
        return this;
    }

    /**
     * Setzt die Attribute dieses Vektors auf 0.
     *
     * @return dieser Vektor zur Verkettung
     */
    @Override
    public IntVector2 setZero() {
        return set(0, 0);
    }

    /**
     * @return eine Kopie dieses Vektors als Float-Äquivalent {@link Vector2}
     */
    public Vector2 toFloat() {
        return new Vector2(x, y);
    }

    /**
     * Addiert die angegebenen Werte zu diesem Vektor.
     *
     * @param x x-Komponente
     * @param y y-Komponente
     * @return dieser Vektor zur Verkettung
     */
    public IntVector2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
