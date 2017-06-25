package yxs.shuai.Fragment;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import yan.yxs.toastutils.ToastUtils;
import yxs.shuai.HttpUtils.HttpRequest;
import yxs.shuai.Utils.NativeBackUpUtils;
import yxs.shuai.Utils.NativeBackUpUtils.OnProgressDialog;
import yxs.shuai.Utils.SpUtils;
import yxs.shuai.Utils.Utils;
import yxs.shuai.app.Activity_Setting;
import yxs.shuai.bean.Person;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import yxs.shuai.note.R;

/**
 * 侧边栏Fragment
 * 
 * @author Shuai__Xiu
 * 
 */
@SuppressLint("HandlerLeak")
public class LeftMentFragment extends BaseFragment implements OnClickListener {
	RadioGroup rad_group;
	RadioButton chk_list, chk_grid;
	private int size = 0;
	private boolean isNull = true;
	private boolean flag = true;
	private static String Address = Environment.getExternalStorageDirectory()
			+ "/shuai/backup/"; // 本地备份地址
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1: // 本地备份更新进度条
				if (pd == null)
					return;
				int backup = (Integer) msg.obj;
				pd.setProgress(backup);
				if (backup == size) {
					pd.dismiss();
					pd.cancel();
					ToastUtils.ShowToast(mActivity, "备份成功", 500);
				}
				break;
			case 2:
				if (pd == null)
					return;
				String message = (String) msg.obj;
				ToastUtils.ShowToast(mActivity, message, 500);
				pd.dismiss();
				pd.cancel();
				break;
			case 3:
				int JsonArraysize = (Integer) msg.obj;
				pd = new ProgressDialog(mActivity);
				pd.setTitle("提示");
				pd.setMessage("正在还原，请等待..");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMax(JsonArraysize);
				size = JsonArraysize;
				pd.show();
				break;
			case 4:// 本地还原更新进度条
				if (pd == null)
					return;
				int restore = (Integer) msg.obj;
				pd.setProgress(restore);
				if (restore == size) {
					pd.dismiss();
					pd.cancel();
					ToastUtils.ShowToast(mActivity, "还原成功", 500);
				}
				break;
			case 5:// 还原过后更新界面
				mCallback.OnUpdateUI();
				break;
			default:
				break;
			}
		};
	};
	private ProgressDialog pd;

	@Override
	public View InitView() {
		View view = View.inflate(mActivity, R.layout.content_leftmenu, null);
		rad_group = (RadioGroup) view.findViewById(R.id.radioGroup);
		chk_list = (RadioButton) view.findViewById(R.id.Rad_btn_List);
		chk_grid = (RadioButton) view.findViewById(R.id.Rad_btn_Grid);
		view.findViewById(R.id.Btn_Net_backUp).setOnClickListener(this);
		view.findViewById(R.id.Btn_Net_HuanY).setOnClickListener(this);
		view.findViewById(R.id.Btn_Native_backUp).setOnClickListener(this);
		view.findViewById(R.id.Btn_Native_HuanY).setOnClickListener(this);
		view.findViewById(R.id.btn_Setting).setOnClickListener(this);

		return view;
	}

	@Override
	public void InitData() {
		int id = 0;
		if (SpUtils.getBoolean(mActivity, "isListView", true)) {
			id = R.id.Rad_btn_List;
		} else {
			id = R.id.Rad_btn_Grid;
		}
		rad_group.check(id);
		rad_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.Rad_btn_List) {
					SpUtils.putBoolean(mActivity, "isListView", true);
					mCallback.OnUpdateUI();
				} else if (checkedId == R.id.Rad_btn_Grid) {
					SpUtils.putBoolean(mActivity, "isListView", false);
					mCallback.OnUpdateUI();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Btn_Net_backUp:// 网络备份
			new Thread() {
				public void run() {
					list = fun2.queryzhang(zhanghao);
					JSONArray JsonArray;
					JsonArray = Utils.ListToJson(list);
					HttpRequest.SocketBackup(JsonArray, mActivity);
				};
			}.start();
			break;

		case R.id.Btn_Net_HuanY: // 网络还原
			new Thread() {
				public void run() {
					List<Person> socketRecover = HttpRequest.SocketRecover();
					if (socketRecover != null) {
						fun2.deleteAll();
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						fun2.AddAll(socketRecover);
					}
				};
			}.start();

			break;

		case R.id.Btn_Native_backUp: // 本地备份
			final List<Person> queryzhang = fun2.queryzhang(zhanghao);
			size = queryzhang.size();
			pd = new ProgressDialog(mActivity);
			pd.setTitle("提示");
			pd.setMessage("正在备份，请等待..");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMax(queryzhang.size());
			pd.show();
			new Thread() {
				public void run() {
					try {
						boolean backUp = NativeBackUpUtils.BackUp(zhanghao,
								queryzhang, new OnProgressDialog() {

									@Override
									public void OnDialog(int i, int size) {
										isNull = false;
										mHandler.obtainMessage(1, i)
												.sendToTarget();
									}
								});
						if (!backUp) {
							if (isNull)
								mHandler.obtainMessage(2, "暂无笔记")
										.sendToTarget();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			break;
		case R.id.Btn_Native_HuanY: // 本地还原
			new Thread() {
				public void run() {
					try {
						size = NativeBackUpUtils.NativeRestore(mActivity,
								Address + zhanghao + ".shuai",
								new OnProgressDialog() {

									@Override
									public void OnDialog(int i, int size) {
										if (flag) {
											flag = false;
											mHandler.obtainMessage(3, size)
													.sendToTarget();
										}
										mHandler.obtainMessage(4, i + 1)
												.sendToTarget();
									}
								});
						if (size == 0) {
							if (isNull)
								mHandler.obtainMessage(2, "暂无备份")
										.sendToTarget();
						}
						flag = true;
						mHandler.sendEmptyMessage(5);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			break;
		case R.id.btn_Setting:
			startActivity(new Intent(mActivity, Activity_Setting.class));
			mActivity.finish();
			break;
		default:
			break;
		}
	}

}
