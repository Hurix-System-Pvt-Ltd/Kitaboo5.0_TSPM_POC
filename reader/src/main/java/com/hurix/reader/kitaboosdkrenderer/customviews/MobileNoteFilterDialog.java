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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.iconify.Typefaces;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MobileNoteFilterDialog extends DialogFragment implements View.OnClickListener {
    private AppCompatCheckBox mCheckAll, mCheckRed, mCheckYellow, mCheckBlue, mCheckGreen, mCheckPurple,
            mCheckShare, mCheckNotes, mCheckContextNotes;
    private Context mContext;
    private Typeface typeFace;
    private LinearLayout mApplyBtn;
    private AddMyDataFilterListener listener;
    private ArrayList<String> mSelectedColorList;
    private CheckBox[] checkBoxes, mHighlightCheckboxes;
    private ArrayList<String> currentSelectedCheckBox;
    private RelativeLayout mFirstCheckContainer, mSecondCheckContainer;
    private String mLastSelectedCheckbox;
    private boolean mIsMobile;
    private View mTransparentView;
    private Button mApplyBtnNew;
    private boolean mTextNotesClciked, mTextContextNotesClicked;
    private TextView mApplyText, mTextNotes, mTextContextualNotes, mTitleText;
    private LinearLayout mApplyContainer, mCheckBoxContainer, mNoteContainer;
    private ArrayList<String> mListOfStickyNote;
    private ArrayList<String> mListOfContextNote;
    private ArrayList<String> mCurrentNote, mCurrentContextNote;
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private int mShareSetting;

    public MobileNoteFilterDialog(ReaderThemeSettingVo readerThemeSettingVo, int mShareSettingEnable) {
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
            mApplyBtnNew.setOnClickListener(this);
            mApplyText.setVisibility(View.VISIBLE);
            mApplyBtnNew.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().
                    getDayMode().getMyData().getFilterPopup().getActionTextColor()));
            mApplyBtnNew.setBackground(getResources().getDrawable(R.drawable.transparent_view_background));
            mApplyBtnNew.setVisibility(View.GONE);
        } else {
            getDialog().getWindow().setGravity(Gravity.CENTER);

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mApplyBtnNew.setOnClickListener(this);
            mApplyBtnNew.setVisibility(View.VISIBLE);
            mApplyText.setVisibility(View.GONE);
            mApplyBtnNew.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));
            mApplyBtnNew.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
           // mApplyBtnNew.setBackground(getResources().getDrawable(R.drawable.my_data_apply_btn_background));
           // mApplyContainer.setBackground(null);
            mCheckBoxContainer.setBackground(null);
            mTransparentView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()));
            mNoteContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));

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
        enableApplyButton();
        if (v == mCheckAll) {
            hideCheckboxes();
            if (mCheckAll.isChecked()) {
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i].setChecked(true);
                }
                mCheckAll.setTextColor(Color.parseColor(mReaderThemeSettingVo.
                        getReader().getDayMode().getMyData().getFilterPopup().getAllBoxBorderColor()));
                mTextNotes.setTextColor(getResources().getColor(R.color.grey));
                mTextContextualNotes.setTextColor(getResources().getColor(R.color.grey));
                mListOfStickyNote.clear();
                mListOfContextNote.clear();
                mListOfStickyNote.add("#F4D631");
                mListOfStickyNote.add("#fcf9a3");
                mListOfStickyNote.add("#cd3a3a");
                mListOfStickyNote.add("#33E5B5");
                mListOfStickyNote.add("#99cc00");
                mListOfStickyNote.add("#8E44AD");
                mListOfStickyNote.add("shared_note");
                mListOfStickyNote.add("#ffff7061");
                mListOfStickyNote.add("#fffff261");
                mListOfStickyNote.add("#F00004");
                mListOfStickyNote.add("#FF7061");
                mListOfStickyNote.add("#80FFFF00");
                mListOfStickyNote.add(getResources().getString(R.string.note_yellow_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_red_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_purple_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_lightgreen_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_skyblue_color));

                mListOfStickyNote.add(getResources().getString(R.string.note_orange_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_pink_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_new_purple_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_green_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_blue_color));


                mListOfContextNote.add("#F4D631");
                mListOfContextNote.add("#fcf9a3");
                mListOfContextNote.add("#cd3a3a");
                mListOfContextNote.add("#33E5B5");
                mListOfContextNote.add("#99cc00");
                mListOfContextNote.add("#8E44AD");
                mListOfContextNote.add("shared_note");
                mListOfContextNote.add("#ffff7061");
                mListOfContextNote.add("#fffff261");
                mListOfContextNote.add("#F00004");
                mListOfContextNote.add("#FF7061");
                mListOfContextNote.add("#80FFFF00");
                mListOfContextNote.add(getResources().getString(R.string.note_yellow_color));
                mListOfContextNote.add(getResources().getString(R.string.note_red_color));
                mListOfContextNote.add(getResources().getString(R.string.note_purple_color));
                mListOfContextNote.add(getResources().getString(R.string.note_lightgreen_color));
                mListOfContextNote.add(getResources().getString(R.string.note_skyblue_color));

                mListOfContextNote.add(getResources().getString(R.string.note_orange_color));
                mListOfContextNote.add(getResources().getString(R.string.note_pink_color));
                mListOfContextNote.add(getResources().getString(R.string.note_new_purple_color));
                mListOfContextNote.add(getResources().getString(R.string.note_green_color));
                mListOfContextNote.add(getResources().getString(R.string.note_blue_color));

                // visibleCheckboxes();
            } else {
                disableAppluButton();
                mListOfStickyNote.clear();
                mListOfContextNote.clear();
                mCheckAll.setTextColor(getResources().getColor(R.color.grey));
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i].setChecked(false);
                }
                // hideCheckboxes();
            }
        }
        if (v == mTextNotes) {
            if (!mTextNotesClciked) {
                mTextNotes.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));
                mTextContextualNotes.setTextColor(getResources().getColor(R.color.grey));
                mTextContextNotesClicked = false;
                mTextNotesClciked = true;
                //applycheckboxEnable();
                setCheckboxText("¸");
                visibleCheckboxes();
                unselectAllCheckboxes();
                enableSelectedCheckBox(mListOfStickyNote);
            } else {
                mTextNotes.setTextColor(getResources().getColor(R.color.grey));
                mTextNotesClciked = false;
                hideCheckboxes();
            }

        }

        if (v == mTextContextualNotes) {
            if (!mTextContextNotesClicked) {
                mTextNotesClciked = false;
                mTextNotes.setTextColor(getResources().getColor(R.color.grey));
                mTextContextualNotes.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));
                mTextContextNotesClicked = true;
                //applycheckboxEnable();
                setCheckboxText("V");
                visibleCheckboxes();
                unselectAllCheckboxes();
                enableSelectedCheckBox(mListOfContextNote);
            } else {
                mTextContextualNotes.setTextColor(getResources().getColor(R.color.grey));
                mTextContextNotesClicked = false;
                hideCheckboxes();
            }

        }

        if (v == mCheckYellow) {
            if (mCheckYellow.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("#F4D631");
                    mListOfStickyNote.add("#fcf9a3");
                    mListOfStickyNote.add("#ffff7061");
                    mListOfStickyNote.add("#fffff261");
                    mListOfStickyNote.add("#80FFFF00");
                    mListOfStickyNote.add(getResources().getString(R.string.note_yellow_color));
                    mListOfStickyNote.add(getResources().getString(R.string.note_orange_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("#F4D631");
                    mListOfContextNote.add("#fcf9a3");
                    mListOfContextNote.add("#ffff7061");
                    mListOfContextNote.add("#fffff261");
                    mListOfContextNote.add("#80FFFF00");
                    mListOfContextNote.add(getResources().getString(R.string.note_yellow_color));
                    mListOfContextNote.add(getResources().getString(R.string.note_orange_color));
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("#F4D631");
                    mListOfStickyNote.remove("#fcf9a3");
                    mListOfStickyNote.remove("#ffff7061");
                    mListOfStickyNote.remove("#fffff261");
                    mListOfStickyNote.remove("#80FFFF00");
                    mListOfStickyNote.remove(getResources().getString(R.string.note_yellow_color));
                    mListOfStickyNote.remove(getResources().getString(R.string.note_orange_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("#F4D631");
                    mListOfContextNote.remove("#fcf9a3");
                    mListOfContextNote.remove("#ffff7061");
                    mListOfContextNote.remove("#fffff261");
                    mListOfContextNote.remove("#80FFFF00");
                    mListOfContextNote.remove(getResources().getString(R.string.note_yellow_color));
                    mListOfContextNote.remove(getResources().getString(R.string.note_orange_color));
                }
            }
        }

        if (v == mCheckRed) {
            if (mCheckRed.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("#cd3a3a");
                    mListOfStickyNote.add("#F00004");
                    mListOfStickyNote.add("#FF7061");
                    mListOfStickyNote.add(getResources().getString(R.string.note_red_color));
                    mListOfStickyNote.add(getResources().getString(R.string.note_pink_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("#cd3a3a");
                    mListOfContextNote.add("#F00004");
                    mListOfContextNote.add("#FF7061");
                    mListOfContextNote.add(getResources().getString(R.string.note_red_color));
                    mListOfContextNote.add(getResources().getString(R.string.note_pink_color));
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("#cd3a3a");
                    mListOfStickyNote.remove("#F00004");
                    mListOfStickyNote.remove("#FF7061");
                    mListOfStickyNote.remove(getResources().getString(R.string.note_red_color));
                    mListOfStickyNote.remove(getResources().getString(R.string.note_pink_color));

                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("#cd3a3a");
                    mListOfContextNote.remove("#F00004");
                    mListOfContextNote.remove("#FF7061");
                    mListOfContextNote.remove(getResources().getString(R.string.note_red_color));
                    mListOfContextNote.remove(getResources().getString(R.string.note_pink_color));
                }
            }
        }

        if (v == mCheckBlue) {
            if (mCheckBlue.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("#33E5B5");
                    mListOfStickyNote.add(getResources().getString(R.string.note_skyblue_color));
                    mListOfStickyNote.add(getResources().getString(R.string.note_blue_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("#33E5B5");
                    mListOfContextNote.add(getResources().getString(R.string.note_skyblue_color));
                    mListOfContextNote.add(getResources().getString(R.string.note_blue_color));
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("#33E5B5");
                    mListOfStickyNote.remove(getResources().getString(R.string.note_skyblue_color));
                    mListOfStickyNote.remove(getResources().getString(R.string.note_blue_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("#33E5B5");
                    mListOfContextNote.remove(getResources().getString(R.string.note_skyblue_color));
                    mListOfContextNote.remove(getResources().getString(R.string.note_blue_color));
                }
            }
        }

        if (v == mCheckGreen) {
            if (mCheckGreen.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("#99cc00");
                    mListOfStickyNote.add(getResources().getString(R.string.note_lightgreen_color));
                    mListOfStickyNote.add(getResources().getString(R.string.note_green_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("#99cc00");
                    mListOfContextNote.add(getResources().getString(R.string.note_lightgreen_color));
                    mListOfContextNote.add(getResources().getString(R.string.note_green_color));
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("#99cc00");
                    mListOfStickyNote.remove(getResources().getString(R.string.note_lightgreen_color));
                    mListOfStickyNote.remove(getResources().getString(R.string.note_green_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("#99cc00");
                    mListOfContextNote.remove(getResources().getString(R.string.note_lightgreen_color));
                    mListOfContextNote.remove(getResources().getString(R.string.note_green_color));
                }
            }
        }

        if (v == mCheckPurple) {
            if (mCheckPurple.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("#8E44AD");
                    mListOfStickyNote.add(getResources().getString(R.string.note_purple_color));
                    mListOfStickyNote.add(getResources().getString(R.string.note_new_purple_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("#8E44AD");
                    mListOfContextNote.add(getResources().getString(R.string.note_purple_color));
                    mListOfContextNote.add(getResources().getString(R.string.note_new_purple_color));
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("#8E44AD");
                    mListOfStickyNote.remove(getResources().getString(R.string.note_purple_color));
                    mListOfStickyNote.remove(getResources().getString(R.string.note_new_purple_color));
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("#8E44AD");
                    mListOfContextNote.remove(getResources().getString(R.string.note_purple_color));
                    mListOfContextNote.remove(getResources().getString(R.string.note_new_purple_color));
                }
            }
        }

        if (v == mCheckShare) {
            if (mCheckShare.isChecked()) {
                if (mTextNotesClciked) {
                    mListOfStickyNote.add("shared_note");
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.add("shared_note");
                }

            } else {
                if (mTextNotesClciked) {
                    mListOfStickyNote.remove("shared_note");
                }

                if (mTextContextNotesClicked) {
                    mListOfContextNote.remove("shared_note");
                }
            }
        }

        if (v == mCheckNotes) {
            if (mCheckNotes.isChecked()) {
                if (mTextNotesClciked) {
                    applycheckboxEnable();
                }

                mListOfStickyNote.clear();
                mListOfStickyNote.add("#F4D631");
                mListOfStickyNote.add("#fcf9a3");
                mListOfStickyNote.add("#cd3a3a");
                mListOfStickyNote.add("#33E5B5");
                mListOfStickyNote.add("#99cc00");
                mListOfStickyNote.add("#8E44AD");
                mListOfStickyNote.add("#ffff7061");
                mListOfStickyNote.add("#fffff261");
                mListOfStickyNote.add("#F00004");
                mListOfStickyNote.add("#FF7061");
                mListOfStickyNote.add("#80FFFF00");
                mListOfStickyNote.add("#80FFFF00");
                mListOfStickyNote.add(getResources().getString(R.string.note_yellow_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_red_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_purple_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_lightgreen_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_skyblue_color));

                mListOfStickyNote.add(getResources().getString(R.string.note_orange_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_pink_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_new_purple_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_green_color));
                mListOfStickyNote.add(getResources().getString(R.string.note_blue_color));
                mListOfStickyNote.add("shared_note");
            } else {
                if (mTextNotesClciked) {

                }
                unselectAllCheckboxes();
                mListOfStickyNote.clear();
            }
        }

        if (v == mCheckContextNotes) {
            if (mCheckContextNotes.isChecked()) {
                if (mTextContextNotesClicked) {
                    applycheckboxEnable();
                }

                mListOfContextNote.clear();
                mListOfContextNote.add("#F4D631");
                mListOfContextNote.add("#fcf9a3");
                mListOfContextNote.add("#cd3a3a");
                mListOfContextNote.add("#33E5B5");
                mListOfContextNote.add("#99cc00");
                mListOfContextNote.add("#8E44AD");
                mListOfContextNote.add("#ffff7061");
                mListOfContextNote.add("#fffff261");
                mListOfContextNote.add("#F00004");
                mListOfContextNote.add("#FF7061");
                mListOfContextNote.add("#80FFFF00");
                mListOfContextNote.add(getResources().getString(R.string.note_yellow_color));
                mListOfContextNote.add(getResources().getString(R.string.note_red_color));
                mListOfContextNote.add(getResources().getString(R.string.note_purple_color));
                mListOfContextNote.add(getResources().getString(R.string.note_lightgreen_color));
                mListOfContextNote.add(getResources().getString(R.string.note_skyblue_color));

                mListOfContextNote.add(getResources().getString(R.string.note_orange_color));
                mListOfContextNote.add(getResources().getString(R.string.note_pink_color));
                mListOfContextNote.add(getResources().getString(R.string.note_new_purple_color));
                mListOfContextNote.add(getResources().getString(R.string.note_green_color));
                mListOfContextNote.add(getResources().getString(R.string.note_blue_color));

                mListOfContextNote.add("shared_note");
            } else {
                if (mTextContextNotesClicked) {

                }
                unselectAllCheckboxes();
                mListOfContextNote.clear();
            }
        }

        if (mCheckYellow.isChecked() && mCheckRed.isChecked() && mCheckGreen.isChecked()
                && mCheckPurple.isChecked() && mCheckBlue.isChecked() && mCheckShare.isChecked()) {
            if (mTextContextNotesClicked) {
                mCheckContextNotes.setChecked(true);
            } else if (mTextNotesClciked) {
                mCheckNotes.setChecked(true);
            }
        }

        if (!mCheckYellow.isChecked() || !mCheckRed.isChecked() || !mCheckGreen.isChecked()
                || !mCheckPurple.isChecked() || !mCheckBlue.isChecked() || !mCheckShare.isChecked()) {
            if (mTextContextNotesClicked) {
                mCheckContextNotes.setChecked(false);
            } else if (mTextNotesClciked) {
                mCheckNotes.setChecked(false);
            }
        }

        if (mCheckNotes.isChecked() && mCheckContextNotes.isChecked()) {
            mCheckAll.setChecked(true);
            enableApplyButton();
        }

        if (!mCheckNotes.isChecked() || !mCheckContextNotes.isChecked()) {
            mCheckAll.setChecked(false);
            mCheckAll.setTextColor(getResources().getColor(R.color.grey));
        }

        if (v == mApplyBtn || v == mApplyBtnNew || v == mApplyText) {
            if (listener != null) {

                if(!mCheckNotes.isChecked() && ! mTextNotesClciked ) {
                    mListOfStickyNote.clear();
                }

                if(!mCheckContextNotes.isChecked() && ! mTextContextNotesClicked){
                    mListOfContextNote.clear();
                }

                listener.onNoteFilterApplyButtonClicked(mListOfStickyNote, mListOfContextNote);
            }
        }

       if (mCheckAll.isChecked() || mCheckContextNotes.isChecked() || mCheckNotes.isChecked() ||
               mCheckBlue.isChecked() || mCheckGreen.isChecked() || mCheckPurple.isChecked()
               || mCheckRed.isChecked() || mCheckYellow.isChecked() || mCheckShare.isChecked()){
            enableApplyButton();
       }

        if (!mCheckAll.isChecked() && !mCheckContextNotes.isChecked() && !mCheckNotes.isChecked() &&
                !mCheckBlue.isChecked() && !mCheckGreen.isChecked() && !mCheckPurple.isChecked()
                && !mCheckRed.isChecked() && !mCheckYellow.isChecked() && !mCheckShare.isChecked()){
            disableAppluButton();
        }

    }

    private void enableSelectedCheckBox(ArrayList<String> list) {
        for (int i = 0; list.size() > i; i++) {
            if (list.get(i).equalsIgnoreCase("All")) {
                for (int j = 0; j < checkBoxes.length; j++) {
                    checkBoxes[j].setChecked(true);
                }
            } else if (list.get(i).equalsIgnoreCase("shared_note")) {
                mCheckShare.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#cd3a3a") ||
                    list.get(i).equalsIgnoreCase("#F00004") || list.get(i).equalsIgnoreCase("#FF7061") || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_red_color))
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_pink_color))) {
                mCheckRed.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#F4D631")|| list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_yellow_color))
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_orange_color))) {
                mCheckYellow.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#8E44AD")|| list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_purple_color))
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_new_purple_color))) {
                mCheckPurple.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#99cc00")|| list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_lightgreen_color))
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_green_color))) {
                mCheckGreen.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#33E5B5")|| list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_skyblue_color))
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_blue_color))) {
                mCheckBlue.setChecked(true);
            } else if (list.get(i).equalsIgnoreCase("#fcf9a3")
                    || list.get(i).equalsIgnoreCase("#80FFFF00") || list.get(i).equalsIgnoreCase("#ffff7061") || list.get(i).equalsIgnoreCase("#fffff261")
                    || list.get(i).equalsIgnoreCase(getResources().getString(R.string.note_orange_color))) {
                mCheckYellow.setChecked(true);
            }

        }
    }

    private void applycheckboxEnable() {
        for (int i = 3; i < checkBoxes.length; i++) {
            checkBoxes[i].setChecked(true);
        }
    }

    private void setCheckboxText(String text) {
        for (int i = 0; i < mHighlightCheckboxes.length; i++) {
            mHighlightCheckboxes[i].setTypeface(typeFace);
            mHighlightCheckboxes[i].setAllCaps(false);
            mHighlightCheckboxes[i].setText(text);
        }
    }

    private void unselectAllCheckboxes() {
        for (int j = 3; j < checkBoxes.length; j++) {
            checkBoxes[j].setChecked(false);
        }
    }

    private void hideCheckboxes() {
        mFirstCheckContainer.setVisibility(View.GONE);
        mSecondCheckContainer.setVisibility(View.GONE);
    }

    private void visibleCheckboxes() {
        mFirstCheckContainer.setVisibility(View.VISIBLE);
        mSecondCheckContainer.setVisibility(View.VISIBLE);
    }

    private void disableAppluButton() {
        mApplyBtn.setEnabled(false);
        mApplyBtn.setAlpha(0.5f);
        mApplyBtnNew.setEnabled(false);
        mApplyBtnNew.setAlpha(0.5f);
        mApplyText.setEnabled(false);
        mApplyText.setAlpha(0.5f);
    }

    private void enableApplyButton() {
        mApplyBtn.setEnabled(true);
        mApplyBtn.setAlpha(1f);
        mApplyBtnNew.setEnabled(true);
        mApplyBtnNew.setAlpha(1f);
        mApplyText.setEnabled(true);
        mApplyText.setAlpha(1f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mobile_note_filter_dialog, container);

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);

        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    private void initView(View view) {
        setReaderTyface();
        mCheckAll = (AppCompatCheckBox) view.findViewById(R.id.check_all);
        mCheckAll.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getAllBoxBorderColor()))));
        mCheckRed = (AppCompatCheckBox) view.findViewById(R.id.check_red);
        mCheckYellow = (AppCompatCheckBox) view.findViewById(R.id.check_yellow);
        mCheckBlue = (AppCompatCheckBox) view.findViewById(R.id.check_blue);
        mCheckGreen = (AppCompatCheckBox) view.findViewById(R.id.check_green);
        mCheckPurple = (AppCompatCheckBox) view.findViewById(R.id.check_violet);
        mApplyBtn = (LinearLayout) view.findViewById(R.id.apply_btn);
        mApplyBtnNew = (Button) view.findViewById(R.id.appy_btn_new);
        mTitleText = (TextView) view.findViewById(R.id.titleText);
        mTitleText.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getTextColor()));
        //mApplyBtnNew.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
        mCheckShare = (AppCompatCheckBox) view.findViewById(R.id.check_share);
        mCheckNotes = (AppCompatCheckBox) view.findViewById(R.id.check_notes);
        mCheckNotes.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mCheckContextNotes = (AppCompatCheckBox) view.findViewById(R.id.check_context_notes);
        mCheckContextNotes.setSupportButtonTintList(ColorStateList.valueOf((Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()))));
        mFirstCheckContainer = (RelativeLayout) view.findViewById(R.id.first_check_container);
        mSecondCheckContainer = (RelativeLayout) view.findViewById(R.id.second_check_container);
        mTransparentView = view.findViewById(R.id.trasparent_view);
        mApplyContainer = (LinearLayout) view.findViewById(R.id.apply_container);
        mCheckBoxContainer = (LinearLayout) view.findViewById(R.id.check_box_container);
        //mCheckBoxContainer.setBackground(getResources().getDrawable(R.drawable.highlight_filter_view_background));
        mNoteContainer = (LinearLayout) view.findViewById(R.id.note_filter_container);
        mTransparentView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBoxBorderColor()));
        mNoteContainer.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
        mApplyText = (TextView) view.findViewById(R.id.appy_btn_text);
        mApplyText.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getBackground()));
        mApplyText.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));
        mTextNotes = (TextView) view.findViewById(R.id.notes_text);
        mTextContextualNotes = (TextView) view.findViewById(R.id.context_note_text);

        mSelectedColorList = new ArrayList<>();

        checkBoxes = new CheckBox[]{mCheckAll, mCheckNotes, mCheckContextNotes, mCheckShare,
                mCheckRed, mCheckYellow, mCheckBlue, mCheckGreen, mCheckPurple};

        mHighlightCheckboxes = new CheckBox[]{mCheckRed, mCheckYellow,
                mCheckBlue, mCheckGreen, mCheckPurple};


        mCheckShare.setTypeface(typeFace);
        mCheckShare.setAllCaps(false);
        mCheckShare.setText("ř");
        mCheckShare.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getActionTextColor()));

        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setOnClickListener(this);
        }


        mApplyText.setOnClickListener(this);
        mTextNotes.setOnClickListener(this);
        mTextContextualNotes.setOnClickListener(this);
        mCheckAll.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getCheckColor()));
        mCheckNotes.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getCheckColor()));
        mCheckContextNotes.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMyData().getFilterPopup().getCheckColor()));
        // mCheckNotes.setText("Notes");
        //mCheckContextNotes.setText("Contextual Notes");
        mCheckRed.setTextColor(Color.parseColor(getResources().getString(R.string.note_pink_color)));
        mCheckYellow.setTextColor(Color.parseColor(getResources().getString(R.string.note_orange_color)));
        mCheckPurple.setTextColor(Color.parseColor(getResources().getString(R.string.note_new_purple_color)));
        mCheckGreen.setTextColor(Color.parseColor(getResources().getString(R.string.note_green_color)));
        mCheckBlue.setTextColor(Color.parseColor(getResources().getString(R.string.note_blue_color)));

        mListOfStickyNote = new ArrayList<>();
        mListOfContextNote = new ArrayList<>();
        if (mCurrentNote != null) {
            mListOfStickyNote.addAll(mCurrentNote);
        }

        if (mCurrentContextNote != null) {
            mListOfContextNote.addAll(mCurrentContextNote);
        }


        if (this.mCurrentNote == null && this.mCurrentContextNote == null) {
            //mCheckAll.setChecked(true);
           // disableAppluButton();
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].setChecked(true);
            }

            mListOfStickyNote.clear();
            mListOfContextNote.clear();
            mListOfStickyNote.add("#F4D631");
            mListOfStickyNote.add("#fcf9a3");
            mListOfStickyNote.add("#cd3a3a");
            mListOfStickyNote.add("#33E5B5");
            mListOfStickyNote.add("#99cc00");
            mListOfStickyNote.add("#8E44AD");
            mListOfStickyNote.add("shared_note");
            mListOfStickyNote.add("#ffff7061");
            mListOfStickyNote.add("#fffff261");
            mListOfStickyNote.add("#F00004");
            mListOfStickyNote.add("#FF7061");
            mListOfStickyNote.add("#80FFFF00");
            mListOfStickyNote.add(getResources().getString(R.string.note_yellow_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_red_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_purple_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_lightgreen_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_skyblue_color));

            mListOfStickyNote.add(getResources().getString(R.string.note_orange_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_pink_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_new_purple_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_green_color));
            mListOfStickyNote.add(getResources().getString(R.string.note_blue_color));


            mListOfContextNote.add("#F4D631");
            mListOfContextNote.add("#fcf9a3");
            mListOfContextNote.add("#cd3a3a");
            mListOfContextNote.add("#33E5B5");
            mListOfContextNote.add("#99cc00");
            mListOfContextNote.add("#8E44AD");
            mListOfContextNote.add("shared_note");
            mListOfContextNote.add("#ffff7061");
            mListOfContextNote.add("#fffff261");
            mListOfContextNote.add("#F00004");
            mListOfContextNote.add("#FF7061");
            mListOfContextNote.add("#80FFFF00");
            mListOfContextNote.add(getResources().getString(R.string.note_yellow_color));
            mListOfContextNote.add(getResources().getString(R.string.note_red_color));
            mListOfContextNote.add(getResources().getString(R.string.note_purple_color));
            mListOfContextNote.add(getResources().getString(R.string.note_lightgreen_color));
            mListOfContextNote.add(getResources().getString(R.string.note_skyblue_color));

            mListOfContextNote.add(getResources().getString(R.string.note_orange_color));
            mListOfContextNote.add(getResources().getString(R.string.note_pink_color));
            mListOfContextNote.add(getResources().getString(R.string.note_new_purple_color));
            mListOfContextNote.add(getResources().getString(R.string.note_green_color));
            mListOfContextNote.add(getResources().getString(R.string.note_blue_color));

        } else {
            selectCheckBox();
        }

        if (mCurrentContextNote != null && mCurrentNote != null){
            if (mCurrentContextNote.size() == 12 && mCurrentNote.size() == 12 ){
                mCheckAll.setChecked(true);
            }
        }

        mCheckShare.setVisibility(mShareSetting);
        if(getResources().getBoolean(R.bool.is_ACEP_client) || getResources().getBoolean(R.bool.is_AAO)){
            mCheckShare.setVisibility(View.INVISIBLE);
        }/*else{
            mCheckShare.setVisibility(View.VISIBLE);
        }*/

    }

    private void selectCheckBox() {
        if (mCurrentNote.size() == 12) {
            mCheckNotes.setChecked(true);
        }

        if (mCurrentContextNote.size() == 12) {
            mCheckContextNotes.setChecked(true);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void setListener(AddMyDataFilterListener listener) {
        this.listener = listener;
    }

    public void setCurrentSelectedCheckBox(ArrayList<String> noteList, ArrayList<String> contextNoteList) {
        this.mCurrentNote = noteList;
        this.mCurrentContextNote = contextNoteList;
    }
}
