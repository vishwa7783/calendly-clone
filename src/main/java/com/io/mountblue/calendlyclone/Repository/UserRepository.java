package com.io.mountblue.calendlyclone.Repository;

import com.io.mountblue.calendlyclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
}
