package yxs.shuai.View;

import yxs.shuai.note.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/**
 * ��ɾ����ť��ImageView
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
	 * ��ʼ��
	 */
	private void InitView() {
		View.inflate(getContext(), R.layout.view_groupimageview, this);
		img = (ImageView) findViewById(R.id.View_img);
		img_delete = (ImageView) findViewById(R.id.View_Button_Delete);
	}
	/*
	 * ����ͼƬ
	 */
	public void setBitmap(Bitmap bit){
		img.setImageBitmap(bit);
	}
	
	/*
	 * ��ʼ��ɾ����ť������
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
	 * ���ɾ����ťʱ��Ļص�
	 */
	public interface OnUpdateImageView{
		public void RemoveImageView();
	}
		
	
}