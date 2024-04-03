package com.io.mountblue.calendlyclone.Repository;

import com.io.mountblue.calendlyclone.entity.Meet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetRepository extends JpaRepository<Meet, Integer> {
    List<Meet> findByHostId(int id);
}
