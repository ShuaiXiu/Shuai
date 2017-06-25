package yxs.shuai.Fragment;

import java.util.List;

import yxs.shuai.SqlTools.ImgAddressSql;
import yxs.shuai.Utils.NativeBackUpUtils;
import yxs.shuai.Utils.SpUtils;
import yxs.shuai.app.Activity_AddNote;
import yxs.shuai.app.Activity_Main;
import yxs.shuai.app.Activity_UpdataNote;
import yxs.shuai.bean.Person;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import yxs.shuai.note.R;

/**
 * 主界面的Fragment
 * 
 * @author Shuai__Xiu
 * 
 */
public class ContentFragment extends BaseFragment implements OnClickListener {
	ListView lv;
	GridView grid;
	MyAdapter adapter;
	LinearLayout lin;
	Button btn;
	Activity_Main mainActivity;

	@Override
	public View InitView() {
		View view = View.inflate(mActivity, R.layout.activity_main, null);
		lv = (ListView) view.findViewById(R.id.lv);
		grid = (GridView) view.findViewById(R.id.Gird_main);
		view.findViewById(R.id.Btn_menu_show).setOnClickListener(this);
		view.findViewById(R.id.btn_write_note).setOnClickListener(this);
		mainActivity = (Activity_Main) mActivity;
		imgTools = new ImgAddressSql(mainActivity);
		return view;
	}

	@Override
	public void InitData() {
		adapter = new MyAdapter(list);
		list = fun2.queryzhang(zhanghao);
		lv.setOnItemClickListener(new MyOnItemClickListener());
		lv.setOnItemLongClickListener(new MyOnLongClickListener());
		grid.setOnItemClickListener(new MyOnItemClickListener());
		grid.setOnItemLongClickListener(new MyOnLongClickListener());

		lv.setAdapter(NewAdapter(list));
	}

	/*
	 * 适配器
	 */
	private class MyAdapter extends BaseAdapter {
		private List<Person> list;

		public MyAdapter(List<Person> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				holder = new ViewHolder();
				if (SpUtils.getBoolean(mActivity, "isListView", true)) {
					arg1 = View.inflate(mActivity, R.layout.item, null);
					holder.List_Title = (TextView) arg1
							.findViewById(R.id.ListItem_Title);
					holder.List_neirong = (TextView) arg1
							.findViewById(R.id.ListItem_neirong);
					holder.List_Time = (TextView) arg1
							.findViewById(R.id.ListItem_Time);
				} else {
					arg1 = View.inflate(mActivity, R.layout.grid_item, null);
					holder.Grid_Title = (TextView) arg1
							.findViewById(R.id.GridItem_title);
					holder.Grid_Time = (TextView) arg1
							.findViewById(R.id.GridItem_Time);
					holder.Grid_neirong = (TextView) arg1
							.findViewById(R.id.GridItem_neirong);
				}
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			final Person p = list.get(position);
			if (SpUtils.getBoolean(mActivity, "isListView", true)) {
				holder.List_Title.setText(p.getName());
				holder.List_neirong.setText(p.getMany());
				holder.List_Time.setText(p.getTime());
			} else {
				holder.Grid_Title.setText(p.getName());
				holder.Grid_neirong.setText(p.getMany());
				holder.Grid_Time.setText(p.getTime());
			}
			return arg1;

		}

	}

	/*
	 * GetView优化
	 */
	class ViewHolder {
		private TextView List_Title;
		private TextView List_neirong;
		private TextView List_Time;
		private TextView Grid_Title;
		private TextView Grid_neirong;
		private TextView Grid_Time;

	}

	/*
	 * Item单击事件
	 */
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Person b = list.get(arg2);

			String bt = b.getName().toString();
			String nr = b.getMany().toString();
			String id = b.getId().toString();
			Intent intent = new Intent(mActivity, Activity_UpdataNote.class);
			intent.putExtra("id", id);
			intent.putExtra("bt", bt);
			intent.putExtra("nr", nr);

			intent.putExtra("img", b.getImgAddress());
			intent.putExtra("zhanghao", zhanghao);
			startActivity(intent);
		}

	}

	/*
	 * Item长按事件
	 */
	private class MyOnLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position1, long arg3) {
			Person c = list.get(position1);
			deletewenjian(c);
			return true;
		}

		/*
		 * 长按删除Item
		 */
		private void deletewenjian(final Person p) {
			android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					list.remove(p);
					fun2.delete(p.getId());
					List<String> findAddress = imgTools.findAddress(p
							.getImgAddress());
					for (String string : findAddress) {
						NativeBackUpUtils.DeleteFile(string);
					}
					mainActivity.OnUpdateUI();
				}
			};

			Builder builder = new Builder(mActivity);
			builder.setTitle("提示");
			builder.setMessage("是否删除文件?");
			builder.setPositiveButton("删除", listener);
			builder.setNegativeButton("取消", null);
			builder.show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.Btn_menu_show) {
			SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
			slidingMenu.showMenu();
		} else if (v.getId() == R.id.btn_write_note) {
			Intent intent = new Intent(mActivity, Activity_AddNote.class);
			Bundle bundle = new Bundle();
			bundle.putString("zhanghao", zhanghao);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	/*
	 * 更新界面
	 */
	public void UpdateUI() {
		boolean isList = SpUtils.getBoolean(mActivity, "isListView", true);
		list = fun2.queryzhang(zhanghao);
		if (!isList) {
			grid.setVisibility(View.VISIBLE);
			grid.setAdapter(NewAdapter(list));
			lv.setVisibility(View.GONE);
		} else {
			lv.setVisibility(View.VISIBLE);
			lv.setAdapter(NewAdapter(list));
			grid.setVisibility(View.GONE);
		}
		adapter.notifyDataSetChanged();
	}

	/*
	 * 快捷方法 每次都新建Adapter 并更新List
	 */
	public MyAdapter NewAdapter(List<Person> p) {
		MyAdapter adapter = new MyAdapter(list);
		return adapter;
	}

}
