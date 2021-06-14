package com.hurix.kitaboocloud.helpscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.hurix.bookreader.views.link.CirclePageIndicator;
import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.sdkDatamodel.SDKManager;



import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboocloud.PlayerController;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.common.HelpVo;

import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomKitabooActionbar;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomKitabooTopActionbar;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomPlayerUIConstants;
import com.hurix.kitaboocloud.model.JWTokenResponseVO;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hurix.kitaboo.constants.utils.ThemeColorConstant.BOOKSHELF;

/**
 * Created by Amit Raj on 08-08-2019.
 */
public class HelpScreenActivity extends Activity {
    private int bottomActionbarYCords = 0;
    private ArrayList<HelpVo> mViewColl;
    private ArrayList<HelpVo> mNewViewColl;
    private Typeface typeFace;
    private RelativeLayout mParetnView;
    //private ImageView imgView;
    private Bitmap bitmap;
    private Canvas canvas;
    private final int lineHeight = 250;
    int i = 0;
    CirclePageIndicator mIndicator;
    HelpScreenAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    private static final String HELPSCREEN_TYPE = "HELPSCREEN_TYPE";
    private static final String READER = "Reader";
    private static final String REVIEW = "Review";
    private String reviewmode = "";
    private CustomKitabooActionbar /*topActionbar,*/ bottomActionbar;
    private CustomKitabooTopActionbar topActionbar;

    private final ArrayList<HelpVo> _collOfHelp_1 = new ArrayList<>();
    private final ArrayList<HelpVo> _collOfHelp_2 = new ArrayList<>();
    private final float actionbartextSize = 22;
    private EBookType mReaderType;
    private boolean isMobile;
    private String accountType;
    private JWTokenResponseVO jwTokenDetails;
    private final int topActionbarYCords = 0;
    private HashMap<Integer, ArrayList<HelpVo>> hashMap;
    private final ArrayList<HelpVo> _collOfHelp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(HelpScreenActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//int flag, int mask

        FullScreencall();

        if (getIntent().getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(REVIEW) && !Utility.isDeviceTypeMobile(this)) {
            setContentView(R.layout.activity_help_screen_review);
        } else {
            setContentView(R.layout.activity_help_screen);
        }
    }

    public void setViewPagerAdapter() {
        hashMap = new HashMap<Integer, ArrayList<HelpVo>>();
        if (_collOfHelp_1 != null && _collOfHelp_1.size() > 0) {
            hashMap.put(0, _collOfHelp_1);
        }
        if (_collOfHelp_2 != null && _collOfHelp_2.size() > 0) {
            hashMap.put(1, _collOfHelp_2);
        } else {
            hashMap = (HashMap<Integer, ArrayList<HelpVo>>) getIntent().getExtras().get("coll");
        }

        if (getIntent().getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(READER)) {
            mViewPagerAdapter = new HelpScreenAdapter(this, hashMap, true, false);
            com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, true);
            reviewmode = getIntent().getStringExtra(HELPSCREEN_TYPE);
        } else if (getIntent().getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(REVIEW)) {
            reviewmode = getIntent().getStringExtra(HELPSCREEN_TYPE);
            mViewPagerAdapter = new HelpScreenAdapter(this, hashMap, false, true);
        } else
            mViewPagerAdapter = new HelpScreenAdapter(this, hashMap, false, false);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        if (hashMap.size() > 1) {
            mIndicator = findViewById(R.id.indicator);
            mIndicator.setVisibility(View.VISIBLE);
            mIndicator.setRadius(8.0f);
            mIndicator.setViewPager(mViewPager);
        }
    }

    @Override
    public void onBackPressed() {
        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED, false);
        //com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_READER, false);
        if (reviewmode.equalsIgnoreCase(REVIEW)) {
            if (com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceBooleanValue(getBaseContext(), com.hurix.kitaboo.constants.Constants.HELPSCREEN_REQUIRED_REVIEW, false))
                com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, false);
        } else if (reviewmode.equalsIgnoreCase(READER)) {
            com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_REVIEW, true);
            com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(getBaseContext(), Constants.HELPSCREEN_REQUIRED_READER, false);
        }
        Intent data = new Intent();
        data.putExtra("closeMainLayout", true);
        setResult(Activity.RESULT_OK, data);
        ActionbarUtils.INSTANCE.clearActionBarSpecs();
        finish();

        // super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ActionbarUtils.INSTANCE.getIsHelpScreenVisible()) {
            if (!getIntent().getStringExtra(HELPSCREEN_TYPE).equalsIgnoreCase(BOOKSHELF)) {
                ActionbarUtils.INSTANCE.clearActionBarSpecs();
                bottomActionbar = findViewById(R.id.helpBottomActionbarid);
                topActionbar = findViewById(R.id.helpTopActionbarid);
                mReaderType = ActionbarUtils.INSTANCE.getMReaderType();
                ActionbarUtils.INSTANCE.setUpBottomBar(this, bottomActionbar);
            } else {
                mReaderType = ActionbarUtils.INSTANCE.getMReaderType();
                topActionbar = findViewById(R.id.helpTopActionbarid);
                bottomActionbar = findViewById(R.id.helpBottomActionbarid);

            }
            ActionbarUtils.INSTANCE.setTopActionbarItem(this, topActionbar);

            isMobile = Utility.isDeviceTypeMobile(this);
            accountType = ActionbarUtils.INSTANCE.getAccountType();
            /*jwTokenDetails = DBController.getInstance(this).getManager().getJWTokenDetails();*/
            // TODO: DBController dependency.

            if (!this.isFinishing())
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActionbarUtils.INSTANCE.setIsHelpScreenVisible(true);
                        if (getIntent().getExtras().get(HELPSCREEN_TYPE).toString().equalsIgnoreCase(BOOKSHELF)) {
                            showHelpForBookShelf();
                        } else if (SDKManager.getInstance().isNewTeacherReviewModeOn()) {
                            helpScreenViewsReview();
                        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            helpScreenViews();
                        } else {
                            helpScreenViewsLandscape();
                        }

                    }
                }, 3000);

        }
        int currentOrientation = getResources().getConfiguration().orientation;
       /* if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }*/
        mParetnView = findViewById(R.id.parent_container);

    }

    private void helpScreenViewsReview() {
       /* if(_collOfHelp_1!=null)
            _collOfHelp_1.clear();
        else if(_collOfHelp_2!=null)
            _collOfHelp_2.clear();*/
        setViewPagerAdapter();
        /*return;
        if (isMobile) {

            View studentprofile = topActionbar.getItem(R.id.teacher_review_profile_image);
            if (studentprofile != null) {
                int[] locationprofile = new int[2];
                studentprofile.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.student_profile, studentprofile.getPivotX(), studentprofile.getPivotY(), locationprofile[0], locationprofile[1], studentprofile.getMinimumWidth(), studentprofile.getMinimumHeight(), "", actionbartextSize, false, false));
            }

            View marker_1 = bottomActionbar.getItem(R.id.teacher_review_green);
            if (marker_1 != null) {
                int[] locationmarker1 = new int[2];
                marker_1.getLocationOnScreen(locationmarker1);
                _collOfHelp_1.add(new HelpVo(R.string.marker_1, marker_1.getPivotX(), marker_1.getPivotY(), locationmarker1[0], locationmarker1[1], marker_1.getMinimumWidth(), marker_1.getMinimumHeight(), CustomPlayerUIConstants.PT_COLOR_IC_TEXT, actionbartextSize, true, false));
            }

            View marker_2 = bottomActionbar.getItem(R.id.teacher_review_red);
            if (marker_2 != null) {
                int[] locationmarker2 = new int[2];
                marker_2.getLocationOnScreen(locationmarker2);
                _collOfHelp_1.add(new HelpVo(R.string.marker_2, marker_2.getPivotX(), marker_2.getPivotY(), locationmarker2[0], locationmarker2[1], marker_2.getMinimumWidth(), marker_2.getMinimumHeight(), CustomPlayerUIConstants.PT_COLOR_IC_TEXT, actionbartextSize, true, false));
            }

            View eraser = bottomActionbar.getItem(R.id.teacher_review_eraser);
            if (eraser != null) {
                int[] locationeraser = new int[2];
                eraser.getLocationOnScreen(locationeraser);
                _collOfHelp_1.add(new HelpVo(R.string.eraser, eraser.getPivotX(), eraser.getPivotY(), locationeraser[0], locationeraser[1], eraser.getMinimumWidth(), eraser.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_ERASER, actionbartextSize, true, false));
            }

            View undo = bottomActionbar.getItem(R.id.teacher_review_undo);
            if (undo != null) {
                int[] locationundo = new int[2];
                undo.getLocationOnScreen(locationundo);
                _collOfHelp_1.add(new HelpVo(R.string.undo, undo.getPivotX(), undo.getPivotY(), locationundo[0], locationundo[1], undo.getMinimumWidth(), undo.getMinimumHeight(), "<", actionbartextSize, true, false));
            }

            View arrow_1 = topActionbar.getItem(R.id.teacher_review_previous);
            if (arrow_1 != null) {
                int[] locationarrow1 = new int[2];
                arrow_1.getLocationOnScreen(locationarrow1);
                _collOfHelp_2.add(new HelpVo(R.string.arrow1, arrow_1.getPivotX(), arrow_1.getPivotY(), locationarrow1[0], locationarrow1[1], arrow_1.getMinimumWidth(), arrow_1.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_PREVIOUS, actionbartextSize, false, false));
            }

            View arrow_2 = topActionbar.getItem(R.id.teacher_review_next);
            if (arrow_2 != null) {
                int[] locationarrow2 = new int[2];
                arrow_2.getLocationOnScreen(locationarrow2);
                _collOfHelp_2.add(new HelpVo(R.string.arrow2, arrow_2.getPivotX(), arrow_2.getPivotY(), locationarrow2[0], locationarrow2[1], arrow_2.getMinimumWidth(), arrow_2.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_NEXT, actionbartextSize, false, false));
            }

            View done = bottomActionbar.getItem(R.id.teacher_review_done);
            if (done != null) {
                int[] locationdone = new int[2];
                done.getLocationOnScreen(locationdone);
                _collOfHelp_2.add(new HelpVo(R.string.done, done.getPivotX(), done.getPivotY(), locationdone[0], locationdone[1], done.getMinimumWidth(), done.getMinimumHeight(), "", 16, true, false));
            }

            if (_collOfHelp_1 != null && _collOfHelp_1.size() == 5 && _collOfHelp_2 != null && _collOfHelp_2.size() == 3)
                startHelpScreenNextActivity(REVIEW);

        } else {

            View studentprofile = topActionbar.getItem(R.id.teacher_review_profile_image);
            if (studentprofile != null) {
                int[] locationprofile = new int[2];
                studentprofile.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.student_profile, studentprofile.getPivotX(), studentprofile.getPivotY(), locationprofile[0], locationprofile[1], studentprofile.getMinimumWidth(), studentprofile.getMinimumHeight(), "", actionbartextSize, false, false));
            }

            View marker_1 =  new KitabooActionItemView(this);
            if (marker_1 != null) {
                int[] locationmarker_1 = new int[2];
                marker_1.getLocationOnScreen(locationmarker_1);
                _collOfHelp_1.add(new HelpVo(R.string.marker_1, marker_1.getPivotX(), marker_1.getPivotY(), locationmarker_1[0] - 3, locationmarker_1[1] + 6, marker_1.getMinimumWidth() + 3, marker_1.getMinimumHeight() + 3, CustomPlayerUIConstants.PT_COLOR_IC_TEXT, actionbartextSize, false, false));
            }

            View marker_2 = topActionbar.getItem(R.id.teacher_review_red);
            if (marker_2 != null) {
                int[] locationmarker_2 = new int[2];
                marker_2.getLocationOnScreen(locationmarker_2);
                _collOfHelp_1.add(new HelpVo(R.string.marker_2, marker_2.getPivotX(), marker_2.getPivotY(), locationmarker_2[0] - 3, locationmarker_2[1] + 6, marker_2.getMinimumWidth() + 3, marker_2.getMinimumHeight() + 3, CustomPlayerUIConstants.PT_COLOR_IC_TEXT, actionbartextSize, false, false));
            }

            View arrow_1 = topActionbar.getItem(R.id.teacher_review_previous);
            if (arrow_1 != null) {
                int[] locationarrow1 = new int[2];
                arrow_1.getLocationOnScreen(locationarrow1);
                _collOfHelp_1.add(new HelpVo(R.string.arrow1, arrow_1.getPivotX(), arrow_1.getPivotY(), locationarrow1[0], locationarrow1[1], arrow_1.getMinimumWidth(), arrow_1.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_PREVIOUS, actionbartextSize, false, false));
            }
            View arrow_2 = topActionbar.getItem(R.id.teacher_review_next);
            if (arrow_2 != null) {
                int[] locationarrow2 = new int[2];
                arrow_2.getLocationOnScreen(locationarrow2);
                _collOfHelp_1.add(new HelpVo(R.string.arrow2, arrow_2.getPivotX(), arrow_2.getPivotY(), locationarrow2[0], locationarrow2[1], arrow_2.getMinimumWidth(), arrow_2.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_NEXT, actionbartextSize, false, false));
            }

            View eraser = topActionbar.getItem(R.id.teacher_review_eraser);
            if (eraser != null) {
                int[] locationeraser = new int[2];
                eraser.getLocationOnScreen(locationeraser);
                _collOfHelp_2.add(new HelpVo(R.string.eraser, eraser.getPivotX(), eraser.getPivotY(), locationeraser[0], locationeraser[1], eraser.getMinimumWidth(), eraser.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_ERASER, actionbartextSize, false, false));
            }

            View done = topActionbar.getItem(R.id.teacher_review_done);
            if (done != null) {
                int[] locationdone = new int[2];
                done.getLocationOnScreen(locationdone);
                _collOfHelp_2.add(new HelpVo(R.string.done, done.getPivotX(), done.getPivotY(), locationdone[0], locationdone[1], done.getMinimumWidth(), done.getMinimumHeight(), "", actionbartextSize, false, false));
            }
            if (!getResources().getBoolean(R.bool.is_ADA_Client)) {
                View clearall = topActionbar.getItem(R.id.teacher_review_clear_all);
                if (clearall != null) {
                    int[] locationclearall = new int[2];
                    clearall.getLocationOnScreen(locationclearall);
                    _collOfHelp_2.add(new HelpVo(R.string.clear_All, clearall.getPivotX(), clearall.getPivotY(), locationclearall[0], locationclearall[1], clearall.getMinimumWidth(), clearall.getMinimumHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, actionbartextSize, false, false));
                }
            }
            View undo = topActionbar.getItem(R.id.teacher_review_undo);
            if (undo != null) {
                int[] locationundo = new int[2];
                undo.getLocationOnScreen(locationundo);
                _collOfHelp_2.add(new HelpVo(R.string.undo, undo.getPivotX(), undo.getPivotY(), locationundo[0], locationundo[1], undo.getMinimumWidth(), undo.getMinimumHeight(), "<", actionbartextSize, false, false));
            }

            if (_collOfHelp_1 != null && _collOfHelp_1.size() >= 5 && _collOfHelp_2 != null && _collOfHelp_2.size() == 4)
                setViewPagerAdapter();
        }*/
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionbarUtils.INSTANCE.setIsHelpScreenVisible(false);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActionbarUtils.INSTANCE.clearActionBarSpecs();
        ActionbarUtils.INSTANCE.setIsHelpScreenVisible(false);
        FullScreencall();
        finish();
    }

    public void FullScreencall() {
      /*  if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }*/
    }

    private void helpScreenViewsLandscape() {

        if (_collOfHelp_1 != null)
            _collOfHelp_1.clear();
        if (_collOfHelp_2 != null)
            _collOfHelp_2.clear();

        bottomActionbarYCords = Resources.getSystem().getDisplayMetrics().heightPixels - 160;

        if (getResources().getBoolean(R.bool.is_Infobase_Client)) {
            if (true) { // TODO: DBController dependency.

                View home = topActionbar.getItem(R.id.action_home);
                if (home != null) {
                    int[] locationhome = new int[2];
                    home.getLocationOnScreen(locationhome);
                    _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
                }
                View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
                if (profilePicIcon != null) {
                    int[] locationprofile = new int[2];
                    profilePicIcon.getLocationOnScreen(locationprofile);
                    _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
                }
                View toc = bottomActionbar.getItem(R.id.action_toc);
                if (toc != null) {
                    int[] locationtoc = new int[2];
                    toc.getLocationOnScreen(locationtoc);
                    _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
                }
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_1.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

            } else {

                View home = topActionbar.getItem(R.id.action_home);
                if (home != null) {
                    int[] locationhome = new int[2];
                    home.getLocationOnScreen(locationhome);
                    _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
                }
                View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
                if (profilePicIcon != null) {
                    int[] locationprofile = new int[2];
                    profilePicIcon.getLocationOnScreen(locationprofile);
                    _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
                }
                View toc = bottomActionbar.getItem(R.id.action_toc);
                if (toc != null) {
                    int[] locationtoc = new int[2];
                    toc.getLocationOnScreen(locationtoc);
                    _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
                }
                View myData = bottomActionbar.getItem(R.id.action_my_data);
                if (myData != null) {
                    int[] locationmyData = new int[2];
                    myData.getLocationOnScreen(locationmyData);
                    _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], locationmyData[1], myData.getMinimumWidth(), myData.getMinimumHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
                }
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

                if (mReaderType == EBookType.REFLOWEPUB) {
                    View search = bottomActionbar.getItem(R.id.action_search);
                    if (search != null) {
                        int[] locationsearch = new int[2];
                        search.getLocationOnScreen(locationsearch);
                        _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                    }
                } else {
                    View search = bottomActionbar.getItem(R.id.action_search);
                    if (search != null) {
                        int[] locationsearch = new int[2];
                        search.getLocationOnScreen(locationsearch);
                        _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                    }
                }
                View pen = bottomActionbar.getItem(R.id.action_pen);
                if (pen != null) {
                    int[] locationpen = new int[2];
                    pen.getLocationOnScreen(locationpen);
                    _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], locationpen[1], pen.getMinimumWidth(), pen.getMinimumHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
                }

                if (isMobile) {
                    View bookmark = topActionbar.getItem(R.id.action_profile_image);
                    if (bookmark != null) {
                        int[] locationbookMark = new int[2];
                        bookmark.getLocationOnScreen(locationbookMark);
                        if (_collOfHelp_2 != null) {
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                        }
                    }
                } else {

                    View bookmark = topActionbar.getItem(R.id.action_profile_image);
                    if (bookmark != null) {
                        int[] locationbookMark = new int[2];
                        bookmark.getLocationOnScreen(locationbookMark);
                        if (_collOfHelp_2 != null) {
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), (int) topActionbar.getPivotY(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                        }
                    }
                }

                if (getResources().getBoolean(R.bool.show_data_submit)) {
                    if (PlayerController.getInstance(this).isUgcShareEnabled()) {
                        if (accountType.equals(com.hurix.commons.Constants.Constants.TEACHER)) {
                            //   if (bookVo.IsClassAssociated()) {
                            View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                            if (teacherreview != null) {
                                int[] locationteacherreview = new int[2];
                                teacherreview.getLocationOnScreen(locationteacherreview);
                                _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], locationteacherreview[1], teacherreview.getMinimumWidth(), teacherreview.getMinimumHeight(), PlayerUIConstants.TB_TEACHER_IC_TEXT, actionbartextSize, true, false));
                            }
                        } else {
                            View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                            if (teacherreview != null) {
                                int[] locationteacherreview = new int[2];
                                teacherreview.getLocationOnScreen(locationteacherreview);
                                _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], locationteacherreview[1], teacherreview.getMinimumWidth(), teacherreview.getMinimumHeight(), PlayerUIConstants.TB_STUDENT_IC_TEXT, actionbartextSize, true, false));
                            }
                        }
                    }
                }
                View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
                if (thumbnail != null) {
                    int[] locationthumbnail = new int[2];
                    thumbnail.getLocationOnScreen(locationthumbnail);
                    _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], locationthumbnail[1], thumbnail.getMinimumWidth(), thumbnail.getMinimumHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
                }

                View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
                if (addnote != null) {
                    int[] locationaddnote = new int[2];
                    addnote.getLocationOnScreen(locationaddnote);
                    _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], locationaddnote[1], addnote.getMinimumWidth(), addnote.getMinimumHeight(), CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
                }

            }
        } else if (getResources().getBoolean(R.bool.is_it_worldbook)) {

            View home = topActionbar.getItem(R.id.action_home);
            if (home != null) {
                int[] locationhome = new int[2];
                home.getLocationOnScreen(locationhome);
                _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
            }
            View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
            if (profilePicIcon != null) {
                int[] locationprofile = new int[2];
                profilePicIcon.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
            }
            View toc
                    = bottomActionbar.getItem(R.id.action_toc);
            if (toc != null) {
                int[] locationtoc = new int[2];
                toc.getLocationOnScreen(locationtoc);
                _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
            }
            View myData = bottomActionbar.getItem(R.id.action_my_data);
            if (myData != null) {
                int[] locationmyData = new int[2];
                myData.getLocationOnScreen(locationmyData);
                _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], locationmyData[1], myData.getMinimumWidth(), myData.getMinimumHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
            }

            if (mReaderType == EBookType.REFLOWEPUB) {
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            } else {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            }

            View pen = bottomActionbar.getItem(R.id.action_pen);
            if (pen != null) {
                int[] locationpen = new int[2];
                pen.getLocationOnScreen(locationpen);
                _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], locationpen[1], pen.getMinimumWidth(), pen.getMinimumHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
            }

            if (isMobile) {
                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            } else {

                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), (int) topActionbar.getPivotY(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            }

            View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
            if (thumbnail != null) {
                int[] locationthumbnail = new int[2];
                thumbnail.getLocationOnScreen(locationthumbnail);
                _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], locationthumbnail[1], thumbnail.getMinimumWidth(), thumbnail.getMinimumHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
            }

            View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
            if (addnote != null) {
                int[] locationaddnote = new int[2];
                addnote.getLocationOnScreen(locationaddnote);
                _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], locationaddnote[1], addnote.getMinimumWidth(), addnote.getMinimumHeight(), CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
            }
        } else {

            View home = topActionbar.getItem(R.id.action_home);
            if (home != null) {
                int[] locationhome = new int[2];
                home.getLocationOnScreen(locationhome);
                _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1] - 20, home.getWidth(), home.getHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
            }

            View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
            if (profilePicIcon != null) {
                int[] locationprofile = new int[2];
                profilePicIcon.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1] - 20, profilePicIcon.getWidth(), profilePicIcon.getHeight(), "", actionbartextSize, false, false));
            }

            View toc = bottomActionbar.getItem(R.id.action_toc);
            if (toc != null) {
                int[] locationtoc = new int[2];
                toc.getLocationOnScreen(locationtoc);
                _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getWidth(), toc.getHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
            }

            View myData = bottomActionbar.getItem(R.id.action_my_data);
            if (myData != null) {
                int[] locationmyData = new int[2];
                myData.getLocationOnScreen(locationmyData);
                _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], locationmyData[1], myData.getWidth(), myData.getHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
            }

        /*    View search = (View) bottomActionbar.getItem(R.id.action_search);
            if (search != null) {
                int[] locationsearch = new int[2];
                search.getLocationOnScreen(locationsearch);
                _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
            }

            View fontsize = (View) bottomActionbar.getItem(R.id.action_font_settings);
            if (fontsize != null) {
                int[] locationfontsetting = new int[2];
                fontsize.getLocationOnScreen(locationfontsetting);
                _collOfHelp_1.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
            }
          */

            View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
            if (fontsize != null) {
                int[] locationfontsetting = new int[2];
                fontsize.getLocationOnScreen(locationfontsetting);
                _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], bottomActionbarYCords, fontsize.getWidth(), fontsize.getHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
            }

            if (mReaderType == EBookType.REFLOWEPUB) {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], bottomActionbarYCords, search.getWidth(), search.getHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            } else {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], bottomActionbarYCords, search.getWidth(), search.getHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            }

            View pen = bottomActionbar.getItem(R.id.action_pen);
            if (pen != null) {
                int[] locationpen = new int[2];
                pen.getLocationOnScreen(locationpen);
                _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], bottomActionbarYCords, pen.getWidth(), pen.getHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
            }

            if (isMobile) {

                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        if (mReaderType == EBookType.FIXEDKITABOO) {
                            if (getResources().getBoolean(R.bool.show_data_submit) && (PlayerController.getInstance(this).isUgcShareEnabled())) {
//                                _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) bottomActionbar.getItem(R.id.topbar_review).getX(), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                            } else {
                                _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) bottomActionbar.getItem(R.id.action_thumbnail).getX(), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                            }
                        } else {
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbarYCords, bookmark.getMinimumWidth(), bookmark.getMinimumHeight() + 20, "", actionbartextSize, false, false));
                        }

                    }
                }
            } else {

                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), (int) topActionbar.getPivotY(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                        else
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight()) - 30, (int) topActionbar.getPivotY() * 2, bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            }

            if (getResources().getBoolean(R.bool.show_data_submit)) {
                if ((PlayerController.getInstance(this).isUgcShareEnabled())) {
                    if (accountType.equals(com.hurix.commons.Constants.Constants.TEACHER)) {
                        //  if (bookVo.IsClassAssociated()) {
                        View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                        if (teacherreview != null) {
                            int[] locationteacherreview = new int[2];
                            teacherreview.getLocationOnScreen(locationteacherreview);
                            _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], bottomActionbarYCords, teacherreview.getWidth(), teacherreview.getHeight(), PlayerUIConstants.TB_TEACHER_IC_TEXT, actionbartextSize, true, false));
                        }
                    } else {
                        View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                        if (teacherreview != null) {
                            int[] locationteacherreview = new int[2];
                            teacherreview.getLocationOnScreen(locationteacherreview);
                            _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], bottomActionbarYCords, teacherreview.getWidth(), teacherreview.getHeight(), PlayerUIConstants.TB_STUDENT_IC_TEXT, actionbartextSize, true, false));
                        }
                    }
                }
            }

            if (mReaderType == EBookType.FIXEDKITABOO) {
                View clearAll = bottomActionbar.getItem(R.id.teacher_review_clear_all);
                if (clearAll != null) {
                    int[] locationaddnote = new int[2];
                    clearAll.getLocationOnScreen(locationaddnote);
                    _collOfHelp_2.add(new HelpVo(R.string.clearall, clearAll.getPivotX(), clearAll.getPivotY(), locationaddnote[0], bottomActionbarYCords, clearAll.getWidth(), clearAll.getHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, actionbartextSize, true, false));
                }
            }
            View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
            if (thumbnail != null) {
                int[] locationthumbnail = new int[2];
                thumbnail.getLocationOnScreen(locationthumbnail);
                _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], bottomActionbarYCords, thumbnail.getWidth(), thumbnail.getHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
            }

            View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
            if (addnote != null) {
                int[] locationaddnote = new int[2];
                addnote.getLocationOnScreen(locationaddnote);
                _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], bottomActionbarYCords, addnote.getWidth(), addnote.getHeight() + 20, CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
            }


        }

        setViewPagerAdapter();
//        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(this, com.hurix.kitaboo.constants.Constants.HELPSCREEN_REQUIRED_REVIEW, true);
    }


    private void helpScreenViews() {

        bottomActionbarYCords = Resources.getSystem().getDisplayMetrics().heightPixels - 160;
        if (_collOfHelp_1 != null)
            _collOfHelp_1.clear();
        if (_collOfHelp_2 != null)
            _collOfHelp_2.clear();

        if (getResources().getBoolean(R.bool.is_Infobase_Client)) {
            if (jwTokenDetails.getUserType().equalsIgnoreCase("GenericAccount")) {

                View home = topActionbar.getItem(R.id.action_home);
                if (home != null) {
                    int[] locationhome = new int[2];
                    home.getLocationOnScreen(locationhome);
                    _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
                }
                View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
                if (profilePicIcon != null) {
                    int[] locationprofile = new int[2];
                    profilePicIcon.getLocationOnScreen(locationprofile);
                    _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
                }
                View toc = bottomActionbar.getItem(R.id.action_toc);
                if (toc != null) {
                    int[] locationtoc = new int[2];
                    toc.getLocationOnScreen(locationtoc);
                    _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
                }
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_1.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

            } else {

                View home = topActionbar.getItem(R.id.action_home);
                if (home != null) {
                    int[] locationhome = new int[2];
                    home.getLocationOnScreen(locationhome);
                    _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
                }
                View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
                if (profilePicIcon != null) {
                    int[] locationprofile = new int[2];
                    profilePicIcon.getLocationOnScreen(locationprofile);
                    _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
                }
                View toc = bottomActionbar.getItem(R.id.action_toc);
                if (toc != null) {
                    int[] locationtoc = new int[2];
                    toc.getLocationOnScreen(locationtoc);
                    _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
                }
                View myData = bottomActionbar.getItem(R.id.action_my_data);
                if (myData != null) {
                    int[] locationmyData = new int[2];
                    myData.getLocationOnScreen(locationmyData);
                    _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], locationmyData[1], myData.getMinimumWidth(), myData.getMinimumHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
                }
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

                if (mReaderType == EBookType.REFLOWEPUB) {
                    View search = bottomActionbar.getItem(R.id.action_search);
                    if (search != null) {
                        int[] locationsearch = new int[2];
                        search.getLocationOnScreen(locationsearch);
                        _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                    }
                } else {
                    View search = bottomActionbar.getItem(R.id.action_search);
                    if (search != null) {
                        int[] locationsearch = new int[2];
                        search.getLocationOnScreen(locationsearch);
                        _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                    }
                }
                View pen = bottomActionbar.getItem(R.id.action_pen);
                if (pen != null) {
                    int[] locationpen = new int[2];
                    pen.getLocationOnScreen(locationpen);
                    _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], locationpen[1], pen.getMinimumWidth(), pen.getMinimumHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
                }

                if (isMobile) {
                    View bookmark = topActionbar.getItem(R.id.action_profile_image);
                    if (bookmark != null) {
                        int[] locationbookMark = new int[2];
                        bookmark.getLocationOnScreen(locationbookMark);
                        if (_collOfHelp_2 != null) {
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                        }
                    }
                } else {

                    View bookmark = topActionbar.getItem(R.id.action_profile_image);
                    if (bookmark != null) {
                        int[] locationbookMark = new int[2];
                        bookmark.getLocationOnScreen(locationbookMark);
                        if (_collOfHelp_2 != null) {
                            _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), (int) topActionbar.getPivotY(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                        }
                    }
                }

                if (getResources().getBoolean(R.bool.show_data_submit)) {
                    if ((PlayerController.getInstance(this).isUgcShareEnabled())) {
                        if (accountType.equals(com.hurix.commons.Constants.Constants.TEACHER)) {
                            //   if (bookVo.IsClassAssociated()) {
                            View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                            if (teacherreview != null) {
                                int[] locationteacherreview = new int[2];
                                teacherreview.getLocationOnScreen(locationteacherreview);
                                _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], locationteacherreview[1], teacherreview.getMinimumWidth(), teacherreview.getMinimumHeight(), PlayerUIConstants.TB_TEACHER_IC_TEXT, actionbartextSize, true, false));
                            }
                        } else {
                            View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                            if (teacherreview != null) {
                                int[] locationteacherreview = new int[2];
                                teacherreview.getLocationOnScreen(locationteacherreview);
                                _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], locationteacherreview[1], teacherreview.getMinimumWidth(), teacherreview.getMinimumHeight(), PlayerUIConstants.TB_STUDENT_IC_TEXT, actionbartextSize, true, false));
                            }
                        }
                    }
                }
                View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
                if (thumbnail != null) {
                    int[] locationthumbnail = new int[2];
                    thumbnail.getLocationOnScreen(locationthumbnail);
                    _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], locationthumbnail[1], thumbnail.getMinimumWidth(), thumbnail.getMinimumHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
                }

                View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
                if (addnote != null) {
                    int[] locationaddnote = new int[2];
                    addnote.getLocationOnScreen(locationaddnote);
                    _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], locationaddnote[1], addnote.getMinimumWidth(), addnote.getMinimumHeight(), CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
                }

            }
        } else if (getResources().getBoolean(R.bool.is_it_worldbook)) {

            View home = topActionbar.getItem(R.id.action_home);
            if (home != null) {
                int[] locationhome = new int[2];
                home.getLocationOnScreen(locationhome);
                _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], locationhome[1], home.getMinimumWidth(), home.getMinimumHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
            }
            View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
            if (profilePicIcon != null) {
                int[] locationprofile = new int[2];
                profilePicIcon.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], locationprofile[1], profilePicIcon.getMinimumWidth(), profilePicIcon.getMinimumHeight(), "", actionbartextSize, false, false));
            }
            View toc
                    = bottomActionbar.getItem(R.id.action_toc);
            if (toc != null) {
                int[] locationtoc = new int[2];
                toc.getLocationOnScreen(locationtoc);
                _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], locationtoc[1], toc.getMinimumWidth(), toc.getMinimumHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
            }
            View myData = bottomActionbar.getItem(R.id.action_my_data);
            if (myData != null) {
                int[] locationmyData = new int[2];
                myData.getLocationOnScreen(locationmyData);
                _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], locationmyData[1], myData.getMinimumWidth(), myData.getMinimumHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
            }

            if (mReaderType == EBookType.REFLOWEPUB) {
                View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
                if (fontsize != null) {
                    int[] locationfontsetting = new int[2];
                    fontsize.getLocationOnScreen(locationfontsetting);
                    _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
                }

                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            } else {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            }

            View pen = bottomActionbar.getItem(R.id.action_pen);
            if (pen != null) {
                int[] locationpen = new int[2];
                pen.getLocationOnScreen(locationpen);
                _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], locationpen[1], pen.getMinimumWidth(), pen.getMinimumHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
            }

            if (isMobile) {
                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbar.getHeight(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            } else {

                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), (int) topActionbar.getPivotY(), bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            }

            View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
            if (thumbnail != null) {
                int[] locationthumbnail = new int[2];
                thumbnail.getLocationOnScreen(locationthumbnail);
                _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], locationthumbnail[1], thumbnail.getMinimumWidth(), thumbnail.getMinimumHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
            }

            View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
            if (addnote != null) {
                int[] locationaddnote = new int[2];
                addnote.getLocationOnScreen(locationaddnote);
                _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], locationaddnote[1], addnote.getMinimumWidth(), addnote.getMinimumHeight(), CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
            }

        } else {

            View home = topActionbar.getItem(R.id.action_home);
            if (home != null) {
                int[] locationhome = new int[2];
                home.getLocationOnScreen(locationhome);
                _collOfHelp_1.add(new HelpVo(R.string.home, home.getPivotX(), home.getPivotY(), locationhome[0], topActionbarYCords, home.getWidth(), home.getHeight(), CustomPlayerUIConstants.TOP_ACTION_HOME_TEXT, actionbartextSize, false, false));
            }
            View profilePicIcon = topActionbar.getItem(R.id.action_profile_image);
            if (profilePicIcon != null) {
                int[] locationprofile = new int[2];
                profilePicIcon.getLocationOnScreen(locationprofile);
                _collOfHelp_1.add(new HelpVo(R.string.profile, profilePicIcon.getPivotX(), profilePicIcon.getPivotY(), locationprofile[0], topActionbarYCords, profilePicIcon.getWidth(), profilePicIcon.getHeight(), "", actionbartextSize, false, false));
            }
            View toc
                    = bottomActionbar.getItem(R.id.action_toc);
            if (toc != null) {
                int[] locationtoc = new int[2];
                toc.getLocationOnScreen(locationtoc);
                Log.d("toc", "XY coords for " + R.string.toc + ":" + (locationtoc[0] + (toc.getWidth() / 2) - 11) + ":" + (bottomActionbarYCords));
                _collOfHelp_1.add(new HelpVo(R.string.toc, toc.getPivotX(), toc.getPivotY(), locationtoc[0], bottomActionbarYCords, toc.getWidth(), toc.getHeight(), CustomPlayerUIConstants.ACTION_TOC_TEXT, actionbartextSize, true, false));
            }
            View myData = bottomActionbar.getItem(R.id.action_my_data);
            if (myData != null) {
                int[] locationmyData = new int[2];
                myData.getLocationOnScreen(locationmyData);
                _collOfHelp_1.add(new HelpVo(R.string.my_data, myData.getPivotX(), myData.getPivotY(), locationmyData[0], bottomActionbarYCords, myData.getWidth(), myData.getHeight(), CustomPlayerUIConstants.ACTION_MYDATA_TEXT, actionbartextSize, true, false));
            }
           /* View search = (View) bottomActionbar.getItem(R.id.action_search);
            if(search != null) {
                int[] locationsearch = new int[2];
                search.getLocationOnScreen(locationsearch);
                _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], locationsearch[1], search.getMinimumWidth(), search.getMinimumHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
            }
            View fontsize = (View) bottomActionbar.getItem(R.id.action_font_settings);
            if(fontsize != null) {
                int[] locationfontsetting = new int[2];
                fontsize.getLocationOnScreen(locationfontsetting);
                _collOfHelp_1.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], locationfontsetting[1], fontsize.getMinimumWidth(), fontsize.getMinimumHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
            }
*/
            View fontsize = bottomActionbar.getItem(R.id.action_font_settings);
            if (fontsize != null) {
                int[] locationfontsetting = new int[2];
                fontsize.getLocationOnScreen(locationfontsetting);
                _collOfHelp_2.add(new HelpVo(R.string.font_setting, fontsize.getPivotX(), fontsize.getPivotY(), locationfontsetting[0], bottomActionbarYCords, fontsize.getWidth(), fontsize.getHeight(), CustomPlayerUIConstants.ACTION_FONT_SETTING, actionbartextSize, true, false));
            }

            if (mReaderType == EBookType.REFLOWEPUB) {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_2.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], bottomActionbarYCords, search.getWidth(), search.getHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            } else {
                View search = bottomActionbar.getItem(R.id.action_search);
                if (search != null) {
                    int[] locationsearch = new int[2];
                    search.getLocationOnScreen(locationsearch);
                    _collOfHelp_1.add(new HelpVo(R.string.search, search.getPivotX(), search.getPivotY(), locationsearch[0], bottomActionbarYCords, search.getWidth(), search.getHeight(), CustomPlayerUIConstants.ACTION_SEARCH_TEXT, actionbartextSize, true, false));
                }
            }

            View pen = bottomActionbar.getItem(R.id.action_pen);
            if (pen != null) {
                int[] locationpen = new int[2];
                pen.getLocationOnScreen(locationpen);
                _collOfHelp_1.add(new HelpVo(R.string.pen, pen.getPivotX(), pen.getPivotY(), locationpen[0], bottomActionbarYCords, pen.getWidth(), pen.getHeight(), CustomPlayerUIConstants.ACTION_PEN_TEXT, actionbartextSize, true, false));
            }

            if (isMobile) {
                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) (bookmark.getX() + 10), topActionbarYCords, bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            } else {

                View bookmark = topActionbar.getItem(R.id.action_profile_image);
                if (bookmark != null) {
                    int[] locationbookMark = new int[2];
                    bookmark.getLocationOnScreen(locationbookMark);
                    if (_collOfHelp_2 != null) {
                        _collOfHelp_2.add(new HelpVo(R.string.bookMark, locationbookMark[0], 0, (int) ((bookmark.getX()) - topActionbar.getHeight() / 2), topActionbarYCords, bookmark.getMinimumWidth(), bookmark.getMinimumHeight(), "", actionbartextSize, false, false));
                    }
                }
            }

            if (getResources().getBoolean(R.bool.show_data_submit)) {
                if ((PlayerController.getInstance(this).isUgcShareEnabled())) {
                    if (accountType.equals(com.hurix.commons.Constants.Constants.TEACHER)) {
                        //   if (bookVo.IsClassAssociated()) {
                        View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                        if (teacherreview != null) {
                            int[] locationteacherreview = new int[2];
                            teacherreview.getLocationOnScreen(locationteacherreview);
                            _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], bottomActionbarYCords, teacherreview.getWidth(), teacherreview.getHeight(), PlayerUIConstants.TB_TEACHER_IC_TEXT, actionbartextSize, true, false));
                        }
                    } else {
                        View teacherreview = bottomActionbar.getItem(R.id.topbar_review);
                        if (teacherreview != null) {
                            int[] locationteacherreview = new int[2];
                            teacherreview.getLocationOnScreen(locationteacherreview);
                            _collOfHelp_2.add(new HelpVo(R.string.settings, teacherreview.getPivotX(), teacherreview.getPivotY(), locationteacherreview[0], bottomActionbarYCords, teacherreview.getWidth(), teacherreview.getHeight(), PlayerUIConstants.TB_STUDENT_IC_TEXT, actionbartextSize, true, false));
                        }
                    }
                }
            }

            if (mReaderType == EBookType.FIXEDKITABOO) {
                View clearAll = bottomActionbar.getItem(R.id.teacher_review_clear_all);
                if (clearAll != null) {
                    int[] locationclearall = new int[2];
                    clearAll.getLocationOnScreen(locationclearall);
                    _collOfHelp_2.add(new HelpVo(R.string.clearall, clearAll.getPivotX(), clearAll.getPivotY(), locationclearall[0], bottomActionbarYCords, clearAll.getWidth(), clearAll.getHeight(), CustomPlayerUIConstants.TEACHER_ACTIONBAR_CLEARALL, actionbartextSize, true, false));
                }
            }


            View thumbnail = bottomActionbar.getItem(R.id.action_thumbnail);
            int[] locationthumbnail = new int[2];
            if (thumbnail != null) {
                thumbnail.getLocationOnScreen(locationthumbnail);
                _collOfHelp_2.add(new HelpVo(R.string.thumbnail, thumbnail.getPivotX(), thumbnail.getPivotY(), locationthumbnail[0], bottomActionbarYCords, thumbnail.getWidth(), thumbnail.getHeight(), CustomPlayerUIConstants.ACTION_THUMBNAIL_TEXT, actionbartextSize, true, false));
            }

            View addnote = bottomActionbar.getItem(R.id.action_sticky_note);
            if (addnote != null) {
                int[] locationaddnote = new int[2];
                addnote.getLocationOnScreen(locationaddnote);
                _collOfHelp_2.add(new HelpVo(R.string.addnote, addnote.getPivotX(), addnote.getPivotY(), locationaddnote[0], bottomActionbarYCords, addnote.getWidth(), addnote.getHeight(), CustomPlayerUIConstants.NOTE_ICON_TEXT, actionbartextSize, true, false));
            }

        }

        setViewPagerAdapter();

    }

    public void showHelpForBookShelf() {
        hashMap =
                (HashMap<Integer, ArrayList<HelpVo>>) getIntent().getExtras().get("coll");
        if (hashMap != null && hashMap.values().size() >= 1) {
            setViewPagerAdapter();
        }
    }


}
