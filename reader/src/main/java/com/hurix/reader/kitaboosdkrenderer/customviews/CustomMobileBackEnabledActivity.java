
package com.hurix.reader.kitaboosdkrenderer.customviews;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hurix.commons.Constants.Constants;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.thumbnails.ThumbnailVO;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.ReaderThemeController;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;




/**
 * Created by amitraj.sharma on 2/7/2018.
 */

public class CustomMobileBackEnabledActivity extends FragmentActivity implements CustomThumbnailsListner {
    private ArrayList<ThumbnailVO> mThumbnailColl = null;
    private CustomMobileThumbFragment mobileThumbnailFragment = null;
    private RelativeLayout mPageThumbnailcontainer;
    private TextView backbutton,thumbnail_title_bar;
    private Typeface customTypeFace;
    private View thumbnaillayout = null;
    private int currentviewpagerindex = 0;
    private ReaderThemeSettingVo readerThemeSettingVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.grey_transparency));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.custom_generic_back_enabled_layout);
        mPageThumbnailcontainer = (RelativeLayout) findViewById(R.id.mobilethumbnailview);
        thumbnail_title_bar = (TextView) findViewById(R.id.thumbnail_title_bar);
        thumbnail_title_bar.setText(getResources().getString(R.string.thumbnails));
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            customTypeFace = Typefaces.get(this, fontfile);
        } else {
            customTypeFace = Typefaces.get(this, "kitabooread.ttf");
        }

        Intent intent = getIntent();
        currentviewpagerindex = intent.getIntExtra("currentviewpagerindex", 0);
       // readerThemeSettingVo = (ReaderThemeSettingVo) getIntent().getSerializableExtra("themedata");
        readerThemeSettingVo = ReaderThemeController.getInstance(getApplicationContext()).getReaderThemeUserSettingVo();
        mThumbnailColl = (ArrayList<ThumbnailVO>) intent.getSerializableExtra("thumbnaildata");
        initActivityForToolType();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // overridePendingTransition(R.anim.in_from_left ,0);
    }

    private void initActivityForToolType() {
        mobileThumbnailFragment = new CustomMobileThumbFragment("thumbnail", mThumbnailColl, currentviewpagerindex, readerThemeSettingVo);
        mobileThumbnailFragment.setThumbListener(this);
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mPageThumbnailcontainer.setVisibility(View.VISIBLE);
            fragmentTransaction.add(R.id.mobilethumbnailview, mobileThumbnailFragment, "thumbnail");
            fragmentTransaction.commit();
            ObjectAnimator transAnimation = ObjectAnimator.ofFloat(mPageThumbnailcontainer, "translationY",
                    mPageThumbnailcontainer.getTranslationY(), 0);
            //transAnimation.setInterpolator(new DecelerateInterpolator());
            transAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            transAnimation.start();
        } catch (Exception e) {
            if (Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ThumbnailpageNavigation(long pageid) {
        /*
        Works on page index
         */
        Intent intent = new Intent();
        intent.putExtra("thumnbnailpagenavigationresult", (int) pageid);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onThumbnailViewCreated(View view) {
        thumbnaillayout = view;
    }

    @Override
    public void onSeekbarViewCreated(SeekBar seekBarHint) {

    }

    @Override
    public void onGotoClick(String Pageno) {

    }

    @Override
    public void NavigatePreviousPage() {
        Intent intent = new Intent();
        intent.putExtra("result", "Previous");
        intent.putExtra("value", 10);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void NavigateNextPage() {
        Intent intent = new Intent();
        intent.putExtra("result", "Next");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPageHistoryButtonsCreated(Button mPageHistNext, Button mPageHistPrevious) {
        mPageHistNext.setEnabled(true);
        mPageHistPrevious.setEnabled(true);
        mPageHistNext.setAlpha(1f);
        mPageHistPrevious.setAlpha(1f);
        if (SDKManager.getInstance().getHistoryStack().size() < 1) {
            mPageHistNext.setAlpha(0.5f);
            mPageHistPrevious.setAlpha(0.5f);
            mPageHistNext.setEnabled(false);
            mPageHistPrevious.setEnabled(false);
        } else {
            if (SDKManager.getInstance().getHistoryStack().size() > 0 &&
                    (SDKManager.getInstance().getHistoryStackPosition()) <= 0) {
                /*
                Make Next button Enabled
                 */
                mPageHistPrevious.setAlpha(0.5f);
                mPageHistPrevious.setEnabled(false);
            }
            if (SDKManager.getInstance().getHistoryStackPosition() + 1 ==
                    SDKManager.getInstance().getHistoryStack().size()) {
                  /*
                Make Previous button Enabled
                 */
                mPageHistNext.setAlpha(0.5f);
                mPageHistNext.setEnabled(false);
            }
        }
    }

    @Override
    public void navigate(String baseUrl) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}