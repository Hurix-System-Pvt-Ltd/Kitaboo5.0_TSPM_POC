package com.hurix.reader.kitaboosdkrenderer.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;




import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.constants.Constants;
import com.hurix.reader.kitaboosdkrenderer.constants.ShelfUIConstants;
import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.interfaces.CameraGalleryController;
import com.hurix.reader.kitaboosdkrenderer.notifier.GlobalDataManager;

import com.hurix.reader.kitaboosdkrenderer.utils.Utils;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Ravi Ranjan on 4/20/2015.
 */
public class CameraGallery extends Dialog implements View.OnClickListener{

    private TextView mTitle;
    private TextView mClose;
    private View mDefault;
    private View mCamera;
    private View mLib;
    private TextView mDefaultImage;
    private TextView mDefaultText;
    private TextView mCameraImage;
    private TextView mCameraText;
    private TextView mLibImage;
    private TextView mLibText;
    private Context mContext;
    private CameraGalleryController mListener;
    private LinearLayout mParentLayout;

    public CameraGallery(Context context, CameraGalleryController listener){
        super(context);
        this.mContext = context;
        mListener = listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Utils.isMobile(mContext)) {
            setContentView(R.layout.camera_gallery_dialog_mob);
        }else{
            setContentView(R.layout.camera_gallery_dialog);
        }
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        initView();
        setUpIcon();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.camera_gallery_title);
        mClose = (TextView) findViewById(R.id.closeWindow);
        if(Utils.isMobile(mContext)) {
            mTitle.setText(mContext.getResources().getString(R.string.profile_pic_option_mob));
        }
        else{
            mTitle.setText(mContext.getResources().getString(R.string.profile_pic_option));
        }
        mDefault = findViewById(R.id.camera_gallery_item_default);
        mCamera = findViewById(R.id.camera_gallery_item_webcam);
        mLib = findViewById(R.id.camera_gallery_item_lib);
        mDefaultImage = (TextView) mDefault.findViewById(R.id.camera_gallery_image);
        mDefaultText = (TextView) mDefault.findViewById(R.id.camera_gallery_text);
        mCameraImage = (TextView) mCamera.findViewById(R.id.camera_gallery_image);
        mCameraText = (TextView) mCamera.findViewById(R.id.camera_gallery_text);
        mLibImage = (TextView) mLib.findViewById(R.id.camera_gallery_image);
        mLibText = (TextView) mLib.findViewById(R.id.camera_gallery_text);

        mParentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        mDefaultImage.setOnClickListener(this);
        mCameraImage.setOnClickListener(this);
        mLibImage.setOnClickListener(this);

        setTheme();
    }

    private void setTheme() {
        mTitle.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTitleTextColor()));
        mDefaultImage.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        mDefaultText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        mCameraImage.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        mCameraText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        mLibImage.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        mLibText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        mParentLayout.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getBackground()));
    }

    private void setUpIcon() {
        Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.kitaboo_bookshelf_font_file_name));
        mClose.setTypeface(typeFace);
        mClose.setAllCaps(false);
        mClose.setBackgroundColor(Color.TRANSPARENT);
        mClose.setOnClickListener(this);
        mDefaultImage.setTypeface(typeFace);
        mDefaultImage.setAllCaps(false);
        mDefaultImage.setText(ShelfUIConstants.CAMERA_GALLARY_DEFAULT_IC_TEXT);
        mDefaultImage.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        mDefaultImage.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        mDefaultText.setText(mContext.getResources().getString(R.string.default_text));
        //mDefaultText.setTextColor(Color.parseColor("#333333"));
        mCameraImage.setTypeface(typeFace);
        mCameraImage.setAllCaps(false);
        mCameraImage.setText(ShelfUIConstants.CAMERA_GALLARY_CAMERA_IC_TEXT);
        mCameraImage.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        mCameraImage.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        mCameraText.setText(mContext.getResources().getString(R.string.profile_pic_camera));
        //mCameraText.setTextColor(Color.parseColor("#333333"));
        mLibImage.setTypeface(typeFace);
        mLibImage.setAllCaps(false);
        mLibImage.setText(ShelfUIConstants.CAMERA_GALLARY_LIBRARY_IC_TEXT);
        mLibImage.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        mLibImage.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        mLibText.setText(mContext.getResources().getString(R.string.profile_pic_gallery));
        //mLibText.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    public void onClick(View v) {
        if (v == mDefaultImage) {
            mListener.defaultPicture();
            dismiss();
        } else if (v == mCameraImage) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                mListener.cameraPicture();
                dismiss();
            } else {
//                ActivityCompat.requestPermissions(((NewProfileSetting)mContext), Constants.PERMISSIONS_CAMERA,
//                        Constants.REQUEST_PERMISSION_CAMERA);
            }
        } else if (v == mLibImage) {
            mListener.galleryPicture();
            dismiss();
        } else if (v == mClose) {
            dismiss();
        }
    }

    public void updatePermissionInPreview(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_PERMISSION_CAMERA) {
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
                mCameraImage.callOnClick();
//            else if (!ActivityCompat.shouldShowRequestPermissionRationale(((NewProfileSetting)mContext), Constants.PERMISSIONS_CAMERA[0]))
//                DialogUtils.showOKAlert(mCameraImage, 1, mContext, mContext.getResources()
//                                .getString(R.string.permission_request), mContext.getResources()
//                                .getString(R.string.camera_permission_not_granted), null);
            dismiss();
        }

    }
}
