package yxs.shuai.app;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Activity�Ļ���
 * 
 * @author Shuai__Xiu
 * 
 */
public class BaseActivity extends Activity implements OnClickListener {
	// Activity����
	public static List<Activity> list = new ArrayList<Activity>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list.add(this);// ÿ�γ�ʼ������Activity���뵽����
		Init();
		InitListener();
	}

	public void Init() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	/*
	 * ���������ڼ��ϵ�Activity
	 */
	public static void FinishAll() {
		for (Activity a : list) {
			if (!a.isFinishing()) {
				a.finish();
			}
		}
	}

	public void InitListener() {
	};

	@Override
	public void onClick(View v) {

	}
}
