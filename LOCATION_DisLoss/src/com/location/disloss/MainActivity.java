package com.location.disloss;

import com.location.model.TestPoint;
import com.location.thread.disThread;
import com.location.thread.locThread;
import com.location.thread.wifiThread;
import com.location.thread.writeThread;
import com.location.utill.MessageUtill;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Point;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private wifiThread wifithread = null;
	private disThread disthread = null;
	private writeThread writethread = null;
	private locThread locthread = null;
	private TextView txt1 = null;
	private TextView txt2 = null;
//	private String[] ads = {"ec:88:8f:b4:f3:24","6c:e8:73:69:1c:52",
//			"6c:e8:73:68:fe:96","6c:e8:73:69:1d:16","c8:3a:35:0b:b5:90"};
	private String[] ads = {"24:69:a5:d4:6d:ec","0a:18:d6:2f:0e:f1"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txt1 = (TextView)findViewById(R.id.text1);
		txt2 = (TextView)findViewById(R.id.text2);
		
		this.wifithread = new wifiThread(this);
		this.disthread = new disThread();
		this.locthread = new locThread(this);
		//this.writethread = new writeThread(ads);
		this.wifithread.setSendHandler(handler);
		//this.disthread.setSendHandler(handler);
		//this.locthread.setSendHandler(handler);
		this.wifithread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onDestroy() {
		this.wifithread.isStop = true;
		this.wifithread = null;
		//this.writethread.isStop = true;
		//this.writethread.closeAllFiles();
		super.onDestroy();
	}


	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == MessageUtill.POINT){
					TestPoint points = (TestPoint)msg.obj;
					txt1.setText(points.toString());
					//System.out.println("0a:18:d6:2f:0e:f1" + "---" +points.getSigs().get("0a:18:d6:2f:0e:f1"));
					System.out.println("24:69:a5:d4:6d:ec" + "---" +points.getSigs().get("24:69:a5:d4:6d:ec"));
			}
			super.handleMessage(msg);
		}
	};

}
