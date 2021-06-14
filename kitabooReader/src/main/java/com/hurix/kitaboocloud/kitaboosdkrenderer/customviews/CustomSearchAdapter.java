package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hurix.commons.Constants.EBookType;
import com.hurix.customui.datamodel.SearchItemVO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;
import java.util.Locale;


public class CustomSearchAdapter extends BaseAdapter {
    private LayoutInflater _mInflater;
    private ViewHolder holder = null;
    protected ArrayList<SearchItemVO> _listofpages;
    protected Context mContext;
    protected String selectedtext = "";
    private ReaderThemeSettingVo mReaderThemeSettingVo;
    private EBookType mReaderType;

    public CustomSearchAdapter(Context context, ReaderThemeSettingVo theme, EBookType readerType) {
        super();
        _mInflater = LayoutInflater.from(context);
        _listofpages = new ArrayList<SearchItemVO>();
        mContext = context;
        mReaderThemeSettingVo = theme;
        mReaderType = readerType;
    }

    public void setData(ArrayList<SearchItemVO> listofpages) {
        _listofpages = listofpages;
    }

    @Override
    public int getCount() {
        return _listofpages.size();
    }

    @Override
    public Object getItem(int position) {
        return _listofpages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() != null) {
            convertView = _mInflater.inflate(R.layout.custom_search_view_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtsearchdesc);
            holder.txtPageNum = (TextView) convertView.findViewById(R.id.txtsearchpageno);
            holder.mChapterName = (TextView) convertView.findViewById(R.id.chapter_name);
            holder.txtDesc.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getDescriptionColor()));
            holder.txtPageNum.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getSubtextColor()));
            holder.mChapterName.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getSubtextColor()));
            holder.txtPageNum.setAlpha(0.6f);
            holder.mChapterName.setAlpha(0.6f);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 1 || position % 2 != 0) {
            convertView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getSeperationBackground()));
        } else {
            convertView.setBackgroundColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getPopupBackground()));
        }
        if (_listofpages.get(position) != null) {

            if (_listofpages.get(position).get_pageNumber().equals("noSearchResult")) {
                holder.txtDesc.setGravity(Gravity.CENTER);
                holder.txtDesc.setText(_listofpages.get(position).get_pageTextData().trim());
                holder.txtPageNum.setText(mContext.getResources().getText(R.string.search_pages) + " " + _listofpages.get(position).get_displayNumber());
                holder.txtPageNum.setText(mContext.getResources().getText(R.string.reader_page) + _listofpages.get(position).get_displayNumber());
            } else {
                holder.txtDesc.setText(_listofpages.get(position).get_pageTextData().trim());
                if (mReaderType == EBookType.REFLOWEPUB) {
                    holder.mChapterName.setText(_listofpages.get(position).getChapterTitleEpub());
                    holder.txtPageNum.setVisibility(View.GONE);
                } else if (mReaderType == EBookType.FIXEDEPUB) {
                    holder.mChapterName.setText(_listofpages.get(position).getChapterTitleEpub());
                    holder.txtPageNum.setText(mContext.getResources().getText(R.string.search_pages) + " " + getPageNumber(_listofpages.get(position).getChapterIdEpub()));
                    holder.txtPageNum.setText(mContext.getResources().getText(R.string.reader_page)  + getPageNumber(_listofpages.get(position).getChapterIdEpub()));
                } else if (mReaderType == EBookType.FIXEDKITABOO) {
                    holder.txtPageNum.setText(mContext.getResources().getText(R.string.search_pages) + " " +  _listofpages.get(position).get_displayNumber());
                    holder.txtPageNum.setText(mContext.getResources().getText(R.string.reader_page)  + _listofpages.get(position).get_displayNumber());
                    holder.mChapterName.setText(_listofpages.get(position).getChapterName());
                }

                getHighlightedString(holder.txtDesc, _listofpages.get(position).get_pageTextData());
            }
        }
        return convertView;
    }

    private String getPageNumber(String content) {
        String[] pageno;
        if (content.contains("/")) {
            pageno = content.split("/")[1].substring(4).split(".xhtml");
        } else {
            pageno = content.substring(4).split(".xhtml");
        }
        return pageno[0];
    }

    private void getHighlightedString(TextView view, String Data) {
        // default color 2d8dc3
        // porto a9a9a9
        view.setText(Html.fromHtml(replaceAllIgnoreCase(Data, selectedtext)));
    }

    public void setHighlightText(String selectedtext) {
        this.selectedtext = selectedtext.trim();
    }

    protected String replaceAllIgnoreCase(final String text, final String search) {
        if (text != null && search != null) {
            if (text.toUpperCase().contains(search.toUpperCase())) {

                final StringBuffer buffer = new StringBuffer(text);
                int i = 0;
                int prev = 0;
                while ((i = buffer.toString().toLowerCase(Locale.ENGLISH).indexOf(search.toLowerCase(Locale.ENGLISH), prev)) > -1) {
                    String replacement = "<font color='" + Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getSearch().getSelectedTextColor()) + "'>" + buffer.substring(i, i + search.length()) + "</font>";
                    buffer.replace(i, i + search.length(), replacement);
                    prev = i + replacement.length();
                }
                return buffer.toString();
            }
        }
        return text;
    }

    static class ViewHolder {
        TextView txtDesc, mChapterName;
        TextView txtPageNum;


    }
}
