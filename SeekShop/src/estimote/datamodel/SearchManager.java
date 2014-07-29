package estimote.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
/**
 * ���ڲ�ѯ���ݿ�
 * @author ZHANG Fan
 *
 * 2014��7��28��
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
	 * ��Mac��ַ�������LID
	 * @param MAC
	 * @param dis
	 * @return ���̵�LID
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
	 * ͨ�����̵�LID��ȡ���̵�����
	 * @param list
	 * @return ��������
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
	 * ��Mac���iBeacon��IID
	 * @param MAC
	 * @return iBeacon��IID
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
