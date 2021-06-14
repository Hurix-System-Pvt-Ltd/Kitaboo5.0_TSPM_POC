package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.thumbnails.ThumbnailVO;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * Created by amitraj.sharma on 2/3/2018.
 */
public class CustomTabThumbnailLayout extends FrameLayout {

    LayoutInflater inflater;
    private String _mPath;
    View mView;
    private Context mContext;
    private Typeface customTypeFace;
    public static final String THUMBNAIL_PATH = "assets/images/thumbnails/";
    private String mPreviousChapterTitle = "";
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    //public final String THUMBNAIL_PATH =mContext.getResources().getString(R.string.thumbnail_path);
    public CustomTabThumbnailLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomTabThumbnailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //initView();
        init();
    }

    private void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.custom_enterprise_image_thumb_view, this);
        customTypeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.text_font_file));
    }

    public void initView(String path) {
        _mPath = getBookFolderPathCompat(path)
                + File.separator + THUMBNAIL_PATH + "page_";
    }


    public void setData(ThumbnailVO pageVo, String path, String extFileName, boolean selectedofrpage1,
                        boolean selectedofrpage2, ReaderThemeSettingVo themeUserSettingVo) {
        LinearLayout chapter_layout = (LinearLayout) mView.findViewById(R.id.chapter_layout);
        LinearLayout thumbnail_holder = (LinearLayout) mView.findViewById(R.id.thumbnails_holder);
        RelativeLayout thumb_layout_1 = (RelativeLayout) thumbnail_holder.findViewById(R.id.thumb_layout_1);
        RelativeLayout thumb_layout_2 = (RelativeLayout) thumbnail_holder.findViewById(R.id.thumb_layout_2);
        TextView txtpagename = (TextView) chapter_layout.findViewById(R.id.txtchapterName);
        ImageView imageview_1 = (ImageView) thumb_layout_1.findViewById(R.id.thumbView);
        TextView pagetextview_1 = (TextView) thumb_layout_1.findViewById(R.id.tpageView);
        FrameLayout textcontainer_1 = (FrameLayout) thumb_layout_1.findViewById(R.id.textcontainer);
        ImageView imageview_2 = (ImageView) thumb_layout_2.findViewById(R.id.thumbView);
        TextView pagetextview_2 = (TextView) thumb_layout_2.findViewById(R.id.tpageView);
        FrameLayout textcontainer_2 = (FrameLayout) thumb_layout_2.findViewById(R.id.textcontainer);
        mReaderThemeSettingVo = themeUserSettingVo;
        thumbnail_holder.setVisibility(View.VISIBLE);
        thumb_layout_2.setVisibility(View.VISIBLE);
        initView(path);
        txtpagename.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getTitleColor()));
        txtpagename.setTypeface(null, Typeface.BOLD);
        txtpagename.setText(pageVo.getChapterName());
        if (pageVo.getIsChapter()) {
            thumbnail_holder.setVisibility(View.GONE);
        } else {

            thumbnail_holder.setVisibility(View.VISIBLE);
            if (pageVo.getPageColl() != null) {
                if (pageVo.getPageColl().length == 1) {
                    thumb_layout_2.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) thumb_layout_1.getLayoutParams();
                    params.weight = 2;
                    thumb_layout_1.setLayoutParams(params);
                    Picasso.with(getContext()).load(new File(_mPath + pageVo.getPageColl()[0].getPageID() + extFileName)).into(imageview_1);
                    pagetextview_1.setText(pageVo.getPageColl()[0].getFolioID());
                    pagetextview_1.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getThumbnailTextColor()));
                    pagetextview_1.setTypeface(customTypeFace, Typeface.BOLD);
                    if (selectedofrpage1) {
                        textcontainer_1.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                        //thumb_layout_1.setBackground(getResources().getDrawable(R.drawable.thumb_image_selected_drawable));
                        thumb_layout_1.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                                new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0,Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
                        txtpagename.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                    } else {
                        textcontainer_1.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
                        thumb_layout_1.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (pageVo.getPageColl().length == 1 && selectedofrpage2) {
                        if (selectedofrpage2) {
                            textcontainer_1.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                            thumb_layout_1.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0,Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
                            txtpagename.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                        } else {
                            textcontainer_1.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
                            thumb_layout_1.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width), (int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_height));
                }

                if (pageVo.getPageColl().length == 2) {
                    thumb_layout_2.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) thumb_layout_1.getLayoutParams();
                    params1.weight = 1;
                    thumb_layout_1.setLayoutParams(params1);

                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) thumb_layout_2.getLayoutParams();
                    params2.weight = 1;
                    thumb_layout_2.setLayoutParams(params2);

                    Picasso.with(getContext()).load(new File(_mPath + pageVo.getPageColl()[0].getPageID() + extFileName)).into(imageview_1);
                    pagetextview_1.setText(pageVo.getPageColl()[0].getFolioID());
                    pagetextview_1.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getThumbnailTextColor()));
                    pagetextview_1.setTypeface(customTypeFace, Typeface.BOLD);
                    Picasso.with(getContext()).load(new File(_mPath + pageVo.getPageColl()[1].getPageID() + extFileName)).into(imageview_2);
                    pagetextview_2.setText(pageVo.getPageColl()[1].getFolioID());
                    pagetextview_2.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                    pagetextview_2.setTypeface(customTypeFace, Typeface.BOLD);
                    LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(
                            2 * (int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width),
                            (int) getResources().getDimension(R.dimen.book_gallery_thumbnail_item_height));
                } else {
                    thumb_layout_2.setVisibility(View.GONE);
                }
            }
        }
    }

    public static String getBookFolderPathCompat(String bookpath) {
        File file = new File(bookpath);
        if (file.exists()) {
            return bookpath;
        }
        return bookpath;
    }
}