package com.io.mountblue.calendlyclone.dto;

import biweekly.property.Attendee;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class CalenderDto {
    private String subject;
    private String description;
    private String summary;
    private String organizer;
    private String meetingLink;
    private LocalDateTime eventDateTime;
    private List<Attendee> attendees;

    public CalenderDto() {
    }

    public CalenderDto(String subject, String description, String summary, String organizer, String meetingLink, LocalDateTime eventDateTime, List<Attendee> attendees) {
        this.subject = subject;
        this.description = description;
        this.summary = summary;
        this.organizer = organizer;
        this.meetingLink = meetingLink;
        this.eventDateTime = eventDateTime;
        this.attendees = attendees;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }
}
