package chushi.bawang.activity;

import java.util.ArrayList;
import java.util.List;

import example.webbowers.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class Welcome extends Activity implements OnPageChangeListener,
		OnTouchListener {
	private ViewPager mViewPager;
	private View view1, view2, view3;
	private List<View> list;
	public boolean flag = false;
	private LinearLayout pointLLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private int lastX = 0;// 获得当前X坐标
	String welText;
	TextView textWelcome;
	//final ShowDialog Dialog = new ShowDialog();
	private boolean isFirstUse;
	private SharedPreferences preferences;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		//textWelcome=(TextView)findViewById(R.id.textwelcome);
		count = pointLLayout.getChildCount();
		// Log.d("aaaa", "" + count);
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);
		LayoutInflater inflater = LayoutInflater.from(Welcome.this);
		list = new ArrayList<View>();
		
		
		SharedPreferences sharedPreferences = this.getSharedPreferences(//得到
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {	
			initpage(inflater);
			//DataUser dataUser = new DataUser();// 更新数据库，添加初始输出
			//dataUser.DataUserFirst(Welcome.this);
			//Dialog.getToast(Welcome.this, "数据初始化完成！",Gravity.BOTTOM,0);
			editor.putBoolean("isFirstRun", false);
			editor.commit();		
		}
			
		
//		SharedPreferences sp = getSharedPreferences("loding", 0);
//		flag = sp.getBoolean("loding_flag", false);
//		if (!flag) {
//			initpage(inflater);
//		} 
			else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Welcome.this, Start.class);
					startActivity(intent);
					finish();
				}
			}, 0);
		}
	}

	@SuppressLint("ResourceAsColor")
	public void initpage(LayoutInflater flater) {
		view1 = flater.inflate(R.layout.loading, null);
		view1.setBackgroundResource(R.drawable.welcomebg_1);	
		view2 = flater.inflate(R.layout.loading, null);
		view2.setBackgroundResource(R.drawable.welcomebg_2);
		view3 = flater.inflate(R.layout.loading, null);
		view3.setBackgroundResource(R.drawable.welcomebg_3);
		list.add(view1);
		list.add(view2);
		list.add(view3);
		mViewPager.setAdapter(pager);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

	PagerAdapter pager = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(list.get(position));

			return list.get(position);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setcurrentPoint(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) arg1.getX();
			break;

		case MotionEvent.ACTION_UP:
			if (!flag) {
				if ((lastX - arg1.getX() > 100)
						&& (mViewPager.getCurrentItem() == mViewPager
								.getAdapter().getCount() - 1)) {// 从最后一页向右滑动
					new Handler().postDelayed(new Runnable() {
						public void run() {														
							SharedPreferences sp = getSharedPreferences(
									"loding", 0);
							boolean isFirstRun = sp.getBoolean("isFirstRun", true);
							SharedPreferences.Editor et = sp.edit();
							et.putBoolean("isFirstRun", true);
							et.commit();
							Intent intent = new Intent(Welcome.this,
									Start.class);
							startActivity(intent);
							finish();
						};
					}, 0);
				}
			}
		}
		return false;
	}
	
		
}
