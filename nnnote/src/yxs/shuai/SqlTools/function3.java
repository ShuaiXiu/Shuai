package yxs.shuai.SqlTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yxs.shuai.Sql.Info;
import yxs.shuai.Utils.MD5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 密保问题的增删改查
 * 
 * @author Shuai__Xiu
 * 
 */
public class function3 {
	private Info info;
	private SQLiteDatabase db;

	public function3(Context context) {
		info = new Info(context);
	}

	public long insert(String wenti, String daan, String z) {
		db = info.getWritableDatabase();
		z = MD5.encode(z);
		ContentValues values = new ContentValues();
		values.put("wenti", wenti);
		values.put("daan", daan);
		values.put("z", z);
		long id = db.insert("mb", null, values);
		db.close();
		return id;
	}

	public void isSetting(String z, String istrue) {
		db = info.getWritableDatabase();
		z = MD5.encode(z);
		ContentValues values = new ContentValues();
		values.put("isSetting", istrue);
		db.update("mb", values, "z = ?", new String[] { z });
		db.close();
	}

	public String findSetting(String z) {
		db = info.getReadableDatabase();
		z = MD5.encode(z);
		Cursor query = db.query("mb", null, "z = ?", new String[] { z }, null,
				null, null);
		String isSet = "";
		while (query.moveToNext()) {
			isSet = query.getString(query.getColumnIndex("isSetting"));
		}
		query.close();
		db.close();
		return isSet;
	}

	public int update(String wenti, String daan) {
		db = info.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("daan", daan);
		int number = db.update("mb", values, "wenti=?", new String[] { wenti });
		db.close();
		return number;
	}

	public List<Map<String, String>> findM(String zh) {
		db = info.getReadableDatabase();
		zh = MD5.encode(zh);
		Cursor cursor = db.query("mb", null, "z = ?", new String[] { zh },
				null, null, null);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		while (cursor.moveToNext()) {
			map = new HashMap<String, String>();
			String wenti = cursor.getString(cursor.getColumnIndex("wenti"));
			String daan = cursor.getString(cursor.getColumnIndex("daan"));
			map.put(wenti, daan);
			list.add(map);
		}
		db.close();
		cursor.close();
		return list;
	}

	public boolean findisNull() {
		db = info.getReadableDatabase();
		Cursor query = db.query("mb", null, null, null, null, null, null);
		boolean moveToNext = query.moveToNext();
		db.close();
		query.close();
		return moveToNext;
	}
}
