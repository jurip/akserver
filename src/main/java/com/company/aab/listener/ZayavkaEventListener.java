package com.company.aab.listener;

import com.company.aab.app.FcmSender;
import com.company.aab.entity.User;
import com.company.aab.entity.Zayavka;
import io.jmix.core.DataManager;
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
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class ZayavkaEventListener {
    private final ObjectProvider<SessionData> sessionDataProvider;
    private final DataManager dataManager;

    public ZayavkaEventListener(DataManager dataManager,ObjectProvider<SessionData> sessionDataProvider) {
        this.sessionDataProvider = sessionDataProvider;
        this.dataManager = dataManager;
    }

    @EventListener
    public void onZayavkaSaving(final EntitySavingEvent<Zayavka> event) {

        Zayavka z =  event.getEntity();
        if("avtokonnekt".equals(z.getTenantAttribute()))
            return;
        if (z.getUsername() == null) {
            SessionData s = sessionDataProvider.stream().iterator().next();
            Object n = s.getAttribute("username");
            if (n != null) {
                z.setUsername(n.toString());



            }

        }
        if(z.getNachalo()==null){
            z.setNachalo(new Date());
        }
        if(z.getEnd_date_time()==null){
            z.setEnd_date_time(new Date());
        }
        if(z.getStatus()==null){
            z.setStatus("NOVAYA");
        }

        if(z.getUsername()!=null){
            User user = dataManager.load(User.class)
                    .query("select u from User u where u.username = :username")
                    .parameter("username", z.getUsername()).one();


            if(user.getFcmRegistrationToken()!=null){

                FcmSender.sendMessageToApp(user.getFcmRegistrationToken(),z);

            }
        }
    }
}