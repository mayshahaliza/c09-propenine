package com.propenine.siku.service;

import com.propenine.siku.model.User;

public interface UserService {

    public void registerUser(User user);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public void editUserProfile(User user);

    public boolean existsOtherUserWithSameUsername(Long userId, String username);

    public boolean existsOtherUserWithSameEmail(Long userId, String email);
}
