package yxs.shuai.HttpUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.Utils.Utils;
import yxs.shuai.bean.Person;

import android.content.Context;

/**
 * Http引擎类
 * 
 * @author Shuai__Xiu
 * 
 */
public class HttpRequest {
	private static final String Address = "http://192.168.0.122:8080/shuai.json";
	private static Socket mSocket = null;

	private static void getSocket() {
		if (mSocket == null) {
			try {
				mSocket = new Socket("192.168.0.125", 22222);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean BackUpNote(JSONArray ArrayJson) {
		boolean isOK = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(Address)
					.openConnection();

			conn.setRequestMethod("GET");
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.connect();

			PrintWriter writer = new PrintWriter(conn.getOutputStream());
			writer.write(ArrayJson.toString());
			writer.flush();

			int response = conn.getResponseCode();
			if (response == 200) {
				isOK = true;
			}
			writer.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isOK;
	}

	public static String RecoverNote() {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(Address)
					.openConnection();
			conn.connect();

			InputStream in = conn.getInputStream();
			ByteArrayOutputStream out = HttpRequest.resolve(in);
			in.close();
			out.close();
			return out.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Socket的方式备份
	 */
	public static void SocketBackup(JSONArray ArrayJson, Context c) {
		try {
			getSocket();
			PrintWriter writer = new PrintWriter(mSocket.getOutputStream());
			writer.write(ArrayJson.toString());
			writer.flush();

			ByteArrayOutputStream out = HttpRequest.resolve(mSocket
					.getInputStream());
			 if(out.toString().equals("备份成功")){
			 ToastUtils.ShowToast(c, "备份成功", 0);
			 }else{
			 ToastUtils.ShowToast(c, "备份失败", 0);
			 }
			 writer.close();
			 out.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Socket的方式恢复数据
	 */
	public static List<Person> SocketRecover() {
		try {
			getSocket();
			OutputStream output = mSocket.getOutputStream();
			output.write("恢复数据".getBytes());
			output.flush();
			InputStream in = mSocket.getInputStream();
			ByteArrayOutputStream Byteout = resolve(in);
			List<Person> jsonToList = Utils.JsonToList(Byteout.toString());
			return jsonToList;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 解析返回流的方法
	 */
	public static ByteArrayOutputStream resolve(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int a = -1;
		byte[] b = new byte[10240];
		try {
			if ((a = in.read(b, 0, 10240)) != -1) {
				out.write(b, 0, a);
			}
			out.flush();
			out.close();
			return out;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
