package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hurix.commons.Constants.Constants;
import com.hurix.commons.utils.Utils;
import com.hurix.customui.actionbar.KitabooActionItemView;
import com.hurix.customui.actionbar.KitabooActionbarBuilder;
import com.hurix.customui.actionbar.OverflowItem;
import com.hurix.customui.iconify.Typefaces;
import com.hurix.customui.interfaces.IBuildUI;
import com.hurix.customui.interfaces.IDestroyer;
import com.hurix.customui.interfaces.ITheme;
import com.hurix.kitaboo.constants.ShelfUIConstants;
import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.helpscreen.ActionbarUtils;
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters.CustomListPopupWindowAdapter;
import com.hurix.kitaboocloud.sdkreadertheme.themePojo.ReaderThemeSettingVo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CustomKitabooActionbar extends FrameLayout implements ITheme, IBuildUI, IDestroyer {
    private KitabooActionbarBuilder.KitabooActionBarMenuClick eventClick;
    private View mCustomView;
    private LinearLayout ll_menu_icon_holder;
    private Typeface mTypeface;
    private LinkedHashMap<View, Integer> listItemView = new LinkedHashMap<>();
    private Context mContext;
    private ListPopupWindow popupWindow;
    private TextView viewMore;
    private final int numberOfMoreItems = 0;
    private ReaderThemeSettingVo mReaderThemeSettingVo;

    public CustomKitabooActionbar(Context context, KitabooActionbarBuilder.KitabooActionBarMenuClick event, Typeface type) {
        super(context);
        this.eventClick = event;
        this.mTypeface = type;

        buildUI();
        loadFont(context);
        init(context);
        mContext = context;
        listItemView = new LinkedHashMap<>();
    }

    private void loadFont(Context context) {
        AssetManager am = context.getApplicationContext().getAssets();
        // mTypeface = Typeface.createFromAsset(am, "kitabooread.ttf");

        String fontfile = Utils.getFontFilePath();
        if (fontfile != null && !fontfile.isEmpty()) {
            mTypeface = Typefaces.get(context, fontfile);
        } else {
            mTypeface = Typefaces.get(context, "kitabooread.ttf");
        }

    }

    public Typeface defaultActionbarTypeface(Context context) {
       /* if(null == mTypeface){
            loadFont(context);
        }*/

        loadFont(context);
        return mTypeface;
    }


    private ImageView getTextView() {
        ImageView textView = new ImageView(getContext());
        textView.setLayoutParams(new LayoutParams(20, 20));
        textView.setImageDrawable(getResources().getDrawable(R.drawable.loader0009));
        return textView;
    }

    public CustomKitabooActionbar addActionItem(View item, Integer gravity) {
        ll_menu_icon_holder = mCustomView.findViewById(R.id.ll_menu_icon_holder);
        ll_menu_icon_holder.removeAllViews();
        this.listItemView.put(item, gravity);
        return this;
    }

    public View getItem(int itemId) {
        if (listItemView != null && listItemView.keySet() != null) {
            for (View itemview : listItemView.keySet()) {
                if (itemview.getId() == itemId) {
                    return itemview;
                }
            }
        }
        return null;
    }

    public CustomKitabooActionbar addEventCallback(KitabooActionbarBuilder.KitabooActionBarMenuClick callback) {
        this.eventClick = callback;
        return this;
    }

    @SuppressLint("ResourceType")
    public CustomKitabooActionbar build() {
//        if (!((PlayerActivity) getContext()).isFinishing()) {
        LinearLayout.LayoutParams params;
        int deviceWidth = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;  // In case of portrait mode

        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.right_view_id), 0);
        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.left_view_id), 0);
        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.center_view_id), 0);

        viewMore.setVisibility(GONE);
        viewMore.setTypeface(mTypeface);
        viewMore.setText(ShelfUIConstants.ACTIONBAR_OVERFLOW);
        popupWindow = new ListPopupWindow(getContext());
        final List<OverflowItem> overFlowItemList = new ArrayList<>();
        final CustomListPopupWindowAdapter overFlowListAdapter = new CustomListPopupWindowAdapter(getContext(), overFlowItemList);
        popupWindow.setAdapter(overFlowListAdapter);
        popupWindow.setAnchorView(viewMore);
        //popupWindow.setPromptPosition(POSITION_PROMPT_BELOW);
        popupWindow.setWidth((int) this.getResources().getDimension(R.dimen.overflow_popup_width));
        //popupWindow.setHeight(400);
        popupWindow.setModal(true);

        if (mReaderThemeSettingVo != null) {
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMain().getToolbar().getTop().getBackground())));
            ((TextView) mCustomView.findViewById(R.id.btn_overflow)).setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMain().getToolbar().getTop().getIconsColor()));
        }
        // Getting a Set of Key-value pairs
        Set entrySet = listItemView.entrySet();

        // Obtaining an iterator for the entry set
        Iterator it = entrySet.iterator();
        int deviceCapacity = deviceWidth / 70;
//            int deviceCapacity =5;
        Log.d("action:entrySet.size():", entrySet.size() + "");
        Log.d("action:deviceCapacity:", deviceCapacity + "");
        if ((deviceWidth % 70) == 0 || deviceCapacity > entrySet.size()) {
            if (entrySet.size() < deviceCapacity) {
                ll_menu_icon_holder.setWeightSum(entrySet.size());
            } else {
                ll_menu_icon_holder.setWeightSum(deviceCapacity);
            }
            LinearLayout.LayoutParams holderParam =
                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            holderParam.weight = 1;
            ll_menu_icon_holder.setLayoutParams(holderParam);
            viewMore.setVisibility(GONE);
        } else {
            ll_menu_icon_holder.setWeightSum(deviceCapacity - 1);
            viewMore.setVisibility(VISIBLE);
        }
        // Iterate through HashMap entries(Key-Value pairs)
        int i = 0;
        int viewWidth = 0;
        while (it.hasNext()) {
            i++;
            Map.Entry map = (Map.Entry) it.next();

            final View itemView = (View) map.getKey();
            viewWidth = itemView.getMinimumWidth() + viewWidth;

            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (itemView.getId() == R.id.action_home) {
//                    params.weight = 1;
                params.rightMargin = 36;
                params.gravity = Gravity.LEFT;
            } else if (itemView.getId() == R.id.action_profile_image) {
//                    params.weight = 1;
                params.height = 70;
                params.width = 70;
                params.gravity = Gravity.RIGHT;
                params.rightMargin = 36;
            } else if (itemView.getId() == R.id.action_print_page) {
                params.weight = (float) 0.5;
            } else if (itemView.getId() == R.id.read_aloud) {
                params.weight = 1;
            } else if (itemView.getId() == R.id.action_chapter_title) {
                params.weight = entrySet.size();
                params.gravity = Gravity.CENTER_HORIZONTAL;
            } else {
                params.weight = 1;
            }

            if (deviceCapacity < i) {
                addItemToOverflowView(itemView, overFlowItemList, popupWindow, overFlowListAdapter);
            } else {
                itemView.setLayoutParams(params);
                try {
                    ll_menu_icon_holder.addView(itemView);
                    ActionbarUtils.INSTANCE.setBottomActionBarItems(i);
                    ActionbarUtils.INSTANCE.setPageTwoCount((i / 2)+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ((View) map.getKey()).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eventClick != null) {
                        eventClick.onMenuItemClick(view);
                    }
                }
            });
        }
//        }
        return this;
    }

    private void addItemToOverflowView(View itemView, List<OverflowItem> overFlowItemList, final ListPopupWindow popupWindow, CustomListPopupWindowAdapter overFlowListAdapter) {
        viewMore.setVisibility(VISIBLE);

        if (itemView instanceof KitabooActionItemView) {
            KitabooActionItemView menuView = ((KitabooActionItemView) itemView);
            if (mReaderThemeSettingVo != null)
                menuView.setTextColor(Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().getMain().getToolbar().getTop().getIconsColor()));
            ((TextView) viewMore.findViewById(R.id.btn_overflow)).setTextColor(menuView.getCurrentTextColor());
            OverflowItem menuItem = new OverflowItem(menuView.getUniqueName(), menuView.getText().toString(), menuView.getCurrentTextColor(), 20, itemView.getId());
            overFlowItemList.add(menuItem);
        }

        overFlowListAdapter.notifyDataSetChanged();
    }

    public CustomKitabooActionbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

       /* TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.KitabooActionbar,
                0, 0);*/

        try {
            loadFont(context);
            init(context);
        } finally {
            // a.recycle();
        }
    }

    private void init(Context context) {
        mCustomView = inflate(context, R.layout.custom_kitaboo_actionbar, this);
//        mCustomView = inflate(context, R.layout.layout_kitaboo_bottom_bar, this);
        viewMore = mCustomView.findViewById(R.id.btn_overflow);
        viewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (eventClick != null) {
                            eventClick.onMenuItemClick(view);
                        }
                        if ((popupWindow != null) && popupWindow.isShowing() && !((Activity) getContext()).isFinishing() && !((Activity) getContext()).isDestroyed()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                popupWindow.show();
            }
        });

    }


    public CustomKitabooActionbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        buildUI();
    }

    @Override
    public void applyTheme() {

    }

    public void setBackColor(int color) {
        if (mCustomView != null) {
            mCustomView.setBackgroundColor(color);
        }
        if (ll_menu_icon_holder != null)
            ll_menu_icon_holder.setBackgroundColor(color);

    }

    @Override
    public void setTheme() {
        applyTheme();
    }

    public void setTheme(ReaderThemeSettingVo theme) {
        mReaderThemeSettingVo = theme;
    }

    @Override
    public void init() {

    }

    @Override
    public void buildUI() {

        this.setBackgroundColor(Color.parseColor("#f1f1f1"));
    }

    @Override
    public void addListners() {

    }

    @Override
    public void clearMemory() {

    }

    public void removeAllActionBarItem() {
        if (listItemView != null) {
            listItemView.clear();
            if (ll_menu_icon_holder != null) {
                ll_menu_icon_holder.removeAllViews();
                //ll_menu_icon_holder.removeAllViews();
            }
        }


    }


    public void closeSearchtextview() {

    }

    public void getBackground(Drawable res) {
    }

    public CustomKitabooActionbar addTopActionItem(View item, int itemGravity) {
        ll_menu_icon_holder = mCustomView.findViewById(R.id.ll_menu_icon_holder);
        ll_menu_icon_holder.removeAllViews();
        this.listItemView.put(item, itemGravity);
        return this;
    }
}