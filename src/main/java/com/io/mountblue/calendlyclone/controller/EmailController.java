package com.io.mountblue.calendlyclone.controller;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.service.EmailService;
import com.io.mountblue.calendlyclone.service.EventService;
import com.io.mountblue.calendlyclone.dto.CalenderDto;

import com.io.mountblue.calendlyclone.service.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.io.IOException;
import biweekly.property.Attendee;
import jakarta.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailController {
    private EmailService emailService;

    private EventService eventService;

    private MeetService meetService;

    @Autowired
    public EmailController(EmailService emailService, EventService eventService, MeetService meetService) {
        this.emailService = emailService;
        this.eventService = eventService;
        this.meetService = meetService;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("sender") String sender,
                            @RequestParam("recipients") String recipients,
                            @RequestParam("subject") String subject,Model model) {
        Event event=(Event)model.getAttribute("event");
        emailService.sendEmail(sender,recipients,subject,event);

        return "redirect:/dashboard";
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
                               @RequestParam("email") String email,
                               @RequestParam("name") String name,
                               @PathVariable("eventId") int eventId) throws MessagingException, IOException {
        Event event = eventService.findEventById(eventId);
        List<Attendee> attendees = new ArrayList<>();
        Attendee attendee = new Attendee(name, email);
        attendees.add(attendee);

        meetService.saveMeet(name, email, event, calenderDto);
        calenderDto.setAttendees(attendees);
        emailService.sendCalenderInvite(calenderDto, event);
        return "redirect:/dashboard";
    }
}

