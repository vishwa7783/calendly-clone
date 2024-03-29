package com.io.mountblue.calendlyclone.controller;

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
                            @RequestParam("subject") String subject) {
        emailService.sendEmail(sender,recipients,subject);

        return "redirect:/dashboard";
    }

    @GetMapping("/calDtoDetails")
    public String calDetails(Model model){
        CalenderDto calenderDto = new CalenderDto();
        model.addAttribute("calenderDto", calenderDto);
        return "calendar-invite";
    }

    @PostMapping("/sendCalendar")
    public String sendCalendar(@ModelAttribute CalenderDto calenderDto, @ModelAttribute("emails") String emails) throws MessagingException, IOException {
        List<Attendee> attendees = new ArrayList<>();
        System.out.println(emails);
        System.out.println("\n"+calenderDto.getAttendees());
        Attendee attendee = new Attendee("vishwa", "vishwjeet7783@gmail.com");
        attendees.add(attendee);

        calenderDto.setAttendees(attendees);
        emailService.sendCalenderInvite(calenderDto);
        return "redirect:/event_types";
    }
}

