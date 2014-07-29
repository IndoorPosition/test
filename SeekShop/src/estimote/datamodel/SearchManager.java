package estimote.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
/**
 * 用于查询数据库
 * @author ZHANG Fan
 *
 * 2014年7月28日
 */
public class SearchManager {
	
	private SearchHelper helper;
	private SQLiteDatabase db;
	
	public SearchManager(Context context)
	{
		helper = new SearchHelper(context);
		db = helper.getReadableDatabase();
	}
	/**
	 * 从Mac地址获得商铺LID
	 * @param MAC
	 * @param dis
	 * @return 商铺的LID
	 */
	public ArrayList<String> getLIDsFromMAC(String MAC , int dis)
	{
		int id = this.getIIDFromMAC(MAC);
		ArrayList<String> result = null;
		if(id != -1)
		{
			String sql = "Select LID, Distance From L_B where IID = "+id;
			Cursor cursor = db.rawQuery(sql, null);
			result = new ArrayList<String>();
			while(!cursor.isLast()&&cursor.getCount()>0)
			{
				cursor.moveToNext();
				int d = cursor.getInt(cursor.getColumnIndex("Distance"));
				if(d==dis)
					result.add(cursor.getString(cursor.getColumnIndex("LID")));
			}
		}
		return result;
	}
	/**
	 * 通过商铺的LID获取商铺的坐标
	 * @param list
	 * @return 商铺坐标
	 */
	public List<Point> getXYFromLID(ArrayList<String> list)
	{
		List<Point> result =null;
		if(list!=null&&list.size()>0)
		{
			result = new ArrayList<Point>();
			String sql = "Select x,y from LocationID where ";
			for(int i =0;i<list.size();i++)
			{
				if(i != list.size()-1)
					sql+="LID = \'"+list.get(i)+"\'or ";
				else
					sql+="LID = \'"+list.get(i)+"\'";
			}
			Cursor cursor = db.rawQuery(sql, null);
			while(!cursor.isLast())
			{
				cursor.moveToNext();
				int x = cursor.getInt(cursor.getColumnIndex("X"));
				int y = cursor.getInt(cursor.getColumnIndex("Y"));
				Point point = new Point(x, y);
				result.add(point);
			}
		}
		
		return result;
	}
	
	/**
	 * 从Mac获得iBeacon的IID
	 * @param MAC
	 * @return iBeacon的IID
	 */
	private int getIIDFromMAC(String MAC)
	{
		String sql = "Select ID From iBeaconID Where Mac = \'"+MAC+"\'";
		Cursor cursor = db.rawQuery(sql,null);
		int result =-1;
		while(!cursor.isLast())
		{
			cursor.moveToNext();
			result = cursor.getInt(cursor.getColumnIndex("ID"));
		}
		return result;
	}

	@Override
	protected void finalize() throws Throwable {
		db.close();
		super.finalize();
	}
	
	
	
}
