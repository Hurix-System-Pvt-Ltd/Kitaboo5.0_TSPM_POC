package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.content.res.Configuration;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Amit Raj on 22-08-2019.
 */
public class CustomThumbnailsRecyclerAdapter extends RecyclerView.Adapter<CustomThumbnailsRecyclerAdapter.ThumbnailHolder> {

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
    private SeekBarPositionChangeListner seekBarPositionChangeListner;

    private int setpositionscroll = 0;

    public CustomThumbnailsRecyclerAdapter(Context context, ArrayList<ThumbnailVO> ThumbnailColl,
                                           int mThumbnailIconsColor, ReaderThemeSettingVo themeUserSettingVo, SeekBarPositionChangeListner mSeekBarPositionChangeListner) {
        _mContext = context;
        seekBarPositionChangeListner= mSeekBarPositionChangeListner;
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
    public ThumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.custom_thumb, parent, false);

        return new ThumbnailHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailHolder holder, int position) {

        if ((_mThumbNails.get(position).getPageID() == 0)) {
            int mcurrentpageindex = currentpageindex;
            if (_mThumbNails.get(position).getPageColl() != null && _mThumbNails.get(position).getPageColl().length == 1 &&
                    (_mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || SDKManager.getInstance().getPageMode() ==1)){
                mcurrentpageindex = mcurrentpageindex + 1;
                if (mcurrentpageindex == _mThumbNails.get(position).getPageColl()[0].getPageID()) {
                    mSelected1 = true;
                    setSetpositionscroll(position);
                } else {
                    mSelected1 = false;
                }
            }
            if (_mThumbNails.get(position).getPageColl() != null && _mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                    &&  SDKManager.getInstance().getPageMode() !=1 ) {
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
        holder.thumbnailLayout.setData(_mThumbNails.get(position), thumbnailpath, _extFileName, mSelected1, mSelected2, mReaderThemeSettingVo);
        if (seekBarPositionChangeListner!=null)
            seekBarPositionChangeListner.changePosition(position);

    }


    @Override
    public int getItemCount() {
        return  _mThumbNails.size();
    }

    static class ThumbnailHolder extends RecyclerView.ViewHolder {
        CustomTabThumbnailLayout thumbnailLayout;

        public ThumbnailHolder(View itemView) {
            super(itemView);
            thumbnailLayout = (CustomTabThumbnailLayout) itemView.findViewById(R.id.layout);
            thumbnailLayout.setPadding(0, 8, 0, 8);
        }
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
