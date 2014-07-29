package com.location.thread;


import java.util.ArrayList;

import com.location.disloss.R;
import com.location.model.TestPoint;
import com.location.model.adjust;
import com.location.utill.MessageUtill;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

public class locThread extends Thread{
	
	public boolean isStop = false;
	private String[] Mac = null;
	//资源中不能放浮点数
	private int[] x0 = null;
	private int[] y0 = null;
	
	public locThread(Context context)
	{
		//初始化资源Mac x0 y0
		Mac = context.getResources().getStringArray(R.array.Mac);
		x0 =  context.getResources().getIntArray(R.array.x10);
		y0 =  context.getResources().getIntArray(R.array.y10);
		
	}
	private Handler sendHandler = null;
	public Handler locHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MessageUtill.DIS && !isStop)
			{
				isStop = true;
				TestPoint point = (TestPoint)msg.obj;
				Point result = calculate(point);
				if(sendHandler != null && result != null)
				{
					Message m = sendHandler.obtainMessage(MessageUtill.LOC,result);
					sendHandler.sendMessage(m);
				}
				isStop = false;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private Point calculate(TestPoint point)
	{
		
		//准备数据
		ArrayList<Double> tx = new ArrayList<Double>();
		ArrayList<Double> ty = new ArrayList<Double>();
		ArrayList<Double> ts = new ArrayList<Double>();
		ArrayList<String> tMac1 = new ArrayList<String>();
		
		for(int i = 0; i<Mac.length;i++)
		{
			if(point.getSigs().containsKey(Mac[i]))
			{
				double dis = point.getSigs().get(Mac[i]);
				tx.add(x0[i]/10.0);
				ty.add(y0[i]/10.0);
				ts.add(dis);
				tMac1.add(Mac[i]);
			}
		}
		//带入计算
		if(tx.size()>0){
			Double[] x = new Double[tx.size()];
			tx.toArray(x);
			Double[] y = new Double[ty.size()];
			tx.toArray(y);
			Double[] s = new Double[ts.size()];
			tx.toArray(s);
			String[] Mac1 = new String[tMac1.size()];
			tMac1.toArray(Mac1);
			adjust ad = new adjust(s, x, y,Mac, Mac1);
			//返回结果
			System.out.println(ad.x + " " + ad.y);
			Point result = new Point((int)(ad.x*10), (int)(ad.y*10));
			return result;
		}
			
		return null;
	}

	public void setSendHandler(Handler sendHandler) {
		this.sendHandler = sendHandler;
	}
	

}
