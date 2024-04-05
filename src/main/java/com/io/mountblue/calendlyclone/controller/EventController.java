package com.io.mountblue.calendlyclone.controller;
import com.io.mountblue.calendlyclone.entity.Availability;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.entity.Meet;
import com.io.mountblue.calendlyclone.entity.User;
import com.io.mountblue.calendlyclone.service.AvailabilityService;
import com.io.mountblue.calendlyclone.service.EventService;
import com.io.mountblue.calendlyclone.service.MeetService;
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
import java.util.stream.Collectors;

@Controller
public class EventController {
    @Value("${server.port}")
    private int serverPort;

    EventService eventService;
    UserService userService;
    AvailabilityService availabilityService;

    MeetService meetService;

    @Autowired
    public EventController(EventService eventService, UserService userService,
                           AvailabilityService availabilityService, MeetService meetService) {
        this.eventService = eventService;
        this.userService = userService;
        this.availabilityService = availabilityService;
        this.meetService = meetService;
    }

    @GetMapping("/event_types")
    public String eventType() {
        return "event-type";
    }

    @GetMapping("/")
    public String showDashboard() {
        return "dashboard";
    }
    @GetMapping("/homepage")
    public String showHomepage() {
        return "homepage";
    }

    @GetMapping("/emailform")
    public String showEmailForm() {
        return "email-form";
    }

    @GetMapping("event_types/solo")
    public String eventTypeSolo(Model model){
        Event event = new Event();
        String[] days={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        List<Availability> availabilities = new ArrayList<>();
        for (String day : days) {
            availabilities.add(new Availability(day, LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        }
        event.setEventType("solo");
        event.setAvailableHoursByDays(availabilities);
        model.addAttribute("event", event);
        model.addAttribute("type", "solo");
        model.addAttribute("daysOfWeek", Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

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
        model.addAttribute("daysOfWeek", Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

        return "group-event";
    }

    @GetMapping("/saveEvent")
    public String saveEvent(@AuthenticationPrincipal UserDetails userDetails,
                            @ModelAttribute("event") Event event,
                            @RequestParam("type") String eventType,
                            @RequestParam(value = "checkedDays", required = false) List<String> checkedDays,
                            @RequestParam Map<String, String> requestParams, Model model) {

        String meetingId = UUID.randomUUID().toString();
        User host = userService.findUserByEmail(userDetails.getUsername());
        String eventLink = "https://calendly.com/"+host.getName()+"/"+event.getDuration()+"/"+meetingId;
        event.setEventLink(eventLink);
        event.setHost(host);
        event.setEventType(eventType);

        List<Availability> availabilities = new ArrayList<>();

        if (checkedDays != null) {
            for (String day : checkedDays) {
                String startTime = requestParams.get("startTime-" + day);
                String endTime = requestParams.get("endTime-" + day);
                Availability availability = new Availability();
                availability.setDay(day);
                availability.setStartTime(LocalTime.parse(startTime));
                availability.setEndTime(LocalTime.parse(endTime));
                availability.setHost(host);
                availabilities.add(availability);

            }
        }

        event.setAvailableHoursByDays(availabilities);
        eventService.save(event);
        Event theEvent = eventService.findByEventLink(eventLink);
        for (Availability availability : theEvent.getAvailableHoursByDays()) {
            availability.setEvent(theEvent);
            availabilityService.save(availability);
        }
        String eventNewLink ="event/" + theEvent.getId() + "/select-date-time?eventId=" + meetingId;
        event.setEventLink(eventNewLink);
        eventService.save(event);
        model.addAttribute("event", theEvent);

        return "redirect:/scheduled_events";
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
        List<String> daysOfWeek = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        model.addAttribute("event", event);
        model.addAttribute("daysOfWeek", daysOfWeek);
        return "update-event";
    }
    @PostMapping("/event/update/{eventId}")
    public String updateEvent(@PathVariable("eventId") int eventId, @ModelAttribute("event") Event updatedEvent,@RequestParam("checkedDays") List<String> checkedDays, @RequestParam Map<String, String> requestParams, Model model) {
        Event event = eventService.findEventById(eventId);
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDuration(updatedEvent.getDuration());
        event.setPlatform(updatedEvent.getPlatform());

        Set<String> existingDays = event.getAvailableHoursByDays().stream()
                .map(Availability::getDay)
                .collect(Collectors.toSet());

        for (String day : checkedDays) {
            String startTime = requestParams.get("startTime-" + day);
            String endTime = requestParams.get("endTime-" + day);
            Availability availability = new Availability();
            availability.setDay(day);
            availability.setStartTime(LocalTime.parse(startTime));
            availability.setEndTime(LocalTime.parse(endTime));
            availability.setEvent(event);
            if (existingDays.contains(day)) {
                for (Availability existingAvailability : event.getAvailableHoursByDays()) {
                    if (existingAvailability.getDay().equals(day)) {
                        existingAvailability.setStartTime(LocalTime.parse(startTime));
                        existingAvailability.setEndTime(LocalTime.parse(endTime));
                        break;
                    }
                }
            } else {
                event.getAvailableHoursByDays().add(availability);
            }
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

    @GetMapping("/my_meets")
    public String getMeets(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Meet> meets= meetService.findMeetsByHostId(user.getId());
        model.addAttribute("meets",meets);
        return "scheduled-meets";
    }
}
