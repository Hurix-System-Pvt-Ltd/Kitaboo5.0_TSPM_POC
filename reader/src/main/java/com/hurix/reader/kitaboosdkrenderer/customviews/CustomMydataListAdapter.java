package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.KitabooSDKModel;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.datamodel.HighlightVO;
import com.hurix.customui.datamodel.UserVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.iconify.Utils;
import com.hurix.customui.views.DividerView;


import com.hurix.downloadbook.controller.UserController;
import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.renderer.utility.Utility;

import java.util.ArrayList;


public class CustomMydataListAdapter extends BaseAdapter implements Filterable {
    private static final int ITEM_NOTE = 0;
    private static final int ITEM_NOTIFICATION = 1;
    private ArrayList<HighlightVO> mListofdata;
    private Context mContext;
    private LayoutInflater mInflater;
    private Typeface mTypeFace;
    int mNotificationCount;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private int mShareVisible = View.VISIBLE;
    boolean isEditEnabled = false;
    private int mSettingEnable;

    private CustomMyDataLayout.CustomMydataLayoutCallback mListner;

    public CustomMydataListAdapter(Context context, int mShareSettingEnable) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSettingEnable = mShareSettingEnable;
        setReaderTyface();
    }

    public void setData(ArrayList<HighlightVO> listofdata, int notifycount, ReaderThemeSettingVo themeUserSettingVo) {
        this.mListofdata = listofdata;
        mNotificationCount = notifycount;
        readerThemeSettingVo = themeUserSettingVo;
    }

    public void setShareSettingVisibility(int visibility) {
        mShareVisible = visibility;
    }


    public ArrayList<HighlightVO> getData() {
        return this.mListofdata;
    }

    @Override
    public int getCount() {
        return mListofdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mListofdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mNotificationCount) ? ITEM_NOTIFICATION : ITEM_NOTE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ITEM_NOTIFICATION) {

        } else {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_enterprise_ugc_item, parent, false);
                holder = new ViewHolder();

                holder.txttitle = (TextView) convertView.findViewById(R.id.txttitle);
                holder.chapterName = (TextView) convertView.findViewById(R.id.txtChapterName);
                holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
                holder.txtdata = (TextView) convertView.findViewById(R.id.txthighlightdata);
                holder.mLayoutButtons = (LinearLayout) convertView.findViewById(R.id.layoutButtons);
                if (mContext.getResources().getBoolean(R.bool.is_AAO) || mContext.getResources().getBoolean(R.bool.is_ACEP_client)) {
                    holder.mLayoutButtons.setVisibility(View.GONE);
                }

                holder.imgnotetype = (TextView) convertView.findViewById(R.id.btnresourcetype);
                holder.imgnotetype.setTypeface(mTypeFace);
                holder.imgnotetype.setAllCaps(false);

                holder.tvShare = (TextView) convertView.findViewById(R.id.tvShare);
                holder.dividerView = (DividerView) convertView.findViewById(R.id.dividerViewTop);


                StateListDrawable stateListDrawable = new StateListDrawable();

                stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                        Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground()),
                                new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 0, Color.TRANSPARENT));

                stateListDrawable.addState(new int[]{},
                        Utils.getRectAngleDrawable(Color.TRANSPARENT, new float[]{
                                8, 8, 8, 8, 8, 8, 8, 8}, 0, Color.TRANSPARENT));

                holder.tvShare.setBackgroundDrawable(stateListDrawable);

                stateListDrawable = new StateListDrawable();

                stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                        Utils.getRectAngleDrawable(Color.parseColor(readerThemeSettingVo.
                                        getReader().getDayMode().getMyData().getDeSelectedButton().getBackground()),
                                new float[]{8, 8, 8, 8, 8, 8, 8, 8}, 0, Color.TRANSPARENT));

                stateListDrawable.addState(new int[]{},
                        Utils.getRectAngleDrawable(Color.TRANSPARENT, new float[]{
                                8, 8, 8, 8, 8, 8, 8, 8}, 0, Color.TRANSPARENT));

                holder.tvComments = (TextView) convertView.findViewById(R.id.tvComments);

                holder.tvComments.setBackgroundDrawable(stateListDrawable);
                holder.tvCommentsIcon = (TextView) convertView.findViewById(R.id.iconComments);
                holder.tvCommentsIcon.setTypeface(mTypeFace);
                holder.tvCommentsIcon.setAllCaps(false);
                holder.tvCommentsIcon.setText("É");
                holder.tvComments.setVisibility(mSettingEnable);
                holder.tvCommentsIcon.setVisibility(mSettingEnable);

                holder.tvShareIcon = (TextView) convertView.findViewById(R.id.iconShare);
                holder.tvShareIcon.setTypeface(mTypeFace);
                holder.tvShareIcon.setAllCaps(false);
                holder.tvShareIcon.setText("ṋ");
                holder.tvShareIcon.setTextColor(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getSubButton().getDisabledTextIconColor()));
                holder.tvShare.setTextColor(Color.parseColor(readerThemeSettingVo.
                        getReader().getDayMode().getMyData().getSubButton().getDisabledTextIconColor()));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txttitle.setTextSize(16);
            holder.chapterName.setTextSize(19);
            holder.txtdate.setTextSize(15);
            holder.txtdata.setTextSize(18);

            if (mListofdata.size() - 1 == position) {
                holder.dividerView.setVisibility(View.GONE);
            } else {
                holder.dividerView.setVisibility(View.VISIBLE);
            }

            if (mListofdata.get(position) != null) {
                final HighlightVO txtvo = mListofdata.get(position);
                holder.setHighlightVo(txtvo);
                holder.txtdata.setTypeface(holder.txttitle.getTypeface(), Typeface.ITALIC);
                holder.txttitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getContextualtextColor()));
                holder.txtdata.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getDescriptionColor()));
                holder.chapterName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getTextColor()));
                holder.txtdate.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getMetadataColor()));
                holder.txtdate.setAlpha(0.7f);
                holder.txtdata.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getDescriptionColor()));

                if (txtvo.getCreatedByUserVO() != null) {
                    isEditEnabled = txtvo.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID();
                }
                checkForItemVisibility(holder, txtvo);

                if (txtvo.getNoteData() != null) {
                    if (txtvo.getNoteData().equals("")) {
                        //only highlight with out note
                        holder.txtdata.setVisibility(View.VISIBLE);

                        String mainChapterName = txtvo.getChapterName().trim();

                        if (txtvo.getChapterName().contains("/")) {
                            mainChapterName = txtvo.getChapterName().substring(txtvo.getChapterName().lastIndexOf("/") + 1);
                        }

                        holder.chapterName.setText(mContext.getResources().getString(R.string.ugc_mydata_chapter_label) + " " + mainChapterName);

                        if (Utility.isDeviceTypeMobile(mContext)) {
                            if (isEditEnabled) {
                                String datestring = Utils.getDateInLocalFormat(
                                        txtvo.getDateTime(), "hh:mm a dd MMMM yyyy");
                                if (SDKManager.getInstance().getReaderType() != EBookType.REFLOWEPUB) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.reader_page) + " " + txtvo.getFolioID() + "  " + datestring);
                                } else {
                                    holder.txtdate.setText(datestring);
                                }
                                String colorCode = txtvo.getColor();
                                if (new HexValidator().validate(colorCode)) {
                                    if (colorCode.startsWith("#")) {
                                        holder.imgnotetype.setTextColor(Color.parseColor(colorCode));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#" + colorCode));
                                    }
                                } else {
                                    if (txtvo.isImportant()) {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#cd3a3a"));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#F4D631"));
                                    }
                                }

                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);

                            } else {
                                UserVO userVo = null;

                                if (userVo != null) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.
                                            ugc_mydata_shared_by_label) + " " + userVo.getFirstName()
                                            + " " + userVo.getLastName() + " " +
                                            Utils.getDateInLocalFormat(txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                } else {
                                    holder.txtdate.setText(
                                            Utils.getDateInLocalFormat
                                                    (txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                }
                                holder.imgnotetype.setTextColor(PlayerUIConstants.UD_H_IC_READ_ONLY_FC);
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_READ_ONLY_BGC);
                            }
                        } else {
                            if (isEditEnabled) {
                                String datestring = Utils.getDateInLocalFormat(
                                        txtvo.getDateTime(), "hh:mm a dd MMMM yyyy");
                                if (SDKManager.getInstance().getReaderType() != EBookType.REFLOWEPUB) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.reader_page) + " " + txtvo.getFolioID() + "  " + datestring);
                                } else {
                                    holder.txtdate.setText(datestring);
                                }
                                String colorCode = txtvo.getColor();
                                if (new HexValidator().validate(colorCode)) {
                                    if (colorCode.startsWith("#")) {
                                        holder.imgnotetype.setTextColor(Color.parseColor(colorCode));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#" + colorCode));
                                    }
                                } else {
                                    if (txtvo.isImportant()) {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#cd3a3a"));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#F4D631"));
                                    }
                                }

                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);

                            } else {
                                UserVO userVo = null;

                                if (userVo != null) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.
                                            ugc_mydata_shared_by_label) + " " + userVo.getFirstName()
                                            + " " + userVo.getLastName() + " " +
                                            Utils.getDateInLocalFormat(txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                } else {
                                    holder.txtdate.setText(
                                            Utils.getDateInLocalFormat
                                                    (txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                }
                                holder.imgnotetype.setTextColor(PlayerUIConstants.UD_H_IC_READ_ONLY_FC);
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_READ_ONLY_BGC);
                            }
                        }

                        holder.txtdata.setText("");
                        holder.txttitle.setText(txtvo.getHighlightedText());
                        holder.imgnotetype.setText("e");
                        holder.tvComments.setVisibility(View.GONE);
                        holder.tvShare.setVisibility(View.GONE);
                        holder.tvCommentsIcon.setVisibility(View.GONE);
                        holder.tvShareIcon.setVisibility(View.GONE);
                        holder.txtdata.setVisibility(View.GONE);
                        holder.txtdata.setText(txtvo.getNoteData());
                    } else {
                        //Highlight and Sticky note

                        holder.txtdata.setVisibility(View.VISIBLE);

                        String mainChapterName = txtvo.getChapterName().trim();

                        if (txtvo.getChapterName().contains("/")) {
                            mainChapterName = txtvo.getChapterName().substring(txtvo.getChapterName().lastIndexOf("/") + 1);
                        }


                        holder.chapterName.setText(mContext.getResources().getString(R.string.ugc_mydata_chapter_label) + " " + mainChapterName);

                        if (txtvo.getHighlightedText() != null && !txtvo.getHighlightedText().trim().isEmpty()) {
                            holder.imgnotetype.setText(PlayerUIConstants.SN_TEXT_IC_TEXT);
                        } else {
                            holder.imgnotetype.setText(PlayerUIConstants.TB_STICKYNOTE_IC_TEXT);
                        }

                        if (Utility.isDeviceTypeMobile(mContext)) {
                            if (isEditEnabled) {
                                String datestring = Utils.getDateInLocalFormat(
                                        txtvo.getDateTime(), "hh:mm a dd MMMM yyyy");
                                if (SDKManager.getInstance().getReaderType() != EBookType.REFLOWEPUB) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.reader_page) + " " + txtvo.getFolioID() + "  " + datestring);
                                } else {
                                    holder.txtdate.setText(datestring);
                                }
                                String colorCode = txtvo.getColor();
                                if (new HexValidator().validate(colorCode)) {
                                    if (colorCode.startsWith("#")) {
                                        holder.imgnotetype.setTextColor(Color.parseColor(colorCode));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#" + colorCode));
                                    }
                                } else {
                                    if (txtvo.isImportant()) {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#cd3a3a"));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#F4D631"));
                                    }
                                }
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);
                                if (txtvo.getUserShareColl() != null && txtvo.getUserShareColl().size() > 0) {
                                    holder.tvShare.setText(mContext.getString(R.string.share) + " " + (txtvo.
                                            getUserShareColl().size() - 1));
                                } else {
                                    holder.tvShare.setText(mContext.getString(R.string.share) + " " + (txtvo.
                                            getUserShareColl().size()));
                                }
                                if (txtvo.getUserShareColl().size() == 0) {
                                    holder.tvShareIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSubButton().getDisabledTextIconColor()));
                                    holder.tvShare.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSubButton().getDisabledTextIconColor()));
                                } else {
                                    holder.tvShareIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSubButton().getTextIconColor()));
                                    holder.tvShare.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getMyData().getSubButton().getTextIconColor()));
                                }
                                holder.txttitle.setText(txtvo.getHighlightedText());


                            } else {
                                UserVO userVo = null;
                                if (userVo != null) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.
                                            ugc_mydata_shared_by_label) + " " + userVo.getFirstName() +
                                            " " + userVo.getLastName() + " " + Utils.getDateInLocalFormat(txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                } else {
                                    holder.txtdate.setText(Utils.getDateInLocalFormat
                                            (txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                }
                                holder.txttitle.setText(txtvo.getHighlightedText());
                                holder.imgnotetype.setTextColor(PlayerUIConstants.SN_TEXT_IC_READ_ONLY_BGC);
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);

                                if (isEditEnabled && SDKManager.getInstance().isClassAssociated()) {
                                    holder.tvShare.setVisibility(View.VISIBLE);
                                    holder.tvShareIcon.setVisibility(View.VISIBLE);
                                } else {
                                    holder.tvShare.setVisibility(View.GONE);
                                    holder.tvShareIcon.setVisibility(View.GONE);
                                }
                                if (!SDKManager.getInstance().isClassAssociated()) {
                                    holder.tvComments.setVisibility(View.GONE);
                                    holder.tvCommentsIcon.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if (isEditEnabled) {
                                String datestring = Utils.getDateInLocalFormat(
                                        txtvo.getDateTime(), "hh:mm a dd MMMM yyyy");
                                if (SDKManager.getInstance().getReaderType() == EBookType.REFLOWEPUB) {
                                    holder.txtdate.setText(datestring);
                                } else {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.reader_page) + " " + txtvo.getFolioID() + "  " + datestring);
                                }
                                String colorCode = txtvo.getColor();
                                if (new HexValidator().validate(colorCode)) {
                                    if (colorCode.startsWith("#")) {
                                        holder.imgnotetype.setTextColor(Color.parseColor(colorCode));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#" + colorCode));
                                    }
                                } else {
                                    if (txtvo.isImportant()) {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#cd3a3a"));
                                    } else {
                                        holder.imgnotetype.setTextColor(Color.parseColor("#F4D631"));
                                    }
                                }
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);
                                if (txtvo.getUserShareColl() != null && txtvo.getUserShareColl().size() > 0) {
                                    holder.tvShare.setText(mContext.getString(R.string.share) + " " + (txtvo.
                                            getUserShareColl().size() - 1));
                                } else {
                                    holder.tvShare.setText(mContext.getString(R.string.share) + " " + (txtvo.
                                            getUserShareColl().size()));
                                }
                                holder.txttitle.setText(txtvo.getHighlightedText());


                            } else {
                                UserVO userVo = null;

                                if (userVo != null) {
                                    holder.txtdate.setText(mContext.getResources().getString(R.string.
                                            ugc_mydata_shared_by_label) + " " + userVo.getFirstName() +
                                            " " + userVo.getLastName() + " " + Utils.getDateInLocalFormat(txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                } else {
                                    holder.txtdate.setText(Utils.getDateInLocalFormat
                                            (txtvo.getDateTime(), "hh:mm a dd MMM yyyy"));
                                }
                                holder.txttitle.setText(txtvo.getHighlightedText());
                                holder.imgnotetype.setTextColor(PlayerUIConstants.SN_TEXT_IC_READ_ONLY_BGC);
                                holder.imgnotetype.setBackgroundColor(PlayerUIConstants.UD_H_IC_NORMAL_BGC);

                                if (isEditEnabled && SDKManager.getInstance().isClassAssociated()) {
                                    holder.tvShare.setVisibility(View.VISIBLE);
                                    holder.tvShareIcon.setVisibility(View.VISIBLE);
                                } else {
                                    holder.tvShare.setVisibility(View.GONE);
                                    holder.tvShareIcon.setVisibility(View.GONE);
                                }
                                if (!SDKManager.getInstance().isClassAssociated()) {
                                    holder.tvComments.setVisibility(View.GONE);
                                    holder.tvCommentsIcon.setVisibility(View.GONE);
                                }

                            }
                        }
                        holder.tvComments.setText(mContext.getString(R.string.TotalComments) + " " + txtvo.getCommentVos().size());
                        holder.tvCommentsIcon.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_default_panel_actions()));
                        holder.tvComments.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_default_panel_actions()));
                        holder.tvComments.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListner.onMyDataCommentBtnClick(holder.getHighlightVo());
                            }
                        });
                        if (txtvo.getCommentVos().size() == 0) {
                            holder.tvCommentsIcon.setAlpha(0.7f);
                            holder.tvComments.setAlpha(0.7f);
                            holder.tvComments.setEnabled(true);
                        } else {
                            holder.tvCommentsIcon.setAlpha(1.0f);
                            holder.tvComments.setAlpha(1.0f);
                            holder.tvComments.setEnabled(true);
                        }

                        holder.tvShare.setText(mContext.getString(R.string.share) + " " + txtvo.getUserShareColl().size());
                        holder.tvShareIcon.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_default_panel_actions()));
                        holder.tvShare.setTextColor(Color.parseColor(UserController.getInstance(mContext).getUserSettings().get_reader_default_panel_actions()));
                        if (txtvo.getUserShareColl().size() == 0) {
                            holder.tvShareIcon.setAlpha(0.7f);
                            holder.tvShare.setAlpha(0.7f);
                        } else {
                            holder.tvShareIcon.setAlpha(1.0f);
                            holder.tvShare.setAlpha(1.0f);
                        }
                        holder.tvShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListner.onSharebtnClick(holder.getHighlightVo());
                            }
                        });

                        holder.txtdata.setVisibility(View.VISIBLE);
                        holder.txtdata.setText(txtvo.getNoteData());
                    }
                }
            }
        }
        return convertView;
    }

    private void setReaderTyface() {

        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeFace = Typefaces.get(mContext, fontfile);
        } else {
            mTypeFace = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }


    static class ViewHolder {
        LinearLayout mLayoutButtons;
        //Notification fields
        TextView txtpersonname;
        TextView tvAccept, tvReject;
        View mViewNoteNotificationDivider;
        //Note fields
        TextView txttitle;
        TextView chapterName;
        TextView txtdate;
        TextView txtdata;
        TextView imgnotetype;
        TextView tvComments, tvShare;
        TextView tvCommentsIcon, tvShareIcon;
        DividerView dividerView;
        RelativeLayout ListViewContainer;
        private HighlightVO highlightVo;

        public void setHighlightVo(HighlightVO highlightVo) {
            this.highlightVo = highlightVo;
        }

        public HighlightVO getHighlightVo() {
            return highlightVo;
        }
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return null;
    }


    public void setMydataListener(CustomMyDataLayout.CustomMydataLayoutCallback listner) {
        this.mListner = listner;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void checkForItemVisibility(ViewHolder holder, HighlightVO txtvo) {
        // Comments and Share options not required for LTPM
        if (mContext.getResources().getBoolean(R.bool.is_ltpm_client)) {
            holder.tvComments.setVisibility(View.GONE);
            holder.tvCommentsIcon.setVisibility(View.GONE);

            holder.tvShare.setVisibility(View.GONE);
            holder.tvShareIcon.setVisibility(View.GONE);
        } else {
            holder.tvComments.setVisibility(mSettingEnable);
            holder.tvCommentsIcon.setVisibility(mSettingEnable);
            if (isEditEnabled && SDKManager.getInstance().isClassAssociated()) {
                holder.tvShare.setVisibility(mSettingEnable);
                holder.tvShareIcon.setVisibility(mSettingEnable);
            } else {
                holder.tvShare.setVisibility(View.GONE);
                holder.tvShareIcon.setVisibility(View.GONE);
            }
            if (!SDKManager.getInstance().isClassAssociated()) {
                holder.tvComments.setVisibility(View.GONE);
                holder.tvCommentsIcon.setVisibility(View.GONE);
            }

        }

        if (!isSameUser(txtvo)) {
            holder.tvComments.setVisibility(mSettingEnable);
            holder.tvCommentsIcon.setVisibility(mSettingEnable);
        }
    }

    private boolean isSameUser(HighlightVO _data) {
        boolean isSameUser = true;
        if (_data != null && _data.getCreatedByUserVO() != null) {
            isSameUser = _data.getCreatedByUserVO().getUserID() == KitabooSDKModel.getInstance().getUserID();
        }
        return isSameUser;
    }

    public String mData;
    boolean isRequestSuccessful = false;


}