package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hurix.commons.Constants.EBookType;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.datamodel.UserClassVO;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IStickyNoteShareSettingListener;
import com.hurix.customui.interfaces.IUser;
import com.hurix.customui.sharingSetting.ListItemUserDAO;



import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.customviews.adapters.MyDataClassesSpinnerAdapter;
import com.hurix.reader.kitaboosdkrenderer.customviews.adapters.StickyNoteShareAdapter;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import static com.hurix.commons.Constants.Constants.STUDENT;
import static com.hurix.commons.Constants.Constants.TEACHER;


public class CustomStickyNoteShareScreen extends RelativeLayout implements OnClickListener,
        OnItemSelectedListener, OnItemClickListener {
    private final LinearLayout ll_share_note_color_container;
    private LayoutInflater inflater;
    private TextView mCancelBtn, mShareBtn;
    private View mView;
    private RelativeLayout mMainContainer, mSharingTopContainer;
    private LinearLayout mSharingBottomContainer;
    private ListView mSharingMiddleContainer;
    private RelativeLayout mProgressContainer;
    private Spinner mClassesSpinner;
    private MyDataClassesSpinnerAdapter mClassesSpinnerAdp;
    //private LinearLayout mClassesDropDownContainer;
    private StickyNoteShareAdapter mAdapter;
    private Context mContext;
    private ArrayList<ListItemUserDAO> mSharedUserlist = new ArrayList<ListItemUserDAO>();
    private HashSet<Integer> mCurrentSharedUserlist = new HashSet<Integer>();
    private UserClassVO mCurrentSelectedClass;
    private IStickyNoteShareSettingListener mListener;
    private TextView mNoUserInGroup;
    //private TextView mShareNote;
    private ArrayList<IClass> arrayList = null;
    private long userID;
    private String RoleName;
    private HighlightVO _mNoteVO = null;
    private CustomNoteView _noteView;
    private onNoteShareItemClick mBtnclicklistner;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    public CustomStickyNoteShareScreen(Context context, CustomNoteView noteView, ReaderThemeSettingVo readerThemeSettingVo) {
        super(context);
        this.mContext = context;
        mListener = (IStickyNoteShareSettingListener) context;
        _noteView = noteView;
        mReaderThemeSettingVo = readerThemeSettingVo;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.note_popup_share, this);
        mMainContainer = (RelativeLayout) mView.findViewById(com.hurix.epubreader.R.id.mainContainer);
        mCancelBtn = (TextView) mView.findViewById(com.hurix.epubreader.R.id.sharingBtnCancel);
        mCancelBtn.setOnClickListener(this);
        mShareBtn = (TextView) mView.findViewById(R.id.sharingBtnShare);
        mSharingTopContainer = (RelativeLayout) mView.findViewById(R.id.sharingTopContainer);
        mSharingTopContainer.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        mNoUserInGroup = (TextView) mView.findViewById(R.id.noUserInGroup);
        //mClassesDropDownContainer = (LinearLayout) mView.findViewById(R.id.classesDropDownContainer);
        mClassesSpinner = (Spinner) mView.findViewById(R.id.classesSpinner);
        //mShareNote = (TextView) mView.findViewById(R.id.shareNote);
        mSharingMiddleContainer = (ListView) mView.findViewById(R.id.note_share_middle_container);
        mProgressContainer = (RelativeLayout) mView.findViewById(R.id.progressContainer);
        ll_share_note_color_container = (LinearLayout) mView.findViewById(R.id.ll_share_note_color_container);
        mSharingBottomContainer = (LinearLayout) mView.findViewById(R.id.sharingBottomContainer);
        mSharingBottomContainer.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getNote().getPopupBackground()));
        mClassesSpinnerAdp = new MyDataClassesSpinnerAdapter(getContext());
        mClassesSpinner.setOnItemSelectedListener(this);
        mSharingMiddleContainer.setOnItemClickListener(this);
        mShareBtn.setVisibility(VISIBLE);
        mShareBtn.setEnabled(false);
        mShareBtn.setAlpha(0.5f);

    }

    private void setthemeColor() {
        mMainContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBackground()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 1, Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
        ((TextView) mView.findViewById(R.id.sharewithmetitle)).setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getTitleColor()));
        mCancelBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getActionButton().getCancel().getTextColor()));
        mShareBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        }


    public void setData(ArrayList<IClass> allarrayList, Long userId, String _RoleName, HighlightVO vo,
                        EBookType mReaderType, String selectedHighlightedColor) {
        arrayList = allarrayList;
        userID = userId;
        _mNoteVO = vo;
        RoleName = _RoleName;
        setthemeColor();
        resetEnable();
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                mCurrentSelectedClass = (UserClassVO) arrayList.get(0);

                mSharedUserlist = prepareLists((UserClassVO) arrayList.get(0));
                setShareButtonClickListner(true);
                if (arrayList.size() > 1) {
                    mClassesSpinner.setEnabled(true);
                    mClassesSpinner.setOnItemSelectedListener(this);
                } else {
                    mClassesSpinner.setEnabled(false);
                }
            } else {
                setShareButtonClickListner(false);
            }
        }
        /*String backgroundColor = "";

        try {
                //if (new JSONObject(_mNoteVO.getColor()).has("backgroundColor"))
            backgroundColor = new JSONObject(_mNoteVO.getColor()).getString("backgroundColor");

        } catch (JSONException e) {
                e.printStackTrace();
                backgroundColor = _mNoteVO.getColor();
        }*/

        if (new HexValidator().validate(selectedHighlightedColor)) {
            if (selectedHighlightedColor.startsWith("#")) {
                ll_share_note_color_container.setBackgroundColor(Color.parseColor(selectedHighlightedColor));
            } else {
                ll_share_note_color_container.setBackgroundColor(Color.parseColor("#" + selectedHighlightedColor));
            }

        } else {
            if (_mNoteVO.isImportant()) {
                ll_share_note_color_container.setBackgroundColor(Color.parseColor("#cd3a3a"));
            } else {
                ll_share_note_color_container.setBackgroundColor(Color.parseColor("#F4D631"));
            }
        }

        mAdapter = new StickyNoteShareAdapter(getContext(), mSharedUserlist);
        mSharingMiddleContainer.setAdapter(mAdapter);
        mSharingMiddleContainer.setDivider(null);
        mSharingMiddleContainer.setDividerHeight(-1);
        if (mSharedUserlist != null && mSharedUserlist.size() > 0) {
            //mShareNote.setVisibility(VISIBLE);
        } else {
            mSharingMiddleContainer.setEmptyView(mNoUserInGroup);
            mSharingMiddleContainer.getEmptyView().setVisibility(VISIBLE);
            //mShareNote.setVisibility(INVISIBLE);
         /*   mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                    .getReaderFont()));*/
            mShareBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
            mShareBtn.setEnabled(false);
            mShareBtn.setAlpha(0.5f);
        }
        mCancelBtn.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mCancelBtn.getId()) {
            mBtnclicklistner.onCancelClicked(v);
        } else if (v.getId() == mShareBtn.getId()) {
            mBtnclicklistner.onShareClicked(v);
        }

    }

    public void addItemClickListner(onNoteShareItemClick listner) {
        mBtnclicklistner = listner;
    }

    public void cancelClicked() {
        mCurrentSharedUserlist.clear();
        mCancelBtn.setEnabled(false);
        _noteView.removeViewForNoteShare(mView);
    }

    public void shareClicked() {
        updateSharedUserList(true);
        _noteView.removeViewForNoteShare(mView);
        //button disabled forn now
        mShareBtn.setEnabled(false);
        mShareBtn.setAlpha(0.5f);
        //button disabled forn now
    }

    public void myDataSharehareClicked() {
        updateSharedUserList(true);
        //button disabled forn now
        mShareBtn.setEnabled(false);
        mShareBtn.setAlpha(0.5f);
        //button disabled forn now
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

    public void sharelist() {
        updateSharedUserList(true);
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
                    if (!mShareBtn.isEnabled()) {
                        setDoneClickable();
                    }
                }
            }
        }
        if (sharedWithNewUser && isSaveInDB) {
            _mNoteVO.getUserShareColl().addAll(mCurrentSharedUserlist);
            if (mListener != null)
                mListener.onStickyNoteDataSaveToDataBase(_mNoteVO, mCurrentSelectedClass);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arrayList.size() > 0) {

            updateSharedUserList(false);
            mCurrentSelectedClass = (UserClassVO) arrayList.get(arg2);
            mSharedUserlist = prepareLists(mCurrentSelectedClass);

            if (mSharedUserlist != null && mSharedUserlist.size() > 0) {
                mAdapter.setData(mSharedUserlist);
                mAdapter.notifyDataSetChanged();


            } else {
                mAdapter.setData(mSharedUserlist);
                mAdapter.notifyDataSetChanged();
                mSharingMiddleContainer.setEmptyView(mNoUserInGroup);
                mSharingMiddleContainer.getEmptyView().setVisibility(VISIBLE);
                //mShareNote.setVisibility(INVISIBLE);
             /*   mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                        .getReaderFont()));*/
                mShareBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
                mShareBtn.setEnabled(false);
                mShareBtn.setAlpha(0.5f);
            }

            setDoneClickable();
        }
    }

    private void toggleTeacher(boolean status) {
        if (status) {
            for (ListItemUserDAO userDao : mSharedUserlist) {
                if (userDao.getRoleName().equalsIgnoreCase(TEACHER)) {
                    userDao.setSelected(true);
                }
            }
        } else {
            for (ListItemUserDAO userDao : mSharedUserlist) {
                if (userDao.getRoleName().equalsIgnoreCase(TEACHER)) {
                    userDao.setSelected(false);
                }
            }
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


    private void toggleItem(int position) {
        if (mSharedUserlist.get(position).isEnabled()) {
            if (mSharedUserlist.get(position).getRoleName().equalsIgnoreCase(TEACHER)
                    && mSharedUserlist.get(position).isSection()) {
                if (mSharedUserlist.get(position).isSelected()) {
                    toggleTeacher(false);
                    mCurrentSharedUserlist.remove((int) mSharedUserlist.get(position).getUserID());
                } else {
                    toggleTeacher(true);
                }
            } else if (mSharedUserlist.get(position).getRoleName().equalsIgnoreCase(STUDENT)
                    && mSharedUserlist.get(position).isSection()) {
                if (mSharedUserlist.get(position).isSelected()) {
                    toggleStudent(false);
                    mCurrentSharedUserlist.remove((int) mSharedUserlist.get(position).getUserID());
                } else {
                    toggleStudent(true);
                }

            } else {
                if (mSharedUserlist.get(position).isSelected()) {
                    mSharedUserlist.get(position).setSelected(false);
                    mCurrentSharedUserlist.remove((int) mSharedUserlist.get(position).getUserID());

                } else {
                    mSharedUserlist.get(position).setSelected(true);
                }
            }
        }
        setAllStudentSelection();
        mAdapter.setData(mSharedUserlist);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSharedUserlist.get(position).getRoleName().equalsIgnoreCase(TEACHER)) {
            if (mSharedUserlist.get(position).isSection()) {
                toggleItem(position);
                setDoneClickable();
            }
        } else {
            toggleItem(position);
            setDoneClickable();
        }
    }

    private void setShareButtonClickListner(boolean state) {
        if (state) {
            mShareBtn.setOnClickListener(this);
        } else {
            mShareBtn.setOnClickListener(null);
        }
    }

    private void setDoneClickable() {
        if (!checkForShareNote()) {
            mShareBtn.setEnabled(false);
            mShareBtn.setAlpha(0.5f);
           /* mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                    .getReaderFont()));*/
            mShareBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        } else if (checkForShareNote()) {
            mShareBtn.setEnabled(true);
            mShareBtn.setAlpha(1f);
            //mShareBtn.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_icon_color()));
            mShareBtn.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getActionButton().getMain().getBackground()));
        }

    }

    public void resetEnable() {
        mCancelBtn.setEnabled(true);
        mClassesSpinnerAdp.setData(arrayList);
        mClassesSpinner.setAdapter(mClassesSpinnerAdp);
        mShareBtn.setEnabled(false);
        mShareBtn.setAlpha(0.5f);
        //setData();

    }

    private void setAllStudentSelection() {
        for (ListItemUserDAO userDAO : mSharedUserlist) {
            if (userDAO.isSection() && userDAO.getDisplayName().equalsIgnoreCase("All Students")) {
                userDAO.setSelected(checkIfAllStudentSelected());
            }
        }
    }

    private boolean setShared(ListItemUserDAO userDAO) {
        for (int noteVO : mCurrentSharedUserlist) {
            if (noteVO == userDAO.getUserID()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfAllStudentSelected() {
        for (ListItemUserDAO userDao : mSharedUserlist) {
            if (!userDao.isSelected() && userDao.getRoleName().equalsIgnoreCase(STUDENT)
                    && !userDao.getDisplayName().equalsIgnoreCase("All Students")) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<ListItemUserDAO> prepareLists(UserClassVO userClassVO) {
        ArrayList<ListItemUserDAO> studentList = new ArrayList<ListItemUserDAO>();
        ArrayList<ListItemUserDAO> teacherList = new ArrayList<ListItemUserDAO>();

        ArrayList<ListItemUserDAO> listItem = new ArrayList<ListItemUserDAO>();

        for (int i = 0; i < userClassVO.getStudentList().size(); i++) {

            if (userID != userClassVO.getStudentList().get(i).getUserID()) {
                ListItemUserDAO userDao = getListItemUserDAO(userClassVO.getStudentList().get(i));

                if (_mNoteVO.getUserShareColl().size() > 0) {
                    userDao.setSelected(getHighlightSharedChecked(userDao.getUserID(), _mNoteVO.getUserShareColl()));
                    if (userDao.isSelected()) {
                        userDao.setEnabled(false);
                    }
                }
                if (userDao.isEnabled()) {
                    userDao.setSelected(getHighlightSharedChecked(userDao.getUserID(), mCurrentSharedUserlist));
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
            listItemUserDAO.setDisplayName(mContext.getResources().getString(R.string.teacher));
            listItemUserDAO.setSection(true);
            listItemUserDAO.setRoleName(TEACHER);
            long refID = 0;
            for (IUser user : userClassVO.getStudentList()) {
                if (user.getRoleName().equalsIgnoreCase(TEACHER)) {
                    refID = user.getUserID();
                    break;
                }
            }
            listItemUserDAO.setSelected(getHighlightSharedChecked(refID, _mNoteVO.getUserShareColl()));
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
                ListItemUserDAO listItemUserDAO = new ListItemUserDAO();
                listItemUserDAO.setDisplayName(mContext.getResources().getString(R.string.student));
                if (checkForAllStudentSelected(studentList)) {
                    listItemUserDAO.setSelected(true);
                }
                listItemUserDAO.setSection(true);
                listItem.add(listItemUserDAO);
                listItemUserDAO.setRoleName(STUDENT);
                for (ListItemUserDAO item : studentList) {
                    listItem.add(item);

                }
            }
        }
        return listItem;
    }

    private boolean checkForAllStudentSelected(ArrayList<ListItemUserDAO> studentList) {
        boolean allSelected = true;
        for (ListItemUserDAO item : studentList) {
            if (!item.isSelected()) {
                allSelected = false;
            }
        }
        return allSelected;
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

    public interface onNoteShareItemClick {
        void onShareClicked(View clikedview);

        void onCancelClicked(View view);
    }
}
