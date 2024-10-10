package com.company.aab.view.avtomobil;

import com.company.aab.entity.Avtomobil;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "avtomobils", layout = MainView.class)
@ViewController("Avtomobil.list")
@ViewDescriptor("avtomobil-list-view.xml")
@LookupComponent("avtomobilsDataGrid")
@DialogMode(width = "64em")
public class AvtomobilListView extends StandardListView<Avtomobil> {
}