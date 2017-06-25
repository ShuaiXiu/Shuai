package yan.yxs.music.Utils;

import java.util.Random;

import yan.yxs.music.ThreadUtils.ThreadPool;
import yan.yxs.music.ThreadUtils.ThreadPool.ThreadPoolProx;
import yan.yxs.music.bean.Music;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

/**
 * ���� ������
 * 
 * @author �����᲻��
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
	 * ʹ��������������һ��MudiaPlayer
	 */
	public static MediaPlayer newMediaPlayer() {
		return player;
	}

	/*
	 * new��ʱ�����
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
	 * ����
	 */
	public synchronized void play(final Music music) {
		if(sp.getBoolean("isNull", true))return;
		Runnable runn = new Runnable() {
			@Override
			public void run() {
				// ������ ����һֱ��ͬһ�׸�
				sp.edit().putString("url", music.getUrl()).commit();
				try {
					player.reset();
					Thread.sleep(50);
					player.setDataSource(music.getUrl());
					player.prepare();
					player.start();
					/*
					 * ���ݵ�ǰ��ʾ����ȫ�����ֻ�����ϲ�������֣������б��
					 * ʹ������һ��ʱ����is_AllMusic_Playing�жϴ��Ǹ����ݿ���ȡ����
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
	 * ������һ��
	 */
	public void playNext() {
		Runnable runn = new Runnable() {
			
			@Override
			public void run() {
				boolean Random_play = sp.getBoolean("sui", true);
				boolean alone_circulation = sp.getBoolean("dan", false);
				boolean list_circulation = sp.getBoolean("lie", false);
				// �б�ѭ��
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
				} else if (alone_circulation) { // ����ѭ��
					Music music_alone = Alone_circulation(1);
					play(music_alone);
					nextAndup.next_add(music_alone.getUrl());
					sp.edit().putInt("shuai", music_alone.getId()).commit();
				} else if (Random_play) { // �������
					RandomPlay();
				}
				
			}
		};
		createPool.execute(runn);
	}

	/*
	 * ȡĳ����Χ�����
	 */
	private int getRandom(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/*
	 * �����������
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
	 * ������һ��
	 */
	public void playup() {
		Runnable runn = new Runnable() {
			
			@Override
			public void run() {
				boolean Random_play = sp.getBoolean("sui", true);
				boolean alone_circulation = sp.getBoolean("dan", false);
				boolean list_circulation = sp.getBoolean("lie", false);
				if (list_circulation) { // �б�ѭ��
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
				} else if (alone_circulation) { // �����ط�
					Music play_up_alone = Alone_circulation(2);
					play(play_up_alone);
					nextAndup.delete_next(play_up_alone.getUrl());
					sp.edit().putInt("shuai", play_up_alone.getId()).commit();
				} else if (Random_play) { // �������
					Music play_up_Random = nextAndup.find_next();
					if (play_up_Random == null) { // ������ݿ�Ϊ�� ���������
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
	 * �����ط�
	 */
	private Music Alone_circulation(int i) {
		Music music_alone = null;
		if (i == 1) { // ��һ��
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
		} else if (i == 2) {// ��һ��

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
	 * ������һ����ʱ���б�ĩβ��������Խ��
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
	 * ������һ����ʱ���б��״���������Խ��
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
