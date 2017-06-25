package yxs.shuai.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferenceπ§æﬂ¿‡
 * 
 * @author Shuai__Xiu
 * 
 */
public class SpUtils {
	public static boolean getBoolean(Context c, String name, boolean defaultdata) {
		SharedPreferences sp = c.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		return sp.getBoolean(name, defaultdata);
	}

	public static void putBoolean(Context c, String name, boolean b) {
		SharedPreferences sp = c.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(name, b).commit();
	}

	public static String getString(Context c, String name, String defaultdata) {
		SharedPreferences sp = c.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		return sp.getString(name, defaultdata);
	}

	public static void putString(Context c, String name, String data) {
		SharedPreferences sp = c.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		sp.edit().putString(name, data).commit();
	}
}
