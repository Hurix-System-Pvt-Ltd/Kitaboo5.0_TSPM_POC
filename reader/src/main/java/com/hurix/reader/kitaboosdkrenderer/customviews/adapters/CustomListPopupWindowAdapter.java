
package com.hurix.reader.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hurix.customui.actionbar.OverflowItem;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.R;


import java.util.List;

public class CustomListPopupWindowAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<OverflowItem> mMenuItemList;
    private Context mContext;
    private Typeface mTypeface;

    public CustomListPopupWindowAdapter(Context context, List<OverflowItem> menuItemList) {
        mContext = context;
        mMenuItemList = menuItemList;
    }

    @Override
    public int getCount() {
        return mMenuItemList.size();
    }

    @Override
    public OverflowItem getItem(int i) {
        return mMenuItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        mLayoutInflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.custom_overflow_menu_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.mTVMenuTitle.setText(getItem(position).getMenuTitle());
        holder.mTVMenuImage.setText(getItem(position).getMenuImage());

        AssetManager am = mContext.getApplicationContext().getAssets();
        //Typeface mTypeface = Typeface.createFromAsset(am, "kitabooread.ttf");
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if(fontfile!=null && !fontfile.isEmpty()){
            mTypeface   = Typefaces.get(mContext,fontfile);
        } else{
            mTypeface   = Typefaces.get(mContext,"kitabooread.ttf");
        }
        holder.mTVMenuImage.setTypeface(mTypeface);

        //holder.mTVMenuTitle.setTextColor(getItem(position).getMenuColor());
        holder.mTVMenuImage.setTextColor(getItem(position).getMenuColor());

        //holder.mTVMenuTitle.setTextSize(getItem(position).getMenuSize());
        holder.mTVMenuImage.setTextSize(getItem(position).getMenuSize());

        convertView.setId(getItem(position).getMenuId());
        return convertView;
    }

    static class ViewHolder {
        //TextView mTVMenuTitle;
        TextView mTVMenuImage;

        ViewHolder(View view) {
            //mTVMenuTitle = (TextView) view.findViewById(R.id.tv_menu_title);
            mTVMenuImage = (TextView) view.findViewById(R.id.tv_menu_image);
        }
    }
}
