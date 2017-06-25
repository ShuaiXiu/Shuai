package yan.yxs.music.Utils;

import java.util.ArrayList;
import java.util.List;

import yan.yxs.music.Sql.Sqlite;
import yan.yxs.music.bean.Music;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Ï²»¶µÄ¸èµÄÊý¾Ý¿â
 * 
 * @author ×ø»³ìá²»ÂÒ
 * 
 */
public class LoveSql {
	private SQLiteDatabase db;
	private SharedPreferences sp;
	private Sqlite sql;
	private Cursor cursor;

	public LoveSql(Context context) {
		sql = new Sqlite(context);
		sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
	}

	public void addMusic(Music music) {
		int i = sp.getInt("id", 0);
		i += 1;
		db = sql.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("music_title", music.getMusic_title());
		values.put("music_name", music.getMusic_name());
		values.put("music_url", music.getUrl());
		values.put("music_time", music.getDuration());
		values.put("_id", i);
		values.put("isAll", 2);
		values.put("myLove", 2);
		sp.edit().putInt("id", i).commit();
		db.insert("Love", null, values);
		db.close();
	}

	public void deleteMusic(String url) {
		db = sql.getWritableDatabase();
		int i = sp.getInt("id", 0);
		i -= 1;
		db.delete("Love", "music_url=?", new String[] { url });
		sp.edit().putInt("id", i).commit();
		db.close();
	}

	public List<Music> findMusic() {
		db = sql.getWritableDatabase();
		List<Music> list = new ArrayList<Music>();
		cursor = db.query("Love", null, null, null, null, null,
				"_id ASC");
		Music music;
		while (cursor.moveToNext()) {
			music = new Music();
			String title = cursor.getString(cursor
					.getColumnIndex("music_title"));
			String name = cursor.getString(cursor.getColumnIndex("music_name"));
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String duration = cursor.getString(cursor
					.getColumnIndex("music_time"));
			int like = cursor.getInt(cursor.getColumnIndex("myLove"));
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			int isAll = cursor.getInt(cursor.getColumnIndex("isAll"));
			music.setIsAll(isAll);
			music.setId(id);
			music.setMusic_name(name);
			music.setMusic_title(title);
			music.setUrl(url);
			music.setDuration(duration);
			music.setLike(like);
			list.add(music);
			music = null;
		}
		cursor.close();
		db.close();
		return list;
	}
	public Music findLoveNextUrl(int i) {
		db = sql.getReadableDatabase();
		cursor = db.query("Love", null, "_id=?",
				new String[] { i + "" }, null, null, null);
		Music music = new Music();
		while (cursor.moveToNext()) {
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String duration = cursor.getString(cursor
					.getColumnIndex("music_time"));
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String title = cursor.getString(cursor
					.getColumnIndex("music_title"));
			String name = cursor.getString(cursor.getColumnIndex("music_name"));
			int isAll = cursor.getInt(cursor.getColumnIndex("isAll"));
			
			music.setIsAll(isAll);
			music.setMusic_title(title);
			music.setMusic_name(name);
			music.setId(id);
			music.setUrl(url);
			music.setDuration(duration);
		}
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
