package estimote.nearbytips;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 用于显示Tips的Activity
 *    包含:蓝牙设备检查
 *        测试数据显示
 * @author ZHANG Fan
 *
 */
public class MainActivity extends Activity {
	
	private TipListAdapter adapter;
	private ScanBluetooth scanBLE;
	private BeaconManager beaconManager;
	
	/**
	 * 用于显示测试数据的存储单元 测试iBeacon
	 * "D9:B6:F4:47:AB:24" -- 深蓝
	 * "FD:34:F8:83:5F:57" -- 浅蓝
	 * "FD:89:78:F6:FC:BC" -- 浅绿
	 */
	private Hashtable<String,Tip> tiptable ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		this.testDataPre();
		
	}
	/**
	 * 准备数据 tiptable
	 *    包括：名称 图片
	 */
	private void testDataPre()
	{
		tiptable = new Hashtable<String , Tip>();
		Tip tip1 = new Tip();
		tip1.tip_image = getResources().getDrawable(R.drawable.tip_1);
		tip1.tip_name = (String) getResources().getText(R.string.tip_name_1);
		String mac1 =(String) getResources().getText(R.string.mac1);
		Tip tip2 = new Tip();
		tip2.tip_image = getResources().getDrawable(R.drawable.tip_2);
		tip2.tip_name = (String) getResources().getText(R.string.tip_name_2);
		String mac2 = (String) getResources().getText(R.string.mac2);;
		Tip tip3 = new Tip();
		tip3.tip_image = getResources().getDrawable(R.drawable.tip_3);
		tip3.tip_name = (String) getResources().getText(R.string.tip_name_3);
		String mac3 = (String) getResources().getText(R.string.mac3);;
		
		tiptable.put(mac1, tip1);
		tiptable.put(mac2, tip2);
		tiptable.put(mac3, tip3);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	protected void onStart() {
		
		beaconManager = new BeaconManager(this);
		this.checkBLE();
		
		this.adapter = new TipListAdapter(this);
		ListView list = (ListView)findViewById(R.id.tip_list);
		
		list.setAdapter(adapter);
		//点击tip条目，显示已读
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				adapter.readAlready(position);
			}
		});
		
		super.onStart();
	}
	
	/**
	 * 检查设备中是否有BLE设备，蓝牙是否打开
	 */
	private void checkBLE() {
		//检查是否有BLE设备
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this,R.string.NO_BLE,Toast.LENGTH_LONG).show();;
			return;
		}

		// 检查蓝牙是否打开
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, ScanBluetooth.REQUEST_ENABLE_BT);
		} else {
			this.startScanBLE();
		}
	}
	
	/**
	 * 开启蓝牙设备 和 SacnBlutooth服务
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ScanBluetooth.REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				this.startScanBLE();
			} else {
				Toast.makeText(this, R.string.UNABLE_BLE, Toast.LENGTH_LONG).show();
			}
		}		
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 开启蓝牙扫描
	 */
	private void startScanBLE()
	{
		scanBLE = new ScanBluetooth(beaconManager);
		scanBLE.sendHandler =  new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == ScanBluetooth.MSG_BT)
				{
					List<Beacon> beacons = (List<Beacon>)msg.obj;
					List<Tip> tips = new ArrayList<Tip>();
					//扫描搜到的iBeacon信号
					for(int i = 0 ; i < beacons.size(); i ++)
					{
						Beacon beacon = beacons.get(i);
						String mac = beacon.getMacAddress();
						if(tiptable.containsKey(mac)){
							Tip tip = tiptable.get(mac);
							tips.add(tip);
							tiptable.remove(mac);
						}
					}
					//更新数据显示
					if(tips.size() >0)
						adapter.refresh(tips);
				}
				super.handleMessage(msg);
			}
			
		};
		
	}
}
