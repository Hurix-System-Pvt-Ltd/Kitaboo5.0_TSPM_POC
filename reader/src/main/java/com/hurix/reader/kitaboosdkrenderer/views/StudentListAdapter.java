package com.hurix.reader.kitaboosdkrenderer.views;


import android.content.Context;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.commons.utils.DialogUtils;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.interfaces.AssesmentControlListener;
import com.hurix.customui.interfaces.IClass;
import com.hurix.customui.interfaces.IUser;
import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.ReaderThemeController;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import pl.droidsonroids.gif.GifImageView;


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentListHolder>  {

    private final Context context;
    private final ArrayList< IUser> mStudentListList;
    private final IClass mClassObj;
    private AssesmentControlListener mAssesmentControlListener;
    private  ClickListener mClickListener;
    private ReaderThemeSettingVo readerThemeSettingVo;

    public StudentListAdapter(Context context, IClass classObj, ArrayList< IUser> studentList, AssesmentControlListener assesmentControlListener, ClickListener clickListener)
    {
        this.context = context;
        this.mStudentListList = studentList;
        mAssesmentControlListener = assesmentControlListener;
        mClassObj = classObj;
        mClickListener=clickListener;
        readerThemeSettingVo = ReaderThemeController.getInstance(context.getApplicationContext()).getReaderThemeUserSettingVo();
    }

    @Override
    public StudentListAdapter.StudentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_listview, parent, false);

        return new StudentListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentListAdapter.StudentListHolder holder, final int position) {
        IUser studentObj = mStudentListList.get(position);
        holder.name.setText(studentObj.getFirstName() + " " + studentObj.getLastName());

        ArrayList<com.hurix.commons.datamodel.UserPageVO> ugcDataArrayList = GlobalDataManager.getInstance().
                getUgcListPerUserIdMap(""+mClassObj.getID()+studentObj.getUserID());



        holder.tvRefresh.setVisibility(View.GONE);
        if(ugcDataArrayList != null)
        {
            holder.loader.setVisibility(View.GONE);
            holder.tvUgcStatus.setVisibility(View.VISIBLE);
            if(ugcDataArrayList.size() > 0)
            {
                if(ugcDataArrayList.get(0).getPageID() == -1)
                {
                    GradientDrawable refreshDrawable = new GradientDrawable();
                    refreshDrawable.setShape(GradientDrawable.RECTANGLE);
                    refreshDrawable.setStroke((int) 2, Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getRefresh().getBoxBorderColor()));
                    refreshDrawable.setCornerRadius(7);
                    holder.tvRefresh.setBackground(refreshDrawable);
                    holder.tvRefresh.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getRefresh().getButtonTextColor()));
                    holder.tvRefresh.setVisibility(View.VISIBLE);
                    holder.tvUgcStatus.setText(CustomPlayerUIConstants.TEACHER_ACTIONBAR_NOT_SELECTED);
                    holder.tvUgcStatus.setTextColor(Color.parseColor("#d9d9d9"));
                }
                else
                {
                    holder.tvUgcStatus.setText(CustomPlayerUIConstants.PT_COLOR_IC_TEXT);
                    holder.tvUgcStatus.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getDataAddedColor()));
                }
            }
            else
            {
                holder.tvUgcStatus.setText(CustomPlayerUIConstants.PT_COLOR_IC_TEXT);
                holder.tvUgcStatus.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getNoDataAddedColor()));
            }
        }
        else
        {
            holder.tvUgcStatus.setVisibility(View.GONE);
            holder.loader.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mStudentListList.size();
    }

    public class StudentListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout main;
        TextView name;
        TextView tvUgcStatus, tvRefresh;
        GifImageView loader;
        Typeface typeFace;


        public StudentListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
             main = (LinearLayout) itemView.findViewById(R.id.main);

             name = (TextView) itemView.findViewById(R.id.name);
             name.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTeacherStudentList().getNameColor()));
            tvUgcStatus = (TextView) itemView.findViewById(R.id.tv_ugc_status);
            loader = (GifImageView) itemView.findViewById(R.id.loader);
            tvRefresh = (TextView) itemView.findViewById(R.id.tv_refresh);
             typeFace = Typefaces.get(context, context.getResources().getString(R.string.kitaboo_bookshelf_font_file_name));
            tvRefresh.setText(context.getResources().getString(R.string.refresh));
            tvRefresh.setOnClickListener(this);
            tvUgcStatus.setTypeface(typeFace);
            tvUgcStatus.setAllCaps(false);
            tvUgcStatus.setText(CustomPlayerUIConstants.PT_COLOR_IC_TEXT);
            tvUgcStatus.setTextColor(Color.parseColor("#ececec"));
            //CustomPlayerUIConstants.PT_COLOR_IC_TEXT      -for blue and grey symbols
            //CustomPlayerUIConstants.TEACHER_ACTIONBAR_NOT_SELECTED        -for not selected symbol
            //#ececec       -grey color to use
            //#00cefd       -blue color to use
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.tv_refresh)
            {
                SDKManager.getInstance().setNewTeacherReviewModeOn(true);
                if(Utils.isOnline(context)) {
                    tvRefresh.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);
                    tvUgcStatus.setVisibility(View.GONE);
                    mClickListener.onRefreshClick(getAdapterPosition());
                }else{
                    DialogUtils.displayToast(context,
                            context.getResources().getString(R.string.network_not_available_msg),
                            Toast.LENGTH_LONG, Gravity.CENTER);
                }

            }
            else {
                SDKManager.getInstance().setNewTeacherReviewModeOn(true);
                mClickListener.onItemClick(getAdapterPosition());
            }
        }

    }

    public interface ClickListener {
        void onItemClick(int position);
        void onRefreshClick(int position);
    }
}




