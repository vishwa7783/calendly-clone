package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import biweekly.property.Attendee;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import jakarta.mail.MessagingException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailController {
    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("sender") String sender,
                            @RequestParam("recipients") String recipients,
                            @RequestParam("subject") String subject,Model model) {
        Event event=(Event)model.getAttribute("event");
        emailService.sendEmail(sender,recipients,subject,event);

        return "redirect:/dashboard";
    }

    @GetMapping("/calDtoDetails")
    public String calDetails(Model model){
        CalenderDto calenderDto = new CalenderDto();
        model.addAttribute("calenderDto", calenderDto);
        return "calendar-invite";
    }

    @PostMapping("/sendCalendar")
    public String sendCalendar(@ModelAttribute CalenderDto calenderDto, @ModelAttribute("emails") String emails,Model model) throws MessagingException, IOException {
        List<Attendee> attendees = new ArrayList<>();
        System.out.println(emails);
        System.out.println("\n"+calenderDto.getAttendees());
        Attendee attendee1 = new Attendee("vishwa", "vishwjeet7783@gmail.com");
        Attendee attendee2 = new Attendee("Harsha", "harsha113@gmail.com");
        attendees.add(attendee1);
        attendees.add(attendee2);
        Event event = (Event) model.getAttribute("event");
        calenderDto.setAttendees(attendees);
        emailService.sendCalenderInvite(calenderDto,event);
        return "redirect:/event_types";
    }

}

