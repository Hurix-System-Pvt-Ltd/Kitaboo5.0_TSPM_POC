package com.hurix.kitaboocloud.common;



import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hurix.kitaboocloud.R;


public class HelpLayout extends RelativeLayout implements OnClickListener {

	private ViewPager _mviewPager;
	private CirclePageIndicator _mIndicator;
	Context _context;
	private RelativeLayout _helpPanel;
	private OnHelpListener _monHelpListener;
	private Button _btnOK;
	
	
	private ViewPagerAdapter _mviewPagerAdapter;
	
	public HelpLayout(Context context) {
		super(context);
		_context = context;
		buildView();
		
	}	
	
	public HelpLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		buildView();
		
	}
	
	public HelpLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		_context = context;
		buildView();
		
	}
	
	private void buildView()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.activity_help, this, true);
		initHelpView();
		
	}

	private void initHelpView() 
	{
		_helpPanel = (RelativeLayout) findViewById(R.id.helpPanel);
		_mviewPager = (ViewPager)findViewById(R.id.viewpager);
		_mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		_btnOK = (Button) findViewById(R.id.btnOK);
		_btnOK.setOnClickListener(this);
		
	}
	
	public void setData(String[] images)
	{
		_mviewPagerAdapter = new ViewPagerAdapter(_context, images);
		_mviewPager.setAdapter(_mviewPagerAdapter);
		_mviewPager.setCurrentItem(0);
		_mIndicator.setViewPager(_mviewPager);
		_mIndicator.setRadius(6f);
	}
	
	public void setListener(OnHelpListener listener)
	{
	    _monHelpListener = listener;
	}

	@Override
	public void onClick(View v) 
	{
		if(v.getId() == R.id.btnOK)
		{
			if(_monHelpListener!=null)
			{
				_monHelpListener.onOKClick();
			}
		}
	}
	


}
