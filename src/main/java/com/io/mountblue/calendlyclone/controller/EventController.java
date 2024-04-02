package com.io.mountblue.calendlyclone.controller;
import com.io.mountblue.calendlyclone.entity.Availability;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.entity.User;
import com.io.mountblue.calendlyclone.service.AvailabilityService;
import com.io.mountblue.calendlyclone.service.EventService;
import com.io.mountblue.calendlyclone.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalTime;
import java.util.*;


@Controller
public class EventController {
    @Value("${server.port}")
    private int serverPort;

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
    public String eventTypeSolo(Model model){
        Event event = new Event();
        String[] days={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        List<Availability> availabilities = new ArrayList<>();
        for (String day : days) {
            availabilities.add(new Availability(day, LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        }
        event.setEventType("solo");
        event.setAvailableHoursByDays(availabilities);
        model.addAttribute("event", event);
        model.addAttribute("type", "solo");
        model.addAttribute("daysOfWeek", Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

        return "solo-event";
    }

    @GetMapping("event_types/group")
    public String eventTypeGroup(Model model){
        Event event = new Event();
        String[] days={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        List<Availability> availabilities = new ArrayList<>();
        for (String day : days) {
            availabilities.add(new Availability(day, LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        }
        event.setEventType("group");
        event.setAvailableHoursByDays(availabilities);
        model.addAttribute("event", event);
        model.addAttribute("type", "group");
        model.addAttribute("daysOfWeek", Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));

        return "group-event";
    }

    @GetMapping("/saveEvent")
    public String saveEvent(@AuthenticationPrincipal UserDetails userDetails,
                            @ModelAttribute("event") Event event,
                            @RequestParam("type") String eventType, Model model)
    {
        String meetingId = UUID.randomUUID().toString();
        User host = userService.findUserByEmail(userDetails.getUsername());
        String eventLink = "https://calendly.com/"+host.getName()+"/"+event.getDuration()+"/"+meetingId;
        event.setEventLink(eventLink);
        event.setHost(host);
        event.setEventType(eventType);
        for (Availability availability : event.getAvailableHoursByDays()) {
            availability.setHost(host);
        }
        eventService.save(event);
        Event theEvent = eventService.findByEventLink(eventLink);
        for (Availability availability : theEvent.getAvailableHoursByDays()) {
            availability.setEvent(theEvent);
            availabilityService.save(availability);
        }

        String eventnewlink = "https://calendly-clone-73249cf67193.herokuapp.com/" + serverPort + "/event/" + theEvent.getId() + "/select-date-time?eventId=" + meetingId;
        event.setEventLink(eventnewlink);
        eventService.save(event);
        model.addAttribute("event",theEvent);

        return "event-details";
    }

    @GetMapping("/scheduled_events")
    public String getScheduledEvents(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Event> events = eventService.findEventsByHostId(user.getId());
        model.addAttribute("events", events);

        return "scheduled-events";
    }

    @GetMapping("/event/{eventId}")
    public String seeEventDetails(@PathVariable("eventId") int eventId, Model model){
        Event event = eventService.findEventById(eventId);
        model.addAttribute("event", event);

        return "event-details";
    }

    @PostMapping("/event/delete/{eventId}")
    public String deleteEvent(@PathVariable("eventId") int eventId, @AuthenticationPrincipal UserDetails userDetails,Model model) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Event> events = eventService.findEventsByHostId(user.getId());
        if(!events.isEmpty()){
            model.addAttribute("events",events);
        }
        eventService.deleteEventById(eventId);
        availabilityService.deleteAvailabilityByEventId(eventId);
        return "redirect:/scheduled_events";
    }

    @GetMapping("/event/update/{eventId}")
    public String showUpdateForm(@PathVariable("eventId") int eventId, Model model) {
        Event event = eventService.findEventById(eventId);
        model.addAttribute("event", event);
        return "update-event";
    }

    @PostMapping("/event/update/{eventId}")
    public String updateEvent(@PathVariable("eventId") int eventId, @ModelAttribute("event") Event updatedEvent, Model model) {
        Event event = eventService.findEventById(eventId);
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDuration(updatedEvent.getDuration());
        event.setPlatform(updatedEvent.getPlatform());

        List<Availability> updatedAvailability = updatedEvent.getAvailableHoursByDays();
        for (int i = 0; i < updatedAvailability.size(); i++) {
            Availability availability = event.getAvailableHoursByDays().get(i);
            availability.setStartTime(updatedAvailability.get(i).getStartTime());
            availability.setEndTime(updatedAvailability.get(i).getEndTime());
        }
        eventService.save(event);

        return "redirect:/scheduled_events";
    }

    @GetMapping("/event/{eventId}/select-date-time")
    public String selectDateTime(@PathVariable("eventId") int eventId, Model model){
        Event event = eventService.findEventById(eventId);
        if (event != null ) {
            model.addAttribute("eventId", eventId);
            model.addAttribute("event", event);
            return "select-date-time";
        } else {
            return "invalid-link";
        }
    }

    @GetMapping("/event/schedule-meetings")
    public String scheduleMeeting(@RequestParam("eventId") int eventId,
                                  @RequestParam("selectedTime") String selectedTime,
                                  @RequestParam("year") int year,
                                  @RequestParam("month") String month,
                                  @RequestParam("day") int day,
                                  Model model) {
        String meetingLink = "";
        String phonenumber = "";
        Event event = eventService.findEventById(eventId);

        if (event.getPlatform().equals("phone-call")) {
            phonenumber = "+91 9014512348";
        } else {
            meetingLink = "https://nextjs-zegocloud-uikits-sooty-three.vercel.app/";
        }

        model.addAttribute("meetingLink", meetingLink);
        model.addAttribute("phonenumber", phonenumber);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("selectedTime",selectedTime);

        return "meeting-details";
    }
}
