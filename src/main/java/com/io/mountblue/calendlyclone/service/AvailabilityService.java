package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.entity.Availability;

import java.util.List;

public interface AvailabilityService {
    void save(Availability availability);

    List<Availability> findAvailabiltyByUserId(int id);
}
