package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hurix.customui.thumbnails.ThumbnailVO;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by amitraj.sharma on 2/3/2018.
 */
public class CustomMobileThumbnailsAdapter extends BaseAdapter {

    private ArrayList<ThumbnailVO> _mThumbNails;
    private String _mPath;
    private Context _mContext;
    private ThumbnailVO[] _selected_page_coll;
    private String _extFileName;
    private boolean mSelected = false;
    private int mPageID;
    private int mCurrentPosition = 0;
    private int currentpageindex = 0;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    public CustomMobileThumbnailsAdapter(Context context, ArrayList<ThumbnailVO> thumbnailVOc, ReaderThemeSettingVo themeUserSettingVo) {
        this._mContext = context;
        this.mReaderThemeSettingVo = themeUserSettingVo;
    }

    public void setdata(ArrayList<ThumbnailVO> ThumbNails, int currentpageidforselection, ReaderThemeSettingVo themeUserSettingVo) {
        this._mThumbNails = ThumbNails;
        this.mReaderThemeSettingVo = themeUserSettingVo;
        String filepath = _mThumbNails.get(0).getBookpath();
        currentpageindex = currentpageidforselection;
        // For getting path of the respective object
        _mPath = getBookFolderPathCompat(filepath) +
                File.separator + _mContext.getResources().getString(R.string.thumbnail_path) + "page_";
        File file = new File(getBookFolderPathCompat(filepath) +
                File.separator + _mContext.getResources().getString(R.string.thumbnail_path));
        // List all the files in the directory, get their File objects.
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                String name = f.getName();
                if (name.contains("page_1")) {
                    _extFileName = name.substring(name.lastIndexOf('.'), name.length());
                    break;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return _mThumbNails.size();
    }

    @Override
    public Object getItem(int position) {
        return _mThumbNails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomMobileThumbLayout imageView;
        if (convertView == null) {
            imageView = new CustomMobileThumbLayout(parent.getContext(), mReaderThemeSettingVo);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (CustomMobileThumbLayout) convertView;
        }

        imageView.setSelected(false);

        String path = _mPath + _mThumbNails.get(position).getFolioID() + _extFileName;
        imageView.setBackgroundResource(R.drawable.transparent);
        if (currentpageindex == position) {
            imageView.setSelected(true);
        }
        imageView.setData(_mPath, _mThumbNails.get(position), _extFileName);
        return imageView;
    }


    public static String getBookFolderPathCompat(String bookpath) {
        File file = new File(bookpath);
        if (file.exists()) {
            return bookpath;
        }
        return bookpath;
    }
}
