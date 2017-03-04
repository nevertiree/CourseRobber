package qiangke;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Bjfupara extends Httpope {

	public static ArrayList<String[]> getPara() {
		String[] data = new String[6];
		String[] dataname = new String[6];
		dataname[0] = "sEcho";
		dataname[1] = "iColumns";
		dataname[2] = "sColumns";
		dataname[3] = "iDisplayStart";
		dataname[4] = "iDisplayLength";
		dataname[5] = "mDataProp_";
		data[0] = "1";
		data[1] = "13";
		data[2] = "";
		data[3] = "0";// 开始数据
		data[4] = "15";// 长度
		data[5] = "kch,kcmc,xf,skls,sksj,skdd,xxrs,xkrs,syrs,ctsm,szkcflmc,bz,czOper";
		ArrayList<String[]> keyandvalue = new ArrayList<String[]>();
		keyandvalue.add(dataname);
		keyandvalue.add(data);
		return keyandvalue;
	}

	public static String queryInfo(CloseableHttpClient httpclient, String posturl) {
		ArrayList<String[]> keyvalue = getPara();
		List<NameValuePair> params = buildPara(keyvalue.get(0), keyvalue.get(1));
		return doPost(httpclient, posturl, params);
	}

	public static List<NameValuePair> buildPara(String[] keys, String[] values) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < values.length - 1; i++) {
			params.add(new BasicNameValuePair(keys[i], values[i]));
		}
		String[] values2 = values[5].split(",");
		for (int i = 0; i < values2.length; i++) {
			params.add(new BasicNameValuePair(keys[keys.length - 1] + i, values2[i]));
		}
		return params;
	}
	public static String logIn(CloseableHttpClient httpClient, String url, String username, String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("USERNAME", username));
		params.add(new BasicNameValuePair("PASSWORD", password));
		return doPost(httpClient, url, params);
	}

	public static String selectterm(CloseableHttpClient httpClient) throws ClientProtocolException, IOException {
		Pattern p = Pattern.compile("jx0502zbid=(.+?)\"");// 正则匹配返回的数据中本学期需要访问的url
		String resultString = doGet(httpClient,
				"http://newjwxt.bjfu.edu.cn/jsxsd/xsxk/xklc_list?Ves632DSdyV=NEW_XSD_PYGL");
		Matcher m = p.matcher(resultString);
		if (m.find()) {
			resultString = doGet(httpClient,
					"http://newjwxt.bjfu.edu.cn/jsxsd/xsxk/xsxk_index?jx0502zbid=" + m.group(1));
		}
		return resultString;
	}

	public Bjfupara() {
		super();
	}

}