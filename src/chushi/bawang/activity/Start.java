package chushi.bawang.activity;

import example.webbowers.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Start extends Activity {

	String submitData="nosuccess";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		final Animation rotate=AnimationUtils.loadAnimation(this, R.anim.rotate);
		ImageView imageView=(ImageView)findViewById(R.id.imageView1);		
		imageView.startAnimation(rotate);		
		init();
		
	}
 
	
	protected void init() {
		// �ӳ�2500���뷢����Ϣ����Ϣ����
		handler1.sendEmptyMessageDelayed(0, 3000);
	}
	@SuppressLint("HandlerLeak")
	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// ��ת����¼����,��Ĭ�Ϸ�ʽ�رյ�ǰ����
			Intent intent=new Intent(Start.this,Home.class);
//			intent.setAction("android.intent.action.VIEW");
//			intent.addCategory("android.intent.category.DEFAULT");			
			startActivity(intent);			
			Start.this.finish();
		}
	};
	private void startmain() {
		Intent intent=new Intent(Start.this,Home.class);
		startActivity(intent);
		Start.this.finish();
	}
}
