package com.company.aab.view.usluga;

import com.company.aab.entity.Usluga;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "uslugas", layout = MainView.class)
@ViewController("Usluga.list")
@ViewDescriptor("usluga-list-view.xml")
@LookupComponent("uslugasDataGrid")
@DialogMode(width = "64em")
public class UslugaListView extends StandardListView<Usluga> {
}