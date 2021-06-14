package com.hurix.kitaboocloud.common;

import com.hurix.kitaboo.constants.Constants;
import com.hurix.kitaboocloud.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class HelpActivity extends Activity implements OnHelpListener
{
	private HelpLayout _layout;
	private SharedPreferences sharedpreferences;

	private SharedPreferences.Editor editor ;
	private boolean from ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	//	getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
		com.hurix.epubreader.Utility.Utils.setSecureWindowFlags(HelpActivity.this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sharedpreferences = getSharedPreferences(Constants.SHELF_PREFS_NAME, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		from = getIntent().getExtras().getBoolean("from");
		
		String[] images = getResources().getStringArray(R.array.help_screens);
		_layout = new HelpLayout(this);
		_layout.setData(images);
		_layout.setListener(this);
		setContentView(_layout);
	}
	
	
	@Override
	public void onOKClick() 
	{
		if(!from)
		{
		   if(!sharedpreferences.getBoolean(Constants.BOOK_SHELF_LAUNCH_FIRST_TIME,false))
			{
				editor.putBoolean(Constants.BOOK_SHELF_LAUNCH_FIRST_TIME,true);
				editor.commit();			
			}
			else if(!sharedpreferences.getBoolean(Constants.BOOK_PLAYER_LAUNCH_FIRST_TIME,false))
			{
				editor.putBoolean(Constants.BOOK_PLAYER_LAUNCH_FIRST_TIME,true);
				editor.commit();			
			}	
		}
		finish();		
	}
}
