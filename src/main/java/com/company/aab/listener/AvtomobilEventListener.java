package com.company.aab.listener;

import com.company.aab.entity.Avtomobil;
import com.company.aab.entity.Zayavka;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jmix.core.EntityImportExport;
import io.jmix.core.event.EntityLoadingEvent;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class AvtomobilEventListener {
    private final ObjectProvider<SessionData> sessionDataProvider;
    private final EntityImportExport entityImportExport;

    public AvtomobilEventListener(EntityImportExport entityImportExport, ObjectProvider<SessionData> sessionDataProvider) {
        this.entityImportExport = entityImportExport;
        this.sessionDataProvider = sessionDataProvider;
    }

    @EventListener
    public void onAvtomobilSaving(final EntitySavingEvent<Avtomobil> event) {
        SessionData s = sessionDataProvider.stream().iterator().next();

        Object n = s.getAttribute("username");
        if(n!=null&&event.getEntity().getUsername()==null)
            event.getEntity().setUsername(
                    n.toString());

        if(Objects.equals(event.getEntity().getStatus(), Zayavka.VYPOLNENA)){



        }

    }
}