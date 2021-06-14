package com.hurix.kitaboocloud.helpscreen;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hurix.kitaboocloud.R;
import com.hurix.kitaboocloud.common.CirclePageIndicator;
import com.hurix.kitaboocloud.common.OnHelpListener;
import com.hurix.kitaboocloud.common.ViewPagerAdapter;

/**
 * Created by Amit Raj on 08-08-2019.
 */
public class HelpScreenLayout extends RelativeLayout implements View.OnClickListener {

    private ViewPager _mviewPager;
    private CirclePageIndicator _mIndicator;
    Context _context;
    private RelativeLayout _helpPanel;
    private OnHelpListener _monHelpListener;
    private Button _btnOK;


    private ViewPagerAdapter _mviewPagerAdapter;

    public HelpScreenLayout(Context context) {
        super(context);
        _context = context;
        buildView();

    }

    public HelpScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        buildView();

    }

    public HelpScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
