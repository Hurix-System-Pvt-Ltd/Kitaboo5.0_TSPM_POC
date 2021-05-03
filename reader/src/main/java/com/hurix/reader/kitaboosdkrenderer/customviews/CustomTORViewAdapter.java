package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.bookreader.factory.LinkFactory;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.TableOfResourceVo;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.iconify.Typefaces;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class CustomTORViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    public ArrayList<UserChapterVO> _chapterList = new ArrayList<>();
    public Typeface _typeFace;
    private RelativeLayout layout;
    private Typeface customTypeFace;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private boolean fromAutoExpand;
    private String mChapterTitile;

    public CustomTORViewAdapter(Context context) {
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
        return _chapterList.get(groupPosition).getResourcelist().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _chapterList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _chapterList.get(groupPosition).getResourcelist().get(childPosition);
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
        ParentHolder parentHolder = null;

        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.custom_resource_enterprise_itemview, null);
            parentHolder = new ParentHolder();
            parentHolder.chapterNumber = (TextView) convertView.findViewById(R.id.txtViewUnitNum);
            parentHolder.chapterName = (TextView) convertView.findViewById(R.id.txtViewTitle);
            parentHolder.expandableIcon = (TextView) convertView.findViewById(R.id.expandableIcon);
            parentHolder.selected = convertView.findViewById(R.id.selectedview);
            parentHolder.expandableIcon.setTextSize(18);
            if (readerThemeSettingVo != null) {
                parentHolder.selected.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedTabBorder()));
                parentHolder.expandableIcon.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getTitleColor()));
            } else {
                parentHolder.selected.setBackgroundColor(_context.getResources().getColor(R.color.reader_font_color));
                parentHolder.expandableIcon.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            }
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
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
        }else if (mChapterTitile.equalsIgnoreCase(_chapterList.get(groupPosition).getChapterName())){
            parentHolder.selected.setVisibility(View.VISIBLE);
            parentHolder.chapterName.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            parentHolder.chapterNumber.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
        }

        if (!_context.getResources().getBoolean(R.bool.is_ltpm_client)) {
            //LTPM client not required the number sequence
            parentHolder.chapterNumber.setText("" + _chapterList.get(groupPosition).getPostion() + ".");
        }
        parentHolder.chapterName.setText(_chapterList.get(groupPosition).getChapterName());


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.custom_resource_item_enterprise, parent, false);
            layout = (RelativeLayout) convertView.findViewById(R.id.resource_list_item);
            if (readerThemeSettingVo != null) {
                convertView.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            holder = new ChildHolder();
            //layout.setBackgroundColor(Color.WHITE);
            holder.txtname = (TextView) convertView.findViewById(R.id.txttitle);
            holder.imgresourcetype = (TextView) convertView.findViewById(R.id.imgresourcetype);
            holder.imgresourcetype.setAllCaps(false);
            holder.arrow = (TextView) convertView.findViewById(R.id.btnArrow);
            holder.arrow.setAllCaps(false);
            holder.txtpageNo = (TextView) convertView.findViewById(R.id.txtresourcepageno);

            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.arrow.setTypeface(_typeFace);
        holder.arrow.setAllCaps(false);
        holder.arrow.setText(PlayerUIConstants.BD_RESOURCES_ARROW_IC_TEXT);
        holder.imgresourcetype.setTypeface(_typeFace);
        holder.imgresourcetype.setAllCaps(false);
        holder.arrow.setTextColor(PlayerUIConstants.BD_RESOURCES_ARROW_IC_FC);
        holder.imgresourcetype.setVisibility(View.VISIBLE);
        holder.txtname.setText(_chapterList.get(groupPosition).getResourcelist().get(childPosition).getResourceName());
        if (readerThemeSettingVo != null) {
            holder.txtname.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            holder.imgresourcetype.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtname.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            holder.imgresourcetype.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        if (_context.getResources().getBoolean(R.bool.show_toc_folio_id)) {
            holder.txtpageNo.setText(_context.getResources().getString(R.string.reader_page) + _chapterList.get(groupPosition).getResourcelist().get(childPosition).getFolioNo());
        } else {
            holder.txtpageNo.setVisibility(View.GONE);
        }
        setResourcetype(_chapterList.get(groupPosition).getResourcelist().get(childPosition), holder.imgresourcetype);
        if (readerThemeSettingVo != null) {
            holder.txtpageNo.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtpageNo.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        return convertView;
    }

    public void setResourcetype(TableOfResourceVo tableOfResourceVo, TextView view) {
        switch (tableOfResourceVo.getType()) {
            case LINK:
                view.setText(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                break;
            case VIDEO:
                view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                break;
            case IMAGE:
                view.setText(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_UNSELECTED_BGC);
                break;
            case INTERACTIVE:
                view.setText(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_UNSELECTED_BGC);
                break;
            case DOCUMENTS:
                view.setText(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_DOCUMENTS:
                view.setText(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                break;
            case ACTIVITY:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case HTML_WRAP:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_HTML_WRAP:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case KITABOO_WIDGET:
                view.setTypeface(_typeFace);
                view.setAllCaps(false);
                view.setText(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case WEB_ADDRESSES:
                view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_WEB_LINK:
                view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case AUDIO:
                view.setText(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                break;
            case TOC:
                view.setText(PlayerUIConstants.BD_RESOURCES_TOC_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_TOC_IC_UNSELECTED_BGC);
                break;
            case JUMP_TO_BOOK:
                view.setText(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_UNSELECTED_BGC);
                break;
            case ACTIVITY_INJECTION:
                view.setTypeface(_typeFace);
                view.setAllCaps(false);
                String property = LinkFactory.getActivityType(tableOfResourceVo.getProperties());
                if (property.isEmpty() == false) {
                    if (property.equalsIgnoreCase("dropdown")) {
                        view.setText(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_TEXT);
                        view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_UNSELECTED_BGC);
                    } else {
                        view.setText(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_TEXT);
                        view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_UNSELECTED_BGC);
                    }
                }
                break;
            case SLIDESHOW:
                view.setText(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_TEXT);
                view.setBackgroundColor(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_UNSELECTED_BGC);
                break;
            case YOUTUBESTREAMING:
                view.setText(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_UNSELECTED_BGC);
                break;
            case KALTURASTREAMING:
                view.setText(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_UNSELECTED_BGC);
                break;
            case SURVEY:
                view.setText(PlayerUIConstants.BD_RESOURCES_SURVEY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case STANDALONE_INSTRUCTION:
                view.setText(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_UNSELECTED_BGC);
            case VIMEO_VIDEO:
                view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                break;
            case MPO_PLAYER:
                view.setText(PlayerUIConstants.ONLINE_VIDIEO_PLAY_IC_TEXT);
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
            default:
                break;

        }
        if (SDKManager.getInstance().isTeacherExclusive()) {
            if (tableOfResourceVo.getIconUrl().contains("teachericon.png")) {
                view.setText(PlayerUIConstants.TEACHER_ICON);
            }
        }
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

