package yan.yxs.music;

import java.util.Timer;
import java.util.TimerTask;

import yan.yxs.music.Utils.GetMusic;
import yan.yxs.music.Utils.Play_Utils;
import yan.yxs.music.Utils.SQLTools;
import yan.yxs.music.bean.Music;
import yan.yxs.toastutils.ToastUtils;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * 闪屏页面 欢迎界面（暂未使用 因启动程序时会有白屏时间 所以设置了WindowBackGround）
 * 
 * @author 坐怀灬不乱
 * 
 */

/*
 * url: String 正在播放的歌曲的路径 isplayer:Boolean 歌曲是否正在播放 all_music: int 全部音乐的长度
 * isOne: Boolean 是否第一次进入应用 shuai: int 正在播放的音乐的ID love_music: int 我喜欢 音乐列表的长度
 * allMusic: Boolean 判断当前是在 全部音乐 还是我喜欢音乐 sui: Boolean 随机播放 dan: Boolean 单曲循环
 * lie: Boolean 列表循环 playup: Boolean 点击了上一曲 不是自动循环的 playnext: Boolean
 * 点击了下一曲不是自动循环的 id: int 添加音乐的时候音乐的ID 自增长 i += 1 is_AllMusic_Playing: Boolean
 * 据此判断下一曲或上一曲的是从那个数据库取歌 isNull:判断播放器是否正确添加歌曲
 */
@SuppressLint("NewApi")
public class Activity_Splash extends BaseActivity {
	private Timer timer;
	private SharedPreferences sp;
	private NotificationManager notifiManager;
	private RemoteViews mRemoteViews;
	private MediaPlayer player;
	private SQLTools sqlTools;
	private Play_Utils playUtils;
	private ReceiveBtnListener mBtnListener;
	private IntentFilter mFilter;
	private Notification mNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitUI();
	}

	private void InitUI() {
		setContentView(R.layout.acitivity_splash);
		InitSp();
		GetMusic.Query(Activity_Splash.this);
		notifiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		player = Play_Utils.newMediaPlayer();
		mRemoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_view);
		sqlTools = new SQLTools(this);
		playUtils = new Play_Utils(this);
		
		mFilter = new IntentFilter();
		mBtnListener = new ReceiveBtnListener();
		mFilter.addAction("shuai.com");
		registerReceiver(mBtnListener, mFilter);
		ShowNitification();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				startActivity(new Intent(Activity_Splash.this,
						MainActivity.class));
			}
		};
		timer = new Timer();
		timer.schedule(task, 0);
	}

	private void InitSp() {
		sp = getSharedPreferences("data", MODE_PRIVATE);
		sp.edit().putString("url", "谷文秀").commit();
		sp.edit().putInt("id", 0).commit();
		sp.edit().putBoolean("isOne", true).commit();
		sp.edit().putBoolean("isplayer", false).commit();
	}

	/*
	 * 显示通知栏菜单
	 */
	private void ShowNitification() {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.baishou);
		builder.setTicker("欢迎使用肖谷音乐~");
		mNotification = builder.build();
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		UpdateUI();
		mRemoteViews.setOnClickPendingIntent(R.id.Img_notifi_finish,
				PendingIntent.getBroadcast(this, 2, new Intent().setAction("shuai.com").putExtra("exit", true), 0));

		mRemoteViews.setOnClickPendingIntent(R.id.Img_notifi_up, PendingIntent
				.getBroadcast(this, 3, new Intent().setAction("shuai.com").putExtra("up", true), 0));

		mRemoteViews.setOnClickPendingIntent(R.id.Img_notifi_play,
				PendingIntent.getBroadcast(this, 4, new Intent().setAction("shuai.com").putExtra("play", true), 0));

		mRemoteViews.setOnClickPendingIntent(R.id.Img_notifi_next,
				PendingIntent.getBroadcast(this, 5, new Intent().setAction("shuai.com").putExtra("next", true), 0));
		mNotification.bigContentView = mRemoteViews;
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pending_gotoMain = PendingIntent.getActivity(this, 1,
				intent, 0);
		mNotification.contentIntent = pending_gotoMain;
		notifiManager.notify(0, mNotification);
	}

	/*
	 * 获取正在播放的歌曲的信息
	 */
	private Music getPlayInfo() {
		String url = sp.getString("url", "");
		return sqlTools.findUrltoAllMusic(url);
	}

	/*
	 * 更新界面
	 */
	public void UpdateUI() {
		if (mRemoteViews == null){
			return;}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sp.getBoolean("isplayer", false)) {
			mRemoteViews.setImageViewResource(R.id.Img_notifi_play,
					R.drawable.stop);
		} else {
			mRemoteViews.setImageViewResource(R.id.Img_notifi_play,
					R.drawable.play);}
		if(sp.getString("url", "").equals("谷文秀")){
			mRemoteViews.setTextViewText(R.id.Tv_notifi_title, "幸福摩天轮");
			mRemoteViews.setTextViewText(R.id.Tv_notifi_name, "谷文秀");
			mNotification.contentView = mRemoteViews;
			notifiManager.notify(0, mNotification);
			return ;
		}
		mRemoteViews.setTextViewText(R.id.Tv_notifi_title, getPlayInfo()
				.getMusic_title());
		mRemoteViews.setTextViewText(R.id.Tv_notifi_name, getPlayInfo()
				.getMusic_name());
		mNotification.contentView = mRemoteViews;
		notifiManager.notify(0, mNotification);
	}

	@Override
	protected void onDestroy() {
		timer.cancel();
		notifiManager.cancel(0);
		unregisterReceiver(mBtnListener);
		super.onDestroy();

	}

	private class ReceiveBtnListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean exit = intent.getBooleanExtra("exit", false);
			boolean up = intent.getBooleanExtra("up", false);
			boolean play = intent.getBooleanExtra("play", false);
			boolean next = intent.getBooleanExtra("next", false);
			boolean update = intent.getBooleanExtra("update", false);
			if(update){
				Activity_Splash.this.UpdateUI();
			}
			if (exit) {
				notifiManager.cancel(0);
				player.stop();
				sp.edit().putBoolean("isplayer", false).commit();
				sendBroadcast(new Intent().setAction("shuai.mainUpdateUI"));
				return ;
			}
			if (up) {
				playUtils.playup();
				sp.edit().putBoolean("isplayer", true).commit();
			}
			if (play) {
				if (!sp.getBoolean("isplayer", false)) {
					if (sp.getBoolean("isOne", true)) { // 如果第一个进入程序 单击播放按钮
														// 会随机播放一首
						if (sp.getBoolean("isNull", true)) {
							ToastUtils.ShowToast(Activity_Splash.this,
									"还没有音乐，快去添加吧！", 500);
							return;
						}
						playUtils.RandomPlay();
					} else {
						player.start();
					}
					sp.edit().putBoolean("isplayer", true).commit();
				} else {
					player.pause();
					sp.edit().putBoolean("isplayer", false).commit();
				}
				sp.edit().putBoolean("isOne", false).commit();
			}
			if (next) {
				playUtils.playNext();
				sp.edit().putBoolean("isplayer", true).commit();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Activity_Splash.this.UpdateUI();
			sendBroadcast(new Intent().setAction("shuai.mainUpdateUI"));
		}

	}
}
