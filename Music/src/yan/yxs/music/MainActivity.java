package yan.yxs.music;

import java.util.ArrayList;
import java.util.List;

import yan.yxs.music.Utils.DimFindMusic;
import yan.yxs.music.Utils.LoveSql;
import yan.yxs.music.Utils.NextAndUp;
import yan.yxs.music.Utils.Play_Utils;
import yan.yxs.music.Utils.SQLTools;
import yan.yxs.music.bean.Music;
import yan.yxs.toastutils.ToastUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ҳ��
 * 
 * @author �����᲻��
 * 
 */
@SuppressLint({ "HandlerLeak", "NewApi" })
public class MainActivity extends BaseActivity implements OnClickListener {
	private static final int unlove = 1;// ��ϲ����
	private static final int love = 2;// ϲ����
	private ListView mListView;
	// private ImageView mImageView ;
	private ImageView play , Btn_BackGround;
	private TextView tv_title, tv_name;
	private ListView Edit_ListView;
	private Button btn_All , btn_Love;
	private EditText ed_find;
	private SQLTools tools;
	private LoveSql Love_tools;
	private List<Music> list;
	private List<Music> MyLove;
	private List<Music> findMusci;
	private MyAdapter adapter;
	private SharedPreferences sp;
	private MediaPlayer player;
	private NextAndUp nextAndup;
	private Play_Utils utils;
	private boolean all_music = true;
	private boolean isOne = true;
	private final int UPDATEALLLIST = 1;
	private final int UPDATEMYLOVELIST = 2;
	private int ItemPosition = 0;
	
	private MainUpdateUI mainUpdate ;
	private IntentFilter mFilter;
	
	/*
	 * ��ˢ�½��������
	 */
	private Handler mHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATEALLLIST:
				UpdateList();
				break;
			case UPDATEMYLOVELIST:
				UpdateList();
				break;
			case 3:
				
				break;
			case 4:

				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		InitUI();
	}
	/*
	 * ���ٴλ�ȡ�����ʱ��̬���½���
	 */
	@Override
	protected void onResume() {
		
		mListView.setSelection(ItemPosition);
		UpdataTextView();
		if(sp.getBoolean("isplayer", false)){
			play.setImageResource(R.drawable.stop);
		}else{
			play.setImageResource(R.drawable.play);
		}
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				utils.playNext();
				UpdataTextView();
				sendBroadcast(new Intent().setAction("shuai.com"));
			}
		});
		super.onResume();
	}
	/*
	 * 
	 */
	@Override
	protected void onPause() {
		Love_tools.Close();
		tools.Close();
		
		super.onPause();
	}
	
	/*
	 * ��ʼ������
	 */
	private void InitUI() {
		setContentView(R.layout.activity_main);
		player = Play_Utils.newMediaPlayer();
		mListView = (ListView) findViewById(R.id.lv);
		// mImageView = (ImageView) findViewById(R.id.Music_Img); //��ʱ����
		tv_title = (TextView) findViewById(R.id.Music_title);
		tv_name = (TextView) findViewById(R.id.Music_name);
		play = (ImageView) findViewById(R.id.music_play);
		Btn_BackGround = (ImageView) findViewById(R.id.Img_Btn_BackGround);
		ed_find = (EditText) findViewById(R.id.ED_FindMusic);
		Edit_ListView = (ListView) findViewById(R.id.List_FindMusic);
		sp = getSharedPreferences("data", MODE_PRIVATE);
		
		sp.edit().putBoolean("isplayer", false).commit();
		
		utils = new Play_Utils(MainActivity.this);

		//findViewById(R.id.addMusic).setOnClickListener(this);
		findViewById(R.id.music_play).setOnClickListener(this);
		findViewById(R.id.Lin_Info).setOnClickListener(this);
		btn_All = (Button) findViewById(R.id.Btn_music_all);
		btn_Love = (Button) findViewById(R.id.Btn_music_MyLove);
		btn_All.setOnClickListener(this);
		btn_Love.setOnClickListener(this);

		tools = new SQLTools(MainActivity.this);
		Love_tools = new LoveSql(MainActivity.this);

		nextAndup = new NextAndUp(this);
		nextAndup.deleteAll_next();

		MyLove = new ArrayList<Music>();

		list = tools.findMusic();

		sp.edit().putInt("all_music", list.size()).commit();// �������ʱѡ������б�ѡ��ʹ���Ǹ��б�
		sp.edit().putBoolean("allMusic", true).commit();// ����Ĭ��Adapter���� list

		adapter = new MyAdapter(list , true);

		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new MyOnItemClickListener());
		mListView.setOnItemLongClickListener(new MyOnItemLongListener());
		
		ed_find.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Edit_ListView.setVisibility(View.VISIBLE);
				if(s.toString().isEmpty()){ 
					Edit_ListView.setVisibility(View.GONE);
					return ;}
				findMusci = DimFindMusic.FindMusci(MainActivity.this, s.toString().trim());
				Edit_ListView.setAdapter(new MyAdapter(findMusci, false));
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		Edit_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					Music music = findMusci.get(position);
					ItemPosition = music.getId();
					utils.play(music);
					Edit_ListView.setVisibility(View.GONE);
					ed_find.setText("");
					sp.edit().putBoolean("isOne", false).commit();
					sp.edit().putBoolean("isplayer", true).commit();
					MainActivity.this.onResume();
					sendBroadcast(new Intent().setAction("shuai.com"));
			}
		});
		
		mainUpdate = new MainUpdateUI();
		mFilter = new IntentFilter();
		mFilter.addAction("shuai.mainUpdateUI");
		registerReceiver(mainUpdate, mFilter);
	}
	/*
	 * ListView��������
	 */
	private class MyAdapter extends BaseAdapter {
		List<Music> list;
		boolean isEdite;
		
		public MyAdapter(List<Music> list , boolean isEdit) {
			this.list = list;
			this.isEdite = isEdit;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(isEdite){
			final Music music = list.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(MainActivity.this,
						R.layout.music_item, null);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.isgn_heart);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.item_name);
				viewHolder.title = (TextView) convertView
						.findViewById(R.id.item_title);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.item_time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (music.getLike() == 1) {
				viewHolder.img.setImageResource(R.drawable.xinbian);
			} else if (music.getLike() == 2) {
				viewHolder.img.setImageResource(R.drawable.xin);
			}
			viewHolder.title.setText(music.getMusic_title());
			viewHolder.name.setText(music.getMusic_name());
			viewHolder.time.setText(music.getDuration());
		}else{
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = View.inflate(MainActivity.this, R.layout.findlist_item, null);
				viewHolder.title = (TextView) convertView.findViewById(R.id.FindList_Title);
				viewHolder.name = (TextView) convertView.findViewById(R.id.FindList_Name);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Music music = findMusci.get(position);
			viewHolder.title.setText(music.getMusic_title());
			viewHolder.name.setText(music.getMusic_name());
		}
			return convertView;
			}
	}
	/*
	 * ListView �Ż���
	 */
	class ViewHolder {
		private ImageView img;
		private TextView title;
		private TextView name;
		private TextView time;
	}
	/*
	 * ListView ��Item�����¼�
	 */
	private class MyOnItemLongListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final Music music;
			if (all_music) {
				music = list.get(position);
			} else {
				music = MyLove.get(position);
			}
			
			final int findLove = tools.findLove(music.getMusic_title());
			Builder builder = new Builder(MainActivity.this);
			builder.setTitle("����");
			View layout = View.inflate(MainActivity.this,
					R.layout.builder_item, null);
			builder.setView(layout);
			
			final Button tv = (Button) layout.findViewById(R.id.onclick);
			
			if (findLove == 1) {
				tv.setText("��ӵ���ϲ��");

			} else if (findLove == 2) {
				tv.setText("����ϲ��ɾ��");
			} else {
				Toast.makeText(MainActivity.this, "ִ�д���"+findLove, Toast.LENGTH_SHORT)
						.show();
			}
			final Dialog dialog = builder.create();
			dialog.show();
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (findLove == 1) {
						tools.update(music.getMusic_title(), love);
						Love_tools.addMusic(music);
						mHandle.sendEmptyMessage(UPDATEALLLIST);
						dialog.dismiss();
					} else if (findLove == 2) {
						tools.update(music.getMusic_title(), unlove);
						Love_tools.deleteMusic(music.getUrl());
						mHandle.sendEmptyMessage(UPDATEALLLIST);//UPDATEMYLOVELIST
						dialog.dismiss();
					}

				}
			});
			return true;
		}

	}
	/*
	 * ListView �ĵ����¼�����
	 */
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ItemPosition = position;
			sp.edit().putBoolean("isOne", false).commit();
			Music music;
			if (all_music) {
				music = list.get(position);
			} else {
				music = MyLove.get(position);
			}
			try {
				if (!isOne) {  //����ǵ�һ�ν������  �����������ϴ��˳�ʱ���ŵĸ��� ���²���ʧ��
					if (music.getUrl().equals(sp.getString("url", ""))) {
						return;
					}
				}
				isOne = false;
				utils.play(music);

				tv_title.setText(music.getMusic_title());
				tv_name.setText(music.getMusic_name());
				play.setImageResource(R.drawable.stop);
				sp.edit().putInt("shuai", music.getId()).commit();
				sp.edit().putBoolean("isplayer", false).commit();
				sendBroadcast(new Intent().setAction("shuai.com"));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/*
	 * ��ť�����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.music_play:				//���Ű�ť
			if (!sp.getBoolean("isplayer", false)) {
				if (sp.getBoolean("isOne", true)) {				//�����һ���������  �������Ű�ť ���������һ��
					if(sp.getBoolean("isNull", true)){ 
						ToastUtils.ShowToast(MainActivity.this, "��û�����֣���ȥ��Ӱɣ�", 500);
						break;
						} 
					utils.RandomPlay();
					sp.edit().putBoolean("isOne", false).commit();
					UpdataTextView();
				} else {
					player.start();			
				}
				play.setImageResource(R.drawable.stop);
				sp.edit().putBoolean("isplayer", true).commit();
			} else {
				player.pause();
				play.setImageResource(R.drawable.play);
				sp.edit().putBoolean("isplayer", false).commit();
			}
			sendBroadcast(new Intent().setAction("shuai.com"));
			break;

		case R.id.Lin_Info:					//��ת����ϸ��Ϣ
			startActivity(new Intent(MainActivity.this, Music_Info.class));
			overridePendingTransition(R.anim.trans_y_in, R.anim.trans_y_out);
			break;

		case R.id.Btn_music_all:			//ȫ������  ��ť
			if(!all_music)
				MovetoAll();
			all_music = true;
			sp.edit().putBoolean("allMusic", all_music).commit();
			break;

		case R.id.Btn_music_MyLove:			//��ϲ��  ��ť
			if(all_music)
			MovetoLove();
			all_music = false;
			sp.edit().putInt("love_music", MyLove.size()).commit();
			sp.edit().putBoolean("allMusic", all_music).commit();
			break;
		default:
			break;
		}
	}
	/*
	 * �����ϲ�����������Ķ���
	 */
	private void MovetoLove(){
		TranslateAnimation tran = new TranslateAnimation(0, 700, 0, 0);
		tran.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				Btn_BackGround.setImageResource(R.drawable.btn_back);
				btn_All.setBackground(null);
				Btn_BackGround.setImageResource(R.drawable.btn_back);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Btn_BackGround.setImageBitmap(null);
				btn_Love.setBackgroundResource(R.drawable.btn_back);
				UpdataMyLove();
				adapter.notifyDataSetChanged();
			}
		});
		tran.setDuration(500);
		tran.setFillAfter(true);
		Btn_BackGround.startAnimation(tran);
	}
	/*
	 * ���ȫ�����ָ��������Ķ���
	 */
	private void MovetoAll(){
		TranslateAnimation tran = new TranslateAnimation(700, 0, 0, 0);
		tran.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				Btn_BackGround.setImageResource(R.drawable.btn_back);
				btn_Love.setBackground(null);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Btn_BackGround.setImageBitmap(null);
				btn_All.setBackgroundResource(R.drawable.btn_back);
				UpdateList();
			}
		});
		tran.setDuration(500);
		tran.setFillAfter(true);
		Btn_BackGround.startAnimation(tran);
	}
	
	/*
	 * ���� ȫ������ �б�
	 */
	private void UpdateList() {
		list = tools.findMusic();
		mListView.setAdapter(new MyAdapter(list , true));
		adapter.notifyDataSetChanged();
	}
	/*
	 * ���� ��ϲ�� �б�
	 */
	private void UpdataMyLove() {
		MyLove = Love_tools.findMusic();
		mListView.setAdapter(new MyAdapter(MyLove , true));
	}
	/*
	 * �������ڲ��Ÿ�������Ϣ
	 */
	private void UpdataTextView() {
		if(sp.getString("url", "").equals("������")){
			tv_title.setText("�Ҹ�Ħ����");
			tv_name.setText("������");
			return ;
		}
		Music findNextUrl = tools.findUrltoAllMusic(sp.getString("url", ""));
		tv_title.setText(findNextUrl.getMusic_title());
		tv_name.setText(findNextUrl.getMusic_name());
	}
	/*
	 * �����˳���ʱ���ͷ���Դ
	 */
	@Override
	protected void onDestroy() {
		if(player != null && player.isPlaying()){ 
			player.pause();
			player.release();
		}
		Love_tools.Close();
		tools.Close();
		unregisterReceiver(mainUpdate);
		super.onDestroy();
	}
	/*
	 * �����ֻ�������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(Edit_ListView.getVisibility() == View.VISIBLE){
				Edit_ListView.setVisibility(View.GONE);
				ed_find.setText("");
				return false;
			}else if (player.isPlaying()) {
				moveTaskToBack(true);
				Log.d("tag", "������");
				return false;
			} else {
				FinishAll();
			}
		}
		return false;
	}
	/*
	 * ���½���Ĺ㲥
	 */
	private class MainUpdateUI extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			MainActivity.this.onResume();
		}
		
	}
}
