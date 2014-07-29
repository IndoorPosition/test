package com.location.thread;

import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import android.os.Handler;
import android.os.Message;

import com.location.model.FileIO;
import com.location.model.TestPoint;
import com.location.utill.FileUtill;
import com.location.utill.MessageUtill;

public class writeThread extends Thread{
	
	public boolean isStop = true;
	
	private Hashtable<String, FileOutputStream> filetable = null;
	private String[] adscopy = null;
	private String filedir = null;
	
	private Handler sendHandler = null;
	public Handler writehandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//判断TestPoint数据传入
			if(msg.what == MessageUtill.POINT && (!isStop))
			{
				//阻塞
				isStop = true;
				//写入数据
				writeSig2file((TestPoint)msg.obj);
				//创建新的TestPoint
				TestPoint newPoint = buildTestPoint((TestPoint)msg.obj);
				//传出新建的TestPoint
				if(sendHandler!= null)
				{
					Message m = sendHandler.obtainMessage(MessageUtill.WRITE,newPoint);
					sendHandler.sendMessage(m);
				}
				//放弃阻塞
				isStop = false;
			}
			
			
			super.handleMessage(msg);
		}
		
	};
	
	public writeThread(String[] ads)
	{
		if(ads.length != 0)
		{
			//初始化filetable和adscopy filedir
			filetable = new Hashtable<String, FileOutputStream>();
			adscopy = ads;
			filedir = FileUtill.DIR+'/'+System.currentTimeMillis();
			FileUtill.DirCheck(filedir);
			//赋值filetable
			for(int i = 0;i <ads.length;i++)
			{
				String path = filedir + '/'+ads[i]+".txt";
				FileOutputStream output = FileIO.openWriteFile(path);
				filetable.put(ads[i], output);
			}
			//
			//更改isStop
			isStop = false;
		}
	}
	
	public void writeSig2file(TestPoint point)
	{
		//是否成功初始化、adscopy的值是否为空
		if(adscopy.length != 0)
		{
			for(int j= 0;j<adscopy.length;j++)
			{
				//判断是否存在ad
				if(point.getSigs().containsKey(adscopy[j]))
				{
					//提取强度
					float  sig = point.getSigs().get(adscopy[j]);
					//创建StringBuilder
					StringBuilder builder = new StringBuilder();
					builder.append(sig);
					builder.append('\n');
					//写入文件
					FileOutputStream output = filetable.get(adscopy[j]);
					FileIO.WriteFile(output, builder);
				}
				
			}
		}
	}
	
	private TestPoint buildTestPoint(TestPoint point)
	{
		if(adscopy.length != 0)
		{
			TestPoint newPoint = new TestPoint(0);
			for(int i=0;i<adscopy.length;i++){
				if(point.getSigs().containsKey(adscopy[i]))
					newPoint.addSig(adscopy[i], point.getSigs().get(adscopy[i]));
			}
				
			return newPoint;
		}
		return null;
	}
	
	public void closeAllFiles()
	{
		if(!filetable.isEmpty())
		{
			isStop = true;
			//获取文件列表
			Enumeration<String> keys = filetable.keys();
			while(keys.hasMoreElements())
			{
				//关闭文件
				String key = keys.nextElement();
				FileOutputStream output = filetable.get(key);
				FileIO.closeWriteFile(output);
			}
			//清除FileOutputSteam
			filetable.clear();
			adscopy = null;
				
		}
	}

	public void setSendHandler(Handler sendHandler) {
		this.sendHandler = sendHandler;
	}
	

}
