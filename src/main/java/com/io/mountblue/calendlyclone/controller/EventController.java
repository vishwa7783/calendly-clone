package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.entity.Availability;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.entity.User;
import com.io.mountblue.calendlyclone.service.AvailabilityService;
import com.io.mountblue.calendlyclone.service.EventService;
import com.io.mountblue.calendlyclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalTime;
import java.util.*;


@Controller
public class EventController {
    EventService eventService;
    UserService userService;
    AvailabilityService availabilityService;

    @Autowired
    public EventController(EventService eventService, UserService userService, AvailabilityService availabilityService) {
        this.eventService = eventService;
        this.userService = userService;
        this.availabilityService = availabilityService;
    }

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
    public String eventTypeSolo(Model model, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println(userDetails.getUsername());
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

    @GetMapping("/saveEvent")
    public String createEvent(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("event") Event event, Model model)
    {
        String meetingId = UUID.randomUUID().toString();
        User host = userService.findUserByEmail(userDetails.getUsername());
        String eventLink = "https://calendly.com/"+host.getName()+"/"+event.getDuration()+"/"+meetingId;
        event.setEventLink(eventLink);
        event.setHost(host);

        for (Availability availability : event.getAvailableHoursByDays()) {
            availability.setHost(host);
        }

        eventService.save(event);
        Event theEvent = eventService.findByEventLink(eventLink);
        for (Availability availability : theEvent.getAvailableHoursByDays()) {
            availability.setEvent(theEvent);
            availabilityService.save(availability);
        }

        model.addAttribute("event",theEvent);

        return "check";
    }
}
