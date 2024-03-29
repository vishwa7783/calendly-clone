package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.entity.Availability;
import com.io.mountblue.calendlyclone.entity.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class EventController {
    @GetMapping("/event_types")
    public String eventType() {
        return "event-type";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/emailform")
    public String showEmailForm() {
        return "email-form";
    }

    @GetMapping("event_types/solo")
    public String eventTypeSolo(Model model){
        Event event = new Event();
        String[] days={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        List<Availability> availabilities = new ArrayList<>();
        for (String day : days) {
            availabilities.add(new Availability(day, LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        }

        event.setAvailableHoursByDays(availabilities);
        model.addAttribute("event", event);
        model.addAttribute("daysOfWeek", Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

        return "solo-event";
    }


    @GetMapping("event_types/group")
    public String eventTypeGroup(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "group-event";
    }

    @GetMapping("/")
    public String fun(@ModelAttribute("event") Event event,Model model){

//        @AuthenticationPrincipal UserDetails userDetails
//        event.setEventLink("https://calendly.com/"+event.getHost().getName());
        event.setEventLink("https://calendly.com/");
        model.addAttribute("event",event);
        for(Availability A:event.getAvailableHoursByDays()){
            System.out.println(A.getDay());
            System.out.println(A.getStartTime());
            System.out.println(A.getEndTime());
        }
        return "event-type";
    }
}
