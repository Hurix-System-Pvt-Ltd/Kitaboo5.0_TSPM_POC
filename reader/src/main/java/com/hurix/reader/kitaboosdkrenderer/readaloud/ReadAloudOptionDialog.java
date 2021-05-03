package com.hurix.reader.kitaboosdkrenderer.readaloud;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.bookreader.views.link.LinkAudioView;
import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.IPage;
import com.hurix.commons.datamodel.KitabooFixedBook;
import com.hurix.commons.datamodel.LinkVO;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;

/**
 * Created by Amit Raj on 03-09-2019.
 */
public class ReadAloudOptionDialog extends DialogFragment implements View.OnClickListener {

    private TextView mBtnLetMeRead;
    private TextView mBtnAutoPlay;
    private TextView mBtnReadToMe;
    private TextView mChooseOption;
    private TextView mTvLetMeReadIcon,mTvAutoplayIcon,mTvReadToMeIcon;
    private TextView mTvLetMeRead,mTvAutoplay,mTvReadToMe;
    private LinkAudioView mAudioSyncLink = null;
    private onReadAloudListener mReadAloudStartedListener;
    private Context mContext;
    private LinearLayout mMainReadConatiner;
    private TextView mReadClose;
    private ThemeUserSettingVo mUserSettingVO;
    private Typeface customTypeFace;
    private long mUserID,mBookId;
    private   ArrayList<LinkVO> linkVOs;
    private IBook mBook;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        mContext = dialog.getContext();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*if(Utility.isDeviceTypeMobile(mContext))
            dialog.setContentView(com.hurix.epubreader.R.layout.read_aloud_layout);
        else
            dialog.setContentView(com.hurix.epubreader.R.layout.camera_gallery_dialog);*/
        if(com.hurix.reader.kitaboosdkrenderer.utils.Utils.isMobile(mContext)) {
            dialog.setContentView(R.layout.camera_gallery_dialog_mob);
        }else{
            dialog.setContentView(R.layout.camera_gallery_dialog);
        }
        //dialog.setContentView(R.layout.camera_gallery_dialog);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(com.hurix.epubreader.R.string.kitaboo_font_file_name));
        /*mMainReadConatiner = (LinearLayout) dialog.findViewById(com.hurix.epubreader.R.id.mainReadContainer);
        mTvLetMeRead = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvLetmeRead);
        mTvAutoplay = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvAutoPlay);
        mTvReadToMe = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvReadToMe);

        mTvLetMeReadIcon = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.letMeReadIcon);
        mTvAutoplayIcon = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.btnAutoPlay);
        mTvReadToMeIcon = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.readToMeIcon);

        mReadClose = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.readCLose);
        mTvLetMeReadIcon.setTypeface(typeFace);
        mTvLetMeReadIcon.setAllCaps(false);
        mTvAutoplayIcon.setTypeface(typeFace);
        mTvAutoplayIcon.setAllCaps(false);
        mTvReadToMeIcon.setTypeface(typeFace);
        mTvReadToMeIcon.setAllCaps(false);
        mReadClose.setTypeface(typeFace);
        mReadClose.setAllCaps(false);
        mChooseOption = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvSelectOPtion);
        mBtnLetMeRead = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvLetmeRead1);
        mBtnAutoPlay = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvAutoPlay1);
        mBtnReadToMe = (TextView) dialog.findViewById(com.hurix.epubreader.R.id.tvReadToMe1);
        //mUserSettingVO = UserController.getInstance(mContext).getUserSettings();
        mReadAloudStartedListener = (onReadAloudListener)getActivity();
        if(!mContext.getResources().getBoolean(com.hurix.epubreader.R.bool.show_read_aloud_close)){
            mReadClose.setVisibility(View.GONE);
        }*/
       /* setListeners();
        setUpTheme();
        setButtonState();
        customTypeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(com.hurix.epubreader.R.string.text_font_file));
        if(!mUserSettingVO.getLoginHeaderFontFace().isEmpty()){
            setCustomTypeFace();
        }*/
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Utility.isDeviceTypeMobile(mContext)) {
          //  getDialog().getWindow().setLayout((int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.enterprise_read_aloud_dialog_width_mobile), (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.enterprise_read_aloud_dialog_height_mobile));
        }
        Display display = (getActivity()).getWindowManager().getDefaultDisplay();
        if (display != null && getDialog() != null) {
            setLayout();
        }
    }

    private void setLayout() {
       /* getDialog().getWindow().setLayout(mWidth, mHeight);
        getDialog().getWindow().setGravity(mGravity);*/
    }

    public void setData(IBook book, ThemeUserSettingVo userSettingVO, long userID, long bookId){
        mBook=book;
        linkVOs=book.getLinkColl();
        mUserSettingVO=userSettingVO;
        mUserID=userID;
        mBookId=bookId;
    }

    private void setCustomTypeFace(){
        mChooseOption.setTypeface(customTypeFace);
        mTvLetMeRead.setTypeface(customTypeFace);
        mTvAutoplay.setTypeface(customTypeFace);
        mTvReadToMe.setTypeface(customTypeFace);
        mBtnLetMeRead.setTypeface(customTypeFace);
        mBtnAutoPlay.setTypeface(customTypeFace);
        mBtnReadToMe.setTypeface(customTypeFace);
    }

    private void setUpTheme() {
        mMainReadConatiner.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mUserSettingVO.getRAMBGBackground()),
                new float[]{10, 10, 10, 10, 10, 10, 10, 10}, 3, Color.parseColor(mUserSettingVO.getRAMButtonLMRBorderColor())));
        mChooseOption.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonLMRBorderColor()));
        mChooseOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMTitleFontSize()));
        mReadClose.setTextColor(Color.parseColor(mUserSettingVO.getRAMCloseColor()));
        mReadClose.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMCloseFontSize()));
        mTvLetMeRead.setTextColor(Color.parseColor(mUserSettingVO.getRAMOptionTextColor()));
        mTvAutoplay.setTextColor(Color.parseColor(mUserSettingVO.getRAMOptionTextColor()));
        mTvReadToMe.setTextColor(Color.parseColor(mUserSettingVO.getRAMOptionTextColor()));
        mTvLetMeRead.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMOptionTextFontSize()));
        mTvAutoplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMOptionTextFontSize()));
        mTvReadToMe.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMOptionTextFontSize()));
        mBtnLetMeRead.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonLMRColor()));
        mBtnAutoPlay.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonAutoPlayColor()));
        mBtnReadToMe.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonRTMColor()));
        mBtnLetMeRead.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mUserSettingVO.getRAMButtonLMRBackground()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getRAMButtonLMRBorderColor())));
        mBtnAutoPlay.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mUserSettingVO.getRAMButtonAutoPlayBackground()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getRAMButtonAutoPlayBorderColor())));
        mBtnReadToMe.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mUserSettingVO.getRAMButtonRTMBackground()),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mUserSettingVO.getRAMButtonRTMBorderColor())));
        mBtnLetMeRead.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMButtonLMRFontSize()));
        mBtnAutoPlay.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMButtonAutoPlayFontSize()));
        mBtnReadToMe.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getRAMButtonRTMFontSize()));

        mTvLetMeReadIcon.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonLMRIconColor()));
        mTvAutoplayIcon.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonAutoPlayIconColor()));
        mTvReadToMeIcon.setTextColor(Color.parseColor(mUserSettingVO.getRAMButtonRTMIconColor()));
    }

    private void setButtonState() {

        if(GlobalDataManager.getInstance().getLocalBookData().isReadToMe()){
            mBtnReadToMe.setSelected(true);
            mBtnReadToMe.setEnabled(false);
            mBtnReadToMe.setAlpha(0.5f);

            mBtnAutoPlay.setEnabled(true);
            mBtnLetMeRead.setEnabled(true);
        }else if(GlobalDataManager.getInstance().getLocalBookData().isIsReadAloudAutoPlay()){
            mBtnAutoPlay.setSelected(true);
            mBtnAutoPlay.setEnabled(false);
            mBtnAutoPlay.setAlpha(0.5f);

            mBtnReadToMe.setEnabled(true);
            mBtnLetMeRead.setEnabled(true);
        }else if(GlobalDataManager.getInstance().getLocalBookData().isLetMeRead()){
            mBtnLetMeRead.setSelected(true);
            mBtnLetMeRead.setEnabled(false);
            mBtnLetMeRead.setAlpha(0.5f);

            mBtnAutoPlay.setEnabled(true);
            mBtnReadToMe.setEnabled(true);
        }
    }

    private void setListeners() {
        mBtnLetMeRead.setOnClickListener(this);
        mBtnAutoPlay.setOnClickListener(this);
        mBtnReadToMe.setOnClickListener(this);
        mReadClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mReadClose){
            getDialog().dismiss();
        } else {
            getDialog().dismiss();

           /* GlobalDataManager.getInstance().getLocalBookData().notifyReadAloudStateChange(Constants.READ_ALOUD_STOP_STATE);
            mReadAloudStartedListener.setonReadAloudListener(false);
            GlobalDataManager.getInstance().getLocalBookData().setReadAloudType(getReadAloudType(v));
            if (v.getId() == com.hurix.epubreader.R.id.btnAutoPlay || v.getId() == com.hurix.epubreader.R.id.btnReadToMe) {
                GlobalDataManager.getInstance().setAutoPlay(true);
                playCurrentPageAudio(3);
            }*/
        }
    }

    private KitabooFixedBook.ReadAloudType getReadAloudType(View button) {
        KitabooFixedBook.ReadAloudType type = KitabooFixedBook.ReadAloudType.LETMEREAD;
       /* mBtnAutoPlay.setSelected(false);
        mBtnAutoPlay.setEnabled(false);
        mBtnReadToMe.setSelected(false);
        mBtnReadToMe.setEnabled(false);
        mBtnLetMeRead.setSelected(false);
        mBtnLetMeRead.setEnabled(false);
        if (button.getId() == com.hurix.epubreader.R.id.btnAutoPlay) {
            type = KitabooFixedBook.ReadAloudType.AUTOPLAY;
            mReadAloudStartedListener.notifyReadAloudAutoPlayStartStop(true);
        } else if  (button.getId() == com.hurix.epubreader.R.id.btnReadToMe) {
            type = KitabooFixedBook.ReadAloudType.READTOME;
            mReadAloudStartedListener.notifyReadAloudAutoPlayStartStop(true);
        } else if (button.getId() == com.hurix.epubreader.R.id.btnLetmeRead)  {
            type = KitabooFixedBook.ReadAloudType.LETMEREAD;
        }*/
        return type;
    }

    public void setContextForReadAloudDialog(Context context){
        mContext = context;
    }

    public  void setReadAloudListener(onReadAloudListener readAloudStartedListener){
        mReadAloudStartedListener = readAloudStartedListener;
    }

    public synchronized void playCurrentPageAudio(int callID) {
        Log.d("TAG", " playCurrentPageAudio callID: " + callID);
        int nextAudioSyncPageID;
        int mOriantation = mContext.getResources().getConfiguration().orientation;
        if(GlobalDataManager.getInstance().getLocalBookData().isReadToMe()){
            nextAudioSyncPageID = GlobalDataManager.getInstance().getLocalBookData().getCurrentPageID();
            if(mOriantation == Configuration.ORIENTATION_LANDSCAPE && !GlobalDataManager.getInstance().getLocalBookData().isReadAloudReadMeModeCalledFromOnCompletion()) {
               // UserPageVO[] mUserPageVOs = GlobalDataManager.getInstance().getLocalBookData().getVisiblePages(nextAudioSyncPageID);
                for(IPage userPageVO : mBook.getAllPageColl()){
                    if (hasAudio(userPageVO.getFolioID())){
                        nextAudioSyncPageID = userPageVO.getPageID();
                        break;
                    }
                }
            }
            GlobalDataManager.getInstance().getLocalBookData().setIsReadAloudReadMeModeCalledFromOnCompletion(false);
        } else {
            nextAudioSyncPageID = currentAudioSyncPageID();
        }
        if (nextAudioSyncPageID != -1) {
            mReadAloudStartedListener.setonReadAloudListener(true);
           // GlobalDataManager.getInstance().getLocalBookData().setCurrentPageByPageID(nextAudioSyncPageID, true, 19);
            GlobalDataManager.getInstance().refreshAssestOnPage();
        }
    }

    public int  currentAudioSyncPageID() {
        int tempCurrentpageID = GlobalDataManager.
                getInstance().getLocalBookData().getCurrentPageID();
        boolean pageHasAudio = false;
        for (IPage totalPage : mBook.getAllPageColl()) {
            if(totalPage.getPageID() >= tempCurrentpageID) {
                if(hasAudio(totalPage.getFolioID())){
                    return  totalPage.getPageID();
                }
            }
        }
        return  -1;
    }

    public boolean hasAudio(String folioID){
        return getCurrentPageAudioResource(folioID) != 0;
    }

    public long getCurrentPageAudioResource(String folioID){
      /*  ArrayList<LinkVO> linkVOs = (ArrayList<LinkVO>) DBController.getInstance(getActivity())
                .getManager().getLinksForPage(folioID,
                        mUserID,
                        mBookId,
                        SDKManager.getInstance().isTeacherExclusive());*/
        for (LinkVO linkVo : linkVOs) {
            if (linkVo.getType() == LinkVO.LinkType.AUDIO && linkVo.isAudioSync()) {
                return linkVo.getLinkID();
            }
        }
        return 0;
    }

   /* public interface onReadAloudListener {
        void setonReadAloudListener(boolean readAloudPlaying);
        void notifyReadAloudAutoPlayStartStop(boolean isAutoPlay);
    }*/
}
