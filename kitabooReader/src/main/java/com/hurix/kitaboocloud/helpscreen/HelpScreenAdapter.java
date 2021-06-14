package com.hurix.kitaboocloud.helpscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;


import com.hurix.epubreader.Utility.Utils;
import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboo.iconify.Typefaces;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.common.HelpVo;

import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Amit Raj on 08-08-2019.
 */
public class HelpScreenAdapter extends PagerAdapter implements View.OnClickListener {

    HashMap<Integer, ArrayList<HelpVo>> hashMap;
    Context mContext;
    private Bitmap bitmap;
    private Canvas canvas;
    private int lineHeight = 0;
    private int lineHeightBottom = 0;
    private final int lineHeightNext = 0;
    private int lineHeightBottomNext = 0;
    private int lineHeightBottomNext1 = 0;
    private int lineHeightReview = 0;
    private int lineHeightReviewNext = 0;
    private int lineHeightReviewNext1 = 0;
    private int lineHeightReviewConst;
    private int helpLineWidth = 2;
    private int helpCircleHight = 10;
    int i = 1;
    private Typeface typeFace;
    ViewGroup view;
    int helpPosition;
    int gapHelplineTxt;
    int gapHelpCirle = 20;
    private final String PROFILE_SETTINGS_TEXT = "?";
    private final SharedPreferences _sharedpreferences;
    private final int screenHeight = 0;
    private final boolean isReader;
    private final boolean isreview;
    private final GradientDrawable drawable = new GradientDrawable();
    private float startx = 0, starty = 0, endx = 0, endy = 0, startarrowx = 0, startarrowy = 0, endarrowx = 0, endarrowy = 0;
    private final ReaderThemeSettingVo readerThemeSettingVo;
    private LinearLayout llBottomFeatureTextContainer;
    private RelativeLayout rlTopFeatureTextContainer;

    public HelpScreenAdapter(Context context, HashMap<Integer, ArrayList<HelpVo>> hashMap, boolean mReader, boolean review) {
        this.hashMap = hashMap;
        this.mContext = context;
        isReader = mReader;
        isreview = review;
        readerThemeSettingVo = InsightReaderThemeController.getInstance(mContext.getApplicationContext()).getReaderThemeUserSettingVo();
        _sharedpreferences = mContext.getSharedPreferences(Constants.SHELF_PREFS_NAME, Context.MODE_PRIVATE);
        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED, true);
        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_READER, true);
        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, true);
    }

    @Override
    public int getCount() {
        return hashMap.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object object) {
        ViewGroup parent = collection;
        parent.removeView((View) object);
        System.gc();
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
        final ImageView iv;
        TextView mBtnHelp;
        helpCircleHight = mContext.getResources().getInteger(com.hurix.kitaboo.constants.R.integer.help_circle_hight);
        helpPosition = position;
        i = 0;
        try {
            WindowManager wm = (WindowManager) collection.getContext().getSystemService(Context.WINDOW_SERVICE);
            final DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int screenSize = mContext.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;
            int density = mContext.getResources().getDisplayMetrics().densityDpi;
            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                // lineHeight= 150;
            }
            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                // lineHeight= 100;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //lineHeight= 80;
                }

                helpLineWidth = 1;
                gapHelplineTxt = 8;
                helpCircleHight = 5;
            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //  lineHeight= 60;
                }
                gapHelplineTxt = 13;

            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                //lineHeight= 80;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // lineHeight= 60;
                }
                gapHelplineTxt = 13;

            } else {
                gapHelplineTxt = 33;
            }
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (isreview && !Utility.isDeviceTypeMobile(mContext)) {
                if (hashMap.get(position).get(0).isBookShelf())
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_review, null);
                else
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_review_next, null);
            } else {
                if (hashMap.get(position).get(0).isBookShelf())
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow, null);
                else
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_next, null);
            }
            iv = view.findViewById(R.id.imagelayout);
//            iv.setColorFilter(Color.parseColor(readerThemeSettingVo.getHelp().getOverlayPanel().getBackground()));
//            iv.setAlpha(Color.parseColor(readerThemeSettingVo.getHelp().getOverlayPanel().getOpacity()));
            mBtnHelp = view.findViewById(R.id.txthelp);
            mBtnHelp.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getTextColor()));
            _sharedpreferences.edit().putBoolean(Constants.BOOK_SHELF_LAUNCH_FIRST_TIME, true).commit();
            Intent intent = new Intent();
            intent.putExtra("Action","SET_BOOKSHELF_HELPSCREEN");
            intent.setAction("DBCONTROLLER_RECIVER");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            /*if (hashMap.size() > 1) {
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_skip_txt)));

            }*/
            if (hashMap.size() > 1) {
                if (position == 0) {
                    mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_skip_txt)));

                } else {
                    mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_got_txt)));
                }
            } else {
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_got_txt)));
            }

            if (isReader) {
                mBtnHelp.setVisibility(View.VISIBLE);
                _sharedpreferences.edit().putBoolean(Constants.BOOK_PLAYER_LAUNCH_FIRST_TIME, true).commit();
                Intent intentPlayer = new Intent();
                intentPlayer.putExtra("Action","SET_BOOKPLAYER_HELPSCREEN");
                intentPlayer.setAction("DBCONTROLLER_RECIVER");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentPlayer);
            }

            mBtnHelp.setOnClickListener(this);

            try {
                System.gc();
                bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
            } catch (Exception e) {
                System.gc();
                bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
            } finally {

            }
            canvas = new Canvas(bitmap);
            llBottomFeatureTextContainer = view.findViewById(R.id.llBottomFeatureTextContainer);
            llBottomFeatureTextContainer.setWeightSum(ActionbarUtils.INSTANCE.getBottomActionBarItems());
            rlTopFeatureTextContainer = view.findViewById(R.id.rlTopFeatureTextContainer);

            iv.setImageBitmap(bitmap);
            if (position == 0 && ((HelpScreenActivity) mContext).getIntent().
                    getStringExtra("HELPSCREEN_TYPE").equalsIgnoreCase("BookShelf")) {
                addChildViews(hashMap.get(position), view, true);
            } else {
                //Collections.reverse(hashMap.get(position));
                if (isReader) {
                    if (position == 0) {
                        addChildViews(hashMap.get(position), view, false);
                    } else {
                        addChildViewsNext(hashMap.get(position), view, false);
                        view.findViewById(R.id.txtViewCategories).setVisibility(View.GONE);
                    }
                } else {
                    if (position == 0) {
                        addChildViewsReview(hashMap.get(position), view, false);
                    } else {
                        addChildViewsReviewNext(hashMap.get(position), view);
                        view.findViewById(R.id.txtViewCategories).setVisibility(View.GONE);
                    }
                }
            }
            (collection).addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    public void addChildViews(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                LinearLayout.LayoutParams params = null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue = 0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if (isBookShelf) {
                        if (Utility.isDeviceTypeMobile(mContext)) {
                            sizeMinimizeValue = 35;
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                                sizeMinimizeValue = 44;
                            }
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 55;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 55;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 37;
                                }
                            }
                        } else {
                            sizeMinimizeValue = 27;
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                                sizeMinimizeValue = 9;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                                sizeMinimizeValue = 27;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                                sizeMinimizeValue = 29;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH) {
                                sizeMinimizeValue = 11;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 9;
                                }

                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 27;
                                }
                            }
                        }
                        tv.setTextSize(vo.getTextSize() - sizeMinimizeValue);
                        tv.setGravity(Gravity.CENTER);

                        params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                        tv.setLayoutParams(params);

                    } else {
                        if (Utility.isDeviceTypeMobile(mContext)) {
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                                sizeMinimizeValue = 0;
                            }
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }
                            }
                            tv.setTextSize(vo.getTextSize());
                            tv.setGravity(Gravity.CENTER);
                            /*tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.setGravity(Gravity.CENTER_VERTICAL);*/
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            tv.setVisibility(View.GONE);
                            params = new LinearLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();

                            params.topMargin = vo.getViewTop();
                            Log.d("TOC", "view in adapter: " + vo.getHelpText() + "left:" + vo.getViewLeft() +
                                    "top" + vo.getViewTop());
                        } else {

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                                sizeMinimizeValue = 9;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                                sizeMinimizeValue = 0;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                                sizeMinimizeValue = 0;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }

                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 27;
                                }
                            }
                            tv.setTextSize(vo.getTextSize() - sizeMinimizeValue);
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            tv.setVisibility(View.GONE);

                            params = new LinearLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();
                            Log.d("TOC", "view in adapter: " + vo.getHelpText() + "left:" + vo.getViewLeft() +
                                    "top" + vo.getViewTop());
                           /* tv.setTextSize(vo.getTextSize());
                            tv.setGravity(Gravity.CENTER);
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isBookShelf)
                    view.addView(tv, params);
                view.invalidate();
                createFeatureTexts(vo, isBookShelf, mViewColl, 0);
//                drawArrowLine(vo, isBookShelf);
            }

        }
        ActionbarUtils.INSTANCE.addExtraWeight(mContext, llBottomFeatureTextContainer);

    }

    private void createFeatureTexts(HelpVo vo, boolean isBookShelf, ArrayList<HelpVo> mViewColl,
                                    int pagePosition) {
        ActionbarUtils.INSTANCE.createActionBarFeatureText(vo, isBookShelf,
                llBottomFeatureTextContainer, rlTopFeatureTextContainer, mContext, pagePosition);
    }


    public void addChildViewsNext(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {
        Collections.reverse(mViewColl);
        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params = null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue = 0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if (isBookShelf) {
                        if (Utility.isDeviceTypeMobile(mContext)) {
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                                sizeMinimizeValue = 44;
                            }
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 55;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 37;
                                }
                            }
                        } else {

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                                sizeMinimizeValue = 9;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                                sizeMinimizeValue = 27;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                                sizeMinimizeValue = 29;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH) {
                                sizeMinimizeValue = 11;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 15;
                                }

                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 27;
                                }
                            }
                        }
                        tv.setTextSize(vo.getTextSize() - sizeMinimizeValue);
                        tv.setGravity(Gravity.CENTER);
                        //tv.setHeight(vo.getViewHeight());
                        //tv.setWidth(vo.getViewWidth());
                        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                        tv.setLayoutParams(params);

                    } else {
                        if (Utility.isDeviceTypeMobile(mContext)) {
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                                sizeMinimizeValue = 44;
                            }
                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 44;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 55;
                                }
                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 37;
                                }
                            }
                            tv.setTextSize(vo.getTextSize());
                            tv.setGravity(Gravity.CENTER);
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            tv.setVisibility(View.GONE);
                            params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();
                        } else {

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                                sizeMinimizeValue = 9;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                                sizeMinimizeValue = 15;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                                sizeMinimizeValue = 0;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH) {
                                sizeMinimizeValue = 0;
                            }

                            if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 0;
                                }

                            } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    sizeMinimizeValue = 27;
                                }
                            }
                            tv.setTextSize(vo.getTextSize());
                            tv.setGravity(Gravity.CENTER);
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            tv.setVisibility(View.GONE);

                            params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                view.addView(tv, params);
                view.invalidate();
//                drawArrowLineNext(vo);
                createFeatureTexts(vo, isBookShelf, mViewColl, 1);
            }

        }

    }


    public void addChildViewsReview(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params = null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                int sizeMinimizeValue = 0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if (Utility.isDeviceTypeMobile(mContext)) {
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                            sizeMinimizeValue = 44;
                        }
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 55;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 37;
                            }
                        }
                        tv.setTextSize(vo.getTextSize());
                        tv.setGravity(Gravity.CENTER);
                        tv.setHeight(vo.getViewHeight());
                        tv.setWidth(vo.getViewWidth());
                        params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();

                        if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1))) || (mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                            //tv.setPadding(4, 4, 4, 4);
                            if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {
                                   /* drawable.setShape(GradientDrawable.OVAL);
                                    drawable.setStroke(2, Color.parseColor("#49ef1c"));*/
                                tv.setGravity(Gravity.LEFT);
                                params.topMargin = vo.getViewTop();
                                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                                  /*  drawable.setShape(GradientDrawable.OVAL);
                                    drawable.setStroke(2, Color.parseColor("#ff1818"));*/
                                tv.setGravity(Gravity.LEFT);
                                params.topMargin = vo.getViewTop();
                                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                            }
                        }
                    } else {

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                            sizeMinimizeValue = 9;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                            sizeMinimizeValue = 15;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                            sizeMinimizeValue = 0;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH) {
                            sizeMinimizeValue = 0;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 0;
                            }

                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 27;
                            }
                        }
                        tv.setTextSize(vo.getTextSize());
                        tv.setGravity(Gravity.CENTER);
                        tv.setHeight(vo.getViewHeight());
                        tv.setWidth(vo.getViewWidth());
                        params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                        // params.topMargin = vo.getViewTop();
                        if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1))) || (mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                            //tv.setPadding(4, 4, 4, 4);
                            if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {

                                params.leftMargin = vo.getViewLeft() - 12;
                                params.topMargin = vo.getViewTop() - 10;
                                tv.setTextColor(Color.parseColor("#49ef1c"));
                            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {

                                params.leftMargin = vo.getViewLeft() - 12;
                                params.topMargin = vo.getViewTop() - 10;
                                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv.setVisibility(View.VISIBLE);
                view.addView(tv, params);
                view.invalidate();
               /* if (Utility.isDeviceTypeMobile(mContext)) {
                    createFeatureTexts(vo, isBookShelf, mViewColl, 0);
                } else {
                    drawArrowLineReview(vo);
                }*/
                drawArrowLineReview(vo);
                ActionbarUtils.bottomActionbar.setVisibility(View.INVISIBLE);


            }
        }
//        ActionbarUtils.INSTANCE.addExtraWeight(mContext, llBottomFeatureTextContainer);

    }


    @SuppressLint("WrongConstant")
    public void addChildViewsReviewNext(ArrayList<HelpVo> mViewColl, ViewGroup view) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params = null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                if (vo.getViewText().equalsIgnoreCase("Done"))
                    tv.setTypeface(null);
                else
                    tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue = 0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if (Utility.isDeviceTypeMobile(mContext)) {
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                            sizeMinimizeValue = 44;
                        }
                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_XXHIGH) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 44;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_560) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 55;
                            }
                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && density == DisplayMetrics.DENSITY_420) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 37;
                            }
                        }
                        tv.setTextSize(vo.getTextSize());
                        tv.setGravity(Gravity.CENTER);
                        tv.setHeight(vo.getViewHeight());
                        tv.setWidth(vo.getViewWidth());
                        params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                    } else {

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM) {
                            sizeMinimizeValue = 9;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_XHIGH) {
                            sizeMinimizeValue = 15;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_360) {
                            sizeMinimizeValue = 0;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_HIGH) {
                            sizeMinimizeValue = 0;
                        }

                        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV) {
                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 0;
                            }

                        } else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

                            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                sizeMinimizeValue = 27;
                            }
                        }
                        tv.setTextSize(vo.getTextSize());
                        tv.setGravity(Gravity.CENTER);
                        tv.setHeight(vo.getViewHeight());
                        tv.setWidth(vo.getViewWidth());
                        params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.addView(tv, params);
                view.invalidate();
               /* if (Utility.isDeviceTypeMobile(mContext)) {
                    createFeatureTexts(vo, false, mViewColl, 1);
                } else {
                    drawArrowLineNextReview(vo);
                }*/
                drawArrowLineNextReview(vo);

            }
        }
    }


    private void getReaderFont() {

        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(mContext, fontfile);
        } else {
            typeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }

    }


    public void drawArrowLineNext(HelpVo vo) {

        Paint paint;
        Paint paintText;
        i = i + 1;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(helpLineWidth);
        paint.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        // paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeWidth(1);
        paintText.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getTextColor()));
        paintText.setAntiAlias(true);
        paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size_new));
        if (Utils.isDeviceTypeMobile(mContext)) {
            if (vo.getBottomState()) {
                if (lineHeightBottomNext == 0) {
                    lineHeightBottomNext = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 3;
                } else {
                    lineHeightBottomNext = lineHeightBottomNext - (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 7);
                }

                float viewCenterX;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                } else {
                    viewCenterX = vo.getViewLeft() + (vo.getViewWidth() - 180) / 2;

                }
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext, helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelplineTxt - textWidth - mContext.getResources().getDimension(R.dimen.paint_text_size),
                        vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + 20, paintTextCircle);


                if (mContext.getResources().getBoolean(R.bool.is_Academic_Approach) &&
                        (mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.search)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_the_text));
                    canvas.drawText(mContext.getResources().getString(R.string.search_the_text), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.search)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_));
                    canvas.drawText(mContext.getResources().getString(R.string.search_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.search_1), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + (gapHelplineTxt * 4) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.font_setting)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.font_setting_));
                    canvas.drawText(mContext.getResources().getString(R.string.font_setting_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.addnote)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.addnote_));
                    canvas.drawText(mContext.getResources().getString(R.string.addnote_), viewCenterX - textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.thumbnail)))) {
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_1), viewCenterX - gapHelplineTxt - (textWidth * 2) + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_2), viewCenterX - gapHelplineTxt - (textWidth * 2) + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.clearall)))) {
                    if (mContext.getResources().getBoolean(R.bool.is_WSET_client)) {
                        canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - (textWidth * 3) + (gapHelpCirle * 4),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                    } else {
                        canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle * 4),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                    }
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.settings)))) {

                    if (mContext.getResources().getBoolean(R.bool.is_WSET_client)) {
                        canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - (textWidth * 4) + (gapHelpCirle * 4),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                    } else {
                        canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle * 4),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                    }
                }
            } else {
                if (lineHeightBottomNext1 == 0) {
                    lineHeightBottomNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 6;
                }
                float viewCenterX;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                } else {
                    viewCenterX = vo.getViewLeft() + (vo.getViewWidth() - 180) / 2;

                }
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1, helpCircleHight, paintTextCircle);
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.bookMark)))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2) - gapHelplineTxt,
                            vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1 + gapHelplineTxt, paintTextCircle);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.bookMark_));
                    canvas.drawText(mContext.getResources().getString(R.string.bookMark_), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeightBottomNext1 + gapHelplineTxt, paintText);
                }
            }
        } else {
            if (vo.getBottomState()) {
                if (lineHeightBottomNext == 0) {
                    lineHeightBottomNext = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else {
                    lineHeightBottomNext = lineHeightBottomNext - (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 7);
                }

                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext, helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelplineTxt - textWidth,
                        vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + 20, paintTextCircle);

                if (mContext.getResources().getBoolean(R.bool.is_Academic_Approach) &&
                        (mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.search)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_the_text));
                    canvas.drawText(mContext.getResources().getString(R.string.search_the_text), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.search)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_));
                    canvas.drawText(mContext.getResources().getString(R.string.search_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.search_1), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + (gapHelplineTxt * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.font_setting)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.font_setting_));
                    canvas.drawText(mContext.getResources().getString(R.string.font_setting_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.addnote)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.addnote_));
                    canvas.drawText(mContext.getResources().getString(R.string.addnote_), viewCenterX - textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.thumbnail)))) {
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_1), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_2), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.clearall)))) {
                    if (mContext.getResources().getBoolean(R.bool.is_WSET_client)) {
                        canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - (textWidth * 2) - (gapHelpCirle * 3),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    } else {
                        canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - textWidth - (gapHelpCirle * 3),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    }
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.settings)))) {
                    if (mContext.getResources().getBoolean(R.bool.is_WSET_client)) {
                        canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - (textWidth * 3) - (gapHelpCirle * 3),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    } else {
                        canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - textWidth - (gapHelpCirle * 3),
                                vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                    }
                }
            } else {
                if (lineHeightBottomNext1 == 0) {
                    lineHeightBottomNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 6;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1, helpCircleHight, paintTextCircle);
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.bookMark)))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1, paintTextCircle);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.bookMark_));
                    canvas.drawText(mContext.getResources().getString(R.string.bookMark_), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeightBottomNext1, paintText);
                }
            }
        }
    }


    public void drawArrowLineReview(HelpVo vo) {

        Paint paint;
        Paint paintText;
        i = i + 1;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(helpLineWidth);
        paint.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        // paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeWidth(2);
        paintText.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getTextColor()));
        paintText.setAntiAlias(true);
        paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size_new));
        if (Utility.isDeviceTypeMobile(mContext))
            lineHeightReviewConst = (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4) - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
        else
            lineHeightReviewConst = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 6;
        if (Utility.isDeviceTypeMobile(mContext)) {
            if (vo.getBottomState()) {
                if (lineHeightReview == 0) {
                    lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else {
                    lineHeightReview = lineHeightReview - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst, paint);
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst, paint);
                } else {
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview, paint);
                }
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(1);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {
                    startx = viewCenterX;
                    starty = vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                    endx = viewCenterX;
                    endy = vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst;
                    canvas.drawLine(startx, starty,
                            endx, endy, paint);
                    canvas.drawLine(((endx - startx) / 2 + startx), endy,
                            ((endx - startx) / 2 + startx), endy - lineHeightReviewConst / 2, paint);

                    canvas.drawCircle(((endx - startx) / 2 + startx), endy - lineHeightReviewConst / 2, helpCircleHight, paintTextCircle);
                }
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 2) - gapHelpCirle, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - textWidth + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 3), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - textWidth + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 2) - (gapHelpCirle * 3), paintText);
                    lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.undo)))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_1), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + (gapHelpCirle * 3) + gapHelplineTxt, paintText);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview, helpCircleHight, paintTextCircle);

                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.eraser)))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview, helpCircleHight, paintTextCircle);
                }
            } else {
                if (lineHeightReview == 0) {
                    lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(1);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview, helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - gapHelpCirle - gapHelplineTxt - mContext.getResources().getDimension(R.dimen.paint_text_size),
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paintTextCircle);


                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.student_profile)))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile1));
                    canvas.drawText(mContext.getResources().getString(R.string.student_profile1), viewCenterX - textWidth - gapHelplineTxt - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile2));
                    canvas.drawText(mContext.getResources().getString(R.string.student_profile2), viewCenterX - textWidth - gapHelplineTxt - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelplineTxt + gapHelpCirle + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
        } else {
            if (lineHeightReview == 0) {
                lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
            }
            float viewCenterX;
            viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
            float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
            Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTextCircle.setStrokeWidth(1);
            paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
            paintTextCircle.setAntiAlias(true);
            paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));

            if (!(mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1))) && !(mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2))) && !(mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1))) && !(mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2)))) {

                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paint);
            } else {


                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst, paint);
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {
                    startx = viewCenterX;
                    starty = vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                    endx = viewCenterX;
                    endy = vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1)))) {
                    startarrowx = viewCenterX;
                    startarrowy = vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2)))) {
                    endarrowx = viewCenterX;
                    endarrowy = vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                }

            }
            if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.student_profile)))) {
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paintTextCircle);
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview, helpCircleHight, paintTextCircle);
                textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile1));
                canvas.drawText(mContext.getResources().getString(R.string.student_profile1), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelplineTxt, paintText);
                textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile2));
                canvas.drawText(mContext.getResources().getString(R.string.student_profile2), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1)))) {

                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - (gapHelpCirle * 8),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - (gapHelpCirle * 5),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                } else {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - (gapHelpCirle * 8),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - (gapHelpCirle * 5),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }

            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2)))) {
                canvas.drawLine(startx, starty,
                        endx, endy, paint);

                canvas.drawLine(((endx - startx) / 2 + startx), endy,
                        ((endx - startx) / 2 + startx), endy + lineHeightReviewConst / 2, paint);

                canvas.drawCircle(((endx - startx) / 2 + startx), endy + lineHeightReviewConst / 2, helpCircleHight, paintTextCircle);

            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1)))) {

                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 4), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 4) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 4) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                } else {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2)))) {

                canvas.drawLine(startarrowx, startarrowy,
                        endarrowx, endarrowy, paint);

                canvas.drawLine(((endarrowx - startarrowx) / 2 + startarrowx), endarrowy,
                        ((endarrowx - startarrowx) / 2 + startarrowx), endy + lineHeightReviewConst / 2, paint);
                canvas.drawCircle(((endarrowx - startarrowx) / 2 + startarrowx), endy + lineHeightReviewConst / 2, helpCircleHight, paintTextCircle);

            }
        }
    }

    public void drawArrowLineNextReview(HelpVo vo) {
        Paint paint;
        Paint paintText;
        i = i + 1;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(helpLineWidth);
        paint.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        // paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeWidth(1);
        paintText.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getTextColor()));
        paintText.setAntiAlias(true);
        paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size_new));
        if (Utility.isDeviceTypeMobile(mContext))
            lineHeightReviewConst = (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4) - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
        if (Utility.isDeviceTypeMobile(mContext)) {
            if (vo.getBottomState()) {
                if (lineHeightReviewNext == 0) {
                    lineHeightReviewNext = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                }

                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight(), paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight(), helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight(), paintTextCircle);

                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.done1)))) {
                    canvas.drawText(mContext.getResources().getString(R.string.done2), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + gapHelpCirle + gapHelplineTxt, paintText);
                }

            } else {
                if (lineHeightReviewNext == 0) {
                    lineHeightReviewNext = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1)))) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst, paint);
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2)))) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst, paint);
                } else
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext, paint);

                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintTextCircle);

                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1)))) {
                    startarrowx = viewCenterX;
                    startarrowy = vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst;
                } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2)))) {
                    endarrowx = viewCenterX;
                    endarrowy = vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst;
                    canvas.drawLine(startarrowx, startarrowy,
                            endarrowx, endarrowy, paint);

                    canvas.drawLine(((endarrowx - startarrowx) / 2 + startarrowx), endarrowy,
                            ((endarrowx - startarrowx) / 2 + startarrowx), endarrowy + lineHeightReviewConst / 3, paint);
                    canvas.drawCircle(((endarrowx - startarrowx) / 2 + startarrowx), endarrowy + lineHeightReviewConst / 3, helpCircleHight, paintTextCircle);
                }
                if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1)))) {

                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + gapHelpCirle + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + (gapHelpCirle * 3) + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
        } else {
            float viewCenterX;
            viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
            float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
            Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTextCircle.setStrokeWidth(2);
            paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
            paintTextCircle.setAntiAlias(true);
            paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));

            if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.eraser)))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 5;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 3),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 2), paintText);
                } else {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 3),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                }
            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.undo)))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - (textWidth) - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 4),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_1), viewCenterX - (textWidth * 2),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle), paintText);
                } else {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - (textWidth) - (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 4),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 4), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_1), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 2), paintText);
                }

            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.clear_All)))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 3;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.clear_all), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle), paintText);
                } else {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.clear_all), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 2), paintText);
                }

            } else if ((mContext.getResources().getString(vo.getHelpText()).equalsIgnoreCase(mContext.getResources().getString(R.string.done1)))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX - (gapHelpCirle * 2), vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX - (gapHelpCirle * 2), vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX - (gapHelpCirle * 2), vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 3),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.done2), viewCenterX - (textWidth * 3) - (gapHelpCirle * 3),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                } else {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.done2), viewCenterX - (textWidth * 3) - (gapHelpCirle * 2),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 4), paintText);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txthelp) {
            com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED, false);
            if(isreview){
                if(com.hurix.kitaboo.constants.utils.Utils.getSharedPreferenceBooleanValue(mContext, com.hurix.kitaboo.constants.Constants.HELPSCREEN_REQUIRED_REVIEW, false))
                    com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, false);
            }else {
                if(isReader){
                        com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, true);
                    com.hurix.kitaboo.constants.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_READER, false);
                }
            }
            Intent data = new Intent();
            data.putExtra("closeMainLayout", true);
            ((Activity) mContext).setResult(Activity.RESULT_OK, data);
            ((Activity) mContext).finish();
        }
    }
}
