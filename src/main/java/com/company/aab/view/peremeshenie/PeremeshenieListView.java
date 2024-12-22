package com.company.aab.view.peremeshenie;

import com.company.aab.entity.Peremeshenie;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "peremeshenies", layout = MainView.class)
@ViewController("Peremeshenie.list")
@ViewDescriptor("peremeshenie-list-view.xml")
@LookupComponent("peremesheniesDataGrid")
@DialogMode(width = "64em")
public class PeremeshenieListView extends StandardListView<Peremeshenie> {
}