package chushi.bawang.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

public class SyncLight {
	public void syncLight(Context context) {
		//设置完毕后，保证每个页面的亮度都一致
				SharedPreferences spset = context.getSharedPreferences(
						"LIGHTSET", 1);
				String lightSet= spset.getString("light", null);
				if (lightSet!=null) {
					if (lightSet.equals("0")) {
						WindowManager.LayoutParams p = ((Activity) context).getWindow()
								.getAttributes();
						p.screenBrightness = 0.01f;
						((Activity) context).getWindow().setAttributes(p);						
					}else if (lightSet.equals("0")) {
						WindowManager.LayoutParams p1 = ((Activity) context).getWindow()
								.getAttributes();
							p1.screenBrightness = 1.0f;
							((Activity) context).getWindow().setAttributes(p1);
						}else{
							WindowManager.LayoutParams p2 = ((Activity) context).getWindow()
								.getAttributes();
							p2.screenBrightness = 0.45f;
							((Activity) context).getWindow().setAttributes(p2);
						}
					
				}
	}

}
