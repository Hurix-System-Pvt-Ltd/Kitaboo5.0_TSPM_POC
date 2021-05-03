package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.commons.Constants.EBookType;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.iconify.Typefaces;
import com.hurix.customui.datamodel.BookMarkVO;
import com.hurix.customui.iconify.Utils;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;

/**
 * Created by amitraj.sharma on 4/13/2018.
 */

public class CustomTOBNewAdpater extends BaseAdapter {

    private LayoutInflater _mInflater;
    private ArrayList<BookMarkVO> data;
    private RelativeLayout _layout;
    private Typeface _typeFace;
    private Context _mContext;
    private EBookType mReaderType;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private ArrayList<BookMarkVO> mListOfCurrentBookmark;


    public CustomTOBNewAdpater(Context context) {
        _mContext = context;
        this._mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getReaderTyface();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(ArrayList<BookMarkVO> _data, EBookType readerType, ReaderThemeSettingVo _themeUserSettingVo, ArrayList<BookMarkVO> bookmarkList) {
        this.data = _data;
        mReaderType = readerType;
        readerThemeSettingVo = _themeUserSettingVo;
        mListOfCurrentBookmark = bookmarkList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = _mInflater.inflate(R.layout.custom_bookmarks_item, parent, false);
            _layout = (RelativeLayout) convertView.findViewById(R.id.bookmark_list_item);
            if (readerThemeSettingVo != null) {
                _layout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            } else {
                _layout.setBackgroundColor(Color.WHITE);
            }
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.txttitle);
            holder.mTextTime = (TextView) convertView.findViewById(R.id.text_time);
            holder.imgresourcetype = (TextView) convertView.findViewById(R.id.imgresourcetype);
            holder.arrow = (TextView) convertView.findViewById(R.id.btnArrow);
            holder.txtpageNo = (TextView) convertView.findViewById(R.id.txtresourcepageno);
            holder.mSelecedIndicator = convertView.findViewById(R.id.selectedview);
            holder.txtpageNo.setVisibility(View.INVISIBLE);
            holder.arrow.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mSelecedIndicator.setVisibility(View.INVISIBLE);
        holder.arrow.setTypeface(_typeFace);
        holder.arrow.setAllCaps(false);
        holder.arrow.setText(PlayerUIConstants.BD_RESOURCES_ARROW_IC_TEXT);
        holder.imgresourcetype.setTypeface(_typeFace);
        holder.imgresourcetype.setAllCaps(false);
        holder.arrow.setTextColor(PlayerUIConstants.BD_RESOURCES_ARROW_IC_FC);
        holder.txtname.setText(data.get(position).getBookmarkTitle());
        holder.mTextTime.setText(Utils.getDateInLocalFormat(data.get(position).getDateTime(), "hh:mm a dd MMMM yyyy"));
        holder.mTextTime.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        if (readerThemeSettingVo != null) {
            holder.txtname.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtname.setTextColor(_mContext.getResources().getColor(R.color.reader_font_color));
        }

        if (_mContext.getResources().getBoolean(R.bool.show_toc_folio_id)) {
            if (mReaderType == EBookType.FIXEDKITABOO) {
                holder.txtpageNo.setText(_mContext.getResources().getString(R.string.bookmark_page_label)+ "" + data.get(position).getFolioID());
                holder.txtpageNo.setVisibility(View.VISIBLE);
            } else if (mReaderType == EBookType.FIXEDEPUB) {
                holder.txtpageNo.setText(_mContext.getResources().getString(R.string.bookmark_page_label)+ "" + data.get(position).getPageNoFixedEpub());
                holder.txtpageNo.setVisibility(View.VISIBLE);
            } else {
                holder.txtpageNo.setVisibility(View.GONE);
            }
            holder.txtpageNo.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
        } else {
            holder.txtpageNo.setVisibility(View.GONE);
        }
        for (int i = 0; i < mListOfCurrentBookmark.size(); i++) {
            if (mListOfCurrentBookmark.get(i).getFolioID().equalsIgnoreCase(data.get(position).getFolioID())) {
                holder.mSelecedIndicator.setVisibility(View.VISIBLE);
                holder.mSelecedIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor().contains("#") ? "#" + readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));

                holder.txtname.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            }
        }
        holder.imgresourcetype.setText(PlayerUIConstants.BM_IC_ADDED_TEXT);

        if(!readerThemeSettingVo.getReader().
                getDayMode().getTableofcontents().getSelectedToc().getIconColor().toString().contains("#"))
        {
            holder.imgresourcetype.setTextColor(Color.parseColor("#"+readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getSelectedToc().getIconColor()));
        }
        else
        {
            holder.imgresourcetype.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getSelectedToc().getIconColor()));
        }

        holder.imgresourcetype.setBackgroundColor(PlayerUIConstants.BM_IC_SELECTED_BGC);
        return convertView;
    }

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            _typeFace = Typefaces.get(_mContext, fontfile);
        } else {
            _typeFace = Typefaces.get(_mContext, "kitabooread.ttf");
        }
    }

    static class ViewHolder {
        TextView txtname;
        TextView imgresourcetype, arrow;
        TextView txtpageNo, txmessage, mTextTime;
        View mSelecedIndicator;
    }
}
