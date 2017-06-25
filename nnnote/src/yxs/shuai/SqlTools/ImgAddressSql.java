package yxs.shuai.SqlTools;

import java.util.ArrayList;
import java.util.List;

import yxs.shuai.Sql.Info;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 每条笔记图片的物理地址数据库的增删改查
 * 
 * @author Shuai__Xiu
 * 
 */
public class ImgAddressSql {
	private Info info;
	private SQLiteDatabase db;

	public ImgAddressSql(Context context) {
		info = new Info(context);
	}

	public void AddAddresss(String time, String address) {
		db = info.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", time);
		values.put("address", Environment.getExternalStorageDirectory()
				+ "/shuai/img/" + address + ".jpg");
		db.insert("img", null, values);
		db.close();

	}

	public List<String> findAddress(String id) {
		
		db = info.getReadableDatabase();
		Cursor query = db.query("img", null, "id = ?", new String[] { id },
				null, null, null);
		List<String> list = new ArrayList<String>();
		while (query.moveToNext()) {
			String img_address = query.getString(query
					.getColumnIndex("address"));
			list.add(img_address);
		}
		query.close();
		db.close();
		return list;
	}

	public void update(String time, String address) {
		db = info.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("address", address);
		db.update("img", values, "id = ?", new String[] { time });
		db.close();
	}

	public void DeleteOne(String tag) {
		db = info.getWritableDatabase();
		db.delete("img", "address = ?", new String[] { tag });
		db.close();
	}
}
