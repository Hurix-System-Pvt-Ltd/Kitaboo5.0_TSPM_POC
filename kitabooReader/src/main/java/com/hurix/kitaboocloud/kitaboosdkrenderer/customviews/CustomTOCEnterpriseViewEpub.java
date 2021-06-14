package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.IPage;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.toc.TableOfContentVO;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;


import java.util.ArrayList;


public class CustomTOCEnterpriseViewEpub extends CustomTOCBaseView {
    private  String mCurrentChapterTitle;
    private TextView _txtmessage;
    private ArrayList<ArrayList<TableOfContentVO>> _rootColl = new ArrayList<ArrayList<TableOfContentVO>>();
    private int _totalDepth;
    private Typeface mTypeface;
    private LinearLayout _expandableRoot;
    private ScrollView _scrollRoot;
    private Context mContext;
    private boolean Ismobile;
    private boolean _isExpandClick;
    private ReaderThemeSettingVo readerThemeSettingVo;
    private IPage mActivePageData;
    ArrayList<TableOfContentVO> parentColl;
    String firstPosition = "";
    String secondPosition = "";
    String thirdPosition = "";
    String tocPostionId;
    boolean isFirsttime;


    public CustomTOCEnterpriseViewEpub(Context context) {
        super(context, R.layout.custom_toc_enterprise);
    }

    public CustomTOCEnterpriseViewEpub(Context context, ArrayList<TableOfContentVO> tocColl,
                                       CustomTOCEnterpriseView.TocItemClickListener listner,
                                       boolean ismobile, ReaderThemeSettingVo _themeUserSettingVo, IPage pagedata, String currentChapterTitle) {
        //super(context,R.layout.toc_enterprise);
        super(context, R.layout.custom_toc_enterprise, tocColl);
        super.setListener(listner);
        readerThemeSettingVo = _themeUserSettingVo;
        Ismobile = ismobile;
        mContext = context;
        mActivePageData = pagedata;
        mCurrentChapterTitle = currentChapterTitle;
        super.initializeContext(context, R.layout.custom_toc_enterprise, tocColl);

    }

    @Override
    public void initView(View view, ArrayList<TableOfContentVO> tocColl) {
        getReaderTyface();
        parentColl = tocColl;
        _scrollRoot = (ScrollView) ((ViewGroup) view).getChildAt(0);
        _expandableRoot = (LinearLayout) view.findViewById(R.id.expandListContainer);

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

        if (parentColl != null) {
            for (int i = 0; i < parentColl.size(); i++) {
                ExpandingViewGroup groupParent = new ExpandingViewGroup(getContext(), 0);
                groupParent.setData(parentColl.get(i), i);
                _expandableRoot.addView(groupParent);

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

            }
        }
    }

    private String setParentChildPosition() {
        String position = "";
        if (parentColl != null && parentColl.size() > 0) {
            for (int i = 0; i < parentColl.size(); i++) {

                if(mActivePageData != null && mCurrentChapterTitle != null )
                {
                    if (parentColl.get(i).getTitle().trim().equalsIgnoreCase(mCurrentChapterTitle)) {
                        position = parentColl.get(i).getTOCPosition();
                    } else {
                        if (parentColl.get(i).getChildren().size() > 0) {
                            for (int j = 0; j < parentColl.get(i).getChildren().size(); j++) {
                                if (parentColl.get(i).getChildren().get(j).getTitle().trim().equalsIgnoreCase(mCurrentChapterTitle)) {
                                    position = parentColl.get(i).getChildren().get(j).getTOCPosition();
                                } else {
                                    if (parentColl.get(i).getChildren().get(j).getChildren().size() > 0) {
                                        for (int k = 0; k < parentColl.get(i).getChildren().get(j).getChildren().size(); k++) {
                                            if (parentColl.get(i).getChildren().get(j).getChildren().get(k).getTitle().trim().equalsIgnoreCase(mCurrentChapterTitle)) {
                                                position = parentColl.get(i).getChildren().get(j).getChildren().get(k).getTOCPosition();
                                            }
                                        }
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
    public int getTotalDepth() {
        return 0;
    }

    @Override
    public void OnClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //LogReporter.getInstance().info(SyncStateContract.Constants.TAG,"onItemClick :" +id );
    }

    @Override
    public void openNextLevel(int position) {

    }

    public class ExpandingViewGroup extends LinearLayout implements OnClickListener {

        private boolean _isExpanded = false;
        private boolean _canExpand = false;
        private TableOfContentVO _data;
        private LinearLayout _container;
        private LinearLayout _childContainer;
        private int _depth;
        private LayoutTransition mTransitioner = new LayoutTransition();

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

        public void expandView(TableOfContentVO data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            TOCElementViewHolder holder = (TOCElementViewHolder) view.getTag();
            onClick(holder.frameNext);
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


        public void setData(TableOfContentVO data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            TOCElementViewHolder holder = (TOCElementViewHolder) view.getTag();
            holder.frameNext.setOnClickListener(this);
            TextView mainTocRootLayout = (TextView) view.findViewById(R.id.txtViewUnitNum);
            mainTocRootLayout.setPadding(_depth * 30, 0, 0, 0);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 1;
            view.setLayoutParams(params);

            _container.addView(view);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.wrapParent) {
                if (_data.getChildren().size() > 0) {
                    TOCElementViewHolder holder = returnHolderForView(_container);
                    if (_childContainer.getChildCount() == 0) {
                        _childContainer.setVisibility(View.VISIBLE);
                        holder.textViewTocNext.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
                        holder.textViewTocNext.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getMoreIconColor()));
                        //holder.txtViewTitle.setTextColor(Color.parseColor(themeUserSettingVo.get_reader_icon_color()));
                        /*if (themeUserSettingVo != null) {
                            //holder.selectionIndicator.setBackgroundColor(Color.parseColor(themeUserSettingVo.get_reader_icon_color()));
                        } else {
                            // holder.selectionIndicator.setBackgroundColor(getResources().getColor(R.color.kitaboo_main_color));
                        }*/
                        holder.txtViewPageNum.setVisibility(GONE);
                        for (int i = 0; i < _data.getChildren().size(); i++) {
                            final ExpandingViewGroup groupParent = new ExpandingViewGroup(getContext(), _depth + 1);
                            _isExpandClick = true;
                            groupParent.setData(_data.getChildren().get(i), i);
                            _childContainer.addView(groupParent);
                            if (!thirdPosition.isEmpty()) {
                                if (_data.getChildren().get(i).getTOCPosition().equalsIgnoreCase(firstPosition + "." + secondPosition)) {
                                    groupParent.expandView(parentColl.get(Integer.parseInt(firstPosition) - 1).getChildren().get(Integer.parseInt(secondPosition) - 1), Integer.parseInt(secondPosition) - 1);
                                }
                            }

                        }
                    } else {
                        _childContainer.setVisibility(View.GONE);
                        _childContainer.removeAllViews();
                        _isExpandClick = false;
                        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
                        //  holder.selectionIndicator.setBackgroundColor(getResources().getColor(R.color.white));
                       /* if (themeUserSettingVo != null) {
                            holder.selectionIndicator.setBackgroundColor(Color.parseColor(themeUserSettingVo.get_reader_icon_color()));
                        }else {
                            holder.selectionIndicator.setBackgroundColor(getResources().getColor(R.color.kitaboo_main_color));
                        }*/
                        holder.txtViewPageNum.setVisibility(GONE);
                    }
                }
            } else if (v.getId() == this.getId()) {
                String baseUrl = _data.getmBaseUrl();
                String anchor = _data.getmAnchor();
                getListener().onTocitemClick("", baseUrl, anchor, Ismobile, _data.getPageid());
            }
        }
    }

    class TOCElementViewHolder {
        TextView txtViewUnitNum;
        TextView txtViewTitle;
        RelativeLayout frameNext, mTocTitleLayout;
        TextView textViewTocNext;
        TextView txtViewPageNum;
        View selectionIndicator;
        View divider;
    }

    public View getView(TableOfContentVO currTocVO, final int position, View convertView) {
        TOCElementViewHolder holder = null;
        if (currTocVO != null) {
            if (convertView != null && convertView.getTag() == null) {
                convertView = null;
            }
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_tocenterpriselistitem, null);
                holder = returnHolderForView(convertView);
                convertView.setTag(holder);
            } else {
                holder = (TOCElementViewHolder) convertView.getTag();
            }
            if (readerThemeSettingVo != null) {
                holder.txtViewPageNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
                holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getTitleColor()));
            } else {
                holder.txtViewPageNum.setTextColor(Color.parseColor("#000000"));
                holder.txtViewTitle.setTextColor(Color.parseColor("#000000"));
            }

            holder.txtViewUnitNum.setVisibility(View.INVISIBLE);
            holder.txtViewPageNum.setVisibility(GONE);
            //}
            holder.txtViewTitle.setText(currTocVO.getTitle().trim());

            if (_isExpandClick) {
                //holder.selectionIndicator.setBackgroundColor(Color.parseColor(themeUserSettingVo.get_reader_icon_color()));

            } else {
                // holder.selectionIndicator.setBackgroundColor(getResources().getColor(R.color.white));
                convertView.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().getTableofcontents().getPopupBackground()));
            }

            if (currTocVO.getChildren().size() == 0) {
                holder.frameNext.setVisibility(View.INVISIBLE);
            } else {
                holder.frameNext.setVisibility(View.VISIBLE);
            }

            if (currTocVO.getFolioID() != null && currTocVO.getFolioID().equalsIgnoreCase(mActivePageData.getFolioID())){
                holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getTitleColor()));
                holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getTitleColor()));
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getArrowColor()));
            }else if (currTocVO!=null && mActivePageData != null && currTocVO.getPageid() == (mActivePageData.getPageID())){
                holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getTitleColor()));
                holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getTitleColor()));
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(readerThemeSettingVo.getReader().getDayMode().
                        getTableofcontents().getSelectedToc().getArrowColor()));
            }else if (currTocVO.getTitle() != null && mActivePageData != null && currTocVO.getTitle().trim().equalsIgnoreCase(mCurrentChapterTitle)) {
                holder.txtViewTitle.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                holder.txtViewUnitNum.setTextColor(Color.parseColor(!readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                                getDayMode().getTableofcontents().getSelectedToc().getTitleColor()));
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
            }

            if (!firstPosition.isEmpty() && currTocVO.getTOCPosition().startsWith(firstPosition)) {
                holder.selectionIndicator.setBackgroundColor(Color.parseColor(!readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor().contains("#") ? "#" +readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getTitleColor() : readerThemeSettingVo.getReader().
                        getDayMode().getTableofcontents().getSelectedToc().getArrowColor()));
            }
            holder.txtViewUnitNum.setText(currTocVO.getTOCPosition());

        } else {
            convertView = new TextView(getContext());
            convertView.setTag(null);
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
        holder.mTocTitleLayout = (RelativeLayout) findViewById(R.id.toc_title_layout);
        holder.divider = convertView.findViewById(R.id.divider);

        if (readerThemeSettingVo != null) {
            holder.txtViewUnitNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getTitleColor()));
            holder.txtViewTitle.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getTitleColor()));
            holder.txtViewPageNum.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getTitleColor()));
            holder.textViewTocNext.setTextColor(Color.parseColor(readerThemeSettingVo.getReader().
                    getDayMode().getTableofcontents().getMoreIconColor()));

        } else {
            holder.txtViewUnitNum.setTextColor(Color.parseColor("#000000"));
            holder.txtViewTitle.setTextColor(Color.parseColor("#000000"));
            holder.txtViewPageNum.setTextColor(Color.parseColor("#000000"));
            holder.textViewTocNext.setTextColor(Color.parseColor("#000000"));
        }
        holder.divider.setBackgroundColor(getResources().getColor(R.color.toc_divider_color));
        holder.txtViewPageNum.setVisibility(View.VISIBLE);
        holder.txtViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.txtViewUnitNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.txtViewPageNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.textViewTocNext.setTypeface(mTypeface);
        holder.textViewTocNext.setAllCaps(false);
        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        return holder;
    }

    private void getReaderTyface() {
        String fontfile = com.hurix.commons.utils.Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(mContext, fontfile);
        } else {
            mTypeface = Typefaces.get(mContext, "kitabooread.ttf");
        }
    }
}

