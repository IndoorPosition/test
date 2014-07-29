package estimote.datamodel;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;
/**
 * 用于保存Beacon滤波单元的类，可进行排序
 * @author ZHANG Fan
 *
 * 2014年7月28日
 */
public class meanBeacon extends Beacon implements Comparable<meanBeacon>{

	private double sumDistance  = Double.MAX_VALUE;
	private int times = 0;
	
	public meanBeacon(Beacon beacon) {
		
		super(beacon.getProximityUUID(),
			  beacon.getName(), 
			  beacon.getMacAddress(), 
			  beacon.getMajor(), 
			  beacon.getMinor(), 
			  beacon.getMeasuredPower(), 
			  beacon.getRssi());
	}
	
	/**
	 * 向meanBeacon中添加一次测量值
	 * @param beacon
	 */
	public void addOneScan(Beacon beacon)
	{
		if(this.getMacAddress().equals(beacon.getMacAddress()))
		{
			double dis = Utils.computeAccuracy(beacon);
			if(times == 0)
				this.sumDistance = dis;
			else
				this.sumDistance += dis;
			
			times++;
		}
		
	}
	
	@Override
	public int compareTo(meanBeacon other) {
		double objthis = this.getMean();
		double objother = this.getMean();
		
		if(objthis>objother)
			return 1;
		else if(objthis<objother)
			return -1;
		else 
			return 0;
	}
	
	/**
	 * 获取均值滤波值
	 * @return 滤波值
	 */
	public double getMean()
	{
		if(times == 0)
			return Double.MAX_VALUE;
		else
			return (sumDistance/times);
	}

	@Override
	public String toString() {
		return "meanBeacon [Mac=" + this.getMacAddress() + ", times=" + times
				+ ", getMean()=" + getMean() + "]";
	}
	
	
	
	

}
