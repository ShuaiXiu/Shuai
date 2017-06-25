package yan.yxs.music;

import yan.yxs.music.ThreadUtils.ThreadPool;
import yan.yxs.music.ThreadUtils.ThreadPool.ThreadPoolProx;
import yan.yxs.music.Utils.Play_Utils;
import yan.yxs.music.Utils.SQLTools;
import yan.yxs.music.bean.Music;
import yan.yxs.toastutils.ToastUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������ϸ��Ϣ��
 * 
 * @author �����᲻��
 * 
 */
@SuppressLint("HandlerLeak")
public class Music_Info extends BaseActivity implements OnClickListener {
	private SharedPreferences sp;
	private final int UpdataSeekBar = 1;
	private final int UpdataTextState = 2;
	private final int UpDATAUI = 3;
	private final int FirstUpdate = 4;
	private final int UpdatePresentTime = 5;
	private TextView tv;
	private TextView run_time, end_time, tv_title, tv_name;
	private SeekBar seekbar;
	private SQLTools tools;
	private boolean isPlay;
	private ImageView img_up, img_center, img_next;
	private MediaPlayer mMediaplayer;
	private Play_Utils utils;
	private int isMUSIC_INFO = 0;
	private boolean sui;
	private boolean dan;
	private boolean lie;
	private boolean isRun = true; // �������˳�ʱֹͣwhileѭ��
	private ThreadPool threadPool;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UpdataSeekBar:
				if (!mMediaplayer.isPlaying())
					break;
				
				int time = (Integer) msg.obj;
				
				seekbar.setProgress(time);
				
				break;
			case UpdataTextState:
				if (sui && !dan && !lie) {
					tv.setText("��");
				} else if (!sui && dan && !lie) {
					tv.setText("��");
				} else if (!sui && !dan && lie) {
					tv.setText("��");
				}
				if (isPlay) {
					img_center.setImageResource(R.drawable.stop);
				} else {
					img_center.setImageResource(R.drawable.play);
				}
				break;
			case UpDATAUI:
				Music music = (Music) msg.obj;
				if (isMUSIC_INFO == 1) {
					sp.edit().putBoolean("isplayer", true).commit();
					img_center.setImageResource(R.drawable.stop);
					isMUSIC_INFO = 0;
				}
				seekbar.setMax(Integer.valueOf(music.getDuration()));
				tv_title.setText(music.getMusic_title());
				tv_name.setText(music.getMusic_name());
				end_time.setText(SQLTools.FormatTime(Integer.valueOf(music
						.getDuration())) + "");
				break;
			case FirstUpdate:
				if (sp.getBoolean("isOne", true)) {
					tv_title.setText("�Ҹ�Ħ����");
					tv_name.setText("������");
					return;
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = sp.getString("url", "");
				Music findNextUrl = tools.findUrltoAllMusic(url);
				mHandler.obtainMessage(UpDATAUI, findNextUrl).sendToTarget();
				break;
			case UpdatePresentTime:
				int Present = (Integer) msg.obj;
				run_time.setText(SQLTools.FormatTime(Present));
				break;
			default:
				break;
			}
		};
	};
	private ThreadPoolProx createPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitUI();
	}

	/*
	 * ��ʼ������
	 */
	@SuppressLint("CutPasteId")
	private void InitUI() {
		setContentView(R.layout.activity_lin);
		mMediaplayer = Play_Utils.newMediaPlayer();
		seekbar = (SeekBar) findViewById(R.id.SeekBar);
		threadPool = ThreadPool.getInstence();
		createPool = threadPool.createPool();
		initState();
		
	}

	/*
	 * ����������Ϣ��ʼ�� ������Ϣ ����ģʽ
	 */
	private void initState() {
		Runnable runn = new Runnable() {
			@Override
			public void run() {

				sp = getSharedPreferences("data", MODE_PRIVATE);
				run_time = (TextView) findViewById(R.id.Tv_run_time);
				end_time = (TextView) findViewById(R.id.Tv_end_time);
				tv = (TextView) findViewById(R.id.circulation);
				tv_title = (TextView) findViewById(R.id.Tv_music_title);
				tv_name = (TextView) findViewById(R.id.Tv_music_name);
				img_up = (ImageView) findViewById(R.id.left);
				img_center = (ImageView) findViewById(R.id.centre);
				img_next = (ImageView) findViewById(R.id.right);

				tv.setOnClickListener(Music_Info.this);
				img_up.setOnClickListener(Music_Info.this);
				img_center.setOnClickListener(Music_Info.this);
				img_next.setOnClickListener(Music_Info.this);
				findViewById(R.id.exit).setOnClickListener(Music_Info.this);

				tools = new SQLTools(Music_Info.this);
				utils = new Play_Utils(Music_Info.this);

				sui = sp.getBoolean("sui", true);
				dan = sp.getBoolean("dan", false);
				lie = sp.getBoolean("lie", false);
				isPlay = sp.getBoolean("isplayer", false);
				mHandler.sendEmptyMessage(UpdataTextState);
				UpdateSeekBar();
			}
		};
		createPool.execute(runn);
	}

	/*
	 * ��ť����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.circulation: // ���Ĳ���ģʽ
			if (tv.getText().equals("��")) {
				tv.setText("��");
				sp.edit().putBoolean("sui", true).commit();
				sp.edit().putBoolean("dan", false).commit();
				sp.edit().putBoolean("lie", false).commit();
				ToastUtils.ShowToast(this, "�������", Toast.LENGTH_SHORT);
			} else if (tv.getText().equals("��")) {
				tv.setText("��");
				sp.edit().putBoolean("sui", false).commit();
				sp.edit().putBoolean("dan", true).commit();
				sp.edit().putBoolean("lie", false).commit();
				ToastUtils.ShowToast(this, "����ѭ��", Toast.LENGTH_SHORT);
			} else if (tv.getText().equals("��")) {
				tv.setText("��");
				sp.edit().putBoolean("sui", false).commit();
				sp.edit().putBoolean("dan", false).commit();
				sp.edit().putBoolean("lie", true).commit();
				ToastUtils.ShowToast(this, "�б�ѭ��", Toast.LENGTH_SHORT);
			}
			break;
		case R.id.exit: // �˳�
			startActivity(new Intent(Music_Info.this, MainActivity.class));
			overridePendingTransition(R.anim.trans_ypross_out,
					R.anim.trans_ypross_in);
			break;
		case R.id.left: // ��һ��
			if (sp.getBoolean("dan", false)) {
				sp.edit().putBoolean("playup", true).commit();
			}
			isMUSIC_INFO = 1;
			utils.playup();
			sp.edit().putBoolean("isplayer", true).commit();
			sp.edit().putBoolean("isOne", false).commit();
			UpdataUI();
			break;
		case R.id.centre: // ��ͣ
			if (sp.getBoolean("isOne", true)) {
				utils.RandomPlay();
				sp.edit().putBoolean("isOne", false).commit();
				sp.edit().putBoolean("isplayer", true).commit();
				img_center.setImageResource(R.drawable.stop);
				UpdataUI();
				break;
			}
			if (!sp.getBoolean("isplayer", false)) {
				mMediaplayer.start();
				img_center.setImageResource(R.drawable.stop);
				sp.edit().putBoolean("isplayer", true).commit();
			} else {
				mMediaplayer.pause();
				img_center.setImageResource(R.drawable.play);
				sp.edit().putBoolean("isplayer", false).commit();
			}
			UpdataUI();
			break;
		case R.id.right: // ��һ��
			if (sp.getBoolean("dan", false)) {
				sp.edit().putBoolean("playnext", true).commit();
			}
			isMUSIC_INFO = 1;
			utils.playNext();
			sp.edit().putBoolean("isplayer", true).commit();
			sp.edit().putBoolean("isOne", false).commit();
			UpdataUI();
			break;

		default:

			break;
		}
	}

	/*
	 * �������ڲ��ŵĸ�������Ϣ
	 */
	private void UpdataUI() {
		Sleep(100);
		mHandler.sendEmptyMessage(FirstUpdate);
		sendBroadcast(new Intent().setAction("shuai.com"));
	}

	/*
	 * ÿ�λ�ȡ�����ʱ��̬��ȡ������Ϣ
	 */
	@Override
	protected void onResume() {
		mMediaplayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (sp.getBoolean("isOne", true))
					return;
				utils.playNext();
				UpdataUI();
			}
		});
		UpdataUI();
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mMediaplayer.seekTo(seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mHandler.obtainMessage(UpdatePresentTime, progress)
						.sendToTarget();
			}
		});
		super.onResume();
	}

	/*
	 * ���½�����
	 */
	private void UpdateSeekBar() {
		Runnable runn = new Runnable() {

			@Override
			public void run() {
				while (isRun) {
					if (mMediaplayer.isPlaying()) {
						mHandler.obtainMessage(UpdataSeekBar,
								mMediaplayer.getCurrentPosition())
								.sendToTarget();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		createPool.execute(runn);
	}

	/*
	 * �˳�ʱ����
	 */
	@Override
	protected void onStop() {
		super.onStop();
		isRun = false;
	}

	/*
	 * �����ٵ�ʱ�����
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRun = false;
		tools.Close();
	}
	
	public void Sleep(long l){
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(Music_Info.this, MainActivity.class));
			overridePendingTransition(R.anim.trans_ypross_out,
					R.anim.trans_ypross_in);
		}
		return super.onKeyDown(keyCode, event);
	}
}