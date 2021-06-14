package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import com.hurix.customui.datamodel.HighlightVO;

import java.util.ArrayList;

public interface AddUGCAcceptRejectListener {
    void onUGCAcceptRejectData(boolean value, int position);

    void onAcceptRejectViewClicked(boolean v, HighlightVO vo,  ArrayList<HighlightVO> mUGClist);
}
