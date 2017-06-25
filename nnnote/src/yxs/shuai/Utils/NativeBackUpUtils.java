package yxs.shuai.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import yxs.shuai.SqlTools.function;
import yxs.shuai.bean.Person;

/**
 * 备份的工具类
 * 
 * @author Shuai__Xiu
 * 
 */
public class NativeBackUpUtils {
	private static final String Address = Environment
			.getExternalStorageDirectory() + "/shuai/";
	private static boolean isOk = false;
	private static function NoteUtils;

	/*
	 * 本地备份
	 */
	public static boolean BackUp(String z, final List<Person> list,
			OnProgressDialog update) throws IOException, InterruptedException {
		if (list.size() == 0) {
			return false;
		}
		File f1 = new File(Address);
		if (!f1.exists()) {
			f1.mkdir();
		}
		File f2 = new File(Address + "backup");
		if (!f2.exists()) {
			f2.mkdir();
		}
		File f3 = new File(Address + "backup/" + z + ".shuai");
		if (!f3.exists()) {
			f3.createNewFile();
		}
		final BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(f3));
		JSONArray json = Utils.ListToJson(list);
		out.write("[".getBytes());
		for (int i = 0; i < json.length(); i++) {
			try {
				JSONObject j = (JSONObject) json.get(i);
				out.write(j.toString().getBytes());
				Thread.sleep(50);
				update.OnDialog(i + 1, 0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (i + 1 == json.length())
				continue;
			out.write(",".getBytes());
		}
		out.write("]".getBytes());
		out.flush();
		out.close();
		return isOk;
	}

	public interface OnProgressDialog {
		public void OnDialog(int i, int size);
	}

	/*
	 * 本地还原
	 */
	public static int NativeRestore(Context c, String address,
			OnProgressDialog Callback) throws IOException, JSONException,
			InterruptedException {
		File file = new File(address);
		if (!file.exists()) {
			return 0;
		}
		NoteUtils = new function(c);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file));
		String inputToString = Utils.InputToString(in);
		JSONArray jsonArray = new JSONArray(inputToString);
		for (int i = 0; i < jsonArray.length(); i++) {
			Callback.OnDialog(i, jsonArray.length());
			JSONObject json = (JSONObject) jsonArray.get(i);
			String title = (String) json.getString("title");
			String many = json.getString("many");
			String time = json.getString("time");
			String zhanghao = json.getString("zhanghao");
			String imgAddress = json.getString("imgAddress");
			NoteUtils.addBJ(zhanghao, title, many, time, imgAddress, false);
			Thread.sleep(1000);
		}
		return jsonArray.length();
	}
	/*
	 * 当删除笔记图片的时候同时删除的文件
	 */
	public static void DeleteFile(String address) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/shuai/img");
		if (!file.exists())
			return;
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			if (address.equals(file2.getPath())) {
				file2.delete();
			}
		}
	}

}
