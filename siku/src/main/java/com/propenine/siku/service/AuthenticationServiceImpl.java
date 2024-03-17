package com.propenine.siku.service;

import com.propenine.siku.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private User loggedInUser;

    public void addLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void removeLoggedInUser() {
        this.loggedInUser = null;
    }
}
