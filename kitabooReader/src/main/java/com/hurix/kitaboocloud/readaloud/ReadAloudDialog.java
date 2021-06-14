package com.hurix.kitaboocloud.readaloud;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.commons.datamodel.KitabooFixedBook;
import com.hurix.kitaboo.constants.ShelfUIConstants;
import com.hurix.kitaboo.constants.utils.Utils;
import com.hurix.kitaboo.iconify.Typefaces;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.interfaces.ReadAloudController;

/**
 * Created by Amit Raj on 10/09/2019.
 */
public class ReadAloudDialog extends Dialog implements View.OnClickListener {

    private TextView mTitle;
    private TextView mClose;
    private View letMe;
    private View autoPlay;
    private View readtoMe;
    private TextView letmeReadIcon;
    private TextView letmeReadIconText;
    private TextView autoPLayIcon;
    private TextView autoPLayIconText;
    private TextView readtomeIcon;
    private TextView readtomeText;
    private Context mContext;
    private ReadAloudController mListener;
    private LinearLayout mParentLayout;
    private KitabooFixedBook.ReadAloudType type;

    public ReadAloudDialog(Context context, ReadAloudController listener) {
        super(context);
        this.mContext = context;
        mListener = listener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Utils.isMobile(mContext)) {
            setContentView(R.layout.read_dialog_mob);
        } else {
            setContentView(R.layout.read_dialog);
        }
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        initView();
        setUpIcon();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.camera_gallery_title);
        mClose = (TextView) findViewById(R.id.closeWindow);
        if (Utils.isMobile(mContext)) {
            mTitle.setText(mContext.getResources().getString(R.string.choose_readaloud_option));
        } else {
            mTitle.setText(mContext.getResources().getString(R.string.choose_readaloud_option));
        }
        letMe = findViewById(R.id.letmeread);
        autoPlay = findViewById(R.id.autoplay);
        readtoMe = findViewById(R.id.readtoMe);
        letmeReadIcon = (TextView) letMe.findViewById(R.id.letmeReadIcon);
        letmeReadIconText = (TextView) letMe.findViewById(R.id.letmeread_text_);
        autoPLayIcon = (TextView) autoPlay.findViewById(R.id.autoplayIcon);
        autoPLayIconText = (TextView) autoPlay.findViewById(R.id.read_aloud_autoPlay_text_);
        readtomeIcon = (TextView) readtoMe.findViewById(R.id.readtomeicon);
        readtomeText = (TextView) readtoMe.findViewById(R.id.readtome_text_);

        mParentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        letmeReadIcon.setOnClickListener(this);
        autoPLayIcon.setOnClickListener(this);
        readtomeIcon.setOnClickListener(this);

        setTheme();
    }

    private void setTheme() {
        /*mTitle.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTitleTextColor()));
        letmeReadIcon.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        letmeReadIconText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        // mCameraImage.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        autoPLayIcon.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        autoPLayIconText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        readtomeIcon.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getIconsColor()));
        readtomeText.setTextColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getTextColor()));

        mParentLayout.setBackgroundColor(Color.parseColor(GlobalDataManager.getInstance().getTheme().getProfile().getProfileEditPopup().getBackground()));*/
    }

    private void setUpIcon() {
        Typeface typeFace = Typefaces.get(mContext, mContext.getResources()
                .getString(R.string.kitaboo_bookshelf_font_file_name));
        mClose.setTypeface(typeFace);
        mClose.setText(ShelfUIConstants.READALOUD_CLOSE_IC_TEXT);
        mClose.setAllCaps(false);
        mClose.setBackgroundColor(Color.TRANSPARENT);
        mClose.setOnClickListener(this);

        letmeReadIcon.setTypeface(typeFace);
        letmeReadIcon.setAllCaps(false);
        letmeReadIcon.setText(ShelfUIConstants.READALOUD_LETMEREAD_IC_TEXT);
        letmeReadIcon.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        letmeReadIcon.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        letmeReadIconText.setText(mContext.getResources().getString(R.string.let_me_read_text_option_title));

        autoPLayIcon.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        autoPLayIcon.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        autoPLayIcon.setTypeface(typeFace);
        autoPLayIcon.setText(ShelfUIConstants.READALOUD_AUTOPLAY_IC_TEXT);
        autoPLayIconText.setText(mContext.getResources().getString(R.string.auto_play_option_title));


        readtomeIcon.setAllCaps(false);
        readtomeIcon.setTypeface(typeFace);
        readtomeIcon.setText(ShelfUIConstants.READALOUD_READTOME_IC_TEXT);
        readtomeIcon.setTextSize(mContext.getResources().getDimension(R.dimen.edit_profile_icon_size));
        readtomeIcon.setBackgroundResource(R.drawable.camera_icon_bg_circle);
        readtomeText.setText(mContext.getResources().getString(R.string.read_to_me_option_title));

    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v == letmeReadIcon) {
                mListener.letMeReadClicked(KitabooFixedBook.ReadAloudType.LETMEREAD);
                mListener.readDialogDismiss();
                dismiss();
            } else if (v == autoPLayIcon) {
                mListener.autoPlayClicked(KitabooFixedBook.ReadAloudType.AUTOPLAY);
                mListener.readDialogDismiss();
                dismiss();
            } else if (v == readtomeIcon) {
                mListener.readToMeClicked(KitabooFixedBook.ReadAloudType.READTOME);
                mListener.readDialogDismiss();
                dismiss();
            } else if (v == mClose) {
                mListener.readDialogDismiss();
                dismiss();
            }
        } else {
            mListener.readDialogDismiss();
            dismiss();
        }
    }

    public String wrapText(int textviewWidth) {

        String string = mContext.getResources()
                .getString(R.string.let_me_read_text_option_title);


        String temp = "";
        String sentence = "";

        String[] array = string.split(" "); // split by space

        for (String word : array) {

            if ((temp.length() + word.length()) < textviewWidth) {  // create a temp variable and check if length with new word exceeds textview width.

                temp += " " + word;

            } else {
                sentence += temp + "\n"; // add new line character
                temp = word;
            }

        }

        return (sentence.replaceFirst(" ", "") + temp);

    }

    public void selectIcon(KitabooFixedBook.ReadAloudType type) {
        if (type != null) {
            if (type.equals(KitabooFixedBook.ReadAloudType.LETMEREAD))
                letmeReadIcon.setAlpha(0.5f);
            if (type.equals(KitabooFixedBook.ReadAloudType.AUTOPLAY))
                autoPLayIcon.setAlpha(0.5f);
            if (type.equals(KitabooFixedBook.ReadAloudType.READTOME))
                readtomeIcon.setAlpha(0.5f);
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.readDialogDismiss();
    }


}
