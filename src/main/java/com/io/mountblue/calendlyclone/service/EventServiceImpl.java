package com.io.mountblue.calendlyclone.service;

import biweekly.property.Attendee;
import com.io.mountblue.calendlyclone.Repository.EventRepository;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.entity.Event;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public boolean setAttendees(String names, String mails, CalenderDto calenderDto) {
        String[] namesArray = names.split(",");
        String[] mailsArray = mails.split(",");

        List<String> namesList = new ArrayList<>();
        for (String name : namesArray) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                namesList.add(trimmedName);
            }
        }

        List<String> mailsList = new ArrayList<>();
        for (String mail : mailsArray) {
            String trimmedMail = mail.trim();
            if (!trimmedMail.isEmpty()) {
                mailsList.add(trimmedMail);
            }
        }
        List<Attendee> attendees = new ArrayList<>();
        if(namesList.size() == mailsList.size()) {
            for (int i = 0; i < namesList.size(); i++) {
                String name = namesList.get(i);
                String mail = mailsList.get(i);
                Attendee attendee = new Attendee(name, mail);
                attendees.add(attendee);
            }
            calenderDto.setAttendees(attendees);

            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean checkNumberofMailsAndNames(String names, String mails) {
        String[] namesArray = names.split(",");
        String[] mailsArray = mails.split(",");

        List<String> namesList = new ArrayList<>();
        for (String name : namesArray) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                namesList.add(trimmedName);
            }
        }

        List<String> mailsList = new ArrayList<>();
        for (String mail : mailsArray) {
            String trimmedMail = mail.trim();
            if (!trimmedMail.isEmpty()) {
                mailsList.add(trimmedMail);
            }
        }
        if(mailsList.size()>1 || namesList.size()>1){
            return false;
        }else{
            return true;
        }
    }
}
