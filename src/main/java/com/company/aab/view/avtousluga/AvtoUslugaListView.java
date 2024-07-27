package com.company.aab.view.avtousluga;

import com.company.aab.entity.AvtoUsluga;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "avtoUslugas", layout = MainView.class)
@ViewController("AvtoUsluga.list")
@ViewDescriptor("avto-usluga-list-view.xml")
@LookupComponent("avtoUslugasDataGrid")
@DialogMode(width = "64em")
public class AvtoUslugaListView extends StandardListView<AvtoUsluga> {
}