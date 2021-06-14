package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by amitraj.sharma on 2/9/2018.
 */
public interface CustomThumbnailsListner {
    void ThumbnailpageNavigation(long pageid);

    void onThumbnailViewCreated(View view);

    void onSeekbarViewCreated(SeekBar seekBarHint);

    void onGotoClick(String Pageno);

    void NavigatePreviousPage();

    void NavigateNextPage();

    void onPageHistoryButtonsCreated(Button mPageHistNext, Button mPageHistPrevious);

    void navigate(String baseUrl);

}