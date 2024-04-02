package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.Repository.AvailabilityRepository;
import com.io.mountblue.calendlyclone.entity.Availability;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService{
    AvailabilityRepository availabilityRepository;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Transactional
    @Override
    public void save(Availability availability) {
        availabilityRepository.save(availability);
    }

    @Override
    public List<Availability> findAvailabiltyByUserId(int id) {
        return availabilityRepository.findAllByHostId(id);
    }

    @Override
    public void deleteAvailabilityByEventId(int eventId) {
        availabilityRepository.deleteByEventId(eventId);
    }
}
