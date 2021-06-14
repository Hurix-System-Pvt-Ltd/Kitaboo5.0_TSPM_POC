package com.hurix.kitaboocloud.kitaboosdkrenderer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.bookreader.interfaces.AVPlayerlistner;
import com.hurix.bookreader.views.audiovideo.Content;
import com.hurix.bookreader.views.audiovideo.CustomVideoMediaPlayer;
import com.hurix.bookreader.views.audiovideo.MediaMarkupAdapter;
import com.hurix.bookreader.views.audiovideo.MediaTocParser;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.kitaboocloud.R;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by tejashri.magar on 27/12/2019.
 */

/**
 * This class will be called when the content is of video type.
 */
public class VideoMediaPlayer extends Activity implements AVPlayerlistner, View.OnClickListener/*, OnVideoItemClickListener*/ {

    private RecyclerView rv_video_items;
    private MediaMarkupAdapter mediaMarkupAdapter;
    private CustomVideoMediaPlayer mediaPlayer;
    private RelativeLayout mediaPlayerMain;
    private MediaTocParser mediaTocParser;
    private int mScreenHeight, mScreenWidth, mCurrentVideoDuration, mTotalVideoDuration;
    private Boolean mIsOnline = false, iserror;
    private Boolean mIsInline = false;
    private String isbnNo;
    private long bookId;
    private String mUrl;
    private Boolean mIsPlaying = false;
    private Boolean mIsBookShelfLaunch = false;
    private int mResizedWidth, mResizedHeight;
    private static final long ANIM_DURATION = 1000;
    private Typeface typeFace;
    private TextView bookTitleInMedia;
    private String strBookTitle;
    private boolean isTOCJsonAvailable;
    private boolean disableFullScrnIfTocNotPresentForInsight;
    private Locale mLocale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(VideoMediaPlayer.this);

        if (getResources().getBoolean(R.bool.is_multi_lang_support)) {
            setLocale(com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceStringValue(
                    VideoMediaPlayer.this, com.hurix.kitaboo.constants.Constants.SHELF_PREFS_NAME, com.hurix.kitaboo.constants.Constants.DEFAULT_APP_LANGUAGE, ""));
        }
        setContentView(R.layout.video_markup_player);
        Bundle data = this.getIntent().getExtras();
        String mediaThumbnailPath = data.getString("mediathumbnail");
        mediaTocParser = (MediaTocParser) data.getSerializable("videoTocDataContent");
        mediaMarkupAdapter = new MediaMarkupAdapter(VideoMediaPlayer.this, mediaThumbnailPath, mediaTocParser,false);
        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(true);
        mIsInline = data.getBoolean("isInline");
        mTotalVideoDuration = data.getInt("totalduration");
        iserror = data.getBoolean("error");
        isbnNo = data.getString("isbnNo");
        bookId = data.getLong("bookId");
        mUrl = data.getString("mediapath");
        isTOCJsonAvailable = data.getBoolean("isTOCJsonAvailable");
        if(mediaTocParser != null)
            strBookTitle = mediaTocParser.getTitle();
        disableFullScrnIfTocNotPresentForInsight = !isTOCJsonAvailable && getResources().getBoolean(R.bool.show_insight_bookshelf);
        mIsPlaying = true;
        mIsBookShelfLaunch = data.getBoolean("isBookshelfLaunch");
        initView();
        playDefaultVideo(mUrl, mediaPlayer);
    }

    public void setLocale(String localeName) {
        mLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = mLocale;
        res.updateConfiguration(conf, dm);
        //onConfigurationChanged(conf);
        com.hurix.kitaboo.constants.utils.Utils.insertSharedPrefernceStringValues(this,
                com.hurix.kitaboo.constants.Constants.SHELF_PREFS_NAME, com.hurix.kitaboo.constants.Constants.DEFAULT_APP_LANGUAGE, localeName);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("param", mediaPlayer.getVideoPlayer().getCurrentPosition());
        outState.putBoolean("isPlaying", mediaPlayer.getVideoPlayer().isPlaying());
        outState.putBoolean("isSoundMute", CustomVideoMediaPlayer.isSoundMute);

        /*if(mediaPlayer != null)
        {
            Utils.insertSharedPrefernceIntValues(this,Constants.SHELF_PREFS_NAME, bookId+"_position", mediaPlayer.getVideoPlayer().getCurrentPosition());
            Utils.insertSharedPreferenceBooleanValue(this, bookId+"_isPlaying",mediaPlayer.getVideoPlayer().isPlaying());
            Utils.insertSharedPreferenceBooleanValue(this, bookId+"_isSoundMute",  CustomVideoMediaPlayer.isSoundMute);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(mediaPlayer != null)
        {
            final long lastPosition = Utils.getSharedPreferenceIntValue(this,Constants.SHELF_PREFS_NAME, bookId+"_position",0);
            final boolean isPlaying = Utils.getSharedPreferenceBooleanValue(this, bookId+"_isPlaying",false);
            final boolean isSoundMute = Utils.getSharedPreferenceBooleanValue(this, bookId+"_isSoundMute",false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.resumeVideo(lastPosition,isPlaying,isSoundMute);
                }
            }, 1000);
        }*/
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getLong("param") != 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.resumeVideo(savedInstanceState.getLong("param"), savedInstanceState.getBoolean("isPlaying"),
                            savedInstanceState.getBoolean("isSoundMute"));
                }
            }, 1000);
        }
    }

    private void playDefaultVideo(String mUrl, CustomVideoMediaPlayer mMediaPlayer) {
        mMediaPlayer.setVideoPath(mUrl, mIsOnline, mIsInline, mCurrentVideoDuration, mIsPlaying, mIsBookShelfLaunch);
        mMediaPlayer.setCallback(this);
        mMediaPlayer.setMediaLayout(disableFullScrnIfTocNotPresentForInsight);

    }

    private RelativeLayout initializePlayer(String strBookTitle) {
        if(mediaTocParser != null)
            mediaPlayer = new CustomVideoMediaPlayer(this, mIsInline, mTotalVideoDuration, bookId, isbnNo, mediaTocParser.getContent(), rv_video_items, strBookTitle,getResources().getBoolean(R.bool.show_insight_bookshelf));
        else
            mediaPlayer = new CustomVideoMediaPlayer(this, mIsInline, mTotalVideoDuration, bookId, isbnNo, new ArrayList<Content>(), rv_video_items, strBookTitle,getResources().getBoolean(R.bool.show_insight_bookshelf));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mediaPlayer.setLayoutParams(params);
        if(disableFullScrnIfTocNotPresentForInsight)
        {
            enterFullScreen(true);
            if(mediaPlayer != null)
                mediaPlayer.makeFullScreenVisible(false);
        }
        return mediaPlayer;

    }

    public void setLayoutParamsToVideoPlayer() {

        ResizeAnimation animation = new ResizeAnimation(mediaPlayer.getContainer(),
                mediaPlayer.getVideoPlayer(), mediaPlayerMain.getWidth(), mediaPlayerMain.getHeight());
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateInterpolator());
        mediaPlayer.getContainer().startAnimation(animation);

    }

    @Override
    public void onCloseAVPlayer() {
        this.finish();
    }

    @Override
    public void pausePlayingVideo() {
        String s = "";
    }

    @Override
    public void startPlayingVideo() {
        String s = "";
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
    }

    public class ResizeAnimation extends Animation {
        int startWidth = 0;
        int targetWidth = 0;
        int startHeight = 0;
        int targetHeight = 0;
        View containerView, videoView;


        public ResizeAnimation(View containerView, View videoView, int targetWidth, int targetHeight) {
            this.containerView = containerView;
            this.videoView = videoView;
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            startWidth = containerView.getWidth();
            startHeight = containerView.getHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            rv_video_items.setScrollContainer(true);
            rv_video_items.requestLayout();
            mediaPlayerMain.requestLayout();
            mediaPlayer.setLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private void initView() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(VideoMediaPlayer.this, fontfile);
        } else {
            typeFace = Typefaces.get(VideoMediaPlayer.this, "kitabooread.ttf");
        }

        mediaPlayerMain = (RelativeLayout) findViewById(R.id.ll_video_player);
        rv_video_items = (RecyclerView) findViewById(R.id.rv_video_items);
        bookTitleInMedia = (TextView) findViewById(R.id.txt_book_title_in_media);
        bookTitleInMedia.setText(strBookTitle);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_video_items.setLayoutManager(mLayoutManager);
        rv_video_items.setItemAnimator(new DefaultItemAnimator());
        rv_video_items.setAdapter(mediaMarkupAdapter);

        mediaPlayerMain.addView(initializePlayer(this.strBookTitle));
        setLayoutParamsToVideoPlayer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(getResources().getBoolean(R.bool.show_insight_bookshelf))
            enterFullScreen(true);
    }

    @Override
    public void enterFullScreen(boolean b) {
        final LinearLayout.LayoutParams params, recyclerParamas;
        boolean animationRequired;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (((LinearLayout.LayoutParams) mediaPlayerMain.getLayoutParams()).weight < 2) {
                bookTitleInMedia.setVisibility(View.GONE);
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);
                animationRequired = true;
            } else {
                bookTitleInMedia.setVisibility(View.VISIBLE);
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.8f);
                recyclerParamas.setMargins(10, 10, 0, 15);
                animationRequired = false;
                if(mediaPlayer != null)
                    mediaPlayer.makeFullScreenVisible(true);
            }
        } else {
            //if(((LinearLayout.LayoutParams)mediaPlayerMain.getLayoutParams()).weight<2){
            bookTitleInMedia.setVisibility(View.GONE);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f);
            recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);
            animationRequired = true;
            /*}else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1f);
                recyclerParamas.setMargins(10,10,0,15);
                animationRequired=false;
            }*/
        }
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator transAnimation = ObjectAnimator.ofFloat(this, "translationY", this.mediaPlayerMain.getY(), 0);
        transAnimation.setInterpolator(new DecelerateInterpolator());
        transAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Animation animationm = AnimationUtils.loadAnimation(getBaseContext(), R.anim.video_zoom_out);
                animationm.setDuration(ANIM_DURATION);
                rv_video_items.setLayoutParams(recyclerParamas);
                mediaPlayerMain.setLayoutParams(params);
                mediaPlayerMain.setAnimation(animationm);
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
        if (animationRequired) {
            transAnimation.start();
        } else {
            mediaPlayerMain.setLayoutParams(params);
            rv_video_items.setLayoutParams(recyclerParamas);
        }
    }

    private Display dislay() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display;
    }

    public void resizeVideoPlayer() {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        mResizedWidth = videoWidth;
        mResizedHeight = videoHeight;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        dislay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        mScreenWidth = displaymetrics.widthPixels;
        if (videoWidth <= 0) {
            videoWidth = mScreenWidth;
        }
        if (videoHeight <= 0) {
            videoHeight = mScreenHeight;
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mResizedWidth = mScreenWidth;
            mResizedHeight = (int) ((videoHeight * mResizedWidth * 1f) / videoWidth * 1f);
        } else {
            mResizedHeight = mScreenHeight;
            mResizedWidth = (int) ((mResizedHeight * videoWidth * 1f) / videoHeight * 1f);
        }

        if (mediaPlayer.isInfullScreen()) {
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                mResizedWidth = (int) (mResizedWidth * 1f);
                mResizedHeight = (int) (mResizedHeight * 1f);
            }
            mediaPlayer.setFullScreenIcon(true);
        } else {
            if (videoWidth < (int) (mScreenWidth * 1f) && videoHeight < mScreenHeight * 1) {
                mResizedWidth = videoWidth;
                mResizedHeight = videoHeight;
            } else {
                mResizedWidth = (int) (mResizedWidth * 1f);
                mResizedHeight = (int) (mResizedHeight * 1f);
            }
            mediaPlayer.setFullScreenIcon(false);
        }
        //if original resolution of video is very small then
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mResizedWidth < (int) (mScreenWidth * 0.75)) {
                mResizedWidth = (int) (mScreenWidth * 0.75);
                mResizedHeight = (int) ((videoHeight * mResizedWidth * 1f) / videoWidth * 1f);
            }
        } else {
            if (mResizedHeight < (int) (mScreenHeight * 0.75)) {
                mResizedHeight = (int) (mScreenHeight * 0.75);
                mResizedWidth = (int) ((mResizedHeight * videoWidth * 1f) / videoHeight * 1f);
            }
        }
    }

    @Override
    public void onBackPressed() {
       // BaseActivity.isReaderOpen = false;
        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false);
        setResult(RESULT_OK);
        SharedPreferences preferences = getSharedPreferences("Orientation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  BaseActivity.isReaderOpen = false;
        com.hurix.kitaboocloud.notifier.GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false);
    }
}



/*
interface OnMediaListItemClickListener {
    void changeProgressOnMediaItemClick(Content item);
}*/
