package yxs.shuai.Utils;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 格式化时间工具类
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	public static String TUtils(long l) {
		Date date = new Date(l);
		SimpleDateFormat simple = new SimpleDateFormat("yy.MM.dd HH");
		String format = simple.format(date);

		return format;
	}
}
