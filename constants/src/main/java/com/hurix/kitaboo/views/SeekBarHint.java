package com.hurix.kitaboo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.hurix.kitaboo.constants.R;

public class SeekBarHint extends SeekBar implements SeekBar.OnSeekBarChangeListener {

	private int mPopupWidth;
	private int mPopupStyle;
	public static final int POPUP_FIXED = 1;
	public static final int POPUP_CENTERED = 2;
	public static final int POPUP_FOLLOW = 0;

	private PopupWindow mPopup;
	//	private TextView mPopupTextView;
	private int mYLocationOffset;

	private OnSeekBarChangeListener mInternalListener;
	private OnSeekBarChangeListener mExternalListener;

	private OnSeekBarHintProgressChangeListener mProgressChangeListener;
	private int mPopupLayoutID;

	public interface OnSeekBarHintProgressChangeListener {
		String onHintTextChanged(SeekBarHint seekBarHint, int progress);
	}

	public SeekBarHint (Context context) {
		super(context);
		init(context, null);
	}

	public SeekBarHint (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public SeekBarHint (Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs){

		setOnSeekBarChangeListener(this);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarHint);

		mPopupWidth = (int) a.getDimension(R.styleable.SeekBarHint_popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
		mYLocationOffset = (int) a.getDimension(R.styleable.SeekBarHint_yOffset, 0);
		mPopupStyle = a.getInt(R.styleable.SeekBarHint_popupStyle, POPUP_FOLLOW);
		mPopupLayoutID = a.getResourceId(R.styleable.SeekBarHint_popupResource, -1);
		a.recycle();
		initHintPopup();
	}

	public void setPopupStyle(int style){
		mPopupStyle = style;
	}

	public int getPopupStyle(){
		return mPopupStyle;
	}
	

	private void initHintPopup()
	{

		if (mProgressChangeListener!=null){
			mProgressChangeListener.onHintTextChanged(this, getProgress());
		}

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(mPopupLayoutID!=-1)
		{
			final View undoView = inflater.inflate(mPopupLayoutID, null);

			mPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

			//mPopup.setAnimationStyle(R.style.fade_animation);
		}

	}

	private void showPopup(){

		if(mPopup == null)
			return;

		if(mPopupStyle==POPUP_FOLLOW)
		{
			mPopup.showAtLocation(this, Gravity.LEFT | Gravity.BOTTOM,
                    (int) (this.getX()+(int) getXPosition(this)), (int) (this.getY()+mYLocationOffset+this.getHeight()));
		}
		else if (mPopupStyle==POPUP_FIXED)
		{
			mPopup.showAtLocation(this, Gravity.CENTER | Gravity.BOTTOM, 0, (int) (this.getY()+mYLocationOffset+this.getHeight()));
		}
		else if (mPopupStyle==POPUP_CENTERED)
		{
			mPopup.showAtLocation(this.getRootView(), Gravity.CENTER, 0, 0);
		}
	}

	private void hidePopup(){
		if(mPopup == null)
			return;
		if(mPopup.isShowing()) {
			mPopup.dismiss();
		}
	}

	public void setHintView(View view){
		//TODO
		//initHintPopup();
	}

	public View getHintView()
	{
		return mPopup!=null?mPopup.getContentView():null;
	}

	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		if (mInternalListener == null) {
			mInternalListener = l;
			super.setOnSeekBarChangeListener(l);
		} else {
			mExternalListener = l;
		}
	}

	public void setOnProgressChangeListener(OnSeekBarHintProgressChangeListener l){
		mProgressChangeListener = l;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
		String popupText = null;
		if (mProgressChangeListener!=null){
			popupText = mProgressChangeListener.onHintTextChanged(this, getProgress());
		}

		if(mExternalListener !=null){
			mExternalListener.onProgressChanged(seekBar, progress, b);
		}

		if(mPopupStyle==POPUP_FOLLOW && mPopup!=null){
			mPopup.update((int) (this.getX()+(int) getXPosition(seekBar)),
                    (int) (this.getY()+mYLocationOffset+this.getHeight()), -1, -1);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if(mExternalListener !=null){
			mExternalListener.onStartTrackingTouch(seekBar);
		}

		showPopup();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if(mExternalListener !=null){
			mExternalListener.onStopTrackingTouch(seekBar);
		}

		hidePopup();
	}

	private float getXPosition(SeekBar seekBar){
		float val = (((float)seekBar.getProgress() * (float)(seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax());
		float offset = seekBar.getThumbOffset();

		int textWidth = mPopupWidth;
		float textCenter = (textWidth/2.0f);

		float newX = val + offset - textCenter;

		return newX;
	}
}