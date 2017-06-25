package yxs.shuai.app;

import yxs.shuai.Fragment.BaseFragment.OnFragmentCall;
import yxs.shuai.Fragment.ContentFragment;
import yxs.shuai.Fragment.LeftMentFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import yxs.shuai.note.R;

/**
 * 主界面
 * 
 * @author Shuai__Xiu
 * 
 */
public class Activity_Main extends SlidingFragmentActivity implements
		OnFragmentCall {
	private static final String LEFT_MENU_FRAGMENT = "left_menu_fragment";
	private static final String CONTENT_FRAGMENT = "content_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Init();
	}

	/*
	 * 初始化
	 */
	private void Init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置没有标题栏
		setContentView(R.layout.content_main);
		// 加载侧边栏
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slMenu = getSlidingMenu();
		slMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slMenu.setBehindOffset(200);

		InitFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.OnUpdateUI();
	}

	/*
	 * 初始化碎片
	 */
	private void InitFragment() {
		FragmentManager fm = getSupportFragmentManager(); // 获得SupportV4的管理者
		FragmentTransaction transaction = fm.beginTransaction();// 开启事务

		transaction.replace(R.id.fl_left, new LeftMentFragment(),
				LEFT_MENU_FRAGMENT);// 替换 并添加TAG 以便以后对布局文件做处理
		transaction.replace(R.id.fl_main_acti, new ContentFragment(),
				CONTENT_FRAGMENT);

		transaction.commit();// 提交
	}

	long starttime = 0;

	/*
	 * 双击退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long endtime = System.currentTimeMillis();
			if (endtime - starttime >= 2000) {
				Toast.makeText(Activity_Main.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				starttime = endtime;
			} else {
				// 清理全部
				BaseActivity.FinishAll();
				this.finish();
			}
		}

		return true;
	}

	/*
	 * 更新界面 是ListView还是GridView
	 */
	@Override
	public void OnUpdateUI() {
		ContentFragment content_fragment = (ContentFragment) getSupportFragmentManager()
				.findFragmentByTag("content_fragment");
		if (content_fragment != null) {
			content_fragment.UpdateUI();
		}
	}

}
