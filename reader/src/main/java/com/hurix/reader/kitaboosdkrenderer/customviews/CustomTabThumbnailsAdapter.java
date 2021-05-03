package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
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
public class CustomTabThumbnailsAdapter extends BaseAdapter {


    private ArrayList<ThumbnailVO> _mThumbNails;
    private String _mPath;
    private Context _mContext;
    private ThumbnailVO[] _selected_page_coll;
    private String _extFileName;
    private LayoutInflater mInflater;
    private int mPageID;
    private boolean mSelected1 = false, mSelected2 = false;
    private int currentpageindex, currentpageindex1, currentpageindex2;
    private String thumbnailpath;
    private CustomThumbnailsListner thumblistner;
    private int mThumbnailBackgroundColor;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    private int setpositionscroll = 0;

    public CustomTabThumbnailsAdapter(Context context, ArrayList<ThumbnailVO> ThumbnailColl,
                                      int mThumbnailIconsColor, ReaderThemeSettingVo themeUserSettingVo) {
        _mContext = context;
        _mThumbNails = ThumbnailColl;
        mThumbnailBackgroundColor = mThumbnailIconsColor;
        this.mReaderThemeSettingVo = themeUserSettingVo;
        this.mInflater = (LayoutInflater) _mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void setdata(CustomThumbnailsListner mListner, ArrayList<ThumbnailVO> ThumbNails, String mthumbnailpath,
                        int currentindex, int currentindex1, int currentindex2) {
        thumblistner = mListner;
        currentpageindex = currentindex;
        currentpageindex1 = currentindex1;
        currentpageindex2 = currentindex2;
        this._mThumbNails = ThumbNails;
        thumbnailpath = mthumbnailpath;
        mPageID = ThumbNails.get(0).getCurrPage();
        if (mPageID == 0)
            mPageID = 1;
        ThumbnailVO thumbnailVO = new ThumbnailVO();
        _selected_page_coll = thumbnailVO.getPageColl();
        File file = new File(getBookFolderPathCompat(thumbnailpath) +
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
        ThumbnailHolder thumbnailHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_thumb, parent, false);
            thumbnailHolder = new ThumbnailHolder();
            thumbnailHolder.thumbnailLayout = (CustomTabThumbnailLayout) convertView.findViewById(R.id.layout);
            thumbnailHolder.thumbnailLayout.setPadding(0, 8, 0, 8);
            convertView.setTag(thumbnailHolder);
        } else {
            thumbnailHolder = (ThumbnailHolder) convertView.getTag();
        }

        if ((_mThumbNails.get(position).getPageID() == 0)) {
            int mcurrentpageindex = currentpageindex;
            if (_mThumbNails.get(position).getPageColl() != null && _mThumbNails.get(position).getPageColl().length == 1 && _mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mcurrentpageindex = mcurrentpageindex + 1;
                if (mcurrentpageindex == _mThumbNails.get(position).getPageColl()[0].getPageID()) {
                    mSelected1 = true;
                    setSetpositionscroll(position);
                } else {
                    mSelected1 = false;
                }
            }
            if (_mThumbNails.get(position).getPageColl() != null && _mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (mcurrentpageindex == 0) {
                    if (_mThumbNails.get(position).getPageColl().length == 1) {
                        mcurrentpageindex = mcurrentpageindex + 1;
                        if (mcurrentpageindex == _mThumbNails.get(position).getPageColl()[0].getPageID()) {
                            mSelected1 = true;
                            setSetpositionscroll(position);
                        } else {
                            mSelected1 = false;
                        }
                    }
                } else {
                    if (_mThumbNails.get(position).getPageColl() != null) {
                        if (_mThumbNails.get(position).getPageColl().length == 1) {

                            if (currentpageindex1 == _mThumbNails.get(position).getPageColl()[0].getPageID()) {
                                mSelected1 = true;
                                setSetpositionscroll(position);
                            } else {
                                mSelected1 = false;
                            }
                            if (currentpageindex2 == _mThumbNails.get(position).getPageColl()[0].getPageID()) {
                                mSelected2 = true;
                                setSetpositionscroll(position);
                            } else {
                                mSelected2 = false;
                            }
                        }
                        if (_mThumbNails.get(position).getPageColl().length == 2) {
                            if (currentpageindex2 == _mThumbNails.get(position).getPageColl()[1].getPageID()) {
                                mSelected2 = true;
                                setSetpositionscroll(position);
                            } else {
                                mSelected2 = false;
                            }
                        }
                    }
                }
            }
        }
        thumbnailHolder.thumbnailLayout.setData(_mThumbNails.get(position), thumbnailpath, _extFileName, mSelected1, mSelected2, mReaderThemeSettingVo);

        return convertView;
    }


    static class ThumbnailHolder {
        CustomTabThumbnailLayout thumbnailLayout;
    }

    public static String getBookFolderPathCompat(String bookpath) {
        File file = new File(bookpath);
        if (file.exists()) {
            return bookpath;
        }
        return bookpath;
    }

    public int getSetpositionscroll() {
        return setpositionscroll;
    }

    public void setSetpositionscroll(int setpositionscroll) {
        this.setpositionscroll = setpositionscroll;
    }

}