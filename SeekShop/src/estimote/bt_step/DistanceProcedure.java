package estimote.bt_step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import com.estimote.sdk.Beacon;

import android.os.Handler;
import android.os.Message;
import estimote.datamodel.meanBeacon;;

/**
 * ʵ�־���ľ�ֵ�˲�
 * @author ZHANG Fan
 *
 */
public class DistanceProcedure {
	
	private int times = 0;
	private Hashtable<String, meanBeacon> beacontable;
	
	public Handler sendHandler;
	public Handler receiveHandler;
	
	private static final int N = 10;
	public static final int MSG_DIS = 0x0002;
	
	public DistanceProcedure()
	{
		beacontable = new Hashtable<String, meanBeacon>();
		receiveHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == ScanBluetooth.MSG_BT)
				{
					List<Beacon> list = (List<Beacon>)msg.obj;
					if(list!=null){
						addBeaconList(list);
						sendCheck();
					}
				}
				super.handleMessage(msg);
			}
			
		};
	}
	
	/**
	 * �����ɨ���iBeacon��ӵ�֮ǰ�ļ�¼�У�list���֮ǰ��Ҫ�ж��Ƿ�Ϊ��
	 * @param list
	 */
	private void addBeaconList(List<Beacon> list)
	{
		for(int list_i = 0;list_i <list.size();list_i++)
		{
			Beacon newBeacon = list.get(list_i);
			if(beacontable.containsKey(newBeacon.getMacAddress()))
			{
				meanBeacon item = beacontable.get(newBeacon.getMacAddress());
				item.addOneScan(newBeacon);
				beacontable.put(newBeacon.getMacAddress(), item);
			}
			else
			{
				meanBeacon newItem = new meanBeacon(newBeacon);
				beacontable.put(newBeacon.getMacAddress(), newItem);
			}
		}
		
	}
	
	/**
	 * ���ؾ��������Beacon��Mac�;��룬��Ҫ�жϷ���ֵ�Ƿ�Ϊ��
	 * @return <"MAC", macAddress> <"DIS", meanDis >
	 */
	private HashMap<String, Object> getNearestBeaconID()
	{
		HashMap<String, Object> result = null;
		synchronized (beacontable) {
			if(beacontable.size() > 0)
			{
				ArrayList<meanBeacon> list = new ArrayList<meanBeacon>(beacontable.values());
				Collections.sort(list);
				result = new HashMap<String, Object>();
				result.put("MAC", list.get(list.size()-1).getMacAddress());
				result.put("DIS", list.get(list.size()-1).getMean());
			}
			
		}
		return result;
	}
	
	/**
	 * ����Ƿ�ﵽ��������
	 */
	private void sendCheck()
	{
		if(times < N)
			times++;
		else
		{
			if(sendHandler!= null)
			{
				Message msg = sendHandler.obtainMessage(MSG_DIS);
				msg.obj = this.getNearestBeaconID();
				sendHandler.sendMessage(msg);
			}
			beacontable.clear();
			times = 0;
		}
	}

}
