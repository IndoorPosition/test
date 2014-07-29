package estimote.datamodel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class SearchHelper extends SQLiteOpenHelper{
	
	private static String DATABASE_NAME=Environment.getExternalStorageDirectory()+"/sql/iBeacon";;  
    private static final int DATABASE_VERSION = 1;  

	public SearchHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	
	

}
