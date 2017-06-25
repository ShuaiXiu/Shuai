package yxs.shuai.SqlTools;

import java.util.ArrayList;
import java.util.List;

import yxs.shuai.Sql.Info;
import yxs.shuai.Utils.MD5;
import yxs.shuai.bean.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 笔记的增删改查
 * 
 * @author Shuai__Xiu
 * 
 */
public class function {
	private Info info;
	private SQLiteDatabase db;

	public function(Context context) {
		info = new Info(context);
	}

	public long addBJ(String z, String biaoti, String neirong, String time,
			String img, boolean isMd5) {
		db = info.getWritableDatabase();
		ContentValues values = new ContentValues();
		if (isMd5) {
			z = MD5.encode(z);
		}
		values.put("z", z);
		values.put("name", biaoti);
		values.put("many", neirong);
		values.put("Time", time);
		values.put("img", img);
		long id = db.insert("account", null, values);
		return id;
	}

	/*
	 * 更新笔记内容
	 */
	public int update(String id, String name, String many, String time) {
		db = info.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("many", many);
		values.put("Time", time);
		int number = db.update("account", values, "_id=?", new String[] { id });
		db.close();
		return number;
	}

	/*
	 * 根据Id删除笔记
	 */
	public int delete(long id) {
		db = info.getWritableDatabase();
		int number = db.delete("account", "_id=?", new String[] { id + "" });
		db.close();

		return number;
	}

	/*
	 * 查询全部内容 （现未使用）
	 */
	public List<Person> queryAll() {
		db = info.getReadableDatabase();
		Cursor cursor = db.query("account", null, null, null, null, null,
				"_id ASC");
		List<Person> list = new ArrayList<Person>();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String many = cursor.getString(cursor.getColumnIndex("many"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			list.add(new Person(id, name, many, time));
		}
		cursor.close();
		db.close();
		return list;
	}

	/*
	 * 根据帐号查询它所拥有的笔记
	 */
	public List<Person> queryzhang(String zhanghao) {
		db = info.getReadableDatabase();
		zhanghao = MD5.encode(zhanghao);
		Cursor cursor = db.query("account", null, "z=?",
				new String[] { zhanghao }, null, null, "Time DESC");
		List<Person> list = new ArrayList<Person>();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String many = cursor.getString(cursor.getColumnIndex("many"));
			String time = cursor.getString(cursor.getColumnIndex("Time"));
			String zh = cursor.getString(cursor.getColumnIndex("z"));
			String img = cursor.getString(cursor.getColumnIndex("img"));

			Person p = new Person();
			p.setName(name);
			p.setMany(many);
			p.setId(id);
			p.setTime(time);
			p.setZH(zh);
			p.setImgAddress(img);

			list.add(p);
		}
		cursor.close();
		db.close();
		return list;
	}

	/*
	 * 将List<Person> 的内容全部插入数据库
	 */
	public void AddAll(List<Person> p) {
		db = info.getWritableDatabase();
		ContentValues values;
		for (Person person : p) {
			values = new ContentValues();
			values.put("name", person.getName());
			values.put("many", person.getMany());
			values.put("Time", person.getTime());
			values.put("Z", MD5.encode(person.getZH()));
			db.insert("account", null, values);
		}
		db.close();
	}

	/*
	 * 删除全部数据
	 */
	public void deleteAll() {
		db = info.getWritableDatabase();
		db.delete("account", null, null);
		db.close();
	}
}
