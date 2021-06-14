package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class UGCDataAcceptRejectAdapter extends BaseAdapter {
    private ArrayList<HighlightVO> mListofdata;
    private Context mContext;
    private ThemeUserSettingVo mThemeUserSettingVo;
    private HighlightVO mUserHighlightVo;
    private Typeface mTypeFace;
    private boolean mIsMobile;
    private UGCDataAcceptRejectView listener;

    public void setData(Context context, ArrayList<HighlightVO> list,
                        ThemeUserSettingVo themeUserSettingVo, boolean isMobile) {
        this.mListofdata = list;
        this.mContext = context;
        this.mIsMobile = isMobile;
        this.mThemeUserSettingVo = themeUserSettingVo;
        setReaderTyface();
    }

    @Override
    public int getCount() {
        return mListofdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mListofdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (mIsMobile) {
                convertView = mInflater.inflate(R.layout.custom_mydata_list_item_mobile, parent, false);
            } else {
                convertView = mInflater.inflate(R.layout.custom_mydata_list_item, parent, false);
            }

            holder = new ViewHolder();
            holder.txtpersonname = (TextView) convertView.findViewById(R.id.tvSharedwithName);
            holder.tvAccept = (Button) convertView.findViewById(R.id.btn_accept);
            holder.tvReject = (Button) convertView.findViewById(R.id.btn_reject);
            holder.mResourceType = (TextView) convertView.findViewById(R.id.resource_type);
            holder.mViewNoteNotificationDivider = convertView.findViewById(R.id.listdivider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mResourceType.setTypeface(mTypeFace);
        holder.mResourceType.setAllCaps(false);
        if (mIsMobile) {
            holder.txtpersonname.setTextSize(15);
        } else {
            holder.txtpersonname.setTextSize(19);
        }

        holder.mResourceType.setTextSize(35);
        holder.mViewNoteNotificationDivider.setBackgroundColor(mContext.getResources().getColor(R.color.grey));


        if (position == (mListofdata.size() - 1)) {
            holder.mViewNoteNotificationDivider.setVisibility(View.GONE);
        } else {
            holder.mViewNoteNotificationDivider.setVisibility(View.VISIBLE);
        }


        if (mListofdata.get(position) != null) {
            mUserHighlightVo = mListofdata.get(position);

            holder.txtpersonname.setTypeface(holder.txtpersonname.getTypeface(), Typeface.NORMAL);
            holder.txtpersonname.setTextColor(0xFF316E89);
            holder.txtpersonname.setAllCaps(false);


            holder.tvAccept.setTag(position);
            holder.tvReject.setTag(position);

            holder.txtpersonname.setTextColor(Color.parseColor(mThemeUserSettingVo.getReaderFont()));

            if (mUserHighlightVo.getNoteData().isEmpty()) {
                //highlight
                holder.mResourceType.setText("e");
            } else {
                if (mUserHighlightVo.getHighlightedText().isEmpty()) {
                    //sticky note
                    holder.mResourceType.setText("¸");
                } else {
                    //contextual note
                    holder.mResourceType.setText("V");
                }
            }

            /*String colorCode = mUserHighlightVo.getColor();
            if (colorCode.startsWith("#")) {
                holder.mResourceType.setTextColor(Color.parseColor(colorCode));
            } else {
                holder.mResourceType.setTextColor(Color.parseColor("#" + colorCode));
            }*/
            holder.mResourceType.setTextColor(mContext.getResources().getColor(R.color.shared_color));
            holder.mResourceType.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);


            holder.txtpersonname.setText(mUserHighlightVo.getCreatedByUserVO().getFirstName() + " "
                    + mUserHighlightVo.getCreatedByUserVO().getLastName() + " " + "has " + mContext.getResources()
                    .getString(R.string.ugc_share_note_message));


            holder.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUGCAcceptRejectData(true, (Integer) v.getTag());
                }
            });

            holder.tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUGCAcceptRejectData(false, (Integer) v.getTag());
                }
            });
        }
        return convertView;
    }

    public void setListener(UGCDataAcceptRejectView listener) {
        this.listener = listener;
    }

    private class ViewHolder {
        //Notification fields
        TextView txtpersonname, mResourceType;
        Button tvAccept, tvReject;
        View mViewNoteNotificationDivider;
    }

    private void setReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeFace = Typefaces.get(mContext, fontfile);
        } else {
            mTypeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }
}
