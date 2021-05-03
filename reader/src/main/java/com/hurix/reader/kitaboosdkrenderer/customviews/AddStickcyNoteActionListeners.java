package com.hurix.reader.kitaboosdkrenderer.customviews;

import com.hurix.customui.datamodel.HighlightVO;

public interface AddStickcyNoteActionListeners {
    void onSaveClicked(HighlightVO vo);

    void onShareClicked(HighlightVO vo, String hexColor);

    void onDeleteClicked(HighlightVO vo);

    void onStickyNoteDismissed();

    void onCommentPostClick(String comments, HighlightVO mHighlightObj);

}
