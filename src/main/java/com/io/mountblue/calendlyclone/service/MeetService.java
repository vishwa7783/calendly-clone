package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.entity.Meet;

import java.util.List;

public interface MeetService {
    void saveMeet(String name, String email, Event event, CalenderDto calenderDto);

    List<Meet> findMeetsByHostId(int id);
}
