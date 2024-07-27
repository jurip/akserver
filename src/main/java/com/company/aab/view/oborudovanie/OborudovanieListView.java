package com.company.aab.view.oborudovanie;

import com.company.aab.entity.Oborudovanie;
import com.company.aab.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "oborudovanies", layout = MainView.class)
@ViewController("Oborudovanie.list")
@ViewDescriptor("oborudovanie-list-view.xml")
@LookupComponent("oborudovaniesDataGrid")
@DialogMode(width = "64em")
public class OborudovanieListView extends StandardListView<Oborudovanie> {
}