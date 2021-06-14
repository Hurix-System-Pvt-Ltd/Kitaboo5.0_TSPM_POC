package com.hurix.kitaboo.camera.interfaces;

import java.util.Map;

/**
 * Created by Ravi Ranjan on 5/12/2015.
 */
public interface ProfilePicStatus {
    void profilePicUploadStatus(boolean status, Map.Entry<String, Integer> entry);
}
