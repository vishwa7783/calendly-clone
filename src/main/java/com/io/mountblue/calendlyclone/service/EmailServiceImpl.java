package com.io.mountblue.calendlyclone.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Attendee;
import biweekly.property.Method;
import biweekly.util.Duration;

import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.entity.Event;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Override
    public void sendEmail(String sender,String recipients,String subject,Event event) {
        String emailContent = getEmailContent(event);
    }

    @Override
    public void sendCalenderInvite(CalenderDto calenderDto, Event event) throws IOException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setRecipients(Message.RecipientType.TO, getToAddress(calenderDto.getAttendees()));
        mimeMessage.setSubject(calenderDto.getSubject());
        MimeMultipart mimeMultipart = new MimeMultipart("mixed");
        mimeMultipart.addBodyPart(createCalenderMimeBody(calenderDto,event));

        String emailContent = getEmailContent(event);
        MimeBodyPart emailBodyPart = new MimeBodyPart();
        emailBodyPart.setContent(emailContent, "text/html; charset=utf-8");
        mimeMultipart.addBodyPart(emailBodyPart);

        mimeMessage.setContent(mimeMultipart);
        javaMailSender.send(mimeMessage);
    }

    private BodyPart createCalenderMimeBody(CalenderDto calenderDto, Event event) throws MessagingException, IOException {
        MimeBodyPart calenderBody = new MimeBodyPart();

        final DataSource source = new ByteArrayDataSource(createCal(calenderDto, event), "text/calender; charset=UTF-8");
        calenderBody.setDataHandler(new DataHandler(source));
        calenderBody.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");

        return calenderBody;
    }

    private String createCal(CalenderDto calenderDto, Event theEvent) {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.REQUEST));
        ical.setUrl(calenderDto.getMeetingLink());  //event meet link

        VEvent event = new VEvent();
        event.setUrl(theEvent.getEventLink());
        event.setSummary(calenderDto.getSummary());
        event.setDescription(theEvent.getDescription());
        event.setDateStart(getStartDate(calenderDto.getEventDateTime()));
        event.setDuration(new Duration.Builder().minutes(theEvent.getDuration()).build());
        event.setOrganizer(theEvent.getHost().getName());

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
    public String getEmailContent(Event event) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("event", event);

        StringWriter stringWriter = new StringWriter();
        templateEngine.process("event-details", thymeleafContext, stringWriter);
        String emailContent = stringWriter.toString();

        return emailContent;
    }
}
