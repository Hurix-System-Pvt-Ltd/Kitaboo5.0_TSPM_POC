package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.IPage;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.toc.TableOfContentVO;

import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;


public class CustomTOCEnterpriseView extends CustomTOCBaseView {
    private TextView _txtmessage;
    private ArrayList<ArrayList<TableOfContentVO>> _rootColl = new ArrayList<ArrayList<TableOfContentVO>>();
    private int _totalDepth;
    private Typeface typeface;
    private LinearLayout _expandableRoot;
    private ScrollView _scrollRoot;
    private Context mContext;
    private Typeface customTypeFace;
    public static String DEFAULT_PANEL_PAGE_FONT_SIZE = "16";
    private ArrayList<TableOfContentVO> parentColl;
    private boolean Ismobile;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private ArrayList<String> mTotalTOCPageIdList;
    private IPage mActivePageData;
    boolean iscalled = false;
    String firstPosition = "";
    String secondPosition = "";
    String thirdPosition = "";
    String tocPostionId;
    boolean isFirsttime;

    public CustomTOCEnterpriseView(Context context, ArrayList<TableOfContentVO> tocColl, TocItemClickListener listner, boolean ismobile, ReaderThemeSettingVo _themeUserSettingVo, IPage pagedata) {
        super(context, R.layout.custom_toc_enterprise, tocColl);
        super.setListener(listner);
        readerThemeSettingVo = _themeUserSettingVo;
        Ismobile = ismobile;
        mContext = context;
        mActivePageData = pagedata;
        super.initializeContext(context, R.layout.custom_toc_enterprise, tocColl);

    }

    @Override
    public void initView(View view, ArrayList<TableOfContentVO> tocColl) {
        parentColl = tocColl;
        mTotalTOCPageIdList = new ArrayList<>();
        getReaderTyface();
        _scrollRoot = (ScrollView) ((ViewGroup) view).getChildAt(0);
        _expandableRoot = (LinearLayout) view.findViewById(R.id.expandListContainer);
        customTypeFace = Typefaces.get(getContext(), getContext().getResources()
                .getString(R.string.text_font_file));
        if (readerThemeSettingVo != null) {
            view.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            _expandableRoot.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            _scrollRoot.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));

        }


        ExpandingViewGroup groupParent = null;
        for (int i = 0; i < parentColl.size(); i++) {

            groupParent = new ExpandingViewGroup(getContext(), 0);
            groupParent.setData(parentColl.get(i), i);
            groupParent.setListener(super.getListener());
            _expandableRoot.addView(groupParent);
            findTOCPosition();
            if (!secondPosition.isEmpty()) {
                if (!firstPosition.isEmpty() && parentColl.get(i).getTOCPosition().equalsIgnoreCase(firstPosition)) {
                    if (parentColl.get(Integer.parseInt(firstPosition) - 1).getChildren().size() > 0) {
                        groupParent.expandView(parentColl.get(Integer.parseInt(firstPosition) - 1), Integer.parseInt(firstPosition) - 1);
                        isFirsttime = true;
                    }
                }
            } else {
                firstPosition = "";
            }


            mTotalTOCPageIdList.add(parentColl.get(i).getFolioID());
        }


    }

    private void findTOCPosition() {
        tocPostionId = setParentChildPosition();


        int firstIndex = tocPostionId.indexOf(".");
        if (firstIndex > 0) {
            firstPosition = tocPostionId.substring(0, firstIndex);

            String dummySecond = tocPostionId.substring(firstIndex + 1);


            int secondIndex = dummySecond.indexOf(".");

            if (secondIndex > 0) {
                secondPosition = dummySecond.substring(0, secondIndex);

                String dummyThird = dummySecond.substring(secondIndex + 1);

                int thirdIndex = dummySecond.substring(secondIndex + 1).indexOf(".");
                if (thirdIndex > 0) {

                } else {
                    thirdPosition = dummySecond.substring(secondIndex + 1);
                }
            } else {
                secondPosition = dummySecond.substring(secondIndex + 1);
            }


        } else {
            firstPosition = tocPostionId.substring(firstIndex + 1);
        }
    }


    public int getTotalDepth() {
        return _totalDepth;
    }

    private String setParentChildPosition() {
        String position = "";
        for (int i = 0; i < parentColl.size(); i++) {
            if (parentColl.get(i).getPageid() == (mActivePageData.getPageID())) {
                position = parentColl.get(i).getTOCPosition();
            } else {
                if (parentColl.get(i).getChildren().size() > 0) {
                    for (int j = 0; j < parentColl.get(i).getChildren().size(); j++) {
                        if (parentColl.get(i).getChildren().get(j).getPageid() == (mActivePageData.getPageID())) {
                            position = parentColl.get(i).getChildren().get(j).getTOCPosition();
                        } else {
                            if (parentColl.get(i).getChildren().get(j).getChildren().size() > 0) {
                                for (int k = 0; k < parentColl.get(i).getChildren().get(j).getChildren().size(); k++) {
                                    if (parentColl.get(i).getChildren().get(j).getChildren().get(k).getPageid() == (mActivePageData.getPageID())) {
                                        position = parentColl.get(i).getChildren().get(j).getChildren().get(k).getTOCPosition();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return position;
    }


    @Override
    public void openNextLevel(final int pageid) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void OnClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public class ExpandingViewGroup extends LinearLayout implements OnClickListener {

        private CustomTOCEnterpriseView.TocItemClickListener mListner;
        private boolean _isExpanded = false;
        private boolean _canExpand = false;
        private TableOfContentVO _data;
        private LinearLayout _container;
        private LinearLayout _childContainer;
        private int _depth;

        private LayoutTransition mTransitioner = new LayoutTransition();
        private Object parentChildPosition;

        public ExpandingViewGroup(Context context, int depth) {

            super(context);
            _depth = depth;
            this.setOrientation(LinearLayout.VERTICAL);
            this.setOnClickListener(this);
            _container = new LinearLayout(getContext());
            _container.setOrientation(LinearLayout.VERTICAL);

            _childContainer = new LinearLayout(getContext());
            _childContainer.setOrientation(LinearLayout.VERTICAL);

            this.addView(_container);
            this.addView(_childContainer);

            _childContainer.setLayoutTransition(mTransitioner);
            setupCustomAnimations();
        }

        public void setListener(CustomTOCEnterpriseView.TocItemClickListener listner) {
            mListner = listner;
        }

        private void setupCustomAnimations() {

            // Adding
            ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY", -_childContainer
                    .getMeasuredHeight(), _container.getTranslationY()).
                    setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
            mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);

            // Removing
            ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", _container
                    .getTranslationY(), -_childContainer.getMeasuredHeight()).
                    setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
            mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

        }


        public void expandView(TableOfContentVO data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            TOCElementViewHolder holder = (TOCElementViewHolder) view.getTag();
            onClick(holder.frameNext);
        }

        public void setData(TableOfContentVO data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            TOCElementViewHolder holder = (TOCElementViewHolder) view.getTag();
            holder.frameNext.setOnClickListener(this);
            TextView mainTocRootLayout = (TextView) view.findViewById(R.id.txtViewUnitNum);
            if (readerThemeSettingVo != null) {
                mainTocRootLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            } else {
                mainTocRootLayout.setBackgroundColor(Color.WHITE);
            }
            mainTocRootLayout.setPadding(_depth * 30, 0, 0, 0);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 1;
            view.setLayoutParams(params);
            _container.addView(view);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == this.getId()) {
                if(_data != null) {
                    try {
                        int pageID = _data.getPageid();
                        if (pageID < 1) {
                            pageID = pageID + 1;
                            if (_data.getChildren().size() > 0) {
                                pageID = _data.getChildren().get(0).getPageid();
                            }
                        }

                        if (pageID >= 0) {

                            if (_data.getPageid() != 0) {
                                mListner.onTocitemClick(_data.getFolioID(), "", "", Ismobile, _data.getPageid());
                                ArrayList<String> tags = new ArrayList<String>();
                                tags.add("toc");
                                removeFragements(getContext(), false, tags);
                                _data = null;
                            } else {
                                if (_data.getChildren() != null && _data.getChildren().get(0).getFolioID() != null) {
                                    mListner.onTocitemClick(_data.getChildren().get(0).getFolioID(), "", "", Ismobile, _data.getPageid());
                                    ArrayList<String> tags = new ArrayList<String>();
                                    tags.add("toc");
                                    removeFragements(getContext(), false, tags);
                                    _data = null;
                                } else {
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.resource_not_available), Toast.LENGTH_SHORT).show();
                                }
                                //DialogUtils.displayToast(mContext,mContext.getResources().getString(R.string.resource_not_available), Toast.LENGTH_LONG, Gravity.CENTER);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (v.getId() == R.id.wrapParent) {
                if(_data!= null) {
                    try {
                        if (_data.getChildren().size() > 0) {
                            TOCElementViewHolder holder = returnHolderForView(_container);
                            if (_childContainer.getChildCount() == 0) {
                                holder.textViewTocNext.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
                                holder.textViewTocNext.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
                                holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                                holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                                holder.mainTocRootLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                                        getDayMode().getTableofcontents().getPopupBackground()));
                                ExpandingViewGroup groupParent = null;
                                for (int i = 0; i < _data.getChildren().size(); i++) {
                                    groupParent = new ExpandingViewGroup(getContext(), _depth + 1);
                                    groupParent.setData(_data.getChildren().get(i), i);
                                    groupParent.setListener(mListner);
                                    _childContainer.addView(groupParent);
                                    if (!thirdPosition.isEmpty()) {
                                        if (_data.getChildren().get(i).getTOCPosition().equalsIgnoreCase(firstPosition + "." + secondPosition)) {
                                            groupParent.expandView(parentColl.get(Integer.parseInt(firstPosition) - 1).getChildren().get(Integer.parseInt(secondPosition) - 1), Integer.parseInt(secondPosition) - 1);
                                        }
                                    }
                                }
                            } else {
                                _childContainer.removeAllViews();
                                _container.invalidate();
                                _container.requestLayout();
                                holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class TOCElementViewHolder {
        TextView txtViewUnitNum;
        TextView txtViewTitle;
        RelativeLayout frameNext;
        TextView textViewTocNext;
        TextView txtViewPageNum;
        View selectionIndicator;
        View divider;
        RelativeLayout mainTocRootLayout;

    }

    public View getView(TableOfContentVO currTocVO, final int position, View convertView) {
        TOCElementViewHolder holder = null;
        if (currTocVO != null) {
            if (convertView != null && convertView.getTag() == null) {
                convertView = null;
            }
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_tocenterpriselistitem, null);
                if (readerThemeSettingVo != null) {
                    convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    convertView.setBackgroundColor(Color.WHITE);
                }
                holder = returnHolderForView(convertView);
                convertView.setTag(holder);
            } else {
                holder = (TOCElementViewHolder) convertView.getTag();
            }
            holder.txtViewUnitNum.setVisibility(View.INVISIBLE);
            // If currTocVO.getParentId()== 0 then treat as parent
            if (currTocVO.getParentId() == 0)        //UI for Parent at 0 index i.e. left most item in TOC
            {
                if (currTocVO != null && currTocVO.getChildren().size() > 0) {

                    if (readerThemeSettingVo != null) {

                        holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getTitleColor()));
                        holder.txtViewTitle.setTypeface(null, Typeface.BOLD);
                        holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getTitleColor()));
                        holder.mainTocRootLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getPopupBackground()));
                        holder.txtViewUnitNum.setTypeface(null, Typeface.BOLD);
                    } else {

                        holder.txtViewTitle.setTextColor(Color.parseColor("#000000"));
                        holder.txtViewTitle.setTypeface(null, Typeface.BOLD);
                        holder.txtViewUnitNum.setTextColor(Color.parseColor("#000000"));
                        holder.txtViewUnitNum.setTypeface(null, Typeface.BOLD);
                    }

                }

            } else    //UI for Children
            {

                if (readerThemeSettingVo != null) {
                    holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                            getDayMode().getTableofcontents().getSelectedToc().getDescriptionColor()));
                    holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                            getDayMode().getTableofcontents().getTitleColor()));
                    holder.mainTocRootLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                            getDayMode().getTableofcontents().getPopupBackground()));
                } else {

                    holder.txtViewTitle.setTextColor(Color.parseColor("#000000"));
                    holder.txtViewUnitNum.setTextColor(Color.parseColor("#000000"));
                }
                holder.txtViewUnitNum.setVisibility(View.INVISIBLE);
            }
            // For starting chapter title common for parent and child
            holder.txtViewTitle.setText(currTocVO.getTitle().trim());
            if (currTocVO.getPageid() == mActivePageData.getPageID()) {
                holder.txtViewTitle.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));

                holder.txtViewUnitNum.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));

            }else if (currTocVO.getFolioID().equalsIgnoreCase(mActivePageData.getFolioID())){
                holder.txtViewTitle.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));

                holder.txtViewUnitNum.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
            }

            if (!firstPosition.isEmpty() && currTocVO.getTOCPosition().startsWith(firstPosition)) {
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
            }

            if (currTocVO.getPageid() == mActivePageData.getPageID()) {
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));

            }else if (currTocVO.getFolioID().equalsIgnoreCase(mActivePageData.getFolioID())){
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
            }

            holder.txtViewUnitNum.setText(currTocVO.getTOCPosition().trim());

        } else {
            convertView = new TextView(getContext());
            convertView.setTag(null);
        }
        if (currTocVO != null && currTocVO.getChildren().size() == 0) {
            holder.frameNext.setVisibility(View.INVISIBLE);
            if(currTocVO.getType().equalsIgnoreCase("Chapter"))
            {
                holder.txtViewTitle.setTypeface(null,BOLD);
            }
        } else {
            holder.frameNext.setVisibility(View.VISIBLE);
        }

        // Serial numbers or Unit numbers for TOC not needed for LTPM

        if (getResources().getBoolean(R.bool.is_ltpm_client)) {
            holder.txtViewUnitNum.setVisibility(INVISIBLE);
        }
        return convertView;
    }


    private TOCElementViewHolder returnHolderForView(View convertView) {
        TOCElementViewHolder holder;
        holder = new TOCElementViewHolder();
        holder.txtViewUnitNum = (TextView) convertView.findViewById(R.id.txtViewUnitNum);
        holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
        holder.textViewTocNext = (TextView) convertView.findViewById(R.id.txtnexttoc);
        holder.txtViewPageNum = (TextView) convertView.findViewById(R.id.txtViewPageNum);
        holder.frameNext = (RelativeLayout) convertView.findViewById(R.id.wrapParent);
        holder.selectionIndicator = convertView.findViewById(R.id.selectedview);
        holder.divider = convertView.findViewById(R.id.divider);
        holder.mainTocRootLayout = (RelativeLayout) convertView.findViewById(R.id.mainTocRootLayout);
        holder.txtViewPageNum.setVisibility(View.GONE);
        if (readerThemeSettingVo != null) {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));

        } else {

            convertView.setBackgroundColor(Color.WHITE);
        }
        holder.divider.setBackgroundColor(getResources().getColor(R.color.toc_divider_color));

        holder.txtViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(DEFAULT_PANEL_PAGE_FONT_SIZE));
        holder.txtViewUnitNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(DEFAULT_PANEL_PAGE_FONT_SIZE));
        holder.textViewTocNext.setTypeface(typeface);
        holder.textViewTocNext.setAllCaps(false);
        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        if (readerThemeSettingVo != null) {
            holder.textViewTocNext.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getMoreIconColor()));
            holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getTitleColor()));
            holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getTitleColor()));
            holder.mainTocRootLayout.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getPopupBackground()));
        } else {
            holder.textViewTocNext.setTextColor(Color.parseColor("#000000"));
            holder.txtViewUnitNum.setTextColor(Color.parseColor("#000000"));
            holder.txtViewTitle.setTextColor(Color.parseColor("#000000"));
        }
        return holder;
    }

    public static void removeFragements(Context context,
                                        boolean isAllFragmentRemove, ArrayList<String> tagNametoBeRemoved) {
        try {
            List<Fragment> al = ((FragmentActivity) context)
                    .getSupportFragmentManager().getFragments();
            // You might have to access all the fragments by their tag,
            // in which case just follow the line below to remove the fragment
            if (al != null) {
                // code that handles no existing fragments
                if (isAllFragmentRemove) {
                    for (Fragment frag : al) {
                        if (frag != null && frag.getTag() != null) {
                            ((FragmentActivity) context)
                                    .getSupportFragmentManager()
                                    .beginTransaction().remove(frag).commit();
                        }
                    }
                } else {
                    for (Fragment frag : al) {
                        if (frag != null && frag.getTag() != null) {
                            for (String str : tagNametoBeRemoved) {
                                if (frag.getTag().equalsIgnoreCase(str)) {
                                    ((FragmentActivity) context)
                                            .getSupportFragmentManager()
                                            .beginTransaction().remove(frag)
                                            .commit();
                                }

                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
        }

    }

    public interface TocItemClickListener {
        void onTocitemClick(String folioid, String baseUrl, String anchor, boolean isMobile, int pageid);
    }

    /*public void setThemeColor(ThemeUserSettingVo _themeUserSettingVo) {
        themeUserSettingVo = _themeUserSettingVo;
        if (_scrollRoot != null) {
            _scrollRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
            _expandableRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }*/

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            typeface = Typefaces.get(mContext, fontfile);
        } else {
            typeface = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }
}