package com.io.mountblue.calendlyclone.Repository;

import com.io.mountblue.calendlyclone.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByEventLink(String eventLink);
}
