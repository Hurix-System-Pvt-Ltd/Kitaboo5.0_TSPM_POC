package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.hurix.commons.utils.Utils;

import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.datamodel.CommentsVO;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.views.KitabooEditTextCopyPasteDiabled;
import com.hurix.commons.utils.ClearableEditText;
import com.hurix.commons.Constants.Constants;
import com.hurix.epubreader.database.DatabaseManager;



import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.customviews.adapters.NoteCommentsAdapter;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.reader.kitaboosdkrenderer.views.CustomPlayerUIConstants;

import java.util.ArrayList;


/**
 * Created by biki.sah on 06-08-2018.
 */
public class CustomNoteView extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private HighlightVO mHighlightVo;
    private TextView mNoteTitle, mTextYellow, mTextRed, mTextJstNowTab, mNoteHighlightedText,
            mTextPurple, mTextGreen, mTextBlue, mTextDelete, mTextJustNow, mTextSave, mTextShare;
    private KitabooEditTextCopyPasteDiabled mNoteEditText;
    private Typeface typeface;
    private LinearLayout mHighlightColorContainer;
    private String noteData;
    private TextView[] mHighlightCollections;
    private AddStickcyNoteActionListeners mListeners;
    private String mNoteData;
    private String backgroundColor;
    private boolean mIsMobile, isFromMenuItemClick;
    private LinearLayout mJstNowContainerMobile, mNoteHighlightedTextContainer;
    private ViewFlipper mTopContainer;
    private String highlightedText;
    private View mHighlightedTextDivider;
    private Animation _in_from_left;
    private Animation _out_to_right;
    private Animation _in_from_right;
    private Animation _out_to_left;
    private boolean animstart;
    private TextWatcher textWatcher;
    private LinearLayout post_comment_container;
    private ListView mMembersListCollHolder;
    private ClearableEditText edtPost;
    private Button btnPost;
    private TextView tv_comment_overflow,tv_overflow_share,tv_overflow_post,tv_close_expanded_overflow;
    private NoteCommentsAdapter mCommentListAdapter;
    private LinearLayout ll_expanded_comment_overflow;
    private RelativeLayout rl_normal_note_action_container,rl_comment_action_container,rl_comment_overflow_panel, mInnerColorLayout;
    private Long mBookID;
    private String mCurrentFolioID;
    private Long mUserId;
    private boolean mIsUGCSharingEnable;

    public CustomNoteView(Context context, ReaderThemeSettingVo theme, boolean value, boolean isFromMenuItem, boolean isUgcShareEnabled) {
        super(context, R.style.DialogThemeTransparent);
        mContext = context;
        readerThemeSettingVo = theme;
        mIsMobile = value;
        isFromMenuItemClick = isFromMenuItem;
        mIsUGCSharingEnable = isUgcShareEnabled;
        initView();
    }

    protected CustomNoteView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initView();
    }

    public CustomNoteView(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIsMobile) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams WMLP = getWindow().getAttributes();
            WMLP.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            mTextJstNowTab.setVisibility(View.GONE);
            mJstNowContainerMobile.setVisibility(View.VISIBLE);
            mTopContainer.setBackground(mContext.getResources().getDrawable(R.drawable.sticky_note_background_mobile));
        } else {
            mJstNowContainerMobile.setVisibility(View.GONE);
            mTextJstNowTab.setVisibility(View.VISIBLE);
            mTopContainer.setBackground(mContext.getResources().getDrawable(R.drawable.sticky_note_background));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            //  int mHeightPixels = com.hurix.kitaboocloud.utils.Utils.dpToPx((int) mContext.getResources().getDimension(R.dimen.sticky_NoteDialog_Height));
            // int mWidthPixels = com.hurix.kitaboocloud.utils.Utils.dpToPx((int) mContext.getResources().getDimension(R.dimen.sticky_NoteDialog_Width));
            // int mLandscapeHeightPixels = com.hurix.kitaboocloud.utils.Utils.dpToPx((int) mContext.getResources().getDimension(R.dimen.sticky_NoteDialog_Landscape_Height));

           /* if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                getWindow().setLayout((width / 2) + mWidthPixels, (height / 2) + mHeightPixels);
            } else {
                getWindow().setLayout((width / 2) - mWidthPixels, (height / 2) - mLandscapeHeightPixels);
            }*/
            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, (height / 2));
            WindowManager.LayoutParams WMLP = getWindow().getAttributes();
            WMLP.gravity = Gravity.CENTER;
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sticky_note_view, null);

        initializeView(view);
        setContentView(view);
        setTypeface();
        setTypefacesInViews();
        setTextInViews();
        setTextSizes();
        setHighLightTextsColor();
        setListeners();
        mHighlightCollections = new TextView[]{mTextYellow, mTextRed, mTextPurple,
                mTextGreen, mTextBlue};
        setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_orange_color));
        selectHighlightDrawable(mTextYellow);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mNoteEditText != null && mNoteEditText.getText() != null && !mNoteEditText.getText().toString().isEmpty()) {
                    mTextSave.setAlpha(1f);
                    mTextShare.setAlpha(1f);
                    mTextSave.setEnabled(true);
                    mTextShare.setEnabled(true);
                    mTextDelete.setEnabled(true);
                    mTextDelete.setAlpha(1f);
                } else {
                    mTextSave.setAlpha(0.3f);
                    mTextShare.setAlpha(0.3f);
                    mTextSave.setEnabled(false);
                    mTextShare.setEnabled(false);
                    mTextDelete.setEnabled(false);
                    mTextDelete.setAlpha(0.3f);
                }
            }
        };
        mNoteEditText.addTextChangedListener(textWatcher);

    }

    private void setListeners() {
        mTextYellow.setOnClickListener(this);
        mTextRed.setOnClickListener(this);
        mTextPurple.setOnClickListener(this);
        mTextGreen.setOnClickListener(this);
        mTextBlue.setOnClickListener(this);
        mTextDelete.setOnClickListener(this);
        mTextSave.setOnClickListener(this);
        mTextShare.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        tv_comment_overflow.setOnClickListener(this);
        tv_overflow_share.setOnClickListener(this);
        tv_overflow_post.setOnClickListener(this);
        tv_close_expanded_overflow.setOnClickListener(this);
    }

    private void setBackgroundColorOfHighlightContainer(String color) {
        if (new HexValidator().validate(color)) {
            mHighlightColorContainer.setBackgroundColor(Color.parseColor(color));
        }

    }

    private void setHighLightTextsColor() {
        mTextYellow.setTextColor(Color.parseColor(getContext().getResources().getString(R.string.note_orange_color)));
        mTextRed.setTextColor(Color.parseColor(getContext().getResources().getString(R.string.note_pink_color)));
        mTextPurple.setTextColor(Color.parseColor(getContext().getResources().getString(R.string.note_new_purple_color)));
        mTextGreen.setTextColor(Color.parseColor(getContext().getResources().getString(R.string.note_green_color)));
        mTextBlue.setTextColor(Color.parseColor(getContext().getResources().getString(R.string.note_blue_color)));
        mTextSave.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getActionButtonColor()));
        mTextShare.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getActionButtonColor()));
        mNoteEditText.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        mNoteEditText.setHintTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getHintTextColor()));
        mNoteEditText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getTitleColor()));
        mNoteHighlightedText.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getContextualtextColor()));
        mHighlightedTextDivider.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getHintTextColor()));
        mNoteTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getTitleColor()));
        tv_overflow_post.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getIconColor()));
        tv_overflow_share.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getIconColor()));
        tv_close_expanded_overflow.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getTitleColor()));
        edtPost.setHintTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getBottomPanel().getHintTextColor()));
        mTextJstNowTab.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getTitleColor()));
        mTextDelete.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getBottomPanel().getIconColor()));

        mTextSave.setAlpha(0.3f);
        mTextShare.setAlpha(0.3f);
        mTextJstNowTab.setAlpha(0.5f);
        mTextJustNow.setAlpha(0.5f);
        mTextDelete.setAlpha(0.3f);
        mNoteHighlightedText.setAlpha(0.7f);
        btnPost.setAlpha(0.3f);

        mTextSave.setEnabled(false);
        mTextShare.setEnabled(false);
        mTextDelete.setEnabled(false);
        btnPost.setEnabled(false);
    }

    private void setTextSizes() {
        mTextDelete.setTextSize(21);
        mTextSave.setTextSize(21);
        mTextShare.setTextSize(21);
        mNoteHighlightedText.setTextSize(16);
        btnPost.setTextSize(18);
    }

    private void setTypefacesInViews() {
        mTextYellow.setTypeface(typeface);
        mTextRed.setTypeface(typeface);
        mTextPurple.setTypeface(typeface);
        mTextGreen.setTypeface(typeface);
        mTextBlue.setTypeface(typeface);
        mTextDelete.setTypeface(typeface);
        mTextSave.setTypeface(typeface);
        mTextShare.setTypeface(typeface);
        mNoteHighlightedText.setTypeface(null, Typeface.ITALIC);
        tv_comment_overflow.setTypeface(typeface);
        btnPost.setTypeface(typeface);
        tv_overflow_share.setTypeface(typeface);
        tv_overflow_post.setTypeface(typeface);
        tv_close_expanded_overflow.setTypeface(typeface);

        mTextYellow.setAllCaps(false);
        mTextRed.setAllCaps(false);
        mTextPurple.setAllCaps(false);
        mTextGreen.setAllCaps(false);
        mTextBlue.setAllCaps(false);
        mTextDelete.setAllCaps(false);
        mTextSave.setAllCaps(false);
        mTextShare.setAllCaps(false);
    }

    private void setTypeface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeface = Typefaces.get(mContext, fontfile);
        } else {
            typeface = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }

    private void setTextInViews() {
        mNoteTitle.setText(mContext.getResources().getString(R.string.sticky_note_title));
        mTextYellow.setText(CustomPlayerUIConstants.NOTE_HIGHLIGHT_TEXT);
        mTextRed.setText(CustomPlayerUIConstants.NOTE_HIGHLIGHT_TEXT);
        mTextPurple.setText(CustomPlayerUIConstants.NOTE_HIGHLIGHT_TEXT);
        mTextGreen.setText(CustomPlayerUIConstants.NOTE_HIGHLIGHT_TEXT);
        mTextBlue.setText(CustomPlayerUIConstants.NOTE_HIGHLIGHT_TEXT);
        mTextDelete.setText(CustomPlayerUIConstants.ACTION_DELETE_TEXT);
        mTextJustNow.setText(mContext.getResources().getString(R.string.sticky_just_now_text));
        mTextSave.setText(CustomPlayerUIConstants.ACTION_POST_TEXT);
        mTextShare.setText(CustomPlayerUIConstants.ACTION_SHARE_TEXT);
        tv_comment_overflow.setText(CustomPlayerUIConstants.OVERFLOW_MORE_VERTICAL);
        tv_overflow_share.setText(CustomPlayerUIConstants.ACTION_SHARE_TEXT);
        tv_overflow_post.setText(CustomPlayerUIConstants.ACTION_POST_TEXT);
        tv_close_expanded_overflow.setText(CustomPlayerUIConstants.ACTION_PEN_DONE_TEXT);
        btnPost.setText(CustomPlayerUIConstants.SEND_ARRAOW);
    }

    private void initializeView(View view) {
        mNoteTitle = (TextView) view.findViewById(R.id.note_text);
        mTextYellow = (TextView) view.findViewById(R.id.text_yellow);
        mTextRed = (TextView) view.findViewById(R.id.text_red);
        mTextPurple = (TextView) view.findViewById(R.id.text_purple);
        mTextGreen = (TextView) view.findViewById(R.id.text_green);
        mTextBlue = (TextView) view.findViewById(R.id.text_blue);
        mTextDelete = (TextView) view.findViewById(R.id.text_delete);
        mTextJustNow = (TextView) view.findViewById(R.id.text_time);
        mTextJustNow.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getTitleColor()));
        mNoteEditText = (KitabooEditTextCopyPasteDiabled) view.findViewById(R.id.note_edit_text);
        mTextSave = (TextView) view.findViewById(R.id.text_save);
        mTextShare = (TextView) view.findViewById(R.id.text_share);
        mTextJstNowTab = (TextView) view.findViewById(R.id.text_jst_now_tab);
        mHighlightColorContainer = (LinearLayout) view.findViewById(R.id.color_container);
        mJstNowContainerMobile = (LinearLayout) view.findViewById(R.id.jst_now_text_container);
        mJstNowContainerMobile.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        mTopContainer = (ViewFlipper) view.findViewById(R.id.top_container);
        mNoteHighlightedTextContainer = (LinearLayout) view.findViewById(R.id.highlighted_text_container);
        mNoteHighlightedTextContainer.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        mNoteHighlightedText = (TextView) view.findViewById(R.id.highlighted_text);
        mHighlightedTextDivider = (View) view.findViewById(R.id.highlighted_devider);

        post_comment_container = (LinearLayout) view.findViewById(R.id.post_comment_container);
        mMembersListCollHolder = (ListView) view.findViewById(com.hurix.epubreader.R.id.commentListHolder);
        edtPost = (ClearableEditText) view.findViewById(R.id.edtPostId);
        btnPost = (Button) view.findViewById(R.id.btnPost);
        mInnerColorLayout = (RelativeLayout) view.findViewById(R.id.innerColorLayout);
        mInnerColorLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        tv_comment_overflow = (TextView) view.findViewById(R.id.tv_comment_overflow);
        ll_expanded_comment_overflow = (LinearLayout) view.findViewById(R.id.ll_expanded_comment_overflow);
        tv_comment_overflow.setVisibility(View.VISIBLE);
        ll_expanded_comment_overflow.setVisibility(View.GONE);

        GradientDrawable commentBackground = com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getBottomPanel().getBackground()),
                new float[] { 5, 5, 5, 5, 5, 5, 5, 5 }, (int)1,Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getBottomPanel().getBorderColor()));
        post_comment_container.setBackground(commentBackground);

        GradientDrawable overflowBackgroud = com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(mContext.getResources().getColor(R.color.overflow_panel_background),
                new float[] { 200, 200, 200, 200, 200, 200, 200, 200 }, (int)1,mContext.getResources().getColor(com.hurix.epubreader.R.color.darkgrey));
        ll_expanded_comment_overflow.setBackground(overflowBackgroud);

        tv_overflow_share = (TextView) view.findViewById(R.id.tv_overflow_share);
        tv_overflow_post = (TextView) view.findViewById(R.id.tv_overflow_post);
        tv_close_expanded_overflow = (TextView) view.findViewById(R.id.tv_close_expanded_overflow);

        tv_close_expanded_overflow.setTextColor(mContext.getResources().getColor(R.color.self_note_comment_title));

        rl_normal_note_action_container = (RelativeLayout) view.findViewById(R.id.rl_normal_note_action_container);
        rl_normal_note_action_container.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        rl_comment_action_container = (RelativeLayout) view.findViewById(R.id.rl_comment_action_container);
        rl_comment_overflow_panel = (RelativeLayout) view.findViewById(R.id.rl_comment_overflow_panel);

        mCommentListAdapter = new NoteCommentsAdapter(mContext);

        // Share option not require for LTPM

        if (mIsUGCSharingEnable && SDKManager.getInstance().isClassAssociated()) {
            mTextShare.setVisibility(View.VISIBLE);
        } else {
            mTextShare.setVisibility(View.GONE);
            tv_overflow_share.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,20,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mTextSave.setLayoutParams(params);

        }
        edtPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edtPost.getText().toString().isEmpty()) {
                    btnPost.setAlpha(1f);
                    btnPost.setEnabled(true);
                }
                else
                {
                    btnPost.setAlpha(0.3f);
                    btnPost.setEnabled(false);
                }
            }
        });

    }

    public void setHighlightObj(HighlightVO highlightObj,Long userId, long bookId, String currentFoliId) {
        this.mHighlightVo = highlightObj;
        if( mHighlightVo.getCreatedByUserVO() != null &&  mHighlightVo.getCreatedByUserVO().getUserID()==0) {
            mHighlightVo.getCreatedByUserVO().setUserID(userId);
        }
        mBookID = bookId;
        mCurrentFolioID = mHighlightVo.getFolioID();
        mUserId = userId;

        if (isFromMenuItemClick) {
            mTextJstNowTab.setText(mContext.getResources().getString(R.string.sticky_just_now_text));
            mTextJustNow.setText(mContext.getResources().getString(R.string.sticky_just_now_text));
        } else {
            mTextJstNowTab.setText(com.hurix.customui.iconify.Utils.getDateInLocalFormat(highlightObj.getDateTime(), "hh:mm a dd MMMM yyyy"));
            mTextJustNow.setText(com.hurix.customui.iconify.Utils.getDateInLocalFormat(highlightObj.getDateTime(), "hh:mm a dd MMMM yyyy"));
        }
        setData(userId);
    }

    private void setData(Long userId) {
        if(mCurrentFolioID != null && !mCurrentFolioID.isEmpty())
        {
            getCommentsFromDB();
        }
        viewVisibilityHandlingDependingOnNoteStatus(userId);
        setCommentsDataForFirstNote();
    }

            private void viewVisibilityHandlingDependingOnNoteStatus(Long userId)
            {
                boolean isNoteNotShared = mHighlightVo.getCreatedByUserVO().getUserID() == userId && mHighlightVo.getUserShareColl().size() == 0;

                boolean isNoteSharedNCreatedByUser = mHighlightVo.getCreatedByUserVO() != null && mHighlightVo.getCreatedByUserVO().getUserID() == userId;

                if(!isNoteNotShared) // once note shared cannot be edited by sender or receiver
                {
                    mNoteEditText.setEnabled(true);
                    mNoteEditText.setFocusable(false);
                    mNoteEditText.setLongClickable(false);
                    mNoteEditText.removeTextChangedListener(textWatcher);

                    mTextSave.setAlpha(1f);
                    mTextShare.setAlpha(1f);
                    mTextSave.setEnabled(true);
                    mTextShare.setEnabled(true);
                    mTextSave.setVisibility(View.GONE);

                    if(isNoteSharedNCreatedByUser) // Sender can delete note
                    {
                        mTextDelete.setEnabled(true);
                        mTextDelete.setAlpha(1f);
                    }
                    else // Receiver can not delete note and change color
                    {
                        setEnableChooseColorPanel(false);
                        mTextShare.setVisibility(View.GONE);
                        mTextDelete.setEnabled(false);
                        mTextDelete.setAlpha(0.3f);
                    }
                }
            }

    private void viewVisibilityHandlingForOverflowMenuNoteStatus(Long userId)
    {
        boolean isNoteNotShared = mHighlightVo.getCreatedByUserVO().getUserID() == userId && mHighlightVo.getUserShareColl().size() == 0;

        boolean isNoteSharedNCreatedByUser = mHighlightVo.getCreatedByUserVO() != null && mHighlightVo.getCreatedByUserVO().getUserID() == userId;

        if(!isNoteNotShared) // once note shared for sender or receiver
        {
            if(isNoteSharedNCreatedByUser) // note share to Sender
            {

            }
            else // note share to Receiver
            {
                tv_overflow_share.setVisibility(View.GONE);
            }
        }
    }

    private void setEnableChooseColorPanel(boolean isEnable)
    {
        mTextYellow.setEnabled(isEnable);
        mTextRed.setEnabled(isEnable);
        mTextPurple.setEnabled(isEnable);
        mTextGreen.setEnabled(isEnable);
        mTextBlue.setEnabled(isEnable);
    }

    public void setNoteData(String noteData) {
        this.noteData = noteData;
        mNoteEditText.setText(noteData);
        mNoteEditText.setSelection(noteData.length());
    }

    @Override
    public void onClick(View v) {
        mNoteData = mNoteEditText.getText().toString();
        if (v == mTextYellow) {
            unselectAllHighlightDrawable();
            setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_orange_color));
            selectHighlightDrawable(mTextYellow);
        } else if (v == mTextRed) {
            unselectAllHighlightDrawable();
            setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_pink_color));
            selectHighlightDrawable(mTextRed);
        } else if (v == mTextPurple) {
            unselectAllHighlightDrawable();
            setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_new_purple_color));
            selectHighlightDrawable(mTextPurple);
        } else if (v == mTextGreen) {
            unselectAllHighlightDrawable();
            setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_green_color));
            selectHighlightDrawable(mTextGreen);
        } else if (v == mTextBlue) {
            unselectAllHighlightDrawable();
            setBackgroundColorOfHighlightContainer(getContext().getResources().getString(R.string.note_blue_color));
            selectHighlightDrawable(mTextBlue);
        } else if (v == mTextDelete) {
            Utils.hideKeyboard(mContext, mTextDelete);
            mListeners.onDeleteClicked(mHighlightVo);
        } else if (v == mTextSave) {
            mHighlightVo.setColor(getContainerBGColor()); // set seleted hightlight color in vo
            mHighlightVo.setNoteData(mNoteData);
            mListeners.onSaveClicked(mHighlightVo);
        } else if (v == mTextShare) {
           // mListeners.onSharePostClick(mHighlightVo);
            mHighlightVo.setNoteData(mNoteData);
            mListeners.onShareClicked(mHighlightVo,getContainerBGColor());
        }
        else if (v == btnPost) {
            mListeners.onCommentPostClick(edtPost.getText().toString(), mHighlightVo);
            edtPost.setText("");
            prepareCommentLists();
            Utils.hideKeyboard(mContext, edtPost);
        }
        else if (v == tv_overflow_share) {
            mHighlightVo.setNoteData(mNoteData);
            mListeners.onShareClicked(mHighlightVo,getContainerBGColor());
        }
        else if (v == tv_overflow_post) {
            mHighlightVo.setColor(getContainerBGColor()); // set seleted hightlight color in vo
            mHighlightVo.setNoteData(mNoteData);
            mListeners.onSaveClicked(mHighlightVo);
        }
        else if (v == tv_comment_overflow) {

            if(mUserId != null)
                viewVisibilityHandlingForOverflowMenuNoteStatus(mUserId);

            tv_comment_overflow.setVisibility(View.GONE);
            ll_expanded_comment_overflow.setVisibility(View.VISIBLE);
        }
        else if (v == tv_close_expanded_overflow) {
            tv_comment_overflow.setVisibility(View.VISIBLE);
            ll_expanded_comment_overflow.setVisibility(View.GONE);
        }
    }

    private void getCommentsFromDB() {
        mHighlightVo.getCommentVos().clear();
        ArrayList<HighlightVO> highLightVOColl = new ArrayList<HighlightVO>();
        HighlightVO localComment = new HighlightVO();
        highLightVOColl = DatabaseManager.getInstance(mContext).getHighlightByChapter(mBookID,
                KitabooSDKModel.getInstance().getUserID(), mCurrentFolioID);
        for (HighlightVO highLightVO : highLightVOColl) {
            if (mHighlightVo.getLocalID() == highLightVO.getLocalID()) {
                localComment = highLightVO;
                break;
            }
        }
        for (CommentsVO commentsVO : localComment.getCommentVos()) {
            mHighlightVo.getCommentVos().add(commentsVO);
        }
    }

    private void setCommentsDataForFirstNote() {
        if(!mIsUGCSharingEnable){
            hideCommentContainer(true);
        }else{
            hideCommentContainer(false);
        }
        if (mHighlightVo.getLocalID() == 0) {
            hideCommentContainer(true);
            setEnableChooseColorPanel(true);
        } else {
            if (!mHighlightVo.getNoteData().isEmpty()) {
                Utils.hideKeyboard(mContext, mNoteEditText);
                setCommentsSettings();
                if (mHighlightVo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID()) {
                    mTextDelete.setVisibility(View.VISIBLE);
                    setEnableChooseColorPanel(true);
                }
            } else {
                hideCommentContainer(true);
                setEnableChooseColorPanel(true);
            }
        }
        /*if (GlobalDataManager.getInstance().getLocalBookData()
                .IsClassAssociated()) {
            *//*if (mIsSameUser) {
                mShareDivide.setVisibility(View.VISIBLE);
                mBtnShare.setVisibility(View.VISIBLE);
            }else{
                mShareDivide.setVisibility(View.GONE);
                mBtnShare.setVisibility(View.GONE);
            }*//*
        } else {
            hideCommentContainerForFirstNote();
            mImportantBtnHolder.setEnabled(true);
            *//*mShareDivide.setVisibility(View.GONE);
            mBtnShare.setVisibility(View.GONE);*//*
        }*/
        prepareCommentLists();

    }

    private void prepareCommentLists() {
        if (mHighlightVo.getCommentVos().size() != 0) {

            if(mMembersListCollHolder.getVisibility() == View.GONE) mMembersListCollHolder.setVisibility(View.VISIBLE);
            mCommentListAdapter.setData(mHighlightVo.getCommentVos());

            if (mMembersListCollHolder.getAdapter() == null) {
                mMembersListCollHolder.setAdapter(mCommentListAdapter);
            } else {
                mCommentListAdapter.notifyDataSetChanged();
            }
            mMembersListCollHolder.setSelection(mHighlightVo.getCommentVos().size() - 1);
        }
        else
        {
            mMembersListCollHolder.setVisibility(View.GONE);
        }

    }

    private void setCommentsSettings() {
        if(!mIsUGCSharingEnable){
            hideCommentContainer(true);
        }else{
            hideCommentContainer(false);
        }
        if (mHighlightVo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID() && mHighlightVo.getCommentVos().size() == 0) {
            if (SDKManager.getInstance().getRoleName().equals(Constants.TEACHER)) {

            } else {
                hideCommentContainer(true);
            }
        } else if (SDKManager.getInstance().getRoleName().equals(Constants.STUDENT) && !
                (mHighlightVo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID())) {
            if (mHighlightVo.getCommentVos().size() == 0) {
                hideCommentContainer(true);
            } else {
                //hideCommentContainerForSharedStudent();
            }
        }
    }

    private void hideCommentContainer(boolean hideCommentPanel) {
        if(hideCommentPanel)
        {
            rl_comment_overflow_panel.setVisibility(View.GONE);
            rl_comment_action_container.setVisibility(View.GONE);
            rl_normal_note_action_container.setVisibility(View.VISIBLE);
        }
        else
        {
            rl_comment_overflow_panel.setVisibility(View.VISIBLE);
            rl_comment_action_container.setVisibility(View.VISIBLE);
            rl_normal_note_action_container.setVisibility(View.GONE);
        }

    }

    private void selectHighlightDrawable(TextView text) {
        unselectAllHighlightDrawable();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setStroke(1, Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getSelectedIconBorder()));
        text.setBackground(drawable);
    }

    private void unselectAllHighlightDrawable() {
        for (int i = 0; i < mHighlightCollections.length; i++) {
            mHighlightCollections[i].setBackground(null);
        }
    }

    public void addStickyListeners(AddStickcyNoteActionListeners listeners) {
        mListeners = listeners;
    }

    private String getContainerBGColor()
    {
        int intColor = Color.TRANSPARENT;
        String hexColor  = String.format("#%06X", (0xFFFFFF & intColor));
        if(mHighlightColorContainer != null && mHighlightVo != null)
        {
            Drawable background = mHighlightColorContainer.getBackground(); // returns container color in hex
            if (background instanceof ColorDrawable)
                intColor = ((ColorDrawable) background).getColor();

            hexColor = String.format("#%06X", (0xFFFFFF & intColor));

            if (new HexValidator().validate(hexColor)) {
                return hexColor;
            }
        }
        return  hexColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackgroundColorOfHighlightContainer(backgroundColor);

        if (backgroundColor.equalsIgnoreCase("#cd3a3a") ||
                backgroundColor.equalsIgnoreCase("#F00004") ||
                backgroundColor.equalsIgnoreCase("#FF7061") || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_red_color)) || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_pink_color))) {
            selectHighlightDrawable(mTextRed);
        } else if (backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_yellow_color)) || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_orange_color))) {
            selectHighlightDrawable(mTextYellow);
        } else if (backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_purple_color)) || backgroundColor.equalsIgnoreCase("#8E44AD") || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_new_purple_color))) {
            selectHighlightDrawable(mTextPurple);
        } else if (backgroundColor.equalsIgnoreCase("#99cc00")
                || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_lightgreen_color)) || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_green_color))) {
            selectHighlightDrawable(mTextGreen);
        } else if (backgroundColor.equalsIgnoreCase("#33E5B5") || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_skyblue_color)) || backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_blue_color))) {
            selectHighlightDrawable(mTextBlue);
        } else if (backgroundColor.equalsIgnoreCase(getContext().getResources().getString(R.string.note_lightyellow_color))) {
            selectHighlightDrawable(mTextYellow);
        }
    }

    public void setHighlightedText(String highlightedText) {
        this.highlightedText = highlightedText;
        if (!highlightedText.isEmpty()) {
            mNoteHighlightedTextContainer.setVisibility(View.VISIBLE);
            mNoteHighlightedText.setText(highlightedText);
            setBackgroundColor(mHighlightVo.getColor());
        } else {
            mNoteHighlightedTextContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListeners.onStickyNoteDismissed();
    }

    public void removeViewForNoteShare(View view) {
        animstart=false;
        //sharingsettingpanel=view;
        //mNotelayout.startAnimation(_out_to_right);
        //mTopContainer.startAnimation(_out_to_right);
        // mNotelayout.removeView(view);
        mTopContainer.setInAnimation(_in_from_left);
        mTopContainer.setOutAnimation(_out_to_right);
        mTopContainer.setDisplayedChild(0);
        mTopContainer.removeViewAt(1);
    }

    public void buildViewForNoteShare(View view) {
       animstart=true;
        AnimateListener listener = new AnimateListener();
        _in_from_left = AnimationUtils.loadAnimation(mContext, R.anim.in_from_left);
        _out_to_right = AnimationUtils.loadAnimation(mContext, R.anim.out_to_right);
        _in_from_right = AnimationUtils.loadAnimation(mContext, R.anim.in_from_right);
        _out_to_left = AnimationUtils.loadAnimation(mContext, R.anim.out_to_left);
        _in_from_left.setAnimationListener(listener);
        _out_to_right.setAnimationListener(listener);
        _in_from_right.setAnimationListener(listener);
        _out_to_left.setAnimationListener(listener);
        // The Next screen will come in form Right and current Screen will go OUT from Left
        mTopContainer.addView(view, 1);
        //mTopContainer.startAnimation(_in_from_right);

        mTopContainer.setInAnimation(_in_from_right);
        mTopContainer.setOutAnimation(_out_to_left);
        // Show The Previous Screen
        mTopContainer.showPrevious();
    }

    private class AnimateListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation arg0) {
            if (animstart){
                //mNotelayout.addView(sharingsettingpanel, 1);
            }else {
                //mTopContainer.removeViewAt(1);
            }
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationStart(Animation arg0) {

        }

    }
}
