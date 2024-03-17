package com.propenine.siku.service;

import com.propenine.siku.model.User;

public interface AuthenticationService {
    public void addLoggedInUser(User user);
    public User getLoggedInUser();
    public void removeLoggedInUser();
}
