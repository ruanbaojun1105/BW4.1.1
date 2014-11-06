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
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν�����
		mDialog.setTitle("ȷ��ɾ����");
		mDialog.setMessage("���ѡ��ȷ������SD���ڶ�Ӧ�ļ�������ʧ��ע�����ָܻ�\n\n���ѡ��ȡ�������������������");
		mDialog.setIndeterminate(true);// ���ý������Ƿ�Ϊ����ȷ
		mDialog.setCancelable(true);// ���ý������Ƿ���԰��˻ؼ�ȡ��
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setButton("ȷ��", new DialogInterface.OnClickListener() {

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
				//Toast.makeText(context, "�ļ�ɾ���ɹ���", 0).show();
				//String tishiString="ע��/mnt/sdcard/YuanBaBowersD�ļ���\n������ɾ�����ļ��л���Ȼ�����б�ĵ�һ��\n��Ϊ�����������ר���ļ��У�";
				ischeck=true;
				// settingMenu.setAudioList(null);
				//listView.setVisibility(View.GONE);
				//audioList.clear();

			}
		});
		mDialog.setButton2("ȡ��", new DialogInterface.OnClickListener() {

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
				Log.i("�����ļ�Ŀ���ַ��" + file.getPath(), "ɾ���ɹ���");
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					Log.i("�����ļ��е���Ŀ¼Ŀ���ַ��" , files[i].getPath());
					this.iteratorFile(files[i]);
					num++;
					Log.i("ѭ��"+file.getPath()+"����ɾ����"+i+1+"�����ݣ�" , files[i].getPath());
				}
			}
			file.delete();
			
		} else {
			System.out.println("��ɾ�����ļ������ڣ�" + '\n');
		}
		if (num>=1) {
			Log.i("Ŀ���ַ" , "��ɾ����ɣ���������");
			num=0;
			//a=true;
		}
		
	}
	public boolean setflase(boolean b) {
		b= false;
		return b;
	}

}
