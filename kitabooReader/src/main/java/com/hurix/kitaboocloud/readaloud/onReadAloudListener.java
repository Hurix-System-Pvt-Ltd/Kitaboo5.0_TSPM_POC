package com.hurix.kitaboocloud.readaloud;

/**
 * Created by Amit Raj on 03-09-2019.
 */
public interface onReadAloudListener {
    void setonReadAloudListener(boolean readAloudPlaying);
    void notifyReadAloudAutoPlayStartStop(boolean isAutoPlay);
}
