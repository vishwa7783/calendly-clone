package com.io.mountblue.calendlyclone.entity;

import biweekly.property.DurationProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable=false)
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User host;

    @Column(name = "description")
    private String description;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "date_range")
    private int dateRange;

    @Column(name="event_type")
    private String eventType;

    @Column(name = "event_link", unique = true)
    private  String eventLink;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Availability> availableHoursByDays;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL})
    private List<Meet> meets;

    public Event() {
    }

    public Event(String title, User host, String description, int duration, String platform, int dateRange, String eventLink) {
        this.title = title;
        this.host = host;
        this.description = description;
        this.duration = duration;
        this.platform = platform;
        this.dateRange = dateRange;
        this.eventLink = eventLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getDateRange() {
        return dateRange;
    }

    public void setDateRange(int dateRange) {
        this.dateRange = dateRange;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<Availability> getAvailableHoursByDays() {
        return availableHoursByDays;
    }

    public void setAvailableHoursByDays(List<Availability> availableHoursByDays) {
        this.availableHoursByDays = availableHoursByDays;
    }

    public List<Meet> getMeets() {
        return meets;
    }

    public void setMeets(List<Meet> meets) {
        this.meets = meets;
    }

    public void addAvailability(Availability availability){
        if(availableHoursByDays == null){
            availableHoursByDays = new ArrayList<>();
        }
        availableHoursByDays.add(availability);
    }

    public void addMeet(Meet meet){
        if(meets == null){
            meets = new ArrayList<>();
        }
        meets.add(meet);
    }
}
