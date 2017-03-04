package qiangke;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class NocodeSelectCourse extends Bjfupara {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// http://newjwxt.bjfu.edu.cn/jsxsd/xk/LoginToXk
		CloseableHttpClient httpclient = HttpClients.custom().build();
		String loginurl = "http://newjwxt.bjfu.edu.cn/jsxsd/xk/LoginToXk";
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入学号：");
		String username = scanner.nextLine();
		System.out.println("请输入密码：");
		String password = scanner.nextLine();
		System.out.println("请输入所要选的课程，课程间以逗号分隔：");
		String[] coursesStrings = scanner.nextLine().split(",");
		System.out.println("请输入课程分类（数字）\n1.人文科学\n2.数学与自然科学\n3.社会科学\n4.艺术审美\n5.体育\n6.视频课(本校)\n7.视频课(外校)\n：");
		String typeString = scanner.nextLine();
		System.out.println("接收邮件的邮箱：");
		String extrainfo = scanner.nextLine();
		logIn(httpclient, loginurl, username, password);
		System.out.println(selectterm(httpclient));
		StringBuilder statisticinfo = new StringBuilder();
		MessageSend messageSend = new MessageSend();
		// 创建不同的日期格式
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE");
		statisticinfo.append("开始时间：\n" + df.format(new Date()) + "\n");// 统计信息加入开始时间
		Pattern p = Pattern.compile("\"jx0404id\":\"(.+?)\",\"xf\"");// 正则匹配返回的数据中是否含课程id
		while (true) {
			for (int i = 0; i < coursesStrings.length; i++) {
				System.out.println(df.format(new Date()) + "\n" + coursesStrings[i]);
				String posturl = "http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/xsxkGgxxkxk?kcxx="
						+ java.net.URLEncoder.encode(java.net.URLEncoder.encode(coursesStrings[i], "utf-8"), "utf-8")
						+ "&skls=&skjc=&szjylb=" + typeString + "&skxq=";
				String postdataString = queryInfo(httpclient, posturl);// 查询课程剩余信息
				Matcher m = p.matcher(postdataString);
				boolean success = false;
				while (m.find()) {
					String geturl = "http://newjwxt.bjfu.edu.cn/jsxsd/xsxkkc/ggxxkxkOper?jx0404id=" + m.group(1)
							+ "&xkzy=";// 构造直接选课的网址（访问该网址时即选课）
					String resultString = doGet(httpclient, geturl);// 访问，返回服务器结果
					statisticinfo.append(resultString);// 统计信息记录服务器返回结果
					try {
						if (resultString.contains("\"success\":true")) {
							statisticinfo.append("结束时间：\n" + df.format(new Date()) + "\n选到了：" + coursesStrings[i]);
							System.out.println(statisticinfo.toString());
							if (extrainfo == null || checkEmail(extrainfo) == false || extrainfo == "") {
								messageSend.sendmail("939275201@qq.com",
										extrainfo + "学号：" + username + "已成功选中" + coursesStrings[i],
										statisticinfo.toString() + "\n没有用户邮箱");// 成功时给管理员发邮件
							} else {
								messageSend.sendmail("939275201@qq.com",
										extrainfo + "学号：" + username + "已成功选中" + coursesStrings[i],
										statisticinfo.toString());// 成功时给管理员发邮件
								messageSend.sendmail(extrainfo, "您已成功选中" + coursesStrings[i], statisticinfo.toString());// 成功时给用户发邮件
							}
							return;
						} else {
							messageSend.sendmail("939275201@qq.com", "选课错误提醒", statisticinfo.toString() + extrainfo);// 失败时给管理员发邮件
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
}
