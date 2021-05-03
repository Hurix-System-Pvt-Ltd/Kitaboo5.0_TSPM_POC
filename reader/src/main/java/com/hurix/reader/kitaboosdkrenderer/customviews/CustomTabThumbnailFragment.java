package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.customui.views.KEditText;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;


/**
 * Created by amitraj.sharma on 2/3/2018.
 */

public class CustomTabThumbnailFragment extends Fragment implements OnClickListener, OnTouchListener,SeekBarPositionChangeListner{

    private RecyclerView mThumbnailList;
    private CustomThumbnailsRecyclerAdapter mAdapter;
    private Button mPageHistPrevious = null, mPageHistNext = null, mGotoPgBtn;
    private KEditText mGotoPgTxt;
    private TextView mPageTxt;
    private Typeface mTypeFace;
    private SeekBar mSeekbar;
    private RelativeLayout mControllerButton,mPagenavmainID;
    private View mRootView, mDummyView;
    private static ArrayList<ThumbnailVO> mThumbnailColl = new ArrayList<ThumbnailVO>();
    //private UserSettingVO mUserSettingVO;
    private Typeface customTypeFace;
    private CustomThumbnailsListner mListner;
    private static String mthumbnailpath = "";
    private static int mcurrentindex = 0, mcurrentindexl1 = 0, mcurrentindexl2 = 0;
    private int mThumbnailBackgroundColor, mThumbnailIconsColor;
    private boolean mShowHistoryButtons;
    private boolean mThumbnailContainer;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private TextView mTotalPageCount;
    private static int mTOCSize;


    public static CustomTabThumbnailFragment newInstance(String toc, ArrayList<ThumbnailVO> thumbnailVOc,
                                                         String thumbnailpath, int currentindex, int currentindex1, int currentindex2, int tocSize) {
        CustomTabThumbnailFragment fragmentFirst = new CustomTabThumbnailFragment();
        Bundle args = new Bundle();
        mThumbnailColl = thumbnailVOc;
        mthumbnailpath = thumbnailpath;
        mcurrentindex = currentindex;
        mcurrentindexl1 = currentindex1;
        mcurrentindexl2 = currentindex2;
        mTOCSize = tocSize;
        args.putString("data", toc.toString());
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public void setThemeColor(ReaderThemeSettingVo _themeUserSettingVo) {
        this.mReaderThemeSettingVo = _themeUserSettingVo;
    }

    public void setThumbListener(CustomThumbnailsListner listner) {
        mListner = listner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.custom_thumbnail_layout_enterprise, parent, false);
        initView(mRootView);
        if (mListner != null)
            mListner.onThumbnailViewCreated(mRootView);
        getArguments().getBoolean("ismobile");
        return mRootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //this.dismissAllowingStateLoss();
    }

    private void initView(View view) {
        initViews(view);
        setUpIconFonts();
        setData();
        setupSeekBar();
        customTypeFace = Typefaces.get(getActivity(), getActivity().getResources()
                .getString(R.string.text_font_file));
        setCustomTypeFace();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mcurrentindex == 0) {
                //mSeekbar.setProgress(1);
                mGotoPgTxt.setText(String.valueOf(1));

            } else {
                mGotoPgTxt.setText(String.valueOf(mcurrentindex + 1));
            }
        } else {
            if (mcurrentindex == 0) {
               // mSeekbar.setProgress(mcurrentindexl1);
                mGotoPgTxt.setText(String.valueOf(mcurrentindexl1));

            }else {
                //mSeekbar.setProgress(mcurrentindexl2);
                mGotoPgTxt.setText(String.valueOf(mcurrentindexl2));
            }
        }
    }

    private void setCustomTypeFace() {
        //mGotoPgTxt.setTypeface(customTypeFace);
        //mPageTxt.setTypeface(customTypeFace);
    }

    private GradientDrawable getSeekBarGradient(int itemColor) {
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{itemColor, itemColor});
        gradient.setShape(GradientDrawable.OVAL);
        gradient.setSize(getResources().getInteger(com.hurix.epubreader.R.integer.seekbar_thumb_size), getResources().getInteger(com.hurix.epubreader.R.integer.seekbar_thumb_size));
        gradient.setStroke(4, itemColor);
        return gradient;
    }
    private void setupSeekBar() {
        int colornew = Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSliderFilledColor());
        mSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.transparent), PorterDuff.Mode.SRC_ATOP);
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{colornew, colornew});
        gradient.setShape(GradientDrawable.OVAL);
        gradient.setSize(getResources().getInteger(R.integer.seekbar_thumb_size), getResources().getInteger(R.integer.seekbar_thumb_size));
        gradient.setStroke(0, Color.BLACK);

        mSeekbar.setThumb(getSeekBarGradient(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSliderColor())));
        mSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.thumbnail_progress_seek));
        mSeekbar.setMax(mAdapter.getItemCount() - 1);

        if (mListner != null)
            mListner.onSeekbarViewCreated(mSeekbar);
        if (mAdapter.getSetpositionscroll() != 0) {
            mThumbnailList.scrollToPosition(mAdapter.getSetpositionscroll());
            mGotoPgTxt.setText(String.valueOf(mAdapter.getSetpositionscroll()));
        }
    }

    public void setData() {
        ArrayList<ThumbnailVO> thumbNails = mThumbnailColl;
        for (int i = thumbNails.size(); i > 0; i--) {
            if (thumbNails.get(i - 1).getChapterNumber() == 0) {
                thumbNails.remove(i - 1);
            }
        }
        mAdapter.setdata(mListner, thumbNails, mthumbnailpath, mcurrentindex, mcurrentindexl1, mcurrentindexl2);
        if (mThumbnailList.getAdapter() == null) {
            // add a divider after each item for more clarity
            mThumbnailList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            mThumbnailList.setLayoutManager(horizontalLayoutManager);
            mThumbnailList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        setScrollBarToCurrentpage();
    }

    private void setScrollBarToCurrentpage() {
        if (mThumbnailColl != null) {
            for (int position = 0; position < mThumbnailColl.size(); position++) {
                if ((mThumbnailColl.get(position).getPageID() == 0)) {
                    int mcurrentpageindex = mcurrentindex;
                    if (mThumbnailColl.get(position).getPageColl() != null && mThumbnailColl.get(position).getPageColl().length == 1 &&
                            (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) || SDKManager.getInstance().getPageMode() == 1 ) {
                        mcurrentpageindex = mcurrentpageindex + 1;
                        if (mcurrentpageindex == mThumbnailColl.get(position).getPageColl()[0].getPageID()) {
                            //set seekbar position when book loaded below two lines for scrolling seekbar to currentpage
                            if(position>0)
                           // mThumbnailList.smoothScrollToPosition(position);
                                mThumbnailList.scrollToPosition(position);
                            mThumbnailList.setSelected(true);
                        }
                    }
                    if (mThumbnailColl.get(position).getPageColl() != null && getResources().getConfiguration().
                            orientation == Configuration.ORIENTATION_LANDSCAPE &&  SDKManager.getInstance().getPageMode() != 1) {
                        if (mcurrentpageindex == 0) {
                            if (mThumbnailColl.get(position).getPageColl().length == 1) {
                                mcurrentpageindex = mcurrentpageindex + 1;
                                if (mcurrentpageindex == mThumbnailColl.get(position).getPageColl()[0].getPageID()) {
                                    //set seekbar position when book loaded below two lines for scrolling seekbar to currentpage
                                    if(position>0)
                                    mThumbnailList.scrollToPosition(position - 1);
                                    mThumbnailList.setSelected(true);
                                }

                            }
                        } else {
                            if (mThumbnailColl.get(position).getPageColl() != null) {
                                if (mThumbnailColl.get(position).getPageColl().length == 2) {
                                    if (mcurrentindexl2 == mThumbnailColl.get(position).getPageColl()[0].getPageID()) {
                                        //set seekbar position when book loaded below two lines for scrolling seekbar to currentpage
                                        if(position>0)
                                        mThumbnailList.scrollToPosition(position - 1);
                                        mThumbnailList.setSelected(true);
                                    }
                                }
                                if (mThumbnailColl.get(position).getPageColl().length == 1) {

                                    if (mcurrentindexl1 == mThumbnailColl.get(position).getPageColl()[0].getPageID()) {
                                        //set seekbar position when book loaded below two lines for scrolling seekbar to currentpage
                                        if(position>0)
                                        mThumbnailList.scrollToPosition(position - 1);
                                        mThumbnailList.setSelected(true);
                                    }
                                    if (mcurrentindexl2 == mThumbnailColl.get(position).getPageColl()[0].getPageID()) {
                                        //set seekbar position when book loaded below two lines for scrolling seekbar to currentpage
                                        if(position>0)
                                        mThumbnailList.scrollToPosition(position - 1);
                                        mThumbnailList.setSelected(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void initViews(View view) {
        mThumbnailList = (RecyclerView) view.findViewById(R.id.thumbnailImagelayout);

        mThumbnailList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
              /*  if(mSeekbar!=null){
                    final int offset = recyclerView.computeHorizontalScrollOffset();
                    if (offset % recyclerView.getChildAt(0).getMeasuredWidth() == 0) {
                        final int position = offset / recyclerView.getChildAt(0).getMeasuredWidth();
                      if(position>mSeekbar.getProgress())
                          mSeekbar.setProgress(mSeekbar.getProgress()+1);
                      else
                          mSeekbar.setProgress(mSeekbar.getProgress()-1);
                    }

                }*/


            }
        });
        mControllerButton = (RelativeLayout) view.findViewById(R.id.controllerbuttons);
        mPageHistPrevious = (Button) view.findViewById(R.id.btnHistoryPrevious);
        mPagenavmainID =(RelativeLayout)view.findViewById(R.id.pagenav_mainID);
        mPageHistPrevious.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getIconColor()));
        mPageHistPrevious.setOnClickListener(this);
        mPageHistNext = (Button) view.findViewById(R.id.btnHistoryNext);
        mPageHistNext.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getIconColor()));
        mDummyView = view.findViewById(R.id.dummy_center_view);
        mPageHistNext.setOnClickListener(this);
        //mThumbnailList.setOnItemClickListener(this);
        mThumbnailList.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        ThumbnailVO thumbnailVO = mThumbnailColl.get(position);
                        if (thumbnailVO != null && thumbnailVO.getPageID() != 0) {
                            mListner.ThumbnailpageNavigation(thumbnailVO.getPageID());
                            hideKeyboard(getActivity().getBaseContext(), mGotoPgTxt);
                        } else {
                            if (thumbnailVO != null && thumbnailVO.getPageColl() != null) {
                                if (thumbnailVO != null && thumbnailVO.getPageColl().length == 1) {
                                    mListner.ThumbnailpageNavigation(thumbnailVO.getPageColl()[0].getPageID());
                                    hideKeyboard(getActivity(), mGotoPgTxt);
                                }
                            }
                        }
                    }
                }));
        mPagenavmainID.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getPopupBackground()));
        mTotalPageCount = (TextView) view.findViewById(R.id.total_page);
        mTotalPageCount.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTextColor()));
        mGotoPgTxt = (KEditText) view.findViewById(R.id.goto_pg_no);
        mGotoPgTxt.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTextColor()));
        /*mGotoPgTxt.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTextColor()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 1, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTextColor())));
*/
        if (getResources().getBoolean(R.bool.is_BE_Publishing)){
            mGotoPgTxt.setTextColor(Color.BLACK);
            mTotalPageCount.setTextColor(Color.BLACK);
            mPageHistPrevious.setTextColor(Color.BLACK);
            mPageHistNext.setTextColor(Color.BLACK);
        }
        mGotoPgTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    mListner.onGotoClick(mGotoPgTxt.getText().toString().trim());
                    hideKeyboard(getContext(), v);
                    return true;
                }
                return false;
            }
        });
        mGotoPgTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               // Log.d("TEST", " focus changed has focus: " + hasFocus);
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                getView().getWindowVisibleDisplayFrame(r);
                int heightDiff = getView().getRootView().getHeight() - (r.bottom - r.top);
               // Log.d("TEST", " heightDiff: " + heightDiff);
            }
        });
        mAdapter = new CustomThumbnailsRecyclerAdapter(getActivity().getBaseContext(), mThumbnailColl, mThumbnailIconsColor, mReaderThemeSettingVo,this);
        mSeekbar = (SeekBar) view.findViewById(R.id.seekbarProgress);
        mSeekbar.setMax(mThumbnailColl.size());
        mTotalPageCount.setText("/" + (mThumbnailColl.size()));
        mSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

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
                    scrollToPage(progress);
                }
            }
        });
        setScrollListeners();
        setHistoryButtonNavigationmode();

        mControllerButton.setVisibility(View.VISIBLE);
        if (mShowHistoryButtons) {
            mPageHistPrevious.setVisibility(View.VISIBLE);
            mPageHistNext.setVisibility(View.VISIBLE);
        } else {
            mPageHistPrevious.setVisibility(View.GONE);
            mPageHistNext.setVisibility(View.GONE);
        }
    }

    private void setScrollListeners() {
       /* mThumbnailList.setOnScrollListener(new AbsHListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsHListView absHListView, int i) {

            }

            @Override
            public void onScroll(AbsHListView absHListView, int firstVisibleItem, int visibleItemCount, int totalCount) {
                mSeekbar.setProgress(firstVisibleItem);
                *//*if (firstVisibleItem >= totalCount - 5) {
                    mSeekbar.setProgress(totalCount);
                }*//*

            }

        });*/
    }

    public void scrollToPage(int pageID) {
        if(pageID>0)
        mThumbnailList.scrollToPosition(pageID);

    }

    public void setHistoryButtonNavigationmode() {
        if (mListner != null) {
            mListner.onPageHistoryButtonsCreated(mPageHistNext, mPageHistPrevious);
        }
    }

    private void setUpIconFonts() {
        getReaderTyface();
        mPageHistPrevious.setTypeface(mTypeFace);
        mPageHistPrevious.setAllCaps(false);
        mPageHistPrevious.setText(PlayerUIConstants.TG_HISTORY_PREV_IC_TEXT);
        mPageHistNext.setTypeface(mTypeFace);
        mPageHistNext.setAllCaps(false);
        mPageHistNext.setText(PlayerUIConstants.TG_HISTORY_NEXT_IC_TEXT);
        if (!getActivity().getResources().getBoolean(R.bool.show_history_navigation)) {
            mPageHistPrevious.setVisibility(View.GONE);
            mPageHistNext.setVisibility(View.GONE);
            mDummyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnHistoryPrevious) {
            if (mListner != null) {
                mListner.NavigatePreviousPage();
            }
        } else if (v.getId() == R.id.btnHistoryNext) {
            if (mListner != null) {
                mListner.NavigateNextPage();
            }
        } else if (v.getId() == R.id.goto_pg_btn) {
            mListner.onGotoClick(mGotoPgTxt.getText().toString().trim());
            hideKeyboard(getContext(), v);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

  /*  @Override
    public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int position, long l) {
        ThumbnailVO thumbnailVO = mThumbnailColl.get(position);
        if (thumbnailVO != null && thumbnailVO.getPageID() != 0) {
            mListner.ThumbnailpageNavigation(thumbnailVO.getPageID());
            hideKeyboard(getActivity().getBaseContext(), mGotoPgTxt);
        } else {
            if (thumbnailVO != null && thumbnailVO.getPageColl() != null) {
                if (thumbnailVO != null && thumbnailVO.getPageColl().length == 1) {
                    mListner.ThumbnailpageNavigation(thumbnailVO.getPageColl()[0].getPageID());
                    hideKeyboard(this.getActivity(), mGotoPgTxt);
                }
            }
        }

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();

        mThumbnailList = null;
        mAdapter = null;
        mSeekbar = null;
        mRootView = null;
        hideKeyboard(getContext(), mGotoPgTxt);
    }

    public static void hideKeyboard(Context context, View view) {
        // Check if no view has focus:
        try {
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeFace = Typefaces.get(getActivity().getBaseContext(), fontfile);
        } else {
            mTypeFace = Typefaces.get(getActivity().getBaseContext(), "kitabooread.ttf");
        }
    }

    public void showHistoryButtons(boolean b) {
        mShowHistoryButtons = b;
    }

    @Override
    public void changePosition(int position) {
        if(mSeekbar!=null)
            mSeekbar.setProgress(position);
    }
}