package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.HighlightSettingSelectionListener;
import com.hurix.customui.sharingSetting.ListItemUserDAO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomISharingSettingListner;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.UGCSettingDetailsListItem;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;


public class UGCSharingSettingAdapter extends BaseAdapter {

    private final HighlightSettingSelectionListener mHighlightSelectionListener;
    private Context mContext;
    private ArrayList<ListItemUserDAO> mItems;
    private LayoutInflater mLayoutInflater;

    // private HighlightSettingSelectionListener mSelectionListener;
    private Typeface mTypeFace;
    public static final String STUDENT = "LEARNER";
    public static final String TEACHER = "INSTRUCTOR";
    public static final String UGC_ITEM_MODE_NEW = "N";
    public static final String UGC_ITEM_MODE_MODIFIED = "M";
    public static final String UGC_ITEM_MODE_DELETED = "D";
    private CustomISharingSettingListner iSharingSettingListner;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    public UGCSharingSettingAdapter(Context context, CustomISharingSettingListner _iSharingSettingListner,
                                    HighlightSettingSelectionListener highlightSettingSelectionListener) {
        this.mContext = context;
        //this.mItems = items;
        this.mHighlightSelectionListener = highlightSettingSelectionListener;
        iSharingSettingListner=_iSharingSettingListner;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mTypeFace = Typefaces.get(context, context.getResources().getString(R.string.kitaboo_font_file_name));
        getReaderTyface();
    }


    public void setData(ArrayList<ListItemUserDAO> items, ReaderThemeSettingVo themeUserSettingVo) {
        this.mItems = items;
        mReaderThemeSettingVo = themeUserSettingVo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final ListItemUserDAO i = mItems.get(position);
        if (i != null) {
            if (i.isSection()) {
                v = mLayoutInflater.inflate(R.layout.setting_section_item, null);
                final TextView sectionView = (TextView) v.findViewById(R.id.list_section_tvID);
                sectionView.setText(i.getDisplayName());
                TextView shareCheckBox = (TextView) v.findViewById(R.id.sharecheckBox);
                TextView recieveCheckBox = (TextView) v.findViewById(R.id.recievecheckBox);

                shareCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
                shareCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
                recieveCheckBox.setWidth((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));
                recieveCheckBox.setHeight((int)mContext.getResources().getDimension(R.dimen.share_receive_checkbox_ht_wdt));

               /* sectionView.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                        .getUserSettings().get_reader_icon_color()));*/
               /* shareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(UserController.getInstance(
                                mContext).getUserSettings().get_reader_icon_color())));
                shareCheckBox.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                        .getReaderFont()));*/
               /* recieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(UserController.getInstance(
                                mContext).getUserSettings().get_reader_icon_color())));
                recieveCheckBox.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                        .getReaderFont()));*/


                sectionView.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getSectionTitleColor()));
                shareCheckBox.setTypeface(mTypeFace);
                shareCheckBox.setAllCaps(false);
                shareCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(mContext.getResources().getColor(R.color.transparent),
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(mReaderThemeSettingVo.
                                getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
                shareCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getIconColor()));
                recieveCheckBox.setTypeface(mTypeFace);
                recieveCheckBox.setAllCaps(false);
                recieveCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(mContext.getResources().getColor(R.color.transparent),
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(mReaderThemeSettingVo.
                                getReader().getDayMode().getMyData().getSettings().getBoxBorderColor())));
                recieveCheckBox.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getSettings().getIconColor()));
                if(iSharingSettingListner!=null)
                    //iSharingSettingListner.customizeteacherStudentview(sectionView);
                    if(i.getRoleName() == TEACHER) {
                        shareCheckBox.setVisibility(View.VISIBLE);
                        recieveCheckBox.setVisibility(View.VISIBLE);
                        if (i.ishighlightSharedWithUser()) {
                            shareCheckBox.setText(PlayerUIConstants.UD_SHARE_ITEM_CHECKBOX_TEXT);
                            shareCheckBox.setAlpha(0.5f);
                            i.setSelected(true);
                        } else {
                            shareCheckBox.setText("");
                            shareCheckBox.setAlpha(1f);
                            i.setSelected(false);

                        }
                        if (i.ishighlightRecieveWithUser()) {
                            recieveCheckBox.setText(PlayerUIConstants.UD_SHARE_ITEM_CHECKBOX_TEXT);
                            recieveCheckBox.setAlpha(0.5f);
                        } else {
                            recieveCheckBox.setText("");
                            recieveCheckBox.setAlpha(1f);

                        }
                    } else {
                        shareCheckBox.setVisibility(View.GONE);
                        recieveCheckBox.setVisibility(View.GONE);
                    }

            } else {
                v = mLayoutInflater.inflate(R.layout.setting_details_list_item, null);
                UGCSettingDetailsListItem frm = (UGCSettingDetailsListItem) v.findViewById(R.id.note_share_setting_item_frameId);
                //frm.setData(i, this.mSelectionListener);
                frm.setData(i,iSharingSettingListner, mReaderThemeSettingVo,mHighlightSelectionListener);
            }
        }
        return v;
    }

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if(fontfile!=null && !fontfile.isEmpty()){
            mTypeFace   = Typefaces.get(mContext,fontfile);
        } else{
            mTypeFace   = Typefaces.get(mContext,"kitabooread.ttf");
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

