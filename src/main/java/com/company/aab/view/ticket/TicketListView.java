package com.company.aab.view.ticket;

import com.company.aab.entity.Ticket;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "tickets", layout = MainView.class)
@ViewController("Ticket.list")
@ViewDescriptor("ticket-list-view.xml")
@LookupComponent("ticketsDataGrid")
@DialogMode(width = "64em")
public class TicketListView extends StandardListView<Ticket> {
}