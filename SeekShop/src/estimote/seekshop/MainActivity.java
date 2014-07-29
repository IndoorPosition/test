package estimote.seekshop;

import java.util.ArrayList;
import java.util.List;

import com.estimote.sdk.BeaconManager;

import estimote.bt_step.DistanceProcedure;
import estimote.bt_step.InformationPre;
import estimote.bt_step.ScanBluetooth;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * �ڵ�ͼ�ϣ���ʾ��Χ���̵�λ��
 * ����:�������
 *     ��������
 * @author ZHANG Fan
 *
 */
public class MainActivity extends Activity {

	private BeaconManager beaconManager;
	private ScanBluetooth scanBT;
	private DistanceProcedure distanceFilter;
	private InformationPre informationPre;
	
	private int OffsetY = 0;
	private int OffsetX = 0;
	private RelativeLayout mainLayout ;
	/**
	 * ������ʾ��ͼ
	 */
	private MapView mapView = null;
	/**
	 * ��������λ�ñ�ʾ��
	 */
	private List<ImageView> imgList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		beaconManager = new BeaconManager(this);
		this.checkBLE();
		
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ScanBluetooth.REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				this.startScanThread();
			} else {
				Toast.makeText(this,R.string.UNABLE_BLE, Toast.LENGTH_LONG).show();
			}
		}		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		scanBT.stopConnect();
		super.onDestroy();
	}
	
	/**
	 * ����豸�Ƿ���BLE�豸����������Ƿ���
	 */
	private void checkBLE() {
		//����Ƿ���BLE�豸
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this,R.string.NO_BLE, Toast.LENGTH_LONG).show();
			return;
		}

		// ��������Ƿ��
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, ScanBluetooth.REQUEST_ENABLE_BT);
		} else {
			this.startScanThread();
		}
	}
	/**
	 * ��������ɨ�� �����о����˲�������׼��
	 */
	private void startScanThread()
	{
		scanBT = new ScanBluetooth(beaconManager);
		distanceFilter = new DistanceProcedure();
		informationPre = new InformationPre(this);
		
		scanBT.sendHandler = distanceFilter.receiveHandler;
		distanceFilter.sendHandler = informationPre.receiveHandler;
		informationPre.sendHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what == InformationPre.MSG_XY)
				{
					List<Point> pointlist = (List<Point>)msg.obj;
					//��ʾ��������
					MainActivity.this.getActionBar().setSubtitle("" + msg.arg1/100.0);
					buildImage(pointlist);
				}
				super.handleMessage(msg);
			}
			
		};
		
	}
	

	@Override
	protected void onStart() {
		mainLayout = (RelativeLayout)findViewById(R.id.Layout);
		mapView = (MapView)findViewById(R.id.sonar);
		imgList = new ArrayList<ImageView>();

		this.getoffset();
		
		super.onStart();
	}
	/**
	 * ��ʾλ�õ�ͼƬ��������imgList��
	 * @param points
	 */
	private void buildImage(List<Point> points)
	{
		for(int i = 0;i < imgList.size() ; i++)
		{
			mainLayout.removeView(imgList.get(i));
		}
		imgList.clear();
	
		for(int i = 0; i < points.size() ; i++)
		{
			Point point = points.get(i);
			ImageView view = new ImageView(this);
			view.setBackgroundResource(R.drawable.near_l);
			
			float x =  point.x*mapView.widthscale ;
			float y = point.y*mapView.heightscale ;
			view.setX(x);
			view.setY(y);
			view.setVisibility(0);
			
			imgList.add(view);
			mainLayout.addView(view);
		}
	}
	
	/**
	 * ��ȡ��Ļƫ����
	 */
	private void getoffset()
	{
		//�������߶�
		Rect frame = new Rect();  
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		int statusBarHeight = frame.top;  
		int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop()-statusBarHeight;
		
		this.OffsetY = contentTop;
		this.OffsetX = mapView.getLeft();
	}



	


}
