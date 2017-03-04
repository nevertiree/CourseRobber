package qiangke;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Selectcourse_ip extends Bjfupara {
	// JSESSIONID=FEDFE57444BBA5F43BEAD22DE0C0C68C
	//免验证码地址http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/comeInGgxxkxk
	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws MessagingException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException, MessagingException {
		String ip = "202.204.121.78";// 202.204.121.79
		String extrainfo = null;
		String typeString = "";
		String[] cookieString = null;
		String[] coursesStrings = null;
		MessageSend messageSend = new MessageSend();
		switch (args.length) {
		case 5:
			extrainfo = args[4];
		case 4:
			ip = args[3];
		case 3:
			typeString = args[2];
		case 2:
			coursesStrings = args[1].split(",");
			cookieString = args[0].split("=");
			break;
		case 0:
			System.out
					.println("请输入正确的参数例：java -jar ***.jar cookiestring coursename type ip emailaddress");
			return;
		case 1:
			System.out
					.println("请输入正确的参数例：java -jar ***.jar cookiestring coursename type ip emailaddress");
			return;
		default:
			break;
		}

		CookieStore cookieStore = createCookieStore(cookieString, ip);
		// Set the store
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		// 2016-3-9 不访问的话会报空指针错误
		doGet(httpclient,
				"http://"
						+ ip
						+ "/jsxsd/xsxk/xsxk_index?jx0502zbid=B0EAA093040D499EBF3848AC2C8E4414");
		StringBuilder statisticinfo = new StringBuilder();
		// 创建不同的日期格式
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE");
		statisticinfo.append("开始时间：\n" + df.format(new Date()) + "\n");// 统计信息加入开始时间
		while (true) {
			for (int i = 0; i < coursesStrings.length; i++) {
				System.out.println(coursesStrings[i]);
				String posturl = "http://"
						+ ip
						+ "/jsxsd/xsxkkc/xsxkGgxxkxk?kcxx="
						+ java.net.URLEncoder.encode(java.net.URLEncoder
								.encode(coursesStrings[i], "utf-8"), "utf-8")
						+ "&skls=&skjc=&szjylb=" + typeString + "&skxq=";
				String postdataString = queryInfo(httpclient, posturl);
				Pattern p = Pattern.compile("\"jx0404id\":\"(.+?)\",\"xf\"");// 正则匹配返回的数据中是否含课程id
				Matcher m = p.matcher(postdataString);
				boolean success=false;
				while (m.find()) {
					String geturl = "http://" + ip
							+ "/jsxsd/xsxkkc/ggxxkxkOper?jx0404id="
							+ m.group(1) + "&xkzy=";// 构造直接选课的网址（访问该网址时即选课）
					String resultString = doGet(httpclient, geturl);// 访问，返回服务器结果
					statisticinfo.append(resultString);// 统计信息记录服务器返回结果
					try {
						if (resultString.contains("\"success\":true")) {
							statisticinfo.append("结束时间：\n"
									+ df.format(new Date()) + "\n选到了："
									+ coursesStrings[i]);
							System.out.println(statisticinfo.toString());
							if (extrainfo == null) {
								messageSend
										.sendmail("939275201@qq.com", extrainfo
												+ "已成功选中" + coursesStrings[i],
												statisticinfo.toString()
														+ "\n没有用户邮箱");// 成功时给管理员发邮件
							} else {
								messageSend
										.sendmail("939275201@qq.com", extrainfo
												+ "已成功选中" + coursesStrings[i],
												statisticinfo.toString());// 成功时给管理员发邮件
								messageSend.sendmail(extrainfo, "您已成功选中"
										+ coursesStrings[i],
										statisticinfo.toString());// 成功时给用户发邮件
							}
							return;
						} else {
							messageSend.sendmail("939275201@qq.com", "选课错误提醒",
									statisticinfo.toString() + extrainfo);// 失败时给管理员发邮件
						}
					} catch (Exception e) {
						System.out.println(e.toString());
						if (success) {
							return;
						}
					}
				}
				System.out.println(postdataString);
			}
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}
}