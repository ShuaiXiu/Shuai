package yxs.shuai.app;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function2;
import yxs.shuai.Utils.SpUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import yxs.shuai.note.R;

/**
 * �޸�����
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("ShowToast")
public class Activity_UpdatePass extends BaseActivity {
	private EditText MM3, MM4;
	private function2 fun2;
	private boolean isOk = false;

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_updatepass);
		fun2 = new function2(this);

		MM3 = (EditText) findViewById(R.id.mima2);
		MM4 = (EditText) findViewById(R.id.mima22);
	}

	/*
	 * ��ʼ��������
	 */
	@Override
	public void InitListener() {
		findViewById(R.id.Btn_Updatepass_update).setOnClickListener(this);
		findViewById(R.id.Btn_Updatepass_back).setOnClickListener(this);
	}

	/*
	 * ��ť�����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Updatepass_update: // ����

			String zh = SpUtils.getString(Activity_UpdatePass.this, "Mz", "");
			String mm1 = MM3.getText().toString().trim();
			String mm2 = MM4.getText().toString().trim();
			if (TextUtils.isEmpty(zh) || TextUtils.isEmpty(mm1)
					|| TextUtils.isEmpty(mm2)) {
				ToastUtils.ShowToast(Activity_UpdatePass.this, "�ı�����Ϊ��", 500);
			} else {
				if (mm1.length() < 6) {
					ToastUtils.ShowToast(Activity_UpdatePass.this, "���볤�ȱ������6",
							500);
				} else {
					if (mm1.equals(mm2)) {
						if (fun2.find(zh)) {
							isOk = true;
							fun2.update(zh, mm1);
							ToastUtils.ShowToast(Activity_UpdatePass.this,
									"�޸ĳɹ�", 500);
						} else
							ToastUtils.ShowToast(Activity_UpdatePass.this,
									"�û�������", 500);

					} else
						ToastUtils.ShowToast(Activity_UpdatePass.this, "���벻һ��",
								500);
				}

			}

			break;
		case R.id.Btn_Updatepass_back:// ����
			if (isOk) {
				startActivity(new Intent(this, Activity_Login.class));
				ToastUtils.ShowToast(Activity_UpdatePass.this, "�����µ�¼", 500);
			} else{
				if(SpUtils.getBoolean(Activity_UpdatePass.this, "isLogin", true))
					startActivity(new Intent(this, Activity_Login.class));
				else
				startActivity(new Intent(this, Activity_Main.class));
			}
			break;

		default:
			break;
		}
	}

	/*
	 * �������ؼ�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOk) {
				startActivity(new Intent(this, Activity_Login.class));
				ToastUtils.ShowToast(Activity_UpdatePass.this, "�����µ�¼", 500);
			} else{
				if(SpUtils.getBoolean(Activity_UpdatePass.this, "isLogin", true))
					startActivity(new Intent(this, Activity_Login.class));
				else
				startActivity(new Intent(this, Activity_Main.class));
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
