package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.customui.datamodel.UserClassVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.HighlightSettingSelectionListener;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.sharingSetting.ListItemUserDAO;


import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboo.constants.listener.OnDialogOkActionListner;
import com.hurix.kitaboocloud.PlayerController;
import com.hurix.kitaboocloud.R;

import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters.MyDataClassesSpinnerAdapter;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters.UGCSharingSettingAdapter;
import com.hurix.kitaboocloud.notifier.GlobalDataManager;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.service.Interface.IServiceResponse;
import com.hurix.service.Interface.IServiceResponseListener;
import com.hurix.service.exception.ServiceException;
import com.hurix.service.networkcall.SERVICETYPES;
/*import com.hurix.common.services.adapter.KitabooServiceAPI;
import com.hurix.common.services.kitaboo.response.RefreshUserTokenResponse;*/

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


public class CustomUGCSharingSettingsScreen extends LinearLayout implements OnClickListener,
        OnItemSelectedListener, HighlightSettingSelectionListener, IServiceResponseListener {

    public static final String STUDENT = "LEARNER";
    public static final String TEACHER = "INSTRUCTOR";


    private Context mContext;
    private LayoutInflater mLayoutinfilater;
    private View mView/*, mBtnHorizontalDividerView*/, mBtnDividerView, mSettingDividerID;
    private LinearLayout mBackButtonHolder;
    private CustomMyDataFragment mUgcHolder;
    private CustomMyDataTabDialogFragment mUgcHolderTab ;
    private TextView/* mBackBtnArrow,*/ mTitle, mSharetxt, mRecievetxt, /*mClassSelect,*/ mShareCheckBox, mRecieveCheckBox;
    private Button mBtnCancel, mBtnDone;
    //private RelativeLayout mClassesDropDownContainer;
    private Spinner mClassesSpinner;
    private MyDataClassesSpinnerAdapter mClassesSpinnerAdp;
    private ProgressBar mSettingProgressbar;
    private TextView mSettingAlertMessage;
    ListView mUsersList;
    UGCSharingSettingAdapter mAdapter;
    private ArrayList<ListItemUserDAO> mHighlightSettingUserlist;
    ArrayList<ListItemUserDAO> mModifiedUserDaos;
    private LinearLayout mBottomBtnHolder;
    UserClassVO mCurrentLoadingClass;
    private TextView mNoUserInGroup;
    private LinearLayout mShareReceiveBtnsHolder;
    private View mDivider4;
    private ArrayList<IClass> arrayList = null;
    private long userID;
    private String RoleName;
    private CustomISharingSettingListner iSharingSettingListner;
    private boolean selectedColor;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private Typeface typeFace;
    private Context shreSettingListener;
    private boolean mIsMobile ;
    private Dialog mDialog;
    private RelativeLayout mLayoutholder;

    public CustomUGCSharingSettingsScreen(Context context, CustomMyDataFragment ugcBaseDataFragment, ReaderThemeSettingVo themeUserSettingVo , boolean isMobile) {
        super(context);
        mContext = context;
        iSharingSettingListner=(CustomISharingSettingListner)context;
        mUgcHolder = ugcBaseDataFragment;
        mReaderThemeSettingVo = themeUserSettingVo;
        this.mIsMobile = isMobile ;
        initUI();

    }

    public CustomUGCSharingSettingsScreen(Context context, CustomMyDataTabDialogFragment tabMyData, ReaderThemeSettingVo themeUserSettingVo, boolean isMobile, Dialog dialog) {
        super(context);
        mContext = context;
        iSharingSettingListner=(CustomISharingSettingListner)context;
        mUgcHolderTab = tabMyData;
        mReaderThemeSettingVo = themeUserSettingVo;
        this.mIsMobile = isMobile ;
        mDialog = dialog;
        initUI();
    }

    public CustomUGCSharingSettingsScreen(Context context) {
        super(context);
    }

    public CustomUGCSharingSettingsScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomUGCSharingSettingsScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initUI(){
        mLayoutinfilater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mLayoutinfilater.inflate(R.layout.ugc_share_settings, this);
        mUsersList = (ListView) mView.findViewById(R.id.membersListHolder);
        mNoUserInGroup = (TextView) mView.findViewById(R.id.noUserInGroup);
        mLayoutholder = (RelativeLayout) mView.findViewById(R.id.layoutholder);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mBackButtonHolder = (LinearLayout) mView.findViewById(R.id.backBtnHolder);
        mBackButtonHolder.setOnClickListener(this);
        //mBackBtnArrow = (TextView) mView.findViewById(R.id.backBtnTV);
        mBtnCancel = (Button) mView.findViewById(R.id.btnCancel);
        mBtnDone = (Button) mView.findViewById(R.id.btnDone);
        mShareCheckBox = (TextView) mView.findViewById(R.id.sharecheckBox);
        mRecieveCheckBox = (TextView) mView.findViewById(R.id.receivecheckBox);

        mShareCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mShareCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mRecieveCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mRecieveCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));

        //mBtnHorizontalDividerView = mView.findViewById(R.id.btnHorizontalDividerView);
        mBtnDividerView = mView.findViewById(R.id.btnDividerView);
        mSettingDividerID = mView.findViewById(R.id.settingdividerID);
        mSharetxt = (TextView) mView.findViewById(R.id.shareTabTV);
        mRecievetxt = (TextView) mView.findViewById(R.id.receiveTabTV);
        //mClassSelect = (TextView) mView.findViewById(R.id.shareThisNoteWithLableTV);
        mShareReceiveBtnsHolder = (LinearLayout) mView.findViewById(R.id.shareReceiveBtnsHolder);
        mBtnCancel.setOnClickListener(this);
        mBtnDone.setOnClickListener(this);
        mShareCheckBox.setOnClickListener(this);
        mRecieveCheckBox.setOnClickListener(this);
        //mClassesDropDownContainer = (RelativeLayout) mView.findViewById(R.id.classesDropDownContainer);
        mClassesSpinner = (Spinner) mView.findViewById(R.id.classesSpinner);
        //mClassesSpinner.getPopupBackground(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getBoxBorderColor());
        if(getResources().getBoolean(R.bool.is_BE_Publishing) || getResources().getBoolean(R.bool.is_BH365_client)) {
            mClassesSpinner.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getMyData().getSettings().getBoxBorderColor()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getMyData().getSettings().getCheckColor())));
        }
        mClassesSpinner.setOnItemSelectedListener(this);
        mClassesSpinnerAdp = new MyDataClassesSpinnerAdapter(mContext, iSharingSettingListner);
        mSettingProgressbar = (ProgressBar) mView.findViewById(R.id.settingProgressbarID);
        mSettingAlertMessage = (TextView) mView.findViewById(R.id.messagetvID);
        mSettingAlertMessage.setText(getResources().getString(R.string.network_error));
        mBottomBtnHolder = (LinearLayout) mView.findViewById(R.id.bottomBtnsHolder);
        mDivider4 = findViewById(R.id.divider4);
        mLayoutholder.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getBackground()));
        mBottomBtnHolder.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getBackground()));

        /*mBottomBtnHolder.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.WHITE,
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mThemeUserSettingVo.getmKitabooMainColor())));*/
        mShareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
        mRecieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
        mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getIconColor()));
        mShareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getIconColor()));

        mHighlightSettingUserlist = new ArrayList<ListItemUserDAO>();

        RelativeLayout shareTab = (RelativeLayout) mView.findViewById(R.id.shareTab);
        RelativeLayout receiveTab = (RelativeLayout) mView.findViewById(R.id.receiveTab);

        int shareLeftPadding = (int) getContext().getResources().getDimension(R.dimen.share_left_padding);
        int shareRightPadding = (int) getContext().getResources().getDimension(R.dimen.share_right_padding);
        int shareTopPadding = (int) getContext().getResources().getDimension(R.dimen.share_top_padding);
        int shareBottomPadding = (int) getContext().getResources().getDimension(R.dimen.share_bottom_padding);

        int recLeftPadding = (int) getContext().getResources().getDimension(R.dimen.rec_left_padding);
        int recRightPadding = (int) getContext().getResources().getDimension(R.dimen.rec_right_padding);
        int recTopPadding = (int) getContext().getResources().getDimension(R.dimen.rec_top_padding);
        int recBottomPadding = (int) getContext().getResources().getDimension(R.dimen.rec_bottom_padding);

        receiveTab.setPadding(recLeftPadding, recTopPadding, recRightPadding, recBottomPadding);
        shareTab.setPadding(shareLeftPadding, shareTopPadding, shareRightPadding, shareBottomPadding);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)receiveTab.getLayoutParams();
            relativeParams.setMargins(0, 0, 120, 0);  // left, top, right, bottom
            receiveTab.setLayoutParams(relativeParams);

            /*RelativeLayout.LayoutParams relativeParams1 = (RelativeLayout.LayoutParams)shareTab.getLayoutParams();
            relativeParams.setMargins(0, 0, 50, 0);  // left, top, right, bottom
            shareTab.setLayoutParams(relativeParams1);*/
        }
        else{

            RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)receiveTab.getLayoutParams();
            relativeParams.setMargins(0, 0, 50, 0);  // left, top, right, bottom
            receiveTab.setLayoutParams(relativeParams);




        }

        setUpIconFonts();
        setUpTheme();
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void setUpTheme() {
       /* mBtnCancel.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().getReaderFont()));
        mBtnDone.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mBackBtnArrow.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mTitle.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mSharetxt.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mRecievetxt.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mSettingDividerID.setBackgroundColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));
        mClassSelect.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().getReaderFont()));*/
        mBtnCancel.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getActionButton().getCancel().getTextColor()));
        mBtnDone.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getTextColor()));
        mBtnDone.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        //mBackBtnArrow.setTextColor(Color.parseColor(mThemeUserSettingVo.getmKitabooMainColor()));
        mTitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getSectionTitleColor()));
        mSharetxt.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getSectionTitleColor()));
        mRecievetxt.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getSectionTitleColor()));

        mSettingDividerID.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getSectionTitleColor()));
        //mClassSelect.setTextColor(Color.parseColor(mThemeUserSettingVo.getmKitabooMainColor()));
        mSharetxt.setTextSize(15);
        mRecievetxt.setTextSize(15);
        // if(iSharingSettingListner!=null)
        // iSharingSettingListner.customizeOuterViewSharingSettingPanel(mView,mShareCheckBox,mRecieveCheckBox, mBtnCancel, mBtnDone, mBackBtnArrow, mTitle, mSharetxt,  mRecievetxt, mSettingDividerID, mClassSelect,mBottomBtnHolder);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBackButtonHolder.getId()) {
            //disableButtons();
            if (mIsMobile){
                mUgcHolder.openMainScreen();
            }else {
                mUgcHolderTab.openMainScreen();
            }

        } else if (v.getId() == mBtnCancel.getId()) {
            // disableButtons();
            if (mIsMobile){
                mUgcHolder.openMainScreen();
            }else {
                if (mDialog != null && !((Activity)mContext).isFinishing())
                    mDialog.dismiss();
                mUgcHolderTab.openMainScreen();
            }
        } else if (v.getId() == mBtnDone.getId()) {
            saveHighlightShareReceiveSetting(true);
        } else if (v == mShareCheckBox) {
            toggleShareSharedWithUser();
        } else if (v == mRecieveCheckBox) {
            toggleReceiveSharedWithUser();
        }
    }

    private void toggleShareSharedWithUser() {
        if (!checkForShareAllSelect()) {
            if (mHighlightSettingUserlist != null) {
                for (int i = 0; i < mHighlightSettingUserlist.size(); i++) {
                    if (mHighlightSettingUserlist.get(i).getUserName() != null) {
                        mHighlightSettingUserlist.get(i).sethighlightSharedWithUser(true);
                    }
                }
                mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
                mShareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getIconColor()));
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (mHighlightSettingUserlist != null) {
                for (int i = 0; i < mHighlightSettingUserlist.size(); i++) {
                    if (mHighlightSettingUserlist.get(i).getUserName() != null) {
                        if (!mHighlightSettingUserlist.get(i).getRoleName().equals(TEACHER)) {
                            mHighlightSettingUserlist.get(i).sethighlightSharedWithUser(false);
                        }
                    }
                }
                /*if (RoleName.equals(STUDENT)) {
                    mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
                    mShareCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));
                } else {*/
                    mShareCheckBox.setText("");
                //}
                if (mAdapter !=  null){
                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    private void toggleReceiveSharedWithUser() {
        if (!checkForReceiveAllSelect()) {
            if (mHighlightSettingUserlist != null) {
                for (int i = 0; i < mHighlightSettingUserlist.size(); i++) {
                    if (mHighlightSettingUserlist.get(i).getUserName() != null) {
                        mHighlightSettingUserlist.get(i).sethighlightRecieveWithUser(true);
                    }
                }
                mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
                mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getIconColor()));
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (mHighlightSettingUserlist != null) {
                for (int i = 0; i < mHighlightSettingUserlist.size(); i++) {
                    if (mHighlightSettingUserlist.get(i).getUserName() != null) {
                        if (!mHighlightSettingUserlist.get(i).getRoleName().equals(TEACHER)) {
                            mHighlightSettingUserlist.get(i).sethighlightRecieveWithUser(false);
                        }
                    }
                }
                /*if (RoleName.equals(STUDENT)) {
                    mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
                    mRecieveCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));
                } else {*/
                    mRecieveCheckBox.setText("");
                //}
                if (mAdapter != null){
                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    private boolean checkForShareAllSelect() {
        boolean allShareCheck = true;
        for (ListItemUserDAO itemUserDAO : mHighlightSettingUserlist) {
            if (itemUserDAO.getUserName() != null) {
                if (!itemUserDAO.ishighlightSharedWithUser()) {
                    allShareCheck = false;
                    break;
                }
            }
        }
        return allShareCheck;
    }

    private boolean checkForReceiveAllSelect() {
        boolean allReceiveCheck = true;
        for (ListItemUserDAO itemUserDAO : mHighlightSettingUserlist) {
            if (itemUserDAO.getUserName() != null) {
                if (!itemUserDAO.ishighlightRecieveWithUser()) {
                    allReceiveCheck = false;
                    break;
                }
            }
        }
        return allReceiveCheck;
    }


    private void saveHighlightShareReceiveSetting(boolean isSendEnable) {
        boolean blnChanged = false;
        ArrayList<ListItemUserDAO> tempUserLists = new ArrayList<ListItemUserDAO>();
        try {
            if (mHighlightSettingUserlist != null && mHighlightSettingUserlist.size() > 0) {
                for (ListItemUserDAO selectedUserDAO : mHighlightSettingUserlist) {
                    if (selectedUserDAO.isActionTaken()) {
                        tempUserLists.add(selectedUserDAO);
                        blnChanged = true;
                    }
                }
                mModifiedUserDaos = tempUserLists;
                if (!isSendEnable) {
                    updateHighlightSetting();
                } else {
                    if (blnChanged) {
                        if (Utils.isOnline(mContext)) {
                            mSettingProgressbar.setVisibility(View.VISIBLE);
                            updateHighlightSetting();
                            if (iSharingSettingListner != null)
                                iSharingSettingListner.syncUserForHighlightSettingService(arrayList, this);
                        } else {
                            DialogUtils.displayToast(mContext, getResources().getString(R.string.network_error),
                                    Toast.LENGTH_SHORT, Gravity.CENTER);

                        }
                    } else {
                        DialogUtils.displayToast(mContext, getResources().getString(R.string.ugc_setting_select_message),
                                Toast.LENGTH_SHORT, Gravity.CENTER);

                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void setUpIconFonts() {
      /*  Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.kitaboo_font_file_name));
        mBackBtnArrow.setTypeface(typeFace);*/
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(mContext, fontfile);
        } else {
            typeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
        /*mBackBtnArrow.setTypeface(typeFace);
        mBackBtnArrow.setAllCaps(false);
        mBackBtnArrow.setText(PlayerUIConstants.UD_SHARE_SETTINGS_BACK_IC_TEXT);
        mBackBtnArrow.setTextColor(PlayerUIConstants.UD_SHARE_SETTINGS_BACK_IC_FC);*/

        mShareCheckBox.setTypeface(typeFace);
        mShareCheckBox.setAllCaps(false);
        mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
        // mShareCheckBox.setTextColor(getResources().getColor(R.color.kitaboo_main_color));
        /*mShareCheckBox.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));*/
        mShareCheckBox.setAlpha(1f);
        mRecieveCheckBox.setTypeface(typeFace);
        mRecieveCheckBox.setAllCaps(false);
        mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
        mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getIconColor()));
        mRecieveCheckBox.setAlpha(1f);
    }

    private void setShareReceiveComponent() {
        highlightReceiveSelected();
        highlightSharedSelected();
    }

    @Override
    public void highlightReceiveSelected() {
        int selected = 0;
        int header = 0;
        for (ListItemUserDAO itemUserDAO : mHighlightSettingUserlist) {
            if (itemUserDAO.getUserName() != null) {
                if (itemUserDAO.ishighlightRecieveWithUser()) {
                    selected++;
                }
            } else {
                header++;
            }
        }
        if (selected == mHighlightSettingUserlist.size() - header) {
            mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
            mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getCheckColor()));
        } else if (selected == 0) {
            mRecieveCheckBox.setText("");
        } else {
            /*mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
            mRecieveCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));*/
            mRecieveCheckBox.setText("");
        }
    }

    @Override
    public void highlightSharedSelected() {
        int selected = 0;
        int header = 0;
        for (ListItemUserDAO itemUserDAO : mHighlightSettingUserlist) {
            if (itemUserDAO.getUserName() != null) {
                if (itemUserDAO.ishighlightSharedWithUser()) {
                    selected++;
                }
            } else {
                header++;
            }
        }
        if (selected == mHighlightSettingUserlist.size() - header) {
            mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
            mShareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getIconColor()));
        } else if (selected == 0) {
            mShareCheckBox.setText("");
        } else {
            /*mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
            mShareCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));*/
            mShareCheckBox.setText("");
        }
    }

    public void setData(ArrayList<IClass> allarrayList, Long userId, String _RoleName) {
        arrayList = allarrayList;
        userID = userId;
        RoleName = _RoleName;
        //mThemeUserSettingVo = themeUserSettingVo;
        if (arrayList != null) {
            mClassesSpinnerAdp.setData(arrayList,mReaderThemeSettingVo);
            mClassesSpinner.setAdapter(mClassesSpinnerAdp);
            mClassesSpinnerAdp.notifyDataSetChanged();
            mClassesSpinner.setSelection(0);
            if (arrayList.size() > 0) {
                mClassesSpinner.setSelection(0);
                mCurrentLoadingClass = (UserClassVO) arrayList.get(0);
                mHighlightSettingUserlist = prepareLists((UserClassVO) arrayList.get(0));
                setDoneButtonClickListner(true);
                if (arrayList.size() > 1) {
                    mClassesSpinner.setEnabled(true);
                    mClassesSpinner.setOnItemSelectedListener(this);
                    //findViewById(R.id.shareThisNoteWithLableTV).setVisibility(View.VISIBLE);
                } else {
                    mClassesSpinner.setEnabled(false);
                    //findViewById(R.id.shareThisNoteWithLableTV).setVisibility(View.INVISIBLE);
                }
            } else {
                setDoneButtonClickListner(false);
            }
            mAdapter = new UGCSharingSettingAdapter(mContext, iSharingSettingListner,this);
            mAdapter.setData(mHighlightSettingUserlist, mReaderThemeSettingVo);
            mUsersList.setAdapter(mAdapter);
            mUsersList.setDivider(null);
            mUsersList.setDividerHeight(-1);
            if (mHighlightSettingUserlist != null && mHighlightSettingUserlist.size() > 0) {
                setShareReceiveComponent();
            } else {
                mUsersList.setEmptyView(mNoUserInGroup);
                mUsersList.getEmptyView().setVisibility(VISIBLE);
                mShareCheckBox.setOnClickListener(null);
                mRecieveCheckBox.setOnClickListener(null);
                mShareReceiveBtnsHolder.setVisibility(GONE);
                mSettingDividerID.setVisibility(GONE);
                mSettingProgressbar.setVisibility(View.GONE);
                mSettingAlertMessage.setText(mContext.getResources().getString(R.string.alert_no_data));
                mSettingAlertMessage.setVisibility(View.GONE);
                //mBtnDone.setTextColor(getResources().getColor(R.color.reader_font_color));
                mBtnDone.setEnabled(false);
                //mBtnDone.setAlpha(0.2f);
            }

        }
    }

    private ArrayList<ListItemUserDAO> prepareLists(UserClassVO userClassVO) {
        mSettingProgressbar.setVisibility(View.GONE);
        mSettingAlertMessage.setVisibility(View.GONE);
        ArrayList<ListItemUserDAO> studentList = new ArrayList<ListItemUserDAO>();
        ArrayList<ListItemUserDAO> teacherList = new ArrayList<ListItemUserDAO>();
        ArrayList<ListItemUserDAO> listItem = new ArrayList<ListItemUserDAO>();

        for (int i = 0; i < userClassVO.getStudentList().size(); i++) {
            if (userID != userClassVO.getStudentList().get(i).getUserID()) {
                ListItemUserDAO userDao = getListItemUserDAO(userClassVO.getStudentList().get(i));
                if (userDao.getRoleName().equalsIgnoreCase(STUDENT)) {
                    userDao.sethighlightSharedWithUser(getHighlightSharedChecked(userDao.getUserID(),
                            userClassVO.getShareUserList()));
                    userDao.sethighlightRecieveWithUser(getHighlightReceivedChecked(userDao.getUserID(),
                            userClassVO.getRecieveUserList()));
                    studentList.add(userDao);
                } else {
                    userDao.sethighlightSharedWithUser(true);
                    userDao.sethighlightRecieveWithUser(true);
                    userDao.setEnabled(false);
                    if (!RoleName.equalsIgnoreCase(TEACHER)) {
                        teacherList.add(userDao);
                    }
                }
            }
        }


        /*if (teacherList.size() > 0) {
            ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
            listItemUserDAO.setDisplayName(getResources().getString(R.string.teacher));
            listItemUserDAO.setSection(true);
            listItem.add(listItemUserDAO);
            for (ListItemUserDAO item : teacherList) {
                listItem.add(item);

            }
        }
        if (studentList.size() > 0) {
            ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
            listItemUserDAO.setDisplayName(getResources().getString(R.string.student));
            listItemUserDAO.setSection(true);
            listItem.add(listItemUserDAO);
            for (ListItemUserDAO item : studentList) {
                listItem.add(item);

            }
        }*/

        if (teacherList.size() > 0) {
            /*if (RoleName.equalsIgnoreCase(STUDENT)
                    && userClassVO.isHighLightStudentTeacherSharing()) {*/
                ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
                listItemUserDAO.setDisplayName(getResources().getString(R.string.teacher));
                listItemUserDAO.setSection(true);
                listItem.add(listItemUserDAO);
                listItemUserDAO.setRoleName(TEACHER);
                long refID = 0;
                for (IUser user : userClassVO.getStudentList()) {
                    if (user.getRoleName().equalsIgnoreCase(TEACHER)) {
                        refID = user.getUserID();
                        break;
                    }
                }
                listItemUserDAO.sethighlightSharedWithUser(getHighlightSharedChecked(refID,
                        userClassVO.getShareUserList()));
                listItemUserDAO.sethighlightRecieveWithUser(getHighlightReceivedChecked(refID,
                        userClassVO.getRecieveUserList()));
                for (ListItemUserDAO item : teacherList) {
                    listItem.add(item);
                }
            //}
        }
        if (studentList.size() > 0) {
                if(getResources().getBoolean(R.bool.is_Academic_Approach)){
                    if(RoleName.equalsIgnoreCase(STUDENT) && userClassVO.isHighLightStudentStudentSharing()){
                        // Do not add the student lists.
                    }else{
                        ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
                        listItemUserDAO.setDisplayName(getResources().getString(R.string.student));
                        listItemUserDAO.setSection(true);
                        listItem.add(listItemUserDAO);
                        for (ListItemUserDAO item : studentList) {
                            listItem.add(item);

                        }
                    }
                }else{
                    /*if (RoleName.equalsIgnoreCase(TEACHER)
                        && userClassVO.isHighLightTeacherStudentSharing()
                        || RoleName.equalsIgnoreCase(STUDENT)
                        && userClassVO.isHighLightStudentStudentSharing()) {*/
                    ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
                    listItemUserDAO.setDisplayName(getResources().getString(R.string.student));
                    listItemUserDAO.setSection(true);
                    listItem.add(listItemUserDAO);
                    for (ListItemUserDAO item : studentList) {
                        listItem.add(item);

                    }
                }
            }
        //}
        return listItem;
    }

    private boolean getHighlightSharedChecked(long refUserID, HashSet<Integer> shareUserList) {
        for (Integer integer : shareUserList) {
            if (integer.intValue() == refUserID) {
                return true;
            }
        }
        return false;
    }

    private boolean getHighlightReceivedChecked(long refUserID, HashSet<Integer> receiveUserList) {
        for (Integer integer : receiveUserList) {
            if (integer.intValue() == refUserID) {
                return true;
            }
        }
        return false;
    }


    private ListItemUserDAO getListItemUserDAO(IUser iUser) {
        ListItemUserDAO tempUserDao = new ListItemUserDAO();
        tempUserDao.setUserID(iUser.getUserID());
        tempUserDao.setFirstName(iUser.getFirstName());
        tempUserDao.setLastName(iUser.getLastName());
        tempUserDao.setEmail(iUser.getEMail());
        tempUserDao.setDisplayName(iUser.getDisplayName());
        tempUserDao.setUserName(iUser.getUserName());
        tempUserDao.setPassword(iUser.getPassword());
        tempUserDao.setToken(iUser.getToken());
        tempUserDao.setClientID(iUser.getClientID());
        tempUserDao.setRoleName(iUser.getRoleName());
        tempUserDao.setActionTaken(true);
        return tempUserDao;

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
        saveHighlightShareReceiveSetting(false);
        if(arrayList.size() > position)
        {
            mCurrentLoadingClass = (UserClassVO) arrayList.get(position);
            mHighlightSettingUserlist = prepareLists((UserClassVO) arrayList.get(position));
        }
        if (!mHighlightSettingUserlist.isEmpty()) {
            mAdapter.setData(mHighlightSettingUserlist, mReaderThemeSettingVo);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.setData(mHighlightSettingUserlist, mReaderThemeSettingVo);
            mAdapter.notifyDataSetChanged();
            mUsersList.setEmptyView(mNoUserInGroup);
            mUsersList.getEmptyView().setVisibility(VISIBLE);
            mShareCheckBox.setOnClickListener(null);
            mRecieveCheckBox.setOnClickListener(null);
            mShareReceiveBtnsHolder.setVisibility(GONE);
            mSettingDividerID.setVisibility(GONE);
            mSettingProgressbar.setVisibility(View.GONE);
            mSettingAlertMessage.setText(mContext.getResources().getString(R.string.alert_no_data));
            mSettingAlertMessage.setVisibility(View.GONE);
          /*  mBtnDone.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                    .getReaderFont()));*/
            mBtnDone.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
            mBtnDone.setEnabled(false);
            mBtnDone.setAlpha(0.5f);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void enableButtons() {
        mBackButtonHolder.setEnabled(true);
        mBtnCancel.setEnabled(true);
        mBtnDone.setEnabled(true);
    }

    public void disableButtons() {
        mBackButtonHolder.setEnabled(false);
        mBtnCancel.setEnabled(false);
        mBtnDone.setEnabled(false);

    }

    private void updateHighlightSetting() {

        HashSet<Integer> highlightShareSettinglist = new HashSet<Integer>();
        HashSet<Integer> highlightReceiveSettinglist = new HashSet<Integer>();

        for (ListItemUserDAO userDAO : mModifiedUserDaos) {
            if (userDAO.ishighlightSharedWithUser()) {
                highlightShareSettinglist.add((int) userDAO.getUserID());
            }
            if (userDAO.ishighlightRecieveWithUser()) {
                highlightReceiveSettinglist.add((int) userDAO.getUserID());
            }

        }

        for (IClass iClass : arrayList) {
            UserClassVO userclassVO = (UserClassVO) iClass;
            if (userclassVO.getID() == mCurrentLoadingClass.getID()) {
                userclassVO.setShareUserList(highlightShareSettinglist);
                userclassVO.setRecieveUserList(highlightReceiveSettinglist);
            }

        }
    }

    private void setDoneButtonClickListner(boolean state) {
        if (state) {
            mBtnDone.setOnClickListener(this);
        } else {
            mBtnDone.setOnClickListener(null);

        }
    }

    private void setAnyStudentHvPermissionToShareData() {
        try {
            SDKManager.getInstance().setAnyStudentHvPermissionToShareData(false);
            if (!arrayList.isEmpty()) {
                    UserClassVO userClass = ((UserClassVO) arrayList.get(0));
                    for (com.hurix.customui.interfaces.IUser iuser : userClass.getStudentList()) {
                        if(iuser.getRoleName().equals(com.hurix.kitaboo.constants.Constants.STUDENT))
                        {
                            if (getHighlightSharedChecked(iuser.getUserID(),
                                    userClass.getShareUserList())) {
                                SDKManager.getInstance().setAnyStudentHvPermissionToShareData(true);
                            }
                        }
                    }
            }
        } catch (Exception e) {
            if (com.hurix.kitaboo.constants.Constants.IS_DEBUG_ENABLED) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestCompleted(IServiceResponse response) {
        if (response.getResponseRequestType().equals(SERVICETYPES.HIGHLIGHTSETTING)) {
            try {
                mSettingProgressbar.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    setAnyStudentHvPermissionToShareData();
                    disableButtons();
                    if (mIsMobile){
                        mUgcHolder.openMainScreen();
                    }else {
                        if (mDialog != null && !((Activity)mContext).isFinishing())
                            mDialog.dismiss();
                       mUgcHolderTab.openMainScreen();
                    }

                } else {
                    DialogUtils.displayToast(mContext, getResources()
                            .getString(R.string.alert_error_save_settings), Toast.LENGTH_LONG, Gravity.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showSessionExpiredAlert() {
        com.hurix.kitaboo.constants.dialog.DialogUtils.showOKAlert(new View(mContext), 1, mContext, getResources()
                        .getString(R.string.alert_title), getResources().getString(R.string.alert_session_expired),
                new OnDialogOkActionListner() {
                    @Override
                    public void onOKClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("Action","LOGOUT_BY_USERID");
                        intent.setAction("DBCONTROLLER_RECIVER");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        com.hurix.kitaboo.constants.utils.Utils.startLauncherActivity(mContext);
                        GlobalDataManager.getInstance().setWebViewClosedAfterTokenReceived(false);
                    }
                });
    }

    @Override
    public void requestErrorOccured(ServiceException exeption) {
        if (exeption != null) {
            ServiceException exceptionObj = exeption;
            if (exceptionObj.getInvalidFields() != null && !exceptionObj.getInvalidFields().isEmpty()) {
                Map.Entry<String, Integer> entry = exceptionObj.getInvalidFields().entrySet().iterator().next();
                if (PlayerController.getInstance(getContext()).isAutoLogOffEnabled()
                        && entry.getValue() == 103) {
                    showSessionExpiredAlert();
                } else {
                    /*if (entry.getValue() == 103 && !exeption.getResponseRequestType().equals(Constants.SERVICETYPES.REFRESH_USER_TOKEN)) {
                        KitabooServiceAPI.getObject().getServiceAdapter()
                                .refreshUserToken(UserController.getInstance(mContext).getUserVO().getToken(), exeption.getResponseRequestType().toString(), new com.hurix.common.services.listener.IServiceResponseListener() {
                                    @Override
                                    public void requestCompleted(com.hurix.common.services.interfaces.IServiceResponse response) throws JSONException {
                                        RefreshUserTokenResponse responseObj = (RefreshUserTokenResponse) response;
                                        KitabooSDKModel.getInstance().setUserToken(responseObj.getUserVO().getToken());
                                        DBController.getInstance(mContext).getManager().updateUserToken(responseObj.getUserVO());
                                        if (responseObj.getCallbackRequestType().equalsIgnoreCase(SERVICETYPES.HIGHLIGHTSETTING.toString())){
                                            mBtnDone.callOnClick();
                                        }
                                    }

                                    @Override
                                    public void requestErrorOccured(com.hurix.common.services.exception.ServiceException exeption) {
                                        String a= "";
                                    }
                                });
                    } else if (exceptionObj.getResponseRequestType().equals(Constants.SERVICETYPES.REFRESH_USER_TOKEN)) {
                        com.hurix.kitaboo.constants.dialog.DialogUtils.displayToast(mContext, mContext.getResources().getString(R.string.alert_session_expired), Toast.LENGTH_SHORT, Gravity.CENTER);
                    }*/
                }
            }else {
                mSettingProgressbar.setVisibility(View.GONE);
                DialogUtils.displayToast(mContext, getResources().getString(R.string.alert_error_save_settings),
                        Toast.LENGTH_LONG, Gravity.CENTER);
            }
        }



    }

    public void setShreSettingListener(Context shreSettingListener) {
        this.shreSettingListener = shreSettingListener;
    }
}
