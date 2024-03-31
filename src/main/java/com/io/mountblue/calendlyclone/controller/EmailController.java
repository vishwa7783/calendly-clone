package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.service.EmailService;
import com.io.mountblue.calendlyclone.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import biweekly.property.Attendee;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import jakarta.mail.MessagingException;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailController {
    private EmailService emailService;

    private EventService eventService;

    @Autowired
    public EmailController(EmailService emailService, EventService eventService) {
        this.emailService = emailService;
        this.eventService = eventService;
    }

    @GetMapping("/calDtoDetails/{eventId}")
    public String calDetails(Model model, @PathVariable("eventId") int eventId){
        CalenderDto calenderDto = new CalenderDto();

        model.addAttribute("eventId", eventId);
        model.addAttribute("calenderDto", calenderDto);
        return "calendar-invite";
    }

    @PostMapping("/send/{eventId}")
    public String sendCalendar(@ModelAttribute CalenderDto calenderDto,
                               @ModelAttribute("email") String email,
                               @ModelAttribute("name") String name,
                               @PathVariable("eventId") int eventId) throws MessagingException, IOException {
        Event event = eventService.findEventById(eventId);
        List<Attendee> attendees = new ArrayList<>();
        Attendee attendee = new Attendee(name, email);
        attendees.add(attendee);

        calenderDto.setAttendees(attendees);
        emailService.sendCalenderInvite(calenderDto, event);
        return "redirect:/dashboard";
    }
}

