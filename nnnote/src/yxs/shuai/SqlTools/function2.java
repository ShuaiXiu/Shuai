package yxs.shuai.SqlTools;

import yxs.shuai.Sql.Info;
import yxs.shuai.Utils.MD5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 账号密码的增删改查
 * 
 * @author Shuai__Xiu
 * 
 */
public class function2 {
	private Info info;
	private SQLiteDatabase db;

	public function2(Context context) {
		info = new Info(context);
	}

	/*
	 * 插入帐号密码
	 */
	public long insert(String zhanghao, String mima) {
		db = info.getWritableDatabase();

		zhanghao = MD5.encode(zhanghao);
		mima = MD5.encode(mima);

		ContentValues values = new ContentValues();
		values.put("z", zhanghao);
		values.put("m", mima);
		long id = db.insert("zhanghao", null, values);
		db.close();
		return id;
	}

	/*
	 * 更新帐号密码
	 */
	public int update(String zh1, String mima1) {
		db = info.getWritableDatabase();

		zh1 = MD5.encode(zh1);
		mima1 = MD5.encode(mima1);

		ContentValues values = new ContentValues();
		values.put("m", mima1);
		int number = db.update("zhanghao", values, "z=?", new String[] { zh1 });
		db.close();
		return number;
	}

	/*
	 * 根据Id删除记录
	 */
	public int delete(long id) {
		db = info.getWritableDatabase();
		int number = db.delete("zhanghao", "_id=?", new String[] { id + "" });
		db.close();
		return number;
	}

	/*
	 * 根据帐号查询账号是否存在
	 */
	public boolean find(String zh) {
		db = info.getReadableDatabase();
		zh = MD5.encode(zh);
		Cursor cursor = db.query("zhanghao", null, "z=?", new String[] { zh },
				null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

	/*
	 * 根据帐号查询帐号所属密码
	 */
	public String sels(String z) {
		db = info.getReadableDatabase();
		z = MD5.encode(z);
		Cursor cursor = db.query("zhanghao", null, "z=?", new String[] { z },
				null, null, null);
		String m = "";
		while (cursor.moveToNext()) {
			m = cursor.getString(cursor.getColumnIndex("m"));
		}

		db.close();
		cursor.close();
		return m;
	}

	/*
	 * 查询是否拥有帐号密码
	 */
	public boolean findAll() {
		db = info.getReadableDatabase();
		Cursor cursor = db
				.query("zhanghao", null, null, null, null, null, null);
		boolean result = cursor.moveToNext();
		db.close();
		cursor.close();
		return result;
	}
}
