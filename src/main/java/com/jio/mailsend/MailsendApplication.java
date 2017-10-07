package com.jio.mailsend;
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.jio.mailsend.common.Global;
import com.jio.mailsend.handler.MailHandler;


@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class MailsendApplication {
	private static final Logger log = LoggerFactory.getLogger(MailsendApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MailsendApplication.class, args);
		MailHandler mailHandler = new MailHandler();
		log.info("<<<<<<<<Loading Properties files start>>>>>>");
		Global.loadClass();
		ArrayList<String> messageBody = new ArrayList<String>();
		messageBody.add(Global.message);
		log.info("Message to send:"+messageBody);
		try {
			mailHandler.sendEmailWithAttachments(messageBody);
		} catch (AddressException e) {
			log.error("AddressException");
			e.printStackTrace();
		} catch (MessagingException e) {
			log.error("ERROR2 MessagingException");
			e.printStackTrace(); 
		} catch (IOException e) {
			log.error("ERROR2 IOException");
			e.printStackTrace();
		}
	}
}
