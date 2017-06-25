package yan.yxs.music.Utils;

import yan.yxs.music.bean.Music;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * ��ȡ���ص��������� ��������һ�������ڵģ�
 * 
 * @����Գ �����᲻��
 * 
 */
public class GetMusic {
	private static SharedPreferences sp;
	public static void Query(Context mContext) {
		// ����ArryList
		// ArrayList<Music> arrayList;
		// ʵ����ArryList����
		// arrayList = new ArrayList<Music>();
		// ����һ��ɨ���α�
		SQLTools sql = new SQLTools(mContext);
		sp = mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
		Cursor c = mContext.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (c != null) {

			// ����Model����
			Music model;
			// ѭ����ȡ
			// ʵ����Model����

			while (c.moveToNext()) {

				model = new Music();
				// ɨ�豾���ļ����õ������������Ϣ
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
				// ����ֵ��Model�ķ�װ����
				model.setMusic_title(music_title);
				model.setMusic_name(music_singer);
				model.setUrl(path);
				model.setDuration(time+"");

				sql.addMusic(model);
				model = null;
				// ��modelֵ���뵽������
				// arrayList.add(model);

			}
			// ��ӡ������ĳ���
			if(!sql.Find()){
				sp.edit().putBoolean("isNull", true).commit();
			}else{
				sp.edit().putBoolean("isNull", false).commit();
			}
		}
	}
}
