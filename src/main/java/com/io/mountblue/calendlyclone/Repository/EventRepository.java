package com.io.mountblue.calendlyclone.Repository;

import com.io.mountblue.calendlyclone.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByEventLink(String eventLink);
    List<Event> findByHostId(int hostId);

    Event findById(int eventId);
}
