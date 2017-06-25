package yxs.shuai.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * 
 * @author Shuai__Xiu
 * 
 */
public class Info extends SQLiteOpenHelper {
	/*
	 * 笔记 账号 密保问题 图片地址
	 */
	private final String Note = "create table account(_id integer primary key autoincrement,name text,many text,z text,img text,Time text)";
	private final String Number = "create table zhanghao(_id integer primary key autoincrement , z text , m text )";
	private final String Security = "create table mb(_id integer primary key autoincrement , wenti text , daan text , z text , isSetting text)";
	private final String ImgAddress = "create table img(id text , address text)";

	public Info(Context context) {
		super(context, "person.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(Note);
		db.execSQL(Number);
		db.execSQL(Security);
		db.execSQL(ImgAddress);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
