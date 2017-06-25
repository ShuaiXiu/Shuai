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
 * ����ҳ�� ��ӭ���棨��δʹ�� ����������ʱ���а���ʱ�� ����������WindowBackGround��
 * 
 * @author �����᲻��
 * 
 */

/*
 * url: String ���ڲ��ŵĸ�����·�� isplayer:Boolean �����Ƿ����ڲ��� all_music: int ȫ�����ֵĳ���
 * isOne: Boolean �Ƿ��һ�ν���Ӧ�� shuai: int ���ڲ��ŵ����ֵ�ID love_music: int ��ϲ�� �����б�ĳ���
 * allMusic: Boolean �жϵ�ǰ���� ȫ������ ������ϲ������ sui: Boolean ������� dan: Boolean ����ѭ��
 * lie: Boolean �б�ѭ�� playup: Boolean �������һ�� �����Զ�ѭ���� playnext: Boolean
 * �������һ�������Զ�ѭ���� id: int ������ֵ�ʱ�����ֵ�ID ������ i += 1 is_AllMusic_Playing: Boolean
 * �ݴ��ж���һ������һ�����Ǵ��Ǹ����ݿ�ȡ�� isNull:�жϲ������Ƿ���ȷ��Ӹ���
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
		sp.edit().putString("url", "������").commit();
		sp.edit().putInt("id", 0).commit();
		sp.edit().putBoolean("isOne", true).commit();
		sp.edit().putBoolean("isplayer", false).commit();
	}

	/*
	 * ��ʾ֪ͨ���˵�
	 */
	private void ShowNitification() {
		Notification.Builder builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.baishou);
		builder.setTicker("��ӭʹ��Ф������~");
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
	 * ��ȡ���ڲ��ŵĸ�������Ϣ
	 */
	private Music getPlayInfo() {
		String url = sp.getString("url", "");
		return sqlTools.findUrltoAllMusic(url);
	}

	/*
	 * ���½���
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
		if(sp.getString("url", "").equals("������")){
			mRemoteViews.setTextViewText(R.id.Tv_notifi_title, "�Ҹ�Ħ����");
			mRemoteViews.setTextViewText(R.id.Tv_notifi_name, "������");
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
					if (sp.getBoolean("isOne", true)) { // �����һ��������� �������Ű�ť
														// ���������һ��
						if (sp.getBoolean("isNull", true)) {
							ToastUtils.ShowToast(Activity_Splash.this,
									"��û�����֣���ȥ��Ӱɣ�", 500);
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
