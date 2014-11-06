package chushi.bawang.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chushi.bawang.database.SQLiteHelper;
import chushi.bawang.pages.Bookmark;
import chushi.bawang.pages.History;
import chushi.bawang.util.SyncLight;
import example.webbowers.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Home extends Activity {
	private static final int SYS_MASSAGE = 0;
	private static final int BACK = 1;
	private static final int SETTINGS = 2;
	private static final int HISTORY = 3;
	private static final int BOOKMARK = 4;
	private static final int FILETEST = 5;
	private static final int FINSHED = 6;
	private static final int LIGHTSET = 7;
	private static final int ABOUTINFO = 8;
	private static Boolean isExit = false;
	private EditText input_url;
	private Button visitBtn;
	public GridView gridView;
	private SimpleAdapter adapter;
	private SharedPreferences preferences = null;
	private static String search_engine = Settings.search_engine_baidu;
	private ImageButton menu_ImageBtn_back = null;
	private ImageButton menu_ImageBtn_refresh = null;
	private ImageButton menu_ImageBtn_menu = null;
	private ImageButton menu_ImageBtn_multiwins = null;
	private ImageButton menu_ImageBtn_home = null;
	private View add_index_dialog_view = null;
	private EditText add_webname = null;
	private EditText add_weburl = null;
	private SQLiteHelper helper = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_index);
		SyncLight syncLight = new SyncLight();
		syncLight.syncLight(Home.this);

		input_url = (EditText) findViewById(R.id.home_url_input);
		// input_url.requestFocus();
		// input_url.setSelectAllOnFocus(false);
		visitBtn = (Button) findViewById(R.id.home_visit_button);
		gridView = (GridView) findViewById(R.id.index_girdview);
		add_index_dialog_view = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.add_index_dialog, null);
		add_webname = (EditText) add_index_dialog_view
				.findViewById(R.id.add_webname);
		add_weburl = (EditText) add_index_dialog_view
				.findViewById(R.id.add_weburl);
		helper = new SQLiteHelper(getApplicationContext());
		preferences = getSharedPreferences(Settings.PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);
		menu_ImageBtn_back = (ImageButton) findViewById(R.id.menu_imagebtn_back);
		menu_ImageBtn_back.setOnClickListener(new myMenuClickListener());
		menu_ImageBtn_refresh = (ImageButton) findViewById(R.id.menu_imagebtn_refresh);
		menu_ImageBtn_refresh.setOnClickListener(new myMenuClickListener());
		menu_ImageBtn_menu = (ImageButton) findViewById(R.id.menu_imagebtn_menu);
		menu_ImageBtn_menu.setOnClickListener(new myMenuClickListener());
		menu_ImageBtn_home = (ImageButton) findViewById(R.id.menu_imagebtn_home);
		menu_ImageBtn_home.setOnClickListener(new myMenuClickListener());
		ArrayList<HashMap<String, Object>> ItemList = new ArrayList<HashMap<String, Object>>();
		input_url.setOnKeyListener(new OnKeyListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) { // 如果为回车键
					if (input_url.getText().length() > 0) {
						String url = input_url.getText().toString();
						System.out.println(url);
						String standard = "^[http://www.|www.][\\S]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$";
						Pattern pattern = Pattern.compile(standard);
						Matcher match = pattern.matcher(url);
						if (url.isEmpty()) {
							Toast.makeText(Home.this, "你还没有输入任何字符~", 1).show();
						} else {

							if (match.find()) {
								MainView.instance.cur_url = "http://" + url;
								Log.e("isurl", "yes");
							} else {
								Log.e("isurl", "no");
								MainView.instance.cur_url = search_engine + url;
							}
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), MainView.class);
						startActivity(intent);
						Home.this.finish();
						}
						return true;
					} else {
						Toast.makeText(Home.this, "你还未输入任何字符~", 0).show();
					}
				}
				return false;
			}
			
		});
		visitBtn.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			public void onClick(View v) {
				String url = input_url.getText().toString();
				switch (preferences.getInt(Settings.KEY_SEARCH_ENGINE, 0)) {
				case 0:
					search_engine = Settings.search_engine_baidu;
					break;
				case 1:
					search_engine = Settings.search_engine_360;
					break;
				case 2:
					search_engine = Settings.search_engine_soso;
					break;
				}
				String standard = "^[http://www.|www.][\\S]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$";
				Pattern pattern = Pattern.compile(standard);
				Matcher match = pattern.matcher(url);
				if (url.isEmpty()) {
					Toast.makeText(Home.this, "你还没有输入任何字符~", 1).show();
				} else {

					if (match.find()) {
						MainView.instance.cur_url = "http://" + url;
						Log.e("isurl", "yes");
					} else {
						Log.e("isurl", "no");
						MainView.instance.cur_url = search_engine + url;
					}
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainView.class);
				startActivity(intent);
				Home.this.finish();
				}
			}
		});
		// use grid_view and sipleAdapter to create the view
		HashMap<String, Object> map_0 = new HashMap<String, Object>();
		map_0.put("Image", R.drawable.google);
		map_0.put("Tag", "google");
		ItemList.add(map_0);

		HashMap<String, Object> map_1 = new HashMap<String, Object>();
		map_1.put("Image", R.drawable.baidu);
		map_1.put("Tag", "百度");
		ItemList.add(map_1);
		HashMap<String, Object> map_2 = new HashMap<String, Object>();
		map_2.put("Image", R.drawable.qq);
		map_2.put("Tag", "腾讯");
		ItemList.add(map_2);
		HashMap<String, Object> map_3 = new HashMap<String, Object>();
		map_3.put("Image", R.drawable.taobao);
		map_3.put("Tag", "淘宝");
		ItemList.add(map_3);
		HashMap<String, Object> map_4 = new HashMap<String, Object>();
		map_4.put("Image", R.drawable.yahoo);
		map_4.put("Tag", "雅虎");
		ItemList.add(map_4);
		HashMap<String, Object> map_5 = new HashMap<String, Object>();
		map_5.put("Image", R.drawable.wandoujia);
		map_5.put("Tag", "豌豆荚");
		ItemList.add(map_5);
		HashMap<String, Object> map_6 = new HashMap<String, Object>();
		map_6.put("Image", R.drawable.renren);
		map_6.put("Tag", "人人");
		ItemList.add(map_6);
		HashMap<String, Object> map_7 = new HashMap<String, Object>();
		map_7.put("Image", R.drawable.sohu);
		map_7.put("Tag", "搜狐");
		ItemList.add(map_7);
		HashMap<String, Object> map_8 = new HashMap<String, Object>();
		map_8.put("Image", R.drawable.rss);
		map_8.put("Tag", "RSS");
		ItemList.add(map_8);
		adapter = new SimpleAdapter(getApplicationContext(), ItemList,
				R.layout.index_item_style, new String[] { "Image", "Tag" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					MainView.instance.cur_url = "http://www.google.com.hk";
					break;
				case 1:
					MainView.instance.cur_url = "http://www.baidu.com";
					break;
				case 2:
					MainView.instance.cur_url = "http://info.3g.qq.com";
					break;
				case 3:
					MainView.instance.cur_url = "http://www.taobao.com";
					break;
				case 4:
					MainView.instance.cur_url = "http://www.yahoo.com";
					break;
				case 5:
					MainView.instance.cur_url = "http://www.wandoujia.com";
					break;
				case 6:
					MainView.instance.cur_url = "http://www.renren.com";
					break;
				case 7:
					MainView.instance.cur_url = "http://www.sohu.com";
					break;
				// add my rss reader
				case 8:
					MainView.instance.cur_url = "file:///android_asset/index.html";
					break;

				}
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainView.class);
				startActivity(intent);
				Home.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exitBy2Click();

		return false;
	}

	// 设置菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 设置菜单的功能：返回，刷新
		menu.add(0, FILETEST, 0, "文件管理").setIcon(
				android.R.drawable.sym_contact_card);
		menu.add(0, LIGHTSET, 0, "亮度调节").setIcon(
				android.R.drawable.ic_dialog_info);
		menu.add(0, SETTINGS, 0, R.string.settings).setIcon(
				R.drawable.tab_settings);// 设置
		menu.add(0, HISTORY, 0, R.string.menu_history).setIcon(
				android.R.drawable.ic_menu_recent_history);// 历史
		menu.add(0, BOOKMARK, 1, R.string.menu_bookmark).setIcon(
				R.drawable.bookmark);// 书签
		menu.add(0, SYS_MASSAGE, 1, "系统消息").setIcon(
				android.R.drawable.sym_action_chat);
		menu.add(0, ABOUTINFO, 1, "关于").setIcon(R.drawable.about);
		menu.add(0, FINSHED, 1, "退出").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

	// 设置弹出菜单的选项的回调
	@SuppressWarnings("deprecation")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case FILETEST:
			Intent intent_filetest = new Intent();
			intent_filetest.setClass(getApplicationContext(), FileTest.class);
			startActivity(intent_filetest);
			// Home.this.finish();
			break;
		case SYS_MASSAGE:
			Toast.makeText(Home.this, "暂无消息~", 0).show();
			break;
		case SETTINGS:
			Intent intent_settings = new Intent();
			intent_settings.setClass(getApplicationContext(), Settings.class);
			startActivity(intent_settings);
			// Home.this.finish();
			break;
		case HISTORY:
			Intent intent_history = new Intent();
			intent_history.setClass(getApplicationContext(), History.class);
			startActivity(intent_history);
			//Home.this.finish();
			break;
		case BOOKMARK:
			Intent intent_bookmark = new Intent();
			intent_bookmark.setClass(getApplicationContext(), Bookmark.class);
			startActivity(intent_bookmark);
			//Home.this.finish();
			break;
		case FINSHED:
			Home.this.finish();
			finish();
			break;
		case LIGHTSET:
			lightMenu();
			break;
		case ABOUTINFO:
			try {
				PackageManager manager = Home.this.getPackageManager();
				PackageInfo info = manager.getPackageInfo(
						Home.this.getPackageName(), 0);
				String version = info.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date date = new Date(System.currentTimeMillis());
			new AlertDialog.Builder(Home.this)
					.setIcon(R.drawable.about)
					.setMessage(
							"如有意见或建议，请联系QQ邮箱：  1018795480@qq.com\n"
									+ "TEL：18046714505\n"
									+ "软件名："
									+ getResources().getString(
											R.string.app_name) + "\n版本号："
									+ getVersion() + "\t\t\t"
									+ "\t ---20140625_1017\n"+"现在时间："+date.toLocaleString()+"\n\t______________ by baojun\n").create()
					.show();

			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	@SuppressLint("NewApi")
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "can_not_find_version";
		}
	}

	private class myMenuClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.menu_imagebtn_back:
				Intent intent1 = new Intent();
				intent1.setClass(Home.this, MainView.class);
				startActivity(intent1);
				Home.this.finish();
				break;
			case R.id.menu_imagebtn_refresh:
				break;
			case R.id.menu_imagebtn_menu:
				openOptionsMenu();
				break;
			case R.id.menu_imagebtn_home:
				break;
			}
		}
		// public void multiwin(){
		// ArrayList<HashMap<String, String>> windowsList = new
		// ArrayList<HashMap<String,String>>();
		// HashMap<String,String> map = new HashMap<String, String>();
		// map.put("WebName", MainView.webView.getTitle());
		// map.put("WebUrl", MainView.webView.getUrl());
		// windowsList.add(map);
		//
		// }

	}

	// 双击退出
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次回到桌面", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			// finish();
			// System.exit(0);
		}

	}

	// 设置夜间模式,需要用线程实现才能实时调节屏幕的亮度
	public void lightMenu() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// Message m=new Message();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// m.what=0x111;
				// mHandler.sendMessage(m);
				gridView.post(new Runnable() {

					@Override
					public void run() {

						final String[] items = new String[] { "夜间", "日间","正常" };
						Builder builder = new AlertDialog.Builder(Home.this);
						builder.setIcon(R.drawable.about);
						builder.setTitle("Please choose list:");
						builder.setItems(items,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Toast.makeText(Home.this,
												"已切换" + items[which] + "模式",
												Toast.LENGTH_SHORT).show();
										switch (which) {
										case 0:
											WindowManager.LayoutParams lp = getWindow()
													.getAttributes();
											lp.screenBrightness = 0.01f;
											getWindow().setAttributes(lp);
											// 设置完毕后，保证每个页面的亮度都一致
											SharedPreferences sp1 = Home.this
													.getSharedPreferences(
															"LIGHTSET",
															MODE_WORLD_READABLE);
											// BWDown=
											// sp.getString("bwdownfilename",
											// BWDown);
											SharedPreferences.Editor et1 = sp1
													.edit();
											et1.putString("light", "0");
											et1.commit();
											break;
										case 1:
											WindowManager.LayoutParams p = getWindow()
													.getAttributes();
											p.screenBrightness =1f;
											getWindow().setAttributes(p);
											// 设置完毕后，保证每个页面的亮度都一致
											SharedPreferences sp2 = Home.this
													.getSharedPreferences(
															"LIGHTSET",
															MODE_WORLD_READABLE);
											// BWDown=
											// sp.getString("bwdownfilename",
											// BWDown);
											SharedPreferences.Editor et2 = sp2
													.edit();
											et2.putString("light", "1");
											et2.commit();
											break;
											case 2:
												WindowManager.LayoutParams p2 = getWindow()
												.getAttributes();
										p2.screenBrightness = 0.5f;
										getWindow().setAttributes(p2);
										// 设置完毕后，保证每个页面的亮度都一致
										SharedPreferences sp3 = Home.this
												.getSharedPreferences(
														"LIGHTSET",
														MODE_WORLD_READABLE);
										SharedPreferences.Editor et3 = sp3
												.edit();
										et3.putString("light", "0.5");
										et3.commit();
												break;
										default:
											break;
										}
									}
								});
						builder.create().show();

					}
				});

			}

		}).start();

	}

}

class multiwindows implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		ArrayList<HashMap<String, String>> windowsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("WebName", MainView.webView.getTitle());
		map.put("WebUrl", MainView.webView.getUrl());
		windowsList.add(map);

	}

}
