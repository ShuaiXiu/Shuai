package yan.yxs.music.Utils;

import java.util.Random;

import yan.yxs.music.ThreadUtils.ThreadPool;
import yan.yxs.music.ThreadUtils.ThreadPool.ThreadPoolProx;
import yan.yxs.music.bean.Music;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

/**
 * 播放 工具类
 * 
 * @author 坐怀灬不乱
 * 
 */
public class Play_Utils {
	public static final MediaPlayer player = new MediaPlayer();
	public SQLTools tools;
	public SharedPreferences sp;
	public NextAndUp nextAndup;
	private LoveSql loveSql;
	private ThreadPool threadPoll;
	private ThreadPoolProx createPool;
	/*
	 * 使整个播放器公用一个MudiaPlayer
	 */
	public static MediaPlayer newMediaPlayer() {
		return player;
	}

	/*
	 * new的时候调用
	 */
	public Play_Utils(Context context) {
		sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		threadPoll = ThreadPool.getInstence();
		createPool = threadPoll.createPool();
		tools = new SQLTools(context);
		nextAndup = new NextAndUp(context);
		loveSql = new LoveSql(context);
	}

	/*
	 * 播放
	 */
	public synchronized void play(final Music music) {
		if(sp.getBoolean("isNull", true))return;
		Runnable runn = new Runnable() {
			@Override
			public void run() {
				// 作用是 避免一直点同一首歌
				sp.edit().putString("url", music.getUrl()).commit();
				try {
					player.reset();
					Thread.sleep(50);
					player.setDataSource(music.getUrl());
					player.prepare();
					player.start();
					/*
					 * 根据当前显示的是全部音乐还是我喜欢的音乐，来进行标记
					 * 使播放下一首时根据is_AllMusic_Playing判断从那个数据库中取数据
					 */
					if (music.getIsAll() == 1) {
						sp.edit().putBoolean("is_AllMusic_Playing", true)
								.commit();
					} else if (music.getIsAll() == 2) {
						sp.edit().putBoolean("is_AllMusic_Playing", false)
								.commit();
					}
					sp.edit().putBoolean("isplayer", true).commit();
				} catch (Exception e) {
				}
			}
		};
			createPool.execute(runn);
			}

	/*
	 * 播放下一曲
	 */
	public void playNext() {
		Runnable runn = new Runnable() {
			
			@Override
			public void run() {
				boolean Random_play = sp.getBoolean("sui", true);
				boolean alone_circulation = sp.getBoolean("dan", false);
				boolean list_circulation = sp.getBoolean("lie", false);
				// 列表循环
				if (list_circulation) {
					Music music_lie = null;
					shunLengthalikeNext();
					if (sp.getBoolean("is_AllMusic_Playing", true)) {
						music_lie = tools.findNextUrl(sp.getInt("shuai", 0) + 1);
					} else {
						music_lie = loveSql.findLoveNextUrl(sp.getInt("shuai", 0) + 1);
					}
					play(music_lie);

					nextAndup.next_add(music_lie.getUrl());
					sp.edit().putInt("shuai", music_lie.getId()).commit();
				} else if (alone_circulation) { // 单曲循环
					Music music_alone = Alone_circulation(1);
					play(music_alone);
					nextAndup.next_add(music_alone.getUrl());
					sp.edit().putInt("shuai", music_alone.getId()).commit();
				} else if (Random_play) { // 随机播放
					RandomPlay();
				}
				
			}
		};
		createPool.execute(runn);
	}

	/*
	 * 取某个范围随机数
	 */
	private int getRandom(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/*
	 * 随机播放音乐
	 */
	public void RandomPlay() {
		int random;
		if (sp.getBoolean("is_AllMusic_Playing", true)) {
			random = getRandom(1, sp.getInt("all_music", 0));
		} else {
			random = getRandom(1, sp.getInt("love_music", 0));
		}
		Music music;
		if (sp.getBoolean("is_AllMusic_Playing", true)) {
			music = tools.findUrl(random);
		} else {
			music = loveSql.findLoveNextUrl(random);
		}
		play(music);
		nextAndup.next_add(music.getUrl());
		sp.edit().putInt("shuai", music.getId()).commit();
	}

	/*
	 * 播放上一曲
	 */
	public void playup() {
		Runnable runn = new Runnable() {
			
			@Override
			public void run() {
				boolean Random_play = sp.getBoolean("sui", true);
				boolean alone_circulation = sp.getBoolean("dan", false);
				boolean list_circulation = sp.getBoolean("lie", false);
				if (list_circulation) { // 列表循环
					Music play_up_lie = null;
					shunLengthalikeUp();
					if (sp.getBoolean("is_AllMusic_Playing", true)) {
						play_up_lie = tools.findNextUrl(sp.getInt("shuai", 0) - 1);
					} else {
						play_up_lie = loveSql
								.findLoveNextUrl(sp.getInt("shuai", 0) - 1);
					}

					play(play_up_lie);
					nextAndup.delete_next(play_up_lie.getUrl());

					sp.edit().putInt("shuai", play_up_lie.getId()).commit();
				} else if (alone_circulation) { // 单曲回放
					Music play_up_alone = Alone_circulation(2);
					play(play_up_alone);
					nextAndup.delete_next(play_up_alone.getUrl());
					sp.edit().putInt("shuai", play_up_alone.getId()).commit();
				} else if (Random_play) { // 随机播放
					Music play_up_Random = nextAndup.find_next();
					if (play_up_Random == null) { // 如果数据库为空 就随机播放
						RandomPlay();
						return;
					}
					play(play_up_Random);
					nextAndup.delete_next(play_up_Random.getUrl());
				}
				
			}
		};
		createPool.execute(runn);
	}

	/*
	 * 单曲回放
	 */
	private Music Alone_circulation(int i) {
		Music music_alone = null;
		if (i == 1) { // 下一曲
			shunLengthalikeNext();
			if (sp.getBoolean("is_AllMusic_Playing", true)) {
				if (sp.getBoolean("playnext", false)) {

					music_alone = tools.findUrl(sp.getInt("shuai", 0) + 1);
					sp.edit().putBoolean("playnext", false).commit();
					return music_alone;
				} else {
					music_alone = tools.findUrl(sp.getInt("shuai", 0));
					return music_alone;
				}
			} else {

				if (sp.getBoolean("playnext", false)) {
					music_alone = loveSql
							.findLoveNextUrl(sp.getInt("shuai", 0) + 1);
					sp.edit().putBoolean("playnext", false).commit();
					return music_alone;
				} else {
					music_alone = loveSql
							.findLoveNextUrl(sp.getInt("shuai", 0));
					return music_alone;
				}
			}
		} else if (i == 2) {// 上一曲

			shunLengthalikeUp();
			if (sp.getBoolean("is_AllMusic_Playing", true)) {
				if (sp.getBoolean("playup", false)) {
					music_alone = tools.findUrl(sp.getInt("shuai", 0) - 1);
					sp.edit().putBoolean("playup", false).commit();
					return music_alone;
				} else {
					music_alone = tools.findUrl(sp.getInt("shuai", 0));
					return music_alone;
				}
			} else {
				if (sp.getBoolean("playup", false)) {
					music_alone = loveSql
							.findLoveNextUrl(sp.getInt("shuai", 0) - 1);
					sp.edit().putBoolean("playup", false).commit();
					return music_alone;
				} else {
					music_alone = loveSql
							.findLoveNextUrl(sp.getInt("shuai", 0));
					return music_alone;
				}
			}
		}
		return null;
	}

	/*
	 * 避免下一曲的时候到列表末尾导致数组越界
	 */
	private void shunLengthalikeNext() {
		if (sp.getBoolean("is_AllMusic_Playing", true)) {
			if (sp.getInt("shuai", 0) == sp.getInt("all_music", 0)) {
				sp.edit().putInt("shuai", 0).commit();
			}
		} else {
			if (sp.getInt("shuai", 0) == sp.getInt("love_music", 0)) {
				sp.edit().putInt("shuai", 0).commit();
			}
		}
	}

	/*
	 * 避免上一曲的时候到列表首处导致数组越界
	 */
	private void shunLengthalikeUp() {
		if (sp.getBoolean("is_AllMusic_Playing", true)) {
			if (sp.getInt("shuai", 0) == 1) {
				sp.edit().putInt("shuai", sp.getInt("all_music", 0) + 1)
						.commit();
			}
		} else {
			if (sp.getInt("shuai", 0) == 1) {
				sp.edit().putInt("shuai", sp.getInt("love_music", 0) + 1)
						.commit();
			}
		}
	}

}
