package com.propenine.siku.service;

import com.propenine.siku.model.User;
import com.propenine.siku.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(User user){

        userRepository.save(user);
    }

    @Override
    public void editUserProfile(User user){
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsOtherUserWithSameUsername(Long userId, String username) {
    return userRepository.existsByUsernameAndIdNot(username, userId);
    }

    public boolean existsOtherUserWithSameEmail(Long userId, String email) {
        return userRepository.existsByEmailAndIdNot(email, userId);
    }

}
