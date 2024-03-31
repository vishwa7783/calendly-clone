package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.dto.EmailDto;
import com.io.mountblue.calendlyclone.entity.Event;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
//    void sendEmail(String sender,String recipients,String subject);

    void sendCalenderInvite(CalenderDto calenderDto, Event event) throws IOException, MessagingException;

    String getEmailContent(Event event);
}


