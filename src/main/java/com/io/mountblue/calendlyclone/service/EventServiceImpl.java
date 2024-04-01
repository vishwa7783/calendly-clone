package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.Repository.EventRepository;
import com.io.mountblue.calendlyclone.entity.Event;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Event findByEventLink(String eventLink) {
        return eventRepository.findByEventLink(eventLink);
    }

    @Override
    public List<Event> findEventsByHostId(int hostId) {
        return eventRepository.findByHostId(hostId);
    }

    @Override
    public Event findEventById(int eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public void deleteEventById(int eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public Event findEventByEvent(Event event) {
        return eventRepository.findByTitle(event.getTitle());
    }
}
