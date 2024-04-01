package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.entity.Event;

import java.util.List;

public interface EventService {
    void save(Event event);

    Event findByEventLink(String eventLink);

    List<Event> findEventsByHostId(int hostId);

    Event findEventById(int eventId);

    void deleteEventById(int eventId);

    Event findEventByEvent(Event event);
}
