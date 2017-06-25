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
 * ������
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
	 * ��ʼ��
	 */
	private void Init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ����û�б�����
		setContentView(R.layout.content_main);
		// ���ز����
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
	 * ��ʼ����Ƭ
	 */
	private void InitFragment() {
		FragmentManager fm = getSupportFragmentManager(); // ���SupportV4�Ĺ�����
		FragmentTransaction transaction = fm.beginTransaction();// ��������

		transaction.replace(R.id.fl_left, new LeftMentFragment(),
				LEFT_MENU_FRAGMENT);// �滻 �����TAG �Ա��Ժ�Բ����ļ�������
		transaction.replace(R.id.fl_main_acti, new ContentFragment(),
				CONTENT_FRAGMENT);

		transaction.commit();// �ύ
	}

	long starttime = 0;

	/*
	 * ˫���˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long endtime = System.currentTimeMillis();
			if (endtime - starttime >= 2000) {
				Toast.makeText(Activity_Main.this, "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				starttime = endtime;
			} else {
				// ����ȫ��
				BaseActivity.FinishAll();
				this.finish();
			}
		}

		return true;
	}

	/*
	 * ���½��� ��ListView����GridView
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
