package yan.yxs.music.Utils;

import yan.yxs.music.bean.Music;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * 获取本地的所有音乐 （不包括一分钟以内的）
 * 
 * @程序猿 坐怀灬不乱
 * 
 */
public class GetMusic {
	private static SharedPreferences sp;
	public static void Query(Context mContext) {
		// 创建ArryList
		// ArrayList<Music> arrayList;
		// 实例化ArryList对象
		// arrayList = new ArrayList<Music>();
		// 创建一个扫描游标
		SQLTools sql = new SQLTools(mContext);
		sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
		Cursor c = mContext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (c != null) {

			// 创建Model对象
			Music model;
			// 循环读取
			// 实例化Model对象

			while (c.moveToNext()) {

				model = new Music();
				// 扫描本地文件，得到歌曲的相关信息
				String music_title = c.getString(c
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String music_singer = c.getString(c
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				String path = c.getString(c
						.getColumnIndex(MediaStore.Audio.Media.DATA));
				int time = c.getInt(c
						.getColumnIndex(MediaStore.Audio.Media.DURATION));
				if (time < 60000) {
					continue;
				}
				// 设置值到Model的封装类中
				model.setMusic_title(music_title);
				model.setMusic_name(music_singer);
				model.setUrl(path);
				model.setDuration(time+"");

				sql.addMusic(model);
				model = null;
				// 将model值加入到数组中
				// arrayList.add(model);

			}
			// 打印出数组的长度
			if(!sql.Find()){
				sp.edit().putBoolean("isNull", true).commit();
			}else{
				sp.edit().putBoolean("isNull", false).commit();
			}
		}
	}
}
