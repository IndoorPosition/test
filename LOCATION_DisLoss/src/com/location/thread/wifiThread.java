package com.location.thread;

import java.util.List;

import com.location.model.TestPoint;
import com.location.utill.MessageUtill;

//import location.model.LocalPath;
//import location.model.TestPoint;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

public class wifiThread extends Thread{
	
	//private value
	private Context context = null;
	private WifiManager mWifiManager = null;
	private Handler sendHandler = null;
	//public value
	public boolean isStop = false;
	//const
	private final static int TIME = 200;
	
	//construction
	public wifiThread(Context context)
	{
		this.context = context;
		if(this.context != null)
			this.mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	//function
	////isOpen
	private boolean isOpen()
	{
		if (!mWifiManager.isWifiEnabled())
			return false;
		else
			return true;
	}
	////setSendHandler
	public void setSendHandler(Handler handler)
	{
		this.sendHandler = handler;
	}
	////buildTestPoint
	private void buildTestPoint()
	{
		//get ScanResult
		this.mWifiManager.startScan();
		List<ScanResult> list = this.mWifiManager.getScanResults();
		//buildTestPoint（还需修改为const）
		Message msg = sendHandler.obtainMessage(MessageUtill.POINT, TestPoint.buildPointFromList(list));
		this.sendHandler.sendMessage(msg);
	}
	
	//overwrite
	////run
	@Override
	public void run() {
		if(this.isOpen())
		{
			this.buildTestPoint();
			//（还需修改为const）
			if(!isStop)
				this.timeHandler.sendEmptyMessage(MessageUtill.SEND);
		}
		super.run();
	}
	
	//handler
	private Handler timeHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//（还需修改为const）
			if(msg.what == MessageUtill.SEND)
				this.postDelayed(wifiThread.this, TIME);
			if(isStop)
				this.removeCallbacks(wifiThread.this);
			super.handleMessage(msg);
		}
		
	};

	
}
