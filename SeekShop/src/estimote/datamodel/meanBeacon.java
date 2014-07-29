package estimote.datamodel;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;
/**
 * ���ڱ���Beacon�˲���Ԫ���࣬�ɽ�������
 * @author ZHANG Fan
 *
 * 2014��7��28��
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
	 * ��meanBeacon�����һ�β���ֵ
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
	 * ��ȡ��ֵ�˲�ֵ
	 * @return �˲�ֵ
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
