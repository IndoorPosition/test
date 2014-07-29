package estimote.nearbytips;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.estimote.sdk.utils.L;

/**
 * ɨ��iBeacon��BT��Ϣ
 *    ����iBeacon��BLE SDK
 * @author ZHANG Fan
 *
 */
public class ScanBluetooth {
	
	public Handler sendHandler;
	private BeaconManager beaconManager;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

	public static final int MSG_BT = 0x0001;
	public static final int REQUEST_ENABLE_BT = 1234;
	private static final int NEARBY_RANGE = 5;
	
	/**
	 * ScanThread�Ĺ��캯��������BeaconManager
	 * @param manager
	 */
	public ScanBluetooth(BeaconManager manager) {
		//��ʼ��־
		L.enableDebugLogging(true);
		//��BeaconManager��ֵ��������ɨ�����
		this.beaconManager = manager;
		this.beaconManager.setRangingListener(this.createRanagingListener());
		this.beaconManager.connect(this.createCallback());
		
	}
	
	/**
	 * ����RangingListener ��BeaconManager.setRangingListener()������ʹ��
	 * @return BeaconManager.RangingListener
	 */
	private BeaconManager.RangingListener createRanagingListener() {
		BeaconManager.RangingListener listener = new BeaconManager.RangingListener() {

			@Override
			public void onBeaconsDiscovered(Region arg0, List<Beacon> arg1) {
				if(arg1.size()>0){
					if(sendHandler != null)
					{
						Message m = sendHandler.obtainMessage(MSG_BT);
						m.obj = distanceCheck(arg1);
						sendHandler.sendMessage(m);
					}
					
				}
			}
		};
		return listener;
	}
	
	/**
	 * ����ServiceReadyCallBack����BeaconManager.connect()������ʹ��
	 * @return BeaconManager.ServiceReadyCallBack
	 */
	private BeaconManager.ServiceReadyCallback createCallback() {
		BeaconManager.ServiceReadyCallback callBack = new BeaconManager.ServiceReadyCallback() {

			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					
					Log.e("SDK�ڲ�����", e.toString());
				}
			}
		};
		return callBack;
	}
	/**
	 * ֹͣ����
	 */
	public void stopConnect()
	{
		try {
			this.beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.beaconManager.disconnect();
	}
	
	private List<Beacon> distanceCheck( List<Beacon> rawList)
	{
		List<Beacon> tempList = new ArrayList<Beacon>(rawList);
		for(int i = 0; i < rawList.size(); i++)
		{
			if(Utils.computeAccuracy(rawList.get(i)) > NEARBY_RANGE)
				tempList.remove(rawList.get(i));
		}
		return tempList;
	}
	

}
