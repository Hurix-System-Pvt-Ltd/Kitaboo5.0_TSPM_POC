package com.hurix.reader.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hurix.customui.iconify.IconDrawable;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.interfaces.IClass;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.customviews.CustomISharingSettingListner;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.reader.kitaboosdkrenderer.views.CustomPlayerUIConstants;

import java.util.ArrayList;

public class MyDataClassesSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private ArrayList<IClass> _classesColl = new ArrayList<IClass>();
    private Context mContext;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private CustomISharingSettingListner iSharingSettingListner;
    public MyDataClassesSpinnerAdapter(Context context) {
        mContext = context;
    }
    public MyDataClassesSpinnerAdapter(Context context,CustomISharingSettingListner _iSharingSettingListner) {
        mContext = context;
        iSharingSettingListner=_iSharingSettingListner;
    }

    public void setData(ArrayList<IClass> classesColl) {
        _classesColl = classesColl;
        if (_classesColl == null) {
            _classesColl = new ArrayList<IClass>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _classesColl.size();
    }

    @Override
    public Object getItem(int position) {
        return _classesColl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView t1 = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_list_item_layout, null);

            t1 = (TextView) convertView.findViewById(R.id.tvClassName);
            t1.setPadding(15,0,14,0);
            if (parent.getLayoutParams() != null) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(parent.getLayoutParams().width, LinearLayout.LayoutParams.MATCH_PARENT);
                t1.setLayoutParams(param);
            }
            /*t1.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 2,Color.parseColor(UserController.getInstance(mContext)
                    .getUserSettings().get_reader_icon_color())));*/
            t1.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2,mContext.getResources().getColor(R.color.kitaboo_main_color)));

            convertView.setTag(t1);
        } else {
            t1 = (TextView) convertView.getTag();
        }

       /* t1.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                .getReaderFont()));*/
        t1.setTextColor(mContext.getResources().getColor(R.color.reader_font_color));
        //t1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        t1.setText(_classesColl.get(position).getName());
        // if(iSharingSettingListner!=null)
        //iSharingSettingListner.customizeSpinnerDropDownSharingSettingPanel(t1,parent);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View spinView = null;
        TextView t1 = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            spinView = inflater.inflate(R.layout.spinner_list_item_layout, null);
            spinView.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.TRANSPARENT,
                    new float[]{2, 2, 2, 2, 2, 2, 2, 2}, 2,mContext.getResources().getColor(R.color.kitaboo_main_color)));
        } else {
            spinView = convertView;
        }
        t1 = (TextView) spinView.findViewById(R.id.tvClassName);
      /*  t1.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings()
                .getReaderFont()));*/
        t1.setTextColor(mContext.getResources().getColor(R.color.reader_font_color));
        t1.setText(_classesColl.get(position).getName());
        t1.setPadding(15,0,14,0);
        IconDrawable dropDownDrawable = new IconDrawable(mContext, CustomPlayerUIConstants.DROP_DOWN_ARROW,com.hurix.commons.utils.Utils.getFontFilePath());
        dropDownDrawable.sizeDp(18).color(mContext.getResources().getColor(R.color.kitaboo_main_color));
        t1.setCompoundDrawables( null, null, dropDownDrawable, null );

        if(_classesColl.size() < 2) {
            t1.setCompoundDrawables(null,null,null,null);
        }
        //if(iSharingSettingListner!=null)
        //iSharingSettingListner.customizeSpinnerViewSharingSettingPanel(spinView,t1);
        return spinView;
    }
}