package com.company.aab.view.ticket;

import com.company.aab.entity.Ticket;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.core.FileStorage;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "tickets", layout = MainView.class)
@ViewController("Ticket.list")
@ViewDescriptor("ticket-list-view.xml")
@LookupComponent("ticketsDataGrid")
@DialogMode(width = "64em")
public class TicketListView extends StandardListView<Ticket> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private FileStorage fileStorage;


}