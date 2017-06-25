package yxs.shuai.app;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Activity的基类
 * 
 * @author Shuai__Xiu
 * 
 */
public class BaseActivity extends Activity implements OnClickListener {
	// Activity集合
	public static List<Activity> list = new ArrayList<Activity>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list.add(this);// 每次初始化将本Activity加入到集合
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
	 * 销毁所有在集合的Activity
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
