package com.hurix.reader.kitaboosdkrenderer.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hurix.customui.iconify.IconDrawable;
import com.hurix.customui.iconify.Iconify;
import com.hurix.customui.iconify.Utils;


import com.hurix.reader.kitaboosdkrenderer.R;
import com.hurix.reader.kitaboosdkrenderer.sdkreadertheme.themePojo.ReaderThemeSettingVo;
import com.hurix.reader.kitaboosdkrenderer.views.CustomPlayerUIConstants;


import java.util.ArrayList;
import java.util.List;

//import com.kitaboo.sdk.constants.PlayerUIConstants;


public class CustomColorPickerDialogDash extends PopupWindow {

    private ColorGridAdapter mAdapter;
    private GridView mColorGrid;
    private int[] mColorChoices = {};
    private int mValue = 0;
    private int mItemLayoutId = R.layout.custom_dash_grid_item_color;
    private int mNumColumns = 5;
    private RelativeLayout _gridLayout;
    private LinearLayout mColorContainer;
    private static ReaderThemeSettingVo mReaderThemeSettingVo;

    //---------------------------------------------------------------------------------------------------
    //Added
    //---------------------------------------------------------------------------------------------------
    protected int mSelectedColor;
    protected int mTitleResId = R.string.color_picker_default_title;
    protected Context mContext;
    protected OnColorSelectedListener mListener;

    //Bundle
    protected static final String KEY_TITLE_ID = "title_id";
    protected static final String KEY_COLORS = "colors";
    protected static final String KEY_SELECTED_COLOR = "selected_color";
    protected static final String KEY_COLUMNS = "columns";


    public CustomColorPickerDialogDash(Context context) {
        super(context);
        mContext = context;
    }

    public static CustomColorPickerDialogDash newInstance(Context context) {
        return new CustomColorPickerDialogDash(context);
    }

    //---------------------------------------------------------------------------------------------------
    // Added
    //---------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param titleResId    title resource id
     * @param colors        array of colors
     * @param selectedColor selected color
     * @param columns       number of columns
     * @param themeUserSettingVo
     * @return new ColorPickerDialog
     */
    public static CustomColorPickerDialogDash newInstance(Context context, int titleResId, int[] colors,
                                                          int selectedColor, int columns, ReaderThemeSettingVo themeUserSettingVo) {
        CustomColorPickerDialogDash colorPicker = CustomColorPickerDialogDash.newInstance(context);
        colorPicker.initialize(titleResId, colors, selectedColor, columns, themeUserSettingVo);
        mReaderThemeSettingVo = themeUserSettingVo;
        return colorPicker;
    }

	/*public void setArguments(int titleResId, int columns) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TITLE_ID, titleResId);
        bundle.putInt(KEY_COLUMNS, columns);
        setArguments(bundle);
    }*/

    /**
     * Interface for a callback when a color square is selected.
     */
    public interface OnColorSelectedListener {

        /**
         * Called when a specific color square has been selected.
         */
        void onColorSelected(int color);

        void onDismiss();
    }


    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Initialize the dialog picker
     *  @param titleResId    title resource id
     * @param colors        array of colors
     * @param selectedColor selected color
     * @param columns       number of columns
     * @param themeUserSettingVo
     */
    public void initialize(int titleResId, int[] colors, int selectedColor, int columns, ReaderThemeSettingVo themeUserSettingVo) {
        mColorChoices = colors;
        mNumColumns = columns;
        mSelectedColor = selectedColor;
        if (titleResId > 0)
            mTitleResId = titleResId;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dash_dialog_colors, null);
        setBackgroundDrawable(new BitmapDrawable());

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth((int) mContext.getResources().getDimension(R.dimen.color_popup_width));
        setFocusable(true);
        setContentView(view);
        mColorContainer = (LinearLayout) view.findViewById(R.id.color_grid_container);
        mColorGrid = (GridView) view.findViewById(R.id.color_grid);
        mColorContainer.setBackgroundDrawable(Utils.getRectAngleDrawable(Color.parseColor(themeUserSettingVo.
                        getReader().getDayMode().getPentool().getPen().getPopupBackground()), new float[]{10, 10, 10, 10, 10, 10, 10, 10}
                , 2, Color.parseColor(themeUserSettingVo.getReader().getDayMode().getPentool().getPen().getPopupBorder())));
        /*mColorGrid.setNumColumns(DBController.getInstance(mContext).getManager().getUserSettings
				(UserController.getInstance(mContext).getUserVO().getUserID()).getPenColors().length());*/
        mColorGrid.setNumColumns(mNumColumns);
        if (mAdapter == null) {
            mAdapter = new ColorGridAdapter();
        }

        if (mAdapter != null && mColorGrid != null) {
            mAdapter.setSelectedColor(mSelectedColor);  //USE this to select color
            mColorGrid.setAdapter(mAdapter);
        }

        mColorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long itemId) {
                //Added
                //Toast.makeText(getActivity(), "Selected color="+mAdapter.getItem(position), Toast.LENGTH_LONG).show();
                if (mListener != null)
                    mListener.onColorSelected(mAdapter.getItem(position));
                dismiss();
            }
        });
    }

	/*@Override
	public void onCreate(Bundle savedInstanceState) {

	}*/

    private class ColorGridAdapter extends BaseAdapter {
        private List<Integer> mChoices = new ArrayList<Integer>();
        private int mSelectedColor;
        private LinearLayout mParentLayout;

        private ColorGridAdapter() {
            for (int color : mColorChoices) {
                mChoices.add(color);
            }
        }

        @Override
        public int getCount() {
            return mChoices.size();
        }

        @Override
        public Integer getItem(int position) {
            return mChoices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mChoices.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(mItemLayoutId, container, false);
                mParentLayout = (LinearLayout) convertView.findViewById(R.id.layoutParent);
            }

            int color = getItem(position);

            //convertView.setBackgroundColor(color == mSelectedColor? 0x6633b5e5 : 0);
            if (color == mSelectedColor) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    convertView.findViewById(R.id.layoutParent).setBackground(new Drawable() {
                        @Override
                        public void draw(Canvas canvas) {

                        }

                        @Override
                        public void setAlpha(int i) {

                        }

                        @Override
                        public void setColorFilter(ColorFilter colorFilter) {

                        }

                        @Override
                        public int getOpacity() {
                            return 0;
                        }
                    });

                } else {

                    convertView.findViewById(R.id.layoutParent).setBackground(new Drawable() {
                        @Override
                        public void draw(Canvas canvas) {

                        }

                        @Override
                        public void setAlpha(int i) {

                        }

                        @Override
                        public void setColorFilter(ColorFilter colorFilter) {

                        }

                        @Override
                        public int getOpacity() {
                            return 0;
                        }
                    });
                }
            }
            setColorViewValue(convertView.findViewById(R.id.color_view), color, mParentLayout);
            return convertView;
        }

        public void setSelectedColor(int selectedColor) {

            if (mSelectedColor != selectedColor) {
                mSelectedColor = selectedColor;
                notifyDataSetChanged();
            }
        }
    }

    public void setupIconsAccToName(TextView button, String name, int color, LinearLayout parent) {
        // IconDrawable icnPentoolState = new IconDrawable(mContext, name, com.hurix.commons.utils.Utils.getFontFilePath());
        //icnPentoolState.actionBarSize().color(color);
        button.setTypeface(Iconify.getTypeface(mContext, com.hurix.commons.utils.Utils.getFontFilePath()));
        button.setAllCaps(false);
        button.setText(name);
        if (com.hurix.commons.utils.Utils.checkForM()) {
            button.setTextSize(20);
        } else {
            button.setTextSize(21);
        }
        button.setTextColor(color);
        // button.setImageDrawable(icnPentoolState);
        if (mSelectedColor == color) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setStroke(2, Color.parseColor(mReaderThemeSettingVo.getReader().getDayMode().
                    getPentool().getPen().getSelectedBorderColor()));
            //drawable.setSize(50,50);
            parent.setBackgroundDrawable(drawable);
        }
    }

    private void setColorViewValue(View view, int color, LinearLayout parent) {
        if (view instanceof TextView) {
            TextView imageView = (TextView) view;
            setupIconsAccToName(imageView, CustomPlayerUIConstants.PT_COLOR_IC_TEXT, color, parent);

        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
    }

    public void setupIconsAccToName(ImageButton button, String name, int color) {
        IconDrawable icnPentoolState = new IconDrawable(mContext, name, mContext.getResources()
                .getString(R.string.kitaboo_font_file_name));
        icnPentoolState.actionBarSize().color(color);
        button.setImageDrawable(icnPentoolState);
    }
}
