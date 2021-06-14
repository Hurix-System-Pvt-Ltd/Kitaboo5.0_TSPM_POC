package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.customui.bookmark.BookMarkView;
import com.hurix.customui.datamodel.BookMarkVO;
import com.hurix.customui.interfaces.OnDialogOkActionListner;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CustomBookmarkActionHandler implements PopupWindow.OnDismissListener, TextView.OnEditorActionListener,
        OnDialogOkActionListner {
    private final ReaderThemeSettingVo mReaderThemeSettingVo;
    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private Animation mTrackAnim;
    private LayoutInflater inflater;
    private ViewGroup mTrack;
    private CustomBookmarkActionHandler.OnDismissListener mDismissListener;
    private CustomClearableEditText mEdittext;
    private boolean mDidAction;
    private boolean mAnimateTrack;
    private int mAnimStyle;
    public CustomBookmarkActionHandler.PopupWindows mPopupWindows;
    private View mRootView;
    private WindowManager mWindowManager;
    private Dialog mWindow;
    private View mContainer;
    private int arrowHeight, arrowWidth;
    public final int DEFAULT_ANIM = -1;
    public final int NO_ANIM = 0;
    public final int ANIM_GROW_FROM_LEFT = 1;
    public final int ANIM_GROW_FROM_RIGHT = 2;
    public final int ANIM_GROW_FROM_CENTER = 3;
    public final int ANIM_AUTO = 4;
    private Context mContext;
    private boolean mIsConfigChanged = false;
    private CustomBookmarkActionHandler.OnHandlerDismissListener mHandlerListener;
    private BookMarkVO mCurrentBookmarkVO;
    public static final String UGC_ITEM_MODE_NEW = "N";
    public static final String UGC_ITEM_MODE_MODIFIED = "M";
    public static final String UGC_ITEM_MODE_DELETED = "D";
    private ThemeUserSettingVo mThemeUsersetting ;
    private RelativeLayout main_bookmark_panel;
    private int notificationBarHeight = 0;

    public CustomBookmarkActionHandler(Context context, ReaderThemeSettingVo readerThemeSettingVo) {
        mContext = context;
        mReaderThemeSettingVo = readerThemeSettingVo;
        mPopupWindows = new CustomBookmarkActionHandler.PopupWindows(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTrackAnim = AnimationUtils.loadAnimation(context, com.hurix.epubreader.R.anim.rail);
        mTrackAnim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                final float inner = (t * 1.55f) - 1.1f;
                return 1.2f - inner * inner;
            }
        });
        mRootView = mPopupWindows.mRootView;
        mWindowManager = mPopupWindows.mWindowManager;
        mWindow = mPopupWindows.mWindow;
        mWindow.getWindow().getAttributes().dimAmount = 0.4f;
        mWindow.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setRootViewId(com.hurix.epubreader.R.layout.quickaction);
        mAnimStyle = ANIM_GROW_FROM_CENTER;
        mAnimateTrack = false;
    }

    //add listner to notfy all the action of this class or bookmark textview

    public void addOnHandlerDismissListener(CustomBookmarkActionHandler.OnHandlerDismissListener handlerlistener)
    {
        this.mHandlerListener = handlerlistener;
    }

    public void setBookmarkData(BookMarkVO bookmarkVO){
        this.mCurrentBookmarkVO = bookmarkVO;
    }
    public void setRootViewId(int id) {
        mRootView = inflater.inflate(id, null);
        mTrack = (ViewGroup) mRootView.findViewById(com.hurix.epubreader.R.id.tracks);
        mArrowDown = (ImageView) mRootView.findViewById(com.hurix.epubreader.R.id.arrow_down);
        mArrowUp = (ImageView) mRootView.findViewById(com.hurix.epubreader.R.id.arrow_up);
        setArrowHeight(mArrowDown.getDrawable().getIntrinsicHeight());
        setArrowWidth(mArrowDown.getDrawable().getIntrinsicWidth());
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPopupWindows.setContentView(mRootView);
    }
    public View getRootView()
    {
        return mRootView;
    }
    public ImageView getArrowDown() {
        return mArrowDown;
    }

    public ImageView getArrowUp() {
        return mArrowUp;
    }

    public void mAnimateTrack(boolean mAnimateTrack) {
        this.mAnimateTrack = mAnimateTrack;
    }

    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    public void setTheme(ThemeUserSettingVo theme){
        mThemeUsersetting = theme ;
    }

    public void setAlertLayout(int alertLayoutId) {
        mContainer = inflater.inflate(alertLayoutId, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.setLayoutParams(params);
        main_bookmark_panel = (RelativeLayout) mContainer.findViewById(R.id.main_bookmark_panel);
       // main_bookmark_panel.setBackground(mContext.getResources().getDrawable(R.drawable.bookmark_background));
        main_bookmark_panel.setBackgroundDrawable(com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().
                        getDayMode().getBookmark().getPopupBackground()), new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 1, Color.parseColor(mReaderThemeSettingVo.getReader().
                getDayMode().getBookmark().getPopupBorder())));
        mEdittext = (CustomClearableEditText) mContainer.findViewById(R.id.bookmarkedit_text);
        mEdittext.setHintTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getBookmark().getHintTextColor()));
        mEdittext.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getBookmark().getInputPanelBg()));
       //mEdittext.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        mEdittext.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getBookmark().getTextColor()));
        //mEdittext.setBackground(mContext.getResources().getDrawable(R.drawable.edit_text_bottom_border));
        RelativeLayout body = (RelativeLayout) mTrack.findViewById(com.hurix.epubreader.R.id.text_body);

        mEdittext.setSelection(mEdittext.getText().length());

        if (mCurrentBookmarkVO != null){
            mEdittext.setText(mCurrentBookmarkVO.getChaptername());
            String bookmarkData = mCurrentBookmarkVO.getBookmarkTitle();
            if (bookmarkData != null && bookmarkData.isEmpty() == false) {
                mEdittext.setText(bookmarkData);
                if (bookmarkData.length() < 51) {
                    mEdittext.setSelection(bookmarkData.length());
                }
            }
        }


        mEdittext.setOnEditorActionListener(this);
        body.addView(mContainer);
    }

    public void show(View anchor) {
        mPopupWindows.preShow();
        showKeySoftKeyBoard();
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
        final int anchorWidth = anchor.getMeasuredWidth();
        final int anchorHeight = anchor.getMeasuredHeight();
        final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mDidAction = false;
        Rect anchorRect = new Rect(anchorLeft, anchorTop, anchorLeft + anchorWidth, anchorTop + anchorHeight);
        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int rootWidth = mRootView.getMeasuredWidth();
        final int rootHeight = mRootView.getMeasuredHeight();
        int arrowLeftMargin = 10;
        int xPos = anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2) - arrowLeftMargin;
        int yPos = anchorRect.top - rootHeight;

        boolean onTop = true;
        if (rootHeight > anchorTop) {
            yPos = anchorRect.bottom;
            onTop = false;
        }
        if (rootWidth - (anchorWidth / 2) > screenWidth - xPos) {
            xPos = (anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2)) - rootWidth + arrowWidth + 5;
            arrowLeftMargin = rootWidth - arrowWidth - arrowLeftMargin;
        }
        //showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowLeftMargin);
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        WindowManager.LayoutParams params = mWindow.getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
        params.x = xPos;
        params.y = yPos;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        mWindow.getWindow().setAttributes(params);
        mWindow.show();
        if (mAnimateTrack)
            mTrack.startAnimation(mTrackAnim);
    }

    public Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }


    public void showBookMark(View anchor) {
        mPopupWindows.preShow();
        showKeySoftKeyBoard();
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
        final int anchorWidth = anchor.getMeasuredWidth();
        final int anchorHeight = anchor.getMeasuredHeight();
        final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mDidAction = false;
        Rect anchorRect = new Rect(anchorLeft, anchorTop, anchorLeft + anchorWidth, anchorTop + anchorHeight);
        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int rootWidth = mRootView.getMeasuredWidth();
        final int rootHeight = mRootView.getMeasuredHeight();
        int arrowLeftMargin = 10;
        int xPos = anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2) - arrowLeftMargin;
        int yPos = anchorRect.top + rootHeight;

        boolean onTop = true;
        if (rootHeight > anchorTop) {
            yPos = anchorRect.bottom;
            onTop = false;
        }
        if (rootWidth - (anchorWidth / 2) > screenWidth - xPos) {
            xPos = (anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2)) - rootWidth + arrowWidth + 5;
            arrowLeftMargin = rootWidth - arrowWidth - arrowLeftMargin;
        }
        //showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowLeftMargin);
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        WindowManager.LayoutParams params = mWindow.getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
        params.x = xPos;
        params.y = yPos;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        mWindow.getWindow().setAttributes(params);
        mWindow.show();
        if (mAnimateTrack)
            mTrack.startAnimation(mTrackAnim);
    }

    public void showBookmark(View anchor , int[] location)
    {
        statusBarHeight(Resources.getSystem());
        mPopupWindows.preShow();
        showKeySoftKeyBoard();
      /*  int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        final int anchorLeft = location[0];
        final int anchorTop = location[1];
        final int anchorWidth = anchor.getMeasuredWidth();
        final int anchorHeight = anchor.getMeasuredHeight();
        final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();*/
        mDidAction = false;
        //Rect anchorRect = new Rect(anchorLeft, anchorTop, anchorLeft + anchorWidth, anchorTop + anchorHeight);
        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /*final int rootWidth = mRootView.getMeasuredWidth();
        final int rootHeight = mRootView.getMeasuredHeight();
        int arrowLeftMargin = 10;
        int xPos = anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2) - arrowLeftMargin;
        int yPos = anchorRect.top + rootHeight;

        boolean onTop = true;
        if (rootHeight > anchorTop) {
            yPos = anchorRect.bottom;
            onTop = false;
        }
        if (rootWidth - (anchorWidth / 2) > screenWidth - xPos) {
            xPos = (anchorRect.left + (anchorWidth / 2) - (arrowWidth / 2)) - rootWidth + arrowWidth + 5;
            arrowLeftMargin = rootWidth - arrowWidth - arrowLeftMargin;
        }*/
        //showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowLeftMargin);
        //setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        WindowManager.LayoutParams params = mWindow.getWindow().getAttributes();
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
       /* params.x = xPos;
        params.y = yPos;*/
        if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && Build.MODEL.equalsIgnoreCase("Pixel 3 XL")){
            params.x = location[0];
            params.y = location[1] - notificationBarHeight + 20;
        }else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && Build.MODEL.equalsIgnoreCase("Pixel 3 XL")){
            params.x = location[0] - (notificationBarHeight * 2 ) + 40;
            params.y = location[1];
        }else {
            params.x = location[0];
            params.y = location[1];
        }
        params.gravity = Gravity.TOP + Gravity.LEFT;
        mWindow.getWindow().setAttributes(params);
        mWindow.show();
        if (mAnimateTrack)
            mTrack.startAnimation(mTrackAnim);
    }

    private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == com.hurix.epubreader.R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == com.hurix.epubreader.R.id.arrow_up) ? mArrowDown : mArrowUp;
        showArrow.setVisibility(View.VISIBLE);
        (showArrow.getLayoutParams()).width = arrowWidth;
        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();
        param.leftMargin = requestedX;
        hideArrow.setVisibility(View.INVISIBLE);
    }

    private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {

    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    class PopupWindows {
        protected Context mContext;
        protected Dialog mWindow;
        protected View mRootView;
        protected Drawable mBackground = null;
        protected WindowManager mWindowManager;

        PopupWindows(Context context) {
            mContext = context;
            mWindow = new Dialog(context, com.hurix.epubreader.R.style.DialogThemeTransparent);
            mWindow.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    showSoftKeyboard();

                    GlobalDataManager.getInstance().pauseAudio(false);

                    GlobalDataManager.getInstance().closeAudio();
                }
            });
            DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //	hideSoftKeyboard();
                    try {
                        if (!mIsConfigChanged) {
                            onsaveBookmark();
                            mHandlerListener.onDismiss(mCurrentBookmarkVO);
                        }
                    } catch (Exception e) {
                    }
                }
            };
            mWindow.setOnDismissListener(listener);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        protected void onDismiss() {
        }

        protected void onShow() {
        }

        protected void preShow() {
            if (mRootView == null)
                throw new IllegalStateException("setContentView was not called with a view to display.");
            onShow();

            mWindow.setCanceledOnTouchOutside(true);
            mWindow.setContentView(mRootView);
        }

        protected void setBackgroundDrawable(Drawable background) {
            mBackground = background;
        }

        protected void setContentView(View root) {
            mRootView = root;
            mWindow.setContentView(root);
        }

        protected void setContentView(int layoutResID) {
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            setContentView(inflator.inflate(layoutResID, null));
        }

        protected void setOnDismisListener(DialogInterface.OnDismissListener listener) {
            mWindow.setOnDismissListener(listener);
        }

        protected void dismiss() {
            mWindow.dismiss();
        }
    }

    public View findViewById(int id) {
        return mContainer.findViewById(id);
    }

    public void dismiss() {
        if (mWindow != null)
            mWindow.dismiss();
    }

    public void setArrowHeight(int arrowHeight) {
        this.arrowHeight = arrowHeight;
    }

    public void setArrowWidth(int arrowWidth) {
        this.arrowWidth = arrowWidth;
    }

    public int getArrowHeight() {
        return arrowHeight;
    }

    public int getArrowWidth() {
        return arrowWidth;
    }

    private void showKeySoftKeyBoard() {

    }


    private void onsaveBookmark() {
        if (mEdittext.getText().toString().trim().length() > 0) {
            if (mCurrentBookmarkVO.getLocalID() == -1) {
                onSave();
            } else {
                onUpdate();
            }
        } else {
            onDelete();
        }
    }

    private void onSave() {
        if(mCurrentBookmarkVO != null) {
            mCurrentBookmarkVO.setBookmarkTitle(mEdittext.getText().toString().trim());
            mCurrentBookmarkVO.setDateTime(getDateTime());//""+new Date());
            mCurrentBookmarkVO.setMode(UGC_ITEM_MODE_NEW);
        }
    }

    private void onUpdate() {
        mCurrentBookmarkVO.setDateTime(getDateTime());
        mCurrentBookmarkVO.setBookmarkTitle(mEdittext.getText().toString().trim());
        mCurrentBookmarkVO.setMode(UGC_ITEM_MODE_MODIFIED);
        mCurrentBookmarkVO.setSyncStatus(false);
    }

    private void onDelete() {
        mCurrentBookmarkVO.setBookmarkTitle("");
        mCurrentBookmarkVO.setMode(UGC_ITEM_MODE_DELETED);
        mCurrentBookmarkVO.setSyncStatus(false);
        mCurrentBookmarkVO.setDateTime(getDateTime());

    }


    public static String getDateTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = df.format(currentLocalTime);
        return date;
    }

    private void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(mEdittext, 0);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mEdittext.setFocusable(false);
            mEdittext.clearFocus();
            dismiss();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.hurix.kitaboo.constants.listener.OnDialogOkActionListner#onOKClick(android.view.View)
     */
    @Override
    public void onOKClick(View v) {
        // TODO Auto-generated method stub

    }


    public void setConfigChanged(boolean isConfigChanged) {
        mIsConfigChanged = isConfigChanged;
    }

    /** Listner use to inform action of Handler to Bookmark View **/

    public interface OnHandlerDismissListener {
        void onDismiss(BookMarkVO vo);
    }

    private int statusBarHeight(android.content.res.Resources resources) {
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (Build.MODEL.equalsIgnoreCase("HD1901") || Build.MODEL.equalsIgnoreCase("Pixel 3 XL")  || Build.MODEL.equalsIgnoreCase("SM-M307F") && resourceId > 0)
            notificationBarHeight = resources.getDimensionPixelSize(resourceId);
        else if(Build.MODEL.equalsIgnoreCase("HD1901") || Build.MODEL.equalsIgnoreCase("Pixel 3 XL")  || Build.MODEL.equalsIgnoreCase("SM-M307F") && resourceId == 0)
            notificationBarHeight = (int) Math.ceil((Build.VERSION.SDK_INT >= 28 ? 24 : 25) * resources.getDisplayMetrics().density);

        return notificationBarHeight;
    }

}
