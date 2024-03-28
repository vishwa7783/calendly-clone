package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.Repository.UserRepository;
import com.io.mountblue.calendlyclone.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User findUserByEmail(String useremail){

        return userRepository.findUserByEmail(useremail);
    }

    @Override
    @Transactional
    public void save(User user)
    {
        User existingUser = findUserByEmail(user.getEmail());
        if(existingUser == null) {
            String password = user.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            userRepository.save(user);
        }
    }


}
