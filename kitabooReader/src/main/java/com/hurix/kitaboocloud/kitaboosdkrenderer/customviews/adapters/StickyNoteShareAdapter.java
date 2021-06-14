package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hurix.customui.sharingSetting.ListItemUserDAO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.ShareNoteUsersListItemView;


import java.util.ArrayList;

public class StickyNoteShareAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ListItemUserDAO> items;
    private LayoutInflater vi;
    public StickyNoteShareAdapter(Context context, ArrayList<ListItemUserDAO> items) {
        this.mContext = context;
        this.items = items;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<ListItemUserDAO> items) {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemUserDAO i = items.get(position);
        if (i != null) {

            convertView = vi.inflate(R.layout.note_share_details_item, null);
            ShareNoteUsersListItemView usrList = (ShareNoteUsersListItemView) convertView.findViewById(R.id.entry_item_frameId);
            usrList.setData(i);

        }
        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
