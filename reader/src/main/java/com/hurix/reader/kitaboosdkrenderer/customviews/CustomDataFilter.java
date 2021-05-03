package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Filter;

import com.hurix.commons.KitabooSDKModel;
import com.hurix.customui.datamodel.HighlightVO;

import java.util.ArrayList;


public class CustomDataFilter extends Filter {
    private FilterListener mListener;
    private ArrayList<HighlightVO> nList;
    private boolean mIsImpSelected;
    private int mFilterBy;

    //Constant data use for filter data
    public static final int ALL_DATA = 100;
    public static final int SHARED_WITH_ME = 200;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_YES = 201;
    public static final int SHARED_WITH_ME_ACTION_TAKEN_NO = 202;
    public static final int NOTES_DATA = 301;

    public static final String ALL = "all";
    public static final String NOTES = "notes";
    public static final String HIGHLIGHTS = "highlights";
    Context mContext;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint.toString().equalsIgnoreCase(ALL)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < nList.size(); i++) {
                HighlightVO vo = nList.get(i);
                if (vo != null) {
                    if (mFilterBy == SHARED_WITH_ME) {
                        boolean isEditEnabled = vo.getCreatedByUserVO().getUserID() ==
                                KitabooSDKModel.getInstance().getUserID();
                        if (!isEditEnabled) {
                            temp.add(vo);
                        }
                    } else {
                        if (mIsImpSelected) {
                            if (vo != null) {
                                if (vo.isImportant()) {
                                    temp.add(vo);
                                }
                            }
                        } else {
                            temp.add(vo);
                        }
                    }
                }
            }
            results.count = temp.size();
            results.values = temp;
        } else if (constraint.toString().equalsIgnoreCase(NOTES)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < nList.size(); i++) {
                HighlightVO vo = nList.get(i);

                if (!TextUtils.isEmpty(vo.getNoteData())) {
                    temp.add(vo);
                }
            }
            results.count = temp.size();
            results.values = temp;
        } else if (constraint.toString().equalsIgnoreCase(HIGHLIGHTS)) {
            ArrayList<HighlightVO> temp = new ArrayList<HighlightVO>();
            for (int i = 0; i < nList.size(); i++) {
                HighlightVO vo = nList.get(i);
                if ((vo != null && vo.getNoteData() != null && vo.getNoteData().equals("")) ||
                        (vo.getActionTakenStatus().equalsIgnoreCase("N"))) {
                    temp.add(vo);
                }
            }
            results.count = temp.size();
            results.values = temp;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (mListener != null) {
            ArrayList<HighlightVO> coll = null;
            try {
                coll = (ArrayList<HighlightVO>) results.values;
            } catch (Exception e) {

            }
            mListener.onFilteringCompleted(coll);
        }
    }

    public void setData(ArrayList<HighlightVO> alldatalist, Context context) {
        nList = alldatalist;
        mContext = context;
    }

    public interface FilterListener {
        void onFilteringCompleted(ArrayList<HighlightVO> results);
    }

    public void setlistner(FilterListener listener) {
        mListener = listener;
    }

    public void setImpSelected(boolean isImpSelected) {
        mIsImpSelected = isImpSelected;

    }

    public int getFilterBy() {
        return mFilterBy;
    }

    public void setFilterBy(int mFilterBy) {
        this.mFilterBy = mFilterBy;
    }
}
