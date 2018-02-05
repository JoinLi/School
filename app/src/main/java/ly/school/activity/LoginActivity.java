package ly.school.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ly.school.R;
import ly.school.util.LogUtil;
import ly.school.util.NetManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mTextid;
    private EditText mPasswordView, ed_yzm;
    private ImageView im_yzm;
    private NetManager netManager;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        initView();
        netManager.getCode(LoginActivity.this, im_yzm, getCurrentFocus());
        netManager.getIndex();

    }

    private void initView() {
        netManager = NetManager.getNetManager(); //单例模式拿数据
        im_yzm = (ImageView) findViewById(R.id.im_yzm);
        mTextid = (AutoCompleteTextView) findViewById(R.id.text_id);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        mPasswordView = (EditText) findViewById(R.id.password);
        im_yzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                netManager.getCode(LoginActivity.this, im_yzm, getCurrentFocus());
            }
        });
        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                attemptLogin();
                dialogShow();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = netManager.loginByPost(ed_yzm.getText().toString().trim(), "1317170132", "renyahui.");
                            netManager.getLogionSuccessValue();

                            if (result != null) {
                                progressDialog.dismiss();

                            }
                            LogUtil.LogShitou(result);
                            Intent intent = new Intent(LoginActivity.this, Main_Activity.class);
//                                intent.putExtra("result", myString);
                            startActivity(intent);

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(getCurrentFocus(), "登录失败", Snackbar.LENGTH_LONG).show();
                            netManager.getCode(LoginActivity.this, im_yzm, getCurrentFocus());
                            LogUtil.m("登录出错");
//
                        }
                    }
                }).start();

            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });
        

    }

    private void dialogShow() {
        progressDialog = new Dialog(LoginActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.layout_dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("正在加载中");
        progressDialog.show();
    }



}

