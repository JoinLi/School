package ly.school.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BaseTools {
/**
 * 取屏幕宽度
 * @param context
 * @return
 */
public static float getWindowWidth(Context context){
    WindowManager manager = (WindowManager
    		)context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm=new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(dm);
    float width=dm.widthPixels;
    // int height=dm.heightPixels;
    
	return width;
}
public static float getWindowHeight(Context context){
    WindowManager manager = (WindowManager
    		)context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm=new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(dm);
    float height=dm.heightPixels;
    // int height=dm.heightPixels;
    
	return height;
}

}
