package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.epubreader.Utility.Utils;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * Created by amitraj.sharma on 2/3/2018.
 */
public class CustomMobileThumbLayout extends FrameLayout {

    private ImageView _imageview;
    private TextView _pagetextview;
    private FrameLayout _textcontainer;
    private RelativeLayout _imgContainer;
    public static final String THUMBNAIL_PATH = "assets/images/thumbnails/";
    private String _mPath;
    View mView;
    private TextView mChapterTitle;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    public CustomMobileThumbLayout(Context context, ReaderThemeSettingVo themeUserSettingVo) {
        super(context);
        mReaderThemeSettingVo = themeUserSettingVo;


        initializeView();
    }

    private void initializeView() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.custom_mobile_image_thumb_view, this);
        _imageview = (ImageView) findViewById(R.id.thumbView);
        _pagetextview = (TextView) findViewById(R.id.tpageView);
        _textcontainer = (FrameLayout) findViewById(R.id.textcontainer);
        _imgContainer = (RelativeLayout) findViewById(R.id.imageContainer);

      //  _imageview.setBackground(getResources().getDrawable(R.drawable.thumb_image_drawable));
        mChapterTitle = (TextView) mView.findViewById(R.id.chapter_title);
    }

    public CustomMobileThumbLayout(Context context, AttributeSet attrs, ReaderThemeSettingVo themeUserSettingVo) {
        super(context, attrs);
        mReaderThemeSettingVo = themeUserSettingVo;
        initializeView();

    }

    public void setSelected(boolean selected) {
        if (selected) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mChapterTitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedTitleColor()));
                }
            }, 1000);
//            _imgContainer.setBackground(getResources().getDrawable(R.drawable.thumb_image_selected_drawable));
            _imgContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0,Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
            _pagetextview.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
            _textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));

        } else {

            _imgContainer.setBackgroundColor(Color.TRANSPARENT);
            _pagetextview.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
            _textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
        }
    }

    public void setData(String path, ThumbnailVO pageVo, String ext) {
        _pagetextview.setText(pageVo.getFolioID());
        _mPath = getBookFolderPathCompat(path, pageVo, ext);
        mChapterTitle.setText(pageVo.getChapterName());
        mChapterTitle.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTitleColor()));
        _imageview.setImageResource(R.drawable.transparent);
        Picasso.with(getContext()).
                load(new File(_mPath))
                .centerInside()
                .resize((int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width), (int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width))
                .into(_imageview);

    }

    public static String getBookFolderPathCompat(String path, ThumbnailVO pageVo, String ext) {
        File file = new File(path + pageVo.getPageID() + ext);
        if (file.exists()) {
            return path + pageVo.getPageID() + ext;
        } else {
            file = new File(path + pageVo.getFolioID() + ext);
            if (file.exists()) {
                return path + pageVo.getFolioID() + ext;
            }
        }
        return path;
    }
}
