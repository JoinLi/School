package ly.school;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ly.school.bean.SchoolApi;
import ly.school.util.LogUtil;
import ly.school.util.NetManager;
import ly.school.util.StreamTools;
import okhttp3.Call;
import okhttp3.Cookie;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mTextid;
    private EditText mPasswordView, ed_yzm;
    private View mProgressView;
    private View mLoginFormView;
    private ImageView im_yzm;
    private Cookie cookie;
    private NetManager netManager;
    private  Bitmap bm;
    private  String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        initView();
        getCode();


    }

    private void initData() {
        String student_id = mTextid.getText().toString().trim();
        String student_pass = mPasswordView.getText().toString().trim();
        String student_yzm = ed_yzm.getText().toString().trim();
        System.err.println(student_yzm);


//        stringRequestWithPost(student_yzm);
    }

    /**
     * 登录
     */
    private void stringRequestWithPost(final String stu_yzm) {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,
                SchoolApi.SCHOOL_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.err.println(response);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                System.out.println("获取数据出错");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("__VIEWSTATE", "dDw3OTkxMjIwNTU7Oz6bElNpJA3l%2BXqeptxfVrO1JPQhiw%3D%3D");
                map.put("TextBox1", "1305140314");
                map.put("TextBox2", "19941215");
                map.put("TextBox3", stu_yzm);
                map.put("RadioButtonList1", "%D1%A7%C9%FA");
                map.put("Button1", null);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/x-www-form-urlencoded");
                map.put("Connection", "Keep-Alive");
                map.put(" Cache-Control", "no-cache");
                map.put("Cookie", "ASP.NET_SessionId=" + cookie.value());

                return map;
            }
        };
        MyApplication.getApplication().add(stringRequest);
    }


    private void initView() {
//        netManager = NetManager.getNetManager(); //单例模式拿数据
        mTextid = (AutoCompleteTextView) findViewById(R.id.text_id);
        im_yzm = (ImageView) findViewById(R.id.im_yzm);
        ed_yzm = (EditText) findViewById(R.id.ed_yzm);
        im_yzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });
        Button button = (Button) findViewById(R.id.email_sign_in_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                attemptLogin();
                initData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = loginByPost(SchoolApi.SCHOOL_LOGIN_URL, ed_yzm.getText().toString().trim());

                            LogUtil.LogShitou(result);
                        } catch (Exception e) {
                            LogUtil.m("result出错");
//
                        }
                    }
                }).start();
            }
        });
        Button post_button = (Button) findViewById(R.id.get_button);
        post_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = loginByGet("http://222.222.32.17/xscj_gc.aspx?xh=1507140123&xm=赵天&gnmkdm=N121605");

                             value = subString(result);

                            LogUtil.LogShitou( value);
                        } catch (Exception e) {
                            LogUtil.m("result出错");
//
                        }
                    }
                }).start();
            }
        });
        Button post_res_button = (Button) findViewById(R.id.post_res_button);
        post_res_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = loginByPostShuju("http://222.222.32.17/xscj_gc.aspx?xh=1507140123&xm=赵天&gnmkdm=N121605");
                            if (result != null) {
                                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                                intent.putExtra("result", result);
                                startActivity(intent);
                            }

                            LogUtil.LogShitou(result);
                        } catch (Exception e) {
                            LogUtil.m("result出错");
//
                        }
                    }
                }).start();
            }
        });
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void getCode() {
//        OkHttpUtils
//                .get()
//                .url(SchoolApi.SCHOOL_CODE_URL)
//                .build()
//                .execute(new BitmapCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                        LogUtil.m("验证码获取失败");
//                    }
//
//                    @Override
//                    public void onResponse(Bitmap response, int id) {
//                        im_yzm.setImageBitmap(response);
//
//                    }
//
//
//                });
        OkHttpUtils
                .get()
                .url(SchoolApi.SCHOOL_CODE_URL)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {

                        try {
                            InputStream is = response.body().byteStream();
                            bm = BitmapFactory.decodeStream(is);
                            String text = StreamTools.readInputStream(is);
                            LogUtil.m(response.toString());


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);

                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.m("验证码获取失败");
                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
        try {
            CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
            CookieStore cookieStore1 = cookieJar.getCookieStore();
            List<Cookie> cookies1 = cookieStore1.getCookies();
            cookie = cookies1.get(0);
            LogUtil.m("状态值：" + cookie.value());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    im_yzm.setImageBitmap(bm);
                default:
                    break;
            }
        }

    };

    /**
     * 截取参数
     *
     * @return
     */
    private String subString(String html) throws UnsupportedEncodingException {
        int str_start = html.indexOf("dDwxMDE") ;
        String html1=html.substring(str_start);
//        String html2= URLEncoder.encode(html1, "GBK");
        int str_end = html1.indexOf("=");
        String myString = null;
        if (str_start != -1 && str_end != -1) {
            myString = html1.substring(0, str_end);

            } else {
                myString = html1;
            }

        return myString;
    }
//    /**
//     * 从指定URL获取图片
//     *
//     * @param url
//     * @return
//     */
//    private Bitmap getImageBitmap(String url) {
//        Bitmap bitmap = null;
//        try {
//            URL imgUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
//            conn.setDoInput(true);
//            conn.connect();
//            int code = conn.getResponseCode();
//            if (code == 200) {
//                // 请求成功
//                InputStream is = conn.getInputStream();
//                String text = StreamTools.readInputStream(is);
//                LogUtil.m("请求参数：" +text);
//                bitmap = BitmapFactory.decodeStream(is);
//                is.close();
//                return bitmap;
//
//            } else {
//                return null;
//            }
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    public String loginByPost(String path, String yzm) {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(path);
            //利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);
            //设置cookie
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            //设置提交方式
            conn.setRequestMethod("POST");
            // 准备数据
            //String data = "username=" + URLEncoder.encode(username, "UTF-8")+ "&password=" + password;
            String datas = "__VIEWSTATE=dDw3OTkxMjIwNTU7Oz5lckWjxbSRVWnm1fxMfCi4%2BWbxnA%3D%3D&TextBox1=1507140123&TextBox2=z123456&TextBox3=" + yzm + "&RadioButtonList1=%D1%A7%C9%FA&Button1=";
            //协议头
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //conn.setRequestProperty("Content-Length", datas.length() + "");

            // post实际上是浏览器把数据写给了服务器
            conn.setDoOutput(true);//UrlConnection允许向外传数据
            OutputStream os = conn.getOutputStream();
            os.write(datas.getBytes());
            int code = conn.getResponseCode();
            if (code == 200) {
                // 请求成功
                InputStream is = conn.getInputStream();
                String text = StreamTools.readInputStream(is);
                return text;

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String loginByGet(String path) {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(path);
            //利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);
            //设置cookie
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            //设置提交方式
            conn.setRequestMethod("GET");
            //协议头
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            conn.setRequestProperty("Host", "222.222.32.17");
            conn.setRequestProperty("Referer", "http://222.222.32.17/xs_main.aspx?xh=1507140123");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

            int code = conn.getResponseCode();
            if (code == 200) {
                // 请求成功
                InputStream is = conn.getInputStream();
                String text = StreamTools.readInputStream(is);
                return text;

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String loginByPostShuju(String path) {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(path);
            //利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);
            //设置cookie
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            //设置提交方式
            conn.setRequestMethod("POST");
            // 准备数据
            //String data = "username=" + URLEncoder.encode(username, "UTF-8")+ "&password=" + password;
            String datas = "__VIEWSTATE="+ URLEncoder.encode(value, "GBK")+SchoolApi.SCHOOL_RESULT_URL;
            //协议头
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            conn.setRequestProperty("Content-Length", datas.length() + "");
            conn.setRequestProperty("Referer", "http://222.222.32.17/xscj_gc.aspx?xh=1507140123&xm=赵天&gnmkdm=N121605");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            // post实际上是浏览器把数据写给了服务器
            conn.setDoOutput(true);//UrlConnection允许向外传数据
            OutputStream os = conn.getOutputStream();
            os.write(datas.getBytes());
            int code = conn.getResponseCode();
            if (code == 200) {
                // 请求成功
                InputStream is = conn.getInputStream();
                String text = StreamTools.readInputStream(is);
                return text;

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void Logion(String url, String stu_id, String stu_pass, String stu_yzm) {

        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Cookie", "ASP.NET_SessionId=" + cookie.value())
                .addParams("__VIEWSTATE", "dDw3OTkxMjIwNTU7Oz6bElNpJA3l%2BXqeptxfVrO1JPQhiw%3D%3D")
                .addParams("TextBox1", "1305140314")
                .addParams("TextBox2", "19941215")
                .addParams("TextBox3", stu_yzm)
                .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                .addParams("Button1", " ")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.err.println("获取数据失败");
                    }

                    @Override
                    public void onResponse(String string, int id) {
                        try {
                            System.err.println(string);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                });


    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mTextid, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTextid.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mTextid.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mTextid.setError(getString(R.string.error_field_required));
            focusView = mTextid;
            cancel = true;
        }
//        else if (!isEmailValid(email)) {
//            mTextid.setError(getString(R.string.error_invalid_email));
//            focusView = mTextid;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mTextid.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

