package com.location.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.graphics.Point;
import android.net.wifi.ScanResult;

public class TestPoint {
	
	private long pointID = 0L;
	private Point pointCor = null;
	private Hashtable<String , Float> pointSigs = null;
	
	public TestPoint(long id )
	{
		pointID = id;
		pointCor = new Point();
		pointSigs = new Hashtable<String ,  Float>();
	}
	
	public long getID()
	{
		return pointID;
	}
	
	//换成double
	public void setPoint(int x , int y)
	{
		pointCor.x = x ;
		pointCor.y = y;
	}
	
	public Point getPoint()
	{
		return pointCor;
	}
	
	public void addSig(String mac ,  Float sig)
	{
//		System.out.println("add-----" + sig);
		pointSigs.put(mac, sig);
	}
	
	public Hashtable<String ,  Float> getSigs()
	{
		return pointSigs;
	}
	
	public void clearSigs()
	{
		pointSigs.clear();
	}
	
	public static TestPoint buildPointFromList(List<ScanResult> list)
	{
		if(list == null)
		{
			System.out.println("input list to TestPoint is null");
			return null;
		}
		if(list.size() == 0)
		{
			System.out.println("input list to TestPoint is empty");
			return null;
		}
		TestPoint point = new TestPoint(0);
		for(int i = 0 ; i < list.size() ; i++)
		{
			//不同于指纹定位，这里不需要正数处理
			point.addSig(list.get(i).BSSID, (float)(list.get(i).level));
		}
		return point;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("X:" + pointCor.x + "," + pointCor.y + '\n');
		
		Enumeration<String> keys = pointSigs.keys();
		while(keys.hasMoreElements())
		{
			String key = keys.nextElement();
			buffer.append("Key : " + key + " : " + pointSigs.get(key)  + '\n');
		}
		return buffer.toString();
	}

	
	

}
