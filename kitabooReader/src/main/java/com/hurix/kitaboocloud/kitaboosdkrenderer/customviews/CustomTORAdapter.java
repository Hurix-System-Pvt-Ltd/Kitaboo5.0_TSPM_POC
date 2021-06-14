package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.bookreader.factory.LinkFactory;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.datamodel.LinkVO;
import com.hurix.commons.datamodel.TableOfResourceVo;
import com.hurix.commons.datamodel.UserChapterVO;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

public class CustomTORAdapter extends BaseExpandableListAdapter {

    private Context _context;
    public ArrayList<UserChapterVO> _chapterList = new ArrayList<>();
    public Typeface _typeFace;
    private RelativeLayout layout;
    private Typeface customTypeFace;
    private ThemeUserSettingVo themeUserSettingVo;
    private IBook mbookVo;
    private boolean isJumpToBookInNative = false;
    ReaderThemeSettingVo mReaderSettingVo;

    public CustomTORAdapter(Context context,ReaderThemeSettingVo readerSettingVo) {
        this._context = context;
        customTypeFace = Typefaces.get(_context, _context.getResources().getString(R.string.text_font_file));
        mReaderSettingVo = readerSettingVo;
        // _typeFace = Typefaces.get(context, context.getResources().getString(R.string.kitaboo_font_file_name));
        getReaderTyface();
    }

    public void setBookVo(IBook mbookVo, boolean isJumpToBookInNative) {
        this.mbookVo = mbookVo;
        this.isJumpToBookInNative = isJumpToBookInNative;
    }


    public void setData(ArrayList<UserChapterVO> chapterlist, ThemeUserSettingVo _themeUserSettingVo, Typeface typeface) {
        this._chapterList = chapterlist;
        themeUserSettingVo = _themeUserSettingVo;
        _typeFace = typeface;
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
        CustomTORAdapter.ParentHolder parentHolder = null;

        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.resource_enterprise_itemview, null);
            parentHolder = new CustomTORAdapter.ParentHolder();
            parentHolder.chapterNumber = (TextView) convertView.findViewById(R.id.txtViewUnitNum);
            parentHolder.chapterName = (TextView) convertView.findViewById(R.id.txtViewTitle);
            parentHolder.expandableIcon = (TextView) convertView.findViewById(R.id.expandableIcon);
            parentHolder.selected = convertView.findViewById(R.id.selectedview);
            parentHolder.llMainContainer = (LinearLayout) convertView.findViewById(R.id.ll_main_container);
            parentHolder.wrapParent = (FrameLayout) convertView.findViewById(R.id.wrapParent);
            parentHolder.wrapParent.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            if (mReaderSettingVo != null) {
                parentHolder.selected.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedTabBorder()));
                parentHolder.expandableIcon.setTextColor(Color.parseColor(mReaderSettingVo.getReader().
                        getDayMode().getTableofcontents().getTitleColor()));
            } else {
                parentHolder.selected.setBackgroundColor(_context.getResources().getColor(R.color.reader_font_color));
                parentHolder.expandableIcon.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            }
            //parentHolder.selected.setBackgroundColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().get_reader_icon_color()));
            //parentHolder.expandableIcon.setTextColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().getReaderFont()));
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (CustomTORAdapter.ParentHolder) convertView.getTag();
        }

        if (isJumpToBookInNative && _chapterList.get(groupPosition).getResourceObj() != null) {
            ExpandableListView eLV = (ExpandableListView) parent;
            eLV.expandGroup(groupPosition);
        }
        //parentHolder.chapterNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelChapterFontSize()));
        //parentHolder.chapterName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelChapterFontSize()));
        //parentHolder.chapterNumber.setTextColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().getDefaultChapterNameColor()));
        //parentHolder.chapterName.setTextColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().getDefaultChapterNameColor()));

        if (mReaderSettingVo != null) {
            parentHolder.chapterName.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            parentHolder.chapterName.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        parentHolder.chapterNumber.setTextColor(Color.parseColor(mReaderSettingVo.getReader().
                getDayMode().getTableofcontents().getTitleColor()));
        /*if(!mUserSettingVO.getLoginHeaderFontFace().isEmpty()){
            parentHolder.chapterNumber.setTypeface(customTypeFace);
            parentHolder.chapterName.setTypeface(customTypeFace);
        }*/
        parentHolder.selected.setVisibility(View.GONE);
        parentHolder.expandableIcon.setTypeface(_typeFace);
        parentHolder.expandableIcon.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        if (mReaderSettingVo != null)
            parentHolder.expandableIcon.setTextColor(Color.parseColor(mReaderSettingVo.getReader().
                getDayMode().getTableofcontents().getTitleColor()));

        if (isExpanded) {
            if (mReaderSettingVo != null)
                parentHolder.chapterName.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            parentHolder.selected.setVisibility(View.VISIBLE);
            parentHolder.expandableIcon.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
        }

        parentHolder.chapterNumber.setText("" + _chapterList.get(groupPosition).getPostion() + ".");
        parentHolder.chapterName.setText(_chapterList.get(groupPosition).getChapterName());

        if (isJumpToBookInNative && _chapterList.get(groupPosition).getResourceObj() != null) {
            if (_chapterList.get(groupPosition).getResourceObj().getResourceName().equalsIgnoreCase(mbookVo.getChapterName())) {
                parentHolder.selected.setVisibility(View.VISIBLE);
                parentHolder.chapterName.setTextColor(_context.getResources().getColor(R.color.kitaboo_main_color));
                parentHolder.llMainContainer.setBackgroundColor(_context.getResources().getColor(R.color.light_gray));
                onGroupExpanded(groupPosition);
            }
            else
            {
                parentHolder.selected.setVisibility(View.GONE);
                parentHolder.llMainContainer.setBackgroundColor(_context.getResources().getColor(R.color.white));
            }
            if(_chapterList.get(groupPosition).getResourcelist().size() == 0)
            {
                parentHolder.expandableIcon.setVisibility(View.GONE);
            }
            else
            {
                parentHolder.expandableIcon.setVisibility(View.VISIBLE);
            }
            parentHolder.chapterNumber.setVisibility(View.GONE);
        }
        if (mReaderSettingVo != null)
            parentHolder.chapterNumber.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPagenoColor()));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        CustomTORAdapter.ChildHolder holder;
        if (convertView == null) {
            LayoutInflater _mInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _mInflater.inflate(R.layout.resource_item_enterprise, parent, false);
            layout = (RelativeLayout) convertView.findViewById(R.id.resource_list_item);
            if (mReaderSettingVo != null) {
                convertView.setBackgroundColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            }else {
                convertView.setBackgroundColor(Color.WHITE);
            }
            holder = new CustomTORAdapter.ChildHolder();
            //layout.setBackgroundColor(Color.WHITE);
            holder.txtname = (TextView) convertView.findViewById(R.id.txttitle);
            holder.imgresourcetype = (TextView) convertView.findViewById(R.id.imgresourcetype);
            holder.imgresourcetype.setAllCaps(false);
            holder.arrow = (TextView) convertView.findViewById(R.id.btnArrow);
            holder.arrow.setAllCaps(false);
            holder.txtpageNo = (TextView) convertView.findViewById(R.id.txtresourcepageno);

            convertView.setTag(holder);
        } else {
            holder = (CustomTORAdapter.ChildHolder) convertView.getTag();
        }
        //holder.txtname.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelChapterFontSize()));
        //holder.txtpageNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelPageFontSize()));
        /*if(!mUserSettingVO.getLoginHeaderFontFace().isEmpty()){
            holder.txtname.setTypeface(customTypeFace);
            holder.txtpageNo.setTypeface(customTypeFace);
        }*/
        // add data
        holder.arrow.setTypeface(_typeFace);
        holder.arrow.setAllCaps(false);
        holder.arrow.setText(PlayerUIConstants.BD_RESOURCES_ARROW_IC_TEXT);
        holder.imgresourcetype.setTypeface(_typeFace);
        holder.imgresourcetype.setAllCaps(false);
        holder.arrow.setTextColor(PlayerUIConstants.BD_RESOURCES_ARROW_IC_FC);
        holder.imgresourcetype.setVisibility(View.VISIBLE);

        holder.txtname.setText(_chapterList.get(groupPosition).getResourcelist().get(childPosition).getResourceName());
        //holder.txtname.setTextColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().getDefaultChapterNameColor()));
        if (mReaderSettingVo != null) {
            holder.txtname.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            holder.imgresourcetype.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtname.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
            holder.imgresourcetype.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }

        if (_context.getResources().getBoolean(R.bool.show_toc_folio_id)) {
            holder.txtpageNo.setText(_context.getResources().getString(R.string.reader_page) + _chapterList.get(groupPosition).getResourcelist().get(childPosition).getFolioNo());
        } else {
            holder.txtpageNo.setVisibility(View.GONE);
        }
        if (mReaderSettingVo != null) {
            holder.txtpageNo.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtpageNo.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        if (isJumpToBookInNative) {
            setCustomResourcetype(_chapterList.get(groupPosition).getResourcelist().get(childPosition), holder.imgresourcetype);
        } else {
            setResourcetype(_chapterList.get(groupPosition).getResourcelist().get(childPosition), holder.imgresourcetype);
        }
        //holder.txtpageNo.setTextColor(Color.parseColor(UserController.getInstance(parent.getContext()).getUserSettings().getmReaderDefaultPanelHighlightData()));
        if (themeUserSettingVo != null) {
            holder.txtpageNo.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getPagenoColor()));
        }else {
            holder.txtpageNo.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
        }
        return convertView;
    }

    public void setResourcetype(TableOfResourceVo tableOfResourceVo, TextView view) {
        switch (tableOfResourceVo.getType()) {
            case LINK:
                view.setText(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));

                if (themeUserSettingVo != null ) {
                    view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                break;
            case VIDEO:
                view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (mReaderSettingVo != null ) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                break;
            case IMAGE:
                view.setText(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));

                if (mReaderSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_UNSELECTED_BGC);
                break;
            case INTERACTIVE:
                view.setText(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (mReaderSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_UNSELECTED_BGC);
                break;
            case DOCUMENTS:
                view.setText(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_DOCUMENTS:
                view.setText(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                break;
            case ACTIVITY:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case HTML_WRAP:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_HTML_WRAP:
                view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case KITABOO_WIDGET:
                view.setTypeface(_typeFace);
                view.setAllCaps(false);
                view.setText(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_UNSELECTED_BGC);
                break;
            case WEB_ADDRESSES:
                view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case EXTERNAL_WEB_LINK:
                view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case AUDIO:
                view.setText(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                break;
            case TOC:
                view.setText(PlayerUIConstants.BD_RESOURCES_TOC_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_TOC_IC_UNSELECTED_BGC);
                break;
            case JUMP_TO_BOOK:
                view.setText(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_UNSELECTED_BGC);
                break;
            case ACTIVITY_INJECTION:
                view.setTypeface(_typeFace);
                view.setAllCaps(false);
                String property = LinkFactory.getActivityType(tableOfResourceVo.getProperties());
                if (property.isEmpty() == false) {
                    if (property.equalsIgnoreCase("dropdown")) {
                        view.setText(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_TEXT);
                        //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                        if (themeUserSettingVo != null) {
                            view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                        }else {
                            view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                        }
                        view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_UNSELECTED_BGC);
                    } else {
                        view.setText(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_TEXT);
                        //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                        if (themeUserSettingVo != null) {
                            view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                        }else {
                            view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                        }
                        view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_UNSELECTED_BGC);
                    }
                }
                break;
            case SLIDESHOW:
                view.setText(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_UNSELECTED_BGC);
                break;
            case YOUTUBESTREAMING:
                view.setText(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_UNSELECTED_BGC);
                break;
            case KALTURASTREAMING:
                view.setText(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_UNSELECTED_BGC);
                break;
            case SURVEY:
                view.setText(PlayerUIConstants.BD_RESOURCES_SURVEY_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                break;
            case STANDALONE_INSTRUCTION:
                view.setText(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_UNSELECTED_BGC);
            case VIMEO_VIDEO:
                view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                if (themeUserSettingVo != null) {
                    view.setTextColor(Color.parseColor(mReaderSettingVo.getReader().getDayMode().getTableofcontents().getIconsColor()));
                }else {
                    view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                }
                view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                break;
            default:
                break;


        }
        if(SDKManager.getInstance().isTeacherExclusive()) {
            if (tableOfResourceVo.getIconUrl().contains("teachericon.png")) {
                view.setText(PlayerUIConstants.TEACHER_ICON);
            }
        }
    }

    public void setCustomResourcetype(TableOfResourceVo tableOfResourceVo, TextView view) {
        if(!tableOfResourceVo.getMarkupIconType().equals(LinkVO.LinkMarkupType.NONE)){
            switch (tableOfResourceVo.getMarkupIconType()) {
                case TEST_GENERATOR:
                    view.setText(PlayerUIConstants.RESOURCE_TEST_GENERATOR);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case TEST_PAPER:
                    view.setText(PlayerUIConstants.RESOURCE_TEST_PAPER);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case ANSWER_KEYS:
                    view.setText(PlayerUIConstants.RESOURCE_ANSWER_KEYS);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case COMPREHENSIVE_LESSON_PLAN:
                    view.setText(PlayerUIConstants.RESOURCE_COMPREHENSIVE_LESSON_PLAN);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case SUMMARY_LESSON_PLAN:
                    view.setText(PlayerUIConstants.RESOURCE_SUMMARY_LESSON_PLAN);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case LESSON_PLAN:
                    view.setText(PlayerUIConstants.RESOURCE_LESSON_PLAN);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case WORKSHEET:
                    view.setText(PlayerUIConstants.RESOURCE_WORKSHEET);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case APPENDICES:
                    view.setText(PlayerUIConstants.RESOURCE_APPENDICES);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case EMBEDDED_INTERACTIVITY:
                    view.setText(PlayerUIConstants.RESOURCE_EMBEDDED_INTERACTIVITY);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case INTERACTIVITY:
                    view.setText(PlayerUIConstants.RESOURCE_INTERACTIVITY);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case ANIMATION:
                    view.setText(PlayerUIConstants.RESOURCE_ANIMATION);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case VIDEO:
                    view.setText(PlayerUIConstants.RESOURCE_VIDEO);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case AUDIO:
                    view.setText(PlayerUIConstants.RESOURCE_AUDIO_OUP);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case POEM_AUDIO:
                    view.setText(PlayerUIConstants.RESOURCE_POEM_AUDIO);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case PROSE_AUDIO:
                    view.setText(PlayerUIConstants.RESOURCE_PROSE_AUDIO);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case WEB_LINK:
                    view.setText(PlayerUIConstants.RESOURCE_WEB_LINK);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case GLOSSARY:
                    view.setText(PlayerUIConstants.RESOURCE_GLOSSARY);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case PRONUNCIATION:
                    view.setText(PlayerUIConstants.RESOURCE_PRONUNCIATION);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case LEARNING_NUGGETS:
                    view.setText(PlayerUIConstants.RESOURCE_LEARNING_NUGGETS);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case GAME:
                    view.setText(PlayerUIConstants.RESOURCE_GAME);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case GEOME_TOOL:
                    view.setText(PlayerUIConstants.RESOURCE_GEOME_TOOL);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case ABACUS:
                    view.setText(PlayerUIConstants.RESOURCE_ABACUS);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case GEOMETRY_BOX:
                    view.setText(PlayerUIConstants.RESOURCE_GEOMETRY_BOX);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case PDF:
                    view.setText(PlayerUIConstants.RESOURCE_PDF);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case TOOL:
                    view.setText(PlayerUIConstants.RESOURCE_TOOL);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case ADDITIONAL_RESOURCES:
                    view.setText(PlayerUIConstants.RESOURCE_ADDITIONAL_RESOURCES);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case ADDITIONAL_RHYMES:
                    view.setText(PlayerUIConstants.RESOURCE_ADDITIONAL_RHYMES);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case STUDENT_ANSWER_KEYS:
                    view.setText(PlayerUIConstants.RESOURCE_STUDENT_ANSWER_KEYS);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case EXERCISE:
                    view.setText(PlayerUIConstants.RESOURCE_EXERCISE);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case STUDENT_WORK_SHEET:
                    view.setText(PlayerUIConstants.RESOURCE_STUDENT_WORK_SHEET);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case SLIDE_SHOW:
                    view.setText(PlayerUIConstants.RESOURCE_SLIDE_SHOW);
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    } else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                default:
                    break;

            }
        }else{
            switch (tableOfResourceVo.getType()) {
                case LINK:
                    view.setText(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));

                    if (themeUserSettingVo != null ) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case VIDEO:
                    view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null ) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                    break;
                case IMAGE:
                    view.setText(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));

                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_IMAGE_IC_UNSELECTED_BGC);
                    break;
                case INTERACTIVE:
                    view.setText(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_INTERACTIVE_IC_UNSELECTED_BGC);
                    break;
                case DOCUMENTS:
                    view.setText(PlayerUIConstants.RESOURCE_CUSTOMIZED_DOUCUMENT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                    break;
                case EXTERNAL_DOCUMENTS:
                    view.setText(PlayerUIConstants.RESOURCE_CUSTOMIZED_DOUCUMENT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_DOCUMENT_IC_UNSELECTED_BGC);
                    break;
                case ACTIVITY:
                    view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                    break;
                case HTML_WRAP:
                    view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                    break;
                case EXTERNAL_HTML_WRAP:
                    view.setText(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_ACTIVITY_IC_UNSELECTED_BGC);
                    break;
                case KITABOO_WIDGET:
                    view.setTypeface(_typeFace);
                    view.setAllCaps(false);
                    view.setText(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KITABOO_ACTIVITY_IC_UNSELECTED_BGC);
                    break;
                case WEB_ADDRESSES:
                    view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                    break;
                case EXTERNAL_WEB_LINK:
                    view.setText(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                    break;
                case AUDIO:
                    view.setText(PlayerUIConstants.RESOURCE_AUDIO);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_AUDIO_IC_UNSELECTED_BGC);
                    break;
                case TOC:
                    view.setText(PlayerUIConstants.BD_RESOURCES_TOC_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_TOC_IC_UNSELECTED_BGC);
                    break;
                case JUMP_TO_BOOK:
                    view.setText(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_JUMP_TO_BOOK_UNSELECTED_BGC);
                    break;
                case ACTIVITY_INJECTION:
                    view.setTypeface(_typeFace);
                    view.setAllCaps(false);
                    String property = LinkFactory.getActivityType(tableOfResourceVo.getProperties());
                    if (property.isEmpty() == false) {
                        if (property.equalsIgnoreCase("dropdown")) {
                            view.setText(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_TEXT);
                            //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                            if (themeUserSettingVo != null) {
                                view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                            }else {
                                view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                            }
                            view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_DROPDOWN_IC_UNSELECTED_BGC);
                        } else {
                            view.setText(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_TEXT);
                            //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                            if (themeUserSettingVo != null) {
                                view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                            }else {
                                view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                            }
                            view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_FIB_INPUT_IC_UNSELECTED_BGC);
                        }
                    }
                    break;
                case SLIDESHOW:
                    view.setText(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.OS_LINK_IC_SLIDESHOW_UNSELECTED_BGC);
                    break;
                case YOUTUBESTREAMING:
                    view.setText(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_YOUTUBE_VIDEO_IC_UNSELECTED_BGC);
                    break;
                case KALTURASTREAMING:
                    view.setText(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_KALTURA_VIDEO_IC_UNSELECTED_BGC);
                    break;
                case SURVEY:
                    view.setText(PlayerUIConstants.BD_RESOURCES_SURVEY_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_WEBLINK_IC_UNSELECTED_BGC);
                    break;
                case STANDALONE_INSTRUCTION:
                    view.setText(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_Standalone_Instruction_IC_UNSELECTED_BGC);
                case VIMEO_VIDEO:
                    view.setText(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_TEXT);
                    //view.setTextColor(Color.parseColor(UserController.getInstance(_context).getUserSettings().get_reader_icon_color()));
                    if (themeUserSettingVo != null) {
                        view.setTextColor(Color.parseColor(themeUserSettingVo.getReaderFont()));
                    }else {
                        view.setTextColor(_context.getResources().getColor(R.color.reader_font_color));
                    }
                    view.setBackgroundColor(PlayerUIConstants.BD_RESOURCES_VIDEO_IC_UNSELECTED_BGC);
                    break;
                default:
                    break;


            }
        }
        if(SDKManager.getInstance().isTeacherExclusive()) {
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
        if(fontfile!=null && !fontfile.isEmpty()){
            _typeFace   = Typefaces.get(_context,fontfile);
        } else{
            _typeFace   = Typefaces.get(_context,"kitabooread.ttf");
        }
    }

    class ParentHolder {
        LinearLayout llMainContainer;
        View selected;
        TextView chapterNumber;
        TextView chapterName;
        TextView expandableIcon;
        FrameLayout wrapParent;
    }

    class ChildHolder {
        TextView txtname, imgresourcetype, arrow, txtpageNo;
    }
}
