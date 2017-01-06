package ly.school.util;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class NetManager {
	 private static HttpClient client = new DefaultHttpClient();
	 private static String COOKIE = "";
	 private static NetManager netManager = new NetManager();
 
	 private NetManager(){
		 
	 }
	 
	 public static NetManager getNetManager()
	 {
		 if(netManager==null){
			 netManager = new NetManager();
		 }
		 return netManager;
	 }
	 	
		/**
		 * @param path
		 * @param
		 * @param
		 * @return null---->error text--->success
		 */
		public  String loginByPost(String path, String yzm) {
			// 提交数据到服务器
			// 拼装路径
			try {			
				URL url = new URL(path);
				//利用HttpURLConnection对象从网络中获取网页数据
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//设置连接超时
				conn.setConnectTimeout(5000);
				//设置cookie
				conn.setRequestProperty("Cookie","ASP.NET_SessionId=" + COOKIE);
				//设置提交方式
				conn.setRequestMethod("POST");
				// 准备数据
				//String data = "username=" + URLEncoder.encode(username, "UTF-8")+ "&password=" + password;
				String datas="__VIEWSTATE=dDw3OTkxMjIwNTU7Oz51BP9iZRBAJVBQBMDUz%2BsbMgcuEA%3D%3D&TextBox1=1305140314&TextBox2=19941215&TextBox3="+yzm+"&RadioButtonList1=%D1%A7%C9%FA&Button1=";
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


		 
}
