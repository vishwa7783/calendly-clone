package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.entity.Event;

public interface EventService {
    void save(Event event);

    Event findByEventLink(String eventLink);
}
