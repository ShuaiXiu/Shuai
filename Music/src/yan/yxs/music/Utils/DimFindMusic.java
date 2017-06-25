package yan.yxs.music.Utils;

import java.util.ArrayList;
import java.util.List;

import yan.yxs.music.bean.Music;
import android.content.Context;
/**
 * 模糊查询工具类
 * @author Administrator
 *
 */
public class DimFindMusic {
	private static SQLTools tools ;
	private static List<Music> listmusic;
	public synchronized static List<Music> FindMusci(Context c , String msg){
		if(tools == null){
			tools = new SQLTools(c);
		}
		listmusic = new ArrayList<Music>();
		List<Music> findMusic = tools.findMusic();
		for (Music music : findMusic) {
			String music_title = music.getMusic_title();
			 if(music_title.indexOf(msg) != -1){
				 listmusic.add(music);
			 }
		}
		return listmusic;
	}
}
