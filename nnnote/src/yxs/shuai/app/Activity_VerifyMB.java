package yxs.shuai.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yxs.shuai.note.R;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.SqlTools.function3;
import yxs.shuai.Utils.SpUtils;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * ��֤�ܱ�
 * 
 * @author Shuai__Xiu
 * 
 */
public class Activity_VerifyMB extends BaseActivity {
	private function3 fun5;
	private EditText mibaowenti;
	private Spinner sp;
	private List<String> question_list;
	private String question;
	private Map<String, String> map_list;
	private boolean isOK = false;

	/*
	 * ��ʼ��
	 */
	@Override
	public void Init() {
		setContentView(R.layout.activity_verify);
		fun5 = new function3(this);
		mibaowenti = (EditText) findViewById(R.id.mibaowenti);
		sp = (Spinner) findViewById(R.id.Sp_mibao);

		question_list = new ArrayList<String>();
		map_list = new HashMap<String, String>();

		String zh = SpUtils.getString(this, "Mz", "");

		// ���ر��ʺ���Я�����ܱ�����
		if (!zh.equals("")) {
			List<Map<String, String>> list = fun5.findM(zh);
			if (list == null)
				return;
			for (Map<String, String> map : list) {
				for (String s : map.keySet()) {
					question_list.add(s);
					map_list.put(s, map.get(s));
				}
			}
		}
		InitSpinner();
	}

	/*
	 * ��ʼ��������
	 */
	@Override
	public void InitListener() {
		findViewById(R.id.Btn_Mb_verify).setOnClickListener(this);
	}

	// ��ʼ��������
	private void InitSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Activity_VerifyMB.this, android.R.layout.simple_list_item_1,
				question_list);
		sp.setAdapter(adapter);
		sp.setSelection(0);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				question = question_list.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/*
	 * ����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Mb_verify:
			String ques = mibaowenti.getText().toString();
			if (ques.equals(map_list.get(question))) {
				isOK = true;
				startActivity(new Intent(Activity_VerifyMB.this,
						Activity_UpdatePass.class));
			} else {
				ToastUtils.ShowToast(Activity_VerifyMB.this, "�ش����", 1000);
			}
			break;

		default:
			break;
		}
	}

	// �������ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isOK)
				startActivity(new Intent(Activity_VerifyMB.this,
						Activity_Setting.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
