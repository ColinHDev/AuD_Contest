package com.example.animation;

import com.badlogic.gdx.math.Vector2;
import com.example.simulation.Path;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * CharacterPath is a special type of path that implements the difference of origin between simulation and animation
 * <p>
 * e.g. The origin in simulation might be the bottom left while the origin in animation might be the bottom center.
 */
public class CharacterPath implements Path {

    private final Path path;

    private static final Vector2 offset = new Vector2(0, 0);//GameCharacter.getSize().scl(0.5f);

    public CharacterPath(Path path) {
        this.path = path;
    }

    @Override
    public Vector2 getPos(float t) {
        return path.getPos(t).add(offset);
    }

    @Override
    public Vector2 getDir(float t) {
        if (t >= path.getDuration())
            return path.getDir(path.getDuration()).x < 0 ? new Vector2(-1, 0) : new Vector2(1, 0);
        return path.getDir(t);
    }

    @Override
    public float getDuration() {
        return path.getDuration();
    }

    @Override
    public void setDuration(float duration) {
        throw new NotImplementedException();
    }


    @Override
    public void setDuration(Vector2 endPosition) {
        throw new NotImplementedException();
    }
}
