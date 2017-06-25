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
 * ���ý���
 * 
 * @author Shuai__Xiu
 * 
 */
public class Activity_Setting extends BaseActivity {
	private TextView tv_m;
	private ImageView img_mibao;
	private function3 MbUtils;

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_setting);
		tv_m = (TextView) findViewById(R.id.tv_mSeting);
		img_mibao = (ImageView) findViewById(R.id.Img_Setting_mibao);
		MbUtils = new function3(Activity_Setting.this);
	}

	/*
	 * ��ʼ��������
	 */
	@Override
	public void InitListener() {
		tv_m.setOnClickListener(this);
		findViewById(R.id.Setting_UpdatePassword).setOnClickListener(this);
		findViewById(R.id.Setting_Logon).setOnClickListener(this);
	}

	/*
	 * �ٴλ�ý����ʱ����½���
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if ("istrue".equals(MbUtils.findSetting(SpUtils.getString(
				Activity_Setting.this, "z", "")))) {
			tv_m.setText("�������ܱ�");
			img_mibao.setImageResource(R.drawable.xl);
		}
	}

	/*
	 * �����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_mSeting: // �����ܱ�
			if ("istrue".equals(MbUtils.findSetting(SpUtils.getString(
					Activity_Setting.this, "z", "")))) {
				ToastUtils.ShowToast(Activity_Setting.this, "�ܱ������Ѿ��������޸���",
						1000);
				return;
			}
			startActivity(new Intent(Activity_Setting.this,
					Activity_SetMB.class));
			break;
		case R.id.Setting_UpdatePassword: // �޸�����
			SpUtils.putBoolean(Activity_Setting.this, "isLogin", false);
			SpUtils.putString(Activity_Setting.this, "Mz",
					SpUtils.getString(Activity_Setting.this, "z", ""));
			Intent intent = new Intent(Activity_Setting.this,
					Activity_VerifyMB.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.Setting_Logon: // ��½
			startActivity(new Intent(Activity_Setting.this,
					Activity_Login.class));

			break;

		default:
			break;
		}
	}

	/*
	 * �������ذ�������������������
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
