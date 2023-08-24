package com.te.flinko.service.sms.employee;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.te.flinko.exception.employee.MessagingException;

@Service
public class SmsService {

	@Value("${sms.api-key}")
	private String apiKey;

	@Value("${sms.send-id}")
	private String sendId;

	@Value("${sms.language}")
	private String language;

	@Value("${sms.route}")
	private String route;

	public Integer sendSms(String message, String number) {
		try {
			message = URLEncoder.encode(message, "UTF-8");

			String myUrl = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey + "&sender_id=" + sendId
					+ "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + number;

			URL url = new URL(myUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("cache-control", "no-cache");
			StringBuilder response = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				response.append(line);
			}
			return con.getResponseCode();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new MessagingException(exception.getMessage());
		}

	}
}
