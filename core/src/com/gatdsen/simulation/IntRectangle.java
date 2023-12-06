package com.gatdsen.simulation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;

/**
 * Speichert die Grenzen eines Rechtecks, das sich in einem Array oder Gitter befindet.
 * Als Ergebnis wird die minimale Breite und Höhe dieses Rechtecks 1 sein, da selbst ein IntRectangle von den Punkten
 * (0,0) bis (0,0) mindestens das Pixel / die Zelle bei (0,0) belegt.
 */
public class IntRectangle implements Serializable, Shape2D {
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Erstellt ein neues IntRectangle mit den Abmessungen:
     * X = Y = 0 und Breite = Höhe = 1
     */
    public IntRectangle() {
        width = 1;
        height = 1;
    }

    /**
     * Erstellt ein neues IntRectangle mit den angegebenen Abmessungen.
     * Die Breite und Höhe müssen mindestens 1 sein, da das Rechteck
     * mindestens das Pixel / die Zelle an der Position (x, y) belegt.
     *
     * @param x      x-Position der unteren linken Ecke
     * @param y      y-Position der unteren linken Ecke
     * @param width  Größe des Rechtecks in x-Richtung
     * @param height Größe des Rechtecks in y-Richtung
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
     * Erstellt eine Kopie einer anderen Instanz.
     * Kopiert Position und Größe des angegebenen Rechtecks.
     *
     * @param rect Instanz, von der kopiert werden soll
     */
    public IntRectangle(IntRectangle rect) {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }

    /**
     * Erstellt eine Kopie dieser Instanz.
     * Kopiert Position und Größe des angegebenen Rechtecks.
     *
     * @return Kopie dieser Instanz
     */
    public IntRectangle copy() {
        return new IntRectangle(this.x, this.y, this.width, this.height);
    }

    /**
     * Ändert die Position und Abmessung dieser Instanz.
     * Die Breite und Höhe müssen mindestens 1 sein, da das Rechteck
     * mindestens das Pixel / die Zelle an der Position (x, y) belegt.
     *
     * @param x      x-Position der unteren linken Ecke
     * @param y      y-Position der unteren linken Ecke
     * @param width  Größe des Rechtecks in x-Richtung
     * @param height Größe des Rechtecks in y-Richtung
     * @return diese Instanz zum Verketten
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
     * @return x-Position der unteren linken Ecke
     */
    public int getX() {
        return x;
    }

    /**
     * Verändert die x-Komponente der Position dieses Rechtecks.
     *
     * @param x x-Position der unteren linken Ecke
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return y-Position der unteren linken Ecke
     */
    public int getY() {
        return y;
    }

    /**
     * Verändert die y-Komponente der Position dieses Rechtecks.
     *
     * @param y y-Position der unteren linken Ecke
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return größe des Rechtecks in x-Richtung
     */
    public int getWidth() {
        return width;
    }

    /**
     * Verändert die Breite dieses Rechtecks.
     * Die Breite muss mindestens 1 sein, da das Rechteck
     * mindestens das Pixel / die Zelle an der Position (x, y) belegt.
     *
     * @param width Größe des Rechtecks in x-Richtung
     */
    public void setWidth(int width) {
        if (width < 1) throw new IllegalArgumentException("An IntRectangle has to be at least of width 1");
        this.width = width;
    }

    /**
     * @return größe des Rechtecks in y-Richtung
     */
    public int getHeight() {
        return height;
    }

    /**
     * Verändert die Höhe dieses Rechtecks.
     * Die Höhe muss mindestens 1 sein, da das Rechteck
     * mindestens das Pixel / die Zelle an der Position (x, y) belegt.
     *
     * @param height Größe des Rechtecks in y-Richtung
     */
    public void setHeight(int height) {
        if (height < 0) throw new IllegalArgumentException("An IntRectangle has to be at least of height 1");
        this.height = height;
    }

    /**
     * Bewegt dieses Rechteck um den angegebenen Betrag und die angegebene Richtung.
     *
     * @param intVector2 Betrag und Richtung, um dieses Rechteck zu bewegen
     * @return diese Instanz zum Verketten
     */
    public IntRectangle add(IntVector2 intVector2) {
        x += intVector2.x;
        y += intVector2.y;
        return this;
    }

    /**
     * @return Ein Rechteck, das den gleichen Bereich in Gleitkommakoordinaten abdeckt
     */
    public Rectangle toFloat() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Testet, ob die angegebene Position innerhalb des Rechtecks liegt, das durch diese Instanz definiert ist.
     *
     * @param x x-Komponente der Position
     * @param y y-Komponente der Position
     * @return True, wenn der Punkt innerhalb dieses Rechtecks liegt
     */
    @Override
    public boolean contains(float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    /**
     * Testet, ob die angegebene Position innerhalb des Rechtecks liegt, das durch diese Instanz definiert ist.
     *
     * @param x x-Komponente der Position
     * @param y y-Komponente der Position
     * @return True, wenn der Punkt innerhalb dieses Rechtecks liegt
     */
    public boolean contains(int x, int y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    /**
     * Testet, ob die angegebene Position innerhalb des Rechtecks liegt, das durch diese Instanz definiert ist.
     *
     * @param point die zu testende Position als 2D-Gleitkommavektor
     * @return True, wenn der Punkt innerhalb dieses Rechtecks liegt
     */
    @Override
    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * Testet, ob die angegebene Position innerhalb des Rechtecks liegt, das durch diese Instanz definiert ist.
     *
     * @param point die zu testende Position als 2D-Ganzzahlvektor
     * @return True, wenn der Punkt innerhalb dieses Rechtecks liegt
     */
    public boolean contains(IntVector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * Gibt den minimalen Punkt von einer Hitbox zurück, um es AABB-kompatibel zu machen
     *
     * @return minimaler Punkt von der Hitbox
     */
    public IntVector2 min() {
        return new IntVector2(x, y);
    }

    /**
     * Gibt den maximalen Punkt von einer Hitbox zurück, um es AABB-kompatibel zu machen
     *
     * @return maximaler Punkt von der Hitbox
     */
    public IntVector2 max() {
        return new IntVector2(x + width, y + height);
    }

    /**
     * Testet, ob der angegebene Strahl das Rechteck schneidet, das durch diese Instanz definiert ist.
     *
     * @param s Startpunkt des Strahls
     * @param e Endpunkt des Strahls
     * @return True, wenn der Strahl das Rechteck schneidet
     */
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
}
