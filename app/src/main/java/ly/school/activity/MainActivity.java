package ly.school.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ly.school.R;
import ly.school.bean.SchoolBean;
import ly.school.util.LogUtil;
import ly.school.util.NetManager;
import ly.school.util.ToastUtil;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.super_name)
    SuperTextView superName;
    @Bind(R.id.super_xuehao)
    SuperTextView superXuehao;
    @Bind(R.id.super_banji)
    SuperTextView superBanji;
    @Bind(R.id.super_zhuanye)
    SuperTextView superZhuanye;
    @Bind(R.id.super_yuanxiao)
    SuperTextView superYuanxiao;
    @Bind(R.id.post_res_button)
    Button postResButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);
        ButterKnife.bind(this);
        initView();
//        Snackbar.make(getCurrentFocus(), "登录成功", Snackbar.LENGTH_LONG).show();
        ToastUtil.showToast(MainActivity.this, "登录成功");
        initData();

    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("成绩查询");
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            //设置返回按钮
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        postResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        try {
            NetManager netManager = NetManager.getNetManager(); //单例模式拿数据
            SchoolBean schoolBean = netManager.bean;
            superName.setLeftString(schoolBean.getName());
            superXuehao.setLeftString(schoolBean.getXuehao());
            superBanji.setLeftString(schoolBean.getBanji());
            superZhuanye.setLeftString("专业：" + schoolBean.getZhuanye());
            superYuanxiao.setLeftString(schoolBean.getYuanxi());


        } catch (Exception e) {
            LogUtil.m("获取个人信息出错");
//
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
