package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class CustomLTPMTOBViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    public ArrayList<UserChapterVO> _chapterList = new ArrayList<>();
    public Typeface _typeFace;
    private RelativeLayout layout;
    private Typeface customTypeFace;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private boolean fromAutoExpand;
    private String mChapterTitile;

    public CustomLTPMTOBViewAdapter(Context context) {
        this._context = context;
        customTypeFace = Typefaces.get(_context, _context.getResources().getString(R.string.text_font_file));
        getReaderTyface();
    }

    public void setData(ArrayList<UserChapterVO> chapterlist, ReaderThemeSettingVo _themeUserSettingVo) {
        this._chapterList = chapterlist;
        readerThemeSettingVo = _themeUserSettingVo;
    }

    @Override
    public int getGroupCount() {
        return _chapterList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _chapterList.get(groupPosition).getBookMarkList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _chapterList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _chapterList.get(groupPosition).getBookMarkList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return _chapterList.get(groupPosition).getPostion();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CustomLTPMTOBViewAdapter.ParentHolder parentHolder = null;

        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.custom_ltpm_bookmark_enterprise_itemview, null);
            parentHolder = new CustomLTPMTOBViewAdapter.ParentHolder();
            parentHolder.chapterNumber = (TextView) convertView.findViewById(R.id.txtViewUnitNum);
            parentHolder.chapterName = (TextView) convertView.findViewById(R.id.txtViewTitle);
            parentHolder.expandableIcon = (TextView) convertView.findViewById(R.id.expandableIcon);
            parentHolder.selected = convertView.findViewById(R.id.selectedview);
            parentHolder.expandableIcon.setTextSize(18);
            if (readerThemeSettingVo != null) {
                parentHolder.selected.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getTitleColor()));
                parentHolder.expandableIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getTitleColor()));
            } else {
                parentHolder.selected.setBackgroundColor(_context.getResources().getColor(R.color.reader_font_color));
                parentHolder.expandableIcon.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            }
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (CustomLTPMTOBViewAdapter.ParentHolder) convertView.getTag();
        }

        if (readerThemeSettingVo != null) {
            parentHolder.chapterName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            parentHolder.chapterName.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        parentHolder.chapterNumber.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                getDayMode().getTableofcontents().getTitleColor()));
        parentHolder.selected.setVisibility(View.GONE);
        parentHolder.expandableIcon.setTypeface(_typeFace);
        parentHolder.expandableIcon.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        parentHolder.expandableIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                getDayMode().getTableofcontents().getTitleColor()));

        if (mChapterTitile != null) {

            if (isExpanded && (mChapterTitile.equalsIgnoreCase(_chapterList.get(groupPosition).getChapterName()))) {
                // fromAutoExpand = false;
                parentHolder.selected.setVisibility(View.VISIBLE);
                parentHolder.expandableIcon.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
                parentHolder.expandableIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
                parentHolder.chapterName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                parentHolder.chapterNumber.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            } else if (mChapterTitile.equalsIgnoreCase(_chapterList.get(groupPosition).getChapterName())) {
                parentHolder.selected.setVisibility(View.VISIBLE);
                parentHolder.chapterName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                parentHolder.chapterNumber.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            }
        }

        //parentHolder.chapterNumber.setText(groupPosition + 1 + ".");
        parentHolder.chapterName.setText(_chapterList.get(groupPosition).getChapterName());


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        CustomLTPMTOBViewAdapter.ChildHolder holder;
        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.custom_ltpm_bookmark_item_enterprise, parent, false);
            layout = (RelativeLayout) convertView.findViewById(R.id.resource_list_item);
            if (readerThemeSettingVo != null) {
                convertView.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getPopupBackground()));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            holder = new CustomLTPMTOBViewAdapter.ChildHolder();
            //layout.setBackgroundColor(Color.WHITE);
            holder.txtname = (TextView) convertView.findViewById(R.id.txttitle);
            holder.imgresourcetype = (TextView) convertView.findViewById(R.id.imgresourcetype);
            holder.imgresourcetype.setAllCaps(false);
            holder.arrow = (TextView) convertView.findViewById(R.id.btnArrow);
            holder.arrow.setAllCaps(false);
            holder.txtpageNo = (TextView) convertView.findViewById(R.id.txtresourcepageno);

            convertView.setTag(holder);
        } else {
            holder = (CustomLTPMTOBViewAdapter.ChildHolder) convertView.getTag();
        }

        holder.arrow.setTypeface(_typeFace);
        holder.arrow.setAllCaps(false);
        holder.arrow.setText(PlayerUIConstants.BD_RESOURCES_ARROW_IC_TEXT);
        holder.imgresourcetype.setTypeface(_typeFace);
        holder.imgresourcetype.setAllCaps(false);
        holder.arrow.setTextColor(PlayerUIConstants.BD_RESOURCES_ARROW_IC_FC);
        holder.imgresourcetype.setVisibility(View.VISIBLE);
        holder.txtname.setText(_chapterList.get(groupPosition).getBookMarkList().get(childPosition).getBookmarkTitle());
        if (readerThemeSettingVo != null) {
            holder.txtname.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            holder.imgresourcetype.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtname.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            holder.imgresourcetype.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        if (_context.getResources().getBoolean(R.bool.show_toc_folio_id)) {
            holder.txtpageNo.setText(_context.getResources().getString(R.string.reader_page) +
                    _chapterList.get(groupPosition).getBookMarkList().get(childPosition).getFolioID());
        } else {
            holder.txtpageNo.setVisibility(View.GONE);
        }
        holder.imgresourcetype.setText(PlayerUIConstants.BM_IC_ADDED_TEXT);
        holder.imgresourcetype.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
        holder.imgresourcetype.setBackgroundColor(PlayerUIConstants.BM_IC_SELECTED_BGC);
        //setResourcetype(_chapterList.get(groupPosition).getResourcelist().get(childPosition), holder.imgresourcetype);
        if (readerThemeSettingVo != null) {
            holder.txtpageNo.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtpageNo.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            _typeFace = Typefaces.get(_context, fontfile);
        } else {
            _typeFace = Typefaces.get(_context, "kitabooread.ttf");
        }
    }

    public void setFromAutoExpand(boolean autoExpand, String chapterName) {
        this.fromAutoExpand = autoExpand;
        this.mChapterTitile = chapterName;
    }

    class ParentHolder {
        View selected;
        TextView chapterNumber;
        TextView chapterName;
        TextView expandableIcon;
        private boolean fromAutoExpand;
    }

    class ChildHolder {
        TextView txtname, imgresourcetype, arrow, txtpageNo;
    }
}


