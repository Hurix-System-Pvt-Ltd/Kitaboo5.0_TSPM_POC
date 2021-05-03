package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.hurix.bookreader.utils.Typefaces;
import com.hurix.commons.Constants.Constants;
import com.hurix.commons.Constants.PlayerUIConstants;
import com.hurix.commons.datamodel.IBook;
import com.hurix.commons.notifier.GlobalDataManager;
import com.hurix.commons.sdkDatamodel.SDKManager;
import com.hurix.customui.Standard.TableOfExternalResourcesVo;
import com.hurix.customui.playerTheme.ThemeUserSettingVo;
import com.hurix.customui.toc.tor.OnTORItemClick;

import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.utils.DialogUtils;
import com.hurix.reader.kitaboosdkrenderer.utils.Utils;


import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomExternalResourcesEnterpriseView extends CustomExternalResourcesBaseView implements ExpandableListView.OnChildClickListener {
    private TextView _txtmessage;
    private ArrayList<ArrayList<TableOfExternalResourcesVo>> _rootColl = new ArrayList<ArrayList<TableOfExternalResourcesVo>>();
    private int _totalDepth;
    private Typeface kitabooTypeFace;
    private LinearLayout _expandableRoot;
    private ScrollView _scrollRoot;
    private Context mContext;
    private Typeface customTypeFace;
    private IBook iBook;
    private ThemeUserSettingVo mUserSettingVO;
    private OnTORItemClick mOnTORItemClick;
    //private TextView mNoResourceFound;

    public CustomExternalResourcesEnterpriseView(Context context, IBook _bookVo, ThemeUserSettingVo userSettingVO, OnTORItemClick mOnTORItemClick) {
        super(context, R.layout.custom_external_resources_enterprise, _bookVo, userSettingVO, mOnTORItemClick);
    }

    @Override
    public void initView(View view, IBook _bookVo, ThemeUserSettingVo userSettingVO, OnTORItemClick mOnTORItemClick) {
        iBook = _bookVo;
        mUserSettingVO = userSettingVO;
        this.mOnTORItemClick = mOnTORItemClick;
        kitabooTypeFace = Typefaces.get(getContext(), getContext().getResources()
                .getString(R.string.kitaboo_font_file_name));
        ArrayList<TableOfExternalResourcesVo> parentColl = SDKManager.getInstance().getExternalResourcesCollection();
        _scrollRoot = (ScrollView) ((ViewGroup) view).getChildAt(0);
        _expandableRoot = (LinearLayout) view.findViewById(R.id.expandListContainer);
        /*mNoResourceFound = (TextView) view.findViewById(R.id.noResourceFound);
        mNoResourceFound.setVisibility(GONE);*/
        //mUserSettingVO = UserController.getInstance(getContext()).getUserSettings();
        customTypeFace = Typefaces.get(getContext(), getContext().getResources()
                .getString(R.string.text_font_file));
        if (parentColl.size() != 0) {
            for (int i = 0; i < parentColl.size(); i++) {
               CustomExternalResourcesEnterpriseView.ExpandingViewGroup groupParent = new CustomExternalResourcesEnterpriseView.ExpandingViewGroup(getContext(), 0);
                groupParent.setData(parentColl.get(i), i);
                _expandableRoot.addView(groupParent);
            }
        } else {
            /*mNoResourceFound.setVisibility(VISIBLE);
            mNoResourceFound.setText(getResources().getString(R.string.alert_tor_no_resources_found));*/
        }
    }


    public int getTotalDepth() {
        return _totalDepth;
    }


    @Override
    public void openNextLevel(final int pageid) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //              TOCBaseAdapter adapter = (TOCBaseAdapter) parent.getAdapter();
        //              TableOfExternalResourcesVo vo = (TableOfExternalResourcesVo)adapter.getItem(position);
        //              //OnClick(parent, view, position, id);
        //              int pageID = vo.getPageid();
        //              if(vo.getChildren().size()>0)
        //              {
        //                      pageID = vo.getChildren().get(0).getPageid();
        //              }
        //              GlobalDataManager.getInstance().getLocalBookData().setCurrentPageByPageID(pageID,true);
        //              ((Activity) getContext()).finish();
    }

    public void OnClick(AdapterView<?> parent, View view, int position, long id) {
        //              TOCBaseAdapter adapter = (TOCBaseAdapter) parent.getAdapter();
        //              TableOfExternalResourcesVo vo = (TableOfExternalResourcesVo)adapter.getItem(position);
        //              adapter.setSelectedPos(position);
        //              changeChildsAccordingToDepth(adapter.getDepth());
        //              if(vo.getChildren().size()>0)
        //              {
        //                      createListAndAddData(vo.getChildren());
        //                      adapter.notifyDataSetChanged();
        //              }

    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        return false;
    }

    public class ExpandingViewGroup extends LinearLayout implements OnClickListener {

        private boolean _isExpanded = false;
        private boolean _canExpand = false;
        private TableOfExternalResourcesVo _data;
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


        public void setData(TableOfExternalResourcesVo data, int index) {
            _data = data;
            LinearLayout view = (LinearLayout) getView(data, index, null);
            CustomExternalResourcesEnterpriseView.TOCElementViewHolder holder = (CustomExternalResourcesEnterpriseView.TOCElementViewHolder) view.getTag();
            holder.frameNext.setOnClickListener(this);
            holder.mainTocRootLayout.setOnClickListener(this);
            holder.mainTocRootLayout .setPadding(_depth * 50, 0, 0, 0);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 1;
            view.setLayoutParams(params);
            _container.addView(view);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.mainTocRootLayout && _data.getUrl()!= null ) {
                String urlPath = _data.getUrl();
                if (urlPath != null && !urlPath.isEmpty()) {
                     urlPath = "file://"
                            + Utils.getBookFolderPathCompat(GlobalDataManager
                            .getInstance().getLocalBookData().getBookID()
                            + "", GlobalDataManager.getInstance()
                            .getLocalBookData().getBookISBN())
                            + File.separator + urlPath;
                    //openDocument(urlPath);
                    mOnTORItemClick.onExternalResourceClick(urlPath,_data);
                }

            } else if (v.getId() == R.id.wrapParent || v.getId() == R.id.mainTocRootLayout ) {
                if (_data.getChildren().size() > 0) {
                    CustomExternalResourcesEnterpriseView.TOCElementViewHolder holder = returnHolderForView(_container);
                    if (_childContainer.getChildCount() == 0) {
                        holder.textViewTocNext.setText(PlayerUIConstants.UP_ARROW_IC_TEXT);
                        holder.selectionIndicator.setBackgroundColor(Color.parseColor(mUserSettingVO.get_reader_icon_color()));
                        for (int i = 0; i < _data.getChildren().size(); i++) {
                            final CustomExternalResourcesEnterpriseView.ExpandingViewGroup groupParent = new CustomExternalResourcesEnterpriseView.ExpandingViewGroup(getContext(), _depth + 1);
                            groupParent.setData(_data.getChildren().get(i), i);
                            _childContainer.addView(groupParent);
                        }
                    } else {
                        _childContainer.removeAllViews();
                        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
                        holder.selectionIndicator.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }

        }
    }

    private void openDocument(String urlPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String extension = android.webkit.MimeTypeMap
                .getFileExtensionFromUrl(Uri.parse(urlPath).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            intent.setDataAndType(Uri.parse(urlPath), "text/*");
        } else {
            intent.setDataAndType(Uri.parse(urlPath), mimetype);
        }

        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> list = packageManager
                .queryIntentActivities(intent, 0);

        if (list.size() == 0) {
            DialogUtils.displayToast(mContext, mContext.getResources()
                    .getString(R.string.no_application_available), Toast.LENGTH_LONG, Gravity.CENTER);
            GlobalDataManager.getInstance().setAnyPopupVisible(false);
        } else {
            if(Build.VERSION.SDK_INT>=24){
                try{
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            ((Activity) mContext).startActivityForResult(intent,
                    Constants.BOOKPLAYER_OPEN_PDF_REQUESTCODE);
        }
    }

    class TOCElementViewHolder {
        TextView txtViewUnitNum;
        TextView txtViewTitle;
        RelativeLayout frameNext;
        TextView textViewTocNext;
        //TextView txtViewPageNum;
        View selectionIndicator;
        View divider;
        public RelativeLayout mainTocRootLayout;
        // View arrowDivider;
    }

    public View getView(TableOfExternalResourcesVo currTocVO, final int position, View convertView) {
        CustomExternalResourcesEnterpriseView.TOCElementViewHolder holder = null;
        if (currTocVO != null) {
            if (convertView != null && convertView.getTag() == null) {
                convertView = null;
            }
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_external_resourcesenterpriselistitem, null);
                holder = returnHolderForView(convertView);

                convertView.setTag(holder);
            } else {
                holder = (CustomExternalResourcesEnterpriseView.TOCElementViewHolder) convertView.getTag();
            }

            if (currTocVO.getParentID() == 0)        //UI for Parent at 0 index i.e. left most item in TOC
            {
                String displayNum = null;
                if (currTocVO.getChildren().size() > 0) {
                    try {
                    } catch (NumberFormatException e) {
                        displayNum = "test";
                    }

                    holder.txtViewUnitNum.setVisibility(View.VISIBLE);
                    holder.txtViewUnitNum.setText("" + (position + 1));
                    /*holder.txtViewPageNum.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                            .getUserSettings().getmReaderDefaultPanelHighlightData()));*/
                    holder.txtViewTitle.setTextColor(Color.parseColor(mUserSettingVO.getSideMenuHeaderTitleColor()));
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
                holder.txtViewTitle.setTextColor(Color.parseColor(mUserSettingVO.getDefaultChapterNameColor()));
                holder.txtViewUnitNum.setVisibility(View.INVISIBLE);

                /*if (getResources().getBoolean(R.bool.show_toc_folio_id))
                    holder.txtViewPageNum.setText(getContext().getResources().getString(R.string.ugc_mydata_page_label) + GlobalDataManager.getInstance()
                        .getLocalBookData().getCurrPageDisplayNumByPageID(currTocVO.getPageid()));
                else
                    holder.txtViewPageNum.setVisibility(GONE);*/
            }

            holder.txtViewTitle.setText(currTocVO.getResourceTitle().trim());
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView = new TextView(getContext());
            convertView.setTag(null);
        }

        if (currTocVO.getChildren().size() == 0) {
            holder.frameNext.setVisibility(View.INVISIBLE);
        } else {
            holder.frameNext.setVisibility(View.VISIBLE);
        }

        final View finalView = convertView;

        //              holder.frameNext.setOnClickListener(new OnClickListener()
        //              {
        //
        //                      @Override
        //                      public void onClick(View v)
        //                      {
        //                              _rootView.OnClick(_list, finalView, position, 0);
        //                      }
        //              });
        return convertView;
    }

    private CustomExternalResourcesEnterpriseView.TOCElementViewHolder returnHolderForView(View convertView) {
        CustomExternalResourcesEnterpriseView.TOCElementViewHolder holder;
        holder = new CustomExternalResourcesEnterpriseView.TOCElementViewHolder();
        holder.txtViewUnitNum = (TextView) convertView.findViewById(R.id.txtViewUnitNum);
        holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
        holder.textViewTocNext = (TextView) convertView.findViewById(R.id.txtnexttoc);
        //holder.txtViewPageNum = (TextView) convertView.findViewById(R.id.txtViewPageNum);
        holder.frameNext = (RelativeLayout) convertView.findViewById(R.id.wrapParent);
        holder.mainTocRootLayout = (RelativeLayout) convertView.findViewById(R.id.mainTocRootLayout);
        holder.selectionIndicator = convertView.findViewById(R.id.selectedview);
        holder.divider = convertView.findViewById(R.id.divider);
        //holder.arrowDivider = (View)convertView.findViewById(R.id.arrowDivider);

        holder.txtViewUnitNum.setTextColor(Color.parseColor(mUserSettingVO.getDefaultChapterNameColor()));
        holder.txtViewTitle.setTextColor(Color.parseColor(mUserSettingVO.getDefaultChapterNameColor()));
        holder.divider.setBackgroundColor(getResources().getColor(R.color.toc_divider_color));
        /*holder.txtViewPageNum.setVisibility(View.VISIBLE);
        holder.txtViewPageNum.setTextColor(Color.parseColor(UserController.getInstance(getContext())
                .getUserSettings().getmReaderDefaultPanelHighlightData()));*/
        holder.txtViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelChapterFontSize()));
        holder.txtViewUnitNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelPageFontSize()));
        //holder.txtViewPageNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(mUserSettingVO.getDefaultPanelPageFontSize()));
        if (!mUserSettingVO.getLoginHeaderFontFace().isEmpty()) {
            holder.txtViewTitle.setTypeface(customTypeFace);
            holder.txtViewUnitNum.setTypeface(customTypeFace);
            //holder.txtViewPageNum.setTypeface(customTypeFace);
        }
        // holder.arrowDivider.setBackgroundColor(Color.parseColor(UserController
        // .getInstance(getContext()).getUserSettings().getReaderFont()));
        holder.textViewTocNext.setTypeface(kitabooTypeFace);
        holder.textViewTocNext.setAllCaps(false);
        holder.textViewTocNext.setText(PlayerUIConstants.DOWN_ARROW_IC_TEXT);
        holder.textViewTocNext.setTextColor(Color.parseColor(mUserSettingVO.getDefaultChapterNameColor()));
        return holder;
    }
}


