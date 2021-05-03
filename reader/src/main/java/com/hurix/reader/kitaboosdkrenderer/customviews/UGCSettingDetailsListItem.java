package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.HighlightSettingSelectionListener;
import com.hurix.customui.sharingSetting.ListItemUserDAO;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import static com.hurix.commons.Constants.Constants.STUDENT;
import static com.hurix.commons.Constants.Constants.TEACHER;


public class UGCSettingDetailsListItem extends FrameLayout implements OnClickListener {
    private Context mContext;
    private LayoutInflater mLayoutinflater;
    private View mView;
    private TextView mPersonName, mShareCheckBox, mRecieveCheckBox;
    private ListItemUserDAO mData;
    private HighlightSettingSelectionListener mSelectionListener;
    private CustomISharingSettingListner iSharingSettingListner;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private Typeface typeFace;

    public UGCSettingDetailsListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
       /* Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.kitaboo_font_file_name));*/
        getReaderTyface();
        mLayoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mLayoutinflater.inflate(R.layout.ugc_details_list_item, this);
        mPersonName = (TextView) mView.findViewById(R.id.personName);
        mShareCheckBox = (TextView) mView.findViewById(R.id.sharecheckBox);
        mShareCheckBox.setTypeface(typeFace);
        mShareCheckBox.setAllCaps(false);
        mShareCheckBox.setTextColor(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_FC);
        mShareCheckBox.setOnClickListener(this);

        mRecieveCheckBox = (TextView) mView.findViewById(R.id.recievecheckBox);
        mRecieveCheckBox.setTypeface(typeFace);
        mRecieveCheckBox.setAllCaps(false);
        mRecieveCheckBox.setTextColor(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_FC);
        mRecieveCheckBox.setOnClickListener(this);

        mShareCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mShareCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mRecieveCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        mRecieveCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
        /*mPersonName.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));
        mShareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(getResources().getColor(R.color.transparent),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mThemeUserSettingVo.getmKitabooMainColor())));
        mRecieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(getResources().getColor(R.color.transparent),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(mThemeUserSettingVo.getmKitabooMainColor())));
        mRecieveCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));
        mShareCheckBox.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));*/
      /*  mPersonName.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                .getUserSettings().getReaderFont()));
        mShareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(UserController
                        .getInstance(mContext).getUserSettings().get_reader_icon_color())));
        mRecieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(UserController
                        .getInstance(mContext).getUserSettings().get_reader_icon_color())));
        mRecieveCheckBox.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                .getUserSettings().getReaderFont()));
        mShareCheckBox.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                .getUserSettings().getReaderFont()));*/
    }


    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            toggleItem((TextView) v);
        }
    }

    private void toggleItem(TextView tv) {
        mData.setActionTaken(true);
        if (tv.equals(mShareCheckBox)) {
            if (mData.ishighlightSharedWithUser()) {
                tv.setText("");
                mData.sethighlightSharedWithUser(false);
            } else {
                tv.setText(PlayerUIConstants.UD_SHARE_ITEM_CHECKBOX_TEXT);
               /* tv.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                        .getUserSettings().get_reader_icon_color()));*/
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getTitleColor()));
                mData.sethighlightSharedWithUser(true);
            }
            if(mSelectionListener != null){
                mSelectionListener.highlightSharedSelected();
            }
        } else if (tv.equals(mRecieveCheckBox)) {
            if (mData.ishighlightRecieveWithUser()) {
                tv.setText("");
                mData.sethighlightRecieveWithUser(false);

            } else {
                tv.setText(PlayerUIConstants.UD_SHARE_ITEM_CHECKBOX_TEXT);
               /* tv.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                        .getUserSettings().get_reader_icon_color()));*/
                tv.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getTitleColor()));
                mData.sethighlightRecieveWithUser(true);
            }
            if(mSelectionListener != null){
                mSelectionListener.highlightReceiveSelected();
            }
        }

    }

    public void setData(ListItemUserDAO data, CustomISharingSettingListner _iSharingSettingListner, ReaderThemeSettingVo themeUserSettingVo, HighlightSettingSelectionListener highlightSettingSelectionListener) {
        mData = data;
        iSharingSettingListner=_iSharingSettingListner;
        mSelectionListener = highlightSettingSelectionListener;
        mReaderThemeSettingVo = themeUserSettingVo;
        // this.mSelectionListener = selectionListener;
        mPersonName.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getTextColor()));
        mShareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(getResources().getColor(R.color.transparent),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
        mRecieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(getResources().getColor(R.color.transparent),
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
        mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getIconColor()));
        mShareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getSettings().getIconColor()));
        mPersonName.setText(data.getFirstName() + " " + data.getLastName());

        if (data.ishighlightSharedWithUser()) {
            this.setAlpha(1f);
            mShareCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
           /* mShareCheckBox.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                    .getUserSettings().get_reader_icon_color()));*/
            mShareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getIconColor()));
            mData.setActionTaken(true);
        } else {
            this.setAlpha(1f);
            mShareCheckBox.setText("");
        }

        if (mData.ishighlightRecieveWithUser()) {
            this.setAlpha(1f);
            mRecieveCheckBox.setText(PlayerUIConstants.UD_SETTINGS_ITEM_CHECKBOX_TEXT);
         /*   mRecieveCheckBox.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                    .getUserSettings().get_reader_icon_color()));*/
            mRecieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                    getReader().getDayMode().getMyData().getSettings().getIconColor()));
            mData.setActionTaken(true);
        } else {
            this.setAlpha(1f);
            mRecieveCheckBox.setText("");
        }
        if(data.getRoleName().equals(TEACHER)) {
            mShareCheckBox.setVisibility(View.GONE);
            mRecieveCheckBox.setVisibility(View.GONE);
            //view of teacher name
            if (!data.getRoleName().equals(STUDENT)) {
                this.setAlpha(0.5f);
            }
        }

        // if(iSharingSettingListner!=null)
        //iSharingSettingListner.customizeInnerViewSharingSettingPanel(mPersonName,mShareCheckBox,mRecieveCheckBox);
    }
    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if(fontfile!=null && !fontfile.isEmpty()){
            typeFace   = Typefaces.get(mContext,fontfile);
        } else{
            typeFace   = Typefaces.get(mContext,"kitabooread.ttf");
        }
    }
}
