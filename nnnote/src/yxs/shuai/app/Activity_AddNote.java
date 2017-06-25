package yxs.shuai.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.ImgAddressSql;
import yxs.shuai.SqlTools.function;
import yxs.shuai.Utils.NativeBackUpUtils;
import yxs.shuai.Utils.TimeUtils;
import yxs.shuai.Utils.Utils;
import yxs.shuai.View.GroupImageView;
import yxs.shuai.View.GroupImageView.OnUpdateImageView;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import yxs.shuai.note.R;

/**
 * 添加笔记
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint({ "HandlerLeak", "ShowToast" })
public class Activity_AddNote extends BaseActivity {
	private EditText biaoti, text;
	private function fun1;
	private String zhanghao;
	private final int GETIMAGE = 1;
	private final int Camera = 2;
	private LinearLayout lin_img;
	private ImgAddressSql imgTools;
	private String Img_Time = "";
	private String TimeBuffer;
	private List<GroupImageView> imgList;
	private List<String> photoAddress;
	private List<Bitmap> BitList;
	/*
	 * Handler通信
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Bitmap bit = (Bitmap) msg.obj;
				CreateImageView(bit , TimeBuffer);
				TimeBuffer = "";
				break;

			default:
				break;
			}
		};
	};

	/*
	 * 初始化
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_addnote);
		Img_Time = String.valueOf(System.currentTimeMillis());
		biaoti = (EditText) findViewById(R.id.biaoti);
		text = (EditText) findViewById(R.id.wenben);
		fun1 = new function(this);
		imgTools = new ImgAddressSql(this);
		imgList = new ArrayList<GroupImageView>();
		photoAddress = new ArrayList<String>();
		BitList = new ArrayList<Bitmap>();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		zhanghao = bundle.getString("zhanghao");

		lin_img = (LinearLayout) findViewById(R.id.Lin_Addnote_Img);
	}

	/*
	 * 初始化监听器
	 */
	@Override
	public void InitListener() {
		findViewById(R.id.Btn_Addnote_Save).setOnClickListener(this);
		findViewById(R.id.Btn_Addnote_exit).setOnClickListener(this);
		findViewById(R.id.Btn_Addnote_camera).setOnClickListener(this);
		findViewById(R.id.Img_addNote_addphoto).setOnClickListener(this);
	}

	/*
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Addnote_Save:
			String biaoti1 = biaoti.getText().toString();
			String text1 = text.getText().toString();
			String time = TimeUtils.TUtils(System.currentTimeMillis());
			new Thread() {
				public void run() {
					for (int i = 0; i < photoAddress.size(); i++) {
						imgTools.AddAddresss(Img_Time, photoAddress.get(i));
						try {
							Utils.SaveBitmap(BitList.get(i),
									photoAddress.get(i));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}.start();
			if (biaoti1.equals("")) {
				ToastUtils.ShowToast(Activity_AddNote.this, "标题不能为空", 1000);
			} else {
				fun1.addBJ(zhanghao, biaoti1, text1, time, Img_Time, true);
				this.finish();
			}

			break;
		case R.id.Btn_Addnote_exit:
			imgTools.DeleteOne(Img_Time);
			this.finish();

			break;
		case R.id.Btn_Addnote_camera:// 相机
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, Camera);

			break;
		case R.id.Img_addNote_addphoto:// 相册

			if (photoAddress.size() > 3) {
				ToastUtils.ShowToast(Activity_AddNote.this, "只能添加四张图片哦", 1000);
				return;
			}
			Intent Img_intent = new Intent("android.intent.action.GET_CONTENT");

			Img_intent.setType("image/*");

			startActivityForResult(Img_intent, GETIMAGE);

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ContentResolver resolver = getContentResolver();
		String time = String.valueOf(System.currentTimeMillis());
		Bitmap bit = null;
		if (requestCode == GETIMAGE) {
			if (resultCode == RESULT_OK) {

				Uri uri = data.getData();
				try {
					InputStream in = resolver.openInputStream(uri);

					bit = Utils.getBitmap(this, in);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (requestCode == Camera) {
			if (resultCode == RESULT_OK) {
				// 获取照相后返回的图片
				Bundle bundle = data.getExtras();
				bit = (Bitmap) bundle.get("data");

			}
		}
		if(resultCode == RESULT_CANCELED)return ;
		photoAddress.add(time);
		BitList.add(bit);
		TimeBuffer = Environment.getExternalStorageDirectory()
				+ "/shuai/img/"+time+".jpg";
		mHandler.obtainMessage(1, bit).sendToTarget();
	}

	/*
	 * 创建ImageView
	 */
	private void CreateImageView(Bitmap bit , String tag) {
		final GroupImageView img = new GroupImageView(Activity_AddNote.this);
		img.setTag(tag);
		img.InitListener(new OnUpdateImageView() {

			@Override
			public void RemoveImageView() {
				lin_img.removeView(img);
				imgList.remove(img);
				String address = (String) img.getTag();
				NativeBackUpUtils.DeleteFile(address);
				imgTools.DeleteOne(address);
				img.setBitmap(null);
			}
		});
		img.setBitmap(bit);
		lin_img.addView(img);
		imgList.add(img);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Activity_AddNote.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
