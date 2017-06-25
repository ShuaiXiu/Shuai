package yxs.shuai.app;

import yxs.shuai.note.R;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function2;
import yxs.shuai.SqlTools.function3;
import yxs.shuai.Utils.MD5;
import yxs.shuai.Utils.SpUtils;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * ��½
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("ShowToast")
public class Activity_Login extends BaseActivity {
	private EditText zhanghao, mima;
	private function2 fun3;
	private RadioButton rad;

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_login);
		zhanghao = (EditText) findViewById(R.id.zhangh);
		mima = (EditText) findViewById(R.id.mima);

		rad = (RadioButton) findViewById(R.id.Btn_Login_Remeberpass);
		fun3 = new function2(this);

		rad.setChecked(true);

		String zh = SpUtils.getString(this, "z", "");

		zhanghao.setText(zh);
		InitListener();
	}

	@Override
	public void InitListener() {
		findViewById(R.id.Btn_Login_Remeberpass).setOnClickListener(this);
		findViewById(R.id.Btn_Login_login).setOnClickListener(this);
		findViewById(R.id.Btn_Login_Forgetpass).setOnClickListener(this);
		findViewById(R.id.Btn_Login_Register).setOnClickListener(this);
	}

	/*
	 * ����¼�
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.Btn_Login_Remeberpass:// ��ס�˺�

			ToastUtils.ShowToast(Activity_Login.this, "��ֻ��װ�Σ���㣡", 500);
			break;
		case R.id.Btn_Login_login: // ��¼

			String zhang = zhanghao.getText().toString().trim();
			String mi = mima.getText().toString().trim();
			SpUtils.putString(this, "z", zhang);

			intent = new Intent(this, Activity_Main.class);
			if (TextUtils.isEmpty(zhang) || TextUtils.isEmpty(mi)) {
				ToastUtils.ShowToast(this, "�û��������벻��Ϊ��", 1000);
			} else if (fun3.find(zhang)) {
				if (MD5.encode(mi).equals(fun3.sels(zhang))) {
					Bundle bundle = new Bundle();
					bundle.putString("zhanghao", zhang);
					intent.putExtras(bundle);

					startActivity(intent);
				} else
					ToastUtils.ShowToast(this, "�������", 1000);

			} else
				ToastUtils.ShowToast(this, "�˺Ŵ���", 1000);
			break;
		case R.id.Btn_Login_Forgetpass: // ��������
			SpUtils.putBoolean(Activity_Login.this, "isLogin", true);
			ShowDialog("������Ҫ�һص��˺�:");
			break;
		case R.id.Btn_Login_Register: // ע��
			intent = new Intent(this, Activity_Register.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/*
	 * �һ�����Ի���
	 */
	public String ShowDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_Login.this);
		final EditText ed = new EditText(Activity_Login.this);
		builder.setTitle(msg);

		builder.setView(ed);
		z = "";
		builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				z = ed.getText().toString();
				SpUtils.putString(Activity_Login.this, "Mz", z);
				function3 fun5 = new function3(Activity_Login.this);
				boolean result = fun5.findisNull();
				if (!result) {
					Toast.makeText(Activity_Login.this, "����û������ܱ�", 1000)
							.show();
				} else {
					Intent intent = new Intent(Activity_Login.this,
							Activity_VerifyMB.class);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		return z;
	}

	long starttime = 0;
	private String z;

	/*
	 * ˫���˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long endtime = System.currentTimeMillis();
			if (endtime - starttime >= 2000) {
				Toast.makeText(Activity_Login.this, "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				starttime = endtime;
			} else
				BaseActivity.FinishAll();
		}

		return true;
	}

}
