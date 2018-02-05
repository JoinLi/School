package ly.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import ly.school.R;
import ly.school.util.BaseTools;


public class SplahActivity extends Activity implements Runnable {
    private ImageView log1, log2, log3;
    private Boolean isFirstUse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.animation1);
        initView();
        /**
         * 启动一个延迟线程
         */
        new Thread(this).start();
    }


    private void initView() {

        log1 = (ImageView) findViewById(R.id.log1);
        log2 = (ImageView) findViewById(R.id.log2);

        float mheight = (float) (BaseTools.getWindowHeight(this) / 3.5);
        Animation smile = new TranslateAnimation(0, 0, 0, -mheight);
        Animation logio = new AlphaAnimation(0, 1);
        smile.setDuration(3000);
        smile.setFillAfter(true);
        logio.setDuration(3000);
        logio.setFillAfter(true);
        log1.startAnimation(smile);
        log2.startAnimation(logio);

    }

    @Override
    public void run() {
        try {
            /**
             * 延迟两秒时间
             */
            Thread.sleep(3001);

            //读取SharedPreferences中需要的数据
            SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_PRIVATE);

            isFirstUse = preferences.getBoolean("isFirstUse", true);

            /**
             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            if (isFirstUse) {
                startActivity(new Intent(SplahActivity.this, AnimationActivity.class));
            } else {
                startActivity(new Intent(SplahActivity.this, LoginActivity.class));
            }
            finish();

            //实例化Editor对象
            Editor editor = preferences.edit();
            //存入数据
            editor.putBoolean("isFirstUse", false);
            //提交修改
            editor.commit();


        } catch (InterruptedException e) {

        }

    }
}

