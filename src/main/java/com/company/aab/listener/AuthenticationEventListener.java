package com.company.aab.listener;

import com.company.aab.security.DatabaseUserRepository;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

@Component
public class AuthenticationEventListener {
    private final ObjectProvider<SessionData> sessionDataProvider;
    private final DatabaseUserRepository databaseUserRepository;

    public AuthenticationEventListener(ObjectProvider<SessionData> sessionDataProvider, DatabaseUserRepository databaseUserRepository) {
        this.sessionDataProvider = sessionDataProvider;
        this.databaseUserRepository = databaseUserRepository;
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(final InteractiveAuthenticationSuccessEvent event) {
        SessionData s = sessionDataProvider.stream().iterator().next();
        s.setAttribute("username", event.getAuthentication().getName());
    }
}