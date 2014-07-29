package com.location.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 鏂囦欢鎿嶄綔
 * @author zlw
 *
 */
public class FileIO {
	//function
	////OpenWriteFile
	public static FileOutputStream openWriteFile(String filePath)
	{
		File file = new File(filePath);
		FileOutputStream output = null;
		try{
			output = new FileOutputStream(file);
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("鏂囦欢: "+ filePath + " 涓嶅瓨鍦�");
		}
		return output;
	}
	////WriteFile
	public static void WriteFile(FileOutputStream output , StringBuilder builder)
	{
		if(output == null)
	 	{
			System.out.println("fileoutputstream 涓嶅瓨鍦�");
			return;
		}
		
		try {
			output.write(builder.toString().getBytes());
		} catch (IOException e) {
			System.out.println("鍐欐枃浠跺嚭鐜版湭鐭ラ敊璇�");
			e.printStackTrace();
		}
	}
	////CloseWriteFile
	public static void closeWriteFile(FileOutputStream output)
	{
		if(output == null)
	 	{
			System.out.println("fileoutputstream 涓嶅瓨鍦�");
			return;
		}
		
		try {
			output.flush();
			output.close();
		} catch (IOException e) {
			System.out.println("淇濆瓨鏂囦欢鍑虹幇鏈煡閿欒");
			e.printStackTrace();
		}
	}

}
