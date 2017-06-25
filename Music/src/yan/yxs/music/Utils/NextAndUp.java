package yan.yxs.music.Utils;

import yan.yxs.music.Sql.Sqlite;
import yan.yxs.music.bean.Music;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 下一曲上一曲的数据库
 * 
 * @author 坐怀灬不乱
 * 
 */
public class NextAndUp {
	private SQLiteDatabase db;
	private Sqlite sql;
	private Cursor cursor;

	public NextAndUp(Context context) {
		sql = new Sqlite(context);
	}

	public void next_add(String url) {
		db = sql.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("music_url", url);
		db.insert("next", null, values);
		db.close();
	}

	public void deleteAll_next() {
		db = sql.getWritableDatabase();
		db.delete("next", null, null);
		db.close();
	}

	public void delete_next(String url) {
		db = sql.getWritableDatabase();
		db.delete("next", "music_url=?", new String[] { url });
		db.close();
	}

	public Music find_next() {
		db = sql.getReadableDatabase();
		cursor = db.query("next", null, null, null, null, null, null);
		Music music = new Music();
		boolean moveToLast = cursor.moveToLast();
		if(!moveToLast){
			return null;
		}
		String url = cursor.getString(cursor.getColumnIndex("music_url"));
		music.setUrl(url);
	
		cursor.close();
		db.close();
		return music;
	}
	public void Close(){
		if(cursor != null){
			cursor.close();
		}
		if(db != null){
			db.close();
		}
	}
}
