package chushi.bawang.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import chushi.bawang.database.SQLiteHelper;
import chushi.bawang.pages.Bookmark;
import chushi.bawang.pages.History;
import chushi.bawang.util.SyncLight;
import example.webbowers.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

public class MainView extends Activity {
	private static final int REFRESH = 0;
	private static final int BACK = 1;
	private static final int SETTINGS = 2;
	private static final int HISTORY = 3;
	private static final int BOOKMARK = 4;
	private static final int FILETEST = 5;
	private static final int FINSHED = 6;
	private static final int LIGHTSET = 7;
	public static WebView webView = null;
	private EditText url_input = null;
	private TextView downloadProgressPercentage = null;
	private EditText search_EditText = null;
	private SQLiteHelper sqliteHelper;
	// Handler handler = null;
	public static String cur_url = "http://www.baidu.com";
	private ImageButton add_bookmark = null;
	final Activity context = this;
	public static MainView instance = null;
	private SharedPreferences preferences;
	private ProgressBar downProgressBar = null;
	private ProgressBar progressWeb = null;
	private ImageButton menu_ImageBtn_back = null;
	private ImageButton menu_ImageBtn_refresh = null;
	private ImageButton menu_ImageBtn_menu = null;
	private ImageButton menu_ImageBtn_home = null;
	private boolean flag_loading = false;
	private boolean flag_record_history = true;
	public Handler handler = null;
	PopupWindow popWindow = null;
	TextView downFileName = null;
	ProgressBar downloadProgressbar = null;
	TextView downFileProgress = null;
	View contentView = null;
	Button test = null;
	Button close_popwindow_Btn = null;
	public static String save_path = "/BWDownloadFile/";// 下载文件存放地址
	private static String search_engine = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		SyncLight syncLight = new SyncLight();
		syncLight.syncLight(context);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		sqliteHelper = new SQLiteHelper(this);
		instance = this;
		init();
		handler = new PopWin();
		handler = new progressWeb();
	}

	// 初始化
	public void init() {
		setContentView(R.layout.main_view);
		preferences = getSharedPreferences(Settings.PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);
		// setContentView(R.layout.history_display_style);
		webView = (WebView) findViewById(R.id.webview);
		url_input = (EditText) findViewById(R.id.url_input);
		url_input.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// url_input.requestFocus();
				// url_input.setSelectAllOnFocus(true);// TODO Auto-generated
				// method stub

			}
		});
		// 为地址栏添加键盘键被按下的事件监听器
		url_input.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) { // 如果为回车键
					if (url_input.getText().length() > 0) {
						String url = url_input.getText().toString();
						System.out.println(url);
						webView.loadUrl(url);
						return true;
					} else {
						Toast.makeText(context, "你还未输入任何字符~", 0).show();
					}
				}
				return false;
			}

		});
		search_EditText = (EditText) findViewById(R.id.search_EditText); // 访问页面的按钮--“搜索”
		// 为地址栏添加键盘键被按下的事件监听器
		search_EditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				search_EditText.setText("");
			}
		});
		search_EditText.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_ENTER) { // 如果为回车键
							if (search_EditText.getText().length() > 0) {
								String url = search_EditText.getText().toString();
								System.out.println(url);
								webView.loadUrl("http://www.baidu.com/s?wd="+url);
								return true;
							} else {
								Toast.makeText(context, "你还未输入任何字符~", 0).show();
							}
						}
						return false;
					}
					
				});
		progressWeb = (ProgressBar) findViewById(R.id.progressWeb);
		downProgressBar = (ProgressBar) findViewById(R.id.download_progressbar);
		downProgressBar.setVisibility(View.INVISIBLE);
		downProgressBar.setOnClickListener(new PopWin());
		add_bookmark = (ImageButton) findViewById(R.id.add_bookmarks);// 添加书签按钮
		menu_ImageBtn_back = (ImageButton) findViewById(R.id.menu_imagebtn_back);
		menu_ImageBtn_back.setOnClickListener(new myMenuCLickListener());
		menu_ImageBtn_refresh = (ImageButton) findViewById(R.id.menu_imagebtn_refresh);
		menu_ImageBtn_refresh.setOnClickListener(new myMenuCLickListener());
		menu_ImageBtn_menu = (ImageButton) findViewById(R.id.menu_imagebtn_menu);
		menu_ImageBtn_menu.setOnClickListener(new myMenuCLickListener());
		menu_ImageBtn_home = (ImageButton) findViewById(R.id.menu_imagebtn_home);
		menu_ImageBtn_home.setOnClickListener(new myMenuCLickListener());
		downloadProgressPercentage = (TextView) findViewById(R.id.downloadProgress_percentage);
		downloadProgressPercentage.setVisibility(View.INVISIBLE);
		contentView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.download_popupwin, null);
		popWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		downFileName = (TextView) contentView
				.findViewById(R.id.download_fileName);
		downloadProgressbar = (ProgressBar) contentView
				.findViewById(R.id.download_window_progressbar);
		downFileProgress = (TextView) contentView
				.findViewById(R.id.download_window_progress);
		close_popwindow_Btn = (Button) contentView
				.findViewById(R.id.close_popWindow);
		close_popwindow_Btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow.dismiss();

			}
		});
		// webView.getSettings().setJavaScriptEnabled(true);//
		// 设置webView的属性，支持JavaScript
		webView.requestFocus();
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		String dir = this.getApplicationContext()
				.getDir("bawang", Context.MODE_PRIVATE).getPath();
		// 设置应用缓存的路径
		webView.getSettings().setAppCachePath(dir);
		// 设置缓存的模式
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		// 设置应用缓存的最大尺寸
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

		webView.getSettings().setSaveFormData(true);
		webView.getSettings().setSavePassword(true);
		webView.setDownloadListener(new myDownloaderListener());
		webView.setInitialScale(80);// 为25%，最小缩放等级
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				cur_url = url;
				return super.shouldOverrideUrlLoading(view, url);
			}

		});// 设置友好交互，即如果该网页中有链接，在本浏览器中重新定位并加载，而不是调用系统的浏览器
			// 设置添加到书签的动作
		add_bookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sqliteHelper.add_history(getApplicationContext(),
						webView.getTitle(), cur_url, 1);
				Toast.makeText(getApplicationContext(), "书签添加成功!",
						Toast.LENGTH_SHORT).show();
			}
		});
		preferences = getSharedPreferences(Settings.PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);

		/*
		 * button_titlename.setOnClickListener(new OnClickListener() { // 访问按钮事件
		 * 
		 * public void onClick(View v) { switch
		 * (preferences.getInt(Settings.KEY_SEARCH_ENGINE, 0)) { case 0:
		 * search_engine = Settings.search_engine_baidu; break; case 1:
		 * search_engine = Settings.search_engine_360; break; case 2:
		 * search_engine = Settings.search_engine_soso; break; } String url =
		 * url_input.getText().toString(); String standard =
		 * "^[http://www.|www.][\\S]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$"
		 * ; Pattern pattern = Pattern.compile(standard); Matcher match =
		 * pattern.matcher(url); if (match.find()) { cur_url = "http://" + url;
		 * Log.e("isurl", "yes"); } else { Log.e("isurl", "no"); cur_url =
		 * search_engine + url; } url_input.setText(cur_url);// initial the
		 * edit_text webView.loadUrl(cur_url); }
		 * 
		 * });
		 */
		/*
		 * webView.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent arg1) { //
		 * TODO Auto-generated method stub
		 * url_input.setText(webView.getUrl().toString()); return false; } });
		 */
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressWeb.setProgress(0);
				menu_ImageBtn_refresh.setImageDrawable(getResources()
						.getDrawable(R.drawable.cancel));
				flag_loading = true;
				if (newProgress >= 100) {
					if (flag_record_history == true) {
						sqliteHelper.add_history(getApplicationContext(),
								webView.getTitle(), cur_url, 0);
					}
					menu_ImageBtn_refresh.setImageDrawable(getResources()
							.getDrawable(R.drawable.refresh));
					flag_loading = false;

				}
				if (newProgress >= 0) {
					url_input.setText(webView.getUrl().toString());
					search_EditText.setText(webView.getTitle());
				}
				Message msg = handler.obtainMessage();
				msg.arg2 = newProgress;
				handler.sendMessage(msg);
				super.onProgressChanged(webView, newProgress);
			}
		});
		webView.loadUrl(cur_url); // 初始化网页
		// LoadView(webView, cur_url);// 初始化网页

		// initial the settings
		boolean isopen = false;
		isopen = preferences.getBoolean(Settings.KEY_support_JS, isopen);
		if (isopen) {
			webView.getSettings().setJavaScriptEnabled(true); // 设置JavaScript可用
		} else {
			webView.getSettings().setJavaScriptEnabled(false); // 设置JavaScript不可用
		}
		boolean isopen2 = false;
		isopen2 = preferences.getBoolean(Settings.KEY_support_ZOOM, isopen2);
		if (isopen2) {
			webView.getSettings().setSupportZoom(true);// 设置可以支持缩放
			webView.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
		} else {
			webView.getSettings().setSupportZoom(true);// 设置可以支持缩放
			webView.getSettings().setBuiltInZoomControls(false);// 设置出现缩放工具
		}
		boolean isopen3 = false;
		isopen3 = preferences.getBoolean(Settings.KEY_support_PIC, isopen3);
		if (isopen3) {
			webView.getSettings().setBlockNetworkImage(true); // 设置有图模式可用
		} else {
			webView.getSettings().setBlockNetworkImage(false); // 设置无图模式可用
		}

		// add book-mark
		add_bookmark.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				sqliteHelper.add_history(getApplicationContext(),
						webView.getTitle(), cur_url, 1);
				Toast.makeText(getApplicationContext(), "添加书签成功!",
						Toast.LENGTH_SHORT).show();
			};
		});
		// menu_btn_fresh

		SQLiteDatabase database = this.openOrCreateDatabase("History.db",
				MODE_APPEND, null);// 创建数据库
		final SQLiteHelper db = new SQLiteHelper(getApplicationContext());
		db.getWritableDatabase();

	}

	// 设置菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 设置菜单的功能：返回，刷新
		menu.add(0, SETTINGS, 0, R.string.settings).setIcon(
				R.drawable.tab_settings);
		menu.add(0, HISTORY, 1, R.string.menu_history).setIcon(
				android.R.drawable.ic_menu_recent_history);
		menu.add(0, BOOKMARK, 1, R.string.menu_bookmark).setIcon(
				R.drawable.bookmark);
		menu.add(0, FILETEST, 1, "文件").setIcon(
				android.R.drawable.sym_contact_card);
		menu.add(0, FINSHED, 1, "退出").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

	// 设置返回按键的动作：即按返回键时，返回上一级（如果不设置的话，便会由系统的推出当前应用）
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			webView.goBack();
			popWindow.dismiss();
		}
		// 必须这么设置，不然此较高的优先级会屏蔽掉onCreateOptionsMenu的回调函数的执行
		return false;
	}

	// 设置弹出菜单的选项的回调
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case SETTINGS:
			Intent intent_settings = new Intent();
			intent_settings.setClass(getApplicationContext(), Settings.class);
			startActivity(intent_settings);
			// MainView.this.finish();
			break;
		case HISTORY:
			Intent intent_history = new Intent();
			intent_history.setClass(getApplicationContext(), History.class);
			startActivity(intent_history);
			// MainView.this.finish();
			break;
		case BOOKMARK:
			Intent intent_bookmark = new Intent();
			intent_bookmark.setClass(getApplicationContext(), Bookmark.class);
			startActivity(intent_bookmark);
			// MainView.this.finish();
			break;
		case FILETEST:
			Intent intent_filetest = new Intent();
			intent_filetest.setClass(getApplicationContext(), FileTest.class);
			startActivity(intent_filetest);
			// MainView.this.finish();
			break;
		case FINSHED:
			finish();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	// settings
	public void setJavascript(boolean flag) {
		Log.e("my_set_js", ":" + flag);
		webView.getSettings().setJavaScriptEnabled(flag);
		// Toast.makeText(getApplicationContext(), "javascript: " + flag,
		// Toast.LENGTH_SHORT).show();
	}

	public void setZoom(boolean flag) {
		Log.e("my_set_zoom", ":" + flag);
		webView.getSettings().setSupportZoom(flag);
		webView.getSettings().setBuiltInZoomControls(flag);
		// Toast.makeText(getApplicationContext(), "zoom: " + flag,
		// Toast.LENGTH_SHORT).show();
	}

	public void setBlockPicture(boolean flag) {
		Log.e("my_set_pic", ":" + flag);
		webView.getSettings().setBlockNetworkImage(flag);
		// Toast.makeText(getApplicationContext(), "BlockPIC: " + flag,
		// Toast.LENGTH_SHORT).show();
	}

	public void setCache(boolean flag) {
		Log.e("my_set_cache", ":" + flag);
		if (flag) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			// Toast.makeText(getApplicationContext(), "setCACHE: " + flag,
			// Toast.LENGTH_SHORT).show();
		} else {
			webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			// Toast.makeText(getApplicationContext(), "setCACHE: " + flag,
			// Toast.LENGTH_SHORT).show();
		}
	}

	public void setRecordHistory(boolean flag) {
		Log.e("record_history", "write:" + flag);
		flag_record_history = flag;
	}

	// inner class for WebViewCLient

	// inner class for download
	private class myDownloaderListener implements DownloadListener {

		@Override
		public void onDownloadStart(final String url, String userAgent,
				String arg2, String arg3, long arg4) {
			// if the SD card can't be written or read,then toast
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Toast.makeText(getApplicationContext(), "no SDcard!",
						Toast.LENGTH_SHORT).show();
				return;
			} else {

				Dialog alertDialog = new AlertDialog.Builder(MainView.this)
						.setTitle("确定开始下载吗？")
						.setMessage(
								"\n如果点击确定，则开始下载，期间如果不是WIFI网络将会花费大量数据流量，反之取消、\n"
										+ "请想清楚后点击！")
						.setIcon(R.drawable.about)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										DownLoaderTask download_task = new DownLoaderTask();
										download_task.execute(url);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create();
				alertDialog.show();
				// DownLoaderTask download_task = new DownLoaderTask();
				// download_task.execute(url);
				// Toast.makeText(getApplicationContext(), url,
				// Toast.LENGTH_SHORT)
				// .show();
			}
		}

	}

	private class DownLoaderTask extends AsyncTask<String, Integer, String> {

		public DownLoaderTask() {

		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			// default fileName is the after the last '\'
			String fileName = url.substring(url.lastIndexOf("/") + 1);
			try {
				// convert the fileName into UTF-8 format
				fileName = URLDecoder.decode(fileName, "UTF-8");
				Log.e("file", fileName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// save-path ,will self-define later
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);

			if (file.exists()) {
				Toast.makeText(getApplicationContext(), "file exists!",
						Toast.LENGTH_SHORT).show();
				return fileName;
			}
			// if this file doesn't exist

			// Toast.makeText(getApplicationContext(),
			// "start downloading...", Toast.LENGTH_SHORT).show();
			Log.e("file:", Environment.getExternalStorageDirectory().toString());
			try {
				Log.e("file", url);
				File path = new File(Environment.getExternalStorageDirectory()
						+ save_path);
				if (!path.exists()) {
					path.mkdir();
				}
				DownloadFile(Environment.getExternalStorageDirectory()
						+ save_path, url, fileName);
				Log.e("file", "started downloaad");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("file", "not start downloaad");
			}
			return fileName;

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e("result", result);
			if (result == null) {
				Toast.makeText(getApplicationContext(), "网络异常！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "下载完成!",
						Toast.LENGTH_SHORT).show();
				downProgressBar.setVisibility(View.GONE);
				downloadProgressPercentage.setVisibility(View.GONE);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(final Integer... progresses) {
			downProgressBar.setVisibility(View.VISIBLE);
			downloadProgressPercentage.setVisibility(View.VISIBLE);
			downProgressBar.setProgress(progresses[0]);
			downloadProgressPercentage.setText(progresses[0] + "%");
			// through handler to send progress
			Message msg = handler.obtainMessage();
			msg.arg1 = progresses[0];
			handler.sendMessage(msg);
			super.onProgressUpdate(progresses);
		}

		// functions
		public void DownloadFile(String path, String url, String fileName)
				throws IOException {
			long total_length = 0;
			int downloadedSize = 0;
			URL load_url = new URL(url);
			URLConnection connection = load_url.openConnection();
			connection.connect();
			// create the stream
			InputStream inStream = connection.getInputStream();
			total_length = connection.getContentLength();
			Log.e("legnth:", total_length + "");
			// Toast.makeText(getApplicationContext(),
			// "start downloading...", Toast.LENGTH_SHORT).show();
			if (total_length <= 0) {
				throw new RuntimeException("can't get the file_length!");
			}
			if (inStream == null) {
				throw new RuntimeException("can't get the file_stream!");
			}
			// create the output file
			FileOutputStream outputStream = new FileOutputStream(path
					+ fileName);
			byte[] buffer = new byte[2048];
			int temp_length = 0;
			while ((temp_length = inStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, temp_length);
				downloadedSize += temp_length;
				int cur_progress = (int) ((downloadedSize / (float) total_length) * 100);
				Log.e("progress",
						(int) ((downloadedSize / (float) total_length) * 100)
								+ "%");
				// send the progress
				publishProgress(cur_progress);
			}

			try {
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {

			}

		}

	}

	// bottom_menu actions
	public class myMenuCLickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.menu_imagebtn_back:
				if (webView.canGoBack())
					webView.goBack();
				else
					Toast.makeText(getApplicationContext(), "no back page!",
							Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_imagebtn_refresh:
				if (flag_loading == false)
					webView.reload();
				else
					webView.stopLoading();
				break;
			case R.id.menu_imagebtn_menu:
				openOptionsMenu();
				break;

			case R.id.menu_imagebtn_home:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Home.class);
				startActivity(intent);
				MainView.this.finish();
				break;
			}
		}

	}

	private class PopWin extends Handler implements OnClickListener {

		@Override
		public void onClick(View v) {
			popWindow.setFocusable(true);
			if (!popWindow.isShowing()) {
				popWindow.showAtLocation(add_bookmark, Gravity.CENTER, 0, 100);
				popWindow.update(400, 100);
				// popWindow.update(400,100);
				Log.e("pop", "yes");
			} else {
				popWindow.dismiss();
			}
		}

		@Override
		public void handleMessage(Message msg) {
			Log.e("handler", "message:" + msg.arg1);
			downloadProgressbar.setProgress(msg.arg1);
			downFileProgress.setText("已完成" + msg.arg1 + "%");
			if (msg.arg1 == 100)
				popWindow.dismiss();
			super.handleMessage(msg);
		}

	}

	private class progressWeb extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Log.e("handler2", "网页进度:" + msg.arg2);
			progressWeb.setProgress(msg.arg2);
			if (msg.arg2 == 100)
				progressWeb.setProgress(100);
			super.handleMessage(msg);
		}

	}
}
