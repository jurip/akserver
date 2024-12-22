package com.company.aab.listener;

import com.company.aab.entity.Ticket;
import com.company.aab.entity.Zayavka;
import io.jmix.core.event.EntitySavingEvent;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TicketEventListener {

    private final ObjectProvider<SessionData> sessionDataProvider;

    public TicketEventListener(ObjectProvider<SessionData> sessionDataProvider) {
        this.sessionDataProvider = sessionDataProvider;
    }

    @EventListener
    public void onTicketSaving(final EntitySavingEvent<Ticket> event) {
        Ticket  z =  event.getEntity();
        if (z.getUsername() == null) {
            SessionData s = sessionDataProvider.stream().iterator().next();
            Object n = s.getAttribute("username");
            if (n != null) {
                z.setUsername(n.toString());



            }

        }

    }
}