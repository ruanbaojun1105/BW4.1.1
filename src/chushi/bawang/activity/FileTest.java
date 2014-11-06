package chushi.bawang.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chushi.bawang.util.DelectFiles;
import chushi.bawang.util.SyncLight;
import example.webbowers.R;
import android.R.color;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "WorldReadableFiles", "NewApi" })
public class FileTest extends Activity {

	public boolean isChangeDownFn = false;
	public String willDelectFileName;

	public boolean isChangeDownFn() {
		return isChangeDownFn;
	}

	public void setChangeDownFn(boolean isChangeDownFn) {
		this.isChangeDownFn = isChangeDownFn;
	}

	public List<String> audioList = new ArrayList<String>();
	public List<String> audioList2 = new ArrayList<String>();
	ArrayAdapter<String> adapter, adapter2;
	int currentItem = 0;
	
	String[] listName;
	ListView listView;
	String tag;
	String filepath;
	boolean flag = false;
	boolean isAudioList = true;
	// boolean isDirectory=false;
	@SuppressLint("SdCardPath")
	String rootPath = "/mnt/";
	String inFilePath = "/mnt/sdcard/";
	String outFilePath = "/mnt/sdcard1/";
	SharedPreferences sharedPreferences;
	String BWDown, BWDown2;
	String backList;
	ImageButton backHomeActivity;
	ImageButton popUpListButton;
	RelativeLayout relativeLayout;

	public List<String> getAudioList() {
		return audioList;
	}

	public void setAudioList(List<String> audioList) {
		this.audioList = audioList;
	}

	File testFile;
	Button delectAllFile;
	AlertDialog.Builder builder;
	Button inSD, outSD, TopFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置界面没有titlebar
		setContentView(R.layout.filetest);
		SyncLight syncLight=new SyncLight();
		syncLight.syncLight(FileTest.this);
		
		relativeLayout=(RelativeLayout)findViewById(R.id.filebg);
		backHomeActivity = (ImageButton) findViewById(R.id.backmainactivity);// 返回按钮，返回到浏览器页面
		backHomeActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(SettingMenu.this, MainActivity.class);
				String nichen = "";
				SharedPreferences sp = SettingMenu.this.getSharedPreferences(
						"nichenzhuangtai", MODE_WORLD_READABLE);
				nichen = sp.getString("nichen", "");
				intent.putExtra("nichen", nichen);// 设置传递数据
				startActivity(intent);
				SettingMenu.this.finish();*/
				if (backList!=null) {
					try {

						AudioList(listView, backList, audioList);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else {										
						
						FileTest.this.finish();	
					
				}
				
				
			}
		});
		popUpListButton = (ImageButton) findViewById(R.id.popuplist);// 弹出POPup菜单
		popUpListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopup(v);
				backList=null;
			}
		});

		inSD = (Button) findViewById(R.id.inSD);// 内置SD地址
		inSD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioList.clear();
				audioList2.clear();
				try {
					AudioList(listView, inFilePath, audioList);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				backList=null;
			}
		});

		outSD = (Button) findViewById(R.id.outSD);// 外置SD地址
		outSD.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioList.clear();
				audioList2.clear();
				try {

					AudioList(listView, outFilePath, audioList);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				backList=null;
			}
		});
		TopFile = (Button) findViewById(R.id.topFile);// 回到最顶端的地址
		TopFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				audioList.clear();
				audioList2.clear();
				try {

					AudioList(listView, inFilePath, audioList);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				backList=null;
			}
		});
		

		listView = (ListView) findViewById(R.id.listView);
		try {

			AudioList(listView, inFilePath, audioList);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
//			Intent intent = new Intent(FileTest.this, Home.class);			
//			startActivity(intent);
			FileTest.this.finish();
		
		}
		
		if (keyCode==KeyEvent.KEYCODE_MENU) {
			
			showPopup(popUpListButton);
		}
		return true;
	}

	public void AudioList(final ListView listView, String filePathEX,
			final List<String> audioList) throws Exception {

		// final DelectDownFile delectDownFile = new DelectDownFile();
		// final ShowDialog Dialog = new ShowDialog();

		getFiles(filePathEX, audioList);// YuanBaBowersD/
		Toast.makeText(FileTest.this, "===读取数据中===", 1).show();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, audioList);
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, audioList2);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> lisView, View view,
					int position, long id) {
				currentItem = position;				
				willDelectFileName = audioList.get(currentItem);
				
				backList=willDelectFileName.toString();//返回上一级的List菜单数据
				File testFile = new File(willDelectFileName);
				if (testFile.isDirectory()) {
					adapter2.clear();
					audioList2.clear();
					getFiles(filepath, audioList2);
					try {
						AudioList(listView, willDelectFileName, audioList2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// getFiles(willDelectFileName, audioList2);
					// listView.setAdapter(adapter2);

				} else {
					DelectFiles delectFiles = new DelectFiles();
					delectFiles.delefileDialog(FileTest.this,
							willDelectFileName, listView, audioList2,
							currentItem, adapter2);
				}
			}

			// playMusic(audioList.get(currentItem));
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onItemLongClick(AdapterView<?> lisView, View view,
					int position, long id) {
				currentItem = position;
				willDelectFileName = audioList.get(currentItem);// 获得在audialist中的位置，以便删除！
				DelectFiles delectFiles = new DelectFiles();
				delectFiles.delefileDialog(FileTest.this,
						willDelectFileName, listView, audioList2, currentItem,
						adapter2);
				Toast.makeText(FileTest.this, "===此操作会直接删除文件===", 1).show();
				/*
				 * final ProgressDialog mDialog; final ShowDialog Dialog = new
				 * ShowDialog(); mDialog = new ProgressDialog(SettingMenu.this);
				 * mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//
				 * 设置风格为圆形进度条 mDialog.setTitle("确定直接删除此文件/文件夹？");
				 * mDialog.setIcon(R.drawable.about); mDialog.setMessage(
				 * "如果选择【确定】则SD卡内对应文件将会消失，注：不能恢复\n\n如果选择【取消】将不会继续操作！");
				 * mDialog.setIndeterminate(true);// 设置进度条是否为不明确
				 * mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
				 * mDialog.setCanceledOnTouchOutside(false);
				 * mDialog.setButton("确定", new DialogInterface.OnClickListener()
				 * {
				 * 
				 * 
				 * @SuppressLint("SdCardPath")
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * {
				 * 
				 * 
				 * } }); mDialog.setButton2("取消", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * {
				 * 
				 * } }); mDialog.show();
				 */
				return true;
			}
		});

	}

	public static String[] ImageFormatSet = new String[] { "apk" };

	/**
	 * @param判断文件格式函数、可以自定义丰富的功能
	 */
	public static boolean isAudioFile(String path) {
		for (String format : ImageFormatSet) {
			if (path.contains(format)) {
				return true;
			}
		}
		return false;
	}

	public void getFiles(String url, List<String> audioList) {
		File files = new File(url);
		int i = 0;
		File[] file = files.listFiles();
		try {
			for (File f : file) {
				if (f != null) {

					if (f.isDirectory()) {

						// isDirectory=true;
						filepath = f.getAbsolutePath();
						// getFiles(f.getAbsolutePath());
						// //如果path表示的是一个目录则返回true
						tag = f.getAbsolutePath();
						Log.i("真实地址", tag);
						audioList.add(f.getAbsolutePath());

					} else {
						/* if (isAudioFile(f.getPath())) { */
						audioList.add(f.getPath());
						tag = f.getAbsolutePath();
						Log.i("目标地址", tag);

					}
				} else {
					// final ShowDialog Dialog = new ShowDialog();
					// Dialog.showDialog(SettingMenu.this, listView,
					// "没文件！！！fuck!!!", 3000);
					Log.i("目标地址", "没文件！！！fuck!!!");
					// isDirectory=false;
				}
				// audioList.set(0, "/mnt/sdcard/YuanBaBowersD");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int j = 0; j < audioList.size(); j++) {
			if (audioList.get(j).equals("/mnt/sdcard/YuanBaBowersD")
					&& isAudioList) {
				// 如果是浏览器下载的文件夹，则不添加进LIST，以保证它在list的第一行！
				audioList.set(0, "/mnt/sdcard/YuanBaBowersD");
				audioList.remove(j);
				isAudioList = false;
			} else {
				Log.i("bawang目标地址", "不是在SD首项，无需添加！");
			}
		}
		/*
		 * if (isAudioList) { audioList.set(0, "/mnt/sdcard/YuanBaBowersD");
		 * Log.i("bawang目标地址", "设置为第一项添加成功！"); }else { Log.i("bawang目标地址",
		 * "不是在SD首项，无需添加！"); }
		 */

	}

	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	public void showPopup(View v) {
		PopupMenu popupMenu = new PopupMenu(this, v);
		MenuInflater inflater = popupMenu.getMenuInflater();
		inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
		popupMenu.show();
		popupMenu
				.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@SuppressLint("ResourceAsColor")
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch (item.getItemId()) {

						case R.id.lightss:
							Intent intent = new Intent(FileTest.this, FileTest.class);			
							startActivity(intent);
							FileTest.this.finish();							
							return true;
						
						case R.id.greyss:
							relativeLayout.setBackgroundColor(R.color.yellow);
							return true;
						default:
							relativeLayout.setBackgroundColor(R.color.white);
							return false;
						}

					}

				});
	}

	

}