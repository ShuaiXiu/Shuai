package yxs.shuai.app;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function3;
import yxs.shuai.Utils.SpUtils;
import yxs.shuai.note.R;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 设置界面
 * 
 * @author Shuai__Xiu
 * 
 */
public class Activity_Setting extends BaseActivity {
	private TextView tv_m;
	private ImageView img_mibao;
	private function3 MbUtils;

	/*
	 * 初始化
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_setting);
		tv_m = (TextView) findViewById(R.id.tv_mSeting);
		img_mibao = (ImageView) findViewById(R.id.Img_Setting_mibao);
		MbUtils = new function3(Activity_Setting.this);
	}

	/*
	 * 初始化监听器
	 */
	@Override
	public void InitListener() {
		tv_m.setOnClickListener(this);
		findViewById(R.id.Setting_UpdatePassword).setOnClickListener(this);
		findViewById(R.id.Setting_Logon).setOnClickListener(this);
	}

	/*
	 * 再次获得焦点的时候更新界面
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if ("istrue".equals(MbUtils.findSetting(SpUtils.getString(
				Activity_Setting.this, "z", "")))) {
			tv_m.setText("已设置密保");
			img_mibao.setImageResource(R.drawable.xl);
		}
	}

	/*
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_mSeting: // 设置密保
			if ("istrue".equals(MbUtils.findSetting(SpUtils.getString(
					Activity_Setting.this, "z", "")))) {
				ToastUtils.ShowToast(Activity_Setting.this, "密保问题已经不可以修改了",
						1000);
				return;
			}
			startActivity(new Intent(Activity_Setting.this,
					Activity_SetMB.class));
			break;
		case R.id.Setting_UpdatePassword: // 修改密码
			SpUtils.putBoolean(Activity_Setting.this, "isLogin", false);
			SpUtils.putString(Activity_Setting.this, "Mz",
					SpUtils.getString(Activity_Setting.this, "z", ""));
			Intent intent = new Intent(Activity_Setting.this,
					Activity_VerifyMB.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.Setting_Logon: // 登陆
			startActivity(new Intent(Activity_Setting.this,
					Activity_Login.class));

			break;

		default:
			break;
		}
	}

	/*
	 * 监听返回按键，单机返回主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(Activity_Setting.this, Activity_Main.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
