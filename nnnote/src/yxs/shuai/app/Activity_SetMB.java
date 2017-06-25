package yxs.shuai.app;

import yxs.shuai.note.R;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function3;
import yxs.shuai.Utils.SpUtils;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * �ܱ�����
 * 
 * @author Shuai__Xiu
 * 
 */
public class Activity_SetMB extends BaseActivity {
	private EditText daan, wenti, wen_two, da_two;
	private function3 fun4;
	private String zhanghao;

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_setmb);
		ToastUtils.ShowToast(Activity_SetMB.this, "�ܱ�����ֻ�����һ��,�ڼ䲻���޸�,������Ŷ~",
				2000);
		fun4 = new function3(this);
		daan = (EditText) findViewById(R.id.daan);
		wenti = (EditText) findViewById(R.id.wenti22222);

		wen_two = (EditText) findViewById(R.id.wenti_two);
		da_two = (EditText) findViewById(R.id.daan_two);

		zhanghao = SpUtils.getString(Activity_SetMB.this, "z", "");
	}

	@Override
	public void InitListener() {
		findViewById(R.id.Btn_SetMb_addmb).setOnClickListener(this);
		findViewById(R.id.Btn_SetMb_back).setOnClickListener(this);
	}

	/*
	 * �����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_SetMb_addmb: // ����ܱ�
			String da1 = daan.getText().toString();
			String da2 = da_two.getText().toString();
			String wen1 = wenti.getText().toString();
			String wen2 = wen_two.getText().toString();

			if (Jugle(da1, da2, wen1, wen2)) {
				fun4.insert(wen1, da1, zhanghao);
				fun4.insert(wen2, da2, zhanghao);
				fun4.isSetting(zhanghao, "istrue");
			}
			break;

		case R.id.Btn_SetMb_back: // ����
			startActivity(new Intent(Activity_SetMB.this,
					Activity_Setting.class).putExtra("zhanghao", zhanghao));
			this.finish();
			break;

		default:
			break;
		}
	}

	/*
	 * �ж�������Ƿ�Ϊ��
	 */
	private boolean Jugle(String one, String two, String three, String four) {
		boolean a = true;
		if (TextUtils.isEmpty(one)) {
			a = false;
		} else if (TextUtils.isEmpty(two)) {
			a = false;
		} else if (TextUtils.isEmpty(three)) {
			a = false;
		} else if (TextUtils.isEmpty(four)) {
			a = false;
		}
		if (!a) {
			ToastUtils.ShowToast(Activity_SetMB.this, "����𰸲���Ϊ��", 1000);
		} else {
			ToastUtils.ShowToast(Activity_SetMB.this, "��ӳɹ�", 1000);
		}
		return a;
	}
}
