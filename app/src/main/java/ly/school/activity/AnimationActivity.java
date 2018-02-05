package ly.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import ly.school.R;
import ly.school.adapter.LeaderAdapter;

public class AnimationActivity extends Activity {
	private ViewPager vp;  
	private LeaderAdapter vpAdapter;  //PagerAdapter
	private  List<View>  listv; // View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animation);
        initView();
    }
	private void initView() {
		vp=(ViewPager) findViewById(R.id.vp);
		listv=new ArrayList<View>();	
		View view=getLayoutInflater().inflate(R.layout.animation2, null);
		View view1=getLayoutInflater().inflate(R.layout.animation3, null);
		listv.add(view);
		listv.add(view1);
     vpAdapter=new LeaderAdapter(listv);
     vp.setAdapter(vpAdapter);
		
	}

  public void startButton(View v){
	  Intent intent=new Intent(this,LoginActivity.class);
	  startActivity(intent);
	  finish();
  }

    
}
