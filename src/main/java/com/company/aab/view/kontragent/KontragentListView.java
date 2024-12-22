package com.company.aab.view.kontragent;

import com.company.aab.entity.Kontragent;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "kontragents", layout = MainView.class)
@ViewController("Kontragent.list")
@ViewDescriptor("kontragent-list-view.xml")
@LookupComponent("kontragentsDataGrid")
@DialogMode(width = "64em")
public class KontragentListView extends StandardListView<Kontragent> {
}