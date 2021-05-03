package com.hurix.reader.kitaboosdkrenderer.customviews.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.toc.ThumbListVO;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.customviews.SeekBarFragment;
import com.hurix.reader.kitaboosdkrenderer.customviews.SeekBarPositionChangeListner;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.renderer.utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is used as Adapter for EPUB FIXED thumbnails
 * Author Amit B on 23-12-2020
 */
public class EPUBFIXEDThumbnailAdapter extends RecyclerView.Adapter<EPUBFIXEDThumbnailAdapter.ThumbnailHolder> {

    private ArrayList<ThumbListVO> thumbListVOArrayList;
    private final Context context;
    private final ReaderThemeSettingVo mReaderThemeSettingVo;
    private final OnThumbnailClickListener thumbnailClickListener;
    private final SeekBarPositionChangeListner mSeekBarPositionChangeListner;
    private String thumbnailFolderPath = "";

    public EPUBFIXEDThumbnailAdapter(Context context, ReaderThemeSettingVo mReaderThemeSettingVo, OnThumbnailClickListener thumbnailClickListener, SeekBarPositionChangeListner seekBarPositionChangeListner) {
        this.context = context;
        this.mReaderThemeSettingVo = mReaderThemeSettingVo;
        this.thumbnailClickListener = thumbnailClickListener;
        mSeekBarPositionChangeListner = seekBarPositionChangeListner;
        thumbnailFolderPath = SDKManager.getInstance().getNewEPubFixedBaseUrl() + context.getString(R.string.epub_fixed_thumbnail_path) + File.separator + "page_";
    }

    public void setData(ArrayList<ThumbListVO> thumbListVOArrayList) {
        this.thumbListVOArrayList = thumbListVOArrayList;
    }

    @Override
    public ThumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_mobile_image_thumb_view, parent, false);

        return new ThumbnailHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailHolder holder, int position) {
        holder.pageNumber.setText(thumbListVOArrayList.get(position).getText());
        String path = thumbnailFolderPath + thumbListVOArrayList.get(position).getValue() + ".png";

        Picasso.with(context).
                load(new File(path))
                .centerInside()
                .resize((int) context.getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width), (int) context.getResources().getDimension(R.dimen.book_gallery_thumbnail_item_width))
                .into(holder.thumbImage);

        if (SeekBarFragment.Companion.getThumbnailPosition() == position) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> holder.chapter_title.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedTitleColor())), 1000);
//            imageContainer.setBackground(getResources().getDrawable(R.drawable.thumb_image_selected_drawable));
            holder.imageContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
            holder.pageNumber.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
            holder.textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));

        } else {

            holder.imageContainer.setBackgroundColor(Color.TRANSPARENT);
            holder.pageNumber.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
            holder.textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
        }
        if (SeekBarFragment.Companion.getThumbnailPosition() + 1 == position
                && !Utility.isDeviceTypeMobile(context) && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> holder.chapter_title.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedTitleColor())), 1000);
//            imageContainer.setBackground(getResources().getDrawable(R.drawable.thumb_image_selected_drawable));
            holder.imageContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                    new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
            holder.pageNumber.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
            holder.textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));

        }

        if (mSeekBarPositionChangeListner != null) {
            mSeekBarPositionChangeListner.changePosition(position);
        }
    }


    @Override
    public int getItemCount() {
        return thumbListVOArrayList.size();
    }


    class ThumbnailHolder extends RecyclerView.ViewHolder {
        TextView pageNumber, chapter_title;
        RelativeLayout imageContainer;
        FrameLayout textcontainer;
        ImageView thumbImage;

        public ThumbnailHolder(View itemView) {
            super(itemView);
            pageNumber = itemView.findViewById(R.id.tpageView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            imageContainer = itemView.findViewById(R.id.imageContainer);
            textcontainer = itemView.findViewById(R.id.textcontainer);
            thumbImage = itemView.findViewById(R.id.thumbView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(true);
                    if (!Utility.isDeviceTypeMobile(context) &&
                            context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (getAdapterPosition() % 2 == 0) {
                            SeekBarFragment.Companion.setThumbnailPosition(getAdapterPosition());
                        } else {
                            SeekBarFragment.Companion.setThumbnailPosition(getAdapterPosition() - 1);
                        }

                    } else {
                        SeekBarFragment.Companion.setThumbnailPosition(getAdapterPosition());
                    }
                    thumbnailClickListener.onThumbnailSelect(thumbListVOArrayList.get(getAdapterPosition()).getText());
                    notifyDataSetChanged();
                }
            });
        }

        public void setSelected(boolean selected) {
            if (selected) {
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chapter_title.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedTitleColor()));
                    }
                }, 1000);
//            imageContainer.setBackground(getResources().getDrawable(R.drawable.thumb_image_selected_drawable));
                imageContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()),
                        new float[]{0, 0, 0, 0, 0, 0, 0, 0}, 0, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor())));
                pageNumber.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));
                textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getSelectedThumbnailColor()));

            } else {

                imageContainer.setBackgroundColor(Color.TRANSPARENT);
                pageNumber.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
                textcontainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getThumbnailSlider().getDefaultThumbnailColor()));
            }
        }
    }

    /**
     * Use for thumbnail click listenenr
     */
    public interface OnThumbnailClickListener {
        /**
         * Returns selected page number for thumbnail
         *
         * @param position
         */
        void onThumbnailSelect(String position);
    }

}
