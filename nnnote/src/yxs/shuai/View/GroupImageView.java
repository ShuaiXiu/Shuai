package yxs.shuai.View;

import yxs.shuai.note.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * 带删除按钮的ImageView
 */
public class GroupImageView extends RelativeLayout{
	private ImageView img_delete , img;
	public GroupImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitView();
	}

	public GroupImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView();
	}

	public GroupImageView(Context context) {
		super(context);
		InitView();
	}
	/*
	 * 初始化
	 */
	private void InitView() {
		View.inflate(getContext(), R.layout.view_groupimageview, this);
		img = (ImageView) findViewById(R.id.View_img);
		img_delete = (ImageView) findViewById(R.id.View_Button_Delete);
	}
	/*
	 * 设置图片
	 */
	public void setBitmap(Bitmap bit){
		img.setImageBitmap(bit);
	}
	
	/*
	 * 初始化删除按钮监听器
	 */
	public void InitListener(final OnUpdateImageView update){
		img_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			update.RemoveImageView();	
			}
		});
	}
	
	/*
	 * 点击删除按钮时候的回掉
	 */
	public interface OnUpdateImageView{
		public void RemoveImageView();
	}
		
	
}