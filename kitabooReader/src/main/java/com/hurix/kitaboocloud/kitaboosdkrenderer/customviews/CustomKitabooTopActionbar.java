package com.hurix.kitaboocloud.kitaboosdkrenderer.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
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
import com.hurix.kitaboocloud.kitaboosdkrenderer.customviews.adapters.CustomListPopupWindowAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CustomKitabooTopActionbar extends FrameLayout implements ITheme, IBuildUI, IDestroyer {
    private KitabooActionbarBuilder.KitabooActionBarMenuClick eventClick;
    private View mCustomView;
    private RelativeLayout ll_menu_icon_holder;
    private Typeface mTypeface;
    private LinkedHashMap<View, Integer> listItemView = new LinkedHashMap<>();
    private Context mContext;
    private ListPopupWindow popupWindow;

    public CustomKitabooTopActionbar(Context context, KitabooActionbarBuilder.KitabooActionBarMenuClick event, Typeface type) {
        super(context);
        this.eventClick = event;
        this.mTypeface = type;

        buildUI();
        loadFont(context);
        init(context);
        mContext = context;
        listItemView = new LinkedHashMap<>();
    }

    public View getActionBarView()
    {
        return mCustomView;
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

    public CustomKitabooTopActionbar addActionItem(View item, Integer gravity) {
        ll_menu_icon_holder = mCustomView.findViewById(R.id.rl_menu_icon_holder);
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

    public CustomKitabooTopActionbar addEventCallback(KitabooActionbarBuilder.KitabooActionBarMenuClick callback) {
        this.eventClick = callback;
        return this;
    }

    @SuppressLint("ResourceType")
    public CustomKitabooTopActionbar build() {

        RelativeLayout.LayoutParams params;
        int deviceWidth = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;  // In case of portrait mode

        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.right_view_id), 0);
        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.left_view_id), 0);
        Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.center_view_id), 0);

        mCustomView.findViewById(R.id.btn_overflow).setVisibility(GONE);
        ((TextView)mCustomView.findViewById(R.id.btn_overflow)).setTypeface(mTypeface);
        ((TextView)mCustomView.findViewById(R.id.btn_overflow)).setText(ShelfUIConstants.ACTIONBAR_OVERFLOW);
        popupWindow = new ListPopupWindow(getContext());
        final List<OverflowItem> overFlowItemList = new ArrayList<>();
        final CustomListPopupWindowAdapter overFlowListAdapter = new CustomListPopupWindowAdapter(getContext(), overFlowItemList);
        popupWindow.setAdapter(overFlowListAdapter);
        popupWindow.setAnchorView(mCustomView.findViewById(R.id.btn_overflow));
        //popupWindow.setPromptPosition(POSITION_PROMPT_BELOW);
        popupWindow.setWidth((int) this.getResources().getDimension(R.dimen.overflow_popup_width));
        //popupWindow.setHeight(400);
        popupWindow.setModal(true);

        // Getting a Set of Key-value pairs
        Set entrySet = listItemView.entrySet();

        // Obtaining an iterator for the entry set
        Iterator it = entrySet.iterator();

        // Iterate through HashMap entries(Key-Value pairs)

        while (it.hasNext()) {
            Map.Entry map = (Map.Entry) it.next();

            final View itemView = (View) map.getKey();
            int viewWidth = itemView.getMinimumWidth();

            if (0 != viewWidth) {
                if(itemView.getId() == R.id.teacher_review_green || itemView.getId() == R.id.teacher_review_red)
                {
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(itemView.getMinimumWidth(), itemView.getMinimumWidth()));
                    }
                    else {
                        params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(70, 70));
                    }
                }
                else
                {
                    params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(itemView.getMinimumWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            } else {
                params = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            setActionBarItemsAsPerGravityToRelativeLayout(map, params);

           if(itemView.getId() == R.id.action_profile_image)
            {
                params.rightMargin = 20;
                params.topMargin = 16;
            }

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || deviceWidth > viewWidth) {
                itemView.setLayoutParams(params);
                try {
                    ll_menu_icon_holder.addView(itemView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                deviceWidth = deviceWidth - viewWidth;
            } else {
                addItemToOverflowView(itemView, overFlowItemList, popupWindow, overFlowListAdapter);
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
        return this;
    }

    /*@Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((popupWindow != null) *//*&& popupWindow.isShowing()*//* && !((Activity) getContext()).isFinishing() && !((Activity) getContext()).isDestroyed()) {
            popupWindow.dismiss();
            //mCustomView.findViewById(R.id.btn_overflow).setVisibility(GONE);
        }
    }*/

    private void setActionBarItemsAsPerGravityToRelativeLayout(Map.Entry map, RelativeLayout.LayoutParams params) {

        final View itemView = (View) map.getKey();
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        if (((int) map.getValue()) == Gravity.RIGHT) {

            int rightViewId = Utils.getSharedPreferenceIntValue(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.right_view_id), 0);

            if (rightViewId == 0) //First Right menu
            {
                ((View) map.getKey()).setPadding(0, 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            } else {
                params.addRule(RelativeLayout.LEFT_OF, rightViewId);
            }

            Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.right_view_id), itemView.getId());

        } else if (((int) map.getValue()) == Gravity.LEFT) {

            int leftViewId = Utils.getSharedPreferenceIntValue(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.left_view_id), 0);

            if (leftViewId == 0) //First Left menu
            {
                ((View) map.getKey()).setPadding(0, 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                if (getItem(itemView.getId()) instanceof KitabooActionItemView) {
                    params.leftMargin = ((KitabooActionItemView) getItem(itemView.getId())).getLeftMargin();
                }

                if (getItem(itemView.getId()) instanceof CustomCompoundView) {
                    params.leftMargin = ((CustomCompoundView) getItem(itemView.getId())).getLeftMargin();
                }


            } else {
                params.addRule(RelativeLayout.RIGHT_OF, leftViewId);
                if (getItem(itemView.getId()) instanceof KitabooActionItemView) {
                    params.leftMargin = ((KitabooActionItemView) getItem(itemView.getId())).getLeftMargin();
                }

            }

            Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.left_view_id), itemView.getId());
        } else if (((int) map.getValue()) == Gravity.CENTER_HORIZONTAL) {

            int centerViewId = Utils.getSharedPreferenceIntValue(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.center_view_id), 0);

            if (centerViewId == 0) //First Center menu
            {
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            } else {
                params.addRule(RelativeLayout.RIGHT_OF, centerViewId);
            }
            //ll_menu_icon_holder.setGravity(Gravity.CENTER_HORIZONTAL);
            Utils.insertSharedPrefernceIntValues(getContext(), Constants.SHELF_PREFS_NAME, getResources().getString(R.string.center_view_id), itemView.getId());
        }
    }

    private void addItemToOverflowView(View itemView, List<OverflowItem> overFlowItemList, final ListPopupWindow popupWindow, CustomListPopupWindowAdapter overFlowListAdapter) {
        mCustomView.findViewById(R.id.btn_overflow).setVisibility(VISIBLE);

        if (itemView instanceof KitabooActionItemView) {
            KitabooActionItemView menuView = ((KitabooActionItemView) itemView);
            ((TextView)mCustomView.findViewById(R.id.btn_overflow)).setTextColor(menuView.getCurrentTextColor());
            OverflowItem menuItem = new OverflowItem(menuView.getUniqueName(), menuView.getText().toString(), menuView.getCurrentTextColor(), 20, itemView.getId());
            overFlowItemList.add(menuItem);
        }

        mCustomView.findViewById(R.id.btn_overflow).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

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
        overFlowListAdapter.notifyDataSetChanged();
    }

    public CustomKitabooTopActionbar(Context context, @Nullable AttributeSet attrs) {
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
        mCustomView = inflate(context, R.layout.custom_kitaboo_top_actionbar, this);
    }


    public CustomKitabooTopActionbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
}