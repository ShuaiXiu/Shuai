package yan.yxs.music.Sql;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库的类
 * 
 * @author 坐怀灬不乱
 * 
 */
public class Sqlite extends SQLiteOpenHelper {
	private static final String name = "YXS.db";
	private static final int version = 1;
	private String sql_name = "create table shuai(id integer primary key autoincrement,"
			+ "_id int,"
			+ " music_title text,"
			+ "music_name text,"
			+ "music_url text," + "music_time text," + "myLove int, "+"isAll int)";
	private String MyLove = "create table Love(_id int,"
			+ "music_title text,"
			+ "music_name text,"
			+ "music_url text,"
			+ "music_time text," + "myLove int , "+"isAll int)";
	private String next = "create table next(_id integer primary key autoincrement,"
			+ "music_url text)";
	private SharedPreferences sp;
	private int i = 0;

	public Sqlite(Context context) {
		super(context, name, null, version);
		sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql_name);
		db.execSQL(MyLove);
		db.execSQL(next);
		sp.edit().putInt("id", i).commit();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
