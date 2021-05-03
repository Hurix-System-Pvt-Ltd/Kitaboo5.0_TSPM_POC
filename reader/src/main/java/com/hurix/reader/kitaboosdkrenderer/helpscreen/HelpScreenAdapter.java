package com.hurix.reader.kitaboosdkrenderer.helpscreen;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.downloadbook.controller.UserController;
import com.hurix.epubreader.Utility.Utils;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.common.HelpVo;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;

import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.ReaderThemeController;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by Amit Raj on 08-08-2019.
 */
public class HelpScreenAdapter extends PagerAdapter implements View.OnClickListener {

    HashMap<Integer, ArrayList<HelpVo>> hashMap;
    Context mContext;
    private Bitmap bitmap;
    private Canvas canvas;
    private int  lineHeight = 0,lineHeightBottom=0,lineHeightNext = 0,lineHeightBottomNext=0,lineHeightBottomNext1=0,lineHeightReview=0,lineHeightReviewNext=0,lineHeightReviewNext1=0,lineHeightReviewConst;
    private int helpLineWidth = 2;
    private int helpCircleHight = 10;
    int i = 1;
    private Typeface typeFace;
    ViewGroup view;
    int helpPosition;
    int gapHelplineTxt;
    int gapHelpCirle=20;
    private String PROFILE_SETTINGS_TEXT = "?";
    private SharedPreferences _sharedpreferences;
    private int screenHeight = 0;
    private boolean isReader,isreview;
    private     GradientDrawable drawable = new GradientDrawable();
    private  float startx = 0,starty=0,endx=0,endy=0,startarrowx = 0,startarrowy=0,endarrowx=0,endarrowy=0;
    private ReaderThemeSettingVo readerThemeSettingVo;

    public HelpScreenAdapter(Context context, HashMap<Integer, ArrayList<HelpVo>> hashMap,boolean mReader,boolean review) {
        this.hashMap = hashMap;
        this.mContext = context;
        isReader=mReader;
        isreview=review;
        readerThemeSettingVo = ReaderThemeController.getInstance(mContext.getApplicationContext()).getReaderThemeUserSettingVo();
        _sharedpreferences = mContext.getSharedPreferences(Constants.SHELF_PREFS_NAME, Context.MODE_PRIVATE);
        com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED, true);
        com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_READER, true);
        com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, true);
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
        ViewGroup parent = (ViewGroup) collection;
        parent.removeView((View) object);
        System.gc();
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
        final ImageView iv;
        TextView mBtnHelp;
        helpCircleHight = mContext.getResources().getInteger(R.integer.help_circle_hight);
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
            if(isreview && !Utility.isDeviceTypeMobile(mContext)){
                if (hashMap.get(position).get(0).isBookShelf())
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_review, null);
                else
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_review_next, null);
            }else {
                if (hashMap.get(position).get(0).isBookShelf())
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow, null);
                else
                    view = (ViewGroup) inflater.inflate(R.layout.helpscreen_slideshow_next, null);
            }
            iv = (ImageView) view.findViewById(R.id.imagelayout);
//            iv.setColorFilter(Color.parseColor(readerThemeSettingVo.getHelp().getOverlayPanel().getBackground()));
//            iv.setAlpha(Color.parseColor(readerThemeSettingVo.getHelp().getOverlayPanel().getOpacity()));
            mBtnHelp = (TextView) view.findViewById(R.id.txthelp);
            mBtnHelp.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getTextColor()));
            _sharedpreferences.edit().putBoolean(Constants.BOOK_SHELF_LAUNCH_FIRST_TIME, true).commit();

            // // commented for removal of database dependency from reader

           /* DBController.getInstance(mContext).getManager().setBookShelfHelpScreen(UserController
                    .getInstance(mContext).getUserVO().getUserID());*/



            /*if (hashMap.size() > 1) {
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_skip_txt)));

            }*/
            if(hashMap.size() > 1){
                if (position==0) {
                    mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_skip_txt)));

                }else {
                    mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_got_txt)));
                }
            }else {
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_got_txt)));
            }

            if (isReader) {
                mBtnHelp.setVisibility(View.VISIBLE);
                _sharedpreferences.edit().putBoolean(Constants.BOOK_PLAYER_LAUNCH_FIRST_TIME, true).commit();

                String screenSeenKey= String.valueOf(UserController
                        .getInstance(mContext).getUserVO().getUserID())+ "_"+"HelpScreenSeen";

                com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext,screenSeenKey,true);

                String screenSeeKey= String.valueOf(UserController
                        .getInstance(mContext).getUserVO().getUserID())+ "_"+"HelpScreenSeen";

                com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext,screenSeeKey,true);

                // commented for removal of database dependency from reader
                /*com.hurix.database.controller.DBController.getInstance(mContext).getManager().setBookPlayerHelpScreen(UserController
                        .getInstance(mContext).getUserVO().getUserID());*/
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
            iv.setImageBitmap(bitmap);
            if (position == 0 && hashMap.get(position).get(0).isBookShelf()) {
                addChildViews(hashMap.get(position), view, true);
            } else {
                //Collections.reverse(hashMap.get(position));
                if(isReader){
                    if (position == 0) {
                        addChildViews(hashMap.get(position), view, false);
                    } else {
                        addChildViewsNext(hashMap.get(position), view, false);
                        view.findViewById(R.id.txtViewCategories).setVisibility(View.GONE);
                    }
                }else {
                    if (position == 0) {
                        addChildViewsReview(hashMap.get(position), view, false);
                    } else {
                        addChildViewsReviewNext(hashMap.get(position), view);
                        view.findViewById(R.id.txtViewCategories).setVisibility(View.GONE);
                    }
                }
            }
            (collection).addView(view);
        }
        catch(Exception e){

        }
        return view;
    }


    public void addChildViews(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params=null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue=0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if(isBookShelf) {
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

                        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = vo.getViewLeft();
                        params.topMargin = vo.getViewTop();
                        tv.setLayoutParams(params);

                    }else
                    {
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
                            params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();
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
                            tv.setGravity(Gravity.CENTER);
                            tv.setHeight(vo.getViewHeight());
                            tv.setWidth(vo.getViewWidth());
                            params = new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
                            params.leftMargin = vo.getViewLeft();
                            params.topMargin = vo.getViewTop();

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

                view.addView(tv, params);
                view.invalidate();
                drawArrowLine(vo,isBookShelf);
            }

        }

    }
    public void addChildViewsNext(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params=null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue=0;
                try {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final DisplayMetrics metrics = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(metrics);
                    int screenSize = mContext.getResources().getConfiguration().screenLayout &
                            Configuration.SCREENLAYOUT_SIZE_MASK;
                    int density = mContext.getResources().getDisplayMetrics().densityDpi;
                    if(isBookShelf) {
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

                    }else
                    {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                view.addView(tv, params);
                view.invalidate();
                drawArrowLineNext(vo);
            }

        }


    }


    public void addChildViewsReview(ArrayList<HelpVo> mViewColl, ViewGroup view, boolean isBookShelf) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params=null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                int sizeMinimizeValue=0;
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

                        if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString())) || (mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString()))){
                            //tv.setPadding(4, 4, 4, 4);
                            if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))){
                                   /* drawable.setShape(GradientDrawable.OVAL);
                                    drawable.setStroke(2, Color.parseColor("#49ef1c"));*/
                                tv.setGravity(Gravity.LEFT);
                                params.topMargin = vo.getViewTop() - 12;
                                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                            }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString()))) {
                                  /*  drawable.setShape(GradientDrawable.OVAL);
                                    drawable.setStroke(2, Color.parseColor("#ff1818"));*/
                                tv.setGravity(Gravity.LEFT);
                                params.topMargin = vo.getViewTop() - 12;
                                tv.setTextColor( Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
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
                        if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString())) || (mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString()))){
                            //tv.setPadding(4, 4, 4, 4);
                            if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))){

                                params.leftMargin = vo.getViewLeft()-12;
                                params.topMargin = vo.getViewTop() - 10;
                                tv.setTextColor(Color.parseColor("#49ef1c"));
                            }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString()))) {

                                params.leftMargin = vo.getViewLeft()-12;
                                params.topMargin = vo.getViewTop() - 10;
                                tv.setTextColor( Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.addView(tv, params);
                view.invalidate();
                drawArrowLineReview(vo);
            }
        }
    }



    @SuppressLint("WrongConstant")
    public void addChildViewsReviewNext(ArrayList<HelpVo> mViewColl, ViewGroup view) {

        for (HelpVo vo : mViewColl) {
            //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            if (vo.getViewWidth() != 0 && vo.getViewHeight() != 0) {
                FrameLayout.LayoutParams params=null;
                getReaderFont();
                TextView tv = new TextView(mContext);
                if(vo.getViewText().equalsIgnoreCase("Done"))
                    tv.setTypeface(null);
                else
                    tv.setTypeface(typeFace);
                tv.setAllCaps(false);
                tv.setText(vo.getViewText());
                tv.setTextColor(Color.parseColor(readerThemeSettingVo.getHelp().getLineColor()));
                int sizeMinimizeValue=0;
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

    public void drawArrowLine(HelpVo vo, boolean isBookshelf) {
        if (isBookshelf) {
            Paint paint;
            Paint paintText;
            i = i + 1;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(helpLineWidth);
            paint.setColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getHelp().getLineColor()));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);
            // paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setStrokeWidth(1);
            paintText.setColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getHelp().getTextColor()));
            paintText.setAntiAlias(true);
            paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size_new));
            if (lineHeight == 0) {
                lineHeight = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
            } else {
                lineHeight = lineHeight - (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) /5);
            }
            if (!vo.getBottomState()) {
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight() + 4,
                        viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight, helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), (viewCenterX - textWidth) - gapHelpCirle * 2 - gapHelplineTxt,
                        vo.getViewTop() + vo.getViewHeight() + lineHeight + gapHelplineTxt, paintTextCircle);
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.search).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.help_screen_search_text_new));
                    canvas.drawText(mContext.getResources().getString(R.string.help_screen_search_text_new), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeight + gapHelplineTxt, paintText);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.help_screen_search_text_new_));
                    canvas.drawText(mContext.getResources().getString(R.string.help_screen_search_text_new_), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + lineHeight + gapHelplineTxt, paintText);
                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.add_new).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.add_new_content));
                    canvas.drawText(mContext.getResources().getString(R.string.add_new_content), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeight + gapHelplineTxt, paintText);
                }
                if(Utility.isDeviceTypeMobile(mContext)){
                    if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.profile).toString()))) {
                        textWidth = paintText.measureText(mContext.getResources().getString(R.string.more_setting));
                        canvas.drawText(mContext.getResources().getString(R.string.more_setting_), (viewCenterX - textWidth * 2 + gapHelpCirle + gapHelplineTxt),
                                vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeight + gapHelplineTxt, paintText);

                        canvas.drawText(mContext.getResources().getString(R.string.more_setting_1), (viewCenterX - textWidth * 2 + gapHelpCirle + gapHelplineTxt),
                                vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + lineHeight + gapHelplineTxt , paintText);

                        canvas.drawText(mContext.getResources().getString(R.string.more_setting_2), (viewCenterX - textWidth * 2 + gapHelpCirle + gapHelplineTxt),
                                vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) * 3 + lineHeight + gapHelplineTxt , paintText);
                    }
                }else{
                    if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.profile).toString()))) {
                        textWidth = paintText.measureText(mContext.getResources().getString(R.string.profile_));
                        canvas.drawText(mContext.getResources().getString(R.string.profile_), (viewCenterX - textWidth) - gapHelpCirle,
                                vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeight + gapHelplineTxt, paintText);
                    }
                }

            } else {
                if (vo.getViewText().equalsIgnoreCase(PlayerUIConstants.TB_BOOK_DATA_IC_TEXT)) {
                    float viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                    float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - lineHeight, paint);
                    Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paintTextCircle.setStrokeWidth(2);
                    paintTextCircle.setColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getHelp().getPointerColor()));
                    paintTextCircle.setAntiAlias(true);
                    paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - lineHeight, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX,
                            vo.getViewTop() - lineHeight - 20, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX,
                            vo.getViewTop() - lineHeight - 20 + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                } else {
                    float viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                    float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - lineHeight, paint);
                    Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paintTextCircle.setStrokeWidth(2);
                    paintTextCircle.setColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getHelp().getPointerColor()));
                    paintTextCircle.setAntiAlias(true);
                    paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - lineHeight, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth,
                            vo.getViewTop() - lineHeight - 20, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth,
                            vo.getViewTop() - lineHeight - 20 + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
        } else {
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

            if (vo.getBottomState()) {
                if (lineHeightBottom == 0) {
                    lineHeightBottom = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else {
                    lineHeightBottom = lineHeightBottom - (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 7);
                }

                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottom, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightBottom, helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + 20,
                        vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + 20, paintTextCircle);

                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.search).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_));
                    canvas.drawText(mContext.getResources().getString(R.string.search_), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom  + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.search_1), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + gapHelplineTxt+ (gapHelpCirle*2) + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);

                }
                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.toc).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.table_Contents));
                    canvas.drawText(mContext.getResources().getString(R.string.table_Contents_), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);
                }

                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.my_data).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.my_data_1));
                    canvas.drawText(mContext.getResources().getString(R.string.my_data_1), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.my_data_2));
                    canvas.drawText(mContext.getResources().getString(R.string.my_data_2), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);
                }

                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.pen).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.pen_content1));
                    canvas.drawText(mContext.getResources().getString(R.string.pen_content1), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.pen_content2));
                    canvas.drawText(mContext.getResources().getString(R.string.pen_content2), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);
                }

                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.font_setting).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.font_setting_));
                    canvas.drawText(mContext.getResources().getString(R.string.font_setting_), (viewCenterX + gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottom + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                }
            }
            else {
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
                if (lineHeight == 0) {
                    lineHeight = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 7;
                } else {
                    lineHeight = lineHeight - (com.hurix.commons.utils.Utils.getDeviceHeight(mContext) /9);
                }

                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight() + 4,
                        viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight, paint);
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawCircle(viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight, helpCircleHight, paintTextCircle);
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.profile).toString()))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - gapHelpCirle - gapHelplineTxt,
                            vo.getViewTop() + vo.getViewHeight() + lineHeight + gapHelplineTxt, paintTextCircle);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.profile_));
                    canvas.drawText(mContext.getResources().getString(R.string.profile_), (viewCenterX - textWidth) - gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + mContext.getResources().getDimension(R.dimen.paint_text_size) + lineHeight + gapHelplineTxt, paintText);
                }
                else  if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.home).toString()))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + lineHeight + gapHelplineTxt, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.home_), viewCenterX + gapHelpCirle,
                            vo.getViewTop() + vo.getViewHeight() + lineHeight + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }

            }
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
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelplineTxt - textWidth - mContext.getResources().getDimension(R.dimen.paint_text_size),
                        vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + 20, paintTextCircle);


                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.search).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_));
                    canvas.drawText(mContext.getResources().getString(R.string.search_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.search_1), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + (gapHelplineTxt*4) + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.font_setting).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.font_setting_));
                    canvas.drawText(mContext.getResources().getString(R.string.font_setting_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                }else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.addnote).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.addnote_));
                    canvas.drawText(mContext.getResources().getString(R.string.addnote_), viewCenterX - textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.thumbnail).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_1), viewCenterX - gapHelplineTxt - (textWidth * 2) + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_2), viewCenterX - gapHelplineTxt - (textWidth * 2) + gapHelpCirle ,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);

                }
                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.clearall).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - (textWidth*2) + (gapHelpCirle * 4),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                }
                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.settings).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - (textWidth*2) + (gapHelpCirle * 4),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
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
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.bookMark).toString()))) {
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




                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.search).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.search_));
                    canvas.drawText(mContext.getResources().getString(R.string.search_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.search_1), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + (gapHelplineTxt*2) + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.font_setting).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.font_setting_));
                    canvas.drawText(mContext.getResources().getString(R.string.font_setting_), (viewCenterX - textWidth - gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);

                }else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.addnote).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.addnote_));
                    canvas.drawText(mContext.getResources().getString(R.string.addnote_), viewCenterX - textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);

                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.thumbnail).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_1), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.thumbnail_2), viewCenterX - gapHelplineTxt - (textWidth * 2) + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) * 2 + gapHelplineTxt, paintText);

                }
                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.clearall).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.clearall_), viewCenterX - gapHelplineTxt - textWidth - (gapHelpCirle * 3),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                }
                else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.settings).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.settings_), viewCenterX - gapHelplineTxt - textWidth - (gapHelpCirle * 3),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightBottomNext + mContext.getResources().getDimension(R.dimen.paint_text_size) + gapHelplineTxt, paintText);
                }
            }else{
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
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.bookMark).toString()))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightBottomNext1 , paintTextCircle);
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
        if(Utility.isDeviceTypeMobile(mContext))
            lineHeightReviewConst=(com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4) - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
        else
            lineHeightReviewConst=com.hurix.commons.utils.Utils.getDeviceHeight(mContext)/6;
        if(Utility.isDeviceTypeMobile(mContext)) {
            if(vo.getBottomState()) {
                if (lineHeightReview == 0) {
                    lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else {
                    lineHeightReview = lineHeightReview - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))){
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst , paint);
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString())) ){
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst , paint);
                }else {
                    canvas.drawLine(viewCenterX, vo.getViewTop(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview , paint);
                }
                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(1);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))){
                    startx=viewCenterX;
                    starty=vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst ;
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString())) ){
                    endx=viewCenterX;
                    endy=vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst ;
                    canvas.drawLine(startx, starty ,
                            endx, endy, paint);
                    canvas.drawLine(((endx-startx)/2+startx), endy ,
                            ((endx-startx)/2+startx), endy-lineHeightReviewConst/2 , paint);

                    canvas.drawCircle(((endx-startx)/2+startx), endy-lineHeightReviewConst/2 , helpCircleHight, paintTextCircle);
                }
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX ,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 2) - gapHelpCirle , paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - textWidth + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 3), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - textWidth + (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReviewConst - (textWidth * 2) - (gapHelpCirle * 3) , paintText);
                    lineHeightReview = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.undo).toString()))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview , paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_1), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + (gapHelpCirle * 3) + gapHelplineTxt, paintText);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview, helpCircleHight, paintTextCircle);

                } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.eraser).toString()))) {
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview , paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() - lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() - lineHeightReview, helpCircleHight, paintTextCircle);
                }
            }else{
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



                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.student_profile).toString()))) {
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile1));
                    canvas.drawText(mContext.getResources().getString(R.string.student_profile1), viewCenterX - textWidth - gapHelplineTxt - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelpCirle + gapHelplineTxt, paintText);
                    textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile2));
                    canvas.drawText(mContext.getResources().getString(R.string.student_profile2), viewCenterX - textWidth - gapHelplineTxt - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelplineTxt + gapHelpCirle + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
        }
        else
        {
            if(lineHeightReview == 0){
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

            if(!(mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString())) && !(mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString())) && !(mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString())) && !(mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2).toString()))){

                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paint);
            }else {


                canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight() ,
                        viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst , paint);
                if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))){
                    startx=viewCenterX;
                    starty=vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString())) ){
                    endx=viewCenterX;
                    endy=vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString())) ){
                    startarrowx=viewCenterX;
                    startarrowy=vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2).toString())) ){
                    endarrowx=viewCenterX;
                    endarrowy=vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst;
                }

            }
            if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.student_profile).toString()))) {
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview, paintTextCircle);
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReview , helpCircleHight, paintTextCircle);
                textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile1));
                canvas.drawText(mContext.getResources().getString(R.string.student_profile1), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview + gapHelplineTxt , paintText);
                textWidth = paintText.measureText(mContext.getResources().getString(R.string.student_profile2));
                canvas.drawText(mContext.getResources().getString(R.string.student_profile2), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReview + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size) , paintText);
            }
            else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_1).toString()))) {

                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT  ) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - (gapHelpCirle * 8),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - (gapHelpCirle * 5),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }else{

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.marker1), viewCenterX - (gapHelpCirle * 8),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.marker2), viewCenterX - (gapHelpCirle * 5),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }

            } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.marker_2).toString()))) {
                canvas.drawLine(startx, starty,
                        endx , endy, paint);

                canvas.drawLine(((endx-startx)/2+startx), endy ,
                        ((endx-startx)/2+startx), endy+lineHeightReviewConst/2 , paint);

                canvas.drawCircle(((endx-startx)/2+startx), endy+lineHeightReviewConst/2 , helpCircleHight, paintTextCircle);

            }
            else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString()))) {

                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 4), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 4) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 4) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }else{

                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2), paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 2) + (gapHelpCirle * 2) + gapHelplineTxt, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX + textWidth - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + gapHelplineTxt + (textWidth * 2) + (gapHelpCirle * 2) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
            else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2).toString()))) {

                canvas.drawLine(startarrowx, startarrowy,
                        endarrowx , endarrowy, paint);

                canvas.drawLine(((endarrowx-startarrowx)/2+startarrowx), endarrowy ,
                        ((endarrowx-startarrowx)/2+startarrowx), endy+lineHeightReviewConst/2 , paint);
                canvas.drawCircle(((endarrowx-startarrowx)/2+startarrowx), endy+lineHeightReviewConst/2 , helpCircleHight, paintTextCircle);

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
        if(Utility.isDeviceTypeMobile(mContext))
            lineHeightReviewConst=(com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4) - com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 9;
        if(Utility.isDeviceTypeMobile(mContext)) {
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
                canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() , helpCircleHight, paintTextCircle);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight()  , paintTextCircle);

                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.done1).toString()))) {
                    canvas.drawText(mContext.getResources().getString(R.string.done2), viewCenterX + gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + gapHelpCirle + gapHelplineTxt , paintText);
                }

            } else {
                if (lineHeightReviewNext == 0) {
                    lineHeightReviewNext = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                }
                float viewCenterX;
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString())) ){
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst, paint);
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2).toString())) ){
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst, paint);
                }else
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext, paint);

                Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintTextCircle.setStrokeWidth(2);
                paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
                paintTextCircle.setAntiAlias(true);
                paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX + textWidth + gapHelpCirle,
                        vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + mContext.getResources().getDimension(R.dimen.paint_text_size), paintTextCircle);

                if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString())) ){
                    startarrowx=viewCenterX;
                    startarrowy=vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst;
                }else if((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow2).toString())) ){
                    endarrowx=viewCenterX;
                    endarrowy=vo.getViewTop() + vo.getViewHeight() + lineHeightReviewConst;
                    canvas.drawLine(startarrowx, startarrowy,
                            endarrowx, endarrowy, paint);

                    canvas.drawLine(((endarrowx-startarrowx)/2+startarrowx), endarrowy ,
                            ((endarrowx-startarrowx)/2+startarrowx), endarrowy+lineHeightReviewConst/3 , paint);
                    canvas.drawCircle(((endarrowx-startarrowx)/2+startarrowx), endarrowy+lineHeightReviewConst/3 , helpCircleHight, paintTextCircle);
                }
                if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.arrow1).toString()))) {

                    canvas.drawText(mContext.getResources().getString(R.string.arrow_1), viewCenterX ,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + gapHelpCirle + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.arrow_2), viewCenterX ,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewConst + (textWidth * 5) + (gapHelpCirle * 3) + gapHelplineTxt + mContext.getResources().getDimension(R.dimen.paint_text_size), paintText);
                }
            }
        }
        else
        {
            float viewCenterX;
            viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
            float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
            Paint paintTextCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintTextCircle.setStrokeWidth(2);
            paintTextCircle.setColor(Color.parseColor(readerThemeSettingVo.getHelp().getPointerColor()));
            paintTextCircle.setAntiAlias(true);
            paintTextCircle.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));

            if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.eraser).toString()))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 5;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 3),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 2), paintText);
                }else{
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.eraser_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 3),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                }
            } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.undo).toString()))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 4;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - (textWidth) - gapHelpCirle,
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_), viewCenterX - (textWidth * 2) - (gapHelpCirle * 4),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.undo_1), viewCenterX - (textWidth * 2),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle), paintText);
                }else{
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

            } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.clear_All).toString()))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 3;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.clear_all), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle), paintText);
                }else{
                    canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX, vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 2),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.clear_all), viewCenterX - (textWidth * 2) - (gapHelpCirle),
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 2), paintText);
                }

            } else if ((mContext.getResources().getString(vo.getHelpText()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.done1).toString()))) {
                lineHeightReviewNext1 = com.hurix.commons.utils.Utils.getDeviceHeight(mContext) / 2;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    canvas.drawLine(viewCenterX - (gapHelpCirle * 2), vo.getViewTop() + vo.getViewHeight(),
                            viewCenterX - (gapHelpCirle * 2), vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paint);
                    canvas.drawCircle(viewCenterX - (gapHelpCirle * 2), vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, helpCircleHight, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth - (gapHelpCirle * 3),
                            vo.getViewTop() - vo.getViewHeight() + lineHeightReviewNext1, paintTextCircle);
                    canvas.drawText(mContext.getResources().getString(R.string.done2), viewCenterX - (textWidth * 3) - (gapHelpCirle * 3) ,
                            vo.getViewTop() + vo.getViewHeight() + lineHeightReviewNext1 - textWidth - (gapHelpCirle * 3), paintText);
                }else{
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
            com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED, false);
            if(isreview){
                if(com.hurix.commons.utils.Utils.getSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, false))
                    com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, false);
            }else {
                if(isReader){
                    com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_REVIEW, true);
                    com.hurix.commons.utils.Utils.insertSharedPreferenceBooleanValue(mContext, Constants.HELPSCREEN_REQUIRED_READER, false);
                }
            }
            Intent data = new Intent();
            data.putExtra("closeMainLayout", true);
            ((Activity) mContext).setResult(Activity.RESULT_OK,data);
            ((Activity) mContext).finish();
        }
    }
}
