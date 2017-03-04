package qiangke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;

public class CopyOfTest
{
//JSESSIONID=FEDFE57444BBA5F43BEAD22DE0C0C68C
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException
	{
		// Create a local instance of cookie store
		String[] cookieString=args[0].split("=");
		String[] coursesStrings=args[1].split(",");
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie stdCookie = new BasicClientCookie(cookieString[0], cookieString[1]);
		stdCookie.setVersion(1);
		stdCookie.setDomain("newjwxt.bjfu.edu.cn");
		stdCookie.setPath("/");
		//stdCookie.setSecure(true);
		// 精确设置由服务器发送的属性
		stdCookie.setAttribute(ClientCookie.VERSION_ATTR, "1");
		stdCookie.setAttribute(ClientCookie.DOMAIN_ATTR, "newjwxt.bjfu.edu.cn");
		cookieStore.addCookie(stdCookie);
		// Set the store
		CloseableHttpClient httpclient = HttpClients.custom()
		        .setDefaultCookieStore(cookieStore)
		        .build();
		//doGet(httpclient, "http://newjwxt.bjfu.edu.cn/jsxsd/xsxk/xklc_list?Ves632DSdyV=NEW_XSD_PYGL");
		while(true)
		{
			for(int i=0;i<coursesStrings.length;i++)
			{
				System.out.println(coursesStrings[i]);
				String posturl="http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/xsxkGgxxkxk?kcxx="+java.net.URLEncoder.encode(java.net.URLEncoder.encode(coursesStrings[i],"utf-8"),"utf-8")+"&skls=&skjc=&szjylb=&skxq=";
				String postdataString=doPost(httpclient,posturl);
				Pattern p = Pattern.compile("\"jx0404id\":\"(.+?)\",\"xf\"");
			    Matcher m = p.matcher(postdataString);
			    while(m.find())
			    {
			    	String geturl="http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/ggxxkxkOper?jx0404id="+m.group(1)+"&xkzy=";
			    	String resultString=doGet(httpclient, geturl);
			    	if(resultString.contains("\"success\":true"))
			    	{
			    		System.out.println("选课成功");
			    		return;
			    	}
			    	System.out.println(resultString);
			    }
				System.out.println(postdataString);
			}
		}
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
	
	public static String doGet(CloseableHttpClient httpclient,String uriAPI) throws ClientProtocolException, IOException
    {
		HttpGet httpget = new HttpGet(uriAPI);
		//HttpPost httpPost=new HttpPost("http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/ggxxkxkOper?jx0404id=201520162005082&xkzy=4");
		CloseableHttpResponse response1 = httpclient.execute(httpget);
		HttpEntity entity = response1.getEntity();  
        if (entity != null) {  
            InputStream instreams = entity.getContent();  
            String str = convertStreamToString(instreams);
            System.out.println("Do something"); 
            System.out.println(str); 
            httpget.abort();  
            return str;
        }
        return "0";
    }
	
	
	public static String doPost(CloseableHttpClient httpclient,String uriAPI)
    {
    	String result = "";
    	HttpPost httpRequst = new HttpPost(uriAPI);//创建HttpPost对象
    	
    	List <NameValuePair> params = new ArrayList<NameValuePair>();
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
         data[3] = "0";//开始数据
         data[4] = "15";//长度
         data[5] = "kch,kcmc,xf,skls,sksj,skdd,xxrs,xkrs,syrs,ctsm,szkcflmc,bz,czOper";
         for(int i=0;i<data.length-1;i++)
         {
        	 params.add(new BasicNameValuePair(dataname[i], data[i]));
         }
         String[] data2 = data[5].split(",");
         for (int i = 0; i < data2.length; i++)
         {
        	 params.add(new BasicNameValuePair(dataname[dataname.length - 1]+i, data2[i]));
         }
    	try {
			httpRequst.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));
			CloseableHttpResponse response1 = httpclient.execute(httpRequst);
			HttpEntity entity = response1.getEntity();  
	        if (entity != null) {  
	            InputStream instreams = entity.getContent();  
	            String str = convertStreamToString(instreams);
	            System.out.println("Do something"); 
	            System.out.println(str);
	            result=str;
	            httpRequst.abort();  
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage().toString();
		}
    	return result;
    }
}