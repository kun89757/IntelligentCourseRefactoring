package org.kun.intelligentcourse.service;

import org.kun.intelligentcourse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

//    public UserDetailDTO getCurrentUser() {
//
//    }
}
