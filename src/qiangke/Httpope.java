package qiangke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

public class Httpope {

	public static List<NameValuePair> buildPara(String[] keys, String[] values) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < values.length; i++) {
			params.add(new BasicNameValuePair(keys[i], values[i]));
		}
		return params;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static CookieStore createCookieStore(String[] cookieString, String domain) {
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie stdCookie = new BasicClientCookie(cookieString[0], cookieString[1]);
		stdCookie.setVersion(1);
		stdCookie.setDomain(domain);
		stdCookie.setPath("/");
		// stdCookie.setSecure(true);
		// 精确设置由服务器发送的属性
		stdCookie.setAttribute(ClientCookie.VERSION_ATTR, "1");
		stdCookie.setAttribute(ClientCookie.DOMAIN_ATTR, domain);
		cookieStore.addCookie(stdCookie);
		return cookieStore;
	}

	public static String doGet(CloseableHttpClient httpclient, String uriAPI)
			throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(uriAPI);
		CloseableHttpResponse response1 = httpclient.execute(httpget);
		HttpEntity entity = response1.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			String str = convertStreamToString(instreams);
			httpget.abort();
			return str;
		}
		return "0";
	}

	public static String doPost(CloseableHttpClient httpclient, String uriAPI, List<NameValuePair> params) {
		String result = "";
		HttpPost httpRequst = new HttpPost(uriAPI);// 创建HttpPost对象
		try {
			httpRequst.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
			CloseableHttpResponse response1 = httpclient.execute(httpRequst);
			HttpEntity entity = response1.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				String str = convertStreamToString(instreams);
				result = str;
				httpRequst.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage().toString();
		}
		return result;
	}

	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public Httpope() {
		super();
	}

}