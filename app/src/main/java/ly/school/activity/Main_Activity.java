package ly.school.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ly.school.R;
import ly.school.adapter.ResultAdapter;
import ly.school.util.LogUtil;
import ly.school.util.NetManager;
import ly.school.util.ToastUtil;

/**
 * @author Administrator
 */
public class Main_Activity extends AppCompatActivity {
    private ResultAdapter mAdapter;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private List<String> list;
    private Map<Integer, List<String>> mlistmore = new HashMap<Integer, List<String>>();
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
//        Snackbar.make(getCurrentFocus(), "登录成功", Snackbar.LENGTH_LONG).show();
        ToastUtil.showToast(Main_Activity.this,"登录成功");

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
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new ResultAdapter(mlistmore);
        mRecyclerView.setAdapter(mAdapter);

        Button post_res_button = (Button) findViewById(R.id.post_res_button);
        post_res_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }

    private void initData() {
        dialogShow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NetManager netManager = NetManager.getNetManager(); //单例模式拿数据
                    String result = netManager.postResult();
                    if (result != null) {
                        JsoupXml(result);

                    }


                } catch (Exception e) {
                    LogUtil.m("获取成绩出错");
                    progressDialog.dismiss();
//
                }
            }
        }).start();

    }
    private void JsoupXml(String bg) {
        Document doc = (Document) Jsoup.parse(bg);
//        Elements trs = doc.select("table").select("tr");
        Elements trs = doc.getElementsByAttributeValue("class", "datelist").first().select("tr");
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            list = new ArrayList<String>();
            for (int j = 0; j < tds.size(); j++) {
                String text;
                text = tds.get(j).text();
                list.add(text);


            }
            mlistmore.put(i, list);

        }
        handler.sendEmptyMessage(1);

        for (int i = 0; i < mlistmore.size(); i++) {
            LogUtil.m(mlistmore.get(i).toString());
        }

        LogUtil.m("总计" + mlistmore.size());


    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    progressDialog.dismiss();
                    mAdapter.notifyDataSetChanged();
                default:
                    break;
            }
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void dialogShow() {
        progressDialog = new Dialog(Main_Activity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.layout_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("正在加载中");
        progressDialog.show();
    }
}
