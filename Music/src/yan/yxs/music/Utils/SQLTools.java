package yan.yxs.music.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yan.yxs.music.Sql.Sqlite;
import yan.yxs.music.bean.Music;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 全部歌曲信息数据库的增删改查
 * 
 * @author 坐怀灬不乱
 * 
 */
public class SQLTools {
	private SharedPreferences sp;
	private SQLiteDatabase db;
	private Sqlite sql;
	private Cursor cursor;

	public SQLTools(Context context) {
		sql = new Sqlite(context);
		sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
	}

	public void addMusic(Music music) {
		db = sql.getWritableDatabase();
		int i = sp.getInt("id", 0);
		i += 1;
		ContentValues values = new ContentValues();
		values.put("music_title", music.getMusic_title());
		values.put("music_name", music.getMusic_name());
		values.put("music_url", music.getUrl());
		values.put("isAll", 1);

		values.put("music_time", music.getDuration());
		values.put("myLove", 1);
		values.put("_id", i);
		sp.edit().putInt("id", i).commit();
		if (find(music.getMusic_title())) {
			db.close();
			return;
		}
		db.insert("shuai", null, values);
		db.close();
	}

	public void deleteMusic(String url) {
		db = sql.getWritableDatabase();
		db.delete("shuai", "music_url=?", new String[] { url });
		db.close();
	}

	public void update(String Title, int love) {
		db = sql.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("myLove", love);
		db.update("shuai", values, "music_title=?", new String[] { Title });
		db.close();
	}

	public List<Music> findMusic() {
		db = sql.getWritableDatabase();
		List<Music> list = new ArrayList<Music>();
		cursor = db.query("shuai", null, null, null, null, null,
				"_id ASC");
		Music music;
		while (cursor.moveToNext()) {
			music = new Music();
			int id = cursor.getInt(cursor.getColumnIndex("_id"));

			String title = cursor.getString(cursor
					.getColumnIndex("music_title"));
			String name = cursor.getString(cursor.getColumnIndex("music_name"));
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String duration = cursor.getString(cursor
					.getColumnIndex("music_time"));
			int like = cursor.getInt(cursor.getColumnIndex("myLove"));
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

	private boolean find(String title) {
		db = sql.getReadableDatabase();
		Cursor cursor = db.query("shuai", null, "music_title=?",
				new String[] { title }, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		return result;
	}

	public int findLove(String title) {
		db = sql.getReadableDatabase();
		cursor = db.query("shuai", null, "music_title=?",
				new String[] { title }, null, null, null);
		int like = 0;
		while (cursor.moveToNext()) {
			like = cursor.getInt(cursor.getColumnIndex("myLove"));
		}
		db.close();
		return like;
	}

	public Music findUrl(int i) {
		db = sql.getReadableDatabase();
		cursor = db.query("shuai", null, "id=?",
				new String[] { i + "" }, null, null, null);
		Music music = new Music();
		while (cursor.moveToNext()) {
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String title = cursor.getString(cursor
					.getColumnIndex("music_title"));
			String name = cursor.getString(cursor.getColumnIndex("music_name"));
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			music.setId(id);
			music.setMusic_title(title);
			music.setMusic_name(name);
			music.setUrl(url);
		}
		cursor.close();
		db.close();
		return music;
	}

	public Music findNextUrl(int i) {
		db = sql.getReadableDatabase();
		cursor = db.query("shuai", null, "id=?",
				new String[] { i + "" }, null, null, null);
		Music music = new Music();
		while (cursor.moveToNext()) {
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String duration = cursor.getString(cursor
					.getColumnIndex("music_time"));
			int id = cursor.getInt(cursor.getColumnIndex("id"));
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

	@SuppressLint("SimpleDateFormat")
	public static String FormatTime(int time) {
		long t = Long.valueOf(time);
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		return format.format(new Date(t));
	}

	public synchronized Music findUrltoAllMusic(String isPlaying_url) {
		db = sql.getReadableDatabase();
		cursor = db.query("shuai", null, "music_url=?",
				new String[] { isPlaying_url }, null, null, null);
		Music music = new Music();
		while (cursor.moveToNext()) {
			String url = cursor.getString(cursor.getColumnIndex("music_url"));
			String duration = cursor.getString(cursor
					.getColumnIndex("music_time"));
			int id = cursor.getInt(cursor.getColumnIndex("id"));
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
	public boolean Find(){
		db = sql.getReadableDatabase();
		cursor = db.query("shuai", null, null,
				null, null, null, null);
		boolean moveToNext = cursor.moveToNext();
		return moveToNext;
	}

}
