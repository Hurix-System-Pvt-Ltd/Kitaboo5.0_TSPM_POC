package com.hurix.reader.kitaboosdkrenderer.utils;

import android.content.Context;


import com.hurix.reader.kitaboosdkrenderer.constants.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logger
{
	static File logfile =null;

	public static void writeLog(Context context,String log)
	{
		try
		{
			BufferedWriter writer ;

			logfile = new File(Constants.ALLBOOKROOTPATH+"log.txt");


			if(!logfile.exists())
			{
				logfile.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(logfile,true));
			writer.append(log + "\n");
			writer.close();
		}
		catch(Exception ex)
		{

		}
	}

	public static void clear() 
	{
		logfile = new File(Constants.ALLBOOKROOTPATH+"log.txt");


		if(logfile.exists())
		{
			logfile.delete();
		}
		
	}
}
