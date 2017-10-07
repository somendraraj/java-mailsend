package com.jio.mailsend.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jio.mailsend.common.Global;

public class MailHandler {

	private static final Logger log = LoggerFactory.getLogger(MailHandler.class);

	public void sendEmailWithAttachments(ArrayList<String> message)
			throws AddressException, MessagingException, IOException {
		log.info("***********Send Email With Attachments**************");
		final String regex = "^\\[|\\]+";
		final String MailUsername = Global.MailUsername;
		final String MailPassword = Global.MailPassword;
		final String toEmail = Global.ToMail;
		int smtpAuth = Integer.parseInt(Global.MailAuth);
		String subject = "Test Mail from Somendra!";
		String body = message.toString().replaceAll(regex, "");
		log.info("MailBody" + body);
		body = body + "\n\nRegards,\nSomendra";
		body = body.replace(",", "\n");
		// sets SMTP server properties
		Properties properties = addProperties();

		//properties.setProperty("mail.smtp.ssl.enable", Global.isSSLEnable);
		// creates a new session with an authenticator
		log.info("Create Session");
		Session session = null;
		if (smtpAuth == 1) {
			Authenticator auth = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(MailUsername, MailPassword);
				}
			};
			session = Session.getInstance(properties, auth);
		} else {
			session = Session.getInstance(properties);
		}
		log.info("This is log");
		// creates a new e-mail message
		Message content = setContent(session, subject, toEmail);


		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, "text/plain");

		// creates multi-part and set text as message part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		MimeBodyPart attachPart = null;
		String fileName = Global.fileName;
		log.info("Attaching file : " + fileName);
		attachPart = new MimeBodyPart();
		DataSource source = new FileDataSource(fileName);
		attachPart.setDataHandler(new DataHandler(source));
		attachPart.setFileName(fileName);
		multipart.addBodyPart(attachPart);

		// sets the multi-part as e-mail's content
		content.setContent(multipart);

		// sends the e-mail
		Transport transport = null;
		try {
			transport = session.getTransport("smtp");
			transport.connect(Global.MailHost, MailUsername, MailPassword);
			transport.sendMessage(content, content.getAllRecipients());
			transport.close();
			log.info("*****************EMail  sent successfully!***************");
		} catch (MessagingException e) {
			log.info("***********Unable to send mail!*************");
			e.printStackTrace();
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}

	private Properties addProperties(){
		Properties properties = new Properties();
		properties.put("mail.smtp.host", Global.MailHost);
		properties.put("mail.smtp.socketFactory.port", Global.MailPort);
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.port", Global.MailPort);
		properties.put("mail.smtp.auth", Integer.parseInt(Global.MailAuth));
		properties.put("mail.smtp.starttls.enable", Global.startTlsEnable);
		properties.put("mail.smtp.ssl.enable", Global.isSSLEnable);
		properties.setProperty("mail.debug", "true");
		return properties;
	}

	private Message setContent(Session session, String subject, String toEmail) throws MessagingException, IOException{
		Message content = new MimeMessage(session);
		content.addHeader("Content-type", "text/HTML; charset=UTF-8");
		content.addHeader("format", "flowed");
		content.addHeader("Content-Transfer-Encoding", "8bit");
		content.setFrom(new InternetAddress(Global.FromMail, "Jio Corporate Service"));
		content.setReplyTo(InternetAddress.parse("no-reply@jio.com", false));
		// InternetAddress[] toAddresses = { new InternetAddress(toEmail) };
		content.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		content.setSubject(subject);
		content.setSentDate(new Date());
		return content;
	}

}
