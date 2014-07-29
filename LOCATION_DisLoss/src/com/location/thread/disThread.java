package com.location.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import com.location.model.TestPoint;
import com.location.utill.MessageUtill;

import android.os.Handler;
import android.os.Message;

public class disThread extends Thread{
	
	private static int P0 = -30;
	private static int D0 = 1;
	private static int N = 3;
	private static int times = 20;
	
	public boolean isPause = false;	
	private Handler sendHandler = null;
	private Hashtable<String, ArrayList<Float>> signalTable = null;
	private TestPoint resultPoint = null;
	
	public Handler disHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MessageUtill.POINT && !isPause)
			{
				isPause = true;
				TestPoint point = (TestPoint)msg.obj;
				calculateDis(point);
				if(sendHandler != null)
				{
					Message m = sendHandler.obtainMessage(MessageUtill.DIS, resultPoint);
					sendHandler.sendMessage(m);
				}
			
				isPause = false;
			}
			super.handleMessage(msg);
		}
		
	};
	public disThread()
	{
		signalTable = new Hashtable<String, ArrayList<Float>>();
		resultPoint = new TestPoint(0);
	}
	private void calculateDis(TestPoint point)
	{
		Enumeration<String> keys = point.getSigs().keys();
		while(keys.hasMoreElements())
		{
			String key = keys.nextElement();
			Float sig = point.getSigs().get(key);
			Float index = -(sig - P0)/N/10;
			float dis = (float)Math.pow(10,index);
			
			if(signalTable.containsKey(key))
			{
				ArrayList<Float> list = signalTable.get(key);
				if(list.size()<times)
					list.add(dis);
				else
				{
					Collections.sort(list);
					resultPoint.addSig(key, list.get(times/2));
					list.clear();
					signalTable.put(key, list);
				}
				
			}
			else
			{
				ArrayList<Float> list = new ArrayList<Float>();
				list.add(dis);
				signalTable.put(key, list);
			}
		}
		
		
	}

	public void setSendHandler(Handler sendHandler) {
		this.sendHandler = sendHandler;
	}
	
	

}
