package chushi.bawang.util;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chushi.bawang.activity.FileTest;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DelectFiles {

	

	boolean ischeck = false;

	@SuppressWarnings("deprecation")
	public void delefileDialog(final Context context, final String fileUrl,
			final ListView listView,final List<String> audioList,final int currentItem,final ArrayAdapter<String> adapter) {
		// TODO Auto-generated method stub
		final File testFile = new File(fileUrl);
		 
		final FileTest settingMenu = new FileTest();
		final ProgressDialog mDialog;
		mDialog = new ProgressDialog(context);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mDialog.setTitle("确定删除吗？");
		mDialog.setMessage("如果选择【确定】则SD卡内对应文件将会消失，注：不能恢复\n\n如果选择【取消】将不会继续操作！");
		mDialog.setIndeterminate(true);// 设置进度条是否为不明确
		mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setButton("确定", new DialogInterface.OnClickListener() {

			@SuppressLint("SdCardPath")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				try {
					iteratorFile(testFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//audioList.remove(currentItem);
				//audioList.clear();
				adapter.clear();
				settingMenu.getFiles("/mnt/sdcard/",audioList);
				listView.setAdapter(adapter);
				//System.out.println("button1"+isdele);				
				//Toast.makeText(context, "文件删除成功！", 0).show();
				//String tishiString="注：/mnt/sdcard/YuanBaBowersD文件夹\n如果点击删除此文件夹还依然会在列表的第一个\n因为这是浏览器的专属文件夹！";
				ischeck=true;
				// settingMenu.setAudioList(null);
				//listView.setVisibility(View.GONE);
				//audioList.clear();

			}
		});
		mDialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//setflase(isdele);
				//System.out.println("button2");
				ischeck=false;
			}
		});
		
		
		mDialog.show();
		if (ischeck) {
			audioList.clear();
		}
		
		//return isdele;

		// settingMenu.getFiles("/mnt/sdcard/");
	}

	public void iteratorFile(File file) throws Exception  {
		int num=0;
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
				Log.i("这是文件目标地址：" + file.getPath(), "删除成功！");
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					Log.i("这是文件夹的子目录目标地址：" , files[i].getPath());
					this.iteratorFile(files[i]);
					num++;
					Log.i("循环"+file.getPath()+"中已删除第"+i+1+"条数据：" , files[i].getPath());
				}
			}
			file.delete();
			
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
		if (num>=1) {
			Log.i("目标地址" , "已删除完成！！！！！");
			num=0;
			//a=true;
		}
		
	}
	public boolean setflase(boolean b) {
		b= false;
		return b;
	}

}
