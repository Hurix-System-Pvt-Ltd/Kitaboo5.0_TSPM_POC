package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.toc_standards;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hurix.bookreader.utils.Typefaces;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.customui.Standard.TableOfTEKSVo;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.standard.GetAllTocStandardData;
import com.hurix.customui.toc.standard.GetTocStandardColl;
import com.hurix.customui.toc.standard.TocStandard;
import com.hurix.customui.toc.standard.TocStandardWebview;
import com.hurix.epubreader.Utility.Utils;
import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.CustomTOCEnterpriseView;

import java.util.ArrayList;



/**
 * Created by Amit Raj on 26-07-2019.
 */

public class CustomTEKSEnterpriseView extends CustomTEKSEBaseView implements ExpandableListView.OnChildClickListener,GetTocStandardColl {
    private TextView _txtmessage;
    //private HorizontalSnapLayout _scrollLayout;
    private ArrayList<ArrayList<TableOfTEKSVo>> _rootColl = new ArrayList<ArrayList<TableOfTEKSVo>>();
    private int _totalDepth;
    private Typeface kitabooTypeFace;
    private LinearLayout _expandableRoot;
    private ScrollView _scrollRoot;
    //private UserSettingVO mUserSettingVO;
    private Context mContext;
    private Typeface customTypeFace;
    private boolean isEllipsized = false;
    private boolean isChildExpanded = false;
    private ThemeUserSettingVo themeUserSettingVo;
    public static String DEFAULT_PANEL_PAGE_FONT_SIZE ="16";
    private View rootView;
    private ProgressBar progressBar;
    private CustomTOCEnterpriseView.TocItemClickListener tocListner;
    //private TextView mNoResourceFound;

    public CustomTEKSEnterpriseView(Context context, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> tekscoll) {
        super(context, com.hurix.epubreader.R.layout.teks_enterprise,userSettingVO,tekscoll);
        mContext = context;
    }

    @Override
    public void initView(View view, ThemeUserSettingVo userSettingVO, ArrayList<TableOfTEKSVo> _rootColl) {
        themeUserSettingVo=userSettingVO;
        kitabooTypeFace = Typefaces.get(getContext(), getContext().getResources()
                .getString(com.hurix.epubreader.R.string.kitaboo_font_file_name));
        rootView=view;
        progressBar=(ProgressBar) view.findViewById(com.hurix.epubreader.R.id.progLoadingTeks);
        progressBar.setVisibility(VISIBLE);
        GetAllTocStandardData getAllTocStandardData= new GetAllTocStandardData();
        getAllTocStandardData.setGetTocStandardColl(CustomTEKSEnterpriseView.this);
        getAllTocStandardData.execute(new TocStandard(_rootColl,null));
       /* try {
            ArrayList<TableOfTEKSVo> parentColl=new GetAllTocStandardData().execute(new TocStandard(_rootColl,_teksHelper)).get();
            if (new GetAllTocStandardData().execute(new TocStandard(_rootColl,_teksHelper)).get() != null) {
                _scrollRoot = (ScrollView) ((ViewGroup) view).getChildAt(0);
                _expandableRoot = (LinearLayout) view.findViewById(R.id.expandListContainer);
                customTypeFace = Typefaces.get(getContext(), getContext().getResources()
                        .getString(R.string.text_font_file));
                if (parentColl!=null && parentColl.size() != 0) {
                    for (int i = 0; i < parentColl.size(); i++) {
                        TEKSEnterpriseView.ExpandingViewGroup groupParent = new TEKSEnterpriseView.ExpandingViewGroup(getContext(), 0);
                        groupParent.setData(parentColl.get(i), i);
                        _expandableRoot.addView(groupParent);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        //ArrayList<TableOfTEKSVo> parentColl = _teksHelper.getTEKSColl(_rootColl);
    }


    public int getTotalDepth() {
        return _totalDepth;
    }


    @Override
    public void openNextLevel(final int pageid) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void OnClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void SetListner(CustomTOCEnterpriseView.TocItemClickListener listener) {
        tocListner=listener;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        return false;
    }

    @Override
    public void onTableOfTEKSVoTaskCompleted(ArrayList<TableOfTEKSVo> list) {
        if (list != null) {
            progressBar.setVisibility(INVISIBLE);
            FrameLayout frameLayout = (FrameLayout) ((ViewGroup) rootView).getChildAt(0);
            _scrollRoot = (ScrollView)  frameLayout.getChildAt(0);
            _expandableRoot = (LinearLayout) rootView.findViewById(com.hurix.epubreader.R.id.expandListContainer);
            customTypeFace = Typefaces.get(getContext(), getContext().getResources()
                    .getString(com.hurix.epubreader.R.string.text_font_file));
            if (list!=null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    CustomTEKSEnterpriseView.ExpandingViewGroup groupParent = new CustomTEKSEnterpriseView.ExpandingViewGroup(getContext(), 0);
                    groupParent.setData(list.get(i), i);
                    _expandableRoot.addView(groupParent);
                }
            }
        }
    }

    public class ExpandingViewGroup extends LinearLayout implements OnClickListener {

        private boolean _isExpanded = false;
        private boolean _canExpand = false;
        private TableOfTEKSVo _data;
        private LinearLayout _container;
        private LinearLayout _childContainer;
        private int _depth;
        private LayoutTransition mTransitioner = new LayoutTransition();
        private int exp_height = 0;
        private float exp_height_px = 0;

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


        public void setData(TableOfTEKSVo data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            CustomTEKSEnterpriseView.TOCElementViewHolder holder = (CustomTEKSEnterpriseView.TOCElementViewHolder) view.getTag();
            holder.frameNext.setOnClickListener(this);
            View mainTocRootLayout = view.findViewById(com.hurix.epubreader.R.id.mainTocRootLayout);
            mainTocRootLayout.setPadding(_depth * 50, 0, 0, 0);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 1;
            view.setLayoutParams(params);
            _container.addView(view);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == this.getId() || v.getId() == com.hurix.epubreader.R.id.wrapParent) {
                String urlPath = _data.getUrl();

                int height_default = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.frag_height_default);
                int height_exp = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.height_exp);
                int height = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.frag_height);


                    final CustomTEKSEnterpriseView.TOCElementViewHolder holder = returnHolderForView(_container);
                    if (_childContainer.getChildCount() == 0) {
                        holder.textViewTocNext.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
                        holder.selectionIndicator.setBackgroundColor(Color.parseColor(themeUserSettingVo.get_reader_icon_color()));

                        String sharedFact = holder.txtViewTitle.getText().toString();
                        int no_Words = countWordsUsingSplit(sharedFact);

                        if ((Utils.isDeviceTypeMobile(mContext))) {
                            if ((mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {

                                if (no_Words > 12) {
                                    exp_height_px = (no_Words / 5) * holder.txtViewTitle.getLineHeight();
                                    exp_height = height_default + (int) exp_height_px;
                                } else if (no_Words <= 6) {
                                    exp_height = height_default;
                                } else {
                                    exp_height = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.frag_height);

                                }

                            } else {

                                if (no_Words > 20) {
                                    exp_height_px = (no_Words / 8) * holder.txtViewTitle.getLineHeight();
                                    exp_height = height_default + (int) exp_height_px;
                                } else if (no_Words <= 10) {
                                    exp_height = height_default;
                                } else {
                                    exp_height = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.frag_height);

                                }

                            }

                        } else {
                            if (no_Words > 10) {
                                exp_height_px = (no_Words /7) * holder.txtViewTitle.getLineHeight();
                                exp_height = height_exp + (int) exp_height_px;
                            } else if (no_Words <= 6) {
                                exp_height = height_default;
                            } else {
                                exp_height = (int) mContext.getResources().getDimension(com.hurix.epubreader.R.dimen.frag_height);

                            }

                        }
                        if (_data.getChildren().size() > 0) {
                            if (!_data.getChildren().get(0).getType().equalsIgnoreCase("web links")) {
                                RelativeLayout mainTocRootLayout = (RelativeLayout) findViewById(com.hurix.epubreader.R.id.mainTocRootLayout);
                                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, exp_height);
                                mainTocRootLayout.setLayoutParams(params);
                                holder.txtViewTitle.setEllipsize(TextUtils.TruncateAt.END);
                                holder.txtViewTitle.setSingleLine(false);
                                isEllipsized = true;

                            }
                        }else {
                            if (!isChildExpanded) {
                                isChildExpanded = true;
                                holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
                                holder.selectionIndicator.setBackgroundColor(Color.TRANSPARENT);
                                holder.txtViewTitle.setSingleLine(true);
                                holder.txtViewTitle.setEllipsize(TextUtils.TruncateAt.END);
                                RelativeLayout mainTocRootLayout = (RelativeLayout) findViewById(com.hurix.epubreader.R.id.mainTocRootLayout);
                                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, height_default);
                                mainTocRootLayout.setLayoutParams(params);
                            } else {
                                isChildExpanded = false;
                                RelativeLayout mainTocRootLayout = (RelativeLayout) findViewById(com.hurix.epubreader.R.id.mainTocRootLayout);
                                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, exp_height);
                                mainTocRootLayout.setLayoutParams(params);
                                holder.txtViewTitle.setEllipsize(TextUtils.TruncateAt.END);
                                holder.txtViewTitle.setSingleLine(false);

                            }
                        }


                        if (_data.getChildren().size() > 0) {

                            for (int i = 0; i < _data.getChildren().size(); i++) {
                                final CustomTEKSEnterpriseView.ExpandingViewGroup groupParent = new CustomTEKSEnterpriseView.ExpandingViewGroup(getContext(), _depth + 1);
                                groupParent.setData(_data.getChildren().get(i), i);
                                if (!_data.getChildren().get(i).getType().equalsIgnoreCase("toc") && !_data.getChildren().get(0).getType().equalsIgnoreCase("web links")) {
                                    _childContainer.addView(groupParent);
                                }
                            }

                            if (_data.getChildren().get(0).getResourceTitle().equalsIgnoreCase("toc")) {
                                if (_data.getChildren().get(0).getUrl() != null) {
                                    if(tocListner!=null)
                                    tocListner.onTocitemClick(_data.getChildren().get(0).getUrl(),"","", Utils.isDeviceTypeMobile(mContext),0 );
                                    ArrayList<String> tags = new ArrayList<String>();
                                    tags.add("toc");
                                    Utils.removeFragements(getContext(), false, tags);

                                }
                            } else {
                                if (_data.getChildren().get(0).getUrl() != null) {
                                    if (Utils.isOnline(mContext)) {
                                        String newPath;
                                        newPath = Constants.GOOGLE_DOCS + _data.getChildren().get(0).getUrl();
                                        Intent intent = new Intent(mContext, TocStandardWebview.class);
                                        Bundle b = new Bundle();
                                        b.putBoolean("isOriantationLocked", false);
                                        b.putString("path", Utils.unescapeString(newPath));
                                        b.putString("streamType", "");
                                        intent.putExtras(b);
                                        mContext.startActivity(intent);
                                    }else {
                                        Toast.makeText(mContext, mContext.getResources().getString(com.hurix.epubreader.R.string.no_internet_try_again), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    } else {
                        _childContainer.removeAllViews();
                        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
                        holder.selectionIndicator.setBackgroundColor(Color.TRANSPARENT);

                        holder.txtViewTitle.setSingleLine(true);
                        holder.txtViewTitle.setEllipsize(TextUtils.TruncateAt.END);

                        RelativeLayout mainTocRootLayout = (RelativeLayout) findViewById(com.hurix.epubreader.R.id.mainTocRootLayout);
                        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, height_default);
                        mainTocRootLayout.setLayoutParams(params);
                    }


                if (urlPath != null && !urlPath.isEmpty()) {
                    if (Utils.isOnline(mContext)) {
                        if (urlPath.contains("http")) {
                            urlPath = Constants.GOOGLE_DOCS + urlPath;
                            Intent intent = new Intent(mContext, TocStandardWebview.class);
                            Bundle b = new Bundle();
                            b.putBoolean("isOriantationLocked", false);
                            b.putString("path", Utils.unescapeString(urlPath));
                            b.putString("streamType", "");
                            intent.putExtras(b);
                            mContext.startActivity(intent);
                        } else {
                            // GlobalDataManager.getInstance().getLocalBookData().setCurrentPageByDisplayNum(urlPath, true);
                            ArrayList<String> tags = new ArrayList<String>();
                            tags.add("toc");
                            Utils.removeFragements(getContext(), false, tags);
                        }
                    }else {
                        Toast.makeText(mContext, mContext.getResources().getString(com.hurix.epubreader.R.string.no_internet_try_again), Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }

        public  int countWordsUsingSplit(String input) {
            if (input == null || input.isEmpty()) {
                return 0;
            }

        String[] words = input.split("\\s+");
        return words.length;

        }
    }

    class TOCElementViewHolder {
        TextView txtViewUnitNum;
        TextView txtViewTitle;
        RelativeLayout frameNext;
        TextView textViewTocNext;
        TextView textActivity;
        //TextView txtViewPageNum;
        View selectionIndicator;
        View divider;
        // View arrowDivider;
    }

    public View getView(TableOfTEKSVo currTocVO, final int position, View convertView) {
        CustomTEKSEnterpriseView.TOCElementViewHolder holder = null;
        if (currTocVO != null) {
            if (convertView != null && convertView.getTag() == null) {
                convertView = null;
            }
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(com.hurix.epubreader.R.layout.teksenterpriselistitem, null);
                holder = returnHolderForView(convertView);

                convertView.setTag(holder);
            } else {
                holder = (CustomTEKSEnterpriseView.TOCElementViewHolder) convertView.getTag();
            }

            if (currTocVO.getParentID() == 0)        //UI for Parent at 0 index i.e. left most item in TOC
            {
                String displayNum = null;
                if (currTocVO.getChildren().size() > 0) {

                    holder.txtViewUnitNum.setVisibility(View.GONE);
                    holder.txtViewUnitNum.setText("" + (position + 1));
                    /*holder.txtViewPageNum.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                            .getUserSettings().getmReaderDefaultPanelHighlightData()));*/
                    holder.txtViewTitle.setTextColor(Color.parseColor(themeUserSettingVo.getSideMenuHeaderTitleColor()));

                    /*if (getResources().getBoolean(R.bool.show_toc_folio_id))
                        holder.txtViewPageNum.setText(getContext().getResources().getString(R.string.reader_page) + displayNum);
                    else
                        holder.txtViewPageNum.setVisibility(GONE);*/
                } else {
                    /*displayNum = GlobalDataManager.getInstance().getLocalBookData()
                            .getCurrPageDisplayNumByPageID(currTocVO.getPageid());*/
                    /*if (getResources().getBoolean(R.bool.show_toc_folio_id))
                        holder.txtViewPageNum.setText(getContext().getResources().getString(R.string.reader_page) + displayNum);
                    else
                        holder.txtViewPageNum.setVisibility(GONE);*/

                }

            } else    //UI for Children
            {
                /*holder.txtViewPageNum.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                        .getUserSettings().getmReaderDefaultPanelHighlightData()));*/
                holder.txtViewTitle.setTextColor(Color.parseColor( themeUserSettingVo.getDefaultChapterNameColor()));
                holder.txtViewUnitNum.setVisibility(View.GONE);

                /*if (getResources().getBoolean(R.bool.show_toc_folio_id))
                    holder.txtViewPageNum.setText(getContext().getResources().getString(R.string.ugc_mydata_page_label) + GlobalDataManager.getInstance()
                        .getLocalBookData().getCurrPageDisplayNumByPageID(currTocVO.getPageid()));
                else
                    holder.txtViewPageNum.setVisibility(GONE);*/
            }
            if (!currTocVO.getResourceTitle().trim().equalsIgnoreCase("toc") && !currTocVO.getResourceTitle().trim().equalsIgnoreCase("web links")){
                if (currTocVO.getChildren().size() > 0 && currTocVO.getChildren().get(0).getUrl()!= null){
                    if (currTocVO.getChildren().get(0).getType().equalsIgnoreCase("toc")){
                        holder.txtViewTitle.setText("Narrative: "+currTocVO.getResourceTitle().trim() );
                        holder.txtViewTitle.setSingleLine(false);
                        convertView.setBackgroundColor(Color.WHITE);
                        holder.textViewTocNext.setVisibility(GONE);
                        holder.txtViewTitle.setTextColor(Color.parseColor(themeUserSettingVo.getDefaultChapterNameColor()));
                    }else{

                        String webstr=currTocVO.getResourceTitle().trim() + currTocVO.getChildren().get(0).getUrl();
                        holder.textActivity.setText("Activity: ");
                        holder.textActivity.setVisibility(VISIBLE);
                        holder.textActivity.setTextColor(Color.BLACK);
                        holder.txtViewTitle.setText(webstr);
                        holder.txtViewTitle.setPaintFlags( holder.txtViewTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        holder.txtViewTitle.setTextColor(Color.parseColor("#0000EE"));
                        holder.txtViewTitle.setLinksClickable(true);
                        convertView.setBackgroundColor(Color.WHITE);
                        holder.textViewTocNext.setVisibility(GONE);
                        //holder.txtViewTitle.setMovementMethod(LinkMovementMethod.getInstance());
                        // Linkify.addLinks(  holder.txtViewTitle, Linkify.WEB_URLS);
                    }
                }
                else{
                    holder.txtViewTitle.setText(currTocVO.getResourceTitle().trim() );
                    holder.txtViewTitle.setTextColor(Color.parseColor( themeUserSettingVo.getDefaultChapterNameColor()));
                    convertView.setBackgroundColor(Color.WHITE);
                }

            }

        } else {
            convertView = new TextView(getContext());
            convertView.setTag(null);
        }

        if (currTocVO.getChildren().size() == 0) {
            holder.frameNext.setVisibility(View.INVISIBLE);
        }
        if (currTocVO.getChildren().size() != 0 || isEllipsized) {
            holder.frameNext.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private CustomTEKSEnterpriseView.TOCElementViewHolder returnHolderForView(View convertView) {
        CustomTEKSEnterpriseView.TOCElementViewHolder holder;
        holder = new CustomTEKSEnterpriseView.TOCElementViewHolder();
        holder.txtViewUnitNum = (TextView) convertView.findViewById(com.hurix.epubreader.R.id.txtViewUnitNum);
        holder.txtViewTitle = (TextView) convertView.findViewById(com.hurix.epubreader.R.id.txtViewTitle);
        holder.textActivity = (TextView) convertView.findViewById(com.hurix.epubreader.R.id.activity_text);
        holder.textViewTocNext = (TextView) convertView.findViewById(com.hurix.epubreader.R.id.txtnexttoc);
        //holder.txtViewPageNum = (TextView) convertView.findViewById(R.id.txtViewPageNum);
        holder.frameNext = (RelativeLayout) convertView.findViewById(com.hurix.epubreader.R.id.wrapParent);
        holder.selectionIndicator = convertView.findViewById(com.hurix.epubreader.R.id.selectedview);
        holder.divider = convertView.findViewById(com.hurix.epubreader.R.id.divider);
        //holder.arrowDivider = (View)convertView.findViewById(R.id.arrowDivider);

        holder.txtViewUnitNum.setTextColor(Color.parseColor(themeUserSettingVo.getDefaultChapterNameColor()));
        holder.divider.setBackgroundColor(getResources().getColor(com.hurix.epubreader.R.color.toc_divider_color));
        //holder.txtViewPageNum.setVisibility(View.VISIBLE);
        /*holder.txtViewPageNum.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().getmReaderDefaultPanelHighlightData()));*/
        holder.textActivity.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(DEFAULT_PANEL_PAGE_FONT_SIZE));
        holder.txtViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(DEFAULT_PANEL_PAGE_FONT_SIZE));
        holder.txtViewUnitNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(DEFAULT_PANEL_PAGE_FONT_SIZE));
        //holder.txtViewPageNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelPageFontSize()));
        if (!themeUserSettingVo.getLoginHeaderFontFace().isEmpty()) {
            holder.txtViewTitle.setTypeface(customTypeFace);
            holder.txtViewUnitNum.setTypeface(customTypeFace);
            //holder.txtViewPageNum.setTypeface(customTypeFace);
        }
        // holder.arrowDivider.setBackgroundColor(Color.parseColor(UserController
        // .getInstance(getContext()).getUserSettings().getReaderFont()));
        holder.textViewTocNext.setTypeface(kitabooTypeFace);
        holder.textViewTocNext.setAllCaps(false);
        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        holder.textViewTocNext.setTextColor(Color.parseColor( themeUserSettingVo.getDefaultChapterNameColor()));
        return holder;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}


