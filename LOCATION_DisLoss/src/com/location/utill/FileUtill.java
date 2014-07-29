package com.location.utill;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class FileUtill {
	
	public static String DIR = Environment.getExternalStorageDirectory().toString() + "/L_D";
	public static void FileCheck(String filePath)
	{
		File file = new File(filePath);
		if(file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("FileUtill---FileCheck--fail to create new file");
			Log.e("FileUtill---FileCheck", e.toString());
			e.printStackTrace();
		}
	}
	public static void DirCheck(String dirPath) 
	{
		File file = new File(dirPath);
		if(file.exists())
			file.delete();
		file.mkdirs();
	}

}
