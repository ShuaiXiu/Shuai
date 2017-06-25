package yxs.shuai.Fragment;

import java.util.List;
import java.util.concurrent.CancellationException;

import yxs.shuai.SqlTools.ImgAddressSql;
import yxs.shuai.SqlTools.function;
import yxs.shuai.Utils.SpUtils;
import yxs.shuai.bean.Person;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment的基类
 * 
 * @author Shuai__Xiu
 * 
 */
public abstract class BaseFragment extends Fragment {
	public Activity mActivity;
	OnFragmentCall mCallback;
	function fun2;
	List<Person> list;
	String zhanghao;
	ImgAddressSql imgTools;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		Init();
	}

	/*
	 * 初始化
	 */
	private void Init() {
		fun2 = new function(mActivity);
		zhanghao = SpUtils.getString(mActivity, "z", "");
	}

	/*
	 * 创建Fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return InitView();
	}

	/*
	 * 所依附的Activity被创建
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		InitData();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnFragmentCall) activity;
		} catch (ClassCastException c) {
			throw new CancellationException(activity.toString() + "必须有实现类");
		}
	}

	/*
	 * 必须实现的方法，加载界面
	 */
	public abstract View InitView();

	/*
	 * 两个Fragment之间通信的接口
	 */
	public interface OnFragmentCall {
		public void OnUpdateUI();
	}

	/*
	 * 初始化数据
	 */
	public void InitData() {
	}
}
