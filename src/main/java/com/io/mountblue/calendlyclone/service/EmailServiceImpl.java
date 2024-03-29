package com.io.mountblue.calendlyclone.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Attendee;
import biweekly.property.Method;
import biweekly.util.Duration;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.dto.EmailDto;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String sender,String recipients,String subject) {
        String emailContent = getEmailContent();

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(recipients.split(","));
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCalenderInvite(CalenderDto calenderDto) throws IOException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setRecipients(Message.RecipientType.TO, getToAddress(calenderDto.getAttendees()));
        mimeMessage.setSubject(calenderDto.getSubject());
        MimeMultipart mimeMultipart = new MimeMultipart("mixed");
        mimeMultipart.addBodyPart(createCalenderMimeBody(calenderDto));

        String emailContent = getEmailContent();
        MimeBodyPart emailBodyPart = new MimeBodyPart();
        emailBodyPart.setContent(emailContent, "text/html; charset=utf-8");
        mimeMultipart.addBodyPart(emailBodyPart);

        mimeMessage.setContent(mimeMultipart);
        javaMailSender.send(mimeMessage);

    }

    private BodyPart createCalenderMimeBody(CalenderDto calenderDto) throws MessagingException, IOException {
        MimeBodyPart calenderBody = new MimeBodyPart();

        final DataSource source = new ByteArrayDataSource(createCal(calenderDto), "text/calender; charset=UTF-8");
        calenderBody.setDataHandler(new DataHandler(source));
        calenderBody.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");

        return calenderBody;
    }

    private String createCal(CalenderDto calenderDto) {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.REQUEST));
        ical.setUrl(calenderDto.getMeetingLink());

        VEvent event = new VEvent();
        event.setUrl(calenderDto.getMeetingLink());
        event.setSummary(calenderDto.getSummary());
        event.setDescription(calenderDto.getDescription());
        event.setDateStart(getStartDate(calenderDto.getEventDateTime()));
        event.setDuration(new Duration.Builder()
                .hours(1)
                .build());
        event.setOrganizer(calenderDto.getOrganizer());
        addAttendees(event, calenderDto.getAttendees());
        ical.addEvent(event);
        return Biweekly.write(ical).go();
    }

    private Address[] getToAddress(List<Attendee> attendees) {
        return attendees.stream().map(attendee -> {
            Address address = null;
            try {
                address = new InternetAddress(attendee.getEmail());
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return address;
        }).toList().toArray(new Address[0]);
    }

    private void addAttendees(VEvent event, List<Attendee> attendees) {
        for (Attendee attendee : attendees) {
            event.addAttendee(attendee);
        }
    }

    private Date getStartDate(LocalDateTime eventDateTime) {
        Instant instant = eventDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public String getEmailContent() {
        try {
            ClassPathResource resource = new ClassPathResource("templates/check.html");
            byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error loading email content";
        }
    }
}
