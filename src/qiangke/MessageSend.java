package qiangke;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MessageSend {
	public static void main(String[] args) throws MessagingException, IOException{
		MessageSend messageSend=new MessageSend();
		messageSend.sendmail("939275201@qq.com", "java 发送测试邮件", "测试内容");
	}
	public void sendmail(String accept,String title,String content) throws MessagingException, IOException{
		final Properties props = new Properties();
		//在src目录下面，打包成runable jar的时候会把资源放进去
		InputStream is = Object.class.getResourceAsStream("/mail.properties");
		
//		final File input = new File("D:\\jars\\mail.properties");
//		final InputStream is = new FileInputStream(input);
		
		props.load(is);
		
		
//        props.put("mail.smtp.auth", "true");//是否授权
//        props.put("mail.smtp.host", "smtp.163.com");//服务器地址
//        props.put("mail.smtp.port", 25);   //端口号
//        props.put("mail.user", "*****@***.***");//邮箱账号
//        props.put("mail.password", "*****");//登录授权码

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress(accept);
        message.setRecipient(MimeMessage.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");
        // 发送邮件
        Transport.send(message);
	}
}
