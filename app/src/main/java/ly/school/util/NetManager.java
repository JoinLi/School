package ly.school.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import ly.school.bean.SchoolApi;
import ly.school.bean.SchoolBean;
import okhttp3.Call;
import okhttp3.Cookie;

public class NetManager {
    private Cookie cookie;
    private String loginSuccessfulState;
    private String name, zh_id;
    private String viewstate;
    private static NetManager netManager = new NetManager();
    public  SchoolBean bean;

    private NetManager() {

    }

    public static NetManager getNetManager() {
        if (netManager == null) {
            netManager = new NetManager();
        }
        return netManager;
    }

    /**
     * 1 获取验证码以及cookie
     *
     * @param context
     * @param imageView
     * @param view
     */
    public void getCode(Context context, final ImageView imageView, final View view) {
        OkHttpUtils
                .get()
                .url(SchoolApi.SCHOOL_CODE_URL)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        LogUtil.m("验证码获取失败");
                        Snackbar.make(view, "验证码获取失败", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {

                        imageView.setImageBitmap(response);
                    }


                });
        try {
            CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
            CookieStore cookieStore1 = cookieJar.getCookieStore();
            List<Cookie> cookies1 = cookieStore1.getCookies();
            cookie = cookies1.get(0);
            if (cookie == null) {
                Snackbar.make(view, "验证码获取cookie失败", Snackbar.LENGTH_LONG).show();
            }

            LogUtil.m("COOKIE值：" + cookie.value());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * 2 访问主页获取登录的ViewSTATE参数
     */
    public void getIndex() {
        OkHttpUtils
                .get()
                .url(SchoolApi.SCHOOL_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        viewstate = jsoupIndexXml(response);
                        LogUtil.m("获取主页ViewSTATE值" + viewstate);
                    }
                });

    }

    /**
     * 3 登录教务系统
     *
     * @param yzm 验证码
     * @param zh  账号
     * @param mm  密码
     * @return
     */
    public String loginByPost(String yzm, String zh, String mm) {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(SchoolApi.SCHOOL_LOGIN_URL);
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
            String datas = "__VIEWSTATE=" + URLEncoder.encode(viewstate, "GBK") + "&TextBox1=" + zh + "&TextBox2=" + mm + "&TextBox3=" + yzm + "&RadioButtonList1=%D1%A7%C9%FA&Button1=";
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
                name = subName(text);
                zh_id = zh;
                if (name == null) {
                    return null;
                }
                return name;

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 4 获取登录成功后的ViewSTATE参数
     */
    public void getLogionSuccessValue() {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(SchoolApi.SCHOOL_URL + "xscj_gc.aspx?xh=" + zh_id + "&xm=" + name + "&gnmkdm=N121605");
            //利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);
            //设置提交方式
            conn.setRequestMethod("GET");
            //协议头
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            conn.setRequestProperty("Host", "222.222.32.17:81");
            conn.setRequestProperty("Referer", SchoolApi.SCHOOL_URL + "xs_main.aspx?xh=" + zh_id);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

            int code = conn.getResponseCode();
            if (code == 200) {
                // 请求成功
                InputStream is = conn.getInputStream();
                String text = StreamTools.readInputStream(is);
                loginSuccessfulState = LoginSuccessfulXml(text);
                LogUtil.m("获取成功成功的STATE值" + loginSuccessfulState);


            }

        } catch (Exception e) {
            LogUtil.m("获取value出错");
            e.printStackTrace();
        }

    }


    /**
     * 5 查询学生成绩
     *
     * @return
     */
    public String postResult() {
        // 提交数据到服务器
        // 拼装路径
        try {
            URL url = new URL(SchoolApi.SCHOOL_URL + "xscj_gc.aspx?xh=" + zh_id + "&xm=" + name + "&gnmkdm=N121605");
            //利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);

            //设置提交方式
            conn.setRequestMethod("POST");
            // 准备数据
            //String data = "username=" + URLEncoder.encode(username, "UTF-8")+ "&password=" + password;
            String datas = "__VIEWSTATE=" + URLEncoder.encode(loginSuccessfulState, "GBK") + SchoolApi.SCHOOL_RESULT_URL;
            //协议头
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cookie", "ASP.NET_SessionId=" + cookie.value());
            conn.setRequestProperty("Content-Length", datas.length() + "");
            conn.setRequestProperty("Referer", SchoolApi.SCHOOL_URL + "xscj_gc.aspx?xh=" + zh_id + "&xm=" + name + "&gnmkdm=N121605");
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

    /**
     * 获取登录的ViewSTATE参数
     *
     * @return
     */
    private String jsoupIndexXml(String html) {
        Document doc = Jsoup.parse(html);
        String trs = doc.select("input[name=__VIEWSTATE]").val();
        return trs;
    }

    /**
     * 获取登录成功的ViewSTATE参数
     *
     * @param html
     * @return
     */
    private String LoginSuccessfulXml(String html) {
        String loginSuccessXml = jsoupIndexXml(html);
        Document doc = Jsoup.parse(html);
        String xuehao = doc.select("p.search_con").select("span").select("[id=Label3]").text();
        String xingming = doc.select("p.search_con").select("span").select("[id=Label5]").text();
        String yuanxi = doc.select("p.search_con").select("span").select("[id=Label6]").text();//学院：制药工程系
//        String zhuanye = doc.select("p.search_con").select("span").select("[id=Label9]").text(); //专业
        String zhuanyeContext = doc.select("p.search_con").select("span").select("[id=Label7]").text(); //药品生产技术（化学制药方向）
        String xingzheng = doc.select("p.search_con").select("span").select("[id=Label8]").text(); //行政班级
        bean = new SchoolBean(xingming, xuehao, yuanxi, zhuanyeContext, xingzheng);
        LogUtil.e("获取" + xuehao + " " + xingming + "" + yuanxi + "" + "" + zhuanyeContext + "" + xingzheng);
        return loginSuccessXml;
    }

    /**
     * 截取name参数
     *
     * @return
     */
    private String subName(String html) throws UnsupportedEncodingException {
        int str_start = html.indexOf("xhxm");
        int str_end = html.indexOf("同学");
        String myString = null;
        if (str_start != -1 && str_end != -1) {
            myString = html.substring(str_start + 6, str_end);

        } else {
            myString = null;
        }

        return myString;
    }


}
