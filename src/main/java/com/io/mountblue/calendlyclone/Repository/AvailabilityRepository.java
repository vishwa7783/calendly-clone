package com.io.mountblue.calendlyclone.Repository;

import com.io.mountblue.calendlyclone.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    List<Availability> findAllByHostId(int id);
}
