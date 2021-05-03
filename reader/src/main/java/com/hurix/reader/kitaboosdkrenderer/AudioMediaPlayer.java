
package com.hurix.reader.kitaboosdkrenderer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.hurix.bookreader.views.audiovideo.CustomAudioMediaPlayer;
import com.hurix.bookreader.views.audiovideo.MediaMarkupAdapter;
import com.hurix.bookreader.views.audiovideo.MediaTocParser;
import com.hurix.commons.iconify.Typefaces;




import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;


/**
 * Created by amitraj.sharma on 03/01/2019.
 */


/**
 * This class will be called when the content is of Audio type.
 */
public class AudioMediaPlayer extends Activity implements AVPlayerlistner, View.OnClickListener {

    private RecyclerView rv_video_items;
    private MediaMarkupAdapter mediaMarkupAdapter;
    private CustomAudioMediaPlayer mediaPlayer;
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
    private TextView mBackToBookShelfIcon;
    private Typeface typeFace;
    private TextView mBookTitle, bookTitleInMedia;
    private String strBookTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_markup_player);
        Bundle data = this.getIntent().getExtras();
        String mediaThumbnailPath = data.getString("mediathumbnail");
        mediaTocParser = (MediaTocParser) data.getSerializable("videoTocDataContent");
        mediaMarkupAdapter = new MediaMarkupAdapter(AudioMediaPlayer.this, mediaThumbnailPath, mediaTocParser,false);
       GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(true);
        mIsInline = data.getBoolean("isInline");
        mTotalVideoDuration = data.getInt("totalduration");
        iserror = data.getBoolean("error");
        isbnNo = data.getString("isbnNo");
        bookId = data.getLong("bookId");
        mUrl = data.getString("mediapath");
        strBookTitle = mediaTocParser.getTitle();
        mIsPlaying = true;
        mIsBookShelfLaunch = data.getBoolean("isBookshelfLaunch");
        initView();
        playDefaultVideo(mUrl, mediaPlayer);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (mediaPlayer != null && mediaPlayer.getMediaPlayer() != null)
            mediaPlayer.getMediaPlayer().stop();

        if (viewId == R.id.buttonBookshelf) {
            GlobalDataManager.getInstance().setAnyPopupVisible(false);
          //  com.hurix.reader.kitaboosdkrenderer.abstracts.BaseActivity.isReaderOpen = false;
            Intent intent = new Intent();
            intent.putExtra("Trackingdata", "");
            intent.putExtra("bookID", bookId);
            this.setResult(-1, intent);
            this.finish();
        }
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("param", mediaPlayer.getMediaPlayer().getCurrentPosition());
        if(mediaPlayer != null && mediaPlayer.getMediaPlayer() != null)
            mediaPlayer.getMediaPlayer().stop();
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.getLong("param")!=0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.resumeVideo( savedInstanceState.getLong("param"));
                }
            },2000);
        }
    }*/

    @Override
    protected void onPause() {
        if (mediaPlayer != null && mediaPlayer.getMediaPlayer() != null && mediaPlayer.getMediaPlayer().isPlaying())
            mediaPlayer.pauseAudio();

        super.onPause();
    }

    private void playDefaultVideo(String mUrl, CustomAudioMediaPlayer mMediaPlayer) {
        mMediaPlayer.setAudioPath(mUrl, mIsOnline, mIsInline, mCurrentVideoDuration, mIsPlaying, mIsBookShelfLaunch);
        mMediaPlayer.setCallback(this);

    }

    private RelativeLayout initializePlayer() {
        mediaPlayer = new CustomAudioMediaPlayer(this, mIsInline, mTotalVideoDuration, bookId, isbnNo, mediaTocParser.getContent(), rv_video_items);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mediaPlayer.setLayoutParams(params);
        return mediaPlayer;

    }

    public void setLayoutParamsToVideoPlayer() {

        AudioMediaPlayer.ResizeAnimation animation = new AudioMediaPlayer.ResizeAnimation(mediaPlayer.getContainer(),
                mediaPlayerMain.getWidth(), mediaPlayerMain.getHeight());
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateInterpolator());
        mediaPlayer.getContainer().startAnimation(animation);

    }

    @Override
    public void onCloseAVPlayer() {

    }

    @Override
    public void pausePlayingVideo() {

    }

    @Override
    public void startPlayingVideo() {

    }

    public class ResizeAnimation extends Animation {
        int startWidth = 0;
        int targetWidth = 0;
        int startHeight = 0;
        int targetHeight = 0;
        View containerView;


        public ResizeAnimation(View containerView, int targetWidth, int targetHeight) {
            this.containerView = containerView;
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
            typeFace = Typefaces.get(AudioMediaPlayer.this, fontfile);
        } else {
            typeFace = Typefaces.get(AudioMediaPlayer.this, "kitabooread.ttf");
        }

        mBackToBookShelfIcon = (TextView) this.findViewById(R.id.buttonBookshelf);
        mBackToBookShelfIcon.setTypeface(typeFace);
        mBackToBookShelfIcon.setText("a");
        mBackToBookShelfIcon.setTextColor(this.getResources().getColor(R.color.kitaboo_main_color));
        mBackToBookShelfIcon.setTextSize(25);
        mBackToBookShelfIcon.setOnClickListener(this);
        mBookTitle = (TextView) this.findViewById(R.id.title);
        mBookTitle.setTextColor(this.getResources().getColor(R.color.kitaboo_main_color));
        mBookTitle.setText(this.strBookTitle);

        bookTitleInMedia = (TextView) findViewById(R.id.txt_book_title_in_media);
        bookTitleInMedia.setText(this.strBookTitle);

        mediaPlayerMain = (RelativeLayout) findViewById(R.id.ll_audio_player);
        rv_video_items = (RecyclerView) findViewById(R.id.rv_audio_items);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_video_items.setLayoutManager(mLayoutManager);
        rv_video_items.setItemAnimator(new DefaultItemAnimator());
        rv_video_items.setAdapter(mediaMarkupAdapter);

        mediaPlayerMain.addView(initializePlayer());
        setLayoutParamsToVideoPlayer();
    }

    @Override
    public void enterFullScreen(boolean b) {
        final LinearLayout.LayoutParams params, recyclerParamas;
        boolean animationRequired;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (((LinearLayout.LayoutParams) mediaPlayerMain.getLayoutParams()).weight < 2) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);
                animationRequired = true;
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.8f);
                recyclerParamas.setMargins(10, 10, 0, 15);
                animationRequired = false;
            }
        } else {
            if (((LinearLayout.LayoutParams) mediaPlayerMain.getLayoutParams()).weight < 2) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);
                animationRequired = true;
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
                recyclerParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
                recyclerParamas.setMargins(10, 10, 0, 15);
                animationRequired = false;
            }
        }
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator transAnimation = ObjectAnimator.ofFloat(this, "translationY", this.mediaPlayerMain.getY(), 0);
        transAnimation.setInterpolator(new DecelerateInterpolator());
        transAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Animation animationm = AnimationUtils.loadAnimation(getBaseContext(), R.anim.video_zoom_out);
                animationm.setDuration(ANIM_DURATION);
                mediaPlayerMain.setAnimation(animationm);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mediaPlayerMain.setLayoutParams(params);
                rv_video_items.setLayoutParams(recyclerParamas);
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
        int videoWidth = mediaPlayer.getAudioWidth();
        int videoHeight = mediaPlayer.getAudioHeight();
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
        if (mediaPlayer != null && mediaPlayer.getMediaPlayer() != null)
            mediaPlayer.getMediaPlayer().stop();
        setResult(RESULT_OK);
      //  BaseActivity.isReaderOpen = false;
       GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false);
        SharedPreferences preferences = getSharedPreferences("Orientation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onBackPressed();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
     //   BaseActivity.isReaderOpen = false;
       GlobalDataManager.getInstance().setCurrentSelectedBookLaunch(false);
    }
}



/*interface OnMediaListItemClickListener {
    void changeProgressOnMediaItemClick(Content item);
}*/
