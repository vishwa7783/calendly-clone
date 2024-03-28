package com.io.mountblue.calendlyclone.controller;

import com.io.mountblue.calendlyclone.entity.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class EventController {
    @GetMapping("/event_types")
    public String eventType() {
        return "event-type";
    }

    @GetMapping("event_types/solo")
    public String eventTypeSolo(Model model){
        Event event = new Event();

        model.addAttribute("event", event);
        return "solo-event";
    }

    @GetMapping("/")
    public void fun(@ModelAttribute Event event){
        System.out.println(event.getDuration());
        System.out.println(event.getPlatform());
        System.out.println(event.getTitle());
    }
}
