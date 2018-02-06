package ly.school.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    private ProgressDialog dialog;
    private TextView byAuthor;

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
        byAuthor = (TextView) findViewById(R.id.byAuthor);
        mTextid = (AutoCompleteTextView) findViewById(R.id.text_id);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        mPasswordView = (EditText) findViewById(R.id.password);
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("正在登录...");
        im_yzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                netManager.getCode(LoginActivity.this, im_yzm, getCurrentFocus());
            }
        });
        byAuthor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getCurrentFocus(), "若遇到问题，联系QQ：986483793", Snackbar.LENGTH_LONG).show();
            }
        });
        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = netManager.loginByPost(ed_yzm.getText().toString().trim(),
                                    mTextid.getText().toString().trim(),
                                    mPasswordView.getText().toString().trim());
                            netManager.getLogionSuccessValue();

                            if (result != null) {
                                dialog.dismiss();

                            }
                            LogUtil.LogShitou(result);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("result", myString);
                            startActivity(intent);

                        } catch (Exception e) {
                            dialog.dismiss();
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


    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}

