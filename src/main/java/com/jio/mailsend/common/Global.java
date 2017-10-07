package com.jio.mailsend.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/*	This class is used to get all the necessary configurations required */
public class Global {

	private static final Logger log = LoggerFactory.getLogger(Global.class);

	private static Properties prop_config;

	public static String message;


	/* Email config */
	public static String MailHost;
	public static String FromMail;
	public static String ToMail;
	public static String MailPort;
	public static String MailAuth;
	public static String MailUsername;
	public static String MailPassword;
	public static String startTlsEnable;
	public static String isSSLEnable;
	public static String fileName;

	public static ApplicationContext ctx = null;

	// When partner wants to make his own configurations then change the below
	// flag(isExternalConfig) to true and provide config.properties file to him.
	private static boolean isExternalConfig = true;

	public static void loadClass() {
		log.info("**********Loading all Properties ***********");
	}

	public static final class HeaderType {
		public static final String CONTENT_TYPE = "Content-Type";
		public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
		public static final String AUTHORIZATION = "Authorization";
	}

	// To invoke partner channel configuration
	static {
		try {
			if (isExternalConfig) {
				log.info("*****External config**********");
				prop_config = new Properties();
				prop_config.load(new FileInputStream("./config.properties"));
			} else {
				log.info("*******Internal config**********");
				prop_config = PropertiesLoaderUtils.loadAllProperties("config.properties");
			}
			MailHost = prop_config.getProperty("MailHost");
			FromMail = prop_config.getProperty("FromMail");
			ToMail = prop_config.getProperty("ToMail");
			MailPort = prop_config.getProperty("MailPort");
			MailAuth = prop_config.getProperty("MailAuth");
			MailUsername = prop_config.getProperty("MailUsername");
			MailPassword = prop_config.getProperty("MailPassword");
			startTlsEnable = prop_config.getProperty("tlsEnable");
			isSSLEnable = prop_config.getProperty("isSSLEnable");
			fileName = prop_config.getProperty("fileName");
			message = prop_config.getProperty("Mail_Body");
		} catch (FileNotFoundException e) {
			log.error("*****FileNotFoundException in loading config file****", e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error("*****Properties exception****", e);
			e.printStackTrace();
		}

	}

}
