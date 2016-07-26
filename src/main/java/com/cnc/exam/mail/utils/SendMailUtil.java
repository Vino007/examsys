package com.cnc.exam.mail.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * 使用apache commons mail开源项目发送邮件示例
 * http://commons.apache.org/proper/commons-email/
 * 
 * @author 郭煜
 */
public class SendMailUtil {

	private static String HOSTNAME = "";
	private static String POP_USERNAME = "";
	private static String USERNAME = ""; // 个人姓名
	private static String POP_PASSWORD = "";
	private static String CODING = "UTF-8";

	static {
		try {
			Configuration config = new PropertiesConfiguration("mail.properties");
			HOSTNAME = config.getString("hostname");
			POP_USERNAME = config.getString("pop_username");
			POP_PASSWORD = config.getString("pop_password");
			USERNAME = config.getString("username");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: simpleEmail
	 * @Description: TODO
	 * @param @param toEmail ：收件人地址
	 * @param @param subject ：主题
	 * @param @param msg ：内容
	 * @return void
	 * @throws
	 */
	public static void simpleEmail(String username,String toEmail, String subject, String msg) {
		SimpleEmail email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setAuthentication(POP_USERNAME, POP_PASSWORD);// 邮件服务器验证：用户名/密码
		email.setCharset(CODING);// 必须放在前面，否则乱码
		try {
			email.addTo(toEmail);
			email.setFrom(POP_USERNAME, username);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 默认使用系统邮箱
	 * @Title: simpleEmail
	 * @Description: TODO
	 * @param @param toEmail ：收件人地址
	 * @param @param subject ：主题
	 * @param @param msg ：内容
	 * @return void
	 * @throws
	 */
	public static void simpleEmail(String toEmail, String subject, String msg) {
		SimpleEmail email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setAuthentication(POP_USERNAME, POP_PASSWORD);// 邮件服务器验证：用户名/密码
		email.setCharset(CODING);// 必须放在前面，否则乱码
		try {
			email.addTo(toEmail);
			email.setFrom(POP_USERNAME, USERNAME);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: multiPartEmail
	 * @Description: TODO
	 * @param @param toEmail ：收件人地址
	 * @param @param subject ：主题
	 * @param @param msg ：内容
	 * @param @param filePath ：附件路径
	 * @param @param fileName ：附件名
	 * @return void
	 * @throws
	 */
	public static void multiPartEmail(String toEmail, String subject,
			String msg, String filePath, String fileName) {
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(HOSTNAME);
		email.setAuthentication(POP_USERNAME, POP_PASSWORD);
		email.setCharset(CODING);
		try {
			email.addTo(toEmail);
			email.setFrom(POP_USERNAME, USERNAME);
			email.setSubject(subject);
			email.setMsg(msg);
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath("d:/student_templet.xls");// 本地文件
			// attachment.setURL(new URL("filePath"));//远程文件filePath
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("答辩学生名单");
			attachment.setName("student_templet.xls");// fileName

			email.attach(attachment);
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: htmlEmail
	 * @Description: TODO
	 * @param @param toEmail ：收件人地址
	 * @param @param subject ：主题
	 * @param @param msg ：内容
	 * @return void
	 * @throws
	 */
	public static void htmlEmail(String toEmail, String subject, String msg) {

		HtmlEmail email = new HtmlEmail();
		email.setHostName(HOSTNAME);
		email.setAuthentication(POP_USERNAME, POP_PASSWORD);
		email.setCharset(CODING);
		try {
			email.addTo(toEmail);
			email.setFrom(POP_USERNAME, USERNAME);
			email.setSubject(subject);
			email.setHtmlMsg("<b>关于论文最后答辩时间</b><br/><div>2013-05-18</div>");
			// email.setHtmlMsg(msg);
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getHOSTNAME() {
		return HOSTNAME;
	}

	public static void setHOSTNAME(String hOSTNAME) {
		HOSTNAME = hOSTNAME;
	}

	public static String getPOP_USERNAME() {
		return POP_USERNAME;
	}

	public static void setPOP_USERNAME(String pOP_USERNAME) {
		POP_USERNAME = pOP_USERNAME;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	public static void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static String getPOP_PASSWORD() {
		return POP_PASSWORD;
	}

	public static void setPOP_PASSWORD(String pOP_PASSWORD) {
		POP_PASSWORD = pOP_PASSWORD;
	}

	public static String getCODING() {
		return CODING;
	}

	public static void setCODING(String cODING) {
		CODING = cODING;
	}
}