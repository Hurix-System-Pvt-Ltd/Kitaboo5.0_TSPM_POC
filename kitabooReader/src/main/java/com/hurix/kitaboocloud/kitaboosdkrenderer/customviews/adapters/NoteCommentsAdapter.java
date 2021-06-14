package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.commons.KitabooSDKModel;
import com.hurix.customui.datamodel.CommentsVO;
import com.hurix.customui.iconify.Utils;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.InsightReaderThemeController;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class NoteCommentsAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<CommentsVO> mCommentVO;
    private ReaderThemeSettingVo readerThemeSettingVo;

    public NoteCommentsAdapter(Context context) {
        mContext = context;
        readerThemeSettingVo = InsightReaderThemeController.getInstance(mContext.getApplicationContext()).getReaderThemeUserSettingVo();
    }

    public void setData(ArrayList<CommentsVO> commentVO) {
        this.mCommentVO = commentVO;
    }

    @Override
    public int getCount() {
        return mCommentVO.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCommentVO.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.note_comment_lisitem, parent, false);
            holder = new ViewHolder();
            holder.personDetail = (TextView) convertView.findViewById(R.id.personDetailTvId);
            holder.date = (TextView) convertView.findViewById(R.id.dateTvId);
            holder.commentData = (TextView) convertView.findViewById(R.id.descriptionTvId);
            holder.ll_comment_item_main_panel = (LinearLayout) convertView.findViewById(R.id.ll_comment_item_main_panel);
            holder.cl_comment_container = (ConstraintLayout) convertView.findViewById(R.id.cl_comment_container);
           /* holder.date.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_default_panel_metadata()));*/
           /* holder.personDetail.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_default_panel_color()));
            holder.commentData.setTextColor(Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_default_panel_color()));*/
            holder.personDetail.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getOtherMessage().getNameColor()));
            holder.commentData.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getOtherMessage().getDescriptionColor()));
            holder.date.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getMyMessage().getTimeTextColor()));
            /*holder.commentData.setTextColor(Color.parseColor(UserController.getInstance(
                    mContext).getUserSettings().getReaderFont()));*/
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        /*GradientDrawable greyPanelDrawable = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(mContext.getResources().getColor(R.color.overflow_panel_background));
        shape.setStroke(3, mContext.getResources().getColor(R.color.darkgrey));
        v.setBackground(shape);*/

        GradientDrawable otherUserPanel = com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getOtherMessage().getBackground()),
                new float[] { 8, 8, 8, 8, 8, 8, 8, 8 }, (int)1,Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getOtherMessage().getBorderColor()));

        GradientDrawable selfPanel = com.hurix.epubreader.Utility.Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getMyMessage().getBackground()),
                new float[] { 8, 8, 8, 8, 8, 8, 8, 8 }, (int)0.8,Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getMyMessage().getBorderColor()));

        if (mCommentVO.get(position).getUserID() == KitabooSDKModel.getInstance().getUserID()) {
            params.gravity = Gravity.RIGHT;
            holder.cl_comment_container.setBackground(selfPanel);
            holder.personDetail.setText("Me");
            holder.personDetail.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getMyMessage().getNameColor()));
            holder.commentData.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getComments().getMyMessage().getDescriptionColor()));
        } else {
            params.gravity = Gravity.LEFT;
            holder.cl_comment_container.setBackground(otherUserPanel);
            holder.personDetail.setText(Utils.getEncaptiveName(mCommentVO.get(position).getDisplayName() + mContext.getResources()
                    .getString(R.string.wrote)));
        }
        holder.cl_comment_container.setLayoutParams(params);
        holder.date.setText(Utils.getDateInLocalFormat(
                mCommentVO.get(position).getDateTime(), "hh:mm a ' 'dd MMMM yyyy"));
        holder.commentData.setText(mCommentVO.get(position).getCommentData());
        holder.commentData.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView;
    }

    class ViewHolder {
        TextView personDetail;
        TextView date;
        TextView commentData;
        LinearLayout ll_comment_item_main_panel;
        ConstraintLayout cl_comment_container;
    }
}

