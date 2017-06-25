package yxs.shuai.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import yxs.shuai.note.R;

/**
 * 修改笔记
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("HandlerLeak")
public class Activity_UpdataNote extends BaseActivity {
	private static final int GETIMAGE = 0;
	private static final int CAMERA = 1;
	private function fun7;
	private TextView btTV, nrTV;
	private String id, imgAddress;
	private ImgAddressSql imgTools;
	private LinearLayout lin_img;
	private List<Bitmap> bitList;
	private List<String> ImgAddress;
	private Map<String, Bitmap> AddressBitmap; // 图片缓存
	/*
	 * Handler线程通信 
	 */
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				AddressBitmap = ((Map<String, Bitmap>) msg.obj);
				for (String str : AddressBitmap.keySet()) {
					CreateImageView(AddressBitmap.get(str), str);
				}
				AddressBitmap.clear();
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
		setContentView(R.layout.activity_updatenote);
		fun7 = new function(this);
		btTV = (TextView) findViewById(R.id.Updatebiaoti);
		nrTV = (TextView) findViewById(R.id.Updatewenben);
		lin_img = (LinearLayout) findViewById(R.id.Lin_Updata_Img);
		imgTools = new ImgAddressSql(Activity_UpdataNote.this);

		bitList = new ArrayList<Bitmap>();
		ImgAddress = new ArrayList<String>();
		
		AddressBitmap = new HashMap<String, Bitmap>();

		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		String bt = intent.getStringExtra("bt");
		String nr = intent.getStringExtra("nr");
		imgAddress = intent.getStringExtra("img");

		btTV.setText(bt);
		nrTV.setText(nr);
		try {
			InitImageView();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 初始化按钮监听
	 */
	@Override
	public void InitListener() {
		findViewById(R.id.Btn_UpdateNote_Add).setOnClickListener(this);
		findViewById(R.id.Btn_UpdateNote_Exit).setOnClickListener(this);
		findViewById(R.id.Btn_Updatenote_camera).setOnClickListener(this);
		findViewById(R.id.Img_updateNote_addphoto).setOnClickListener(this);
	}

	/*
	 * 加载本条笔记所带有的图片
	 */
	@SuppressLint("NewApi")
	private void InitImageView() throws FileNotFoundException {
		if (!imgAddress.isEmpty()) {
			List<String> imgList = imgTools.findAddress(imgAddress);
			for (String s : imgList) {
				Bitmap bitmap = getBitmap(s);
				CreateImageView(bitmap, s);
			}
			
		}
	}

	/*
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_UpdateNote_Add: // 确定

			final String bt1 = btTV.getText().toString();
			final String nr1 = nrTV.getText().toString();
			final String TimeHour = TimeUtils
					.TUtils(System.currentTimeMillis());
			new Thread() {
				public void run() {
					for (int i = 0; i < ImgAddress.size(); i++) {
						imgTools.AddAddresss(imgAddress, ImgAddress.get(i));
						try {
							Utils.SaveBitmap(bitList.get(i), ImgAddress.get(i));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					fun7.update(id, bt1, nr1, TimeHour);
					Activity_UpdataNote.this.finish();
				};
			}.start();

			break;
		case R.id.Btn_UpdateNote_Exit: // 返回
			this.finish();

			break;
		case R.id.Btn_Updatenote_camera:// 相机
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(camera, CAMERA);

			break;
		case R.id.Img_updateNote_addphoto:// 相册

			if (ImgAddress.size() > 3) {
				ToastUtils.ShowToast(Activity_UpdataNote.this, "只能添加四张图片哦",
						1000);
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

	/*
	 * 跳转相册或相机返回的数据
	 */
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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == CAMERA) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				bit = (Bitmap) bundle.get("data");
			}
		}
		if(resultCode == RESULT_CANCELED)return ;
		bitList.add(bit);
		ImgAddress.add(time);
		AddressBitmap.put(time, bit);
		mHandler.obtainMessage(1, AddressBitmap).sendToTarget();
	}

	/*
	 * 创建ImageView
	 */
	private void CreateImageView(Bitmap bit, String imgAddress) {
		final GroupImageView img = new GroupImageView(Activity_UpdataNote.this);
		img.setBitmap(bit);
		img.setTag(imgAddress);
		img.InitListener(new OnUpdateImageView() {
			@Override
			public void RemoveImageView() {
				img.setBitmap(null);
				lin_img.removeView(img);
				String address = (String) img.getTag();
				imgTools.DeleteOne(address);
				ImgAddress.remove(address);
				NativeBackUpUtils.DeleteFile(address);
			}
		});
		
		lin_img.addView(img);
	}

	/*
	 * 迭代循环获取所有的图片
	 */
	public Bitmap getBitmap(String s) throws FileNotFoundException {
		FileInputStream in = new FileInputStream(new File(s));
		Bitmap bit = BitmapFactory.decodeStream(in);
		return bit;
	}

	/*
	 * 监听返回按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
