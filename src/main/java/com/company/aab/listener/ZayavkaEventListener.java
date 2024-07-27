package com.company.aab.listener;

import com.company.aab.entity.Zayavka;
import io.jmix.core.EntityImportExport;
import io.jmix.core.event.EntitySavingEvent;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class ZayavkaEventListener {
    private final ObjectProvider<SessionData> sessionDataProvider;

    public ZayavkaEventListener(ObjectProvider<SessionData> sessionDataProvider) {
        this.sessionDataProvider = sessionDataProvider;
    }

    @EventListener
    public void onZayavkaSaving(final EntitySavingEvent<Zayavka> event) {
        if (event.getEntity().getUsername() == null) {
            SessionData s = sessionDataProvider.stream().iterator().next();
            Object n = s.getAttribute("username");
            if (n != null)
                event.getEntity().setUsername(n.toString());
        }
    }
}