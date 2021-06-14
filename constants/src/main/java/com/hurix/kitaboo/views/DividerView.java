package com.hurix.kitaboo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.hurix.kitaboo.constants.R;

public class DividerView extends View {
	static public int ORIENTATION_HORIZONTAL = 0;
	static public int ORIENTATION_VERTICAL = 1;
	private Paint mPaint;
	private int orientation;
	private Path path;

	public DividerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		int dashGap, dashLength, dashThickness;
		int color;
		path = new Path();
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DividerView, 0, 0);

		try {
			dashGap = a.getDimensionPixelSize(R.styleable.DividerView_dashGap, 5);
			dashLength = a.getDimensionPixelSize(R.styleable.DividerView_dashLength, 5);
			dashThickness = a.getDimensionPixelSize(R.styleable.DividerView_dashThickness, 3);
			color = a.getColor(R.styleable.DividerView_dashColor, 0xff000000);
			orientation = a.getInt(R.styleable.DividerView_orientation, ORIENTATION_VERTICAL);
		} finally {
			a.recycle();
		}

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(dashThickness);
		mPaint.setPathEffect(new DashPathEffect(new float[] {dashLength, dashGap}, 0));
	}

	public DividerView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		if (orientation == ORIENTATION_HORIZONTAL) {
//			float center = getMeasuredHeight() * .5f; 
//			canvas.drawLine(0, center, getMeasuredWidth(), center, mPaint);
//		} else {
//			float center = getMeasuredWidth() * .5f; 
//			canvas.drawLine(center, 0, center, getMeasuredHeight(), mPaint);
//		}
		
		path.reset();
		if (orientation == ORIENTATION_HORIZONTAL) 
		{
			float center = getHeight() * .5f; 
			path.moveTo(0, center);
			path.lineTo(getWidth(), center);
		} else 
		{
			float center = getWidth() * .5f; 
			path.moveTo(center, 0);
			path.lineTo(center, getHeight());		
		}

		path.close();
		canvas.drawPath(path, mPaint);
		
	}
}