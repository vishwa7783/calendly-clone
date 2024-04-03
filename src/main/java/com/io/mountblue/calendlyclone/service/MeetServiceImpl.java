package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.Repository.MeetRepository;
import com.io.mountblue.calendlyclone.dto.CalenderDto;
import com.io.mountblue.calendlyclone.entity.Event;
import com.io.mountblue.calendlyclone.entity.Meet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MeetServiceImpl implements MeetService {
    MeetRepository meetRepository;

    @Autowired
    public MeetServiceImpl(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    @Override
    public void saveMeet(String name, String email, Event event, CalenderDto calenderDto) {
        Meet meet = new Meet();
        meet.setEvent(event);
        meet.setInviteeName(name);
        meet.setInviteeEmail(email);
        meet.setDescription(event.getDescription());
        meet.setMeetLink("https://nextjs-zegocloud-uikits-sooty-three.vercel.app/");
        meet.setStartTime(LocalTime.from(calenderDto.getEventDateTime()));
        meet.setDate(Date.from(calenderDto.getEventDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        meet.setHost(event.getHost());
        meet.setEndTime(LocalTime.from(calenderDto.getEventDateTime().plusMinutes(event.getDuration())));

        meetRepository.save(meet);
    }

    @Override
    public List<Meet> findMeetsByHostId(int id) {
        return meetRepository.findByHostId(id);
    }
}
