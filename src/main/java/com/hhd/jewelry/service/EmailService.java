package com.hhd.jewelry.service;

import com.sendinblue.ApiClient;
import com.sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    @Value("${BREVO_SENDER_NAME}")
    private String senderName;

    public void send(String to, String subject, String text) {
        try {
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            defaultClient.setApiKey(apiKey);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(new SendSmtpEmailSender()
                    .email(senderEmail)
                    .name(senderName));
            email.addToItem(new SendSmtpEmailTo().email(to));
            email.setSubject(subject);
            email.setHtmlContent("<p>" + text + "</p>");

            apiInstance.sendTransacEmail(email);
            System.out.println("✅ Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("❌ Send OTP failed for " + to + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
