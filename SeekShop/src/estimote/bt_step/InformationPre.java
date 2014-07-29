package estimote.bt_step;

import java.util.HashMap;
import java.util.List;

import estimote.datamodel.SearchManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

/**
 * 查询数据库，获取周围商铺的位置
 * @author ZHANG Fan
 *
 * 2014年7月28日
 */
public class InformationPre {

	private SearchManager manager = null;
	public Handler receiveHandler = null;
	public Handler sendHandler = null;
	public static final int MSG_XY = 0x0003;
	
	private static final int NEAR = 3;
	private static final int FAR = 5;
	private static final int PRETTY_FAR = 10;

	public InformationPre(Context context) {
		manager = new SearchManager(context);
		receiveHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == DistanceProcedure.MSG_DIS && msg.obj != null) {
					try {
						HashMap<String, Object> received = (HashMap<String, Object>) msg.obj;
						String mac = received.get("MAC").toString();
						double dis = (Double) received.get("DIS");
						int d = defineDistance(dis);
						List<Point> list = manager.getXYFromLID(manager.getLIDsFromMAC(mac, d));
						//为了方便测试使用，将计算出的距离值一并发出
						if(sendHandler!= null&&list!=null)
						{
							Message m = sendHandler.obtainMessage(MSG_XY);
							m.obj = list;
							m.arg1 = (int)(dis*100);
							sendHandler.sendMessage(m);
						}
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				super.handleMessage(msg);
			}
		};
	}

	private int defineDistance(double distance) {
		int result = -1;
				
		if (distance >0 && distance < NEAR)
			result = 1;
		
		if (distance >= NEAR && distance < FAR)
			result = 2;
		
		if (distance >=FAR && distance < PRETTY_FAR)
			result =3;
		
		return result;
	}

}
