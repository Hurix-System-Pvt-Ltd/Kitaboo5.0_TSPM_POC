package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.sharingSetting.ListItemUserDAO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import static com.hurix.commons.Constants.Constants.TEACHER;


public class ShareNoteUsersListItemView extends FrameLayout {
    private Context mContext;
    private LayoutInflater mLayoutinfilater;
    private View mView;
    private TextView mPersonName, mCheckBox;
    private ListItemUserDAO mData;
    private Typeface typeFace;
    private ReaderThemeSettingVo readerThemeSettingVo;
    public ShareNoteUsersListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //Typeface typeFace = Typefaces.get(context,context.getResources().getString(R.string.kitaboo_font_file_name));
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if(fontfile!=null && !fontfile.isEmpty()){
            typeFace   = Typefaces.get(context,fontfile);
        } else{
            typeFace   = Typefaces.get(context,"kitabooread.ttf");
        }
        mLayoutinfilater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mLayoutinfilater.inflate(R.layout.ugc_users_list_item, this);
        readerThemeSettingVo = InsightReaderThemeController.getInstance(mContext).getReaderThemeUserSettingVo();

        mPersonName = (TextView) mView.findViewById(R.id.personName);
       /* mPersonName.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                .getReaderFont()));*/
        mPersonName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getShareSettings().getTextColor()));
        mCheckBox = (TextView) mView.findViewById(R.id.checkBox);
        mCheckBox.setTypeface(typeFace);
        mCheckBox.setAllCaps(false);
    /*    mCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(UserController.getInstance(
                        mContext).getUserSettings().get_reader_icon_color())));
        mCheckBox.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                .getReaderFont()));*/
        mCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2, Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getShareSettings().getBoxBorderColor())));
        mCheckBox.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getIconColor()));
       /* mCheckBox.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,getResources().getColor(R.color.kitaboo_main_color)));
        mCheckBox.setTextColor(getResources().getColor(R.color.reader_font_color));*/

    }


    public void setData(ListItemUserDAO data) {
        mData = data;
        if (data.isSection()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    LayoutParams.WRAP_CONTENT, 8f);
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0, 0, 0);
            mPersonName.setLayoutParams(params);
            mPersonName.setText(data.getDisplayName());
          /*  mPersonName.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_icon_color()));*/
            mPersonName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getShare().getShareSettings().getSectionTitleColor()));
            mCheckBox.setVisibility(View.VISIBLE);

        } else {
            mPersonName.setText(data.getFirstName() + " " + data.getLastName());
            if (data.getRoleName() == TEACHER) {
                mCheckBox.setVisibility(View.GONE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
            }
        }
        if (mData.isSelected()) {
            mCheckBox.setText(PlayerUIConstants.UD_SHARE_ITEM_CHECKBOX_TEXT);
            mData.setSelected(true);
            if (!mData.isEnabled()) {

                this.setAlpha(0.5f);

            }
        } else {
            mCheckBox.setText("");
            this.setAlpha(1f);
            mData.setSelected(false);

        }
    }

    public ListItemUserDAO getData() {
        return mData;
    }
}
