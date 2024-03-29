package chushi.bawang.activity;

import chushi.bawang.database.SQLiteHelper;
import chushi.bawang.util.SyncLight;
import example.webbowers.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	public static String PREFERENCES_NAME = "PRE_NAME";
	public static String KEY_support_JS = "VALUE_JS";
	public static String KEY_support_PIC = "VALUE_PIC";
	public static String KEY_support_ZOOM = "VALUE_ZOOM";
	public static String KEY_SUPPORT_CACHE = "VALUE_CACHE";
	public static String KEY_SUPPORT_HISTORY = "VALUE_HISTORY";
	public static String KEY_SUPPORT_PERCENTAGE = "VALUE_PERCENTAGE";
	public static String KEY_SEARCH_ENGINE = "VALUE_ENGINE";
	private SharedPreferences preferences;
	private SharedPreferences.Editor prefer_editor;
	private CheckBox toggleBtn_setJS = null;
	private CheckBox toggleBtn_setZoom = null;
	private CheckBox toggleBtn_setPIC = null;
	private ImageButton backBtn = null;
	private Button change_save_directory_Btn = null;
	private Button clear_data_Btn = null;
	private View dialogView = null;
	public TextView dialogText = null;
	public EditText dialogEdittext = null;
	private Spinner spinner;
	public static final String search_engine_baidu = "http://www.baidu.com/s?wd=";
	public static final String search_engine_soso = "http://www.soso.com/q?w=";
	public static final String search_engine_360 = "http://www.so.com/s?q=";
	public final String[] engines = { "百度", "360", "搜搜" };
	public boolean clear_flags[] = { false, false};
	public ArrayAdapter<String> adapter;
	public SQLiteHelper sqlitehelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		SyncLight syncLight=new SyncLight();
		syncLight.syncLight(Settings.this);
		sqlitehelper = new SQLiteHelper(this);
		preferences = getSharedPreferences(PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);
		prefer_editor = preferences.edit();

		// define and enable the basic function of support_javascript
		toggleBtn_setJS = (CheckBox) findViewById(R.id.settings_set_javascript_toggle_btn);
		toggleBtn_setJS.setChecked(preferences
				.getBoolean(KEY_support_JS, false));// default case:doesn't
													// support the javascript
		toggleBtn_setJS
				.setOnCheckedChangeListener(new onCheckedChangedListener());
		toggleBtn_setZoom = (CheckBox) findViewById(R.id.settings_set_zoom_toggle_btn);
		toggleBtn_setZoom.setChecked(preferences.getBoolean(KEY_support_ZOOM,
				false));
		toggleBtn_setZoom
				.setOnCheckedChangeListener(new onCheckedChangedListener());
		toggleBtn_setPIC = (CheckBox) findViewById(R.id.settings_set_picture_toggle_btn);
		toggleBtn_setPIC.setChecked(preferences.getBoolean(KEY_support_PIC,
				false));
		toggleBtn_setPIC
				.setOnCheckedChangeListener(new onCheckedChangedListener());
		

		change_save_directory_Btn = (Button) findViewById(R.id.change_directory);
		change_save_directory_Btn
				.setOnClickListener(new myButtonClickListener());
		clear_data_Btn = (Button) findViewById(R.id.clear_data);
		clear_data_Btn.setOnClickListener(new myButtonClickListener());

		spinner = (Spinner) findViewById(R.id.settings_choose_search_engine);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, engines);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setSelection(preferences.getInt(KEY_SEARCH_ENGINE, 0));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				prefer_editor.putInt(KEY_SEARCH_ENGINE, id);
				prefer_editor.commit();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// back button
		backBtn = (ImageButton) findViewById(R.id.settings_back);
		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				Intent intent = new Intent(Settings.this, Home.class);
//				startActivity(intent);
				Settings.this.finish();
			}
		});
		// define and enable the basic functions
		dialogView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.create_directory_dialog, null);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

//			Intent intent = new Intent(Settings.this, Home.class);
//			startActivity(intent);
			Settings.this.finish();

		}
		return true;
	}
	public class onCheckedChangedListener implements OnCheckedChangeListener {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// for these toggle_btns,use switch to do
			switch (buttonView.getId()) {
			case R.id.settings_set_javascript_toggle_btn:
				if (isChecked) {
					prefer_editor.putBoolean(KEY_support_JS, isChecked);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setJavascript(false);
					// Toast.makeText(getApplicationContext(), "click from "+
					// isChecked, Toast.LENGTH_SHORT).show();
				} else {
					prefer_editor.putBoolean(KEY_support_JS, false);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setJavascript(true);
					// Toast.makeText(getApplicationContext(), "click from "+
					// isChecked, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.settings_set_zoom_toggle_btn:
				if (isChecked) {
					prefer_editor.putBoolean(KEY_support_ZOOM, true);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setZoom(false);
					// Toast.makeText(getApplicationContext(), "click from "+
					// isChecked, Toast.LENGTH_SHORT).show();
				} else {
					prefer_editor.putBoolean(KEY_support_ZOOM, false);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setZoom(true);
					// Toast.makeText(getApplicationContext(), "click from "+
					// isChecked, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.settings_set_picture_toggle_btn:
				if (isChecked) {
					prefer_editor.putBoolean(KEY_support_PIC, true);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setBlockPicture(false);
				} else {
					prefer_editor.putBoolean(KEY_support_PIC, false);
					prefer_editor.commit();
					Toast.makeText(Settings.this, "属性更改成功！", 0).show();
					//MainView.instance.setBlockPicture(true);
				}
				break;
			
			
			}

		}

	}

	public class myButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_directory:
				new AlertDialog.Builder(Settings.this)
						.setTitle("更改存储路径")
						.setView(dialogView)
						.setPositiveButton("更改",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialogText = (TextView) dialogView
												.findViewById(R.id.dialog_textview);
										dialogEdittext = (EditText) dialogView
												.findViewById(R.id.dailog_edittext);
										if (dialogEdittext.getText().toString()
												.trim().equals("")) {
											Toast.makeText(
													getApplicationContext(),
													"路径为空！", Toast.LENGTH_SHORT)
													.show();
										} else {
											MainView.instance.save_path = "/"
													+ dialogEdittext.getText()
															.toString().trim()
													+ "/";
											Log.e("create directory:",
													MainView.instance.save_path);
											Toast.makeText(
													getApplicationContext(),
													"更改路径成功！",
													Toast.LENGTH_SHORT).show();
										}
										startActivity(new Intent(Settings.this,
												Settings.class));
										Settings.this.finish();
									}

								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
				break;
			case R.id.clear_data:
				new AlertDialog.Builder(Settings.this)
						.setTitle("清空数据")
						.setMultiChoiceItems(
								new String[] { "清空历史数据", "清空设置"},
								clear_flags, new OnMultiChoiceClickListener() {

									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										clear_flags[which] = isChecked;

									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Log.e("0:", clear_flags[0] + "");
										Log.e("1:", clear_flags[1] + "");
										if(clear_flags[0]){
											//clear history
											try{
											sqlitehelper.clear_history();
											Log.e("clear_history", "cleared");
											}catch(Exception e){
												Log.e("clear_history", "failed");
											}
										}
										if(clear_flags[1]){
											//clear settings
											try{
											prefer_editor.clear();
											prefer_editor.commit();
											Intent intent = new Intent();
											intent.setClass(Settings.this, Settings.class);
											startActivity(intent);
											Settings.this.finish();
											Log.e("clear_settings", "cleared");
											}catch(Exception e){
												Log.e("clear_settings", "failed");
											}
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();
				break;
			}

		}

	}
}
