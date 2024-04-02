package com.io.mountblue.calendlyclone.service;

import biweekly.property.Attendee;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.entity.Event;

import java.util.List;

public interface EventService {
    void save(Event event);

    Event findByEventLink(String eventLink);

    List<Event> findEventsByHostId(int hostId);

    Event findEventById(int eventId);

    void deleteEventById(int eventId);

    Event findEventByEvent(Event event);

    boolean setAttendees(String names, String mails, CalenderDto calenderDto);

    boolean checkNumberofMailsAndNames(String name, String mails);
}
