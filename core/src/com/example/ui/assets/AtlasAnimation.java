package com.example.ui.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AtlasAnimation extends Animation<TextureRegion> {

    public AtlasAnimation(float frameDuration, Array<? extends TextureAtlas.AtlasRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);

        int lastFrameIndex = keyFrames.get(keyFrames.size - 1).index;
        if (lastFrameIndex == -1) {
            super.setKeyFrames(keyFrames.toArray());
            return;
        }
        TextureAtlas.AtlasRegion[] filledKeyframes = new TextureAtlas.AtlasRegion[lastFrameIndex + 1];

        int prevIndex = -1;
        for (TextureAtlas.AtlasRegion frame : keyFrames) {
            int curIndex = frame.index;
            for (int i = prevIndex + 1; i <= curIndex; i++) {
                filledKeyframes[i] = frame;
            }
            prevIndex = curIndex;
        }
        super.setKeyFrames(filledKeyframes);
    }
}
