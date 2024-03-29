package com.io.mountblue.calendlyclone.controller;

import biweekly.property.Attendee;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.dto.EmailDto;
import com.io.mountblue.calendlyclone.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailController {
    @Autowired
    EmailService emailService;
    @GetMapping("/mailDtoDetails")
    public String eventDetails(Model model){
        EmailDto emailDto = new EmailDto();
        model.addAttribute("emailDto", emailDto);
        return "event-details";
    }

    @PostMapping("/submitEmail")
    public String sendMail(@ModelAttribute EmailDto emailDto){
        emailService.sendEmail(emailDto);
        return "event-type";
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
        Attendee attendee = new Attendee("harsha", "harsha113kohli@gmail.com");
        attendees.add(attendee);

        calenderDto.setAttendees(attendees);
        emailService.sendCalenderInvite(calenderDto);
        return "redirect:/event_types";
    }
}
