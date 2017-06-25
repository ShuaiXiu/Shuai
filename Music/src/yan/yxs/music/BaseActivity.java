package yan.yxs.music;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	private static List<Activity> listActivities = new ArrayList<Activity>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listActivities.add(this);
	}
	public void FinishAll(){
		for (Activity a : listActivities) {
			if(!a.isFinishing()){
				a.finish();
			}
		}
	}
}
