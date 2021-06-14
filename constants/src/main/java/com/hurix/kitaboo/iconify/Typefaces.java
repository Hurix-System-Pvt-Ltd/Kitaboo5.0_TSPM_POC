package com.hurix.kitaboo.iconify;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.hurix.kitaboo.constants.Constants;


public class Typefaces {

	private static final String TAG = "Typefaces";

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String assetPath) 
	{
		synchronized (cache) {
			if (!cache.containsKey(assetPath)) 
			{
				try
				{
					Typeface t = Typeface.createFromAsset(c.getAssets(),assetPath);
					cache.put(assetPath, t);
				} catch (Exception e) 
				{
					if(Constants.IS_DEBUG_ENABLED)
					{
						Log.e(TAG, "Could not get typeface '" + assetPath
								+ "' because " + e.getMessage());
					}
					return null;
				}
			}
			return cache.get(assetPath);
		}
	}

}
