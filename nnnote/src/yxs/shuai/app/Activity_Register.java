package yxs.shuai.app;

import yxs.shuai.note.R;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * ע��
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("HandlerLeak")
public class Activity_Register extends BaseActivity {
	private EditText ZH, MM1, MM2;
	private function2 fun2;
	private boolean flag = true;

	/*
	 * Handler�ֳ�ͨ��
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flag) {
				startActivity(new Intent(Activity_Register.this,
						Activity_Login.class));
			}
		};
	};

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_register);
		fun2 = new function2(this);
		ZH = (EditText) findViewById(R.id.zhangh1);
		MM1 = (EditText) findViewById(R.id.mima1);
		MM2 = (EditText) findViewById(R.id.mima12);
	}

	@Override
	public void InitListener() {
		findViewById(R.id.Btn_Regi_regi).setOnClickListener(this);
		findViewById(R.id.Btn_Regi_back).setOnClickListener(this);
	}

	/*
	 * �����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Regi_regi: // ע��
			String zh = ZH.getText().toString().trim();
			String mm1 = MM1.getText().toString().trim();
			String mm2 = MM2.getText().toString().trim();
			if (TextUtils.isEmpty(zh) || TextUtils.isEmpty(mm1)
					|| TextUtils.isEmpty(mm2)) { // �ж�������Ƿ�Ϊ��
				ToastUtils.ShowToast(Activity_Register.this, "�ı�����Ϊ��", 1000);
			} else {
				if (fun2.find(zh)) {
					ToastUtils.ShowToast(Activity_Register.this, "�û��Ѵ��ڣ����޸�����",
							1000);
				} else {
					if (mm1.length() < 6) {
						ToastUtils.ShowToast(Activity_Register.this,
								"���볤�ȱ������6", 1000);
					} else {
						if (mm1.equals(mm2)) {
							flag = true;
							fun2.insert(zh, mm1);
							ToastUtils.ShowToast(Activity_Register.this,
									"ע��ɹ�,������󷵻ص�½����", 1000);

							mHandler.sendEmptyMessageDelayed(1, 3000);
						} else
							ToastUtils.ShowToast(Activity_Register.this,
									"���벻һ��", 1000);
					}
				}
			}
			break;
		case R.id.Btn_Regi_back:// ����
			flag = false;
			Intent intent = new Intent(this, Activity_Login.class);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}
	}
}
