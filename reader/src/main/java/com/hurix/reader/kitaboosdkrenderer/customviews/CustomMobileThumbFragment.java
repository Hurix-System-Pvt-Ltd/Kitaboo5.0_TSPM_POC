package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.thumbnails.QuickAction;
import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.customui.views.KEditText;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by amitraj.sharma on 2/3/2018.
 */
public class CustomMobileThumbFragment extends Fragment implements OnClickListener, OnItemClickListener,
        OnTouchListener {

    private GridView _thumbnailsGallery;
    private CustomMobileThumbnailsAdapter _adapter;
    private Button _pageHistPrevious = null, _pageHistNext = null, _imgbtnGoArrow = null;
    private Typeface _typeFace;
    //private VerticalSeekBar _seekBar;
    private View _anchorView;
    private QuickAction _quickActionGotoPage;
    private KEditText _editPageNum;
    private RelativeLayout _etPageNavHolder;
    private String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    private static ArrayList<ThumbnailVO> mThumbnailColl;
    private boolean _isGridViewScrollByUser;
    RelativeLayout _mainRelativeLayout, _bottombarContainer;
    private CustomThumbnailsListner mListner;
    private int currentpageidforselection = 0;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private TextView mTotalPageCount;
    private SeekBar mSeekbar;
    private int mSeekBarLastPosition;
    private boolean isFromConfigurationChanged;


    public CustomMobileThumbFragment() {

    }
  /*  public void setThemeColor(ThemeUserSettingVo _themeUserSettingVo){
        this.themeUserSettingVo=_themeUserSettingVo;
    }*/


    @SuppressLint("ValidFragment")
    public CustomMobileThumbFragment(String toc, ArrayList<ThumbnailVO> thumbnailVOc, int currentviewpagerindex, ReaderThemeSettingVo themeUserSettingVo) {
        //TabThumbnailFragment fragmentFirst = new TabThumbnailFragment();
       // Bundle args = new Bundle();
        mThumbnailColl = thumbnailVOc;
        this.readerThemeSettingVo = themeUserSettingVo;
        currentpageidforselection = currentviewpagerindex;
        //args.putString("data", toc.toString());
       // fragmentFirst.setArguments(args);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_thumbnail_layout_mobile, parent, false);
        initView(rootView);
        return rootView;

    }

    private void initView(View view) {
        initViews(view);
        setUpIconFonts();
        setData();
    }

    BroadcastReceiver onPageThumbnailConfigChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BCAST_CONFIGCHANGED)) {
                if (_quickActionGotoPage != null && _quickActionGotoPage.isShowing()) {
                    _quickActionGotoPage.dismiss();
                }
                if (_editPageNum != null && getActivity() != null) {
                    hideKeyboard(getActivity());
                }
            }
        }
    };

    public void setData() {
        _adapter.setdata(mThumbnailColl, currentpageidforselection, readerThemeSettingVo);
        if (_thumbnailsGallery.getAdapter() == null) {
            _thumbnailsGallery.setAdapter(_adapter);
        } else {
            _adapter.notifyDataSetChanged();
        }
        setScrollBarToCurrentpage();
        //setPageNo();

    }

    private void setScrollBarToCurrentpage() {
        if (mThumbnailColl != null) {
            for (int position = 0; position < mThumbnailColl.size(); position++) {
                if (currentpageidforselection == position) {
                    _thumbnailsGallery.setSelection(position);
                    _thumbnailsGallery.setSelected(true);
                    mSeekbar.setProgress(position);
                    _editPageNum.setText(mThumbnailColl.get(currentpageidforselection).getFolioID());
                    mSeekBarLastPosition = position;
                }
            }
        }
    }

    private void initViews(View view) {
        _thumbnailsGallery = (GridView) view.findViewById(R.id.thumbnailsGallerymobile);
        _thumbnailsGallery.setSmoothScrollbarEnabled(false);
        _thumbnailsGallery.setVerticalScrollBarEnabled(false);
        _editPageNum = (KEditText) view.findViewById(R.id.etPageNavmobile);
        _editPageNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Field f = null;
                try {
                    f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    try {
                        f.set(_editPageNum, R.drawable.edit_text_cursor);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });

        _editPageNum.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    jumpToPageAction(_editPageNum.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        _pageHistPrevious = (Button) view.findViewById(R.id.btnHistoryPreviousmobile);
        _pageHistPrevious.setOnClickListener(this);
        _anchorView = view.findViewById(R.id.gotoPopupAnchormobile);
        _pageHistNext = (Button) view.findViewById(R.id.btnHistoryNextmobile);
        _pageHistNext.setOnClickListener(this);
        _adapter = new CustomMobileThumbnailsAdapter(getActivity().getBaseContext(), mThumbnailColl, readerThemeSettingVo);
        _thumbnailsGallery.setOnItemClickListener(this);
        _mainRelativeLayout = (RelativeLayout) view.findViewById(R.id.pagenav_mainIDmobile);
        _bottombarContainer = (RelativeLayout) view.findViewById(R.id.controllersContainermobile);
        _bottombarContainer.setVisibility(View.VISIBLE);
        mTotalPageCount = (TextView) view.findViewById(R.id.total_page);
        mTotalPageCount.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTextColor()));
        mSeekbar = (SeekBar) view.findViewById(R.id.seekbarProgress);
        mSeekbar.setMax(mThumbnailColl.size());
        setupSeekBar();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            _thumbnailsGallery.setNumColumns(2);
        } else {
            _thumbnailsGallery.setNumColumns(3);
        }
       /* Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.thumb_grid_animation);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .1f, .1f);
        _thumbnailsGallery.setLayoutAnimation(controller);*/
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    _thumbnailsGallery.setSelection(progress);
                    _thumbnailsGallery.setSelected(true);
                    mSeekBarLastPosition = progress;
                }
            }
        });
        setHistoryButtonNavigationmode();
        mTotalPageCount.setText(String.format("/%1$s", mThumbnailColl.size()));

        _thumbnailsGallery.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isFromConfigurationChanged) {
                    // mSeekbar.setProgress(mSeekBarLastPosition);
                    _thumbnailsGallery.setSelection(mSeekBarLastPosition);
                    _thumbnailsGallery.setSelected(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isFromConfigurationChanged = false;
                        }
                    }, 1000);
                } else {
                    mSeekbar.setProgress(firstVisibleItem);
                    mSeekBarLastPosition = firstVisibleItem;
                    if (firstVisibleItem >= totalItemCount - 6) {
                        mSeekbar.setProgress(totalItemCount);
                    }
                }

            }
        });
        if (getResources().getBoolean(R.bool.is_BE_Publishing)){
            _editPageNum.setTextColor(Color.BLACK);
            mTotalPageCount.setTextColor(Color.BLACK);
        }

    }

    private GradientDrawable getSeekBarGradient(int itemColor) {
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{itemColor, itemColor});
        gradient.setShape(GradientDrawable.OVAL);
        gradient.setSize(getResources().getInteger(com.hurix.epubreader.R.integer.seekbar_thumb_size), getResources().getInteger(com.hurix.epubreader.R.integer.seekbar_thumb_size));
        gradient.setStroke(4, itemColor);
        return gradient;
    }
    private void setupSeekBar() {
        int colornew = Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSliderFilledColor());
        mSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{colornew, colornew});
        gradient.setShape(GradientDrawable.OVAL);
        gradient.setSize(40, 40);
        gradient.setStroke(0, Color.BLACK);

        mSeekbar.setThumb(getSeekBarGradient(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSliderColor())));
        mSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.thumbnail_progress_seek));


        if (mListner != null)
            mListner.onSeekbarViewCreated(mSeekbar);

    }

    public void setHistoryButtonNavigationmode() {
        if (mListner != null) {
            mListner.onPageHistoryButtonsCreated(_pageHistNext, _pageHistPrevious);
        }
    }

    private void setUpIconFonts() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            _typeFace = Typefaces.get(getActivity().getBaseContext(), fontfile);
        } else {
            _typeFace = Typefaces.get(getActivity().getBaseContext(), "kitabooread.ttf");
        }

        _pageHistPrevious.setTypeface(_typeFace);
        _pageHistPrevious.setAllCaps(false);
        _pageHistPrevious.setText(PlayerUIConstants.TG_HISTORY_PREV_IC_TEXT);
        _pageHistPrevious.setBackgroundColor(Color.TRANSPARENT);
        _pageHistPrevious.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getIconColor()));
        _pageHistNext.setTypeface(_typeFace);
        _pageHistNext.setAllCaps(false);
        _pageHistNext.setText(PlayerUIConstants.TG_HISTORY_NEXT_IC_TEXT);
        _pageHistNext.setBackgroundColor(Color.TRANSPARENT);
        _pageHistNext.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getIconColor()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isFromConfigurationChanged = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            _thumbnailsGallery.setNumColumns(2);
        } else {
            _thumbnailsGallery.setNumColumns(3);
        }
        _thumbnailsGallery.smoothScrollToPosition(mSeekBarLastPosition);
        _thumbnailsGallery.setSelection(mSeekBarLastPosition);
        _thumbnailsGallery.setSelected(true);
        mSeekbar.setProgress(mSeekBarLastPosition);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnHistoryPreviousmobile) {
            if (mListner != null) {
                mListner.NavigatePreviousPage();
            }
        } else if (v.getId() == R.id.btnHistoryNextmobile) {
            if (mListner != null) {
                mListner.NavigateNextPage();
            }
        } else if (v.getId() == R.id.btnGoArrowmobile) {
            jumpToPageAction(_editPageNum.getText().toString().trim());
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        mListner.ThumbnailpageNavigation(position + 1);
        getActivity().finish();
    }

    private void jumpToPageAction(String pageNum) {
        if (getCurrPageIDByDisplayNum(pageNum) > -1) {
            mListner.ThumbnailpageNavigation(getCurrPageIDByDisplayNum(pageNum));
            hideKeyboard(getContext());
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.page_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        return true;
    }


    public static void hideKeyboard(Context context) {
        // Check if no view has focus:
        View view;
        try {
            view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setThumbListener(CustomThumbnailsListner listner) {
        mListner = listner;
    }

    public int getCurrPageIDByDisplayNum(String dispNum) {
        if (mThumbnailColl != null) {
            for (int i = 0; i < mThumbnailColl.size(); i++) {
                ThumbnailVO vo = mThumbnailColl.get(i);
                String fname = vo.getFolioID();
                if (dispNum.equalsIgnoreCase(fname)) {
                    if (fname != null && !fname.isEmpty() && dispNum.equalsIgnoreCase(fname)) {
                        return vo.getPageID();
                    }
                }
            }
            return -1;
        }
        return -1;
    }

    public static GradientDrawable getRectAngleDrawable(int solidColor, float[] _radius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(_radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }
}