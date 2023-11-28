package com.gatdsen.ui.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class IndexedAnimation<T> extends Animation<T> {

    public IndexedAnimation(float frameDuration, Array<T> keyFrames, int[] indices, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);

        if (keyFrames.size != indices.length) throw new RuntimeException("Each keyframe has to be indexed");

        int lastFrameIndex = indices[keyFrames.size - 1];
        if (lastFrameIndex == -1) {
            super.setKeyFrames(keyFrames.toArray());
            return;
        }
        Array<T> filledKeyframes = new Array<>(lastFrameIndex + 1);
        filledKeyframes.size=lastFrameIndex + 1;

        int prevIndex = -1;
        for (int i=0;i<keyFrames.size;i++) {
            T frame = keyFrames.get(i);
            int curIndex = indices[i];
            for (int j = prevIndex + 1; j <= curIndex; j++) {
                filledKeyframes.set(j, frame);
            }
            prevIndex = curIndex;
        }
        super.setKeyFrames(filledKeyframes.items);
    }
}
