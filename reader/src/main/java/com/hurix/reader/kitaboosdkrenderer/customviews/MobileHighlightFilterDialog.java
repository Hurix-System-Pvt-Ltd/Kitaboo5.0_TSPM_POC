package com.hurix.reader.kitaboosdkrenderer.customviews;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hurix.customui.iconify.Typefaces;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MobileHighlightFilterDialog extends DialogFragment implements View.OnClickListener {
    private Context mContext;
    private Typeface typeFace;
    private LinearLayout mApplyBtn;
    private AddMyDataFilterListener listener;
    private ArrayList<String> mSelectedColorList;
    private CheckBox[] checkBoxes;
    private ArrayList<String> currentSelectedCheckBox;
    private boolean mIsMobile;
    private View mTransparentView;
    private Button mApplyButtonUpdate;
    private TextView mApplyText, mTitleText;
    private AppCompatCheckBox mCheckAll, mCheckRed, mCheckYellow, mCheckBlue, mCheckGreen, mCheckPurple, mCheckShare;
    private LinearLayout mApplyContainerMobile, mHighlightFilterParentContainer, mHighlightCheckboxParent;
    ReaderThemeSettingVo mReaderThemeSettingVo;
    private int mShareSetting;


    public MobileHighlightFilterDialog(ReaderThemeSettingVo readerThemeSettingVo, int mShareSettingEnable) {
        mReaderThemeSettingVo = readerThemeSettingVo;
        mShareSetting = mShareSettingEnable;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsMobile = getArguments().getBoolean("isMobile");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //setStyle(STYLE_NO_TITLE, R.style.MobileFilterDialogFragment);

        if (mIsMobile) {
            getDialog().getWindow().setGravity(Gravity.BOTTOM);

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mApplyBtn.setOnClickListener(this);
            mApplyText.setVisibility(View.VISIBLE);
            mApplyText.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getActionTextColor()));
            mApplyButtonUpdate.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getActionTextColor()));
            mApplyButtonUpdate.setBackground(getResources().getDrawable(R.drawable.transparent_view_background));
            mApplyButtonUpdate.setVisibility(View.GONE);
        } else {
            getDialog().getWindow().setGravity(Gravity.CENTER);

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mTransparentView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()));
            mApplyButtonUpdate.setVisibility(View.VISIBLE);
            mApplyText.setVisibility(View.GONE);
            mApplyButtonUpdate.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getActionTextColor()));
            mApplyButtonUpdate.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getBackground()));
            mApplyContainerMobile.setBackground(null);
            mHighlightCheckboxParent.setBackground(null);
           /* mApplyButtonUpdate.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getBackground()));*/
           // mApplyButtonUpdate.setBackground(getResources().getDrawable(R.drawable.my_data_apply_btn_background));
            mApplyButtonUpdate.setOnClickListener(this);
            mHighlightFilterParentContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windoparams = window.getAttributes();
        windoparams.dimAmount = 0.5f;
        window.setAttributes(windoparams);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(mReaderThemeSettingVo.
                getReader().getDayMode().getMyData().getFilterPopup().getBackground())));
    }

    private void setReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeFace = Typefaces.get(mContext, fontfile);
        } else {
            typeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v == mCheckAll) {
            if (mCheckAll.isChecked()) {
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i].setChecked(true);
                }
            } else {
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i].setChecked(false);
                }
            }
        }

        if (mCheckPurple.isChecked() && mCheckYellow.isChecked() && mCheckRed.isChecked()
                && mCheckBlue.isChecked() && mCheckGreen.isChecked() && mCheckShare.isChecked()) {
            mCheckAll.setChecked(true);
        } else {
            mCheckAll.setChecked(false);
        }

        if (!mCheckAll.isChecked() && !mCheckYellow.isChecked() && !mCheckRed.isChecked()
                && !mCheckGreen.isChecked() && !mCheckBlue.isChecked() && !mCheckPurple.isChecked()
                && !mCheckShare.isChecked()) {
            mApplyBtn.setEnabled(false);
            mApplyBtn.setAlpha(0.5f);
            mApplyText.setEnabled(false);
            mApplyText.setAlpha(0.5f);
            mApplyButtonUpdate.setEnabled(false);
            mApplyButtonUpdate.setAlpha(0.5f);

        } else {
            mApplyBtn.setEnabled(true);
            mApplyBtn.setAlpha(1f);
            mApplyText.setEnabled(true);
            mApplyText.setAlpha(1f);
            mApplyButtonUpdate.setEnabled(true);
            mApplyButtonUpdate.setAlpha(1f);
        }


        if (v == mApplyBtn || v == mApplyButtonUpdate || v == mApplyText) {
            if (mCheckAll.isChecked()) {
                mSelectedColorList.add("All");
            } else {
                if (mCheckBlue.isChecked()) {
                    mSelectedColorList.add("#33E5B5");
                    mSelectedColorList.add(getResources().getString(R.string.note_skyblue_color));
                    mSelectedColorList.add(getResources().getString(R.string.note_blue_color));
                }

                if (mCheckGreen.isChecked()) {
                    mSelectedColorList.add("#99cc00");
                    mSelectedColorList.add(getResources().getString(R.string.note_lightgreen_color));
                    mSelectedColorList.add(getResources().getString(R.string.note_green_color));
                }

                if (mCheckRed.isChecked()) {
                    mSelectedColorList.add("#cd3a3a");
                    mSelectedColorList.add("#F00004");
                    mSelectedColorList.add("#FF7061");
                    mSelectedColorList.add(getResources().getString(R.string.note_red_color));
                    mSelectedColorList.add(getResources().getString(R.string.note_pink_color));
                }

                if (mCheckYellow.isChecked()) {
                    mSelectedColorList.add("#F4D631");
                    mSelectedColorList.add("#fcf9a3"); //For old normal highlight
                    mSelectedColorList.add("#ffff7061");
                    mSelectedColorList.add("#fffff261");
                    mSelectedColorList.add("#80FFFF00");
                    mSelectedColorList.add(getResources().getString(R.string.note_yellow_color));
                    mSelectedColorList.add(getResources().getString(R.string.note_orange_color));
                }

                if (mCheckPurple.isChecked()) {
                    mSelectedColorList.add("#8E44AD");
                    mSelectedColorList.add(getResources().getString(R.string.note_purple_color));
                    mSelectedColorList.add(getResources().getString(R.string.note_new_purple_color));
                }

                if (mCheckShare.isChecked()) {
                    mSelectedColorList.add("shared_highlight");
                }
            }


            if (listener != null) {
                listener.onApplyButtonClicked(mSelectedColorList);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mobile_filter_dialog, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        if (this.currentSelectedCheckBox == null) {
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].setChecked(true);
            }
        } else {
            selectCheckBox();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    private void initView(View view) {
        setReaderTyface();
        mCheckAll = (AppCompatCheckBox) view.findViewById(R.id.check_all);
        mCheckRed = (AppCompatCheckBox) view.findViewById(R.id.check_red);
        mCheckYellow = (AppCompatCheckBox) view.findViewById(R.id.check_yellow);
        mCheckBlue = (AppCompatCheckBox) view.findViewById(R.id.check_blue);
        mCheckGreen = (AppCompatCheckBox) view.findViewById(R.id.check_green);
        mCheckPurple = (AppCompatCheckBox) view.findViewById(R.id.check_violet);
        mApplyBtn = (LinearLayout) view.findViewById(R.id.apply_btn);
        mCheckShare = (AppCompatCheckBox) view.findViewById(R.id.check_share);
        mTransparentView = view.findViewById(R.id.transparent_view);
        mTransparentView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()));
        mApplyContainerMobile = (LinearLayout) view.findViewById(R.id.apply_container_mobile);
        mHighlightFilterParentContainer = (LinearLayout) view.findViewById(R.id.highlight_filter_parent_container);
        mHighlightFilterParentContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
        mHighlightCheckboxParent = (LinearLayout) view.findViewById(R.id.highlight_check_boxe_parent);
        mApplyButtonUpdate = (Button) view.findViewById(R.id.appy_btn_new);
        mApplyText = (TextView) view.findViewById(R.id.appy_btn_text);
        mTitleText = (TextView)view.findViewById(R.id.titleText);
        mTitleText.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getTextColor()));
        mSelectedColorList = new ArrayList<>();

        checkBoxes = new CheckBox[]{mCheckAll, mCheckRed, mCheckYellow, mCheckBlue, mCheckGreen, mCheckPurple, mCheckShare};

        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setOnClickListener(this);

            if (i != 0 && i != 6) {
                checkBoxes[i].setTypeface(typeFace);
                checkBoxes[i].setAllCaps(false);
                checkBoxes[i].setText("e");
            }
        }

        mCheckShare.setTypeface(typeFace);
        mCheckShare.setAllCaps(false);
        mCheckShare.setText("ř");
        mCheckShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));
        mApplyText.setOnClickListener(this);
        mCheckRed.setTextColor(Color.parseColor(getResources().getString(R.string.note_pink_color)));
        mCheckYellow.setTextColor(Color.parseColor(getResources().getString(R.string.note_orange_color)));
        mCheckPurple.setTextColor(Color.parseColor(getResources().getString(R.string.note_new_purple_color)));
        mCheckGreen.setTextColor(Color.parseColor(getResources().getString(R.string.note_green_color)));
        mCheckBlue.setTextColor(Color.parseColor(getResources().getString(R.string.note_blue_color)));
        mCheckAll.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));

        mCheckAll.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getAllBoxBorderColor()))));
        mCheckRed.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckYellow.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckPurple.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckGreen.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckBlue.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckShare.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));

        mCheckShare.setVisibility(mShareSetting);


        if(getResources().getBoolean(R.bool.is_ACEP_client)|| getResources().getBoolean(R.bool.is_AAO)){
            mCheckShare.setVisibility(View.INVISIBLE);
        }/*else {
            mCheckShare.setVisibility(View.VISIBLE);
        }*/



    }

    private void selectCheckBox() {
        for (int i = 0; currentSelectedCheckBox.size() > i; i++) {
            if (currentSelectedCheckBox.get(i).equalsIgnoreCase("All")) {
                for (int j = 0; j < checkBoxes.length; j++) {
                    checkBoxes[j].setChecked(true);
                }
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("shared_highlight")) {
                mCheckShare.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#cd3a3a") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase("#F00004") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase("#FF7061") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_red_color)) ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_pink_color))) {
                mCheckRed.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#F4D631") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase("#ffff7061") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase("#fffff261") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_yellow_color)) ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_orange_color))) {
                mCheckYellow.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#8E44AD")||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_purple_color))||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_new_purple_color))) {
                mCheckPurple.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#99cc00") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_lightgreen_color))||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_green_color))) {
                mCheckGreen.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#33E5B5") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_skyblue_color))||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase(getResources().getString(R.string.note_blue_color))) {
                mCheckBlue.setChecked(true);
            } else if (currentSelectedCheckBox.get(i).equalsIgnoreCase("#fcf9a3") ||
                    currentSelectedCheckBox.get(i).equalsIgnoreCase("#80FFFF00")) {
                mCheckYellow.setChecked(true);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void setListener(AddMyDataFilterListener listener) {
        this.listener = listener;
    }

    public void setCurrentSelectedCheckBox(ArrayList<String> currentSelectedCheckBox) {
        this.currentSelectedCheckBox = currentSelectedCheckBox;

    }
}
