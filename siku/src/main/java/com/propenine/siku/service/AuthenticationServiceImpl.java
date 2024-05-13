package com.propenine.siku.service;

import com.propenine.siku.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
@SessionScope
public class AuthenticationServiceImpl implements AuthenticationService {
    private final HttpSession httpSession;
    private static final String SESSION_USER_KEY = "loggedInUser";

    public AuthenticationServiceImpl() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        this.httpSession = attr.getRequest().getSession(true);
    }

    @Override
    public void addLoggedInUser(User user) {
        httpSession.setAttribute(SESSION_USER_KEY, user);
    }

    @Override
    public User getLoggedInUser() {
        return (User) httpSession.getAttribute(SESSION_USER_KEY);
    }

    @Override
    public void removeLoggedInUser() {
        httpSession.removeAttribute(SESSION_USER_KEY);
    }
}
