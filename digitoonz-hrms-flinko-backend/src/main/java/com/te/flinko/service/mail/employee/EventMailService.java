package com.te.flinko.service.mail.employee;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.te.flinko.dto.employee.MultipleMailDTO;
import com.te.flinko.exception.employee.MessagingException;

@Service
@ParametersAreNonnullByDefault
public class EventMailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String from;

	
	public Integer sendMultipleMail(MultipleMailDTO multipleMailDto)  {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(multipleMailDto.getSubject());
			helper.setText(multipleMailDto.getBody());
			 Object[] array = multipleMailDto.getTo().toArray();
		    InternetAddress[] addr=	 new InternetAddress[array.length];
		    for (int i = 0; i < array.length; i++) {
			 addr[i]= new InternetAddress((String) array[i]);
				
			}
			helper.setTo(addr);
			javaMailSender.send(message);
			return HttpStatus.OK.value();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MessagingException(e.getMessage());
		}
		
	}
	

}
