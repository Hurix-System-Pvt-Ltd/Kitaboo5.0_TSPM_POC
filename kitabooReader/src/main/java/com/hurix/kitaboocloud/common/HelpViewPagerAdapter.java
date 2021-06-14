package com.hurix.kitaboocloud.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboo.constants.PlayerUIConstants;
import com.hurix.kitaboo.iconify.Typefaces;
import com.hurix.kitaboocloud.R;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harish.edara on 4/14/2016.
 */
public class HelpViewPagerAdapter extends PagerAdapter implements  View.OnClickListener{

    HashMap<Integer, ArrayList<HelpVo>> hashMap;
    Context mContext;
    private Bitmap bitmap;
    private Canvas canvas;
    private int lineHeight = 150;
    private int helpLineWidth = 5;
   private int helpCircleHight = 10;
    int i = 0;
    private Typeface typeFace;
    ViewGroup view;
    int helpPosition;
    int gapHelplineTxt;
    private String PROFILE_SETTINGS_TEXT = "?";
    private SharedPreferences _sharedpreferences;
    public HelpViewPagerAdapter(Context context,HashMap<Integer, ArrayList<HelpVo>> hashMap){
        this.hashMap = hashMap;
        this.mContext = context;
        _sharedpreferences = mContext.getSharedPreferences(Constants.SHELF_PREFS_NAME, Context.MODE_PRIVATE);
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
    public void destroyItem(ViewGroup collection, int position, Object object)
    {
        ViewGroup parent = collection;
        parent.removeView((View) object);
        System.gc();
    }
    @Override
    public Object instantiateItem(final ViewGroup collection, final int position)
    {
        final ImageView iv;
        TextView mBtnHelp;
        lineHeight =  mContext.getResources().getInteger(com.hurix.kitaboo.constants.R.integer.help_line_hight);
        helpLineWidth = mContext.getResources().getInteger(com.hurix.kitaboo.constants.R.integer.help_line_width);
        helpCircleHight = mContext.getResources().getInteger(com.hurix.kitaboo.constants.R.integer.help_circle_hight);
        helpPosition = position;
        i = 0;
        try{
            WindowManager wm = (WindowManager) collection.getContext().getSystemService(Context.WINDOW_SERVICE);
            final DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int screenSize = mContext.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;
            int density= mContext.getResources().getDisplayMetrics().densityDpi;
            if(screenSize ==  Configuration.SCREENLAYOUT_SIZE_XLARGE )
            {
                lineHeight= 150;
            }
            if(screenSize ==   Configuration.SCREENLAYOUT_SIZE_XLARGE && density == DisplayMetrics.DENSITY_MEDIUM)
            {
                lineHeight= 100;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    lineHeight= 80;
                }

                helpLineWidth = 3;
                gapHelplineTxt = 25;
                helpCircleHight = 5;
            }
            else if(screenSize ==   Configuration.SCREENLAYOUT_SIZE_LARGE && density == DisplayMetrics.DENSITY_TV)
            {
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    lineHeight= 60;
                }
                gapHelplineTxt = 30;

            }
            else if(screenSize ==   Configuration.SCREENLAYOUT_SIZE_NORMAL )
            {
                lineHeight= 80;
                if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    lineHeight= 60;
                }
                gapHelplineTxt = 30;

            }
            else{
                gapHelplineTxt = 50;
            }
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (ViewGroup)inflater.inflate(R.layout.helpscreen_slideshow, null);
            iv = (ImageView) view.findViewById(R.id.imagelayout);
            mBtnHelp = (TextView) view.findViewById(R.id.txthelp);
            _sharedpreferences.edit().putBoolean(Constants.BOOK_SHELF_LAUNCH_FIRST_TIME, true).commit();

            Intent intent = new Intent();
            intent.putExtra("Action","SET_BOOKSHELF_HELPSCREEN");
            intent.setAction("DBCONTROLLER_RECIVER");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            if(hashMap.size() > 1)
            {
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_skip_txt)));

            }
            if(position !=0)
            {
                mBtnHelp.setVisibility(View.VISIBLE);
                mBtnHelp.setText(Html.fromHtml(mContext.getResources().getString(R.string.help_got_txt)));
                _sharedpreferences.edit().putBoolean(Constants.BOOK_PLAYER_LAUNCH_FIRST_TIME, true).commit();


                Intent intentPlayer = new Intent();
                intentPlayer.putExtra("Action","SET_BOOKPLAYER_HELPSCREEN");
                intentPlayer.setAction("DBCONTROLLER_RECIVER");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentPlayer);
            }

            mBtnHelp.setOnClickListener(this);

            try {
                System.gc();
                bitmap = Bitmap.createBitmap(metrics.widthPixels,metrics.heightPixels, Bitmap.Config.ARGB_8888);
            } catch (Exception e) {
                System.gc();
                bitmap = Bitmap.createBitmap(metrics.widthPixels,metrics.heightPixels, Bitmap.Config.ARGB_8888);
            } finally {

            }
            canvas = new Canvas(bitmap);
            iv.setImageBitmap(bitmap);
            if(position == 0 && hashMap.get(position).get(0).isBookShelf())
            {
                addChildViews(hashMap.get(position), view , true);
            }
            else{
                addChildViews(hashMap.get(position), view , false);
                view.findViewById(R.id.txtViewCategories).setVisibility(View.GONE);
            }
            (collection).addView(view);
        }catch(Exception e){

        }
        return view;
    }


    public void addChildViews(ArrayList<HelpVo> mViewColl,ViewGroup view , boolean isBookShelf)
    {
        for (HelpVo vo : mViewColl)
        {
          //  typeFace = Typefaces.get(mContext, mContext.getResources().getString(R.string.kitabooreader_font_file_name));
            getReaderFont();
            TextView tv = new TextView(mContext);
            tv.setTypeface(typeFace);
            tv.setAllCaps(false);
            tv.setText(vo.getViewText());
            tv.setTextSize(32);
            tv.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(vo.getViewWidth(), vo.getViewHeight());
            params.leftMargin = vo.getViewLeft();
            params.topMargin = vo.getViewTop();
            tv.setLayoutParams(params);
            //mParetnView.addView(tv, params);
            view.addView(tv, params);
            view.invalidate();
            drawArrowLine(vo);

        }
        if(isBookShelf)
        {
            final TextView txtViewCategories = (TextView) view.findViewById(R.id.txtViewCategories);
            txtViewCategories.setVisibility(View.VISIBLE);
            txtViewCategories.setTypeface(typeFace);
            txtViewCategories.setAllCaps(false);
            txtViewCategories.setText("3");
            txtViewCategories.setGravity(Gravity.CENTER);
            txtViewCategories.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Removing layout listener to avoid multiple calls
                    int[] location = new int[2];
                    txtViewCategories.getLocationOnScreen(location);
                    Paint paint;
                    Paint paintText;
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setStrokeWidth(helpLineWidth);
                    paint.setColor(mContext.getResources().getColor(R.color.dictionary_popup_bg));
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    paint.setAntiAlias(true);
                    paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
                    paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paintText.setStrokeWidth(2);
                    paintText.setColor(mContext.getResources().getColor(R.color.dictionary_popup_bg));
                    //paintText.setStyle(Paint.Style.FILL_AND_STROKE);
                    paintText.setAntiAlias(true);
                    paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
                    float viewCenterX = location[0] + txtViewCategories.getWidth() / 2;
                    canvas.drawLine(viewCenterX, location[1] + txtViewCategories.getHeight(),
                            viewCenterX, location[1] + txtViewCategories.getHeight() + lineHeight * i, paint);
                    canvas.drawCircle(viewCenterX, location[1] + txtViewCategories.getHeight() + lineHeight * i, helpCircleHight, paintText);
                    canvas.drawText(mContext.getResources().getString(R.string.help_view_categories), viewCenterX,
                            location[1] + txtViewCategories.getHeight() + lineHeight * i + gapHelplineTxt, paintText);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        txtViewCategories.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        txtViewCategories.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
        //imgView.setImageBitmap(bitmap);

    }

    private void getReaderFont() {

        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if(fontfile!=null && !fontfile.isEmpty()){
            typeFace   = Typefaces.get(mContext,fontfile);
        } else{
            typeFace   = Typefaces.get(mContext,"kitabooread.ttf");
        }

    }

    public void drawArrowLine(HelpVo vo) {
        Paint paint;
        Paint paintText;
        i = i + 1;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(helpLineWidth);
        paint.setColor(mContext.getResources().getColor(R.color.dictionary_popup_bg));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeWidth(2);
        paintText.setColor(mContext.getResources().getColor(R.color.dictionary_popup_bg));
        paintText.setAntiAlias(true);
        paintText.setTextSize(mContext.getResources().getDimension(R.dimen.paint_text_size));
        if (!vo.getBottomState())
        {
            float viewCenterX;
            if (vo.getViewText().equalsIgnoreCase("Ƥ"))
            {
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 3;
            }
            else {
                viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
            }
            float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
            canvas.drawLine(viewCenterX, vo.getViewTop() + vo.getViewHeight(),
                    viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight * i, paint);
            canvas.drawCircle(viewCenterX, vo.getViewTop() + vo.getViewHeight() + lineHeight * i,helpCircleHight,paintText);
            canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth,
                    vo.getViewTop() + vo.getViewHeight() + lineHeight * i + gapHelplineTxt, paintText);

        }
        else{
            if(vo.getViewText().equalsIgnoreCase(PlayerUIConstants.TB_BOOK_DATA_IC_TEXT))
            {
                float viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - lineHeight, paint);
                canvas.drawCircle(viewCenterX, vo.getViewTop() - lineHeight, helpCircleHight, paintText);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX ,
                        vo.getViewTop() - lineHeight - 20, paintText);
            }
            else {
                float viewCenterX = vo.getViewLeft() + vo.getViewWidth() / 2;
                float textWidth = paintText.measureText(mContext.getResources().getString(vo.getHelpText()));
                canvas.drawLine(viewCenterX, vo.getViewTop(),
                        viewCenterX, vo.getViewTop() - lineHeight, paint);
                canvas.drawCircle(viewCenterX, vo.getViewTop() - lineHeight, helpCircleHight, paintText);
                canvas.drawText(mContext.getResources().getString(vo.getHelpText()), viewCenterX - textWidth,
                        vo.getViewTop() - lineHeight - 20, paintText);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txthelp) {
            ((Activity)mContext).finish();
        }
    }
}
