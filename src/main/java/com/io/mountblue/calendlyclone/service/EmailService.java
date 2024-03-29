package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.dto.EmailDto;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendEmail(EmailDto emailDto);

    void sendCalenderInvite(CalenderDto calenderDto) throws IOException, MessagingException;
}
