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
 * Fragment�Ļ���
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
	 * ��ʼ��
	 */
	private void Init() {
		fun2 = new function(mActivity);
		zhanghao = SpUtils.getString(mActivity, "z", "");
	}

	/*
	 * ����Fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return InitView();
	}

	/*
	 * ��������Activity������
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
			throw new CancellationException(activity.toString() + "������ʵ����");
		}
	}

	/*
	 * ����ʵ�ֵķ��������ؽ���
	 */
	public abstract View InitView();

	/*
	 * ����Fragment֮��ͨ�ŵĽӿ�
	 */
	public interface OnFragmentCall {
		public void OnUpdateUI();
	}

	/*
	 * ��ʼ������
	 */
	public void InitData() {
	}
}
