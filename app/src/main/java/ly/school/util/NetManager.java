package ly.school.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import ly.school.bean.SchoolApi;
import okhttp3.Call;
import okhttp3.Cookie;

public class NetManager {
    private Cookie cookie;
    private String value;
    private static NetManager netManager = new NetManager();

    private NetManager() {

    }

    public static NetManager getNetManager() {
        if (netManager == null) {
            netManager = new NetManager();
        }
        return netManager;
    }

    public void getCode(Context context, final ImageView imageView) {
        OkHttpUtils
                .get()
                .url(SchoolApi.SCHOOL_CODE_URL)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        LogUtil.m("验证码获取失败");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {

                        imageView.setImageBitmap(response);
                    }


                });
//        OkHttpUtils
//                .get()
//                .url(SchoolApi.SCHOOL_CODE_URL)
//                .build()
//                .execute(new Callback() {
//                    @Override
//                    public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
//
//                        try {
//                            InputStream is = response.body().byteStream();
//                            bm = BitmapFactory.decodeStream(is);
//                            String text = StreamTools.readInputStream(is);
//                            LogUtil.m(response.toString());
//
//
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        handler.sendEmptyMessage(1);
//
//                        return null;
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        LogUtil.m("验证码获取失败");
//                    }
//
//                    @Override
//                    public void onResponse(Object response, int id) {
//
//                    }
//                });
        try {
            CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
            CookieStore cookieStore1 = cookieJar.getCookieStore();
            List<Cookie> cookies1 = cookieStore1.getCookies();
            cookie = cookies1.get(0);
            LogUtil.m("COOKIE值：" + cookie.value());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


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

    public void loginByGet(String path) {
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
                value = subString(text);
                LogUtil.m(value);


            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
            String datas = "__VIEWSTATE=" + URLEncoder.encode(value, "GBK") + SchoolApi.SCHOOL_RESULT_URL;
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

    /**
     * 截取参数
     *
     * @return
     */
    private String subString(String html) throws UnsupportedEncodingException {
        int str_start = html.indexOf("dDwxMDE");
        String html1 = html.substring(str_start);
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
}
