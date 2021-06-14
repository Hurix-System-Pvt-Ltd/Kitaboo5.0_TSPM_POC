package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.datamodel.UserClassVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IStickyNoteShareSettingListener;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.sharingSetting.ClassesSpinnerAdapter;
import com.hurix.customui.sharingSetting.ListItemUserDAO;
import com.hurix.customui.sharingSetting.StickyNoteShareAdapter;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;
import java.util.HashSet;

import static com.hurix.commons.Constants.Constants.STUDENT;
import static com.hurix.commons.Constants.Constants.TEACHER;
import static com.hurix.commons.Constants.Constants.UGC_ITEM_MODE_MODIFIED;
import static com.hurix.commons.Constants.Constants.UGC_ITEM_MODE_NEW;


public class CustomUGCEnterpriseItemSharingScreen extends LinearLayout implements OnClickListener,
        OnItemSelectedListener {

    private TextView mShareText;
    private Context mContext;
    private LayoutInflater mLayoutinfilater;
    private View mView, mDivider;
    private LinearLayout mBackButtonHolder;
    private CustomMyDataFragment mUgcHolder;
    private CustomMyDataTabDialogFragment mUgcHolderTab;
    private TextView mBackBtnArrow, mTitle;
    private Button mBtnCancel, mBtnShare;
    private RelativeLayout mClassesDropDownContainer;
    private Typeface mTypeFace;
    private TextView mTxttitle;
    private TextView mChapterName;
    private TextView mTxtdate;
    private TextView mTxtdata;
    private TextView mImgnotetype;
    private Spinner mClassesSpinner;
    private ClassesSpinnerAdapter mClassesSpinnerAdp;
    private HighlightVO mSharingData = null;
    private ArrayList<ListItemUserDAO> mSharedUserlist;
    private ListView mSharingMiddleContainer;
    ArrayList<ListItemUserDAO> studentList;
    private TextView noGroup;
    private CustomISharingSettingListner mListener;
    private ArrayList<IClass> arrayList = null;
    private UserClassVO mCurrentSelectedClass;
    private IStickyNoteShareSettingListener nListener;
    private String RoleName;
    private StickyNoteShareAdapter mAdapter;
    private long userID;
    private OnShareViewItemClickListener mBtnclicklistner;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private HashSet<Integer> mCurrentSharedUserlist = new HashSet<Integer>();
    private boolean mIsMobile;

    public CustomUGCEnterpriseItemSharingScreen(Context context, CustomMyDataFragment ugcHolder, ReaderThemeSettingVo themeUserSettingVo, boolean isMobile) {
        super(context);
        mContext = context;
        nListener = (IStickyNoteShareSettingListener) context;
        mUgcHolder = ugcHolder;
        mReaderThemeSettingVo = themeUserSettingVo;
        mIsMobile = isMobile;
        initUI();
    }

    public CustomUGCEnterpriseItemSharingScreen(Context context, CustomMyDataTabDialogFragment ugcHolder, ReaderThemeSettingVo themeUserSettingVo, boolean isMobile) {
        super(context);
        mContext = context;
        nListener = (IStickyNoteShareSettingListener) context;
        mUgcHolderTab = ugcHolder;
        mIsMobile = isMobile;
        mReaderThemeSettingVo = themeUserSettingVo;
        initUI();
    }

    private void initUI() {
        mLayoutinfilater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mLayoutinfilater.inflate(R.layout.new_share_ugcitem_layout, this);
        mView.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getPopupBackground()),
                new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2, Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSelectedTabBorder())));
        mBackButtonHolder = (LinearLayout) mView.findViewById(R.id.backBtnHolder);
        mBackButtonHolder.setOnClickListener(this);
        mBackBtnArrow = (TextView) mView.findViewById(R.id.backBtnTV);
        mTitle = (TextView) mView.findViewById(R.id.title);
        mBtnCancel = (Button) mView.findViewById(R.id.btnCancel);
        mBtnShare = (Button) mView.findViewById(R.id.btnShare);
        mDivider = mView.findViewById(R.id.divider1);
        mShareText = (TextView) mView.findViewById(R.id.shareThisNoteWithLableTV);
        mBtnCancel.setOnClickListener(this);
        mSharingMiddleContainer = (ListView) mView.findViewById(R.id.note_share_middle_container);
        mClassesDropDownContainer = (RelativeLayout) mView.findViewById(R.id.classesDropDownContainer);
        mTxttitle = (TextView) mView.findViewById(R.id.txttitle);
        mChapterName = (TextView) mView.findViewById(R.id.txtChapterName);
        mTxtdate = (TextView) mView.findViewById(R.id.txtdate);
        mTxtdata = (TextView) mView.findViewById(R.id.txthighlightdata);
        noGroup = (TextView) mView.findViewById(R.id.noUserInGroup);
        mBtnCancel.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getShare().getShareSettings().getTextColor()));
        mBtnShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getShare().getShareSettings().getSectionTitleColor()));
        mTitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getShare().getShareSettings().getSectionTitleColor()));
        mDivider.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getShare().getShareSettings().getSectionTitleColor()));
        mShareText.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getShare().getShareSettings().getTextColor()));
        mShareText.setTextSize(16);
        mChapterName.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
        mTxtdate.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getDescriptionColor()));
        mTxttitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getDescriptionColor()));
        mTxtdata.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getDescriptionColor()));
        mBackBtnArrow.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getIconColor()));
        mImgnotetype = (TextView) mView.findViewById(R.id.btnresourcetype);
        mClassesSpinner = (Spinner) mView.findViewById(R.id.classesSpinner);
        mClassesSpinnerAdp = new ClassesSpinnerAdapter(mContext);
        mClassesSpinnerAdp.setData(arrayList);
        mClassesSpinner.setAdapter(mClassesSpinnerAdp);
        mClassesSpinner.setOnItemSelectedListener(this);
        mSharingMiddleContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleItem(position);
            }
        });
        setUpIconFonts();

    }

    public void setShareSettingListenr(CustomISharingSettingListner listenr) {
        this.mListener = listenr;
    }

    private void toggleItem(int position) {
        if (mSharedUserlist.get(position).isEnabled()) {
            if (mSharedUserlist.get(position).getRoleName().equalsIgnoreCase(TEACHER) &&
                    mSharedUserlist.get(position).isSection()) {
                if (mSharedUserlist.get(position).isSelected()) {
                    for (ListItemUserDAO userDao : mSharedUserlist) {
                        if (userDao.getRoleName().equalsIgnoreCase(TEACHER)) {
                            userDao.setSelected(false);
                        }
                    }
                } else {
                    for (ListItemUserDAO userDao : mSharedUserlist) {
                        if (userDao.getRoleName().equalsIgnoreCase(TEACHER)) {
                            userDao.setSelected(true);
                        }

                    }
                }
            } else if (mSharedUserlist.get(position).getRoleName().equalsIgnoreCase(STUDENT)
                    && mSharedUserlist.get(position).isSection()) {
                if (mSharedUserlist.get(position).isSelected()) {
                    toggleStudent(false);
                } else {
                    toggleStudent(true);
                }

            } else {
                if (mSharedUserlist.get(position).isSelected()) {
                    mSharedUserlist.get(position).setSelected(false);
                } else {
                    mSharedUserlist.get(position).setSelected(true);
                }
            }
        }
        setAllStudentSelection();
        mAdapter.setData(mSharedUserlist);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnclickListner(OnShareViewItemClickListener listner) {
        mBtnclicklistner = listner;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBackButtonHolder.getId()) {
            mBtnclicklistner.onBackbtnClicked(v);
        } else if (v.getId() == mBtnCancel.getId()) {
            mBtnclicklistner.onCancelClicked(v);
        } else if (v.getId() == mBtnShare.getId()) {
            mBtnclicklistner.onShareClicked(v);
        }
    }

    public void cancelClicked() {
        disableButtons();
        if (mIsMobile) {
            mUgcHolder.openMainScreen();
        } else {
            mUgcHolderTab.openMainScreen();
        }
    }

    public void shareData() {
        updateSharedUserList(true);
        disableButtons();
        if (mIsMobile) {
            mUgcHolder.openMainScreen();
        } else {
            mUgcHolderTab.openMainScreen();
        }
    }

    public void myDataSharehareClicked() {
        updateSharedUserList(true);
        //button disabled forn now
        mBtnShare.setEnabled(true);
        mBtnShare.setAlpha(0.5f);
        //button disabled forn now
    }

    private void updateSharedUserList(boolean isSaveInDB) {
        boolean sharedWithNewUser = false;
        if (mCurrentSharedUserlist != null && mSharedUserlist != null && mSharedUserlist.size() > 0) {
            if (RoleName.equals(TEACHER)
                    && isSaveInDB) {
                if (mCurrentSelectedClass.isNoteTeacherStudentSharing()) {
                    for (IUser user : mCurrentSelectedClass.getStudentList()) {
                        if (user.getRoleName().equalsIgnoreCase(TEACHER) && user.getUserID()
                                != userID) {
                            if (!mCurrentSharedUserlist.contains(user.getUserID())) {
                                mCurrentSharedUserlist.add((int) user.getUserID());
                                sharedWithNewUser = true;
                                // GlobalDataManager.getInstance().setFirstshareenable(true);
                                // GlobalDataManager.getInstance().setFirstsharetrue(true);
                            }
                        }
                    }
                }
            }
            for (ListItemUserDAO vo : mSharedUserlist) {
                if (vo.isSelected()) {
                    vo.setSelected(false);
                    if (vo.getRoleName().equalsIgnoreCase(TEACHER)) {
                        for (IUser user : mCurrentSelectedClass.getStudentList()) {
                            if (user.getRoleName().equalsIgnoreCase(TEACHER)) {
                                mCurrentSharedUserlist.add((int) user.getUserID());
                                sharedWithNewUser = true;
                            }
                        }
                    } else if (!vo.getDisplayName().equalsIgnoreCase(getResources().getString(R.string.student))) {
                        mCurrentSharedUserlist.add((int) vo.getUserID());
                        sharedWithNewUser = true;
                    }
                    if (!mBtnShare.isEnabled()) {
                        setDoneClickable();
                    }
                }
            }
        }
        if (sharedWithNewUser && isSaveInDB) {
            mSharingData.getUserShareColl().addAll(mCurrentSharedUserlist);
            if (nListener != null)
                nListener.onStickyNoteDataSaveToDataBase(mSharingData, mCurrentSelectedClass);
        }
    }

    private void setDoneClickable() {
        if (!checkForShareNote()) {
            mBtnShare.setEnabled(false);
            mBtnShare.setAlpha(0.5f);
           /* mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                    .getReaderFont()));*/
            mBtnShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        } else if (checkForShareNote()) {
            mBtnShare.setEnabled(true);
            mBtnShare.setAlpha(1f);
            //mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_icon_color()));
            mBtnShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        }

    }

    private boolean checkForShareNote() {
        boolean result = false;
        for (ListItemUserDAO vo : mSharedUserlist) {
            if (vo.isSelected() && vo.isEnabled() && (!vo.getDisplayName().equalsIgnoreCase("All Students"))) {
                result = true;
                break;
            }
        }
        return result;
    }


    private void setUpIconFonts() {
        //mTypeFace = Typefaces.get(mContext, getResources().getString(R.string.kitaboo_font_file_name));
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeFace = Typefaces.get(mContext, fontfile);
        } else {
            mTypeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
        mImgnotetype.setTypeface(mTypeFace);
        mImgnotetype.setAllCaps(false);
        mBackBtnArrow.setTypeface(mTypeFace);
        mBackBtnArrow.setAllCaps(false);
        mBackBtnArrow.setText(PlayerUIConstants.UD_SHARE_UGC_ITEM_BACK_IC_TEXT);
       /* mBackBtnArrow.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().get_reader_icon_color()));*/
    }

    public void setUgcItemData(HighlightVO vo, ArrayList<IClass> _allarrayList, Long userId, String _RoleName) {
        RoleName = _RoleName;
        userID = userId;
        arrayList = _allarrayList;
        mSharingData = vo;

        if (mSharingData != null) {
            mTxttitle.setTypeface(mTxttitle.getTypeface(), Typeface.ITALIC);
          /*  mTxttitle.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().getmReaderDefaultPanelHighlightData()));
            mChapterName.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_default_panel_metadata()));
            mTxtdate.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_default_panel_metadata()));
            mTxtdata.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().
                    getmReaderDefaultPanelHighlightData()));*/
            if (!mSharingData.getNoteData().equals("")) {
                //Highlight and Sticky note
                mTxttitle.setText(mSharingData.getHighlightedText());
                mChapterName.setText(getResources().getString(R.string.ugc_mydata_chapter_label)+ mSharingData.getChapterName());
                mTxtdate.setText(mContext.getResources().getString(R.string.ugc_mydata_page_label) + vo.getFolioID() + "  " + Utils.getDateInLocalFormat(
                        mSharingData.getDateTime(), "hh:mm a dd MMM yyyy"));
                if (mSharingData.getHighlightedText() != null && !mSharingData.getHighlightedText().trim().isEmpty()) {
                    mImgnotetype.setText(PlayerUIConstants.SN_TEXT_IC_TEXT);
                } else {
                    mImgnotetype.setText(PlayerUIConstants.TB_STICKYNOTE_IC_TEXT);
                }

                if (mSharingData.getColor().equals(mContext.getResources().getColor(
                        R.color.highlight_impotant_color))) {
                    mImgnotetype.setTextColor(PlayerUIConstants.SN_TEXT_IC_IMPORTANT_FC);
                    mImgnotetype.setBackgroundColor(getResources().getColor(R.color.highlight_impotant_color));
                } else {
                    mImgnotetype.setTextColor(PlayerUIConstants.SN_TEXT_IC_NORMAL_FC);
                    //crashed issue mSharingData.getColor() Need to Resolve
                    try {
                        mImgnotetype.setBackgroundColor(Color.parseColor(mSharingData.getColor()));
                    } catch (Exception e) {
                        if (mSharingData.isImportant()) {
                            mImgnotetype.setBackgroundColor(getResources().getColor(R.color.highlight_impotant_color));
                        } else {
                            mImgnotetype.setBackgroundColor(getResources().getColor(R.color.highlight_normal_color));
                        }

                    }
                }

                mTxtdata.setVisibility(View.VISIBLE);
                mTxtdata.setText(mSharingData.getNoteData());
            }
        }
        if (arrayList != null) {
            mClassesSpinnerAdp.setData(arrayList);
            mClassesSpinnerAdp.notifyDataSetChanged();
            mClassesSpinner.setSelection(0);
            if (arrayList != null &&
                    arrayList.size() > 0) {
                mCurrentSelectedClass = (UserClassVO) arrayList.get(0);
                mSharedUserlist = prepareLists(mCurrentSelectedClass);
                if (mSharedUserlist != null && mSharedUserlist.size() > 0) {
                    setShareButtonClickListner(true);
                    mShareText.setVisibility(VISIBLE);
                    if (arrayList.size() > 1) {
                        mClassesSpinner.setEnabled(true);
                        mClassesSpinner.setOnItemSelectedListener(this);
                        mShareText.setText(R.string.select_class);
                        //((TextView) findViewById(R.id.shareThisNoteWithLableTV)).setVisibility(View.VISIBLE);
                    } else {
                        mClassesSpinner.setEnabled(false);
                        mShareText.setText(R.string.share_this_note_with);
                        // ((TextView) findViewById(R.id.shareThisNoteWithLableTV)).setVisibility(View.INVISIBLE);
                    }
                } else {
                    mSharingMiddleContainer.setEmptyView(noGroup);
                    mSharingMiddleContainer.getEmptyView().setVisibility(VISIBLE);
//                    mClassesDropDownContainer.setVisibility(GONE);
                  /*  mBtnShare.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                            .getReaderFont()));*/
                    mBtnShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                            getReader().getDayMode().getShare().getShareSettings().getTextColor()));
                    mBtnShare.setEnabled(false);
                    mBtnShare.setAlpha(0.5f);
                    setShareButtonClickListner(false);
                    mShareText.setVisibility(INVISIBLE);
                }
            }
        }

        if (mSharedUserlist != null && mSharedUserlist.size() >= 0) {
            mAdapter = new StickyNoteShareAdapter(getContext(), mSharedUserlist);
            mSharingMiddleContainer.setAdapter(mAdapter);
            mSharingMiddleContainer.setDivider(null);
            mSharingMiddleContainer.setDividerHeight(-1);
        }
    }


    private ArrayList<ListItemUserDAO> prepareLists(UserClassVO userClassVO) {
        studentList = new ArrayList<ListItemUserDAO>();
        ArrayList<ListItemUserDAO> teacherList = new ArrayList<ListItemUserDAO>();
        ArrayList<ListItemUserDAO> listItem = new ArrayList<ListItemUserDAO>();

        for (int i = 0; i < userClassVO.getStudentList().size(); i++) {

            if (userID != userClassVO.
                    getStudentList().get(i).getUserID()) {
                ListItemUserDAO userDao = getListItemUserDAO(userClassVO.getStudentList().get(i));
                userDao.setSelected(getHighlightSharedChecked(userDao.getUserID(),
                        mSharingData.getUserShareColl()));
                if (userDao.isSelected()) {
                    userDao.setEnabled(false);
                }
                if (userDao.getRoleName().equalsIgnoreCase(STUDENT)) {
                    studentList.add(userDao);
                } else {
                    if (!RoleName.equals(TEACHER)) {
                        teacherList.add(userDao);
                    }
                }
            }
        }


        if (teacherList.size() > 0) {
            ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
            listItemUserDAO.setDisplayName(getResources().getString(R.string.teacher));
            listItemUserDAO.setSection(true);
            listItemUserDAO.setRoleName(TEACHER);
            long refID = 0;
            for (IUser user : userClassVO.getStudentList()) {
                if (user.getRoleName().equalsIgnoreCase(TEACHER) &&
                        userID != user.getUserID()) {
                    refID = user.getUserID();
                    break;
                }
            }
            listItemUserDAO.setSelected(getHighlightSharedChecked(refID,
                    mSharingData.getUserShareColl()));
            if (listItemUserDAO.isSelected()) {
                listItemUserDAO.setEnabled(false);
            }
            if (RoleName.equalsIgnoreCase(STUDENT)
                    && userClassVO.isNoteStudentTeacherSharing()) {
                listItem.add(listItemUserDAO);
                for (ListItemUserDAO item : teacherList) {
                    listItem.add(item);
                }
            }
        }
        if (studentList.size() > 0) {
            if (RoleName.equalsIgnoreCase(TEACHER)
                    && userClassVO.isNoteTeacherStudentSharing()
                    || RoleName.equalsIgnoreCase(STUDENT)
                    && userClassVO.isNoteStudentStudentSharing()) {
                addStudentList(listItem);
            }
        }
        return listItem;
    }

    private void addStudentList(ArrayList<ListItemUserDAO> listItem) {
        ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
        listItemUserDAO.setDisplayName(getResources().getString(R.string.student));
        listItemUserDAO.setSection(true);
        listItemUserDAO.setSelected(isAllSelected(studentList));
        listItemUserDAO.setRoleName(STUDENT);
        listItem.add(listItemUserDAO);
        for (ListItemUserDAO item : studentList) {
            listItem.add(item);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arrayList.size() > 0) {
            updateSharedUserList(false);
            mCurrentSelectedClass = (UserClassVO) arrayList.get(arg2);
            mSharedUserlist = prepareLists(mCurrentSelectedClass);
            if (mAdapter != null) {
                mAdapter.setData(mSharedUserlist);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void enableButtons() {
        mBtnCancel.setEnabled(true);
        mBtnShare.setEnabled(true);
        mBackButtonHolder.setEnabled(true);
    }

    public void disableButtons() {
        mBtnCancel.setEnabled(false);
        mBtnShare.setEnabled(false);
        mBackButtonHolder.setEnabled(false);
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
        return tempUserDao;

    }

    private boolean getHighlightSharedChecked(long refUserID, HashSet<Integer> shareUserList) {
        for (Integer integer : shareUserList) {
            if (integer.intValue() == refUserID) {
                return true;
            }
        }
        return false;
    }

    private void setShareButtonClickListner(boolean state) {
        if (state) {
            mBtnShare.setOnClickListener(this);
        } else {
            mBtnShare.setOnClickListener(null);

        }
    }

    private void toggleStudent(boolean status) {
        if (status) {
            for (ListItemUserDAO userDao : mSharedUserlist) {
                if (userDao.getRoleName().equalsIgnoreCase(STUDENT)) {
                    userDao.setSelected(true);
                }
            }
        } else {
            for (ListItemUserDAO userDao : mSharedUserlist) {
                if (userDao.getRoleName().equalsIgnoreCase(STUDENT)) {
                    userDao.setSelected(setShared(userDao));
                }
            }
        }
    }

    private boolean setShared(ListItemUserDAO userDAO) {
        for (int noteVO : mSharingData.getUserShareColl()) {
            if (noteVO == userDAO.getUserID()) {
                return true;
            }
        }
        return false;
    }

    private void setAllStudentSelection() {
        for (ListItemUserDAO userDAO : mSharedUserlist) {
            if (userDAO.isSection() && userDAO.getDisplayName().equalsIgnoreCase("All Students")) {
                userDAO.setSelected(isAllSelected(studentList));
            }
        }

    }

    public boolean isAllSelected(ArrayList<ListItemUserDAO> userDAOs) {
        for (ListItemUserDAO userDao : userDAOs) {
            //Condition checks the selection of student
            if (!userDao.isSelected() && userDao.getRoleName().equalsIgnoreCase(STUDENT)
                    && !userDao.getDisplayName().equalsIgnoreCase("All Students")) {
                return false;
            }
        }
        return true;
    }

    public interface OnShareViewItemClickListener {
        void onShareClicked(View clikedview);

        void onCancelClicked(View view);

        void onBackbtnClicked(View view);
    }

}
